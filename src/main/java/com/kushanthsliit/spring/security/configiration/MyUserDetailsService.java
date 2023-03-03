package com.kushanthsliit.spring.security.configiration;

import com.kushanthsliit.spring.security.entity.User;
import com.kushanthsliit.spring.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findByUsername(username);
       if(user == null){
           throw new UsernameNotFoundException("User not Found : ".concat(username));
       }
       return new MyUserDetails(user);

//        return new User("admin", "password", new ArrayList<>());
    }
}
