package br.com.servidorcentral;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileReceiver implements Runnable {
    private Socket socket;
    private static final String DESTINO = "received_images"; // Nova pasta onde as imagens serão salvas

    public FileReceiver(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (InputStream in = socket.getInputStream();
             DataInputStream dis = new DataInputStream(in)) {

            // Recebe o nome do arquivo
            String nomeArquivo = dis.readUTF();
            System.out.println("Recebendo arquivo: " + nomeArquivo);

            // Recebe o tamanho
            long tamanho = dis.readLong();

            // Garante que a pasta de destino exista
            Path pastaDestino = Paths.get(DESTINO);
            if (!Files.exists(pastaDestino)) {
                Files.createDirectories(pastaDestino);
            }

            // Define o caminho final
            File outputfile = new File(DESTINO + File.separator + nomeArquivo);

            // Recebe os bytes do arquivo
            try (OutputStream out = new FileOutputStream(outputfile)) {
                byte[] buffer = new byte[4096];
                long bytesRecebidos = 0;
                int bytesLidos;

                while (bytesRecebidos < tamanho &&
                       (bytesLidos = dis.read(buffer, 0, (int)Math.min(buffer.length, tamanho - bytesRecebidos))) != -1) {
                    out.write(buffer, 0, bytesLidos);
                    bytesRecebidos += bytesLidos;
                }
            }

            System.out.println("Arquivo " + nomeArquivo + " recebido com sucesso em: " + outputfile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Erro ao receber arquivo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
