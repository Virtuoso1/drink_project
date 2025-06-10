package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "OrderItems")
public class OrderItem {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int itemID;

  @ManyToOne
  @JoinColumn(name = "OrderID")
  private Order order;
@ManyToOne
@JoinColumn(name = "DrinkID", insertable = false, updatable = false)
private Drink drink;

public Drink getDrink() {
    return drink;
}

public void setDrink(Drink drink) {
    this.drink = drink;}
  private Long drinkID;
  private int quantity;
  private Double subtotal;

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getDrinkID() {
        return drinkID;
    }

    public void setDrinkID(Long drinkID) {
        this.drinkID = drinkID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}
