package com.havefunwith.singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    /*
        createProduct bean was created and kept in the AppliationContext
        for future use.
     */
    @Bean
    public Product createProduct() {
        return new Product("Shoes", 50.0);
    }

}
