package br.com.screenclient;

import br.com.screenclient.keylistener.GlobalKeyListener;

public class Main {
    public static void main(String[] args) {
        GlobalKeyListener.start();
        System.out.println("ScreenClient iniciado e aguardando hotkey...");
    }
}



