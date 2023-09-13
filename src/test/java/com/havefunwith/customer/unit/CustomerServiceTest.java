package com.havefunwith.customer;

import com.havefunwith.exception.DuplicatedResourceException;
import com.havefunwith.exception.ResourceNotFoundException;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class) // another way of initializing mocks (no need to initialize, open, and close AutoCloseable).
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    private final String name = "John Doe";
    private final Integer age = 23;
    private final String email =  "john_doe@email.com";

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void canGetAllCustomers() {
        underTest.getAllCustomers();

        Mockito.verify(customerDao)
                .selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        long customerId = 3;
        Customer customer = new Customer(name, age, email);
        Mockito.when(customerDao.selectCustomerById(customerId))
                .thenReturn(Optional.of(customer));

        var actual = underTest.getCustomer(customerId);

        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional() {
        long customerId = 3;
        Mockito.when(customerDao.selectCustomerById(customerId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCustomer(customerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id [%s] does not exist".formatted(customerId));
    }

    @Test
    void canAddCustomer() {
        CustomerRegistrationRequest customerRequest =
                new CustomerRegistrationRequest(name, email, age);

        Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        underTest.addCustomer(customerRequest);

        /*
            ArgumentCaptor<T> is mainly for complex objects that
            are constructed within the methods.
         */
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );

        Mockito.verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(customerRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customerRequest.age());
    }

    @Test
    void willThrowExceptionIfEmailExists() {
        CustomerRegistrationRequest customerRequest =
                new CustomerRegistrationRequest(name, email, age);

        Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> underTest.addCustomer(customerRequest))
                .isInstanceOf(DuplicatedResourceException.class)
                .hasMessage("Customer with email [%s] already exist.".formatted(email));

        Mockito.verify(customerDao, never()).insertCustomer(any());

    }

    @Test
    void canDeleteCustomerById() {
        long customerId = 1;

        Mockito.when(customerDao.existsPersonById(customerId))
                .thenReturn(true);

        underTest.deleteCustomer(customerId);

        Mockito.verify(customerDao)
                .deleteCustomer(customerId);
    }

    @Test
    void willThrowExceptionIfPersonIdIsNotFound() {
        long customerId = 10;

        Mockito.when(customerDao.existsPersonById(customerId))
                .thenReturn(false);

        assertThatThrownBy(() -> underTest.deleteCustomer(customerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id [%s] was not found.".formatted(customerId));

        Mockito.verify(customerDao, never()).deleteCustomer(any());
    }

    @Test
    void willUpdateAllCustomerProperties() {
        // Given
        long customerId = 10;
        String newEmail = "john123@email.com";
        String newName = "Johnny";
        int newAge = 30;
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                newName,
                newEmail,
                newAge
        );
        Customer customer = new Customer(customerId, name, age, email);

        Mockito.when(customerDao.selectCustomerById(customerId))
                .thenReturn(Optional.of(customer));
        Mockito.when(customerDao.existsPersonWithEmail(newEmail))
                .thenReturn(false);
        // When

        underTest.updateCustomer(customerId, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void willUpdateOnlyCustomerName() {
        // Given
        long customerId = 10;
        String newName = "Johnny";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                newName, null, null
        );
        Customer customer = new Customer(customerId, name, age, email);

        Mockito.when(customerDao.selectCustomerById(customerId))
                .thenReturn(Optional.of(customer));

        // When
        underTest.updateCustomer(customerId, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void willUpdateOnlyCustomerEmail() {

    }

    @Test
    void willUpdateOnlyCustomerAge() {

    }

    @Test
    void willThrowResourceNotChangeException() {

    }

    @Test
    void willThrowDuplicatedResourceException() {

    }
}