import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        setSize(900, 600);
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

        // === 1. 首页 ===
        JMenu menuHome = new JMenu("首页");
        menuHome.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JMenuItem itemBrowseBooks = new JMenuItem("浏览图书");
        itemBrowseBooks.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemBrowseBooks.setIcon(new ImageIcon("material/浏览图书.jpg"));
        itemBrowseBooks.addActionListener(e -> browseBooks());

        JMenuItem itemSearchBooks = new JMenuItem("查找图书");
        itemSearchBooks.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemSearchBooks.setIcon(new ImageIcon("material/查找图书.jpg"));
        itemSearchBooks.addActionListener(e -> searchBooks());

        menuHome.add(itemBrowseBooks);
        menuHome.add(itemSearchBooks);

        // === 2. 个人中心 ===
        JMenu menuPersonal = new JMenu("个人中心");
        menuPersonal.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JMenuItem itemMySpace = new JMenuItem("我的空间");
        itemMySpace.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemMySpace.setIcon(new ImageIcon("material/我的空间.jpg"));
        itemMySpace.addActionListener(e -> showPersonalInfo());

        JMenuItem itemBorrowManage = new JMenuItem("借阅管理");
        itemBorrowManage.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemBorrowManage.addActionListener(e -> openBorrowManage());

        JMenuItem itemLogout = new JMenuItem("退出登录");
        itemLogout.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemLogout.addActionListener(e -> logout());

        menuPersonal.add(itemMySpace);
        menuPersonal.add(itemBorrowManage);
        menuPersonal.addSeparator();
        menuPersonal.add(itemLogout);

        // === 3. 系统维护 ===
        JMenu menuSystem = new JMenu("系统维护");
        menuSystem.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JMenuItem itemUserManage = new JMenuItem("用户管理");
        itemUserManage.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemUserManage.addActionListener(e -> openUserManage());

        JMenuItem itemAdmin = new JMenuItem("后台管理");
        itemAdmin.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemAdmin.setIcon(new ImageIcon("material/后台管理.jpg"));
        itemAdmin.addActionListener(e -> openAdmin());

        JMenuItem itemRefresh = new JMenuItem("刷新数据");
        itemRefresh.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemRefresh.addActionListener(e -> refreshData());

        menuSystem.add(itemUserManage);
        menuSystem.add(itemAdmin);
        menuSystem.addSeparator();
        menuSystem.add(itemRefresh);

        // === 4. 帮助 ===
        JMenu menuHelp = new JMenu("帮助");
        menuHelp.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JMenuItem itemHelpInfo = new JMenuItem("帮助信息");
        itemHelpInfo.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemHelpInfo.setIcon(new ImageIcon("material/帮助信息.jpg"));
        itemHelpInfo.addActionListener(e -> showHelp());

        JMenuItem itemAbout = new JMenuItem("关于系统");
        itemAbout.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        itemAbout.setIcon(new ImageIcon("material/帮助信息.jpg"));
        itemAbout.addActionListener(e -> showAbout());

        menuHelp.add(itemHelpInfo);
        menuHelp.add(itemAbout);

        // 添加所有菜单到菜单栏
        menuBar.add(menuHome);
        menuBar.add(menuPersonal);
        menuBar.add(menuSystem);
        menuBar.add(menuHelp);

        setJMenuBar(menuBar);
    }

    /**
     * 初始化主界面内容
     */
    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        add(mainPanel);
    }

    // === 菜单项对应的方法 ===

    private void browseBooks() {
        JOptionPane.showMessageDialog(this,
                " 浏览图书功能\n\n（此功能由开发人员B负责）",
                "提示",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchBooks() {
        JOptionPane.showMessageDialog(this,
                " 查找图书功能\n\n（此功能由开发人员B负责）",
                "提示",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showPersonalInfo() {
        new UserDetailFrame(currentUser).setVisible(true);
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

    private void openUserManage() {
        new UserManageFrame().setVisible(true);
    }

    private void openAdmin() {
        JOptionPane.showMessageDialog(this,
                " 后台管理功能\n\n（此功能开发中）",
                "提示",
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
                        "1. 首页：浏览和查找图书\n" +
                        "2. 个人中心：我的空间、借阅管理、退出登录\n" +
                        "3. 系统维护：用户管理、后台管理、刷新数据\n" +
                        "4. 帮助：帮助信息和关于系统",
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
