import javax.swing.*;
import java.awt.*;

/**
 * 后台管理密码验证对话框（纯前端，不连接数据库）
 * 输入任意6位数字即可进入后台管理
 */
public class PasswordDialog extends JDialog {
    private JPasswordField passwordField;
    private boolean authenticated = false;

    public PasswordDialog(JFrame parent) {
        super(parent, "身份验证", true);
        setSize(380, 240);
        setLocationRelativeTo(parent);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 20, 30));
        mainPanel.setBackground(new Color(250, 250, 252));

        JLabel titleLabel = new JLabel("请输入管理密码（6位数字）", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        titleLabel.setForeground(new Color(50, 50, 60));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBackground(new Color(250, 250, 252));

        passwordField = new JPasswordField(10);
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        passwordField.setPreferredSize(new Dimension(180, 36));
        passwordField.setEchoChar((char) 0);
        passwordField.addActionListener(e -> verifyPassword());
        inputPanel.add(passwordField);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnPanel.setBackground(new Color(250, 250, 252));

        JButton confirmBtn = new JButton("确认");
        confirmBtn.setFont(new Font("微软雅黑", Font.BOLD, 14));
        confirmBtn.setPreferredSize(new Dimension(100, 36));
        confirmBtn.setBackground(new Color(120, 180, 230));
        confirmBtn.setForeground(new Color(50, 50, 60));
        confirmBtn.setFocusPainted(false);
        confirmBtn.addActionListener(e -> verifyPassword());
        btnPanel.add(confirmBtn);

        JButton cancelBtn = new JButton("取消");
        cancelBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        cancelBtn.setPreferredSize(new Dimension(100, 36));
        cancelBtn.addActionListener(e -> dispose());
        btnPanel.add(cancelBtn);

        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void verifyPassword() {
        String input = new String(passwordField.getPassword());

        if (input.length() != 6) {
            JOptionPane.showMessageDialog(this,
                    "密码必须为6位数字！",
                    "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!input.matches("\\d{6}")) {
            JOptionPane.showMessageDialog(this,
                    "密码只能包含数字！",
                    "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        authenticated = true;
        dispose();
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}
