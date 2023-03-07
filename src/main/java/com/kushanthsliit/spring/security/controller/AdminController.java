package com.kushanthsliit.spring.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin("http://localhost:4200")
public class AdminController {

    @GetMapping("/admin-msg")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin(){
        return "This is Admin Page";
    }
}
