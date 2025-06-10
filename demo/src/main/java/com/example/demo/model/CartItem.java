package com.example.demo.model;

public class CartItem {
    private Drink drink;
    private int quantity;
    private double subtotal;

    public CartItem(Drink drink, int quantity) {
        this.drink = drink;
        this.quantity = quantity;
        this.subtotal = drink.getPrice() * quantity;
    }

}
