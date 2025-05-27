package api_gestao_de_tarefas.service;

import api_gestao_de_tarefas.dto.auth.RegisterRequestDTO;
import api_gestao_de_tarefas.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import api_gestao_de_tarefas.repository.UserRepository;

@Service
public class AuthService {
   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;

   public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
   }

   public User register(RegisterRequestDTO dto) {
      if(userRepository.existsByUsername(dto.getUsername())){
         throw new RuntimeException("Usuário já existe!");
      }
      User user = new User();

      user.setUsername(dto.getUsername());
      user.setPassword(passwordEncoder.encode(dto.getPassword()));
      user.setEmail((dto.getEmail()));
      userRepository.save(user);
      return user;
   }
}