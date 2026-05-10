import javax.swing.*;
import java.awt.*;

/**
 * 系统主界面（重新排版版）
 * 开发人员A - Swing前端任务3/8
 */
public class MainFrame extends JFrame {
    private User currentUser;
    private UserService userService;

    public MainFrame(User user) {
        this.currentUser = user;
        this.userService = new UserService();

        setTitle("SmartLib - 图书馆管理系统");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initMenuBar();
        initUI();
    }

    /**
     * 初始化顶部菜单栏
     */
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // === 1. 常用功能（不变）===
        JMenu menuCommon = new JMenu("常用功能");
        menuCommon.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JMenuItem itemBrowseBooks = new JMenuItem("浏览图书");
        itemBrowseBooks.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemBrowseBooks.addActionListener(e -> browseBooks());

        JMenuItem itemSearchBooks = new JMenuItem("智能小助手");
        itemSearchBooks.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemSearchBooks.addActionListener(e -> openAiAgent());

        menuCommon.add(itemBrowseBooks);
        menuCommon.add(itemSearchBooks);

        // === 2. 个人中心（我的空间 + 借阅管理）===
        JMenu menuPersonal = new JMenu("个人中心");
        menuPersonal.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        // 我的空间（带子菜单：个人信息、编辑信息、登录、退出登录）
        JMenu subMenuMySpace = new JMenu("我的空间");
        subMenuMySpace.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        // subMenuMySpace.setIcon removed - material/ not available

        JMenuItem itemPersonalInfo = new JMenuItem("个人信息");
        itemPersonalInfo.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemPersonalInfo.addActionListener(e -> showPersonalInfo());

        JMenuItem itemEditInfo = new JMenuItem("编辑信息");
        itemEditInfo.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemEditInfo.addActionListener(e -> openEditInfo());

        JMenuItem itemLogin = new JMenuItem("登录");
        itemLogin.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemLogin.addActionListener(e -> reLogin());

        JMenuItem itemLogout = new JMenuItem("退出登录");
        itemLogout.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemLogout.addActionListener(e -> logout());

        subMenuMySpace.add(itemPersonalInfo);
        subMenuMySpace.add(itemEditInfo);
        subMenuMySpace.addSeparator();
        subMenuMySpace.add(itemLogin);
        subMenuMySpace.add(itemLogout);

        // 借阅管理
        JMenuItem itemBorrowManage = new JMenuItem("借阅管理");
        itemBorrowManage.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemBorrowManage.addActionListener(e -> openBorrowManage());

        menuPersonal.add(subMenuMySpace);
        menuPersonal.add(itemBorrowManage);

        // === 3. 系统维护（后台管理 + 用户管理 + 刷新数据）===
        JMenu menuSystem = new JMenu("系统维护");
        menuSystem.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JMenuItem itemAdmin = new JMenuItem("后台管理");
        itemAdmin.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemAdmin.addActionListener(e -> openAdmin());

        JMenu subMenuUserManage = new JMenu("用户管理");
        subMenuUserManage.setFont(new Font("微软雅黑", Font.PLAIN, 13));

        JMenuItem itemUserList = new JMenuItem("用户列表");
        itemUserList.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemUserList.addActionListener(e -> openUserList());

        JMenuItem itemUserStats = new JMenuItem("用户统计");
        itemUserStats.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemUserStats.addActionListener(e -> showUserStats());

        subMenuUserManage.add(itemUserList);
        subMenuUserManage.add(itemUserStats);

        JMenuItem itemRefresh = new JMenuItem("刷新数据");
        itemRefresh.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemRefresh.addActionListener(e -> refreshData());

        menuSystem.add(itemAdmin);
        menuSystem.add(subMenuUserManage);
        menuSystem.addSeparator();
        menuSystem.add(itemRefresh);

        // === 4. 帮助（不变）===
        JMenu menuHelp = new JMenu("帮助");
        menuHelp.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JMenuItem itemHelpInfo = new JMenuItem("帮助信息");
        itemHelpInfo.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemHelpInfo.addActionListener(e -> showHelp());

        JMenuItem itemAbout = new JMenuItem("关于系统");
        itemAbout.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemAbout.addActionListener(e -> showAbout());

        menuHelp.add(itemHelpInfo);
        menuHelp.add(itemAbout);

        // 添加所有菜单到菜单栏（系统维护和帮助在最左边）
        menuBar.add(menuSystem);
        menuBar.add(menuHelp);
        menuBar.add(menuCommon);
        menuBar.add(menuPersonal);

