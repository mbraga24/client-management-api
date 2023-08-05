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

        Customer john = new Customer(1, "John", 24, "john@email.com");
        Customer keyla = new Customer(2, "Keyla", 28, "keila@email.com");

        customers.add(john);
        customers.add(keyla);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

}
