package com.kushanthsliit.spring.security.controller;

import com.kushanthsliit.spring.security.dto.JwtRequest;
import com.kushanthsliit.spring.security.dto.JwtResponse;
import com.kushanthsliit.spring.security.dto.TokenRefreshRequest;
import com.kushanthsliit.spring.security.dto.TokenRefreshResponse;
import com.kushanthsliit.spring.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private UserService userService;

    @GetMapping("/welcome")
    public String home(){
        return "Welcome To The Application";
    }

    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest request) throws Exception {
        return userService.authenticate(request);
    }

    @PostMapping("/sendPasswordResetToken")
    public String sentPasswordResetToken(@RequestParam String email){
        userService.sendPasswordResetToken(email);
        return "Please check your email "+ email + " . We have sent a password reset OTP";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String otp, @RequestParam String newPassword){
        userService.updatePassword(otp, newPassword);
        return "Password Updated Successfully..!";
    }

    @PostMapping("/refreshToken")
    public TokenRefreshResponse getAccessTokenByRefreshToken(@RequestBody TokenRefreshRequest request) {
        return userService.getAccessTokenByRefreshToken(request);
    }

}
