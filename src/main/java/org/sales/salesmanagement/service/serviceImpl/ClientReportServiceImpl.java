package org.sales.salesmanagement.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.sales.salesmanagement.Dto.response.ClientReportResponse;
import org.sales.salesmanagement.Repository.CustomerRepository;
import org.sales.salesmanagement.enums.Roles;
import org.sales.salesmanagement.models.Customers;
import org.sales.salesmanagement.service.ClientReportService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientReportServiceImpl  implements ClientReportService {
    private final CustomerRepository customerRepository;

    @Override
    public ClientReportResponse generateClientReport() {
        List<Customers> allCustomers = customerRepository.findAll();
        ClientReportResponse reportResponse = new ClientReportResponse();
        reportResponse.setTotalClients(allCustomers.size());
        List<Customers> topSpendingClients = allCustomers.stream()
                .filter(customer -> customer.getRoles() != Roles.SELLER) // Filter out sellers
                .sorted(Comparator.comparing(Customers::getTotalSpent).reversed())
                .limit(5)
                .collect(Collectors.toList());

        // Map Customers to a simplified response directly
        List<Map<String, Object>> topSpendingClientsInfo = topSpendingClients.stream()
                .map(customer -> {
                    Map<String, Object> clientInfo = new HashMap<>();
                    clientInfo.put("firstName", customer.getFirstName());
                    clientInfo.put("lastName", customer.getLastName());
                    clientInfo.put("email", customer.getEmail());
                    clientInfo.put("totalSpent", customer.getTotalSpent());
                    clientInfo.put("walletBal", customer.getWalletBal());
                    return clientInfo;
                })
                .collect(Collectors.toList());

        reportResponse.setTopSpendingClients(topSpendingClientsInfo);

        return reportResponse;
    }
    }

