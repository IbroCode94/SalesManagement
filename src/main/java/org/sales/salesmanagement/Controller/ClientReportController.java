package org.sales.salesmanagement.Controller;

import lombok.RequiredArgsConstructor;
import org.sales.salesmanagement.Dto.response.ClientReportResponse;
import org.sales.salesmanagement.service.ClientReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clientReport")
@RequiredArgsConstructor
public class ClientReportController {
    private final ClientReportService clientReportService;
    @GetMapping("/clients")
    public ResponseEntity<ClientReportResponse> generateClientReport() {
        ClientReportResponse reportResponse = clientReportService.generateClientReport();
        return ResponseEntity.ok(reportResponse);
    }
}
