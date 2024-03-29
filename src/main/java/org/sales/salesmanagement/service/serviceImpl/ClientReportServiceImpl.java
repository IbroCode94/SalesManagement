package org.sales.salesmanagement.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.sales.salesmanagement.Dto.response.ClientReportResponse;
import org.sales.salesmanagement.Repository.CustomerRepository;
import org.sales.salesmanagement.models.Customers;
import org.sales.salesmanagement.service.ClientReportService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
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
                .sorted(Comparator.comparing(Customers::getTotalSpent).reversed())
                .limit(5)
                .collect(Collectors.toList());
        reportResponse.setTopSpendingClients(topSpendingClients);

        return reportResponse;
    }
}

