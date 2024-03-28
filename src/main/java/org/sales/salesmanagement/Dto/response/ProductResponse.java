package org.sales.salesmanagement.Dto.response;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductResponse {
    private int quantity;
    private BigDecimal price;
    private String name;
}
