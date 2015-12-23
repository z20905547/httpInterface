package vfh.httpInterface.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 简单的验证码工具类
 * 
 * @author maurice
 *
 */
public class CaptchaUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaUtils.class);

    /**
     * 默认字体
     */
	public static final String FONT_FAMILY = "Arial Black";
    /**
     * 默认字体大小
     */
	public static final Integer FONT_SIZE = 16;
    /**
     * 默认验证码图片宽度
     */
    public static final Integer DEFAULT_CAPTCHA_WIDTH = 60;
    /**
     * 默认验证码图片高度
     */
    public static final Integer DEFAULT_CAPTCHA_HEIGHT = 18;

    /**
     * 生成图形验证码，并返回code值
     *
     * @param w 宽度
     * @param h 高度
     * @param num 验证码位数
     * @param out OutputStream
     *
     * @return String
     */
	public static String getCaptcha(int w, int h, int num, OutputStream out) {
		// 防止非负数
        int width = w > 0 ? w : DEFAULT_CAPTCHA_WIDTH;
        int height = h > 0 ? h : DEFAULT_CAPTCHA_HEIGHT;
		// 生成一张新图片
		BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		Font font = new Font(FONT_FAMILY, Font.PLAIN, FONT_SIZE);
		// 绘制图片
		Random random = new Random();
		Graphics g = image.getGraphics();
		g.setColor(getRandColor(200, 250));
		g.fillRect(1, 1, width - 1, height - 1);
		g.setColor(new Color(102, 102, 103));
		g.drawRect(0, 0, width - 1, height - 1);
		g.setFont(font);
		// 随机生成线条，让图片看起来更加杂乱
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width - 1);
			int y = random.nextInt(height - 1);
			int x1 = random.nextInt(6) + 1;
			int y1 = random.nextInt(12) + 1;
			g.drawLine(x, y, x + x1, y + y1);
		}
		// 该变量用于保存系统生成的随机字符串
		String sRand = "";
		for (int i = 0; i < num; i++) {
			// 取得一个随机字符
			String tmp = getRandomChar();
			sRand += tmp;
			// 将系统随机字符添加到图形验证码图片上
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(tmp, FONT_SIZE * i + 5, FONT_SIZE + 5);
		}
		g.dispose();
		try {
			ImageIO.write(image, "JPEG", out);
			out.close();
		} catch (IOException e) {
			sRand = null;
            LOGGER.error("生成验证码出错", e);
		}
		return sRand;
	}

	/**
	 * 生成随机颜色
	 * 
	 * @param fc 从随机的颜色位置 (0-255)
	 * @param bc 到随机的颜色位置（0-255）
	 * 
	 * @return {@link java.awt.Color}
	 */
	public static Color getRandColor(int fc, int bc) {
		Random random = new Random();

		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);

		return new Color(r, g, b);
	}

	/**
	 * 生成随机字符的方法
	 */
	public static String getRandomChar() {
		int rand = (int) Math.round(Math.random() * 2);
		long itmp;
		char ctmp;
		// 根据rand的值来决定是生成一个大写字母，小写字母和数字
		switch (rand) {
		// 生成大写字母的情形
		case 1:
			itmp = Math.round(Math.random() * 25 + 65);
			ctmp = (char) itmp;
			return String.valueOf(ctmp);
			// 生成小写字母的情形
		case 2:
			itmp = Math.round(Math.random() * 25 + 97);
			ctmp = (char) itmp;
			return String.valueOf(ctmp);
			// 生成数字的情形
		default:
			itmp = Math.round(Math.random() * 9);
			return String.valueOf(itmp);
		}

	}
	
}
