NEW_FILE_CODE
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 用户登录界面
 * 开发人员A - Swing前端任务1/8
 * 对应后端方法：UserService.loginUser()
 */
public class LoginFrame extends JFrame {
    private JTextField usernameField;     // 用户名输入框
    private JButton loginButton;          // 登录按钮
    private JButton registerButton;       // 注册按钮
    private UserService userService;      // 调用后端服务

    public LoginFrame() {
        setTitle("SmartLib - 用户登录");
        setSize(450, 350);
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
        JLabel titleLabel = new JLabel("📚 SmartLib 图书馆管理系统");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setForeground(new Color(41, 128, 185));
        titlePanel.add(titleLabel);
        
        // === 中间：登录表单 ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
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
        
        // 提示文字
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel hintLabel = new JLabel("<html><font color='gray'>请输入用户名后点击登录</font></html>");
        hintLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(hintLabel, gbc);
        
        // === 底部：按钮区域 ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        loginButton = new JButton("登 录");
        loginButton.setFont(new Font("微软雅黑", Font.BOLD, 15));
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setBackground(new Color(41, 128, 185));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        registerButton = new JButton("注 册");
        registerButton.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        registerButton.setPreferredSize(new Dimension(120, 40));
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegisterFrame();
            }
        });
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        // 组合所有面板
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    /**
     * 处理登录逻辑
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        
        // 验证输入
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "请输入用户名！", 
                "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // 调用后端UserService的loginUser方法
            User user = userService.loginUser(username);
            
            if (user != null) {
                JOptionPane.showMessageDialog(this, 
                    "✅ 登录成功！\n\n欢迎，" + user.getUsername() + "\n用户ID: " + user.getUserId(), 
                    "成功", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // 打开主界面（后续开发）
                openMainFrame(user);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ 用户名不存在！\n请检查输入或先注册账号", 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "❌ 登录失败：" + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * 打开注册界面
     */
    private void openRegisterFrame() {
        RegisterFrame registerFrame = new RegisterFrame();
        registerFrame.setVisible(true);
        this.dispose();  // 关闭登录窗口
    }

    /**
     * 打开主界面（待开发）
     */
    private void openMainFrame(User user) {
        // TODO: 后续开发主界面，暂时显示提示
        MainFrame mainFrame = new MainFrame(user);
        mainFrame.setVisible(true);
        this.dispose();  // 关闭登录窗口
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
                new LoginFrame().setVisible(true);
            }
        });
    }
}
