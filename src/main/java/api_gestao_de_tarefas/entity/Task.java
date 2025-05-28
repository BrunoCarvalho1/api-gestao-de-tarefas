package api_gestao_de_tarefas.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne
   @JoinColumn(name = "project_id", nullable = false)
   private Project project;

   @Column(nullable = false)
   private String title;

   @Column(nullable = false)
   private String description;

   @Column(nullable = false)
   private boolean completed;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Project getProject() {
      return project;
   }

   public void setProject(Project project) {
      this.project = project;
   }

   public String getTitle() {
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

   public boolean isCompleted() {
      return completed;
   }

   public void setCompleted(boolean completed) {
      this.completed = completed;
   }
}
