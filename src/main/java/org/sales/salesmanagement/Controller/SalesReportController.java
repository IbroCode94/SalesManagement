package org.sales.salesmanagement.Controller;

import lombok.RequiredArgsConstructor;
import org.sales.salesmanagement.Dto.response.SalesReportResponse;
import org.sales.salesmanagement.service.SalesReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class SalesReportController {
    private final SalesReportService service;
    @GetMapping("/sales")
    public ResponseEntity<SalesReportResponse> generateSalesReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        SalesReportResponse reportResponse = service.generateSalesReport(startDate, endDate);
        return new ResponseEntity<>(reportResponse, HttpStatus.OK);
    }
}
