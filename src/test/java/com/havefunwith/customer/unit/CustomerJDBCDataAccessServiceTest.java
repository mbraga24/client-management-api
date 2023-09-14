package com.havefunwith.customer.unit;

import com.havefunwith.AbstractTestcontainers;
import com.havefunwith.customer.Customer;
import com.havefunwith.customer.CustomerJDBCDataAccessService;
import com.havefunwith.customer.CustomerRowMapper;
import com.havefunwith.customer.CustomerUpdateRequest;
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

    /*
     TO DO WRITE COMMENTS
     */
    @Test
    void selectAllCustomers() {
        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                20,
                FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID()
        );

        underTest.insertCustomer(customer);
        List<Customer> actualCustomers = underTest.selectAllCustomers();

        assertThat(actualCustomers).isNotEmpty();
    }

    /*
     TO DO WRITE COMMENTS
     */
    @Test
    void selectCustomerById() {
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
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

        Optional<Customer> actualCustomer = underTest.selectCustomerById(customerId);

        assertThat(actualCustomer).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(customerId);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getName()).isEqualTo(customer.getName());
        });
    }

    /*
     TO DO WRITE COMMENTS
     */
    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        long customerId = 0;
        var actual = underTest.selectCustomerById(customerId);
        assertThat(actual).isEmpty();
    }

    /*
     TO DO WRITE COMMENTS
     */
    @Test
    void insertCustomer() {
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        Customer customer = new Customer(
                name,
                20,
                email
        );

        underTest.insertCustomer(customer);
        var actual = underTest.existsPersonWithEmail(email);
        assertThat(actual).isTrue();
    }

    /*
     TO DO WRITE COMMENTS
     */
    @Test
    void deleteCustomer() {
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
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

        underTest.deleteCustomer(customerId);
        var actual = underTest.selectCustomerById(customerId);

        assertThat(actual).isNotPresent();
    }

    /*
        TO DO WRITE COMMENTS
     */
    @Test
    void updateCustomerName() {
        String newName = "John Doe";
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        Customer customer = new Customer(
                name,
                20,
                email
        );
        underTest.insertCustomer(customer);

        var customerId = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        // create customer payload for update - change name
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
        // Generate updated attributes.
        String newEmail = FAKER.internet().safeEmailAddress() + "_updated";
        // Generate fake customer data and create  a new customer object.
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress();
        Customer customer = new Customer(
                name,
                20,
                email
        );

        // Add a new customer to the database.
        underTest.insertCustomer(customer);

        // Retrieve the customer's ID using their email.
        long customerId = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        // Create a payload for updating the customer with a modified email.
        Customer updateCustomer = new Customer();
        // Set the ID for the customer update payload.
        updateCustomer.setId(customerId);
        updateCustomer.setEmail(newEmail);
        // Update the customer's information in the database.
        underTest.updateCustomer(updateCustomer);

        // Retrieve the customer using the given ID.
        Optional<Customer> actual = underTest.selectCustomerById(customerId);

        // Assert that only the email has changed,
        // while the other attributes remain the same.
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(updateCustomer.getId());
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(newEmail); // new email
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void updateCustomerAge() {
        // Generate updated attribute.
        int newAge = 80;
        // Generate fake customer data and create new customer object.
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress();
        Customer customer = new Customer(
                name,
                20,
                email
        );

        // Add a new customer to the database.
        underTest.insertCustomer(customer);

        // Retrieve the customer's ID using their email.
        long customerId = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        // Create a payload for updating the customer with a modified age value.
        Customer updateCustomer = new Customer();
        // Set the ID for the customer update payload.
        updateCustomer.setId(customerId);
        updateCustomer.setAge(newAge);

        // Update the customer's information in the database.
        underTest.updateCustomer(updateCustomer);


        // Retrieve the customer using the given ID.
        Optional<Customer> actual = underTest.selectCustomerById(customerId);

        // Assert that only the age value has been updated,
        // while other attributes remain unchanged.
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(updateCustomer.getId());
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(newAge);
        });
    }

    @Test
    void updateAllCustomerProperties() {
        // Generate fake customer data and create new customer object.
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        Customer customer = new Customer(
                name,
                20,
                email
        );
        // Generate updated attributes.
        String newName = "Updated " + name;
        String newEmail = "updated-" + email;
        int newAge = 100;

        // Add a new customer to the database.
        underTest.insertCustomer(customer);

        // Retrieve the customer's ID using their email.
        long customerId = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        // Create a new customer object with all new attributes.
        Customer updateCustomer = new Customer(
                customerId,
                newName,
                newAge,
                newEmail
        );

        // Update the customer's information in the database.
        underTest.updateCustomer(updateCustomer);

        // Retrieve the customer using the given ID.
        var actual = underTest.selectCustomerById(customerId);

        // Assert that all the values were changed after the update.
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(updateCustomer.getId());
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getAge()).isEqualTo(newAge);
        });
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        // Add a new customer to the database.
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        int age = 20;
        Customer customer = new Customer(
                name,
                age,
                email
        );
        // Add a new customer to the database.
        underTest.insertCustomer(customer);

        // Retrieve the customer's ID using their email.
        long customerId = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();
        // Create a payload for updating the customer without making any changes.
        Customer updateCustomer = new Customer();
        updateCustomer.setId(customerId);

        // Update the customer's information in the database.
        underTest.updateCustomer(updateCustomer);
        // Retrieve the customer using the given ID.
        Optional<Customer> actual = underTest.selectCustomerById(customerId);

        // Assert that the values remain unchanged after the update.
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(customerId);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void existsPersonWithEmail() {
        // Generate fake customer data and create a new customer object.
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        Customer customer = new Customer(
                name,
                20,
                email
        );

        // Add a new customer to the database.
        underTest.insertCustomer(customer);

        // Check if a customer with the specified email exists in the system.
        var actual = underTest.existsPersonWithEmail(email);

        // Asserts that the value is true and customer exists.
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithEmailReturnsFalseWhenDoesNotExist() {
        // Initialize an email that is not present in the database.
        String email = "non-existent-email@email.com";

        // Pass the email to the method of the mocked class.
        boolean actual = underTest.existsPersonWithEmail(email);

        // Assert that the email check returns false.
        assertThat(actual).isFalse();
    }

    @Test
    void existsPersonById() {
        // Generate fake customer data and create a new customer object.
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        Customer customer = new Customer(
                name,
                20,
                email
        );

        // Add a new customer to the database.
        underTest.insertCustomer(customer);

        // Retrieve customer's ID using their email.
        var customerId = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        // Checks if a customer with given ID exists in the system.
        var actual = underTest.existsPersonById(customerId);

        // Assert that the value is true and the customer exists.
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithIdReturnsFalseWhenIdNotPresent() {
        // Create a fake ID variable.
        long customerId = -1;

        // Check if a customer with the specified ID exists.
        boolean actual = underTest.existsPersonById(customerId);

        // Assert that the ID check returns false.
        assertThat(actual).isFalse();
    }
}