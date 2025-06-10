package com.example.demo.controller;

import com.example.demo.model.BranchStock;
import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.BranchStockRepository;

import jakarta.servlet.http.HttpSession;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
//general routing
    @Autowired
    private CustomerRepository customerRepository;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("customer", new Customer());
        return "signup"; 
    }

    @PostMapping("/signup")//signup handling
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
    
    @PostMapping("/login")//login handling
    public String processLogin(@RequestParam String phone, @RequestParam String password, HttpSession session, Model model) {
        Customer customer = customerRepository.findByPhone(phone);

        if (customer != null && passwordEncoder.matches(password, customer.getPassword())) {
            session.setAttribute("loggedInUser", customer);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid phone number or password");
            return "login";
        }
    }
    @Autowired
    private BranchStockRepository branchStockRepository;
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("loggedInUser");
        if (customer == null) {
            return "redirect:/login";
        }
        int branchId = customer.getLocation();
        List<BranchStock> stockList = branchStockRepository.findByBranchId(branchId);

        model.addAttribute("stockList", stockList);
        model.addAttribute("customer", customer);
        return "dashboard";
}


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

}
