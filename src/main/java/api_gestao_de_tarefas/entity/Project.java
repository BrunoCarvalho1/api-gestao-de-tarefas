package api_gestao_de_tarefas.entity;

import api_gestao_de_tarefas.entity.User.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false)
   private String name;

   @ManyToOne // Define a relação: Muitos Projetos para Um Usuário
   @JoinColumn(name = "owner_id", nullable = false) // Define a coluna da chave estrangeira no banco
   private User owner;

   // Relacionamento com tasks
   @JsonManagedReference
   @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<Task> tasks;

   // Getters e Setters

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public List<Task> getTasks() {
      return tasks;
   }

   public void setTasks(List<Task> tasks) {
      this.tasks = tasks;
   }

   public User getOwner() {
      return owner;
   }

   public void setOwner(User owner) {
      this.owner = owner;
   }
}
