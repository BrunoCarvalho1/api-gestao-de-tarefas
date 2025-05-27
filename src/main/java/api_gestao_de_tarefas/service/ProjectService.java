package api_gestao_de_tarefas.service;

import api_gestao_de_tarefas.entity.Project;
import api_gestao_de_tarefas.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    public Project createProject(Project project){
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Project updateProject(Long id, Project updatedProject) {
        return projectRepository.findById(id)
                .map(project -> {
                    project.setName(updatedProject.getName());
                    return projectRepository.save(project);
                })
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

}
