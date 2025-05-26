package com.example.demo.model;
import jakarta.persistence.*;
//customer model -> Maps to customer table in database
@Entity
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer custID;

    private String name;
    private String phone;
    private String location;
    
    @Column(nullable = false)
    private String password;

     public Integer getCustID() {
        return custID;
    }

    public void setCustID(Integer custID) {
        this.custID = custID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

      public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
