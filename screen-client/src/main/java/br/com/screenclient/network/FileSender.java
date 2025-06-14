package br.com.screenclient.network;

import java.io.*;
import java.net.Socket;

public class FileSender {

    private static final String SERVER_IP = "192.168.1.186"; // Altere conforme necessário
    private static final int SERVER_PORT = 5000;

    public static void sendFile(File file) throws IOException {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             OutputStream out = socket.getOutputStream();
             DataOutputStream dos = new DataOutputStream(out);
             FileInputStream fis = new FileInputStream(file)) {

            dos.writeUTF(file.getName());
            dos.writeLong(file.length());

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }
        }
    }
}

