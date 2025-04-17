/*
 * Arquivo desenvolvido por: Iago Macarini e Luis Henrique Mendes
 * lab1 - read e write
 * Data: 21/04/2025
 */

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Scanner;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Principal_v0 {

	public final static Path path = Paths.get("src/fortune-br.txt");
	private int NUM_FORTUNES = 0;

	public class FileReader {

		public int countFortunes() throws FileNotFoundException {
			int count = 0;
			try (BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(path.toFile()), StandardCharsets.UTF_8))) {

				String line;
				boolean isFortune = false;

				while ((line = br.readLine()) != null) {
					if (line.equals("%")) {
						count++; // Conta cada "%" como separador
						isFortune = false;
					} else if (!isFortune) {
						isFortune = true;
					}
				}

			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
			return count;
		}

		public void parser(HashMap<Integer, String> hm) throws FileNotFoundException {
			try (BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(path.toFile()), StandardCharsets.UTF_8))) {

				int lineCount = 0;
				StringBuilder fortune = new StringBuilder();
				String line;

				while ((line = br.readLine()) != null) {
					if (line.equals("%")) {
						if (fortune.length() > 0) {
							hm.put(lineCount, fortune.toString().trim());
							fortune.setLength(0); // Limpa o buffer
							lineCount++;
						}
					} else {
						fortune.append(line).append("\n");
					}
				}

				// Adiciona a última fortuna (se o arquivo não terminar com %)
				if (fortune.length() > 0) {
					hm.put(lineCount, fortune.toString().trim());
					lineCount++;
				}

				Principal_v0.this.NUM_FORTUNES = lineCount; // Atualiza o contador

			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
		}

		public void read(HashMap<Integer, String> hm) throws FileNotFoundException {
			if (hm.isEmpty()) {
				System.out.println("Nenhuma fortuna disponível.");
				return;
			}
			SecureRandom random = new SecureRandom();
			int randomIndex = random.nextInt(Principal_v0.this.NUM_FORTUNES); // Índice correto
			String fortune = hm.get(randomIndex);
			System.out.println("Fortuna aleatória:\n" + fortune);
		}

		public void write(HashMap<Integer, String> hm) throws FileNotFoundException {
			Scanner scanner = new Scanner(System.in, Charset.forName("UTF-8"));
			System.out.println("\nDigite a nova fortuna (pressione Enter duas vezes para finalizar):");

			StringBuilder novaFortuna = new StringBuilder();
			String linha;

			while (scanner.hasNextLine()) {
				linha = scanner.nextLine();
				if (linha.isEmpty()) {
					break;
				}
				novaFortuna.append(linha).append("\n");
			}

			try (BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(path.toFile(), true), StandardCharsets.UTF_8))) {
				writer.write("\n%\n" + novaFortuna.toString().trim() + "\n%"); // Adiciona a nova fortuna
				System.out.println("Fortuna adicionada com sucesso!");

			} catch (IOException e) {
				System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
			} finally {
				scanner.close();
			}
		}
	}

	public void iniciar() {

		FileReader fr = new FileReader();
		try {
			NUM_FORTUNES = fr.countFortunes();
			HashMap<Integer, String> hm = new HashMap<>();
			fr.parser(hm);
			fr.read(hm);
			fr.write(hm);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Principal_v0().iniciar();
	}
}