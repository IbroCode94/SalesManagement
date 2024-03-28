package org.sales.salesmanagement.Dto.dtorequest;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductDto {
    private String name;
    private String description;
    private String categoryName;
    private int quantity;
    private BigDecimal price;
}
