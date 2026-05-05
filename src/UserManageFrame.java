import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 用户管理界面（整合用户列表 + 用户统计）
 */
public class UserManageFrame extends JFrame {
    private UserService userService;
    private JTable userTable;
    private Object[][] tableData;
    private String[] columnNames = {"用户ID", "用户名", "手机号"};
    private JLabel statsLabel;

    public UserManageFrame() {
        this.userService = new UserService();
        
        setTitle("SmartLib - 用户管理");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initUI();
        loadUserData();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 标题面板
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("👥 用户管理");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(new Color(41, 128, 185));
        titlePanel.add(titleLabel);
        
        // 统计信息面板
        statsLabel = new JLabel("总用户数: 0");
        statsLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        statsLabel.setForeground(Color.GRAY);
        titlePanel.add(statsLabel);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // 表格面板
        tableData = new Object[0][3];
        userTable = new JTable(tableData, columnNames);
        userTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        userTable.setRowHeight(30);
        userTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton refreshButton = new JButton("🔄 刷新");
        refreshButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        refreshButton.addActionListener(e -> loadUserData());
        
        JButton closeButton = new JButton("❌ 关闭");
        closeButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private void loadUserData() {
        try {
            List<User> users = userService.getAllUsers();
            
            // 更新表格数据
            tableData = new Object[users.size()][3];
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                tableData[i][0] = user.getUserId();
                tableData[i][1] = user.getUsername();
                tableData[i][2] = user.getPhone();
            }
            
            userTable.setModel(new javax.swing.table.DefaultTableModel(tableData, columnNames));
            
            // 更新统计信息
            statsLabel.setText("总用户数: " + users.size());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "❌ 加载失败：" + e.getMessage(),
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
            new UserManageFrame().setVisible(true);
        });
    }
}
