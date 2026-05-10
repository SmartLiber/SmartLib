import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JTextField captchaField;
    private JLabel captchaImageLabel;
    private JButton loginButton;
    private JButton registerButton;
    private UserService userService;
    private CaptchaUtil captchaUtil;

    public LoginFrame() {
        setTitle("SmartLib - 用户登录");
        setSize(450, 430);
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
        JLabel titleLabel = new JLabel("用户登录");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setForeground(new Color(41, 128, 185));
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

        // 验证码图片
        gbc.gridx = 0; gbc.gridy = 1;
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
        refreshBtn.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        refreshBtn.setPreferredSize(new Dimension(55, 30));
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
        gbc.gridx = 0; gbc.gridy = 2;
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
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel hintLabel = new JLabel("<html><font color='gray'>提示：输入已注册的用户名和验证码即可登录</font></html>");
        hintLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(hintLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(250, 250, 252));

        loginButton = new JButton("登 录");
        loginButton.setFont(new Font("微软雅黑", Font.BOLD, 15));
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setBackground(new Color(41, 128, 185));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> handleLogin());

        registerButton = new JButton("注 册");
        registerButton.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        registerButton.setPreferredSize(new Dimension(120, 40));
        registerButton.setBackground(new Color(240, 240, 245));
        registerButton.setForeground(darkColor);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(e -> openRegisterFrame());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void refreshCaptchaImage() {
        captchaImageLabel.setIcon(new ImageIcon(captchaUtil.createImage(120, 40)));
    }

    public void setUsername(String username) {
        usernameField.setText(username);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入用户名！", "提示", JOptionPane.WARNING_MESSAGE);
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
            User user = userService.loginUser(username);
            if (user != null) {
                JOptionPane.showMessageDialog(this,
                        "登录成功！\n\n欢迎，" + user.getUsername(),
                        "成功", JOptionPane.INFORMATION_MESSAGE);
                openMainFrame(user);
            } else {
                JOptionPane.showMessageDialog(this,
                        "用户名不存在！\n\n请先注册或检查用户名是否正确。",
                        "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "登录失败：" + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void openRegisterFrame() {
        RegisterFrame registerFrame = new RegisterFrame();
        registerFrame.setVisible(true);
        this.dispose();
    }

    private void openMainFrame(User user) {
        MainFrame mainFrame = new MainFrame(user);
        mainFrame.setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginFrame().setVisible(true);
        });
    }
}
