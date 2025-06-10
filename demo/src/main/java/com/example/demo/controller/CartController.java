package com.example.demo.controller;
import com.example.demo.model.Drink;
import com.example.demo.model.Order;
import com.example.demo.model.BranchStock;
import com.example.demo.model.Customer;
import com.example.demo.model.OrderItem;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.DrinkRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.BranchStockRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class CartController {
  @Autowired
  private BranchStockRepository branchStockRepository;
  @Autowired private DrinkRepository drinkRepo;
  @Autowired private OrderRepository orderRepo;
  @Autowired private OrderItemRepository itemRepo;

  @GetMapping("/drink/{id}")
  public String drinkDetail(@PathVariable Long id, Model model) {
    Optional<Drink> opt = drinkRepo.findById(id);
    if (opt.isEmpty()) return "redirect:/dashboard";
    model.addAttribute("drink", opt.get());
    return "drink_detail";
  }

  @PostMapping("/cart/add")
public String addToCart(@RequestParam Long drinkId,
                        @RequestParam int quantity,
                        HttpSession session) {

    Order order = (Order) session.getAttribute("currentOrder");
    if (order == null) {
        Customer cust = (Customer) session.getAttribute("loggedInUser");
        if (cust == null) {
    return "redirect:/login";
}
        order = new Order();
        order.setCustID(cust.getCustID());
        order.setBranchID(cust.getLocation()); 
        order.setStatus("Pending"); 
        order.setTotal_amount(0.0); 
        order = orderRepo.save(order);
        session.setAttribute("currentOrder", order);
    }

    Drink drink = drinkRepo.findById(drinkId).orElseThrow();
    List<OrderItem> items = itemRepo.findByOrder(order);
    boolean found = false;

    for (OrderItem existing : items) {
        if (existing.getDrinkID().equals(drinkId)) {
            int newQty = existing.getQuantity() + quantity;
            existing.setQuantity(newQty);
            existing.setSubtotal(drink.getPrice() * newQty);
            itemRepo.save(existing);
            found = true;
            break;
        }
    }

    if (!found) {
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setDrinkID(drinkId);
        item.setQuantity(quantity);
        item.setSubtotal(drink.getPrice() * quantity);
        itemRepo.save(item);

    }

    double total = itemRepo.findByOrder(order)
        .stream()
        .mapToDouble(OrderItem::getSubtotal)
        .sum();

    order.setTotal_amount(total);
    orderRepo.save(order);

    return "redirect:/cart/view";
}


  
@GetMapping("/cart/view")
  public String viewCart(HttpSession session, Model model) {
    Order order = (Order) session.getAttribute("currentOrder");
    if (order != null) {
        List<OrderItem> items = itemRepo.findByOrder(order);
        model.addAttribute("order", order);
        model.addAttribute("items", items);
    }
    return "cart_view";
  }
  @GetMapping("/checkout")
public String checkout(HttpSession session) {

    Order order = (Order) session.getAttribute("currentOrder");
    if (order == null) return "redirect:/dashboard";

    List<OrderItem> items = itemRepo.findByOrder(order);
    for (OrderItem item : items) {
      Long drinkId = item.getDrinkID();
      int branchId = order.getBranchID();

    Drink drink = drinkRepo.findById(drinkId)
    .orElseThrow(() -> new RuntimeException("Drink not found"));

    BranchStock stock = branchStockRepository.findByBranchIdAndDrink(branchId, drink)
    .orElseThrow(() -> new RuntimeException("Stock not found"));
        if (stock != null && stock.getStock() >= item.getQuantity()) {
            stock.setStock(stock.getStock() - item.getQuantity());
            branchStockRepository.save(stock);
        } else {

            return "redirect:/cart/view?error=outofstock";
        }
    }

    order.setStatus("Confirmed"); 
    order.setOrder_date(LocalDateTime.now());
    orderRepo.save(order);

    session.removeAttribute("currentOrder");

    return "redirect:/orders";
}
@GetMapping("/orders")
public String viewOrders(HttpSession session, Model model) {
    Customer cust = (Customer) session.getAttribute("loggedInUser");
    if (cust == null) return "redirect:/login";

    Map<Long, List<OrderItem>> itemsMap = new HashMap<>();
      List<Order> orders = orderRepo.findByCustID(cust.getCustID());
for (Order order : orders) {
    List<OrderItem> items = itemRepo.findByOrder(order);
  itemsMap.put((long) order.getOrderID(), items);
  }
    model.addAttribute("itemsMap", itemsMap);
    model.addAttribute("orders", orders);
    return "order_history";
}

}