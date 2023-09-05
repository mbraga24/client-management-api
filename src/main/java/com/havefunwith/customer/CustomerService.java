package com.havefunwith.customer;

import com.havefunwith.exception.DuplicatedResourceException;
import com.havefunwith.exception.ResourceNotFoundException;
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
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with id [%s] does not exist".formatted(id)
                ));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        String email = customerRegistrationRequest.email();
        if (customerDAO.existsPersonWithEmail(email)) {
            throw new DuplicatedResourceException("Customer with email [%s] already exist.".formatted(email));
        }
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.email()
        );
        customerDAO.insertCustomer(customer);
    }

    public void deleteCustomer(Long customerId) {
        if (!customerDAO.existsPersonById(customerId)) {
            throw new ResourceNotFoundException(
                    "Customer with id [%s] was not found.".formatted(customerId)
            );
        }
        customerDAO.deleteCustomer(customerId);
    }

}
