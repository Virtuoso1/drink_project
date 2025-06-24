package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.repository.BranchStockRepository;
import com.example.demo.repository.BranchRepository;
import com.example.demo.repository.DrinkRepository;
import com.example.demo.model.Drink;
import com.example.demo.model.BranchStock;
import org.springframework.ui.Model;

@Controller
public class AdminController {
//admin page handling
    @Autowired
    private DrinkRepository drinkRepository;
    @Autowired private BranchStockRepository stockRepo;
    @Autowired private BranchRepository branchRepo;
    @Autowired private DrinkRepository drinkRepo;
    @GetMapping("/admin/drinks")
    public String showDrinks(Model model) {
        model.addAttribute("drinks", drinkRepository.findAll());
        model.addAttribute("newDrink", new Drink());
        return "admin/drinks";
    }
@GetMapping("/admin/stock")
    public String viewStock(Model model) {
    List<BranchStock> stockList = stockRepo.findAllByOrderByBranchIdAsc();
    List<BranchStock> lowStock = stockRepo.findStocksBelowThreshold();

    model.addAttribute("stocks", stockList);
    model.addAttribute("alerts", lowStock);
    return "admin/admin_stock";
}
    @GetMapping("/add")
    public String showAddStockForm(Model model) {
        model.addAttribute("branches", branchRepo.findAll());
        model.addAttribute("stock", new BranchStock());
        return "admin/admin_add_stock";
    }

    @PostMapping("/add")
    public String addStock(@ModelAttribute BranchStock stock) {
        
        Optional<BranchStock> existing = stockRepo.findByBranchIdAndDrink(stock.getBranchId(), stock.getDrink());
        if (existing.isPresent()) {
            BranchStock current = existing.get();
            current.setStock(current.getStock() + stock.getStock());
            stockRepo.save(current);
        } else {
            stockRepo.save(stock);
        }
        return "redirect:/admin/stock";
    }


    @PostMapping("/admin/drinks/add")
    public String addDrink(@ModelAttribute Drink drink,@RequestParam("imageFile") MultipartFile imageFile) throws IOException {
    
    String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

    String uploadDir = "src/main/resources/static/images/";

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
