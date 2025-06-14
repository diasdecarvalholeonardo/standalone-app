package br.com.servidorcentral;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSender {
    private static final String SERVER_IP = "127.0.0.1"; // Altere para IP do servidor se for outro dispositivo
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        // Caminho do arquivo que será enviado
        String caminhoArquivo = "src/main/resources/images/cats_icon_spy.jpg"; // Altere para o arquivo que quer enviar

        Path arquivo = Paths.get(caminhoArquivo);
        if (!Files.exists(arquivo)) {
            System.err.println("Arquivo não encontrado: " + caminhoArquivo);
            return;
        }

        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             OutputStream out = socket.getOutputStream();
             DataOutputStream dos = new DataOutputStream(out);
             InputStream in = new FileInputStream(arquivo.toFile())) {

            String nomeArquivo = arquivo.getFileName().toString();
            long tamanhoArquivo = Files.size(arquivo);

            // Envia o nome e o tamanho do arquivo
            dos.writeUTF(nomeArquivo);
            dos.writeLong(tamanhoArquivo);

            // Envia o conteúdo do arquivo
            byte[] buffer = new byte[4096];
            int bytesLidos;
            while ((bytesLidos = in.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesLidos);
            }

            System.out.println("Arquivo enviado com sucesso!");

        } catch (IOException e) {
            System.err.println("Erro ao enviar arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

