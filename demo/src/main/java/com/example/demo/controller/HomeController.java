package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
/*Only controlls routing to the landing page.
I tried to move it to the Auth controller to 
reduce boilerplate but everything broke down.
DO NOT TOUCH THIS FILE.
WITHOUT IT THE APP DOESNT LAUNCH
*/
public class HomeController {
    @GetMapping("/")
public String landingPage() {
    return "landing";
}

}
