package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private CustomerRepository customerRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("customer", new Customer());
        return "signup"; 
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute Customer customer) {
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
        customerRepository.save(customer);
        return "redirect:/login"; 
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
