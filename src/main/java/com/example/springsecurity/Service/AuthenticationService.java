package com.example.springsecurity.Service;

import com.example.springsecurity.dto.request.SignInRequest;
import com.example.springsecurity.dto.response.TokenResponse;
import com.example.springsecurity.model.User;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.util.TokenType;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public TokenResponse authenticate(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUserName(), signInRequest.getPassword()));

        var user = userRepository.findByUsername(signInRequest.getUserName()).orElseThrow(() ->
                new UsernameNotFoundException("Username or password incorrect"));

        String accessToken = jwtService.generateToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        //save token to db
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    public TokenResponse refresh(HttpServletRequest request) {
        String refreshToken = request.getHeader("x-token");
        if(StringUtils.isBlank(refreshToken)) {
            System.out.println("Token invalid");
        }

        //extract user from token

        final String userName = jwtService.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);

        //check have exist in database

        Optional<User> user = userRepository.findByUsername(userName);
        System.out.println("userId="+user.get().getId());

        if(!jwtService.isValid(refreshToken, TokenType.REFRESH_TOKEN, user.get())) {
            System.out.println("Token invalid");
        }
        String accessToken = jwtService.generateToken(user.get());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.get().getId())
                .build();
    }

    public String logout(HttpServletRequest request) {
        String refreshToken = request.getHeader("x-token");
        if(StringUtils.isBlank(refreshToken)) {
            System.out.println("Token invalid");
        }
    }

}
