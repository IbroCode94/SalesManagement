package org.sales.salesmanagement.Repository;

import org.sales.salesmanagement.models.Customers;
import org.sales.salesmanagement.models.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    List<Sales> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Sales> findByBuyer(Customers buyer);
    List<Sales> findBySeller(Customers seller);
}
