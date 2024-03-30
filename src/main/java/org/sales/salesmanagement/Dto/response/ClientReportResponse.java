package org.sales.salesmanagement.Dto.response;

import lombok.Data;
import org.sales.salesmanagement.models.Customers;

import java.util.List;
import java.util.Map;

@Data
public class ClientReportResponse {
    private int totalClients;
    private List<Map<String, Object>> topSpendingClients;
    private List<String> clientActivity;
}
