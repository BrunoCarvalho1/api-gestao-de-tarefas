package api_gestao_de_tarefas.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequestDTO {

   @NotBlank(message = "Username é obrigatório")
   private String username;

   @NotBlank(message = "Senha é obrigatória")
   @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
   private String password;

   private String email;

   @NotBlank(message = "A role é obrigatória")
   private String role;

   public String getRole() {
      return role;
   }

   public void setRole(String role) {
      this.role = role;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
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

}
