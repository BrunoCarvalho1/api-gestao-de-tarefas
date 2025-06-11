package api_gestao_de_tarefas.dto.task;

import java.time.LocalDate;

public class TaskDTO {

   private String description;
   private LocalDate dueDate;

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
