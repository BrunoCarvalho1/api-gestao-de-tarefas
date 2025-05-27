package api_gestao_de_tarefas.security;

import api_gestao_de_tarefas.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value; // Para injetar de application.properties
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import jakarta.annotation.PostConstruct; // Para inicializar a chave
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret.base64}")
    private String jwtSecretBase64;

    private SecretKey secretKeyInstance;

    @PostConstruct
    public void init() {
        try {
            // Adicione esta linha para depuração:
            System.out.println("DEBUG: Valor carregado para jwt.secret.base64: '" + jwtSecretBase64 + "'");

            if (jwtSecretBase64 == null || jwtSecretBase64.trim().isEmpty()) {
                throw new IllegalArgumentException("A propriedade 'jwt.secret.base64' não está configurada ou está vazia no arquivo de propriedades.");
            }
            byte[] keyBytes = Base64.getDecoder().decode(jwtSecretBase64);
            if (keyBytes.length < 64) {
                throw new IllegalArgumentException(
                        String.format("A chave JWT configurada ('jwt.secret.base64') é muito curta para HS512. Esperado: >= 64 bytes, Obtido: %d bytes. Verifique sua propriedade.", keyBytes.length)
                );
            }
            this.secretKeyInstance = Keys.hmacShaKeyFor(keyBytes);
            System.out.println("Chave JWT inicializada com sucesso para HS512.");
        } catch (IllegalArgumentException e) {
            System.err.println("FALHA CRÍTICA AO INICIALIZAR A CHAVE JWT: " + e.getMessage());
            throw new RuntimeException("Erro crítico ao inicializar a chave JWT. A aplicação não pode continuar de forma segura. Causa: " + e.getMessage(), e);
        }
    }

    public String gerarToken(User user) {
        if (this.secretKeyInstance == null) {
            throw new IllegalStateException("A chave secreta JWT (secretKeyInstance) não foi inicializada. Verifique a configuração 'jwt.secret.base64'.");
        }
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("email", user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(this.secretKeyInstance, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extrairUsername(String token) {
        if(this.secretKeyInstance == null) {
            throw new IllegalStateException("A chave secreta JWT (secretKeyInstance) não foi inicializada. Verifique a configuração 'jwt.secret.base64'.");
        }
        return Jwts.parserBuilder()
                .setSigningKey(this.secretKeyInstance)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private boolean isTokenExpirado(String token) {
        if (this.secretKeyInstance == null) {
            throw new IllegalStateException("A chave secreta JWT (secretKeyInstance) não foi inicializada.");
        }
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
        if (this.secretKeyInstance == null) {
            System.err.println("Tentativa de validar token, mas a chave secreta JWT (secretKeyInstance) não foi inicializada.");
            return false;
        }
        try {
            final String username = extrairUsername(token);
            return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpirado(token));
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            System.err.println("Erro ao validar token: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return false;
        }
    }
}