package api_gestao_de_tarefas.controller;

import api_gestao_de_tarefas.dto.auth.LoginRequestDTO;
import api_gestao_de_tarefas.dto.auth.RegisterRequestDTO;
import api_gestao_de_tarefas.entity.User.LoginResponseDTO;
import api_gestao_de_tarefas.entity.User.User;
import api_gestao_de_tarefas.repository.UserRepository;
import api_gestao_de_tarefas.service.AuthService;
import api_gestao_de_tarefas.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

   @Autowired
   private AuthenticationManager authenticationManager;
   private final AuthService authService;
   private UserRepository repository;
   private final JwtService jwtService;

   @Autowired
   public AuthController(AuthService authService, JwtService jwtService) {
      this.authService = authService;
      this.jwtService = jwtService;
   }

   @PostMapping("/register")
   public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO dto) {
      if(this.repository.findByUsername(dto.getUsername()) != null) return ResponseEntity.badRequest().build();

      String encryptedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());
      User newUser = new User(dto.getUsername(), encryptedPassword, dto.role());

      this.repository.save(newUser);

      return ResponseEntity.ok().build();
   }

   @PostMapping("/login")
   public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequest) {
      var usernamePassword = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
      var auth = this.authenticationManager.authenticate(usernamePassword);

      var token = jwtService.generateToken((User) auth.getPrincipal());

      return ResponseEntity.ok(new LoginResponseDTO(token));
   }

}