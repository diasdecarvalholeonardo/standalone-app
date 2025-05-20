package com.standalone;

import com.standalone.screensender.hotkey.HotkeyListener;

public class MainApplication {
    public static void main(String[] args) {
    	System.out.println("Aplicativo iniciado em segundo plano...");
        HotkeyListener.start();}
}
