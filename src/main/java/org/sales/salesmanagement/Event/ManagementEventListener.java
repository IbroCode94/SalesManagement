package org.sales.salesmanagement.Event;

import lombok.RequiredArgsConstructor;
import org.sales.salesmanagement.enums.UserAction;
import org.sales.salesmanagement.models.Customers;
import org.sales.salesmanagement.service.AuditTrailService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ManagementEventListener implements ApplicationListener<ManageEvent> {
    private final AuditTrailService auditTrailService;
    @Override
    public void onApplicationEvent(ManageEvent event) {
        Customers customers = event.getCustomers();
        UserAction action = event.getUserAction();
        auditTrailService.saveAuditTrail(customers,action, customers.getUsername());
    }
}
