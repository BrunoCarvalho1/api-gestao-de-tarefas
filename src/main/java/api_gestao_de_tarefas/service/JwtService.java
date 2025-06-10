package api_gestao_de_tarefas.service;

import api_gestao_de_tarefas.entity.User.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

   @Value("{api.security.token.security}")
   private String secret;

   public String generateToken(User user){
      try{
         Algorithm algorithm = Algorithm.HMAC256(secret);
         String token = JWT.create()
                 .withIssuer("auth-api")
                 .withSubject(user.getUsername())
                 .withExpiresAt(genExpirantionDate())
                 .sign(algorithm);
         return token;
      } catch (JWTCreationException exception){
         throw new RuntimeException("Error while generating token", exception);
      }
   }

   public String validateToken(String token){
      try{
         Algorithm algorithm = Algorithm.HMAC256(secret);
         return JWT.require(algorithm)
                 .withIssuer("auth-api")
                 .build()
                 .verify(token)
                 .getSubject();
      } catch (JWTVerificationException exception){
         return "";
      }
   }

   private Instant genExpirantionDate(){
      return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
   }
}
