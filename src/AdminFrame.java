import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 后台管理界面
 * 包含图书管理和分类管理两个标签页
 */
public class AdminFrame extends JFrame {
    private BookService bookService;
    private BookCategoryDAO categoryDAO;
    private BookDAO bookDAO;
    private User currentUser;

    // 图书管理组件
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private static final String[] BOOK_COLS = {"ID", "书名", "作者", "分类", "状态"};

    // 分类管理组件
    private JTable catTable;
    private DefaultTableModel catTableModel;
    private static final String[] CAT_COLS = {"分类ID", "分类名称", "图书数量"};

    public AdminFrame(User currentUser) {
        this.currentUser = currentUser;
        this.bookService = new BookService();
        this.categoryDAO = new BookCategoryDAO();
        this.bookDAO = new BookDAO();

        setTitle("SmartLib - 后台管理");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        loadBookData();
    }

    private void initUI() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        tabbedPane.addTab("图书管理", createBookPanel());
        tabbedPane.addTab("分类管理", createCategoryPanel());

        add(tabbedPane);
    }

    // ==================== 图书管理面板 ====================

    private JPanel createBookPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("图书管理");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titleLabel.setForeground(new Color(41, 128, 185));
        panel.add(titleLabel, BorderLayout.NORTH);

        bookTableModel = new DefaultTableModel(BOOK_COLS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(bookTableModel);
        bookTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        bookTable.setRowHeight(30);
        bookTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(80);

        panel.add(new JScrollPane(bookTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        Color btnDark = new Color(50, 50, 60);

        JButton addBtn = new JButton("添加图书");
        addBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        addBtn.setBackground(new Color(120, 180, 230));
        addBtn.setForeground(btnDark);
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> showBookEditDialog(null));
        btnPanel.add(addBtn);

        JButton editBtn = new JButton("编辑图书");
        editBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        editBtn.setBackground(new Color(120, 190, 140));
        editBtn.setForeground(btnDark);
        editBtn.setFocusPainted(false);
        editBtn.addActionListener(e -> {
            int bookId = getSelectedBookId();
            if (bookId > 0) {
                Book book = bookDAO.getBookById(bookId);
                if (book != null) showBookEditDialog(book);
            }
        });
        btnPanel.add(editBtn);

        JButton delBtn = new JButton("删除图书");
        delBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        delBtn.setBackground(new Color(230, 130, 120));
        delBtn.setForeground(btnDark);
        delBtn.setFocusPainted(false);
        delBtn.addActionListener(e -> deleteBook());
        btnPanel.add(delBtn);

        JButton detailBtn = new JButton("查看详情");
        detailBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        detailBtn.setBackground(new Color(240, 240, 245));
        detailBtn.setForeground(btnDark);
        detailBtn.setFocusPainted(false);
        detailBtn.addActionListener(e -> {
            int bookId = getSelectedBookId();
            if (bookId > 0) new BookDetailFrame(bookId, currentUser).setVisible(true);
        });
        btnPanel.add(detailBtn);

        JButton refreshBtn = new JButton("刷新");
        refreshBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        refreshBtn.setBackground(new Color(240, 240, 245));
        refreshBtn.setForeground(btnDark);
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> loadBookData());
        btnPanel.add(refreshBtn);

        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadBookData() {
        bookTableModel.setRowCount(0);
        List<Book> books = bookDAO.getAllBooks();
        for (Book b : books) {
            String catName = "";
            BookCategory cat = categoryDAO.getCategoryById(b.getCategoryId());
            if (cat != null) catName = cat.getCategoryName();
            bookTableModel.addRow(new Object[]{b.getId(), b.getName(), b.getAuthor(), catName, b.getStatus()});
        }
    }

    private int getSelectedBookId() {
        int row = bookTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先选择一本书！", "提示", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
        return (Integer) bookTableModel.getValueAt(row, 0);
    }

    private void showBookEditDialog(Book book) {
        JDialog dialog = new JDialog(this, book == null ? "添加图书" : "编辑图书", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JTextField nameField = new JTextField(book != null ? book.getName() : "", 20);
        JTextField authorField = new JTextField(book != null ? book.getAuthor() : "", 20);
        JComboBox<String> catCombo = new JComboBox<>();
        List<BookCategory> categories = categoryDAO.getAllCategories();
        for (BookCategory c : categories) {
            catCombo.addItem(c.getCategoryId() + "-" + c.getCategoryName());
        }
        if (book != null) {
            BookCategory cat = categoryDAO.getCategoryById(book.getCategoryId());
            if (cat != null) catCombo.setSelectedItem(cat.getCategoryId() + "-" + cat.getCategoryName());
        }
        JTextArea introArea = new JTextArea(book != null ? book.getIntroduction() : "", 4, 20);
        introArea.setLineWrap(true);
        introArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        JScrollPane introScroll = new JScrollPane(introArea);

        Font f = new Font("微软雅黑", Font.PLAIN, 14);
        nameField.setFont(f); authorField.setFont(f); catCombo.setFont(f);

        int row = 0;
        addFormRow(formPanel, gbc, row++, "书名：", nameField);
        addFormRow(formPanel, gbc, row++, "作者：", authorField);
        addFormRow(formPanel, gbc, row++, "分类：", catCombo);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("简介："), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(introScroll, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton saveBtn = new JButton("保存");
        saveBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        saveBtn.setPreferredSize(new Dimension(100, 36));
        JButton cancelBtn = new JButton("取消");
        cancelBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        cancelBtn.setPreferredSize(new Dimension(100, 36));

        saveBtn.addActionListener(ev -> {
            String name = nameField.getText().trim();
            String author = authorField.getText().trim();
            String catStr = (String) catCombo.getSelectedItem();
            String intro = introArea.getText().trim();

            if (name.isEmpty() || author.isEmpty() || catStr == null) {
                JOptionPane.showMessageDialog(dialog, "请填写完整信息！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int catId = Integer.parseInt(catStr.split("-")[0]);

            boolean ok;
            if (book == null) {
                ok = bookService.addNewBook(name, author, catId, intro);
            } else {
                ok = bookService.modifyBookInfo(book.getId(), name, author, catId, intro);
            }

            if (ok) {
                JOptionPane.showMessageDialog(dialog, book == null ? "添加成功！" : "更新成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadBookData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "操作失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(ev -> dialog.dispose());
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void deleteBook() {
        int bookId = getSelectedBookId();
        if (bookId <= 0) return;

        int choice = JOptionPane.showConfirmDialog(this,
                "确定删除该书吗？\n注意：只能删除「可借阅」状态的图书。",
                "确认删除", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) return;

        boolean ok = bookService.removeBook(bookId);
        if (ok) {
            JOptionPane.showMessageDialog(this, "删除成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            loadBookData();
        } else {
            JOptionPane.showMessageDialog(this, "删除失败！只能删除「可借阅」状态的图书。", "失败", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================== 分类管理面板 ====================

    private JPanel createCategoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("分类管理");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titleLabel.setForeground(new Color(41, 128, 185));
        panel.add(titleLabel, BorderLayout.NORTH);

        catTableModel = new DefaultTableModel(CAT_COLS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        catTable = new JTable(catTableModel);
        catTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        catTable.setRowHeight(30);
        catTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        catTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(new JScrollPane(catTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        Color btnDark = new Color(50, 50, 60);

        JButton addCatBtn = new JButton("添加分类");
        addCatBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        addCatBtn.setBackground(new Color(120, 180, 230));
        addCatBtn.setForeground(btnDark);
        addCatBtn.setFocusPainted(false);
        addCatBtn.addActionListener(e -> addCategory());
        btnPanel.add(addCatBtn);

        JButton delCatBtn = new JButton("删除分类");
        delCatBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        delCatBtn.setBackground(new Color(230, 130, 120));
        delCatBtn.setForeground(btnDark);
        delCatBtn.setFocusPainted(false);
        delCatBtn.addActionListener(e -> deleteCategory());
        btnPanel.add(delCatBtn);

        JButton refreshCatBtn = new JButton("刷新");
        refreshCatBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        refreshCatBtn.setBackground(new Color(240, 240, 245));
        refreshCatBtn.setForeground(btnDark);
        refreshCatBtn.setFocusPainted(false);
        refreshCatBtn.addActionListener(e -> loadCategoryData());
        btnPanel.add(refreshCatBtn);

        panel.add(btnPanel, BorderLayout.SOUTH);

        loadCategoryData();
        return panel;
    }

    private void loadCategoryData() {
        catTableModel.setRowCount(0);
        List<BookCategory> categories = categoryDAO.getAllCategories();
        for (BookCategory c : categories) {
            List<Book> books = categoryDAO.getBooksByCategory(c.getCategoryId());
            catTableModel.addRow(new Object[]{c.getCategoryId(), c.getCategoryName(), books.size()});
        }
    }

    private void addCategory() {
        String name = JOptionPane.showInputDialog(this, "请输入分类名称：", "添加分类", JOptionPane.QUESTION_MESSAGE);
        if (name == null || name.trim().isEmpty()) return;

        boolean ok = bookService.addCategory(name.trim());
        if (ok) {
            JOptionPane.showMessageDialog(this, "添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            loadCategoryData();
        } else {
            JOptionPane.showMessageDialog(this, "添加失败！分类名可能已存在。", "失败", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCategory() {
        int row = catTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的分类！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int catId = (Integer) catTableModel.getValueAt(row, 0);
        String catName = (String) catTableModel.getValueAt(row, 1);
        int bookCount = (Integer) catTableModel.getValueAt(row, 2);

        if (bookCount > 0) {
            JOptionPane.showMessageDialog(this,
                    "该分类下有 " + bookCount + " 本书，无法删除！\n请先移走或删除这些图书。",
                    "无法删除", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "确定删除分类「" + catName + "」吗？",
                "确认删除", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) return;

        boolean ok = bookService.removeCategory(catId);
        if (ok) {
            JOptionPane.showMessageDialog(this, "删除成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            loadCategoryData();
        } else {
            JOptionPane.showMessageDialog(this, "删除失败！", "失败", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================== 工具方法 ====================

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent comp) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("微软雅黑", Font.BOLD, 14));
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(comp, gbc);
    }
}
