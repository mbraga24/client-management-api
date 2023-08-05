package com.havefunwith;

import com.havefunwith.singleton.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;


@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        /*
            Never instantiate classes like this:
            CustomerService customerService = new CustomerService(new CustomerDataAccessService());
            CustomerController customerController = new CustomerController(customerService);
         */
        ConfigurableApplicationContext applicationContext =
                SpringApplication.run(Main.class, args);

        printBeans(applicationContext);

    }

    @Component
    public static class CustomBeanRunner implements CommandLineRunner {
        private final ProductService productService;

        public CustomBeanRunner(ProductService productService) {
            this.productService = productService;
        }

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

}
