import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 用户列表界面
 * 开发人员A - Swing前端任务4/8
 * 对应后端方法：UserService.getAllUsers()
 */
public class UserListFrame extends JFrame {
    private UserService userService;
    private JTable userTable;
    private Object[][] tableData;
    private String[] columnNames = {"用户ID", "用户名", "手机号"};

    public UserListFrame() {
        this.userService = new UserService();
        
        setTitle("SmartLib - 用户列表");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initUI();
        loadUserData();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("📚 用户列表");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(new Color(41, 128, 185));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        tableData = new Object[0][3];
        userTable = new JTable(tableData, columnNames);
        userTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        userTable.setRowHeight(30);
        userTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
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
            
            tableData = new Object[users.size()][3];
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                tableData[i][0] = user.getUserId();
                tableData[i][1] = user.getUsername();
                tableData[i][2] = user.getPhone();
            }
            
            userTable.setModel(new javax.swing.table.DefaultTableModel(tableData, columnNames));
            
            JOptionPane.showMessageDialog(this,
                "✅ 共加载 " + users.size() + " 个用户",
                "提示",
                JOptionPane.INFORMATION_MESSAGE);
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
            new UserListFrame().setVisible(true);
        });
    }
}
