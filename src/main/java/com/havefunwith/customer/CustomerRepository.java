package com.havefunwith.customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // JPQL constructs customer queries

    boolean existsCustomerByEmail(String email);

    boolean existsCustomerById(Long id);

}
