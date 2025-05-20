package com.standalone.screensender.service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.net.Socket;

public class ScreenSenderService {

    private final String servidorIP = "127.0.0.1"; // Substitua pelo IP real se necessário
    private final int servidorPorta = 5000;

    public void capturarEEnviarImagem() {
        try {
            // 1. Captura da tela
            Robot robot = new Robot();
            Rectangle tela = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage captura = robot.createScreenCapture(tela);

            // 2. Conexão com o servidor
            try (Socket socket = new Socket(servidorIP, servidorPorta);
                 OutputStream out = socket.getOutputStream()) {

                // 3. Envio da imagem em formato PNG
                ImageIO.write(captura, "png", out);

                System.out.println("✅ Imagem capturada e enviada com sucesso.");

            } catch (Exception e) {
                System.err.println("❌ Erro ao conectar com o servidor receptor: " + e.getMessage());
            }

        } catch (AWTException e) {
            System.err.println("❌ Erro ao capturar a tela: " + e.getMessage());
        }
    }
}

