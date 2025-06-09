package api_gestao_de_tarefas.service;

import api_gestao_de_tarefas.dto.auth.RegisterRequestDTO;
import api_gestao_de_tarefas.entity.User.User;
import api_gestao_de_tarefas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

   @Autowired
   private final UserRepository repository;
   private final PasswordEncoder passwordEncoder;

   public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
      this.repository = userRepository;
      this.passwordEncoder = passwordEncoder;
   }

   public User register(RegisterRequestDTO dto) {
      if (repository.existsByUsername(dto.getUsername())) {
         throw new RuntimeException("Usuário já existe!");
      }
      User user = new User();

      user.setUsername(dto.getUsername());
      user.setPassword(passwordEncoder.encode(dto.getPassword()));
      user.setEmail((dto.getEmail()));
      repository.save(user);
      return user;
   }

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      return repository.findByUsername(username);
   }
}