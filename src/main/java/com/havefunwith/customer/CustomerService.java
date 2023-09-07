package com.havefunwith.customer;

import com.havefunwith.exception.DuplicatedResourceException;
import com.havefunwith.exception.ResourceNotChangedException;
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

    public void updateCustomer(Long customerId, CustomerUpdateRequest updateRequest) {
        Customer customer = getCustomer(customerId);

        boolean changes = false;

        if (updateRequest.name() != null && !customer.getName().equals(updateRequest.name())) {
            customer.setName(updateRequest.name());
            changes = true;
        }

        if (updateRequest.age() != null && !customer.getAge().equals(updateRequest.age())) {
            customer.setAge(updateRequest.age());
            changes = true;
        }

        if (updateRequest.email() != null && !customer.getEmail().equals(updateRequest.email())) {

            if (!customerDAO.existsPersonWithEmail(updateRequest.email())) {
                throw new DuplicatedResourceException(
                        "Customer with email [%s] already exist."
                                .formatted(updateRequest.email()));
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }

        if (!changes) {
            throw new ResourceNotChangedException("No data changes found");
        }
        customerDAO.updateCustomer(customer);
    }

}
