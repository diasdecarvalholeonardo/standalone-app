package com.standalone.screensender.sender;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.net.Socket;

public class ScreenSenderService {

    private static final String SERVIDOR_IP = "127.0.0.1"; // ou IP da máquina receptora na LAN
    private static final int SERVIDOR_PORTA = 5000;

    public static void main(String[] args) {
        try {
            // Captura a tela inteira
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenshot = robot.createScreenCapture(screenRect);

            // Conecta ao servidor
            try (Socket socket = new Socket(SERVIDOR_IP, SERVIDOR_PORTA);
                 OutputStream out = socket.getOutputStream()) {

                // Envia a imagem em formato PNG
                ImageIO.write(screenshot, "png", out);
                out.flush();

                System.out.println("Imagem enviada com sucesso!");
            }
        } catch (Exception e) {
            System.err.println("Erro ao capturar ou enviar a tela: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
