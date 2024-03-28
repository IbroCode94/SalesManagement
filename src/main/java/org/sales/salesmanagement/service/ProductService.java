package org.sales.salesmanagement.service;

import org.sales.salesmanagement.Dto.dtorequest.ProductDto;
import org.sales.salesmanagement.Dto.response.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProductService {
    List<ProductResponse> getAllProducts();
    ProductResponse createProduct(ProductDto request);
    ProductResponse updateProduct(Long productId, ProductDto request);
    void deleteProduct(Long productId);
}
