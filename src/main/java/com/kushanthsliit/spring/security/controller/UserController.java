package com.kushanthsliit.spring.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:4200")
public class UserController {

    @GetMapping("/user-msg")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> user(){
        return ResponseEntity.ok("This is User Page");
    }

}
