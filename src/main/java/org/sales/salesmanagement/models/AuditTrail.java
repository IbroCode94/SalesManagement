package org.sales.salesmanagement.models;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "audit_trail")

public class AuditTrail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Customers userId;

    @NonNull
    @Column(name = "first_name")
    private String firstName;

    @NonNull
    @Column(name = "last_name")
    private String lastName;

    @NonNull
    @Column(name = "action")
    private String action;

    @NonNull
    @Column(name = "ip_address")
    private String ipAddress;

    @NonNull
    @Column(name = "time_stamp")
    private LocalDateTime timeStamp;

    @NonNull
    @Column(name = "email") // Specify the column name for the email field
    private String email;
}
