package com.havefunwith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


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

//        printBeans(applicationContext);

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
    */
    private static void printBeans(ConfigurableApplicationContext cntx) {
        String[] beanDefinitionNames = cntx.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }

}
