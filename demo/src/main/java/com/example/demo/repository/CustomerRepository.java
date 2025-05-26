package com.example.demo.repository;
import com.example.demo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

//Customer model repo
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByPhone(String phone);
}