package com.example.demo.repository;

import com.example.demo.model.Customer;
import com.example.demo.model.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByCustID(int custID);
    @Query("SELECT DISTINCT o.custID FROM Order o")
List<Long> findCustomerIDsWithOrders();

@Query("SELECT o.branchID, COUNT(o) FROM Order o GROUP BY o.branchID")
List<Object[]> countOrdersByBranch();

@Query("SELECT o.branchID, SUM(o.total_amount) FROM Order o GROUP BY o.branchID")
List<Object[]> totalSalesByBranch();

@Query("SELECT SUM(o.total_amount) FROM Order o")
Double totalRevenue();

}
