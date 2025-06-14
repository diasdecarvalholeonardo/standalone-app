package br.com.screenclient.capture;

import br.com.screenclient.network.FileSender;
import br.com.screenclient.selenium.SeleniumDriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FullPageCaptureService {

    public static void captureAndSend() {
        WebDriver driver = null;
        try {
            driver = SeleniumDriverFactory.createDriver();
            String url = "https://www.example.com"; // TODO: Substitua pela URL desejada
            driver.get(url);

            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File("fullpage_" + System.currentTimeMillis() + ".png");
            Files.copy(screenshot.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            FileSender.sendFile(destFile);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}

