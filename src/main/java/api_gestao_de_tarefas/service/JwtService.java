package api_gestao_de_tarefas.service;

import api_gestao_de_tarefas.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
   // Chave secreta (mínimo 256 bits para HS256)
   private static final String SECRET_KEY = "dc54d7757c9892db3bfb11b85cb57836f54c7a93a70264f671515619b25bfb3a";

   // Tempo de expiração: 24 horas
   private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

   private Key getSignKey() {
      return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
   }

   public String generateToken(User user) {
      return Jwts.builder()
              .setSubject(user.getUsername())
              .setIssuedAt(new Date(System.currentTimeMillis()))
              .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
              .signWith(getSignKey())
              .compact();
   }

   public String extractUsername(String token) {
      return extractClaim(token, Claims::getSubject);
   }

   public boolean isTokenValid(String token, User user) {
      final String username = extractUsername(token);
      return (username.equals(user.getUsername()) && !isTokenExpired(token));
   }

   private boolean isTokenExpired(String token) {
      return extractExpiration(token).before(new Date());
   }

   private Date extractExpiration(String token) {
      return extractClaim(token, Claims::getExpiration);
   }

   private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
      final Claims claims = Jwts
              .parserBuilder()
              .setSigningKey(getSignKey())
              .build()
              .parseClaimsJws(token)
              .getBody();
      return claimsResolver.apply(claims);
   }

}
