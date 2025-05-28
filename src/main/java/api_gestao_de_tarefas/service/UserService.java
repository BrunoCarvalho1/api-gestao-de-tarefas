package api_gestao_de_tarefas.service;

import api_gestao_de_tarefas.entity.User;
import api_gestao_de_tarefas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

   @Autowired
   private UserRepository repository;

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = repository.findByUsername(username)
              .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

      // Retorna um UserDetails com username, senha e authorities (vazio se não tiver roles por enquanto)
      return new org.springframework.security.core.userdetails.User(
              user.getUsername(),
              user.getPassword(),
              Collections.emptyList() // ou coloque roles se tiver
      );
   }
}
