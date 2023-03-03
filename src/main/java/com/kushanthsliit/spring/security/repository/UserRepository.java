package com.kushanthsliit.spring.security.repository;

import com.kushanthsliit.spring.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByPasswordResetToken(String token);

    User findByRefreshToken(String refreshToken);
}
