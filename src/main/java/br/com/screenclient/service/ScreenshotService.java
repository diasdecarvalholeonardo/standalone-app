package br.com.screenclient.service;

import br.com.screenclient.network.FileSender;
import br.com.screenclient.utils.ScreenCapture;
import java.io.File;

public class ScreenshotService {

    public static void captureAndSend() {
        try {
            String fileName = "screenshot_" + System.currentTimeMillis() + ".png";
            File screenshot = ScreenCapture.captureScreen(fileName);
            FileSender.sendFile(screenshot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
