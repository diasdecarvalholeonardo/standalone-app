package com.standalone;

import java.awt.Desktop;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScreenReceiver {
    public static void main(String[] args) {
        int port = 5000; // Porta onde o cliente vai escutar
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Aguardando envio do servidor...");

            while (true) {
                Socket socket = serverSocket.accept();
                DataInputStream dis = new DataInputStream(socket.getInputStream());

                int length = dis.readInt();
                byte[] imageBytes = new byte[length];
                dis.readFully(imageBytes);

                String fileName = "imagem_recebida_" + System.currentTimeMillis() + ".jpg";
                Files.write(Paths.get(fileName), imageBytes);
                System.out.println("Imagem recebida e salva como " + fileName);
                System.out.println("Imagem recebida e salva com sucesso!");

                // Abrir a imagem automaticamente
                File imageFile = new File(fileName);
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(imageFile);
                }

                dis.close();
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
