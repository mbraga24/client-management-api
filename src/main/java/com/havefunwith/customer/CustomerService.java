package com.havefunwith.customer;

import com.havefunwith.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDAO;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.selectAllCustomers();
    }

    public Customer getCustomer(Long id) {
        return customerDAO.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFound(
                        "Customer id [%s] does not exist".formatted(id)
                ));
    }

}
