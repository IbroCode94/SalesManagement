package org.sales.salesmanagement.Dto.dtorequest;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductBuyRequest {
    private Long productId;
    private int quantity;
    private double price;
}
