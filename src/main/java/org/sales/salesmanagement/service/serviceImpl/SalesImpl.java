package org.sales.salesmanagement.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.sales.salesmanagement.Dto.dtorequest.ProductBuyRequest;
import org.sales.salesmanagement.Dto.response.GenericResponse;
import org.sales.salesmanagement.Dto.response.SalesResponse;
import org.sales.salesmanagement.Exceptions.ResourceNotFoundException;
import org.sales.salesmanagement.Repository.CustomerRepository;
import org.sales.salesmanagement.Repository.ProductRepository;
import org.sales.salesmanagement.Repository.SalesRepository;
import org.sales.salesmanagement.enums.Roles;
import org.sales.salesmanagement.enums.UserAction;
import org.sales.salesmanagement.models.Customers;
import org.sales.salesmanagement.models.Product;
import org.sales.salesmanagement.models.Sales;
import org.sales.salesmanagement.models.Transaction;
import org.sales.salesmanagement.service.AuditTrailService;
import org.sales.salesmanagement.service.SaleService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesImpl implements SaleService {
    private final SalesRepository salesRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final AuditTrailService auditTrailService;

// TODO this method is sale( where a user buys a product)
    @Override
    @Transactional
    @SneakyThrows
    public GenericResponse createSale(List<ProductBuyRequest> productBuyRequests) {
        Customers customers = getCurrentUser();
        BigDecimal totalAmount = calculateTotalAmount(productBuyRequests);
        log.info("Total amount of items purchased by user: {}", totalAmount);

        if (customers.getRoles() == Roles.SELLER) {
            throw new ResourceNotFoundException("Sellers cannot make purchases. Please sign up as a buyer or customer.");
        }
        validateClientBalance(customers, totalAmount);
        BigDecimal initialWalletBalance = customers.getWalletBal();

        customers.setWalletBal(customers.getWalletBal().subtract(totalAmount));

        BigDecimal totalSpent = customers.getTotalSpent().add(totalAmount);
        log.info("Initial wallet balance: {}", initialWalletBalance);
        log.info("Wallet balance after purchase: {}", customers.getWalletBal());
        customers.setTotalSpent(totalSpent);
        customerRepository.save(customers);

        Sales sales = new Sales();
        sales.setBuyer(customers);
        sales.setCreatedAt(LocalDateTime.now());
        List<Transaction> transactions = new ArrayList<>();
        for (ProductBuyRequest request : productBuyRequests) {
            Optional<Product> optionalProduct = productRepository.findById(request.getProductId());
            if (optionalProduct.isEmpty()) {
                throw new ResourceNotFoundException("Product with ID " + request.getProductId() + " not found");
            }

            Product product = optionalProduct.get();
            if (product.getQuantity() < request.getQuantity()) {
                throw new ResourceNotFoundException("Product " + product.getName() + " is out of stock");
            }
            Customers seller = product.getCreatedBy();
            sales.setSeller(seller);

            Transaction transaction = new Transaction();
            transaction.setProduct(product);
            transaction.setQuantity(request.getQuantity());
            BigDecimal pricePerUnit = product.getPrice();
            BigDecimal quantity = BigDecimal.valueOf(request.getQuantity());
            BigDecimal totalPrice = pricePerUnit.multiply(quantity);
            transaction.setPrice(totalPrice);
            transaction.setSales(sales);
            transactions.add(transaction);

            BigDecimal totalPriceDecimal = totalPrice;
            updateSellerWalletBalance(seller, totalPriceDecimal);
            product.setQuantity(product.getQuantity() - request.getQuantity());
        }

        sales.setTransactions(transactions);
        sales.setTotalAmount(totalAmount);
        salesRepository.save(sales);
        log.info("Total amount of items purchased by {}: {}", customers.getEmail(), totalAmount);
        log.info("Wallet balance after purchase for {}: {}", customers.getEmail(), customers.getWalletBal());

        GenericResponse response = new GenericResponse();
        response.setStatus("Success");
        response.setMessage("Sale created successfully");
        return response;
    }

    @Override
    public List<SalesResponse> getAllSales() {
        List<Sales> allSales = salesRepository.findAll();
        List<SalesResponse> salesResponses = new ArrayList<>();

        for (Sales sale : allSales) {
            SalesResponse response = new SalesResponse();
            response.setId(sale.getId());
            response.setCreatedAt(sale.getCreatedAt());
            response.setClient(sale.getBuyer().getFirstName());
            response.setSeller(sale.getSeller().getFirstName());
            response.setTotal(sale.getTotalAmount());
            salesResponses.add(response);
        }

        return salesResponses;
    }

    private void updateSellerWalletBalance(Customers seller, BigDecimal amount) {
        BigDecimal currentBalance = seller.getWalletBal();
        seller.setWalletBal(currentBalance.add(amount));
        customerRepository.save(seller);
    }

    private BigDecimal calculateTotalAmount(List<ProductBuyRequest> productBuyRequests) {
        BigDecimal total = BigDecimal.ZERO;
        for (ProductBuyRequest request : productBuyRequests) {
            Optional<Product> optionalProduct = productRepository.findById(request.getProductId());
            if (optionalProduct.isEmpty()) {
                throw new ResourceNotFoundException("Product with ID " + request.getProductId() + " not found");
            }

            Product product = optionalProduct.get();
            BigDecimal pricePerUnit = product.getPrice();
            BigDecimal quantity = BigDecimal.valueOf(request.getQuantity());
            BigDecimal totalPrice = pricePerUnit.multiply(quantity);
            total = total.add(totalPrice);
        }
        return total;
    }

    private void validateClientBalance(Customers customers, BigDecimal totalAmount) {
        if (customers.getWalletBal().compareTo(totalAmount) < 0) {
            throw new ResourceNotFoundException("Insufficient balance");
        }
    }

    private Customers getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Customers) {
            return (Customers) authentication.getPrincipal();
        }
        return null;
    }

    @Override
    @Transactional
    public GenericResponse deleteSale(Long saleId) {
        Optional<Sales> optionalSales = salesRepository.findById(saleId);
        if (optionalSales.isEmpty()) {
            throw new ResourceNotFoundException("Sale with ID " + saleId + " not found");
        }

        Sales sale = optionalSales.get();
        salesRepository.delete(sale);

        GenericResponse response = new GenericResponse();
        response.setStatus("Success");
        response.setMessage("Sale deleted successfully");
        return response;
    }
}