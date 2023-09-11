package com.havefunwith.customer;

import com.havefunwith.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest // -- 239 beans from context
@ComponentScan(basePackages = "com.havefunwith") // test works with this annotation
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest // when it should work just with this -- 96 beans from context
class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        System.out.println("@BeforeEach :: getBeanDefinitionCount() ===> " + applicationContext.getBeanDefinitionCount());
    }

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

    @Test
    void existsCustomerByEmailFailsWhenEmailNotPresent() {
        String email = "foo";

        boolean actual = underTest.existsCustomerByEmail(email);

        assertThat(actual).isFalse();
    }

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

    @Test
    void existsCustomerByIdFailsWhenIdNotPresent() {
        long customerId = -1;

        var actual = underTest.existsCustomerById(customerId);

        assertThat(actual).isFalse();
    }
}