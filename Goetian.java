import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Arrays;

public class FileEncryptionTool extends JFrame {
    private JTextArea logArea;
    private JTextField filePathField;
    private JComboBox<String> algorithmCombo;
    private JPasswordField passwordField;
    private JTextField keyPathField;
    private JButton selectFileButton, encryptButton, decryptButton;
    private JButton generateKeyButton, loadKeyButton, saveKeyButton;
    private JProgressBar progressBar;
    
    // Chaves em memória
    private SecretKey aesKey;
    private KeyPair rsaKeyPair;
    
    public FileEncryptionTool() {
        initializeUI();
        setupEventListeners();
    }
    
    private void initializeUI() {
        setTitle("Ferramenta de Criptografia de Arquivos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Painel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Seleção de arquivo
        JPanel filePanel = createFileSelectionPanel();
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(filePanel, gbc);
        
        // Configurações de algoritmo
        JPanel algorithmPanel = createAlgorithmPanel();
        gbc.gridy = 1;
        mainPanel.add(algorithmPanel, gbc);
        
        // Gerenciamento de chaves
        JPanel keyPanel = createKeyManagementPanel();
        gbc.gridy = 2;
        mainPanel.add(keyPanel, gbc);
        
        // Botões de ação
        JPanel actionPanel = createActionPanel();
        gbc.gridy = 3;
        mainPanel.add(actionPanel, gbc);
        
        // Barra de progresso
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        gbc.gridy = 4;
        mainPanel.add(progressBar, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Área de log
        logArea = new JTextArea(10, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(new TitledBorder("Log de Operações"));
        add(scrollPane, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private JPanel createFileSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Seleção de Arquivo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Arquivo:"), gbc);
        
        filePathField = new JTextField(30);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(filePathField, gbc);
        
        selectFileButton = new JButton("Selecionar");
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(selectFileButton, gbc);
        
        return panel;
    }
    
    private JPanel createAlgorithmPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Configurações de Criptografia"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Algoritmo:"), gbc);
        
        algorithmCombo = new JComboBox<>(new String[]{"AES-256", "RSA-2048"});
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(algorithmCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Senha (AES):"), gbc);
        
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        
        return panel;
    }
    
    private JPanel createKeyManagementPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Gerenciamento de Chaves"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Caminho da chave:"), gbc);
        
        keyPathField = new JTextField(20);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(keyPathField, gbc);
        
        generateKeyButton = new JButton("Gerar Chave");
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(generateKeyButton, gbc);
        
        loadKeyButton = new JButton("Carregar");
        gbc.gridx = 3;
        panel.add(loadKeyButton, gbc);
        
        saveKeyButton = new JButton("Salvar");
        gbc.gridx = 4;
        panel.add(saveKeyButton, gbc);
        
