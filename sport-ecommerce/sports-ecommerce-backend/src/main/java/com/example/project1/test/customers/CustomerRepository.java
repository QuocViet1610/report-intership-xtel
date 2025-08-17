package com.example.project1.test.customers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    @Query(value = "SELECT * FROM customers ", nativeQuery = true)
    List<Customer> getNative();

    @Query(value = """
    SELECT 
        `Index`,
        customer_id,
        first_name,
        last_name,
        company,
        city,
        country,
        phone1,
        phone2,
        email,
        subscription_date,
        website
    FROM customers
    """, nativeQuery = true)
    List<Object[]> getRawCustomerData();


    Page<Customer> findAll(Pageable pageable);

    @Query(
            value = "SELECT * FROM customers",
            countQuery = "SELECT COUNT(*) FROM customers",
            nativeQuery = true
    )
    Page<Customer> findAllNative(Pageable pageable);

    @Query(value = "SELECT * FROM customers WHERE email = :email", nativeQuery = true)
    Optional<Customer> findByEmail(@Param("email") String email);
}
