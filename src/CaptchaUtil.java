import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 验证码工具类
 * 生成4位随机数字验证码图片
 */
public class CaptchaUtil {
    private String captchaCode;
    private final Random random = new Random();

    public CaptchaUtil() {
        refresh();
    }

    public void refresh() {
        captchaCode = String.format("%04d", random.nextInt(10000));
    }

    public String getCode() {
        return captchaCode;
    }

    public BufferedImage createImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // 浅色背景
        g2d.setColor(new Color(245, 245, 250));
        g2d.fillRect(0, 0, width, height);

        // 干扰线
        g2d.setColor(new Color(180, 180, 200));
        for (int i = 0; i < 4; i++) {
            int x1 = random.nextInt(width / 3);
            int y1 = random.nextInt(height);
            int x2 = width - random.nextInt(width / 3);
            int y2 = random.nextInt(height);
            g2d.drawLine(x1, y1, x2, y2);
        }

        // 干扰点
        g2d.setColor(new Color(160, 160, 180));
        for (int i = 0; i < 30; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g2d.fillOval(x, y, 2, 2);
        }

        // 绘制数字，每个数字不同颜色和轻微旋转
        Font[] fonts = {
            new Font("Arial", Font.BOLD, 24),
            new Font("Georgia", Font.BOLD, 24),
            new Font("Serif", Font.ITALIC | Font.BOLD, 24),
        };

        for (int i = 0; i < 4; i++) {
            g2d.setFont(fonts[random.nextInt(fonts.length)]);

            // 每个数字用不同的深色
            Color[] darkColors = {
                new Color(41, 98, 185),    // 深蓝
                new Color(180, 50, 50),     // 深红
                new Color(30, 130, 80),     // 深绿
                new Color(150, 80, 30),     // 深橙
            };
            g2d.setColor(darkColors[random.nextInt(darkColors.length)]);

            int x = 12 + i * (width / 4);
            int y = 28 + random.nextInt(8);
            g2d.drawString(String.valueOf(captchaCode.charAt(i)), x, y);
        }

        g2d.dispose();
        return image;
    }
}
