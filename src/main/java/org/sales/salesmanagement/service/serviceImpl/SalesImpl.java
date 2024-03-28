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
import org.sales.salesmanagement.models.Customers;
import org.sales.salesmanagement.models.Product;
import org.sales.salesmanagement.models.Sales;
import org.sales.salesmanagement.models.Transaction;
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

    @Override
    @Transactional
    @SneakyThrows
    public GenericResponse createSale(List<ProductBuyRequest> productBuyRequests) {
        Customers customers = getCurrentUser();
        double totalAmount = calculateTotalAmount(productBuyRequests);
        validateClientBalance(customers, totalAmount);

        customers.setWalletBal(customers.getWalletBal() - totalAmount);
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

            product.setQuantity(product.getQuantity() - request.getQuantity());
        }

        sales.setTransactions(transactions);
        sales.setTotalAmount(totalAmount);
        customers.setWalletBal(customers.getWalletBal() - totalAmount);
        customerRepository.save(customers);
        salesRepository.save(sales);

        GenericResponse response = new GenericResponse();
        response.setStatus("Success");
        response.setMessage("Sale created successfully");



        return response;
    }

    private double calculateTotalAmount(List<ProductBuyRequest> productBuyRequests) {
        BigDecimal total = BigDecimal.ZERO;
        for (ProductBuyRequest request : productBuyRequests) {
            Optional<Product> optionalProduct = productRepository.findById(request.getProductId());
            if (optionalProduct.isEmpty()) {
                throw new ResourceNotFoundException("Product with name " + request.getProductId() + " not found");
            }

            Product product = optionalProduct.get();
            BigDecimal pricePerUnit = product.getPrice();
            BigDecimal quantity = BigDecimal.valueOf(request.getQuantity());
            BigDecimal totalPrice = pricePerUnit.multiply(quantity);
            total = total.add(totalPrice);
        }
        return total.doubleValue();
    }

    private void validateClientBalance(Customers customers, double totalAmount) {
        if (customers.getWalletBal() < totalAmount) {
            throw new ResourceNotFoundException("Insufficient balance");
        }
    }

    private Customers getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Customers) {
            return (Customers) authentication.getPrincipal();
        }
        // Handle case when user is not authenticated or is not an instance of Customers
        return null; // Or throw an exception
    }
}