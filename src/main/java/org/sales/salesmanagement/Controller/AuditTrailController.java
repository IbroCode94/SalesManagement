package org.sales.salesmanagement.Controller;

import lombok.RequiredArgsConstructor;
import org.sales.salesmanagement.Dto.dtorequest.AuditTrailDTO;
import org.sales.salesmanagement.service.AuditTrailService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/audit-trail")
@RequiredArgsConstructor
public class AuditTrailController {
    private final AuditTrailService auditTrailService;
    @GetMapping("/all")
    public Page<AuditTrailDTO> getAllUserActions(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return auditTrailService.getAllUserActions(page, size);
    }

}
