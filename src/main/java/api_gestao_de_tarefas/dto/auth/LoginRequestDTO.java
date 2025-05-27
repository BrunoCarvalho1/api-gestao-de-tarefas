package api_gestao_de_tarefas.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {

   @NotBlank(message = "Username é obrigatório")
   private String username;

   @NotBlank(message = "Senha é obrigatória")
   private String password;

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