        return panel;
    }
    
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(new TitledBorder("Ações"));
        
        encryptButton = new JButton("Criptografar");
        encryptButton.setBackground(new Color(46, 125, 50));
        encryptButton.setForeground(Color.WHITE);
        encryptButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        
        decryptButton = new JButton("Descriptografar");
        decryptButton.setBackground(new Color(211, 47, 47));
        decryptButton.setForeground(Color.WHITE);
        decryptButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        
        panel.add(encryptButton);
        panel.add(decryptButton);
        
        return panel;
    }
    
    private void setupEventListeners() {
        selectFileButton.addActionListener(e -> selectFile());
        generateKeyButton.addActionListener(e -> generateKey());
        loadKeyButton.addActionListener(e -> loadKey());
        saveKeyButton.addActionListener(e -> saveKey());
        encryptButton.addActionListener(e -> performEncryption());
        decryptButton.addActionListener(e -> performDecryption());
    }
    
    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            filePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            log("Arquivo selecionado: " + fileChooser.getSelectedFile().getName());
        }
    }
    
    private void generateKey() {
        try {
            String algorithm = (String) algorithmCombo.getSelectedItem();
            
            if (algorithm.startsWith("AES")) {
                // Gerar chave AES a partir da senha
                char[] password = passwordField.getPassword();
                if (password.length == 0) {
                    showError("Digite uma senha para gerar a chave AES");
                    return;
                }
                
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] keyBytes = digest.digest(new String(password).getBytes("UTF-8"));
                aesKey = new SecretKeySpec(keyBytes, "AES");
                
                log("Chave AES gerada com sucesso a partir da senha");
                Arrays.fill(password, '\0'); // Limpar senha da memória
                
            } else if (algorithm.startsWith("RSA")) {
                // Gerar par de chaves RSA
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
                keyGen.initialize(2048);
                rsaKeyPair = keyGen.generateKeyPair();
                
                log("Par de chaves RSA gerado com sucesso (2048 bits)");
            }
            
        } catch (Exception e) {
            showError("Erro ao gerar chave: " + e.getMessage());
        }
    }
    
    private void loadKey() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos de chave", "key", "pem"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String keyPath = fileChooser.getSelectedFile().getAbsolutePath();
                keyPathField.setText(keyPath);
                
                String algorithm = (String) algorithmCombo.getSelectedItem();
                byte[] keyBytes = Files.readAllBytes(Paths.get(keyPath));
                
                if (algorithm.startsWith("AES")) {
                    // Carregar chave AES
                    byte[] decodedKey = Base64.getDecoder().decode(keyBytes);
                    aesKey = new SecretKeySpec(decodedKey, "AES");
                    log("Chave AES carregada: " + keyPath);
                    
                } else if (algorithm.startsWith("RSA")) {
                    // Carregar chave RSA (assumindo chave pública)
                    byte[] decodedKey = Base64.getDecoder().decode(keyBytes);
                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                    
                    try {
                        // Tentar como chave pública
                        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
                        rsaKeyPair = new KeyPair(publicKey, null);
                        log("Chave pública RSA carregada: " + keyPath);
                    } catch (Exception e) {
                        // Tentar como chave privada
                        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
                        rsaKeyPair = new KeyPair(null, privateKey);
                        log("Chave privada RSA carregada: " + keyPath);
                    }
                }
                
            } catch (Exception e) {
                showError("Erro ao carregar chave: " + e.getMessage());
            }
        }
    }
    
    private void saveKey() {
        String algorithm = (String) algorithmCombo.getSelectedItem();
        
        if ((algorithm.startsWith("AES") && aesKey == null) ||
            (algorithm.startsWith("RSA") && rsaKeyPair == null)) {
            showError("Nenhuma chave gerada para salvar");
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos de chave", "key"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String keyPath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!keyPath.endsWith(".key")) {
                    keyPath += ".key";
                }
                
                if (algorithm.startsWith("AES")) {
                    // Salvar chave AES
                    byte[] encoded = Base64.getEncoder().encode(aesKey.getEncoded());
                    Files.write(Paths.get(keyPath), encoded);
                    log("Chave AES salva: " + keyPath);
                    
                } else if (algorithm.startsWith("RSA")) {
                    // Salvar chave pública RSA
                    String publicKeyPath = keyPath.replace(".key", "_public.key");
                    String privateKeyPath = keyPath.replace(".key", "_private.key");
                    
                    if (rsaKeyPair.getPublic() != null) {
                        byte[] publicEncoded = Base64.getEncoder().encode(rsaKeyPair.getPublic().getEncoded());
                        Files.write(Paths.get(publicKeyPath), publicEncoded);
                        log("Chave pública RSA salva: " + publicKeyPath);
                    }
                    
                    if (rsaKeyPair.getPrivate() != null) {
                        byte[] privateEncoded = Base64.getEncoder().encode(rsaKeyPair.getPrivate().getEncoded());
                        Files.write(Paths.get(privateKeyPath), privateEncoded);
                        log("Chave privada RSA salva: " + privateKeyPath);
                    }
                }
                
            } catch (Exception e) {
                showError("Erro ao salvar chave: " + e.getMessage());
            }
        }
    }
    
    private void performEncryption() {
        String filePath = filePathField.getText().trim();
        if (filePath.isEmpty()) {
            showError("Selecione um arquivo para criptografar");
            return;
        }
        
        String algorithm = (String) algorithmCombo.getSelectedItem();
        
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    File inputFile = new File(filePath);
                    String outputPath = filePath + ".encrypted";
                    
                    if (algorithm.startsWith("AES")) {
                        encryptAES(inputFile, new File(outputPath));
                    } else if (algorithm.startsWith("RSA")) {
                        encryptRSA(inputFile, new File(outputPath));
                    }
                    
                    log("Arquivo criptografado salvo: " + outputPath);
                    
                } catch (Exception e) {
                    throw new RuntimeException("Erro na criptografia: " + e.getMessage(), e);
                }
                return null;
            }
            
            @Override
            protected void process(java.util.List<Integer> chunks) {
                for (int progress : chunks) {
                    progressBar.setValue(progress);
                }
            }
            
            @Override
            protected void done() {
                progressBar.setValue(0);
                try {
                    get();
                    JOptionPane.showMessageDialog(FileEncryptionTool.this, 
                        "Criptografia concluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    showError(e.getCause().getMessage());
                }
            }
        };
        
        worker.execute();
    }
    
    private void performDecryption() {
        String filePath = filePathField.getText().trim();
        if (filePath.isEmpty()) {
            showError("Selecione um arquivo para descriptografar");
            return;
        }
        
        String algorithm = (String) algorithmCombo.getSelectedItem();
        
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    File inputFile = new File(filePath);
                    String outputPath = filePath.replace(".encrypted", ".decrypted");
                    
                    if (algorithm.startsWith("AES")) {
                        decryptAES(inputFile, new File(outputPath));
                    } else if (algorithm.startsWith("RSA")) {
                        decryptRSA(inputFile, new File(outputPath));
                    }
                    
                    log("Arquivo descriptografado salvo: " + outputPath);
                    
                } catch (Exception e) {
                    throw new RuntimeException("Erro na descriptografia: " + e.getMessage(), e);
                }
                return null;
            }
            
            @Override
            protected void process(java.util.List<Integer> chunks) {
                for (int progress : chunks) {
                    progressBar.setValue(progress);
                }
            }
            
            @Override
            protected void done() {
                progressBar.setValue(0);
                try {
                    get();
                    JOptionPane.showMessageDialog(FileEncryptionTool.this, 
                        "Descriptografia concluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    showError(e.getCause().getMessage());
                }
            }
        };
        
        worker.execute();
    }
    
    private void encryptAES(File inputFile, File outputFile) throws Exception {
        if (aesKey == null) {
            generateKey(); // Gerar chave automaticamente se não existir
            if (aesKey == null) {
                throw new RuntimeException("Chave AES não disponível");
            }
        }
        
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        
        // Obter IV usado
        byte[] iv = cipher.getIV();
        
        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            
            // Escrever IV no início do arquivo
            fos.write(iv);
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            long totalBytes = inputFile.length();
            long processedBytes = 0;
            
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] encrypted = cipher.update(buffer, 0, bytesRead);
                if (encrypted != null) {
                    fos.write(encrypted);
                }
                
                processedBytes += bytesRead;
                publish((int) ((processedBytes * 100) / totalBytes));
            }
            
            // Finalizar criptografia
            byte[] finalBytes = cipher.doFinal();
            if (finalBytes != null) {
                fos.write(finalBytes);
            }
        }
    }
    
    private void decryptAES(File inputFile, File outputFile) throws Exception {
        if (aesKey == null) {
            throw new RuntimeException("Chave AES não disponível para descriptografia");
        }
        
        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            
            // Ler IV do início do arquivo
            byte[] iv = new byte[16];
            fis.read(iv);
            
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(iv));
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            long totalBytes = inputFile.length() - 16; // Subtrair IV
            long processedBytes = 0;
            
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] decrypted = cipher.update(buffer, 0, bytesRead);
                if (decrypted != null) {
                    fos.write(decrypted);
                }
                
                processedBytes += bytesRead;
                publish((int) ((processedBytes * 100) / totalBytes));
            }
            
            // Finalizar descriptografia
            byte[] finalBytes = cipher.doFinal();
            if (finalBytes != null) {
                fos.write(finalBytes);
            }
        }
    }
    
    private void encryptRSA(File inputFile, File outputFile) throws Exception {
        if (rsaKeyPair == null || rsaKeyPair.getPublic() == null) {
            throw new RuntimeException("Chave pública RSA não disponível");
        }
        
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, rsaKeyPair.getPublic());
        
        // RSA tem limitação de tamanho, então criptografamos em blocos
        int blockSize = 245; // Para RSA 2048 bits
        
        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            
            byte[] buffer = new byte[blockSize];
            int bytesRead;
            long totalBytes = inputFile.length();
            long processedBytes = 0;
            
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] blockToEncrypt = Arrays.copyOf(buffer, bytesRead);
                byte[] encrypted = cipher.doFinal(blockToEncrypt);
                fos.write(encrypted);
                
                processedBytes += bytesRead;
                publish((int) ((processedBytes * 100) / totalBytes));
            }
        }
    }
    
    private void decryptRSA(File inputFile, File outputFile) throws Exception {
        if (rsaKeyPair == null || rsaKeyPair.getPrivate() == null) {
            throw new RuntimeException("Chave privada RSA não disponível");
        }
        
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, rsaKeyPair.getPrivate());
        
        int blockSize = 256; // Para RSA 2048 bits
        
        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            
            byte[] buffer = new byte[blockSize];
            int bytesRead;
            long totalBytes = inputFile.length();
            long processedBytes = 0;
            
            while ((bytesRead = fis.read(buffer)) != -1) {
                if (bytesRead == blockSize) {
                    byte[] decrypted = cipher.doFinal(buffer);
                    fos.write(decrypted);
                }
                
                processedBytes += bytesRead;
                publish((int) ((processedBytes * 100) / totalBytes));
            }
        }
    }
    
    private void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append("[" + java.time.LocalTime.now().toString().substring(0, 8) + "] " + message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
        log("ERRO: " + message);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new FileEncryptionTool().setVisible(true);
        });
    }
}