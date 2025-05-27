package api_gestao_de_tarefas.controller;

import api_gestao_de_tarefas.dto.auth.LoginRequestDTO;
import api_gestao_de_tarefas.dto.auth.RegisterRequestDTO;
import api_gestao_de_tarefas.entity.User;
import api_gestao_de_tarefas.repository.UserRepository;
import api_gestao_de_tarefas.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import api_gestao_de_tarefas.service.AuthService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;
   private final AuthService authService;
   private final JwtService jwtService;

   public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthService authService, JwtService jwtService) {
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
      this.authService = authService;
      this.jwtService = jwtService;
   }

   @PostMapping("/register")
   public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO dto) {
      authService.register(dto); // Agora funciona!
      return ResponseEntity.status(HttpStatus.CREATED).build();
   }

   @PostMapping("/login")
   public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequest) {
      Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername());

      if (optionalUser.isEmpty()) {
         return ResponseEntity.status(401).body("Usuário não encontrado!");
      }

      User user = optionalUser.get();

      if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
         return ResponseEntity.status(401).body("Senha incorreta!");
      }

      // Gerar token JWT
      String token = jwtService.generateToken(user); // jwtService é injetado no controller

      // Retornar o token como JSON
      return ResponseEntity.ok(Map.of("token", token));
   }

}