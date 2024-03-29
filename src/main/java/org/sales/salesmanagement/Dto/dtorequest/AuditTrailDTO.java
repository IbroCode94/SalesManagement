package org.sales.salesmanagement.Dto.dtorequest;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditTrailDTO {
    private String firstName;
    private String lastName;
    private String action;
    private LocalDateTime timeStamp;
    private String email;
}
