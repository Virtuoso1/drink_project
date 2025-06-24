package com.example.demo.repository;
import com.example.demo.model.BranchStock;
import com.example.demo.model.Drink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BranchStockRepository extends JpaRepository<BranchStock, Long> {
    List<BranchStock> findByBranchId(int branchId);
    Optional<BranchStock> findByBranchIdAndDrink(int branchId, Drink drink);
    List<BranchStock> findAllByOrderByBranchIdAsc();
    @Query("SELECT b FROM BranchStock b WHERE b.stock < b.threshold")
    List<BranchStock> findStocksBelowThreshold();
}

