package org.sales.salesmanagement.Repository;

import org.sales.salesmanagement.models.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CustomerRepository extends JpaRepository<Customers, Long> {
    @Query(value = "SELECT * FROM customers c WHERE c.email = :input OR c.mobile = :input", nativeQuery = true)
    Optional<Customers> findCustomerByEmailOrMobile(@Param("input") String input);
    Optional<Customers> findById(Long clientId);
    Optional<Customers> findByEmail(String email);
    void deleteById(Long clientId);
    List<Customers> findAll();
}
