package com.havefunwith.customer;

import com.havefunwith.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        Customer customer = new Customer(
          FAKER.name().fullName(),
          20,
          FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID()
        );

        underTest.insertCustomer(customer);
        List<Customer> actualCustomers = underTest.selectAllCustomers();

        assertThat(actualCustomers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                20,
                email
        );
        underTest.insertCustomer(customer);
        long customerId = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        Optional<Customer> actualCustomer = underTest.selectCustomerById(customerId);

        assertThat(actualCustomer).isPresent().hasValueSatisfying( c -> {
            assertThat(c.getId()).isEqualTo(customerId);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getName()).isEqualTo(customer.getName());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        long customerId = 0;
        var actual = underTest.selectCustomerById(customerId);
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                20,
                email
        );

        underTest.insertCustomer(customer);
        var actual = underTest.existsPersonWithEmail(email);
        assertThat(actual).isTrue();
    }

    @Test
    void deleteCustomer() {
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                20,
                email
        );
        underTest.insertCustomer(customer);
        long customerId = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        underTest.deleteCustomer(customerId);
        var actual = underTest.selectCustomerById(customerId);

        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomerName() {
        String newName = "John Doe";
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                20,
                email
        );
        underTest.insertCustomer(customer);

        var customerId = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        Customer updateCustomer = new Customer();
        updateCustomer.setId(customerId);
        updateCustomer.setName(newName);

        underTest.updateCustomer(updateCustomer);
        var actual = underTest.selectCustomerById(customerId);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
           assertThat(c.getId()).isEqualTo(updateCustomer.getId());
           assertThat(c.getName()).isEqualTo(newName); // new name
           assertThat(c.getEmail()).isEqualTo(customer.getEmail());
           assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerEmail() {

    }

    @Test
    void updateCustomerAge() {

    }

    @Test
    void updateAllCustomerProperties() {
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        String newName = "Updated " + name;
        String newEmail = "updated-" + email;
        int newAge = 100;
        Customer customer = new Customer(
                name,
                20,
                email
        );
        underTest.insertCustomer(customer);

        long customerId = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        Customer updateCustomer = new Customer(
                customerId,
                newName,
                newAge,
                newEmail
        );

        underTest.updateCustomer(updateCustomer);

        var actual = underTest.selectCustomerById(customerId);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
           assertThat(c.getId()).isEqualTo(updateCustomer.getId());
           assertThat(c.getName()).isEqualTo(newName);
           assertThat(c.getEmail()).isEqualTo(newEmail);
           assertThat(c.getAge()).isEqualTo(newAge);
        });
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {

    }

    @Test
    void existsPersonWithEmail() {
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                20,
                email
        );
        underTest.insertCustomer(customer);
        var actual = underTest.existsPersonWithEmail(email);

        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithEmailReturnsFalseWhenDoesNotExist() {

    }

    @Test
    void existsPersonById() {
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                20,
                email
        );
        underTest.insertCustomer(customer);

        var customerId = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        var actual = underTest.existsPersonById(customerId);

        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithIdReturnsFalseWhenIdNotPresent() {

    }
}