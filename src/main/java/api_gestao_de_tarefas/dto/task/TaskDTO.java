package api_gestao_de_tarefas.dto.task;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class TaskDTO {

   @NotBlank(message = "O título da tarefa é obrigatório.")
   private static String title;

   @NotBlank(message = "A descrição é obrigatória.")
   private String description;

   private LocalDate dueDate;

   public static String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public LocalDate getDueDate() {
      return dueDate;
   }

   public void setDueDate(LocalDate dueDate) {
      this.dueDate = dueDate;
   }
}
