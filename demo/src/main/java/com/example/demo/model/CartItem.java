package com.example.demo.model;

public class CartItem {
    @SuppressWarnings("unused")
    private Drink drink;
    @SuppressWarnings("unused")
    private int quantity;
    @SuppressWarnings("unused")
    private double subtotal;

    public CartItem(Drink drink, int quantity) {
        this.drink = drink;
        this.quantity = quantity;
        this.subtotal = drink.getPrice() * quantity;
    }

}
