package com.kushanthsliit.spring.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kushanthsliit.spring.security.configiration.MyUserDetailsService;
import com.kushanthsliit.spring.security.dto.JwtRequest;
import com.kushanthsliit.spring.security.dto.JwtResponse;
import com.kushanthsliit.spring.security.dto.TokenRefreshRequest;
import com.kushanthsliit.spring.security.dto.TokenRefreshResponse;
import com.kushanthsliit.spring.security.entity.User;
import com.kushanthsliit.spring.security.repository.UserRepository;
import com.kushanthsliit.spring.security.utility.JwtUtility;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtility jwTutility;

    @Value("${app.jwtRefreshExpirationMs}")
    private int jwtRefreshExpirationMs;

    @Value("${jwt.secret}")
    private String secretKey;

    public JwtResponse authenticate(JwtRequest request) throws Exception {
        final String token;
        final String refreshToken;
        User user;
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            refreshToken = userService.generateRefreshtokenFromUsername(userDetails.getUsername());
            token = jwTutility.generateToken(userDetails);
            user = userRepository.findByUsername(userDetails.getUsername());
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
        }
        catch (BadCredentialsException e){
            throw  new Exception("INVALID CREDENTIALS ", e);
        }
        return new JwtResponse("Bearer",token, refreshToken, user.getRole());
    }

    public void sendPasswordResetToken(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            /*String token = UUID.randomUUID().toString();*/
                String numbers = "1234567890";
                Random random = new Random();
                char[] otp = new char[4];

                for(int i = 0; i< 4 ; i++) {
                    otp[i] = numbers.charAt(random.nextInt(numbers.length()));
                }
            user.setPasswordResetToken(String.valueOf(otp));
            userRepository.save(user);

            // Email message
            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            passwordResetEmail.setFrom("kushanthantony@gmail.com");
            passwordResetEmail.setTo(user.getEmail());
            passwordResetEmail.setSubject("Password Reset Request");
            passwordResetEmail.setText("Your Password Reset OTP : ".concat(String.valueOf(otp)));

            emailService.sendEmail(passwordResetEmail);

            System.out.println("Email Successfully sent...!");

        } else {
            throw new UsernameNotFoundException("Could not find any user with the email " + email);
        }
    }


    public void updatePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId).get();
        if(user != null){
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);

            user.setPasswordResetToken(null);
            userRepository.save(user);
        }
        else {
            throw new UsernameNotFoundException("Could not update password..!");
        }
    }


    public TokenRefreshResponse getAccessTokenByRefreshToken(TokenRefreshRequest request) {
        DecodedJWT refreshJwt = JWT.decode(request.getRefreshToken());
        if(!verifyRefreshTokenExpiration(refreshJwt)){
            User user = userRepository.findByRefreshToken(request.getRefreshToken());
            if(user != null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
                String token = jwTutility.generateToken(userDetails);
//                String refreshToken = saveRefreshToken(user.getUsername());

                return new TokenRefreshResponse("Bearer", request.getRefreshToken(), token);
            }
        }
        return new TokenRefreshResponse("","Refresh Token Was Expired. Login Again","Access Token Was Expired. Login Again");
    }

    //Generate Refresh Token
    public String generateRefreshtokenFromUsername(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtRefreshExpirationMs)).signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    //Verify RefreshToken
    public boolean verifyRefreshTokenExpiration(DecodedJWT refreshToken) {
        Date expireAt = refreshToken.getExpiresAt();
        return expireAt.before(new Date());
    }

    public User verifyPasswordResetToken(String token) {
        User user = userRepository.findByPasswordResetToken(token);
        if(user != null){
            return user;
        }
        return null;
    }
}
