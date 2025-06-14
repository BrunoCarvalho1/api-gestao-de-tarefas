package api_gestao_de_tarefas.repository;

import api_gestao_de_tarefas.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
   List<Task> findByProjectId(Long projectId);
}
