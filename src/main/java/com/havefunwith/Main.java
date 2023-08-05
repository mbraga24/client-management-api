package com.havefunwith;

import com.havefunwith.singleton.Food;
import com.havefunwith.singleton.FoodService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@SpringBootApplication
public class Main {

    private static FoodService foodService = null;

    public Main(FoodService foodService) {
        this.foodService = foodService;
    }

    public static void main(String[] args) {
        /*
            Never instantiate classes like this:
            CustomerService customerService = new CustomerService(new CustomerDataAccessService());
            CustomerController customerController = new CustomerController(customerService);
         */
        ConfigurableApplicationContext applicationContext =
                SpringApplication.run(Main.class, args);

        // execute bean
        foodService.printFoodDetails();

        printBeans(applicationContext);

    }

    private static void printBeans(ConfigurableApplicationContext cntx) {

        String[] beanDefinitionNames = cntx.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }

    }

}
