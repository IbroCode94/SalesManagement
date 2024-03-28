package org.sales.salesmanagement.Dto.response;

import lombok.Data;
import org.sales.salesmanagement.models.Product;

import java.time.LocalDateTime;

@Data
public class SalesResponse {
    private Long id;
    private String status;
    private LocalDateTime createdAt;
    private String message;
    private Product productSold;
    private String client;
    private String seller;
    private int quantitySold;
    private double total;
}
