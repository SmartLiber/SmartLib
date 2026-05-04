import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 用户注册界面
 * 开发人员A - Swing前端任务2/8
 * 对应后端方法：UserService.registerUser()
 */
public class RegisterFrame extends JFrame {
    private JTextField usernameField;     // 用户名输入框
    private JTextField phoneField;        // 手机号输入框
    private JButton registerButton;       // 注册按钮
    private JButton backButton;           // 返回按钮
    private UserService userService;      // 调用后端服务

    public RegisterFrame() {
        setTitle("SmartLib - 用户注册");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // 窗口居中
        
        // 初始化后端服务
        userService = new UserService();
        
        initUI();
    }

    private void initUI() {
        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        // === 顶部：标题区域 ===
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("📝 用户注册");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setForeground(new Color(46, 204, 113));
        titlePanel.add(titleLabel);
        
        // === 中间：注册表单 ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // 用户名标签
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel userLabel = new JLabel("用户名：");
        userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        formPanel.add(userLabel, gbc);
        
        // 用户名输入框
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(220, 35));
        formPanel.add(usernameField, gbc);
        
        // 手机号标签
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        JLabel phoneLabel = new JLabel("手机号：");
        phoneLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        formPanel.add(phoneLabel, gbc);
        
        // 手机号输入框
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        phoneField = new JTextField(15);
        phoneField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        phoneField.setPreferredSize(new Dimension(220, 35));
        formPanel.add(phoneField, gbc);
        
        // 提示文字
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel hintLabel = new JLabel("<html><font color='gray'>• 用户名不能重复<br>• 手机号必须是11位数字</font></html>");
        hintLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(hintLabel, gbc);
        
        // === 底部：按钮区域 ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        registerButton = new JButton("注 册");
        registerButton.setFont(new Font("微软雅黑", Font.BOLD, 15));
        registerButton.setPreferredSize(new Dimension(120, 40));
        registerButton.setBackground(new Color(46, 204, 113));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });
        
        backButton = new JButton("返 回");
        backButton.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setFocusPainted(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToLogin();
            }
        });
        
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        
        // 组合所有面板
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    /**
     * 处理注册逻辑
     */
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String phone = phoneField.getText().trim();
        
        // 验证输入
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "请输入用户名！", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "请输入手机号！", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // 调用后端UserService的registerUser方法
            boolean result = userService.registerUser(username, phone);
            
            if (result) {
                int choice = JOptionPane.showConfirmDialog(this, 
                    "✅ 注册成功！\n\n用户名：" + username + "\n手机号：" + phone + "\n\n" +
                    "是否立即登录？", 
                    "成功", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
                
                if (choice == JOptionPane.YES_OPTION) {
                    // 返回登录界面，并自动填充用户名
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setUsername(username);
                    loginFrame.setVisible(true);
                    this.dispose();
                } else {
                    // 清空表单，继续注册
                    usernameField.setText("");
                    phoneField.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ 注册失败！\n\n可能原因：\n" +
                    "1. 用户名已存在\n" +
                    "2. 手机号格式不正确（需要11位数字）", 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "❌ 注册失败：" + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * 返回登录界面
     */
    private void backToLogin() {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        this.dispose();  // 关闭注册窗口
    }

    /**
     * 程序入口
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new RegisterFrame().setVisible(true);
            }
        });
    }
}
