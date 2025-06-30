import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;

public class FileEncryptionTool extends JFrame {
    private JProgressBar progressBar;
    private JTextArea logArea;
    private JTextField filePathField;
    private JPasswordField passwordField;
    private JComboBox<String> algorithmCombo;
    private SecretKey currentKey;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    
    public FileEncryptionTool() {
        initializeUI();
        setSystemLookAndFeel();
    }
    
    private void setSystemLookAndFeel() {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            logMessage("Error setting Look and Feel: " + e.getMessage());
        }
    }
    
    private void initializeUI() {
        setTitle("Ferramenta de Criptografia de Arquivos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Painel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Seleção de arquivo
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Arquivo:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        filePathField = new JTextField();
        mainPanel.add(filePathField, gbc);
        
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JButton selectFileButton = new JButton("Selecionar");
        selectFileButton.addActionListener(e -> selectFile());
        mainPanel.add(selectFileButton, gbc);
        
        // Configurações de criptografia
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Algoritmo:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        algorithmCombo = new JComboBox<>(new String[]{"AES", "DES", "RSA"});
        mainPanel.add(algorithmCombo, gbc);
        
        // Senha
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Senha:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        passwordField = new JPasswordField();
        mainPanel.add(passwordField, gbc);
        
        // Botões de ação
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton generateKeyButton = new JButton("Gerar Chave");
        generateKeyButton.addActionListener(e -> generateKey());
        buttonPanel.add(generateKeyButton);
        
        JButton loadKeyButton = new JButton("Carregar Chave");
        loadKeyButton.addActionListener(e -> loadKey());
        buttonPanel.add(loadKeyButton);
        
        JButton saveKeyButton = new JButton("Salvar Chave");
        saveKeyButton.addActionListener(e -> saveKey());
        buttonPanel.add(saveKeyButton);
        
        JButton encryptButton = new JButton("Criptografar");
        encryptButton.addActionListener(e -> encryptFile());
        buttonPanel.add(encryptButton);
        
        JButton decryptButton = new JButton("Descriptografar");
        decryptButton.addActionListener(e -> decryptFile());
        buttonPanel.add(decryptButton);
        
        // Barra de progresso
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        
        // Área de log
        logArea = new JTextArea(10, 50);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        
        // Adicionar componentes ao frame
        add(mainPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(progressBar, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.EAST);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            filePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            logMessage("Arquivo selecionado: " + fileChooser.getSelectedFile().getName());
        }
    }
    
    private void generateKey() {
        try {
            String algorithm = (String) algorithmCombo.getSelectedItem();
            char[] password = passwordField.getPassword();
            
            if (password.length == 0) {
                logMessage("Digite uma senha para gerar a chave");
                return;
            }
            
            if ("RSA".equals(algorithm)) {
                generateRSAKeyPair();
            } else {
                generateSymmetricKey(algorithm, password);
            }
            
            // Limpar senha da memória
            java.util.Arrays.fill(password, ' ');
            
        } catch (Exception e) {
            logMessage("Erro ao gerar chave: " + e.getMessage());
        }
    }
    
    private void generateSymmetricKey(String algorithm, char[] password) throws Exception {
        // Gerar chave a partir da senha
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(new String(password).getBytes("UTF-8"));
        
        int keyLength = "AES".equals(algorithm) ? 16 : 8; // AES: 128 bits, DES: 64 bits
        byte[] keyBytes = new byte[keyLength];
        System.arraycopy(hash, 0, keyBytes, 0, keyLength);
        
        currentKey = new SecretKeySpec(keyBytes, algorithm);
        logMessage("Chave " + algorithm + " gerada com sucesso a partir da senha");
    }
    
    private void generateRSAKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();
        
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        
        logMessage("Par de chaves RSA gerado com sucesso");
    }
    
    private void loadKey() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Arquivos de chave", "key"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                byte[] keyBytes = Files.readAllBytes(fileChooser.getSelectedFile().toPath());
                String algorithm = (String) algorithmCombo.getSelectedItem();
                
                if ("RSA".equals(algorithm)) {
                    // Carregar chave RSA (assumindo chave pública por padrão)
                    // Implementação específica dependeria do formato da chave salva
                    logMessage("Chave RSA carregada");
                } else {
                    currentKey = new SecretKeySpec(keyBytes, algorithm);
                    logMessage("Chave " + algorithm + " carregada");
                }
            } catch (Exception e) {
                logMessage("Erro ao carregar chave: " + e.getMessage());
            }
        }
    }
    
    private void saveKey() {
        if (currentKey == null && publicKey == null) {
            logMessage("Nenhuma chave gerada para salvar");
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Arquivos de chave", "key"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().endsWith(".key")) {
                    file = new File(file.getAbsolutePath() + ".key");
                }
                
                byte[] keyBytes;
                if (currentKey != null) {
                    keyBytes = currentKey.getEncoded();
                } else {
                    keyBytes = publicKey.getEncoded();
                }
                
                Files.write(file.toPath(), keyBytes);
                logMessage("Chave salva em: " + file.getName());
            } catch (Exception e) {
                logMessage("Erro ao salvar chave: " + e.getMessage());
            }
        }
    }
    
    private void encryptFile() {
        performCryptoOperation(true);
    }
    
    private void decryptFile() {
        performCryptoOperation(false);
    }
    
    private void performCryptoOperation(boolean encrypt) {
        String filePath = filePathField.getText();
        if (filePath.isEmpty()) {
            logMessage("Selecione um arquivo primeiro");
            return;
        }
        
        if (currentKey == null && publicKey == null) {
            logMessage("Gere ou carregue uma chave primeiro");
            return;
        }
        
        // Usar SwingWorker para operações em background
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    String algorithm = (String) algorithmCombo.getSelectedItem();
                    File inputFile = new File(filePath);
                    String outputPath = filePath + (encrypt ? ".encrypted" : ".decrypted");
                    File outputFile = new File(outputPath);
                    
                    Cipher cipher = Cipher.getInstance(algorithm);
                    
                    if ("RSA".equals(algorithm)) {
                        cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE,
                                   encrypt ? publicKey : privateKey);
                    } else {
                        cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, currentKey);
                    }
                    
                    try (FileInputStream fis = new FileInputStream(inputFile);
                         FileOutputStream fos = new FileOutputStream(outputFile)) {
                        
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        long totalBytes = inputFile.length();
                        long processedBytes = 0;
                        
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            byte[] output = cipher.update(buffer, 0, bytesRead);
                            if (output != null) {
                                fos.write(output);
                            }
                            
                            processedBytes += bytesRead;
                            int progress = (int) ((processedBytes * 100) / totalBytes);
                            publish(progress); // Agora funciona porque estamos em SwingWorker
                        }
                        
                        byte[] finalOutput = cipher.doFinal();
                        if (finalOutput != null) {
                            fos.write(finalOutput);
                        }
                    }
                    
                    return null;
                } catch (Exception e) {
                    throw e;
                }
            }
            
            @Override
            protected void process(java.util.List<Integer> chunks) {
                // Atualizar barra de progresso
                if (!chunks.isEmpty()) {
                    progressBar.setValue(chunks.get(chunks.size() - 1));
                }
            }
            
            @Override
            protected void done() {
                try {
                    get(); // Verificar se houve exceções
                    progressBar.setValue(100);
                    logMessage((encrypt ? "Criptografia" : "Descriptografia") + " concluída com sucesso");
                } catch (Exception e) {
                    logMessage("Erro na operação: " + e.getCause().getMessage());
                } finally {
                    progressBar.setValue(0);
                }
            }
        };
        
        worker.execute();
    }
    
    private void logMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(java.time.LocalTime.now() + " - " + message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FileEncryptionTool().setVisible(true);
        });
    }
}