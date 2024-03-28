package org.sales.salesmanagement.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sale_item")
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private Customers buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Customers seller;

    @OneToMany(mappedBy = "sales", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
