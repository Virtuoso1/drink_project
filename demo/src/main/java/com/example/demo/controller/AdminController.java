package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.repository.DrinkRepository;
import com.example.demo.model.Drink;
import org.springframework.ui.Model;

@Controller
public class AdminController {
//admin page handling
    @Autowired
    private DrinkRepository drinkRepository;

    @GetMapping("/admin/drinks")
    public String showDrinks(Model model) {
        model.addAttribute("drinks", drinkRepository.findAll());
        model.addAttribute("newDrink", new Drink());
        return "admin/drinks";
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
