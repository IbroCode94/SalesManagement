package org.sales.salesmanagement.Dto.response;

import lombok.Data;
import org.sales.salesmanagement.models.Customers;

import java.util.List;

@Data
public class ClientReportResponse {
    private int totalClients;
    private List<Customers> topSpendingClients;
    private List<String> clientActivity;
}
