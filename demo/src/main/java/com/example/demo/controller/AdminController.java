package com.example.demo.controller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.repository.BranchStockRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.BranchRepository;
import com.example.demo.repository.DrinkRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.model.Drink;
import com.example.demo.model.Order;
import com.example.demo.model.Branch;
import com.example.demo.model.BranchStock;
import com.example.demo.model.Customer;

import org.springframework.ui.Model;

@Controller
public class AdminController {
//admin page handling
    @Autowired
    private DrinkRepository drinkRepository;
@Autowired
private CustomerRepository customerRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired private BranchStockRepository stockRepo;
    @Autowired private BranchRepository branchRepo;
    @Autowired private DrinkRepository drinkRepo;
    @GetMapping("/admin/drinks")
    public String showDrinks(Model model) {
        model.addAttribute("drinks", drinkRepository.findAll());
        model.addAttribute("newDrink", new Drink());
        return "admin/drinks";
    }
@GetMapping("/admin/reports")
public String viewReports(Model model) {
    List<Order> orders = orderRepo.findAll(); // We'll use raw orders to extract info

    List<Map<String, String>> customerOrders = new ArrayList<>();

    for (Order order : orders) {
        Map<String, String> row = new HashMap<>();
        
        String customerName = customerRepo.findById((long) order.getCustID())
                .map(Customer::getName).orElse("Unknown");

       
        String branchName = branchRepo.findById(order.getBranchID())
                .map(Branch::getName).orElse("Unknown");

        row.put("customer", customerName);
        row.put("branch", branchName);
        row.put("orderId", String.valueOf(order.getOrderID()));
        row.put("date", order.getOrder_date().toString());
        
        double total = order.getTotal_amount() != null ? order.getTotal_amount() : 0.0;
        row.put("amount", String.format("Ksh %.2f", total));
        customerOrders.add(row);
    }

    
    List<Object[]> salesTotals = orderRepo.totalSalesByBranch();
    List<String[]> salesTotalsNamed = salesTotals.stream().map(obj -> {
        Integer branchId = (Integer) obj[0];
        Double total = (Double) obj[1];
        String name = branchRepo.findById(branchId).map(b -> b.getName()).orElse("Unknown");
        return new String[] { name, String.format("%.2f", total) };
    }).collect(Collectors.toList());

    Double totalRevenue = orderRepo.totalRevenue();

    model.addAttribute("customerOrders", customerOrders);
    model.addAttribute("salesTotals", salesTotalsNamed);
    model.addAttribute("totalRevenue", totalRevenue);

    return "admin/reports";
}

@GetMapping("/admin/stock")
    public String viewStock(Model model) {
    List<BranchStock> stockList = stockRepo.findAllByOrderByBranchIdAsc();
    List<BranchStock> lowStock = stockRepo.findStocksBelowThreshold();
System.out.println("Branches: " + branchRepo.findAll());

    model.addAttribute("stocks", stockList);
    model.addAttribute("alerts", lowStock);
    model.addAttribute("branches", branchRepo.findAll());
    model.addAttribute("drinks", drinkRepo.findAll());
    model.addAttribute("stock", new BranchStock());
    return "admin/admin_stock";
}
 
@PostMapping("/admin/stock/add")
public String addStock(@ModelAttribute BranchStock stock) {
    if (stock.getDrink() == null || stock.getDrink().getDrinkID() == null) {
        return "redirect:/admin/stock?error=MissingDrink";
    }
    stock.setThreshold(20);
    Optional<Drink> drinkOpt = drinkRepo.findById(stock.getDrink().getDrinkID());
    if (drinkOpt.isPresent()) {
        Drink drink = drinkOpt.get();
        Optional<BranchStock> existing = stockRepo.findByBranchIdAndDrink(stock.getBranchId(), drink);

        if (existing.isPresent()) {
            BranchStock current = existing.get();
            current.setStock(current.getStock() + stock.getStock());
            stockRepo.save(current);
        } else {
            stock.setDrink(drink);
            stockRepo.save(stock);
        }
    }

    return "redirect:/admin/stock";
}

    @PostMapping("/admin/drinks/add")
    public String addDrink(@ModelAttribute Drink drink,@RequestParam("imageFile") MultipartFile imageFile) throws IOException {
    
    String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

    String uploadDir = new File("src/main/resources/static/images").getAbsolutePath();

    Path uploadPath = Paths.get(uploadDir);
    if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
    }

    Path filePath = uploadPath.resolve(fileName);
    Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

    drink.setImageUrl("/images/" + fileName);

    drinkRepository.save(drink);

    return "redirect:/admin/drinks";
}

    @GetMapping("/admin/login")
    public String adminLogin() {
        return "admin/login";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }
}
