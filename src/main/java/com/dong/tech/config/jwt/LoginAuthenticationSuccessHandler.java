//package com.dong.tech.config.jwt;
//
//import com.fasterxml.jackson.databind.util.JSONPObject;
//import io.jsonwebtoken.Jwt;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.AllArgsConstructor;
//import org.apache.el.parser.Token;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.nio.charset.StandardCharsets;
//
///**
// * The Class LoginAuthenticationSuccessHandler.
// *
// * @author dongsulee
// * @date 2023/10/06
// */
//@AllArgsConstructor
//public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//    private final TokenProvider tokenProvider;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        String token = tokenProvider.createToken(authentication);
//
//        response.addHeader("Authorization", token);
//
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
//
//        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
//    }
//}
