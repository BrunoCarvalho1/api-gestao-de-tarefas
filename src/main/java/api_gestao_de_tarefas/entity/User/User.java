package api_gestao_de_tarefas.entity.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity(name = "users")
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails{

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String username;
   private String password;
   private String email;
   private UserRole role;

   public User(String username, String password, String email, UserRole role) {
      this.username = username;
      this.password = password;
      this.role = role;
      this.email = email;
   }
   // Getters e setters

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities(){
      if(this.role == UserRole.ADMIN)return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
      else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
   }
   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }
}
