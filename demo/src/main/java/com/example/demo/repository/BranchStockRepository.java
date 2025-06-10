package com.example.demo.repository;
import com.example.demo.model.BranchStock;
import com.example.demo.model.Drink;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BranchStockRepository extends JpaRepository<BranchStock, Long> {
    List<BranchStock> findByBranchId(int branchId);
    Optional<BranchStock> findByBranchIdAndDrink(int branchId, Drink drink);

}

