import javax.swing.*;
import java.awt.*;

public class AiAgentUI extends JFrame {
    private JPanel chatContainer;     // 对话容器（包含多个对话卡片）
    private JTextField inputField;    // 输入框
    private JButton sendButton;       // 发送按钮
    private JScrollPane scrollPane;   // 滚动面板

    public AiAgentUI() {
        setTitle("SmartLib AI Agent");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        // 主面板垂直排列
        setLayout(new BorderLayout(10, 10));

        // === 顶部标题 ===
        JLabel titleLabel = new JLabel("SmartLib AI助手", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // === 中间：对话区域（可滚动） ===
        // 创建对话容器（垂直排列）
        chatContainer = new JPanel();
        chatContainer.setLayout(new BoxLayout(chatContainer, BoxLayout.Y_AXIS));
        chatContainer.setBackground(new Color(240, 240, 240));

        // 创建滚动面板
        scrollPane = new JScrollPane(chatContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        // === 底部：输入区域 ===
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        inputField = new JTextField();
        inputField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        inputField.setPreferredSize(new Dimension(0, 40));

        sendButton = new JButton("发送");
        sendButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        sendButton.setPreferredSize(new Dimension(80, 40));

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        setupEventListeners();
    }

    private void setupEventListeners() {
        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());
    }

    private void sendMessage() {
        String prompt = inputField.getText().trim();
        if (prompt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入问题！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        sendButton.setEnabled(false);
        inputField.setEnabled(false);

        // 创建对话卡片
        JPanel card = createChatCard(prompt);
        chatContainer.add(card);
        chatContainer.add(Box.createVerticalStrut(15));  // 卡片间距
        chatContainer.revalidate();
        scrollToBottom();

        // 调用流式接口
        AiAgent.askStream(prompt, new AiAgent.StreamCallback() {
            private JTextArea aiTextArea;
            private JTextArea thinkTextArea;

            @Override
            public void onResponse(String text) {
                SwingUtilities.invokeLater(() -> {
                    if (aiTextArea == null) {
                        aiTextArea = findOrCreateAiTextArea(card);
                    }
                    if (aiTextArea != null) {
                        aiTextArea.append(text);
                    }
                });
            }

            @Override
            public void onThinking(String text) {
                SwingUtilities.invokeLater(() -> {
                    if (thinkTextArea == null) {
                        thinkTextArea = findOrCreateThinkTextArea(card);
                    }
                    if (thinkTextArea != null) {
                        thinkTextArea.append(text);
                    }
                });
            }

            @Override
            public void onComplete() {
                SwingUtilities.invokeLater(() -> {
                    sendButton.setEnabled(true);
                    inputField.setEnabled(true);
                    inputField.setText("");
                    scrollToBottom();
                });
            }

            @Override
            public void onError(Throwable t) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(AiAgentUI.this,
                            "发生错误：" + t.getMessage(),
                            "错误", JOptionPane.ERROR_MESSAGE);
                    chatContainer.remove(card);
                    chatContainer.repaint();
                    sendButton.setEnabled(true);
                    inputField.setEnabled(true);
                });
            }
        });
    }

    private JPanel createChatCard(String userPrompt) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        // 用户消息
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(Color.WHITE);
        JTextArea userText = new JTextArea(userPrompt);
        userText.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        userText.setEditable(false);
        userText.setBackground(new Color(210, 255, 210));
        userText.setLineWrap(true);
        userText.setWrapStyleWord(true);
        userText.setColumns(30);
        userText.setRows(1);
        userText.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        userPanel.add(userText);
        card.add(userPanel);

        // 思考过程区域（动态创建）
        JPanel thinkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        thinkPanel.setBackground(new Color(245, 245, 245));
        thinkPanel.setName("thinkPanel");
        thinkPanel.setVisible(false);
        JTextArea thinkText = new JTextArea();
        thinkText.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        thinkText.setEditable(false);
        thinkText.setBackground(new Color(245, 245, 245));
        thinkText.setForeground(Color.GRAY);
        thinkText.setLineWrap(true);
        thinkText.setWrapStyleWord(true);
        thinkText.setColumns(30);
        thinkText.setRows(1);
        thinkText.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        thinkPanel.add(thinkText);
        card.add(thinkPanel);

        // AI 回复区域（动态创建）
        JPanel aiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        aiPanel.setBackground(new Color(230, 247, 255));
        aiPanel.setName("aiPanel");
        JTextArea aiText = new JTextArea();
        aiText.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        aiText.setEditable(false);
        aiText.setBackground(new Color(230, 247, 255));
        aiText.setLineWrap(true);
        aiText.setWrapStyleWord(true);
        aiText.setColumns(30);
        aiText.setRows(1);
        aiText.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        aiPanel.add(aiText);
        card.add(aiPanel);

        return card;
    }

    private JTextArea findOrCreateAiTextArea(JPanel card) {
        for (Component comp : card.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                if ("aiPanel".equals(panel.getName())) {
                    for (Component c : panel.getComponents()) {
                        if (c instanceof JTextArea) {
                            return (JTextArea) c;
                        }
                    }
                }
            }
        }
        return null;
    }

    private JTextArea findOrCreateThinkTextArea(JPanel card) {
        for (Component comp : card.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                if ("thinkPanel".equals(panel.getName())) {
                    panel.setVisible(true);
                    for (Component c : panel.getComponents()) {
                        if (c instanceof JTextArea) {
                            return (JTextArea) c;
                        }
                    }
                }
            }
        }
        return null;
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
            verticalBar.setValue(verticalBar.getMaximum());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new AiAgentUI().setVisible(true);
        });
    }
}
