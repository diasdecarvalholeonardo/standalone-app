package com.standalone.screensender.receiver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServidorReceptor {

    private static final int PORT = 5000;
    private static final String DESTINATION_FOLDER = "C:/SAAS/standalone-app/Imagem_recebida";

    public static void main(String[] args) {
        System.out.println("🖥️ Servidor aguardando conexões na porta " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            // Garante que a pasta existe
            Path path = Paths.get(DESTINATION_FOLDER);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("📁 Pasta criada: " + path.toAbsolutePath());
            }

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("✅ Conexão recebida de: " + clientSocket.getInetAddress());

                // Thread para tratar cada cliente
                new Thread(() -> salvarImagem(clientSocket)).start();
            }

        } catch (IOException e) {
            System.err.println("❌ Erro ao iniciar o servidor: " + e.getMessage());
        }
    }

    private static void salvarImagem(Socket clientSocket) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "screenshot_recebida_" + timestamp + ".png";
        File destino = new File(DESTINATION_FOLDER, filename);

        try (InputStream is = clientSocket.getInputStream();
             FileOutputStream fos = new FileOutputStream(destino)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) > 0) {
                fos.write(buffer, 0, bytesRead);
            }

            System.out.println("📸 Imagem salva com sucesso em: " + destino.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("❌ Erro ao salvar imagem: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("⚠️ Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
