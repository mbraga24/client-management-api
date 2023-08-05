package com.havefunwith.singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FoodFactory {

    @Bean("Food")
    public Food createFood() {
        return new Food("Beans, rice, fried chicken breast with onions, " +
                "tomato, lettuce, cress, onions and cucumbers", 25.00);
    }

}
