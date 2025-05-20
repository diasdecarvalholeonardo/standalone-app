//Classe: ServidorEnvio.java
package com.standalone;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class ServidorEnvio {
 public static void main(String[] args) {
     try {
         // 1. Captura da tela
         Robot robot = new Robot();
         Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
         BufferedImage screenImage = robot.createScreenCapture(screenRect);

         // 2. Converte a imagem em bytes
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         ImageIO.write(screenImage, "jpg", baos);
         byte[] imageBytes = baos.toByteArray();

         // 3. Envia para o cliente (IP fixo ou dinâmico conhecido)
         String clientIp = "192.168.0.101"; // IP do cliente
         int clientPort = 6000;

         try (Socket socket = new Socket(clientIp, clientPort);
              OutputStream os = socket.getOutputStream();
              DataOutputStream dos = new DataOutputStream(os)) {

             dos.writeInt(imageBytes.length);
             dos.write(imageBytes);
             dos.flush();
             System.out.println("Imagem enviada para o cliente com sucesso!");
         }

     } catch (Exception e) {
         e.printStackTrace();
     }
 }
}

