package api_gestao_de_tarefas.controller;

import api_gestao_de_tarefas.entity.Project;
import api_gestao_de_tarefas.entity.Task;
import api_gestao_de_tarefas.repository.ProjectRepository;
import api_gestao_de_tarefas.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

   @Autowired
   private TaskRepository taskRepository;

   @Autowired
   private ProjectRepository projectRepository;

   // Criar nova tarefa associada a um projeto
   @PostMapping
   public ResponseEntity<?> createTask(@RequestBody Task task) {
      // Verifica se o projeto existe antes de salvar a tarefa
      Optional<Project> projectOpt = projectRepository.findById(task.getProject().getId());
      if (projectOpt.isEmpty()) {
         return ResponseEntity.badRequest().body("Projeto não encontrado");
      }

      task.setProject(projectOpt.get());
      Task savedTask = taskRepository.save(task);
      return ResponseEntity.ok(savedTask);
   }

   // Buscar todas as tarefas
   @GetMapping
   public List<Task> getAllTasks() {
      return taskRepository.findAll();
   }

   // Buscar tarefa por ID
   @GetMapping("/{id}")
   public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
      return taskRepository.findById(id)
              .map(ResponseEntity::ok)
              .orElse(ResponseEntity.notFound().build());
   }

   // Atualizar tarefa
   @PutMapping("/{id}")
   public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
      Optional<Task> existingTaskOpt = taskRepository.findById(id);
      if (existingTaskOpt.isEmpty()) {
         return ResponseEntity.notFound().build();
      }

      Task existingTask = existingTaskOpt.get();
      existingTask.setTitle(taskDetails.getTitle());
      existingTask.setDescription(taskDetails.getDescription());
      existingTask.setCompleted(taskDetails.isCompleted());

      // Atualiza projeto, se necessário
      if (taskDetails.getProject() != null) {
         Optional<Project> newProject = projectRepository.findById(taskDetails.getProject().getId());
         newProject.ifPresent(existingTask::setProject);
      }

      Task updatedTask = taskRepository.save(existingTask);
      return ResponseEntity.ok(updatedTask);
   }

   // Deletar tarefa
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
      if (taskRepository.existsById(id)) {
         taskRepository.deleteById(id);
         return ResponseEntity.noContent().build();
      }
      return ResponseEntity.notFound().build();
   }
}

