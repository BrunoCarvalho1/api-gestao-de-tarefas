package api_gestao_de_tarefas.controller;

import api_gestao_de_tarefas.dto.auth.LoginRequestDTO;
import api_gestao_de_tarefas.dto.auth.RegisterRequestDTO;
import api_gestao_de_tarefas.entity.User.LoginResponseDTO;
import api_gestao_de_tarefas.entity.User.User;
import api_gestao_de_tarefas.entity.User.UserRole;
import api_gestao_de_tarefas.repository.UserRepository;
import api_gestao_de_tarefas.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

   @Autowired
   private AuthenticationManager authenticationManager;

   @Autowired
   private UserRepository repository;

   @Autowired
   private JwtService tokenService;

   @PostMapping("/login")
   public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO dto) {
      var usernamePassword = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
      var auth = this.authenticationManager.authenticate(usernamePassword);

      var token = tokenService.generateToken((User) auth.getPrincipal());

      return ResponseEntity.ok(new LoginResponseDTO(token));
   }

   @PostMapping("/register")
   public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO dto) {
      if(this.repository.findByUsername(dto.getUsername()) != null){
         return ResponseEntity.badRequest().body("Username já existe.");
      }

      String encryptedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());

      UserRole roleEnum = UserRole.valueOf(dto.getRole().toUpperCase());
      User newUser = new User(dto.getUsername(), encryptedPassword, dto.getEmail(), roleEnum);

      this.repository.save(newUser);
      return ResponseEntity.ok().build();
   }
}