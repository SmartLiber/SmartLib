import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 编辑用户信息界面
 * 开发人员A - Swing前端任务7/8
 * 对应后端方法：UserService.updateUserInfo()
 */
public class EditUserInfoFrame extends JFrame {
    private User currentUser;
    private UserService userService;
    private JTextField usernameField;
    private JTextField phoneField;

    public EditUserInfoFrame(User user) {
        this.currentUser = user;
        this.userService = new UserService();
        
        setTitle("SmartLib - 编辑用户信息");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initUI();
        loadUserInfo();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("✏️ 编辑用户信息");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setForeground(new Color(46, 204, 113));
        titlePanel.add(titleLabel);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel idLabel = new JLabel("用户ID：");
        idLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        formPanel.add(idLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JLabel idValue = new JLabel(String.valueOf(currentUser.getUserId()));
        idValue.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        formPanel.add(idValue, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        JLabel userLabel = new JLabel("用户名：");
        userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        formPanel.add(userLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(220, 35));
        formPanel.add(usernameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        JLabel phoneLabel = new JLabel("手机号：");
        phoneLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        formPanel.add(phoneLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        phoneField = new JTextField(15);
        phoneField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        phoneField.setPreferredSize(new Dimension(220, 35));
        formPanel.add(phoneField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton saveButton = new JButton("💾 保存");
        saveButton.setFont(new Font("微软雅黑", Font.BOLD, 15));
        saveButton.setPreferredSize(new Dimension(120, 40));
        saveButton.setBackground(new Color(46, 204, 113));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSave();
            }
        });
        
        JButton cancelButton = new JButton("❌ 取消");
        cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private void loadUserInfo() {
        usernameField.setText(currentUser.getUsername());
        phoneField.setText(currentUser.getPhone());
    }

    private void handleSave() {
        String newUsername = usernameField.getText().trim();
        String newPhone = phoneField.getText().trim();
        
        if (newUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "请输入用户名！",
                "提示",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (newPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "请输入手机号！",
                "提示",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            boolean result = userService.updateUserInfo(currentUser.getUserId(), newUsername, newPhone);
            
            if (result) {
                JOptionPane.showMessageDialog(this,
                    "✅ 更新成功！\n\n用户名：" + newUsername + "\n手机号：" + newPhone,
                    "成功",
                    JOptionPane.INFORMATION_MESSAGE);
                
                currentUser.setUsername(newUsername);
                currentUser.setPhone(newPhone);
                
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "❌ 更新失败！\n\n可能原因：\n" +
                    "1. 用户名已被其他用户使用\n" +
                    "2. 手机号格式不正确",
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "❌ 更新失败：" + e.getMessage(),
                "错误",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            User testUser = new User();
            testUser.setUserId(1);
            testUser.setUsername("测试用户");
            testUser.setPhone("13800138000");
            
            new EditUserInfoFrame(testUser).setVisible(true);
        });
    }
}
