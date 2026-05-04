import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 系统主界面
 * 开发人员A - Swing前端任务3/8
 */
public class MainFrame extends JFrame {
    private User currentUser;
    private UserService userService;
    
    public MainFrame(User user) {
        this.currentUser = user;
        this.userService = new UserService();
        
        setTitle("SmartLib - 图书馆管理系统");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initUI();
    }
    
    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel welcomeLabel = new JLabel("欢迎，" + currentUser.getUsername() + "！");
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(41, 128, 185));
        topPanel.add(welcomeLabel);
        
        JPanel centerPanel = new JPanel(new GridLayout(4, 2, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        String[] buttonLabels = {
            "📚 用户列表",
            "📖 借阅历史",
            "⚠️ 未还书籍",
            "✏️ 编辑信息",
            "📊 用户统计",
            "🔍 搜索用户",
            "🔄 刷新数据",
            "🚪 退出登录"
        };
        
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            button.setPreferredSize(new Dimension(200, 60));
            button.setFocusPainted(false);
            
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleButtonClick(label);
                }
            });
            
            centerPanel.add(button);
        }
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel statusLabel = new JLabel("用户ID: " + currentUser.getUserId());
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        bottomPanel.add(statusLabel);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void handleButtonClick(String label) {
        switch (label) {
            case "📚 用户列表":
                new UserListFrame().setVisible(true);
                break;
            case "📖 借阅历史":
                new BorrowHistoryFrame(currentUser.getUserId()).setVisible(true);
                break;
            case "⚠️ 未还书籍":
                new UnreturnedBooksFrame(currentUser.getUserId()).setVisible(true);
                break;
            case "✏️ 编辑信息":
                new EditUserInfoFrame(currentUser).setVisible(true);
                break;
            case "📊 用户统计":
                showUserStatistics();
                break;
            case "🔍 搜索用户":
                searchUser();
                break;
            case "🔄 刷新数据":
                refreshData();
                break;
            case "🚪 退出登录":
                logout();
                break;
        }
    }
    
    private void showUserStatistics() {
        int totalUsers = userService.getTotalUserCount();
        JOptionPane.showMessageDialog(this,
            "📊 用户统计信息\n\n" +
            "总用户数: " + totalUsers + "\n" +
            "当前用户ID: " + currentUser.getUserId() + "\n" +
            "当前用户名: " + currentUser.getUsername(),
            "统计信息",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void searchUser() {
        String input = JOptionPane.showInputDialog(this, "请输入要搜索的用户名：");
        if (input != null && !input.trim().isEmpty()) {
            User foundUser = userService.loginUser(input.trim());
            if (foundUser != null) {
                new UserDetailFrame(foundUser).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "❌ 未找到用户：" + input,
                    "提示",
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    private void refreshData() {
        User updatedUser = userService.getUserById(currentUser.getUserId());
        if (updatedUser != null) {
            this.currentUser = updatedUser;
            JOptionPane.showMessageDialog(this,
                "✅ 数据已刷新！",
                "提示",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this,
            "确定要退出登录吗？",
            "确认",
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                User testUser = new User();
                testUser.setUserId(1);
                testUser.setUsername("测试用户");
                
                new MainFrame(testUser).setVisible(true);
            }
        });
    }
}
