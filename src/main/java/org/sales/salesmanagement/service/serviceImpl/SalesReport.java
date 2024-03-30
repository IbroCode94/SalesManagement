package org.sales.salesmanagement.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.sales.salesmanagement.Dto.response.SalesReportResponse;
import org.sales.salesmanagement.Repository.SalesRepository;
import org.sales.salesmanagement.models.Product;
import org.sales.salesmanagement.models.Sales;
import org.sales.salesmanagement.models.Transaction;
import org.sales.salesmanagement.service.SalesReportService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesReport implements SalesReportService {
    private final SalesRepository salesRepository;
    //TODO Generate a sales report for a specific date range, including the total number of sales, total
   // TODO revenue, top-selling products, and top-performing sellers.
    @Override
    public SalesReportResponse generateSalesReport(LocalDate startDate, LocalDate endDate) {
        List<Sales> salesInRange = salesRepository.findByCreatedAtBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());

        SalesReportResponse reportResponse = new SalesReportResponse();
        reportResponse.setStartDate(startDate);
        reportResponse.setEndDate(endDate);
        reportResponse.setTotalSales(salesInRange.size());

        BigDecimal totalRevenue = BigDecimal.ZERO;
        for (Sales sale : salesInRange) {
            totalRevenue = totalRevenue.add(sale.getTotalAmount());
        }
        reportResponse.setTotalRevenue(totalRevenue);

        Map<Product, Integer> productSalesMap = new HashMap<>();
        for (Sales sale : salesInRange) {
            for (Transaction transaction : sale.getTransactions()) {
                Product product = transaction.getProduct();
                int quantity = transaction.getQuantity();
                productSalesMap.put(product, productSalesMap.getOrDefault(product, 0) + quantity);
            }
        }
        List<Map.Entry<Product, Integer>> sortedProductSalesList = productSalesMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());
        reportResponse.setTopSellingProducts(sortedProductSalesList.stream()
                .limit(5) // Get top 5 selling products
                .map(entry -> entry.getKey().getName() + " (" + entry.getValue() + " units)")
                .collect(Collectors.toList()));

        Map<String, BigDecimal> sellerRevenueMap = new HashMap<>();
        for (Sales sale : salesInRange) {
            String sellerName = sale.getSeller().getFirstName() + " " + sale.getSeller().getLastName();
            BigDecimal saleAmount = sale.getTotalAmount();
            sellerRevenueMap.put(sellerName, sellerRevenueMap.getOrDefault(sellerName, BigDecimal.ZERO).add(saleAmount));
        }
        List<Map.Entry<String, BigDecimal>> sortedSellerRevenueList = sellerRevenueMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());
        reportResponse.setTopPerformingSellers(sortedSellerRevenueList.stream()
                .limit(5) // Get top 5 performing sellers
                .map(entry -> entry.getKey() + " ($" + entry.getValue() + ")")
                .collect(Collectors.toList()));

        return reportResponse;
    }
    }

