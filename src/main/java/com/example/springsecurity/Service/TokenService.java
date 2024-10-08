package com.example.springsecurity.Service;

import com.example.springsecurity.model.Token;
import com.example.springsecurity.repository.TokenRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record TokenService(TokenRepository tokenRepository) {

    public int save(Token token) {
        Optional<Token> optional = tokenRepository.findByUsername(token.getUsername());
        if(optional.isEmpty()) {
            tokenRepository.save(token);
            return token.getId();
        } else {
            Token currToken = optional.get();
            currToken.setAccess_token(token.getAccess_token());
            currToken.setRefresh_token(token.getRefresh_token());
            tokenRepository.save(currToken);
            return currToken.getId();
        }
    }

    public String delete(Token token) {
        tokenRepository.delete(token);
        return "Deleted!";
    }

    public Token getTokenByUsername(String username) {
        return tokenRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Token not exit"));
    }
}
