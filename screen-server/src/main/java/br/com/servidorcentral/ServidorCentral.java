package br.com.servidorcentral;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorCentral {
    private static final int PORTA = 5000;

    public static void main(String[] args) {
        System.out.println("Servidor Central iniciado... Escutando na porta " + PORTA);

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nova conexão de: " + clientSocket.getInetAddress());

                new Thread(new FileReceiver(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
