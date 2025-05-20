package com.standalone.screensender.util;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class NetworkScanner {

    /**
     * Obtém a sub-rede do IP local (ex: "192.168.0.")
     */
    public static String getSubnet() {
        try {
            InetAddress localAddress = InetAddress.getLocalHost();
            String ip = localAddress.getHostAddress(); // ex: 192.168.0.15
            return ip.substring(0, ip.lastIndexOf('.') + 1); // ex: 192.168.0.
        } catch (UnknownHostException e) {
            System.err.println("❌ Não foi possível obter o IP local.");
            return "192.168.0."; // fallback
        }
    }

    /**
     * Escaneia IPs da sub-rede verificando a conexão com a porta especificada.
     */
    public static List<String> scanActiveHosts(String subnet, int port, int timeoutMillis) {
        List<String> activeHosts = new ArrayList<>();

        for (int i = 1; i < 255; i++) {
            String host = subnet + i;
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), timeoutMillis);
                activeHosts.add(host);
                System.out.println("🟢 Dispositivo com porta " + port + " aberta: " + host);
            } catch (IOException ignored) {
                // IP não responde na porta especificada
            }
        }

        return activeHosts;
    }

    /**
     * Faz um "ping" em todos os IPs da sub-rede.
     */
    public static List<String> pingHosts(String subnet, int timeoutMillis) {
        List<String> reachableHosts = new ArrayList<>();

        for (int i = 1; i < 255; i++) {
            String host = subnet + i;
            try {
                InetAddress address = InetAddress.getByName(host);
                if (address.isReachable(timeoutMillis)) {
                    reachableHosts.add(host);
                    System.out.println("✅ IP alcançável (ping): " + host);
                }
            } catch (IOException ignored) {
            }
        }

        return reachableHosts;
    }

    /**
     * Detecta automaticamente a sub-rede e retorna hosts ativos com "ping".
     */
    public static List<String> discoverReachableHosts(int timeoutMillis) {
        String subnet = getSubnet();
        System.out.println("🌐 Sub-rede detectada: " + subnet);
        return pingHosts(subnet, timeoutMillis);
    }

    /**
     * Detecta automaticamente a sub-rede e escaneia IPs com porta específica.
     */
    public static List<String> discoverHostsByPort(int port, int timeoutMillis) {
        String subnet = getSubnet();
        System.out.println("🌐 Sub-rede detectada: " + subnet);
        return scanActiveHosts(subnet, port, timeoutMillis);
    }
}
