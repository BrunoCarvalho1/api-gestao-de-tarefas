package api_gestao_de_tarefas.service;

import api_gestao_de_tarefas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class UserService implements UserDetailsService {

   @Autowired(required = true)
   private UserRepository repository;

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      return repository.findByUsername(username);
   }
}
