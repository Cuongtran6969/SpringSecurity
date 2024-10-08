package com.example.springsecurity.Service;

import com.example.springsecurity.dto.request.ResetPasswordDTO;
import com.example.springsecurity.dto.request.SignInRequest;
import com.example.springsecurity.dto.response.TokenResponse;
import com.example.springsecurity.model.Token;
import com.example.springsecurity.model.User;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.util.TokenType;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final TokenService tokenService;

    private final UserService userService;

    public TokenResponse authenticate(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUserName(), signInRequest.getPassword()));

        var user = userRepository.findByUsername(signInRequest.getUserName()).orElseThrow(() ->
                new UsernameNotFoundException("Username or password incorrect"));

        String accessToken = jwtService.generateToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        //save token to db

        tokenService.save(
                Token.builder()
                        .username(user.getUsername())
                        .access_token(accessToken)
                        .refresh_token(refreshToken)
                        .build()
        );

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
        //extract user from token
        final String userName = jwtService.extractUsername(refreshToken, TokenType.ACCESS_TOKEN);
        //check token in database
        Token currenToken = tokenService.getTokenByUsername(userName);
        //delete token
        tokenService.delete(currenToken);
        return "Deleted!";
    }

    public String forgotPassword(String email) {
        //check email have exist
        User user = userService.findByEmail(email);


        //generate reset token
        String resetToken = jwtService.generateResetToken(user);

        //Send email confirm link
        String confirmLink = String.format("curl --location --request GET 'http://localhost:8080/auth/reset-password' \\\n" +
                "--header 'Content-Type: text/plain' \\\n" +
                "--data '%s'", resetToken);
        log.info("confirmLink={}", confirmLink);
        return "Have send!";
    }

    public String resetPassword(String secretKey) {
        log.info("-----------resetPassword");
        final String userName = jwtService.extractUsername(secretKey, TokenType.RESET_TOKEN);
        //check token in database
        Optional<User> user = userRepository.findByUsername(userName);
        if(!jwtService.isValid(secretKey, TokenType.RESET_TOKEN, user.get())) {
            System.out.println("Token invalid");
        }
        return "reset";
    }

    public String changePassword(ResetPasswordDTO request) {
        User user= isValidUserByToken(request.getSecretKey());
        if(!request.getPassword().equals(request.getConfirmPassword())) {
//            throw new InvalidDataException("Password not match");
        }
        ;
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.saveUser(user);
        return "success";
    }

    private User isValidUserByToken(String secretKey) {
        final String userName = jwtService.extractUsername(secretKey, TokenType.RESET_TOKEN);
        //check token in database
        Optional<User> user = userRepository.findByUsername(userName);
        //user have active
        if(!user.get().isEnabled()) {
//            throw new InvalidDataException("User not active");
        }

        if(!jwtService.isValid(secretKey, TokenType.RESET_TOKEN, user.get())) {
            System.out.println("Token invalid");
            //throw ex
        }
        return user.get();

    }
}
