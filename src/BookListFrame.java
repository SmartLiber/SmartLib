import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * 浏览/搜索图书界面
 * 对应后端方法：BookService.searchBooks() / BookDAO.getAllBooks()
 */
public class BookListFrame extends JFrame {
    private BookService bookService;
    private BookCategoryDAO categoryDAO;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> categoryCombo;
    private JLabel statsLabel;
    private User currentUser;

    private static final String[] COLUMNS = {"ID", "书名", "作者", "分类", "状态"};

    public BookListFrame(User user) {
        this.currentUser = user;
        this.bookService = new BookService();
        this.categoryDAO = new BookCategoryDAO();

        setTitle("SmartLib - 浏览图书");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        loadAllBooks();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 顶部：搜索 + 分类筛选
        JPanel topPanel = new JPanel(new BorderLayout(10, 5));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.add(new JLabel("搜索："));
        searchField = new JTextField(20);
        searchField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(250, 32));
        searchPanel.add(searchField);

        JButton searchBtn = new JButton("搜索");
        searchBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        searchBtn.addActionListener(e -> doSearch());
        searchPanel.add(searchBtn);

        searchPanel.add(new JLabel("  分类："));
        categoryCombo = new JComboBox<>();
        categoryCombo.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        categoryCombo.setPreferredSize(new Dimension(140, 32));
        categoryCombo.addItem("全部");
        loadCategoryCombo();
        categoryCombo.addActionListener(e -> filterByCategory());
        searchPanel.add(categoryCombo);

        JButton refreshBtn = new JButton("刷新全部");
        refreshBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        refreshBtn.addActionListener(e -> {
            searchField.setText("");
            categoryCombo.setSelectedItem("全部");
            loadAllBooks();
        });
        searchPanel.add(refreshBtn);

        topPanel.add(searchPanel, BorderLayout.WEST);

        statsLabel = new JLabel("共 0 本书");
        statsLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        statsLabel.setForeground(Color.GRAY);
        topPanel.add(statsLabel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // 中间：图书表格
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        bookTable.setRowHeight(30);
        bookTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        bookTable.getTableHeader().setReorderingAllowed(false);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(80);

        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openBookDetail();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(bookTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 底部按钮
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton detailBtn = new JButton("查看详情");
        detailBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        detailBtn.addActionListener(e -> openBookDetail());
        bottomPanel.add(detailBtn);

        JButton borrowBtn = new JButton("借阅此书");
        borrowBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        borrowBtn.addActionListener(e -> borrowBook());
        bottomPanel.add(borrowBtn);

        JButton returnBtn = new JButton("归还此书");
        returnBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        returnBtn.addActionListener(e -> returnBook());
        bottomPanel.add(returnBtn);

        JButton closeBtn = new JButton("关闭");
        closeBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        closeBtn.addActionListener(e -> dispose());
        bottomPanel.add(closeBtn);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // 回车键触发搜索
        searchField.addActionListener(e -> doSearch());
    }

    private void loadCategoryCombo() {
        List<BookCategory> categories = categoryDAO.getAllCategories();
        for (BookCategory c : categories) {
            categoryCombo.addItem(c.getCategoryId() + "-" + c.getCategoryName());
        }
    }

    private void loadAllBooks() {
        try {
            List<Book> books = bookService.searchBooks("");
            if (books.isEmpty()) {
                books = new BookDAO().getAllBooks();
            }
            populateTable(books);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadAllBooks();
            return;
        }
        try {
            List<Book> books = bookService.searchBooks(keyword);
            populateTable(books);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "搜索失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterByCategory() {
        String selected = (String) categoryCombo.getSelectedItem();
        if (selected == null || "全部".equals(selected)) {
            loadAllBooks();
            return;
        }
        try {
            String categoryName = selected.substring(selected.indexOf("-") + 1);
            BookCategory cat = categoryDAO.getCategoryByName(categoryName);
            if (cat != null) {
                List<Book> books = categoryDAO.getBooksByCategory(cat.getCategoryId());
                populateTable(books);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void populateTable(List<Book> books) {
        tableModel.setRowCount(0);
        if (books == null) return;
        for (Book b : books) {
            String catName = "";
            BookCategory cat = categoryDAO.getCategoryById(b.getCategoryId());
            if (cat != null) catName = cat.getCategoryName();

            String statusDisplay = b.getStatus();
            if ("已借出".equals(b.getStatus())) {
                statusDisplay = "已借出(user:" + b.getBorrowUserId() + ")";
            }

            tableModel.addRow(new Object[]{b.getId(), b.getName(), b.getAuthor(), catName, statusDisplay});
        }
        statsLabel.setText("共 " + books.size() + " 本书");
    }

    private int getSelectedBookId() {
        int row = bookTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先选择一本书！", "提示", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
        return (Integer) tableModel.getValueAt(row, 0);
    }

    private void openBookDetail() {
        int bookId = getSelectedBookId();
        if (bookId > 0) {
            new BookDetailFrame(bookId, currentUser).setVisible(true);
        }
    }

    private void borrowBook() {
        int bookId = getSelectedBookId();
        if (bookId <= 0) return;

        Book book = bookService.getBookFullInfo(bookId);
        if (book == null) return;

        if (!"可借阅".equals(book.getStatus())) {
            JOptionPane.showMessageDialog(this, "该书当前状态为【" + book.getStatus() + "】，不可借阅！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "确定借阅《" + book.getName() + "》吗？",
                "确认借阅", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) return;

        UserService us = new UserService();
        boolean ok = us.borrowBookWithValidation(currentUser.getUserId(), bookId);
        if (ok) {
            JOptionPane.showMessageDialog(this, "借阅成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            loadAllBooks();
        } else {
            JOptionPane.showMessageDialog(this, "借阅失败！可能原因：\n1. 您有未还书籍\n2. 书籍状态已变更", "失败", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void returnBook() {
        int bookId = getSelectedBookId();
        if (bookId <= 0) return;

        Book book = bookService.getBookFullInfo(bookId);
        if (book == null) return;

        if (!"已借出".equals(book.getStatus())) {
            JOptionPane.showMessageDialog(this, "该书状态为【" + book.getStatus() + "】，无需归还！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 只能还自己借的书
        if (book.getBorrowUserId() == null || !book.getBorrowUserId().equals(currentUser.getUserId())) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "该书由用户(ID:" + book.getBorrowUserId() + ")借出，确定代还吗？",
                    "确认归还", JOptionPane.YES_NO_OPTION);
            if (choice != JOptionPane.YES_OPTION) return;
        }

        boolean ok = bookService.returnBook(bookId);
        if (ok) {
            JOptionPane.showMessageDialog(this, "归还成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            loadAllBooks();
        } else {
            JOptionPane.showMessageDialog(this, "归还失败！", "失败", JOptionPane.ERROR_MESSAGE);
        }
    }
}
