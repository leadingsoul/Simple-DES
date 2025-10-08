import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SDES extends JFrame {
    // 定义S-DES算法所需的置换盒
    private static final int[] IP = {2, 6, 3, 1, 4, 8, 5, 7}; // 初始置换
    private static final int[] IP_INV = {4, 1, 3, 5, 7, 2, 8, 6}; // 初始置换的逆
    private static final int[] P10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6}; // P10置换
    private static final int[] P8 = {6, 3, 7, 4, 8, 5, 10, 9}; // P8置换
    private static final int[] P4 = {2, 4, 3, 1}; // P4置换
    private static final int[] EP = {4, 1, 2, 3, 2, 3, 4, 1}; // 扩展置换
    private static final int[][] S1 = { // S盒1
            {1, 0, 3, 2},
            {3, 2, 1, 0},
            {0, 2, 1, 3},
            {3, 1, 3, 2}
    };
    private static final int[][] S2 = { // S盒2
            {0, 1, 2, 3},
            {2, 0, 1, 3},
            {3, 0, 1, 0},
            {2, 1, 0, 3}
    };

    // 构造函数
    public SDES() {
        super("S-DES加解密系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new CardLayout());

        // 创建功能面板
        JPanel encryptionPanel = createEncryptionPanel();
        JPanel decryptionPanel = createDecryptionPanel();
        JPanel bruteForcePanel = createBruteForcePanel();
        JPanel asciiEncryptionPanel = createAsciiEncryptionPanel();
        JPanel asciiDecryptionPanel = createAsciiDecryptionPanel();
        JPanel asciiBruteForcePanel = createAsciiBruteForcePanel();
        JPanel mainPanel = createMainPanel();

        // 添加到主窗口
        add(mainPanel, "main");
        add(encryptionPanel, "encryption");
        add(decryptionPanel, "decryption");
        add(bruteForcePanel, "bruteForce");
        add(asciiEncryptionPanel, "asciiEncryption");
        add(asciiDecryptionPanel, "asciiDecryption");
        add(asciiBruteForcePanel, "asciiBruteForce");

        // 设置默认显示主面板
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), "main");
    }

    // 创建主面板
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("S-DES加解密系统");
        titleLabel.setFont(new Font("宋体", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // 第一行：二进制加密和ASCII加密
        JButton encryptionBtn = new JButton("二进制加密");
        encryptionBtn.setFont(new Font("宋体", Font.PLAIN, 16));
        encryptionBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) getContentPane().getLayout();
            cl.show(getContentPane(), "encryption");
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(encryptionBtn, gbc);

        JButton asciiEncryptionBtn = new JButton("ASCII字符串加密");
        asciiEncryptionBtn.setFont(new Font("宋体", Font.PLAIN, 16));
        asciiEncryptionBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) getContentPane().getLayout();
            cl.show(getContentPane(), "asciiEncryption");
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(asciiEncryptionBtn, gbc);

        // 第二行：二进制解密和ASCII解密
        JButton decryptionBtn = new JButton("二进制解密");
        decryptionBtn.setFont(new Font("宋体", Font.PLAIN, 16));
        decryptionBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) getContentPane().getLayout();
            cl.show(getContentPane(), "decryption");
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(decryptionBtn, gbc);

        JButton asciiDecryptionBtn = new JButton("ASCII字符串解密");
        asciiDecryptionBtn.setFont(new Font("宋体", Font.PLAIN, 16));
        asciiDecryptionBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) getContentPane().getLayout();
            cl.show(getContentPane(), "asciiDecryption");
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(asciiDecryptionBtn, gbc);

        // 第三行：二进制暴力破解和ASCII暴力破解
        JButton bruteForceBtn = new JButton("二进制暴力破解");
        bruteForceBtn.setFont(new Font("宋体", Font.PLAIN, 16));
        bruteForceBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) getContentPane().getLayout();
            cl.show(getContentPane(), "bruteForce");
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(bruteForceBtn, gbc);

        JButton asciiBruteForceBtn = new JButton("ASCII暴力破解");
        asciiBruteForceBtn.setFont(new Font("宋体", Font.PLAIN, 16));
        asciiBruteForceBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) getContentPane().getLayout();
            cl.show(getContentPane(), "asciiBruteForce");
        });
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(asciiBruteForceBtn, gbc);

        return panel;
    }

    // 创建加密面板
    private JPanel createEncryptionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("S-DES加密");
        titleLabel.setFont(new Font("宋体", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        JLabel plaintextLabel = new JLabel("明文 (8位二进制):");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(plaintextLabel, gbc);

        JTextField plaintextField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(plaintextField, gbc);

        JLabel keyLabel = new JLabel("密钥 (10位二进制):");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(keyLabel, gbc);

        JTextField keyField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(keyField, gbc);

        JButton encryptBtn = new JButton("加密");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(encryptBtn, gbc);

        JLabel ciphertextLabel = new JLabel("密文:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(ciphertextLabel, gbc);

        JTextField ciphertextField = new JTextField(10);
        ciphertextField.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(ciphertextField, gbc);

        JButton backBtn = new JButton("返回主页");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(backBtn, gbc);

        // 添加事件监听
        encryptBtn.addActionListener(e -> {
            try {
                String plaintext = plaintextField.getText();
                String key = keyField.getText();
                
                // 验证输入
                if (plaintext.length() != 8 || !plaintext.matches("[01]++")) {
                    JOptionPane.showMessageDialog(panel, "请输入8位二进制明文！");
                    return;
                }
                if (key.length() != 10 || !key.matches("[01]++")) {
                    JOptionPane.showMessageDialog(panel, "请输入10位二进制密钥！");
                    return;
                }
                
                // 加密
                String ciphertext = encrypt(plaintext, key);
                ciphertextField.setText(ciphertext);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "加密失败: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) getContentPane().getLayout();
            cl.show(getContentPane(), "main");
        });

        return panel;
    }

    // 创建解密面板
    private JPanel createDecryptionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("S-DES解密");
        titleLabel.setFont(new Font("宋体", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        JLabel ciphertextLabel = new JLabel("密文 (8位二进制):");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(ciphertextLabel, gbc);

        JTextField ciphertextField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(ciphertextField, gbc);

        JLabel keyLabel = new JLabel("密钥 (10位二进制):");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(keyLabel, gbc);

        JTextField keyField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(keyField, gbc);

        JButton decryptBtn = new JButton("解密");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(decryptBtn, gbc);

        JLabel plaintextLabel = new JLabel("明文:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(plaintextLabel, gbc);

        JTextField plaintextField = new JTextField(10);
        plaintextField.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(plaintextField, gbc);

        JButton backBtn = new JButton("返回主页");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(backBtn, gbc);

        // 添加事件监听
        decryptBtn.addActionListener(e -> {
            try {
                String ciphertext = ciphertextField.getText();
                String key = keyField.getText();
                
                // 验证输入
                if (ciphertext.length() != 8 || !ciphertext.matches("[01]++")) {
                    JOptionPane.showMessageDialog(panel, "请输入8位二进制密文！");
                    return;
                }
                if (key.length() != 10 || !key.matches("[01]++")) {
                    JOptionPane.showMessageDialog(panel, "请输入10位二进制密钥！");
                    return;
                }
                
                // 解密
                String plaintext = decrypt(ciphertext, key);
                plaintextField.setText(plaintext);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "解密失败: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) getContentPane().getLayout();
            cl.show(getContentPane(), "main");
        });

        return panel;
    }

    // 创建暴力破解面板
    private JPanel createBruteForcePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("S-DES暴力破解");
        titleLabel.setFont(new Font("宋体", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        JLabel plaintextLabel = new JLabel("明文 (8位二进制):");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(plaintextLabel, gbc);

        JTextField plaintextField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(plaintextField, gbc);

        JLabel ciphertextLabel = new JLabel("密文 (8位二进制):");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(ciphertextLabel, gbc);

        JTextField ciphertextField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(ciphertextField, gbc);

        JButton bruteForceBtn = new JButton("开始暴力破解");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(bruteForceBtn, gbc);

        JLabel resultLabel = new JLabel("可能的密钥:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(resultLabel, gbc);

        JTextArea resultArea = new JTextArea(5, 10);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(scrollPane, gbc);

        JLabel timeLabel = new JLabel("破解时间:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(timeLabel, gbc);

        JTextField timeField = new JTextField(10);
        timeField.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(timeField, gbc);

        JButton backBtn = new JButton("返回主页");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(backBtn, gbc);

        // 添加事件监听
        bruteForceBtn.addActionListener(e -> {
            try {
                String plaintext = plaintextField.getText();
                String ciphertext = ciphertextField.getText();
                
                // 验证输入
                if (plaintext.length() != 8 || !plaintext.matches("[01]++")) {
                    JOptionPane.showMessageDialog(panel, "请输入8位二进制明文！");
                    return;
                }
                if (ciphertext.length() != 8 || !ciphertext.matches("[01]++")) {
                    JOptionPane.showMessageDialog(panel, "请输入8位二进制密文！");
                    return;
                }
                
                // 开始计时
                long startTime = System.currentTimeMillis();
                
                // 暴力破解
                List<String> possibleKeys = bruteForce(plaintext, ciphertext);
                
                // 结束计时
                long endTime = System.currentTimeMillis();
                double timeSeconds = (endTime - startTime) / 1000.0;
                
                // 显示结果
                StringBuilder result = new StringBuilder();
                for (String key : possibleKeys) {
                    result.append(key).append("\n");
                }
                resultArea.setText(result.toString());
                timeField.setText(timeSeconds + " 秒");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "破解失败: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) getContentPane().getLayout();
            cl.show(getContentPane(), "main");
        });

        return panel;
    }

    // 主函数
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SDES frame = new SDES();
            frame.setVisible(true);
        });
    }

    // 加密函数
    private static String encrypt(String plaintext, String key) {
        // 生成子密钥
        String[] subkeys = generateSubkeys(key);
        
        // 初始置换
        String ipOutput = permute(plaintext, IP);
        
        // 第一轮
        String fk1Output = fk(ipOutput, subkeys[0]);
        String swOutput = swap(fk1Output);
        
        // 第二轮
        String fk2Output = fk(swOutput, subkeys[1]);
        
        // 最终置换
        String ciphertext = permute(fk2Output, IP_INV);
        
        return ciphertext;
    }

    // 解密函数
    private static String decrypt(String ciphertext, String key) {
        // 生成子密钥
        String[] subkeys = generateSubkeys(key);
        
        // 初始置换
        String ipOutput = permute(ciphertext, IP);
        
        // 第一轮（使用第二轮子密钥）
        String fk1Output = fk(ipOutput, subkeys[1]);
        String swOutput = swap(fk1Output);
        
        // 第二轮（使用第一轮子密钥）
        String fk2Output = fk(swOutput, subkeys[0]);
        
        // 最终置换
        String plaintext = permute(fk2Output, IP_INV);
        
        return plaintext;
    }

    // 暴力破解函数 - 二进制
    private static List<String> bruteForce(String plaintext, String ciphertext) {
        List<String> possibleKeys = new ArrayList<>();
        
        // 遍历所有可能的10位二进制密钥
        for (int i = 0; i < 1024; i++) {
            String key = String.format("%10s", Integer.toBinaryString(i)).replace(' ', '0');
            
            // 用当前密钥加密明文
            String encrypted = encrypt(plaintext, key);
            
            // 检查是否与给定的密文匹配
            if (encrypted.equals(ciphertext)) {
                possibleKeys.add(key);
            }
        }
        
        return possibleKeys;
    }

    // ASCII字符串暴力破解函数
    private static List<String> asciiBruteForce(String plaintextAscii, String ciphertextAscii) {
        List<String> possibleKeys = new ArrayList<>();
        
        // 将ASCII字符串转换为二进制字符串
        String plaintextBinary = asciiToBinary(plaintextAscii);
        String ciphertextBinary = asciiToBinary(ciphertextAscii);
        
        // 检查长度是否匹配
        if (plaintextBinary.length() != ciphertextBinary.length() || plaintextBinary.length() % 8 != 0) {
            return possibleKeys; // 返回空列表表示无法破解
        }
        
        // 遍历所有可能的10位二进制密钥
        for (int i = 0; i < 1024; i++) {
            String key = String.format("%10s", Integer.toBinaryString(i)).replace(' ', '0');
            boolean keyMatches = true;
            
            // 对每8位二进制进行加密并验证
            for (int j = 0; j < plaintextBinary.length(); j += 8) {
                String plaintextBlock = plaintextBinary.substring(j, j + 8);
                String encryptedBlock = encrypt(plaintextBlock, key);
                
                // 检查是否与对应位置的密文块匹配
                if (!encryptedBlock.equals(ciphertextBinary.substring(j, j + 8))) {
                    keyMatches = false;
                    break;
                }
            }
            
            if (keyMatches) {
                possibleKeys.add(key);
            }
        }
        
        return possibleKeys;
    }

    // ASCII字符串转二进制字符串
    private static String asciiToBinary(String ascii) {
        StringBuilder binary = new StringBuilder();
        for (char c : ascii.toCharArray()) {
            // 每个字符转为8位二进制
            String binaryChar = String.format("%8s", Integer.toBinaryString(c & 0xFF)).replace(' ', '0');
            binary.append(binaryChar);
        }
        return binary.toString();
    }

    // 创建ASCII字符串加密面板
    private JPanel createAsciiEncryptionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("S-DES ASCII字符串加密");
        titleLabel.setFont(new Font("宋体", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        JLabel plaintextLabel = new JLabel("明文 (ASCII字符串):");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(plaintextLabel, gbc);

        JTextArea plaintextArea = new JTextArea(5, 20);
        plaintextArea.setLineWrap(true);
        JScrollPane plaintextScrollPane = new JScrollPane(plaintextArea);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(plaintextScrollPane, gbc);

        JLabel keyLabel = new JLabel("密钥 (10位二进制):");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(keyLabel, gbc);

        JTextField keyField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(keyField, gbc);

        JButton encryptBtn = new JButton("加密");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(encryptBtn, gbc);

        JLabel ciphertextLabel = new JLabel("密文 (ASCII字符串):");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(ciphertextLabel, gbc);

        JTextArea ciphertextArea = new JTextArea(5, 20);
        ciphertextArea.setLineWrap(true);
        ciphertextArea.setEditable(false);
        JScrollPane ciphertextScrollPane = new JScrollPane(ciphertextArea);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(ciphertextScrollPane, gbc);

        JButton backBtn = new JButton("返回主页");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(backBtn, gbc);

        // 添加事件监听
        encryptBtn.addActionListener(e -> {
            try {
                String plaintext = plaintextArea.getText();
                String key = keyField.getText();
                
                // 验证密钥输入
                if (key.length() != 10 || !key.matches("[01]++")) {
                    JOptionPane.showMessageDialog(panel, "请输入10位二进制密钥！");
                    return;
                }
                
                // 加密
                String ciphertext = encryptAsciiString(plaintext, key);
                ciphertextArea.setText(ciphertext);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "加密失败: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) getContentPane().getLayout();
            cl.show(getContentPane(), "main");
        });

        return panel;
    }

    // 创建ASCII字符串解密面板
    private JPanel createAsciiDecryptionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("S-DES ASCII字符串解密");
        titleLabel.setFont(new Font("宋体", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        JLabel ciphertextLabel = new JLabel("密文 (ASCII字符串):");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(ciphertextLabel, gbc);

        JTextArea ciphertextArea = new JTextArea(5, 20);
        ciphertextArea.setLineWrap(true);
        JScrollPane ciphertextScrollPane = new JScrollPane(ciphertextArea);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(ciphertextScrollPane, gbc);

        JLabel keyLabel = new JLabel("密钥 (10位二进制):");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(keyLabel, gbc);

        JTextField keyField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(keyField, gbc);

        JButton decryptBtn = new JButton("解密");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(decryptBtn, gbc);

        JLabel plaintextLabel = new JLabel("明文 (ASCII字符串):");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(plaintextLabel, gbc);

        JTextArea plaintextArea = new JTextArea(5, 20);
        plaintextArea.setLineWrap(true);
        plaintextArea.setEditable(false);
        JScrollPane plaintextScrollPane = new JScrollPane(plaintextArea);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(plaintextScrollPane, gbc);

        JButton backBtn = new JButton("返回主页");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(backBtn, gbc);

        // 添加事件监听
        decryptBtn.addActionListener(e -> {
            try {
                String ciphertext = ciphertextArea.getText();
                String key = keyField.getText();
                
                // 验证密钥输入
                if (key.length() != 10 || !key.matches("[01]++")) {
                    JOptionPane.showMessageDialog(panel, "请输入10位二进制密钥！");
                    return;
                }
                
                // 解密
                String plaintext = decryptAsciiString(ciphertext, key);
                plaintextArea.setText(plaintext);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "解密失败: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) getContentPane().getLayout();
            cl.show(getContentPane(), "main");
        });

        return panel;
    }

    // ASCII字符串加密函数
    private static String encryptAsciiString(String plaintext, String key) {
        StringBuilder ciphertext = new StringBuilder();
        
        // 对每个字符进行加密
        for (int i = 0; i < plaintext.length(); i++) {
            char c = plaintext.charAt(i);
            
            // 将字符转换为8位二进制
            String binary = String.format("%8s", Integer.toBinaryString(c & 0xFF)).replace(' ', '0');
            
            // 加密
            String encryptedBinary = encrypt(binary, key);
            
            // 将加密后的二进制转换回字符
            int encryptedInt = Integer.parseInt(encryptedBinary, 2);
            ciphertext.append((char) encryptedInt);
        }
        
        return ciphertext.toString();
    }

    // ASCII字符串解密函数
    private static String decryptAsciiString(String ciphertext, String key) {
        StringBuilder plaintext = new StringBuilder();
        
        // 对每个字符进行解密
        for (int i = 0; i < ciphertext.length(); i++) {
            char c = ciphertext.charAt(i);
            
            // 将字符转换为8位二进制
            String binary = String.format("%8s", Integer.toBinaryString(c & 0xFF)).replace(' ', '0');
            
            // 解密
            String decryptedBinary = decrypt(binary, key);
            
            // 将解密后的二进制转换回字符
            int decryptedInt = Integer.parseInt(decryptedBinary, 2);
            plaintext.append((char) decryptedInt);
        }
        
        return plaintext.toString();
    }

    // 创建ASCII暴力破解面板
    private JPanel createAsciiBruteForcePanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 标题面板
        JLabel titleLabel = new JLabel("S-DES ASCII暴力破解");
        titleLabel.setFont(new Font("宋体", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // 中间内容面板
        JPanel contentPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.add(contentPanel, BorderLayout.CENTER);

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        contentPanel.add(inputPanel);

        // 明文输入区域
        JPanel plaintextPanel = new JPanel(new BorderLayout(5, 5));
        plaintextPanel.setBorder(BorderFactory.createTitledBorder("明文 (ASCII字符串)"));
        JTextArea plaintextArea = new JTextArea(5, 15);
        plaintextArea.setLineWrap(true);
        plaintextArea.setWrapStyleWord(true);
        plaintextPanel.add(new JScrollPane(plaintextArea), BorderLayout.CENTER);
        inputPanel.add(plaintextPanel);

        // 密文输入区域
        JPanel ciphertextPanel = new JPanel(new BorderLayout(5, 5));
        ciphertextPanel.setBorder(BorderFactory.createTitledBorder("密文 (ASCII字符串)"));
        JTextArea ciphertextArea = new JTextArea(5, 15);
        ciphertextArea.setLineWrap(true);
        ciphertextArea.setWrapStyleWord(true);
        ciphertextPanel.add(new JScrollPane(ciphertextArea), BorderLayout.CENTER);
        inputPanel.add(ciphertextPanel);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton bruteForceBtn = new JButton("开始暴力破解");
        bruteForceBtn.setFont(new Font("宋体", Font.PLAIN, 14));
        bruteForceBtn.setPreferredSize(new Dimension(150, 30));
        buttonPanel.add(bruteForceBtn);
        contentPanel.add(buttonPanel);

        // 结果面板
        JPanel resultPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        contentPanel.add(resultPanel);

        // 密钥结果区域
        JPanel keyResultPanel = new JPanel(new BorderLayout(5, 5));
        keyResultPanel.setBorder(BorderFactory.createTitledBorder("可能的密钥"));
        JTextArea resultArea = new JTextArea(5, 10);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        keyResultPanel.add(scrollPane, BorderLayout.CENTER);
        resultPanel.add(keyResultPanel);

        // 状态信息面板
        JPanel statusPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel timeLabel = new JLabel("破解时间: ");
        JTextField timeField = new JTextField(10);
        timeField.setEditable(false);
        timeField.setHorizontalAlignment(JTextField.LEFT);
        timePanel.add(timeLabel);
        timePanel.add(timeField);
        statusPanel.add(timePanel);

        // 底部按钮面板
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backBtn = new JButton("返回主页");
        backBtn.setPreferredSize(new Dimension(100, 25));
        bottomPanel.add(backBtn);
        statusPanel.add(bottomPanel);
        resultPanel.add(statusPanel);

        // 添加事件监听
        bruteForceBtn.addActionListener(e -> {
            try {
                String plaintext = plaintextArea.getText();
                String ciphertext = ciphertextArea.getText();
                
                // 验证输入
                if (plaintext.isEmpty() || ciphertext.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "请输入明文和密文！");
                    return;
                }
                
                // 开始计时
                long startTime = System.currentTimeMillis();
                
                // 暴力破解
                List<String> possibleKeys = asciiBruteForce(plaintext, ciphertext);
                
                // 结束计时
                long endTime = System.currentTimeMillis();
                double timeSeconds = (endTime - startTime) / 1000.0;
                
                // 显示结果
                StringBuilder result = new StringBuilder();
                for (String key : possibleKeys) {
                    result.append(key).append("\n");
                }
                resultArea.setText(result.toString());
                timeField.setText(timeSeconds + " 秒");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "破解失败: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) getContentPane().getLayout();
            cl.show(getContentPane(), "main");
        });

        return panel;
    }

    // 生成子密钥
    private static String[] generateSubkeys(String key) {
        String[] subkeys = new String[2];
        
        // P10置换
        String p10Output = permute(key, P10);
        
        // 分为左右两部分
        String left = p10Output.substring(0, 5);
        String right = p10Output.substring(5, 10);
        
        // 生成k1
        left = leftShift(left, 1);
        right = leftShift(right, 1);
        subkeys[0] = permute(left + right, P8);
        
        // 生成k2
        left = leftShift(left, 2);
        right = leftShift(right, 2);
        subkeys[1] = permute(left + right, P8);
        
        return subkeys;
    }

    // F函数
    private static String f(String right, String subkey) {
        // 扩展置换
        String epOutput = permute(right, EP);
        
        // 与子密钥异或
        String xorOutput = xor(epOutput, subkey);
        
        // S盒替代
        String left = xorOutput.substring(0, 4);
        String rightPart = xorOutput.substring(4, 8);
        
        String s1Output = sBox(left, S1);
        String s2Output = sBox(rightPart, S2);
        
        // P4置换
        String p4Output = permute(s1Output + s2Output, P4);
        
        return p4Output;
    }

    // fk函数
    private static String fk(String input, String subkey) {
        // 分为左右两部分
        String left = input.substring(0, 4);
        String right = input.substring(4, 8);
        
        // 应用f函数并与左部分异或
        String fOutput = f(right, subkey);
        String newLeft = xor(left, fOutput);
        
        // 组合新的左部分和原来的右部分
        return newLeft + right;
    }

    // 置换函数
    private static String permute(String input, int[] permutation) {
        StringBuilder output = new StringBuilder();
        for (int i : permutation) {
            output.append(input.charAt(i - 1));
        }
        return output.toString();
    }

    // 左移函数
    private static String leftShift(String input, int shiftBits) {
        int length = input.length();
        shiftBits = shiftBits % length;
        return input.substring(shiftBits) + input.substring(0, shiftBits);
    }

    // 交换函数
    private static String swap(String input) {
        int length = input.length();
        int half = length / 2;
        return input.substring(half) + input.substring(0, half);
    }

    // 异或函数
    private static String xor(String a, String b) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            result.append(a.charAt(i) == b.charAt(i) ? '0' : '1');
        }
        return result.toString();
    }

    // S盒替代函数
    private static String sBox(String input, int[][] sBox) {
        // 计算行和列
        int row = Integer.parseInt(input.charAt(0) + "" + input.charAt(3), 2);
        int col = Integer.parseInt(input.charAt(1) + "" + input.charAt(2), 2);
        
        // 从S盒中获取值并转换为2位二进制
        int value = sBox[row][col];
        return String.format("%2s", Integer.toBinaryString(value)).replace(' ', '0');
    }
}