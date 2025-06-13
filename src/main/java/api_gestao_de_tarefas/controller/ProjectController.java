package api_gestao_de_tarefas.controller;

import api_gestao_de_tarefas.dto.project.ProjectDTO;
import api_gestao_de_tarefas.entity.Project;
import api_gestao_de_tarefas.dto.task.TaskDTO;
import api_gestao_de_tarefas.entity.Task;
import api_gestao_de_tarefas.entity.User.User;
import api_gestao_de_tarefas.repository.ProjectRepository;
import api_gestao_de_tarefas.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/projects")
public class ProjectController {

   @Autowired
   private ProjectRepository projectRepository;

   @Autowired
   private TaskRepository taskRepository;

   @PostMapping("/create")
   public ResponseEntity<Project> createProject(@RequestBody ProjectDTO projectDTO) {

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      User currentUser = (User) authentication.getPrincipal();

      Project newProject = new Project();
      newProject.setName(projectDTO.getName());
      newProject.setOwner(currentUser);

      Project savedProject = projectRepository.save(newProject);
      return ResponseEntity.ok(savedProject);
   }

   @GetMapping("/viewAll")
   public List<Project> getAllProjects() {
      return projectRepository.findAllWithTasks();
   }

   //Busca projeto por ID
   @GetMapping("/{id}")
   public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
      Optional<Project> project = projectRepository.findById(id);
      return project.map(ResponseEntity::ok)
              .orElse(ResponseEntity.notFound().build());
   }

   public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
      if (projectRepository.existsById(id)) {
         projectRepository.deleteById(id);
         return ResponseEntity.noContent().build();
      }
      return ResponseEntity.notFound().build();
   }

   @PostMapping("/{projectId}/tasks")
   public ResponseEntity<?> createTaskForProject(@PathVariable Long projectId, @RequestBody @Valid TaskDTO taskDTO, Authentication authentication){
      Optional<Project> optionalProject = projectRepository.findById(projectId);

      if(optionalProject.isEmpty()){
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado");
      }

      Project project = optionalProject.get();
      User currentUser = (User) authentication.getPrincipal();

      if(!project.getOwner().getId().equals(currentUser.getId())){
         return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para adicionar tarefas a este projeto.");
      }

      Task newTask = new Task();
      newTask.setTitle(TaskDTO.getTitle());
      newTask.setDescription(taskDTO.getDescription());
      newTask.setDueDate(taskDTO.getDueDate());
      newTask.setProject(project); // Associa a tarefa ao projeto correto
      newTask.setCompleted(false); // Define o status inicial

      Task savedTask = taskRepository.save(newTask);

      return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);

   }
}
