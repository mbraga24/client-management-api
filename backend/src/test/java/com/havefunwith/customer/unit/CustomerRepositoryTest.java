package com.havefunwith.customer.unit;

import com.havefunwith.AbstractTestcontainers;
import com.havefunwith.customer.Customer;
import com.havefunwith.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/*
    Data Access Layer or DAO (Data Access Object) Layer test
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        System.out.println("@BeforeEach :: getBeanDefinitionCount() ===> " + applicationContext.getBeanDefinitionCount());
    }

    /*
        TO DO COMMENTS
     */
    @Test
    void existsCustomerByEmail() {
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        Customer customer = new Customer(
                name,
                20,
                email
        );
        underTest.save(customer);

        boolean actual = underTest.existsCustomerByEmail(email);

        assertThat(actual).isTrue();
    }

    /*
        TO DO COMMENTS
    */
    @Test
    void existsCustomerByEmailFailsWhenEmailNotPresent() {
        String email = "foo";

        boolean actual = underTest.existsCustomerByEmail(email);

        assertThat(actual).isFalse();
    }
    /*
        TO DO COMMENTS
     */
    @Test
    void existsCustomerById() {
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        Customer customer = new Customer(
                name,
                20,
                email
        );

        underTest.save(customer);

        long customerId = underTest.findAll().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        var actual = underTest.existsCustomerById(customerId);

        assertThat(actual).isTrue();
    }
    /*
        TO DO COMMENTS
     */
    @Test
    void existsCustomerByIdFailsWhenIdNotPresent() {
        long customerId = -1;

        var actual = underTest.existsCustomerById(customerId);

        assertThat(actual).isFalse();
    }
}