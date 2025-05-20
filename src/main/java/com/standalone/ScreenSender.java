package com.standalone;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ScreenSender {

    private static final String SERVIDOR_HOST = "localhost";
    private static final int SERVIDOR_PORTA = 5000;
    private static final int MAX_TENTATIVAS = 5;
    private static final int INTERVALO_TENTATIVA_MS = 3000;
    private static final int INTERVALO_ENVIO_MS = 2000;

    private static final File PASTA_SALVAMENTO = new File("C:\\SAAS\\standalone-app\\Imagem_captada");
    private static final File PASTA_BACKUP = new File(PASTA_SALVAMENTO, "backup");

    private static final int LIMITE_IMAGENS = 50;
    private static final int LIMITE_DIAS = 3; // dias

    public static void main(String[] args) {
        if (!PASTA_SALVAMENTO.exists()) {
            boolean criada = PASTA_SALVAMENTO.mkdirs();
            if (criada) {
                System.out.println("📁 Pasta de salvamento criada: " + PASTA_SALVAMENTO.getAbsolutePath());
            } else {
                System.err.println("❌ Falha ao criar pasta de salvamento.");
                return;
            }
        }

        if (!PASTA_BACKUP.exists()) {
            boolean criadaBackup = PASTA_BACKUP.mkdirs();
            if (criadaBackup) {
                System.out.println("📁 Pasta de backup criada: " + PASTA_BACKUP.getAbsolutePath());
            } else {
                System.err.println("❌ Falha ao criar pasta de backup.");
                return;
            }
        }

        while (true) {
            try (Socket socket = conectarAoServidor();
                 OutputStream outputStream = socket.getOutputStream();
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {

                System.out.println("✅ Conectado. Iniciando envio contínuo...");

                while (true) {
                    BufferedImage screenshot = capturarTela();

                    // 1️⃣ Salva localmente
                    File arquivoImagem = gerarArquivoImagem();
                    ImageIO.write(screenshot, "png", arquivoImagem);
                    System.out.println("📂 Imagem salva em: " + arquivoImagem.getAbsolutePath());

                    // 2️⃣ Limpa imagens antigas (por número e dias)
                    limparImagensAntigas();

                    // 3️⃣ Envia via socket
                    String imagemBase64 = converterParaBase64(screenshot);
                    objectOutputStream.writeObject(imagemBase64);
                    objectOutputStream.flush();
                    System.out.println("📤 Imagem enviada com sucesso.");

                    Thread.sleep(INTERVALO_ENVIO_MS);
                }

            } catch (IOException | AWTException | InterruptedException e) {
                System.err.println("⚠️ Erro: " + e.getMessage());
                System.err.println("🔄 Tentando reconectar em 3 segundos...");
                dormir(INTERVALO_TENTATIVA_MS);
            }
        }
    }

    private static Socket conectarAoServidor() throws IOException {
        int tentativas = 0;
        while (tentativas < MAX_TENTATIVAS) {
            try {
                Socket socket = new Socket(SERVIDOR_HOST, SERVIDOR_PORTA);
                System.out.println("🔌 Conectado ao servidor.");
                return socket;
            } catch (IOException e) {
                tentativas++;
                System.err.println("❌ Tentativa " + tentativas + " falhou.");
                dormir(INTERVALO_TENTATIVA_MS);
            }
        }
        throw new IOException("❗ Não foi possível conectar após " + MAX_TENTATIVAS + " tentativas.");
    }

    private static BufferedImage capturarTela() throws AWTException {
        Robot robot = new Robot();
        Rectangle tela = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        return robot.createScreenCapture(tela);
    }

    private static String converterParaBase64(BufferedImage imagem) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(imagem, "jpg", baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    private static void dormir(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private static File gerarArquivoImagem() {
        File[] arquivos = PASTA_SALVAMENTO.listFiles((dir, name) -> name.matches("screenshot_\\d+\\.png"));
        int proximoNumero = 1;

        if (arquivos != null) {
            Pattern pattern = Pattern.compile("screenshot_(\\d+)\\.png");
            for (File f : arquivos) {
                Matcher matcher = pattern.matcher(f.getName());
                if (matcher.matches()) {
                    int num = Integer.parseInt(matcher.group(1));
                    if (num >= proximoNumero) {
                        proximoNumero = num + 1;
                    }
                }
            }
        }

        String nomeArquivo = String.format("screenshot_%03d.png", proximoNumero);
        return new File(PASTA_SALVAMENTO, nomeArquivo);
    }

    private static void limparImagensAntigas() {
        File[] arquivos = PASTA_SALVAMENTO.listFiles((dir, name) -> name.matches("screenshot_\\d+\\.png"));

        if (arquivos == null || arquivos.length == 0) return;

        List<File> imagens = new ArrayList<>(Arrays.asList(arquivos));
        imagens.sort(Comparator.comparingLong(File::lastModified));

        // 1️⃣ Limite de número de imagens
        while (imagens.size() > LIMITE_IMAGENS) {
            File maisAntigo = imagens.remove(0);
            moverParaBackupEExcluir(maisAntigo, "quantidade");
        }

        // 2️⃣ Limite por data
        long agora = System.currentTimeMillis();
        long limiteMillis = LIMITE_DIAS * 24L * 60L * 60L * 1000L;

        for (File imagem : imagens) {
            long idade = agora - imagem.lastModified();
            if (idade > limiteMillis) {
                moverParaBackupEExcluir(imagem, "tempo (dias: " + (idade / 1000 / 60 / 60 / 24) + ")");
            }
        }
    }

    private static void moverParaBackupEExcluir(File imagem, String motivo) {
        try {
            File destino = new File(PASTA_BACKUP, imagem.getName());
            Files.move(imagem.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("📁 Backup criado para imagem " + imagem.getName() + " (motivo: " + motivo + ")");
            boolean deletado = destino.delete();
            if (deletado) {
                System.out.println("🧹 Backup deletado após movimentação segura: " + destino.getName());
            } else {
                System.err.println("⚠️ Imagem movida mas não deletada: " + destino.getName());
            }
        } catch (IOException e) {
            System.err.println("❌ Erro ao mover para backup: " + imagem.getName() + " -> " + e.getMessage());
        }
    }
}
