package com.kushanthsliit.spring.security.controller;

import com.kushanthsliit.spring.security.dto.*;
import com.kushanthsliit.spring.security.entity.User;
import com.kushanthsliit.spring.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
@CrossOrigin("http://localhost:4200")
public class HomeController {
    @Autowired
    private UserService userService;

    @GetMapping("/welcome")
    public ResponseEntity<ApiResponse<String>> home(){
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "",
                "Welcome to the Application"));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<JwtResponse>> authenticate(@RequestBody JwtRequest request) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "",
                userService.authenticate(request)));
    }

    @PostMapping("/sendPasswordResetToken")
    public ResponseEntity<ApiResponse<String>> sentPasswordResetToken(@RequestParam String email){
        userService.sendPasswordResetToken(email);
        return ResponseEntity.ok(new ApiResponse<String>(HttpStatus.OK.value(), "", "Password Reset Token Was sent to "+ email));
    }

    @GetMapping("/verifyToken")
    public ResponseEntity<ApiResponse<User>> verifyPasswordResetToken(@RequestParam String token){
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                    "Password Reset Token Verified Successfully",
                    userService.verifyPasswordResetToken(token)));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestParam long userId, @RequestParam String newPassword){
        userService.updatePassword(userId, newPassword);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "","Password Updated Successfully..!"));
    }

    @PostMapping("/refreshToken")
    public TokenRefreshResponse getAccessTokenByRefreshToken(@RequestBody TokenRefreshRequest request) {
        return userService.getAccessTokenByRefreshToken(request);
    }

}
