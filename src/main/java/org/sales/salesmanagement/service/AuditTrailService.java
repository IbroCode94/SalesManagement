package org.sales.salesmanagement.service;


import org.sales.salesmanagement.Dto.dtorequest.AuditTrailDTO;
import org.sales.salesmanagement.enums.UserAction;
import org.sales.salesmanagement.models.Customers;
import org.springframework.data.domain.Page;

public interface AuditTrailService {
    void saveAuditTrail(Customers customers, UserAction actionPerformed, String emailAddress);
    Page<AuditTrailDTO> getAllUserActions(int pageNumber, int pageSize);
}
