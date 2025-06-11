package api_gestao_de_tarefas.entity.User;

public record RegisterDTO(String login, String password, UserRole role) {
}
