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
    private Integer branchId;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "threshold")
    private Integer threshold;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BranchID", referencedColumnName = "BranchID", insertable = false, updatable = false)
    private Branch branch;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DrinkID", referencedColumnName = "DrinkID")
    private Drink drink;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

public Branch getBranch() {
    return branch;
}

public void setBranch(Branch branch) {
    this.branch = branch;
}

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }
    public Drink getDrink() { return drink; }
    public void setDrink(Drink drink) { this.drink = drink; }
}