        setJMenuBar(menuBar);
    }

    /**
     * 初始化主界面内容
     */
    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 顶部：封面图片
        JLabel coverLabel = new JLabel();
        ImageIcon coverIcon = new ImageIcon("picture/SmartLib封面.png");
        Image coverImage = coverIcon.getImage().getScaledInstance(850, 300, Image.SCALE_SMOOTH);
        coverLabel.setIcon(new ImageIcon(coverImage));
        coverLabel.setHorizontalAlignment(JLabel.CENTER);

        // 中间：欢迎文字 + 功能按钮
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("欢迎使用 SmartLib 图书馆管理系统", JLabel.CENTER);
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 26));
        welcomeLabel.setForeground(new Color(41, 128, 185));

        JLabel userLabel = new JLabel("当前用户：" + currentUser.getUsername()
                + " | ID：" + currentUser.getUserId(), JLabel.CENTER);
        userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        userLabel.setForeground(new Color(100, 100, 100));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 5, 5));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(welcomeLabel);
        titlePanel.add(userLabel);

        // 2×2 功能按钮矩形
        JPanel btnGrid = new JPanel(new GridLayout(2, 2, 30, 30));
        btnGrid.setBackground(Color.WHITE);
        btnGrid.setBorder(BorderFactory.createEmptyBorder(20, 120, 10, 120));

        btnGrid.add(createFuncButton("浏览图书", "📖", new Color(41, 128, 185),
                e -> browseBooks()));
        btnGrid.add(createFuncButton("智能小助手", "🤖", new Color(46, 134, 70),
                e -> openAiAgent()));
        btnGrid.add(createFuncButton("个人信息", "👤", new Color(230, 126, 34),
                e -> showPersonalInfo()));
        btnGrid.add(createFuncButton("借阅管理", "📋", new Color(142, 68, 173),
                e -> openBorrowManage()));

        centerPanel.add(titlePanel, BorderLayout.NORTH);
        centerPanel.add(btnGrid, BorderLayout.CENTER);

        mainPanel.add(coverLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JButton createFuncButton(String text, String icon, Color bgColor,
                                     java.awt.event.ActionListener action) {
        JButton btn = new JButton(icon + "  " + text);
        btn.setFont(new Font("微软雅黑", Font.BOLD, 18));
        btn.setBackground(bgColor);
        btn.setForeground(new Color(50, 50, 60));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 2),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)));
        btn.addActionListener(action);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // === 菜单项对应的方法 ===

    private void browseBooks() {
        new BookListFrame(currentUser).setVisible(true);
    }

    private void openAiAgent() {
        new AiAgentUI().setVisible(true);
    }

    private void showPersonalInfo() {
        new UserDetailFrame(currentUser).setVisible(true);
    }

    private void openEditInfo() {
        new EditUserInfoFrame(currentUser).setVisible(true);
    }

    private void reLogin() {
        int choice = JOptionPane.showConfirmDialog(this,
                "确定要重新登录吗？",
                "确认",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginFrame().setVisible(true);
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

    private void openBorrowManage() {
        new BorrowHistoryFrame(currentUser.getUserId()).setVisible(true);
    }

    private void openAdmin() {
        PasswordDialog pwd = new PasswordDialog(this);
        pwd.setVisible(true);
        if (pwd.isAuthenticated()) {
            new AdminFrame(currentUser).setVisible(true);
        }
    }

    private void openUserList() {
        new UserListFrame().setVisible(true);
    }

    private void showUserStats() {
        int totalUsers = userService.getTotalUserCount();
        JOptionPane.showMessageDialog(this,
                " 用户统计信息\n\n" +
                        "总用户数: " + totalUsers + "\n" +
                        "当前用户ID: " + currentUser.getUserId() + "\n" +
                        "当前用户名: " + currentUser.getUsername(),
                "统计信息",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshData() {
        User updatedUser = userService.getUserById(currentUser.getUserId());
        if (updatedUser != null) {
            this.currentUser = updatedUser;
            JOptionPane.showMessageDialog(this,
                    " 数据已刷新！",
                    "提示",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showHelp() {
        JOptionPane.showMessageDialog(this,
                " 帮助信息\n\n" +
                        "1. 常用功能：浏览和查找图书\n" +
                        "2. 个人中心：我的空间和借阅管理\n" +
                        "3. 系统维护：后台管理、用户管理、刷新数据\n" +
                        "4. 帮助：查看帮助信息",
                "帮助",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this,
                " 关于 SmartLib\n\n" +
                        "版本：1.0\n" +
                        "开发团队：SmartLib开发组\n" +
                        "技术支持：Java Swing + MySQL",
                "关于系统",
                JOptionPane.INFORMATION_MESSAGE);
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
                testUser.setPhone("13800138000");

                new MainFrame(testUser).setVisible(true);
            }
        });
    }
}
