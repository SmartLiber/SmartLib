import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 借阅管理界面
 * 显示当前用户借阅的图书，支持归还操作
 */
public class BorrowHistoryFrame extends JFrame {
    private int userId;
    private UserService userService;
    private BookService bookService;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private String[] columnNames = {"书籍ID", "书名", "作者", "借阅日期"};

    public BorrowHistoryFrame(int userId) {
        this.userId = userId;
        this.userService = new UserService();
        this.bookService = new BookService();

        setTitle("SmartLib - 我的借阅");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        loadBorrowData();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("我的借阅 - 用户ID: " + userId);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(new Color(41, 128, 185));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        bookTable.setRowHeight(30);
        bookTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton detailBtn = new JButton("查看详情");
        detailBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        detailBtn.addActionListener(e -> {
            int bookId = getSelectedBookId();
            if (bookId > 0) {
                User currentUser = userService.getUserById(userId);
                new BookDetailFrame(bookId, currentUser).setVisible(true);
            }
        });
        buttonPanel.add(detailBtn);

        JButton returnBtn = new JButton("归还");
        returnBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        returnBtn.addActionListener(e -> handleReturn());
        buttonPanel.add(returnBtn);

        JButton refreshBtn = new JButton("刷新");
        refreshBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        refreshBtn.addActionListener(e -> loadBorrowData());
        buttonPanel.add(refreshBtn);

        JButton closeBtn = new JButton("关闭");
        closeBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        closeBtn.addActionListener(e -> dispose());
        buttonPanel.add(closeBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadBorrowData() {
        try {
            List<Book> books = userService.getUserBorrowHistory(userId);
            tableModel.setRowCount(0);

            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(this, "暂无借阅记录", "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (Book book : books) {
                tableModel.addRow(new Object[]{
                        book.getId(), book.getName(), book.getAuthor(), book.getBorrowDate()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "加载失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private int getSelectedBookId() {
        int row = bookTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先选择一本书！", "提示", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
        return (Integer) tableModel.getValueAt(row, 0);
    }

    private void handleReturn() {
        int bookId = getSelectedBookId();
        if (bookId <= 0) return;

        Book book = bookService.getBookFullInfo(bookId);
        if (book == null) return;

        int choice = JOptionPane.showConfirmDialog(this,
                "确定归还《" + book.getName() + "》吗？",
                "确认归还", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) return;

        boolean ok = bookService.returnBook(bookId);
        if (ok) {
            JOptionPane.showMessageDialog(this, "归还成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            loadBorrowData();
        } else {
            JOptionPane.showMessageDialog(this, "归还失败！", "失败", JOptionPane.ERROR_MESSAGE);
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
