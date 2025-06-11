package br.com.screenclient.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SeleniumDriverFactory {

    public static WebDriver createDriver() {
        System.setProperty("webdriver.chrome.driver", "chromedriver"); // Ajuste o caminho se necessário

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Executa em modo headless
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        return new ChromeDriver(options);
    }
}

