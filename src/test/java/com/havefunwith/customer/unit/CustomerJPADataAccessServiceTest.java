package com.havefunwith.customer.unit;

import com.github.javafaker.Faker;
import com.havefunwith.customer.Customer;
import com.havefunwith.customer.CustomerJPADataAccessService;
import com.havefunwith.customer.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        underTest.selectAllCustomers();

        Mockito.verify(customerRepository)
                .findAll();
    }

    @Test
    void selectCustomerById() {
        long customerId = -1;

        underTest.selectCustomerById(customerId);

        Mockito.verify(customerRepository)
                .findById(customerId);
    }

    @Test
    void insertCustomer() {
        Customer customer = new Customer(
                "John Doe",
                24,
                "john_doe@email.com"
        );

        underTest.insertCustomer(customer);

        Mockito.verify(customerRepository)
                .save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        String email = "john_doe@email.com";

        underTest.existsPersonWithEmail(email);

        Mockito.verify(customerRepository)
                .existsCustomerByEmail(email);
    }

    @Test
    void existsPersonById() {
        long customerId = 1;

        underTest.existsPersonById(customerId);

        Mockito.verify(customerRepository)
                .existsCustomerById(customerId);
    }

    @Test
    void deleteCustomer() {
        long customerId = 1;

        underTest.deleteCustomer(customerId);

        Mockito.verify(customerRepository)
                .deleteById(customerId);
    }

    @Test
    void updateCustomer() {
        Customer customer = new Customer(
                "John Doe",
                24,
                "john_doe@email.com"
        );

        underTest.updateCustomer(customer);

        Mockito.verify(customerRepository)
                .save(customer);
    }
}