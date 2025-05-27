package api_gestao_de_tarefas.security;

import api_gestao_de_tarefas.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    // Gera uma chave segura automaticamente
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String gerarToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername()) // ou getEmail(), se preferir
                .claim("email", user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extrairUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private boolean isTokenExpirado(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extrairUsername(token);
        return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpirado(token));
    }
}
