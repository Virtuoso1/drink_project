package com.example.demo.service;
import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Service
//handles password encryption before storing
public class CustomerService {
     @Autowired
        private CustomerRepository customerRepository;
    
        private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();//sprigboot inbuilt encryptor
    
        public void registerCustomer(Customer customer) {
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            customerRepository.save(customer);
        }
}
