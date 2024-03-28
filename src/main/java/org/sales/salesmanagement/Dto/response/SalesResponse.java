package org.sales.salesmanagement.Dto.response;

import lombok.Data;
import org.sales.salesmanagement.models.Product;

@Data
public class SalesResponse {
    private String status;
    private String message;
    private Product productSold;
    private int quantitySold;
    private double amount;
}
