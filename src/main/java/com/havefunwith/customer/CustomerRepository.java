package com.havefunwith.customer;

import org.springframework.data.jpa.repository.JpaRepository;

/*
    Data Access Layer or DAO (Data Access Object) Layer
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // *JPQL constructs customer queries.*
    boolean existsCustomerByEmail(String email);
    boolean existsCustomerById(Long id);
}
