package br.com.screenclient.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ScreenCapture {

    public static File captureScreen(String fileName) throws Exception {
        Robot robot = new Robot();
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenFullImage = robot.createScreenCapture(screenRect);

        File outputfile = new File(fileName);
        ImageIO.write(screenFullImage, "png", outputfile);

        System.out.println("Imagem capturada: " + outputfile.getAbsolutePath());
        return outputfile;
    }
}
