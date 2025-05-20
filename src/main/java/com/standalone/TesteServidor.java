package com.standalone;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TesteServidor {
    public static void main(String[] args) {
        int porta = 5000;

        try (ServerSocket servidor = new ServerSocket(porta)) {
            System.out.println("Servidor iniciado na porta " + porta);

            while (true) {
                try {
                    Socket cliente = servidor.accept();
                    System.out.println("Conexão recebida de: " + cliente.getInetAddress());

                    // Entrada de dados do cliente
                    InputStream inputStream = cliente.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);

                    // Lê o tamanho da imagem
                    int tamanhoImagem = dataInputStream.readInt();
                    byte[] imagemBytes = new byte[tamanhoImagem];
                    dataInputStream.readFully(imagemBytes);

                    // Gera nome com timestamp
                    String timestamp = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                    String nomeArquivo = "screenshot_" + timestamp + ".png";

                    // Salva imagem
                    try (FileOutputStream fos = new FileOutputStream(nomeArquivo)) {
                        fos.write(imagemBytes);
                        System.out.println("Imagem salva como '" + nomeArquivo + "'");
                    }

                    cliente.close();
                } catch (Exception e) {
                    System.err.println("Erro ao processar conexão: " + e.getMessage());
                    // Continua ouvindo sem encerrar o servidor
                }
            }

        } catch (Exception e) {
            System.err.println("Erro fatal no servidor: " + e.getMessage());
        }
    }
}
