package com.example.lab08.strategy;

public class MemberDiscountStrategy implements DiscountStrategy {

    @Override
    public double calculateDiscount(double price) {
        return price * 0.90;
    }
}
