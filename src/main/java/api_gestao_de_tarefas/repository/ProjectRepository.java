package api_gestao_de_tarefas.repository;

import api_gestao_de_tarefas.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
   // Você pode adicionar métodos personalizados se quiser, por exemplo:
   List<Project> findAll();

}
