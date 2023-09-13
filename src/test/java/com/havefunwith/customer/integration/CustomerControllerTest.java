package com.havefunwith.customer.integration;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.havefunwith.customer.Customer;
import com.havefunwith.customer.CustomerRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private final String CUSTOMER_URI = "api/v1/customers";
    private final Random RANDOM = new Random();

    @Test
    void canRegisterACustomer() {
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.lastName() + "_" + UUID.randomUUID() + "@emailtesting.com";
        int age = RANDOM.nextInt(1, 99);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name, email, age
        );

        // create customer
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // find all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // customer is present
        Customer expectedCustomer = new Customer(
                name, age, email
        );

        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        // find customer by id
        long customerId = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(customer -> customer.getId())
                .findFirst()
                .orElseThrow();

        expectedCustomer.setId(customerId);

        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteCustomer() {
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.lastName() + "_" + UUID.randomUUID() + "@emailtesting.com";
        int age = RANDOM.nextInt(1, 99);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name, email, age
        );

        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        long customerId = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(customer -> customer.getId())
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(CUSTOMER_URI + "/{id}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {

    }

}
