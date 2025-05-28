package api_gestao_de_tarefas.security;

import api_gestao_de_tarefas.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

   // Sua chave original, que é forte o suficiente para HS384 ou HS256
   private static final String MY_EXISTING_BASE64_KEY = "TmcxWlZ1elhyZnR0bkdRZUxOZndwZ0lQWjZJb1YxWjJNZ2NObDhaY3ByN2xMMnh2M3I3PQ==";
   private SecretKey secretKeyInstance;

   @PostConstruct
   public void init() {
      byte[] keyBytes = Base64.getDecoder().decode(MY_EXISTING_BASE64_KEY);
      this.secretKeyInstance = Keys.hmacShaKeyFor(keyBytes);
      // Verificação opcional de tamanho para o algoritmo escolhido
      // Para HS384, precisamos de pelo menos 48 bytes. Sua chave tem 51 bytes.
      if (keyBytes.length * 8 < 384 && SignatureAlgorithm.HS384.getMinKeyLength() > keyBytes.length * 8) {
         System.err.println("Aviso: Chave pode ser curta para o algoritmo escolhido se não for HS384/HS256.");
      }
      System.out.println("Chave JWT inicializada para uso com HS384 (ou HS256).");
   }

   public String gerarToken(User user) {
      return Jwts.builder()
              .setSubject(user.getUsername())
              .claim("email", user.getEmail())
              .setIssuedAt(new Date())
              .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
              // Use HS384 ou HS256 aqui  <-- Você escolheu HS256
              .signWith(this.secretKeyInstance, SignatureAlgorithm.HS256) // CORRETO para HS256
              .compact();
   }

   public String extrairUsername(String token) {
      return Jwts.parserBuilder()
              .setSigningKey(this.secretKeyInstance)
              .build()
              .parseClaimsJws(token)
              .getBody()
              .getSubject();
   }

   private boolean isTokenExpirado(String token) {
      try {
         return Jwts.parserBuilder()
                 .setSigningKey(this.secretKeyInstance)
                 .build()
                 .parseClaimsJws(token)
                 .getBody()
                 .getExpiration()
                 .before(new Date());
      } catch (io.jsonwebtoken.ExpiredJwtException e) {
         return true;
      }
   }

   public boolean validateToken(String token, UserDetails userDetails) {
      try {
         final String username = extrairUsername(token);
         return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpirado(token));
      } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
         System.err.println("Erro ao validar token: " + e.getMessage());
         return false;
      }
   }
}