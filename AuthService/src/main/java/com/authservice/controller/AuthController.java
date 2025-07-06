package com.authservice.controller;

import com.authservice.model.User;
import com.authservice.repository.AuthRepo;
import com.authservice.service.AuthService;
import com.authservice.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;



      @PostMapping("/register")
      public User register(@Valid @RequestBody User user) {

          return authService.register(user);
      }

      @PostMapping("/login")
      public ResponseEntity<?> login(@Valid @RequestBody User user) {

          String token = authService.verify(user);
          return ResponseEntity.ok(Map.of("token", token));

      }






    @GetMapping("/view")
    public String view()
    {
        return "Hello";
    }

    @GetMapping("/admin-only")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOnly() {
        return "Admin access only";
    }

    @GetMapping("/user-access")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String userAccess() {
        return "User and Admin access";
    }
}

