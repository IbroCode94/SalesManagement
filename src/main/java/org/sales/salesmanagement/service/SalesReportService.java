package org.sales.salesmanagement.service;

import org.sales.salesmanagement.Dto.response.SalesReportResponse;

import java.time.LocalDate;

public interface SalesReportService {
    SalesReportResponse generateSalesReport(LocalDate startDate, LocalDate endDate);
}
