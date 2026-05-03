import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * AI Agent 的图形界面类
 * 提供用户友好的界面来与 AI 进行对话
 */
public class AiAgentUI extends JFrame {
    private JTextArea outputArea;      // AI 回复显示区域
    private JTextArea thinkingArea;    // 思考过程显示区域
    private JTextField inputField;     // 输入框
    private JButton sendButton;        // 发送按钮
    private JButton clearButton;       // 清空按钮

    /**
     * 构造函数：初始化窗口
     */
    public AiAgentUI() {
        setTitle("SmartLib AI Agent");            // 设置窗口标题
        setSize(800, 600);                        // 设置窗口大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 点击关闭按钮时退出程序
        setLocationRelativeTo(null);              // 窗口居中显示

        initUI();  // 初始化界面组件
    }

    /**
     * 初始化界面布局
     */
    private void initUI() {
        // 创建主面板，使用 BorderLayout（东西南北中布局）
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        // 设置面板四周的空白边框
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建输出面板，使用 GridLayout（网格布局，2行1列）
        JPanel outputPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        // === 上半部分：AI 回复区域 ===
        JPanel responsePanel = new JPanel(new BorderLayout());  // 使用 BorderLayout
        JLabel responseLabel = new JLabel("AI 回复：");          // 创建标签
        responseLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));  // 设置字体
        outputArea = new JTextArea();                            // 创建文本区域
        outputArea.setEditable(false);                           // 设置为不可编辑
        outputArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));  // 设置字体
        responsePanel.add(responseLabel, BorderLayout.NORTH);     // 标签放在顶部
        responsePanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);  // 文本区域放在中间（带滚动条）

        // === 下半部分：思考过程区域 ===
        JPanel thinkingPanel = new JPanel(new BorderLayout());    // 使用 BorderLayout
        JLabel thinkingLabel = new JLabel("思考过程：");          // 创建标签
        thinkingLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));  // 设置字体
        thinkingArea = new JTextArea();                          // 创建文本区域
        thinkingArea.setEditable(false);                         // 设置为不可编辑
        thinkingArea.setFont(new Font("微软雅黑", Font.PLAIN, 11));  // 设置字体
        thinkingPanel.add(thinkingLabel, BorderLayout.NORTH);     // 标签放在顶部
        thinkingPanel.add(new JScrollPane(thinkingArea), BorderLayout.CENTER);  // 文本区域放在中间

        // 把两个区域添加到输出面板
        outputPanel.add(responsePanel);
        outputPanel.add(thinkingPanel);

        // === 底部：输入区域 ===
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));  // 使用 BorderLayout
        inputField = new JTextField();                            // 创建输入框
        inputField.setFont(new Font("微软雅黑", Font.PLAIN, 14));  // 设置字体
        sendButton = new JButton("发送");                         // 创建发送按钮
        clearButton = new JButton("清空");                        // 创建清空按钮

        // 创建按钮面板，使用 FlowLayout（流式布局，右对齐）
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(sendButton);   // 添加发送按钮
        buttonPanel.add(clearButton);  // 添加清空按钮

        inputPanel.add(new JLabel("请输入问题："), BorderLayout.NORTH);  // 标签放在顶部
        inputPanel.add(inputField, BorderLayout.CENTER);                  // 输入框放在中间
        inputPanel.add(buttonPanel, BorderLayout.EAST);                   // 按钮面板放在右边

        // === 组合主面板 ===
        mainPanel.add(outputPanel, BorderLayout.CENTER);  // 输出面板放在中间
        mainPanel.add(inputPanel, BorderLayout.SOUTH);    // 输入面板放在底部

        add(mainPanel);  // 把主面板添加到窗口

        setupEventListeners();  // 设置按钮事件监听
    }

    /**
     * 设置按钮和输入框的事件监听器
     */
    private void setupEventListeners() {
        // 发送按钮点击事件
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();  // 调用发送消息方法
            }
        });

        // 输入框按回车键事件
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();  // 调用发送消息方法
            }
        });

        // 清空按钮点击事件
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAll();  // 调用清空方法
            }
        });
    }

    /**
     * 发送消息给 AI（流式输出）
     */
    private void sendMessage() {
        // 获取输入框的内容并去除首尾空格
        final String prompt = inputField.getText().trim();

        // 如果输入为空，显示提示
        if (prompt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入问题！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 发送期间禁用按钮和输入框
        sendButton.setEnabled(false);
        inputField.setEnabled(false);

        // 先添加用户输入
        outputArea.append("你：" + prompt + "\nAI：");

        // 调用流式接口
        AiAgent.askStream(prompt, new AiAgent.StreamCallback() {
            // 收到新的回复内容时
            @Override
            public void onResponse(String text) {
                SwingUtilities.invokeLater(() -> {
                    outputArea.append(text);
                    outputArea.setCaretPosition(outputArea.getDocument().getLength());
                });
            }

            // 收到新的思考内容时
            @Override
            public void onThinking(String text) {
                SwingUtilities.invokeLater(() -> {
                    thinkingArea.append(text);
                    thinkingArea.setCaretPosition(thinkingArea.getDocument().getLength());
                });
            }

            // 完成时
            @Override
            public void onComplete() {
                SwingUtilities.invokeLater(() -> {
                    outputArea.append("\n\n");
                    outputArea.setCaretPosition(outputArea.getDocument().getLength());
                    // 重新启用按钮和输入框
                    sendButton.setEnabled(true);
                    inputField.setEnabled(true);
                    inputField.setText("");
                });
            }

            // 发生错误时
            @Override
            public void onError(Throwable t) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(AiAgentUI.this,
                            "发生错误：" + t.getMessage(),
                            "错误", JOptionPane.ERROR_MESSAGE);
                    sendButton.setEnabled(true);
                    inputField.setEnabled(true);
                    inputField.setText("");
                });
            }
        });
    }

    /**
     * 清空所有内容
     */
    private void clearAll() {
        outputArea.setText("");     // 清空回复区域
        thinkingArea.setText("");   // 清空思考区域
        inputField.setText("");     // 清空输入框
    }

    /**
     * 程序入口
     */
    public static void main(String[] args) {
        // 使用 SwingUtilities.invokeLater 确保在事件分发线程创建界面
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // 设置系统外观，让界面更符合系统风格
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 创建并显示窗口
                new AiAgentUI().setVisible(true);
            }
        });
    }
}
