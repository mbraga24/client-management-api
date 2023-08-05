package com.havefunwith.singleton;

import org.springframework.stereotype.Service;

@Service
public class FoodService {

    private final Food food;

    public FoodService(Food food) {
        this.food = food;
    }

    public void printFoodDetails() {
        System.out.println("Dish: " + food.getName());
        System.out.println("Price: $" + food.getPrice());
    }

}
