package com.example.price_comparator_market;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    @GetMapping("/")
    public String sayHello() {
        return "Hello, World!";
    }
}
