package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Drink")
public class Drink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DrinkID")
    private Long drinkID;

    private String name;
    private double price;
    private String description;
    
    @Column(name = "imageUrl") 
    private String imageUrl;

    // Getters and Setters
    public Long getDrinkID() {
        return drinkID;
    }

    public void setDrinkID(Long drinkID) {
        this.drinkID = drinkID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
