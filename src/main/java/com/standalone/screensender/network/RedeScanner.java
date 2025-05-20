package com.standalone.screensender.network;

import java.io.IOException;
import java.net.InetAddress;

public class RedeScanner {

    public static void escanearDispositivosNaRede(String subnet) {
        System.out.println("🔍 Escaneando a rede: " + subnet + "0/24...");
        for (int i = 1; i < 255; i++) {
            String ip = subnet + i;
            try {
                InetAddress address = InetAddress.getByName(ip);
                if (address.isReachable(500)) {
                    System.out.println("✅ Dispositivo encontrado: " + ip + " (" + address.getHostName() + ")");
                }
            } catch (IOException e) {
                System.err.println("Erro ao testar IP: " + ip);
            }
        }
        System.out.println("✔️ Escaneamento concluído.");
    }

    public static void main(String[] args) {
        // Exemplo para rede 192.168.0.*
        escanearDispositivosNaRede("192.168.0.");
    }
}
