package org.sales.salesmanagement.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sales.salesmanagement.Dto.dtorequest.ProductBuyRequest;
import org.sales.salesmanagement.Dto.response.GenericResponse;
import org.sales.salesmanagement.Dto.response.SalesResponse;
import org.sales.salesmanagement.service.SaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
@Slf4j
public class SaleController {
    private final SaleService service;
    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createSale(@Valid @RequestBody List<ProductBuyRequest> productBuyRequests) {
        GenericResponse salesResponse = service.createSale(productBuyRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(salesResponse);
    }
    @DeleteMapping("/sales/{saleId}")
    public ResponseEntity<GenericResponse> deleteSale(@PathVariable Long saleId) {
        GenericResponse response = service.deleteSale(saleId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/all")
    public List<SalesResponse> getAllSales() {
        return service.getAllSales();
    }
}
