package com.kushanthsliit.spring.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/user-msg")
    @PreAuthorize("hasRole('USER')")
    public String user(){
        return "This is User Page";
    }

}
