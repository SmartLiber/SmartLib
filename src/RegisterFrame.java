import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JTextField phoneField;
    private JTextField captchaField;
    private JLabel captchaImageLabel;
    private JButton registerButton;
    private JButton backButton;
    private UserService userService;
    private CaptchaUtil captchaUtil;

    public RegisterFrame() {
        setTitle("SmartLib - 用户注册");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        userService = new UserService();
        captchaUtil = new CaptchaUtil();

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(new Color(250, 250, 252));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(250, 250, 252));
        JLabel titleLabel = new JLabel("用户注册");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setForeground(new Color(46, 134, 70));
        titlePanel.add(titleLabel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(250, 250, 252));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        Color darkColor = new Color(50, 50, 60);

        // 用户名
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel userLabel = new JLabel("用户名：");
        userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        userLabel.setForeground(darkColor);
        formPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(220, 35));
        usernameField.setForeground(darkColor);
        formPanel.add(usernameField, gbc);

        // 手机号
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel phoneLabel = new JLabel("手机号：");
        phoneLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        phoneLabel.setForeground(darkColor);
        formPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        phoneField = new JTextField(15);
        phoneField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        phoneField.setPreferredSize(new Dimension(220, 35));
        phoneField.setForeground(darkColor);
        formPanel.add(phoneField, gbc);

        // 验证码图片
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel captchaLabel = new JLabel("验证码：");
        captchaLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        captchaLabel.setForeground(darkColor);
        formPanel.add(captchaLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JPanel captchaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        captchaPanel.setBackground(new Color(250, 250, 252));

        captchaImageLabel = new JLabel();
        refreshCaptchaImage();
        captchaPanel.add(captchaImageLabel);

        JButton refreshBtn = new JButton("刷新");
        refreshBtn.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        refreshBtn.setPreferredSize(new Dimension(75, 38));
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBackground(new Color(200, 200, 210));
        refreshBtn.setForeground(darkColor);
        refreshBtn.addActionListener(e -> {
            captchaUtil.refresh();
            refreshCaptchaImage();
            captchaField.setText("");
        });
        captchaPanel.add(refreshBtn);
        formPanel.add(captchaPanel, gbc);

        // 验证码输入
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel(""), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        captchaField = new JTextField(6);
        captchaField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        captchaField.setPreferredSize(new Dimension(100, 35));
        captchaField.setForeground(darkColor);
        formPanel.add(captchaField, gbc);

        // 提示
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel hintLabel = new JLabel("<html><font color='gray'>• 用户名不能重复<br>• 手机号必须是11位数字</font></html>");
        hintLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(hintLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(250, 250, 252));

        registerButton = new JButton("注 册");
        registerButton.setFont(new Font("微软雅黑", Font.BOLD, 15));
        registerButton.setPreferredSize(new Dimension(120, 40));
        registerButton.setBackground(new Color(120, 190, 140));
        registerButton.setForeground(darkColor);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(e -> handleRegister());

        backButton = new JButton("返 回");
        backButton.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setBackground(new Color(240, 240, 245));
        backButton.setForeground(darkColor);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> backToLogin());

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void refreshCaptchaImage() {
        captchaImageLabel.setIcon(new ImageIcon(captchaUtil.createImage(120, 40)));
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String phone = phoneField.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入用户名！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入手机号！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String captchaInput = captchaField.getText().trim();
        if (captchaInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入验证码！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!captchaInput.equals(captchaUtil.getCode())) {
            JOptionPane.showMessageDialog(this, "验证码错误！请重新输入。", "提示", JOptionPane.WARNING_MESSAGE);
            captchaUtil.refresh();
            refreshCaptchaImage();
            captchaField.setText("");
            return;
        }

        try {
            boolean result = userService.registerUser(username, phone);
            if (result) {
                int choice = JOptionPane.showConfirmDialog(this,
                        "注册成功！\n\n用户名：" + username + "\n手机号：" + phone + "\n\n是否立即登录？",
                        "成功", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                if (choice == JOptionPane.YES_OPTION) {
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setUsername(username);
                    loginFrame.setVisible(true);
                    this.dispose();
                } else {
                    usernameField.setText("");
                    phoneField.setText("");
                    captchaField.setText("");
                    captchaUtil.refresh();
                    refreshCaptchaImage();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "注册失败！\n\n可能原因：\n1. 用户名已存在\n2. 手机号格式不正确（需要11位数字）",
                        "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "注册失败：" + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void backToLogin() {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new RegisterFrame().setVisible(true);
        });
    }
}
