package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "branchstock")
public class BranchStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "BranchID")
    private int branchId;

    @Column(name = "stock")
    private int stock;

    @Column(name = "threshold")
    private int threshold;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DrinkID", referencedColumnName = "DrinkID")
    private Drink drink;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }


    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
    public Drink getDrink() { return drink; }
    public void setDrink(Drink drink) { this.drink = drink; }
}
