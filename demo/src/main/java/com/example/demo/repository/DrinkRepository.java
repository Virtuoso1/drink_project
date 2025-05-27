package com.example.demo.repository;

import com.example.demo.model.Drink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DrinkRepository extends JpaRepository<Drink, Long> {
}
