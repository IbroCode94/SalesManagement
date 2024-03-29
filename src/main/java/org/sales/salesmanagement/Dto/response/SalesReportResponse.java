package org.sales.salesmanagement.Dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class SalesReportResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private int totalSales;
    private BigDecimal totalRevenue;
    private List<String> topSellingProducts;
    private List<String> topPerformingSellers;
}
