package com.netgroup.event_registration_backend.controller;

import com.netgroup.event_registration_backend.dto.auth.LoginRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  @Value("${app.jwt.secret}")
  private String jwtSecret;

  @Value("${app.jwt.expiration-ms}")
  private long tokenExpiration;


  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest req) {

    try {
      var auth = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(req.username(), req.password())
      );
      SecurityContextHolder.getContext().setAuthentication(auth);
      String role = auth.getAuthorities().iterator().next().getAuthority();

      String token = Jwts.builder()
          .setSubject(req.username())
          .claim("role", role)
          .setIssuedAt(new Date())
          .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
          .signWith(
              Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)),
              SignatureAlgorithm.HS256
          )
          .compact();

      return ResponseEntity.ok(token);
    } catch (AuthenticationException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad credentials");
    }
  }
}
