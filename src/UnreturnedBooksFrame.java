import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 未还书籍界面
 * 开发人员A - Swing前端任务6/8
 * 对应后端方法：UserService.hasUnreturnedBooks() 和 getUserBorrowHistory()
 */
public class UnreturnedBooksFrame extends JFrame {
    private int userId;
    private UserService userService;
    private JTable bookTable;
    private Object[][] tableData;
    private String[] columnNames = {"书籍ID", "书名", "作者", "借阅时间"};

    public UnreturnedBooksFrame(int userId) {
        this.userId = userId;
        this.userService = new UserService();
        
        setTitle("SmartLib - 未还书籍");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initUI();
        loadUnreturnedBooks();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("⚠️ 未还书籍 - 用户ID: " + userId);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(Color.RED);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        tableData = new Object[0][4];
        bookTable = new JTable(tableData, columnNames);
        bookTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        bookTable.setRowHeight(30);
        bookTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(bookTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton checkButton = new JButton("🔍 检查状态");
        checkButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        checkButton.addActionListener(e -> checkUnreturnedStatus());
        
        JButton refreshButton = new JButton("🔄 刷新");
        refreshButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        refreshButton.addActionListener(e -> loadUnreturnedBooks());
        
        JButton closeButton = new JButton("❌ 关闭");
        closeButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(checkButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private void checkUnreturnedStatus() {
        try {
            boolean hasUnreturned = userService.hasUnreturnedBooks(userId);
            
            if (hasUnreturned) {
                JOptionPane.showMessageDialog(this,
                    "⚠️ 您有未归还的书籍！\n\n请尽快归还。",
                    "警告",
                    JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "✅ 您没有未归还的书籍。",
                    "提示",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "❌ 检查失败：" + e.getMessage(),
                "错误",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadUnreturnedBooks() {
        try {
            List<Book> books = userService.getUserBorrowHistory(userId);
            
            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "📭 暂无借阅记录",
                    "提示",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            tableData = new Object[books.size()][4];
            for (int i = 0; i < books.size(); i++) {
                Book book = books.get(i);
                tableData[i][0] = book.getBookId();
                tableData[i][1] = book.getName();
                tableData[i][2] = book.getAuthor();
                tableData[i][3] = book.getBorrowTime();
            }
            
            bookTable.setModel(new javax.swing.table.DefaultTableModel(tableData, columnNames));
            
            JOptionPane.showMessageDialog(this,
                "✅ 共找到 " + books.size() + " 本未还书籍",
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
            new UnreturnedBooksFrame(1).setVisible(true);
        });
    }
}
