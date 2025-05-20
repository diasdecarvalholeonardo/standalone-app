package com.standalone;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class NetworkScanner {

    private static final int TIMEOUT_MS = 200;
    private static final int MAX_THREADS = 50;

    // Obtém a base do IP local automaticamente, ex: "192.168.1."
    public static String getIpBase() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            return ip.substring(0, ip.lastIndexOf('.') + 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Verifica se determinado IP está ativo na porta informada
    public static boolean isHostAtivo(String ip, int porta) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, porta), TIMEOUT_MS);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Escaneia e retorna todos os IPs ativos com a porta aberta (em paralelo)
    public static List<String> buscarHostsAtivos(int porta) {
        List<String> hostsAtivos = Collections.synchronizedList(new ArrayList<>());
        String ipBase = getIpBase();

        if (ipBase == null) {
            System.out.println("Não foi possível obter a base do IP local.");
            return hostsAtivos;
        }

        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
        List<Future<?>> tarefas = new ArrayList<>();

        for (int i = 1; i < 255; i++) {
            final String ip = ipBase + i;
            tarefas.add(executor.submit(() -> {
                if (isHostAtivo(ip, porta)) {
                    System.out.println("Dispositivo encontrado: " + ip + " (porta " + porta + " aberta)");
                    hostsAtivos.add(ip);
                }
            }));
        }

        // Aguarda todas as tarefas finalizarem
        for (Future<?> tarefa : tarefas) {
            try {
                tarefa.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();

        return hostsAtivos;
    }

    // Teste independente da classe
    public static void main(String[] args) {
        int porta = 5000;
        List<String> ativos = buscarHostsAtivos(porta);
        if (ativos.isEmpty()) {
            System.out.println("Nenhum dispositivo ativo encontrado na porta " + porta + ".");
        } else {
            System.out.println("\nDispositivos disponíveis:");
            ativos.forEach(System.out::println);
        }
    }
}
