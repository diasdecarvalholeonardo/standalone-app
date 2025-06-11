package br.com.screenclient.keylistener;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import br.com.screenclient.capture.FullPageCaptureService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GlobalKeyListener implements NativeKeyListener {

    private boolean ctrlPressed = false;
    private boolean altPressed = false;

    public static void start() {
        try {
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            logger.setUseParentHandlers(false);

            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
        } catch (NativeHookException e) {
            System.err.println("Erro ao registrar NativeHook: " + e.getMessage());
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
            ctrlPressed = true;
        } else if (e.getKeyCode() == NativeKeyEvent.VC_ALT) {
            altPressed = true;
        } else if (e.getKeyCode() == NativeKeyEvent.VC_L) {
            if (ctrlPressed && altPressed) {
                new Thread(() -> FullPageCaptureService.captureAndSend()).start();
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
            ctrlPressed = false;
        } else if (e.getKeyCode() == NativeKeyEvent.VC_ALT) {
            altPressed = false;
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        // Não utilizado
    }
}

