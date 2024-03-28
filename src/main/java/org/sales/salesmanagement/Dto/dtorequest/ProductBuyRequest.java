package org.sales.salesmanagement.Dto.dtorequest;

import lombok.Data;

@Data
public class ProductBuyRequest {
    private Long productId;
    private int quantity;
}
