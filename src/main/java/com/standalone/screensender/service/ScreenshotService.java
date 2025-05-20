package com.standalone.screensender.service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotService {

    private static final String DESTINATION_IP = "192.168.1.204"; // ✅ Personalize se necessário
    private static final int DESTINATION_PORT = 5000;
    private static final String LOCAL_SAVE_PATH = "C:/SAAS/screenshots";
    private static final String AUDIT_SAVE_PATH = "C:/SAAS/standalone-app/Imagem_captada";

    // ✅ Método principal para execução direta
    public static void main(String[] args) {
        System.out.println("▶️ Iniciando captura e envio de screenshot...");
        captureAndSend();
        System.out.println("✅ Processo finalizado.");
    }

    public static void captureAndSend() {
        try {
            // 1. Captura a tela
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screen = new Robot().createScreenCapture(screenRect);

            // 2. Define nome com timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = "screenshot_" + timestamp + ".png";

            // 3. Salva localmente
            File localFile = new File(LOCAL_SAVE_PATH, filename);
            localFile.getParentFile().mkdirs();
            ImageIO.write(screen, "png", localFile);
            System.out.println("✅ Imagem salva localmente em: " + localFile.getAbsolutePath());

            // 4. Salva cópia na pasta de auditoria
            File auditFile = new File(AUDIT_SAVE_PATH, filename);
            auditFile.getParentFile().mkdirs();
            ImageIO.write(screen, "png", auditFile);
            System.out.println("📂 Cópia salva em: " + auditFile.getAbsolutePath());

            // 5. Envia imagem via socket
            sendImage(localFile, DESTINATION_IP, DESTINATION_PORT);

        } catch (Exception e) {
            System.err.println("❌ Erro na captura ou envio da tela: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void sendImage(File imageFile, String ip, int port) {
        try (Socket socket = new Socket(ip, port);
             OutputStream os = socket.getOutputStream();
             FileInputStream fis = new FileInputStream(imageFile)) {

            byte[] buffer = new byte[4096];
            int read;
            while ((read = fis.read(buffer)) > 0) {
                os.write(buffer, 0, read);
            }

            os.flush();
            System.out.println("📤 Imagem enviada com sucesso para " + ip + ":" + port);

        } catch (IOException e) {
            System.err.println("❌ Falha ao enviar imagem: " + e.getMessage());
        }
    }
}
