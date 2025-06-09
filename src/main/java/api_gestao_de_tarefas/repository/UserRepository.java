package api_gestao_de_tarefas.repository;

import api_gestao_de_tarefas.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {

   boolean existsByUsername(String username);

   UserDetails findByUsername(String username);
}