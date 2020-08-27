package com.rimple.shoppinglist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class ShoppingListApp {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingListApp.class, args);
    }
}