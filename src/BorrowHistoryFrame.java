import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 借阅历史界面
 * 开发人员A - Swing前端任务5/8
 * 对应后端方法：UserService.getUserBorrowHistory()
 */
public class BorrowHistoryFrame extends JFrame {
    private int userId;
    private UserService userService;
    private JTable bookTable;
    private Object[][] tableData;
    private String[] columnNames = {"书籍ID", "书名", "作者", "借阅时间"};

    public BorrowHistoryFrame(int userId) {
        this.userId = userId;
        this.userService = new UserService();

        setTitle("SmartLib - 借阅历史");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        loadBorrowHistory();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("📖 借阅历史 - 用户ID: " + userId);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(new Color(41, 128, 185));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        tableData = new Object[0][4];
        bookTable = new JTable(tableData, columnNames);
        bookTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        bookTable.setRowHeight(30);
        bookTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(bookTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton refreshButton = new JButton("🔄 刷新");
        refreshButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        refreshButton.addActionListener(e -> loadBorrowHistory());

        JButton closeButton = new JButton("❌ 关闭");
        closeButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadBorrowHistory() {
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
                tableData[i][0] = book.getId();
                tableData[i][1] = book.getName();
                tableData[i][2] = book.getAuthor();
                tableData[i][3] = book.getBorrowDate();
            }

            bookTable.setModel(new javax.swing.table.DefaultTableModel(tableData, columnNames));

            JOptionPane.showMessageDialog(this,
                    "✅ 共找到 " + books.size() + " 条借阅记录",
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
            new BorrowHistoryFrame(1).setVisible(true);
        });
    }
}
