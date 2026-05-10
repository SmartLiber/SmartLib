import javax.swing.*;
import java.awt.*;

/**
 * 用户详情界面
 * 开发人员A - Swing前端任务8/8
 * 对应后端方法：UserService.getUserById()
 */
public class UserDetailFrame extends JFrame {
    private User user;
    private UserService userService;

    public UserDetailFrame(User user) {
        this.user = user;
        this.userService = new UserService();
        
        setTitle("SmartLib - 用户详情");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("👤 用户详情");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setForeground(new Color(41, 128, 185));
        titlePanel.add(titleLabel);
        
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel idLabel = new JLabel("用户ID：");
        idLabel.setFont(new Font("微软雅黑", Font.BOLD, 15));
        infoPanel.add(idLabel, gbc);
        
        gbc.gridx = 1;
        JLabel idValue = new JLabel(String.valueOf(user.getUserId()));
        idValue.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        infoPanel.add(idValue, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel nameLabel = new JLabel("用户名：");
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 15));
        infoPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        JLabel nameValue = new JLabel(user.getUsername());
        nameValue.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        infoPanel.add(nameValue, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel phoneLabel = new JLabel("手机号：");
        phoneLabel.setFont(new Font("微软雅黑", Font.BOLD, 15));
        infoPanel.add(phoneLabel, gbc);
        
        gbc.gridx = 1;
        JLabel phoneValue = new JLabel(user.getPhone());
        phoneValue.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        infoPanel.add(phoneValue, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton refreshButton = new JButton("🔄 刷新");
        refreshButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        refreshButton.addActionListener(e -> refreshUserInfo());
        
        JButton closeButton = new JButton("❌ 关闭");
        closeButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private void refreshUserInfo() {
        try {
            User updatedUser = userService.getUserById(user.getUserId());
            
            if (updatedUser != null) {
                this.user = updatedUser;
                
                JOptionPane.showMessageDialog(this,
                    "✅ 信息已刷新！\n\n" +
                    "用户ID：" + updatedUser.getUserId() + "\n" +
                    "用户名：" + updatedUser.getUsername() + "\n" +
                    "手机号：" + updatedUser.getPhone(),
                    "提示",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dispose();
                new UserDetailFrame(updatedUser).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "❌ 用户不存在！",
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "❌ 刷新失败：" + e.getMessage(),
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
            
            new UserDetailFrame(testUser).setVisible(true);
        });
    }
}
