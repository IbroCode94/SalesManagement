package org.sales.salesmanagement.service.serviceImpl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.sales.salesmanagement.Dto.dtorequest.AuditTrailDTO;
import org.sales.salesmanagement.Exceptions.ResourceNotFoundException;
import org.sales.salesmanagement.Repository.AuditTrailRepository;
import org.sales.salesmanagement.Repository.CustomerRepository;
import org.sales.salesmanagement.enums.UserAction;
import org.sales.salesmanagement.models.AuditTrail;
import org.sales.salesmanagement.models.Customers;
import org.sales.salesmanagement.service.AuditTrailService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditTrailImpl implements AuditTrailService {
    private final AuditTrailRepository auditTrailRepository;
    private final HttpServletRequest request;
    private final CustomerRepository userRepository;

    @Override
    public void saveAuditTrail(Customers customers, UserAction actionPerformed, String emailAddress) {
        Optional<Customers> userOptional = userRepository.findById(customers.getId());
        if (userOptional.isPresent()) {
            Customers user = userOptional.get();
            AuditTrail auditTrail = new AuditTrail();

            auditTrail.setFirstName(customers.getFirstName());
            auditTrail.setLastName(customers.getLastName());
            auditTrail.setUserId(user);
            auditTrail.setAction(actionPerformed.getActionName());
            auditTrail.setEmail(emailAddress);

            String ipAddress = getClientIpAddress(request);
            auditTrail.setIpAddress(ipAddress);

            auditTrail.setTimeStamp(LocalDateTime.now());
            auditTrailRepository.save(auditTrail);
        } else {
            throw new ResourceNotFoundException("User Not Found");
        }
    }

    @Override
    public Page<AuditTrailDTO> getAllUserActions(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<AuditTrail> auditTrailPage = auditTrailRepository.findAll(pageable);

        return auditTrailPage.map(auditTrail -> {
            AuditTrailDTO dto = new AuditTrailDTO();
            BeanUtils.copyProperties(auditTrail, dto);
            return dto;
        });
    }

    private String getClientIpAddress(HttpServletRequest request) {
        return request.getRemoteAddr();
    }
}
