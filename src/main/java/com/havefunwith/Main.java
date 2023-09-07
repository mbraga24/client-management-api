package com.havefunwith;

import com.havefunwith.customer.Customer;
import com.havefunwith.customer.CustomerRepository;
import com.havefunwith.customer.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@SpringBootApplication
public class Main {

    private final CustomerService customerService;

    public Main(CustomerService customerService) {
        this.customerService = customerService;
    }

    public static void main(String[] args) {
        /*
            Never instantiate classes like this:
            CustomerService customerService = new CustomerService(new CustomerDataAccessService());
            CustomerController customerController = new CustomerController(customerService);
         */
        ConfigurableApplicationContext applicationContext =
                SpringApplication.run(Main.class, args);

        //  printBeans(applicationContext);

    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            Customer john = new Customer(
                    "John",
                    24,
                    "john@email.com");
            Customer keyla = new Customer(
                    "Keyla",
                    28,
                    "keila@email.com");

            List<Customer> customers = List.of(john, keyla);
//            customerRepository.saveAll(customers);
        };
    }
    /*
    @Component
    public static class CustomBeanRunner implements CommandLineRunner {
        private final ProductService productService;

        public CustomBeanRunner(ProductService productService) {
            this.productService = productService;
        }

        // "String...": Special syntax called "varargs" (variable-length argument list).
        // It allows the method to accept an arbitrary number of String arguments,
        // including zero arguments.
        public void run(String... args) {
            productService.processProduct();
        }
    }

    private static void printBeans(ConfigurableApplicationContext cntx) {
        String[] beanDefinitionNames = cntx.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }
    */
}
