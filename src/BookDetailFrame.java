import javax.swing.*;
import java.awt.*;

/**
 * 图书详情界面
 * 对应后端方法：BookService.getBookFullInfo()
 */
public class BookDetailFrame extends JFrame {
    private int bookId;
    private User currentUser;
    private BookService bookService;
    private UserService userService;

    private JLabel nameLabel, authorLabel, categoryLabel, introLabel, statusLabel;
    private JLabel borrowUserLabel, borrowDateLabel;
    private JButton borrowBtn, returnBtn, renewBtn, refreshBtn, closeBtn;

    public BookDetailFrame(int bookId, User currentUser) {
        this.bookId = bookId;
        this.currentUser = currentUser;
        this.bookService = new BookService();
        this.userService = new UserService();

        setTitle("SmartLib - 图书详情");
        setSize(550, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        loadBookDetail();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("图书详情");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setForeground(new Color(41, 128, 185));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        infoPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("微软雅黑", Font.BOLD, 14);
        Font valueFont = new Font("微软雅黑", Font.PLAIN, 14);

        int row = 0;
        addInfoRow(infoPanel, gbc, row++, "书名：", labelFont, valueFont);
        nameLabel = (JLabel) infoPanel.getComponent(infoPanel.getComponentCount() - 1);
        addInfoRow(infoPanel, gbc, row++, "作者：", labelFont, valueFont);
        authorLabel = (JLabel) infoPanel.getComponent(infoPanel.getComponentCount() - 1);
        addInfoRow(infoPanel, gbc, row++, "分类：", labelFont, valueFont);
        categoryLabel = (JLabel) infoPanel.getComponent(infoPanel.getComponentCount() - 1);
        addInfoRow(infoPanel, gbc, row++, "状态：", labelFont, valueFont);
        statusLabel = (JLabel) infoPanel.getComponent(infoPanel.getComponentCount() - 1);
        addInfoRow(infoPanel, gbc, row++, "简介：", labelFont, valueFont);
        introLabel = (JLabel) infoPanel.getComponent(infoPanel.getComponentCount() - 1);

        // 分隔
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        infoPanel.add(new JSeparator(), gbc);

        addInfoRow(infoPanel, gbc, row++, "借阅人：", labelFont, valueFont);
        borrowUserLabel = (JLabel) infoPanel.getComponent(infoPanel.getComponentCount() - 1);
        addInfoRow(infoPanel, gbc, row++, "借阅日期：", labelFont, valueFont);
        borrowDateLabel = (JLabel) infoPanel.getComponent(infoPanel.getComponentCount() - 1);

        mainPanel.add(infoPanel, BorderLayout.CENTER);

        // 按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        Color btnDark = new Color(50, 50, 60);

        borrowBtn = new JButton("借阅");
        borrowBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        borrowBtn.setPreferredSize(new Dimension(100, 36));
        borrowBtn.setBackground(new Color(41, 128, 185));
        borrowBtn.setForeground(Color.WHITE);
        borrowBtn.setFocusPainted(false);
        borrowBtn.addActionListener(e -> handleBorrow());
        buttonPanel.add(borrowBtn);

        returnBtn = new JButton("归还");
        returnBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        returnBtn.setPreferredSize(new Dimension(100, 36));
        returnBtn.setBackground(new Color(200, 60, 50));
        returnBtn.setForeground(Color.WHITE);
        returnBtn.setFocusPainted(false);
        returnBtn.addActionListener(e -> handleReturn());
        buttonPanel.add(returnBtn);

        renewBtn = new JButton("续借");
        renewBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        renewBtn.setPreferredSize(new Dimension(100, 36));
        renewBtn.setBackground(new Color(46, 134, 70));
        renewBtn.setForeground(Color.WHITE);
        renewBtn.setFocusPainted(false);
        renewBtn.addActionListener(e -> handleRenew());
        buttonPanel.add(renewBtn);

        refreshBtn = new JButton("刷新");
        refreshBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        refreshBtn.setPreferredSize(new Dimension(100, 36));
        refreshBtn.setBackground(new Color(240, 240, 245));
        refreshBtn.setForeground(btnDark);
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> loadBookDetail());
        buttonPanel.add(refreshBtn);

        closeBtn = new JButton("关闭");
        closeBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        closeBtn.setPreferredSize(new Dimension(100, 36));
        closeBtn.setBackground(new Color(240, 240, 245));
        closeBtn.setForeground(btnDark);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dispose());
        buttonPanel.add(closeBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addInfoRow(JPanel panel, GridBagConstraints gbc, int row,
                            String labelText, Font labelFont, Font valueFont) {
        Color darkColor = new Color(50, 50, 60);
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(labelFont);
        lbl.setForeground(darkColor);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JLabel val = new JLabel("-");
        val.setFont(valueFont);
        val.setForeground(darkColor);
        panel.add(val, gbc);
    }

    private void loadBookDetail() {
        try {
            Book book = bookService.getBookFullInfo(bookId);
            if (book == null) {
                JOptionPane.showMessageDialog(this, "书籍不存在！", "错误", JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }

            setTitle("SmartLib - " + book.getName() + " 详情");
            nameLabel.setText(book.getName());
            authorLabel.setText(book.getAuthor());

            BookCategory cat = book.getCategory();
            categoryLabel.setText(cat != null ? cat.getCategoryName() : "无分类");

            String status = book.getStatus();
            statusLabel.setText(status);
            if ("可借阅".equals(status)) {
                statusLabel.setForeground(new Color(46, 204, 113));
            } else if ("已借出".equals(status)) {
                statusLabel.setForeground(Color.RED);
            } else {
                statusLabel.setForeground(Color.ORANGE);
            }

            introLabel.setText("<html>" + (book.getIntroduction() != null ? book.getIntroduction() : "无")
                    + "</html>");

            if (book.getBorrowUser() != null) {
                borrowUserLabel.setText(book.getBorrowUser().getUsername() + " (ID:" + book.getBorrowUserId() + ")");
            } else {
                borrowUserLabel.setText("无");
            }
            borrowDateLabel.setText(book.getBorrowDate() != null ? book.getBorrowDate() : "无");

            // 按钮状态
            updateButtonStates(status, book.getBorrowUserId());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "加载失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateButtonStates(String status, Integer borrowUserId) {
        if ("可借阅".equals(status)) {
            borrowBtn.setEnabled(true);
            returnBtn.setEnabled(false);
            renewBtn.setEnabled(false);
        } else if ("已借出".equals(status)) {
            borrowBtn.setEnabled(false);
            returnBtn.setEnabled(true);
            // 只有借阅者本人才能续借
            boolean isMine = borrowUserId != null && borrowUserId.equals(currentUser.getUserId());
            renewBtn.setEnabled(isMine);
        } else {
            borrowBtn.setEnabled(false);
            returnBtn.setEnabled(false);
            renewBtn.setEnabled(false);
        }
    }

    private void handleBorrow() {
        int choice = JOptionPane.showConfirmDialog(this,
                "确定借阅该书吗？",
                "确认借阅", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) return;

        boolean ok = userService.borrowBookWithValidation(currentUser.getUserId(), bookId);
        if (ok) {
            JOptionPane.showMessageDialog(this, "借阅成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            loadBookDetail();
        } else {
            JOptionPane.showMessageDialog(this, "借阅失败！可能原因：\n1. 您有未还书籍\n2. 书籍状态已变更",
                    "失败", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleReturn() {
        int choice = JOptionPane.showConfirmDialog(this,
                "确定归还该书吗？",
                "确认归还", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) return;

        boolean ok = bookService.returnBook(bookId);
        if (ok) {
            JOptionPane.showMessageDialog(this, "归还成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            loadBookDetail();
        } else {
            JOptionPane.showMessageDialog(this, "归还失败！", "失败", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRenew() {
        String input = JOptionPane.showInputDialog(this,
                "请输入续借天数（1-30天）：",
                "续借",
                JOptionPane.QUESTION_MESSAGE);

        if (input == null || input.trim().isEmpty()) return;

        try {
            int days = Integer.parseInt(input.trim());
            boolean ok = bookService.renewBook(bookId, days);
            if (ok) {
                JOptionPane.showMessageDialog(this, "续借成功！续借 " + days + " 天。", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadBookDetail();
            } else {
                JOptionPane.showMessageDialog(this, "续借失败！天数需在1-30之间。", "失败", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "请输入有效数字！", "错误", JOptionPane.WARNING_MESSAGE);
        }
    }
}
