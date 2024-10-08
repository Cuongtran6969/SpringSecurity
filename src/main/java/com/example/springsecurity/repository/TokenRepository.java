package com.example.springsecurity.repository;

import com.example.springsecurity.dto.response.TokenResponse;
import com.example.springsecurity.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
  Optional<Token> findByUsername(String username);
}
