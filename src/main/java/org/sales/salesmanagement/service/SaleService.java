package org.sales.salesmanagement.service;

import org.sales.salesmanagement.Dto.dtorequest.ProductBuyRequest;
import org.sales.salesmanagement.Dto.response.GenericResponse;
import org.sales.salesmanagement.Dto.response.SalesResponse;

import java.util.List;

public interface SaleService {
    public GenericResponse createSale(List<ProductBuyRequest> productBuyRequests);
}
