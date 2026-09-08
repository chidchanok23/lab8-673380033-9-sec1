package com.example.lab08.strategy;

public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public double calculateDiscount(double price) {
        return price;
    }
}
