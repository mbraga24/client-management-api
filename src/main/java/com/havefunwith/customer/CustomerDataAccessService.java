package com.havefunwith.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerDataAccessService implements CustomerDAO {

    private static List<Customer> customers;

    static {
        customers = new ArrayList<>();

        Customer john = new Customer((long) 1.0, "John", 24, "john@email.com");
        Customer keyla = new Customer((long) 2.0, "Keyla", 28, "keila@email.com");

        customers.add(john);
        customers.add(keyla);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

}
