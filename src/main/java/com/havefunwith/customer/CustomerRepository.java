package com.havefunwith.customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsCustomerByEmail(String email); // JPQL constructs the query

    boolean existsCustomerById(Long id);

}
