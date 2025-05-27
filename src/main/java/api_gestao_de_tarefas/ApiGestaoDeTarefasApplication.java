package api_gestao_de_tarefas; // Certifique-se de que o pacote está correto

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Base64; // Import necessário para HexToBase64Converter se for mantido como classe interna

@SpringBootApplication
public class ApiGestaoDeTarefasApplication {

	// Método principal para iniciar a aplicação Spring Boot
	public static void main(String[] args) {
		SpringApplication.run(ApiGestaoDeTarefasApplication.class, args);
	}

	// Classe utilitária para conversão de Hex para Base64
	// Pode ser mantida aqui ou em um arquivo separado.
	// Seu método main é para executar a conversão, não para iniciar a app Spring Boot.
	public static class HexToBase64Converter {

		public static byte[] hexStringToByteArray(String hex) {
			int len = hex.length();
			byte[] data = new byte[len / 2];
			for (int i = 0; i < len; i += 2) {
				data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
						+ Character.digit(hex.charAt(i+1), 16));
			}
			return data;
		}

		// Método main para executar a conversão da chave
		public static void main(String[] args) {
			String hexSecret = "e3ea5db7fbff53a94cd026de4cbf39fe45999dc0dae5000456a7672ec4cfa23b370e52fa77db8fe3d72e2e6665215443cd895fd21e389e91bcc540bc1e8a43df";

			try {
				byte[] secretBytes = hexStringToByteArray(hexSecret);
				String base64Secret = Base64.getEncoder().encodeToString(secretBytes);

				System.out.println("Hex Original: " + hexSecret);
				System.out.println("Bytes Decodificados (comprimento): " + secretBytes.length);
				System.out.println("Chave Secreta em Base64 para usar em application.properties: " + base64Secret);

				if (secretBytes.length < 64) {
					System.err.println("Atenção: A chave resultante tem menos de 64 bytes, o que é curto para HS512.");
				}

			} catch (Exception e) {
				System.err.println("Erro durante a conversão: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}