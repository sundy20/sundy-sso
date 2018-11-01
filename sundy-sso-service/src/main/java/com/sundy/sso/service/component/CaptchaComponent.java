package com.sundy.sso.service.component;

import com.sundy.sso.share.service.Result;
import com.sundy.sso.share.service.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2017/11/20
 *
 * @author plus.wang
 * @description 验证码生产组件
 */
@Component
public class CaptchaComponent {

    private static final Logger log = LoggerFactory.getLogger(CaptchaComponent.class);

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    private SignatureComponent signatureComponent;

    // 图片的宽度
    private static final int CAPTCHA_WIDTH = 90;
    // 图片的高度
    private static final int CAPTCHA_HEIGHT = 20;
    // 验证码的个数
    private static final int CAPTCHA_CODECOUNT = 4;

    private static final int XX = 15;
    private static final int CAPTCHA_FONT_HEIGHT = 18;
    private static final int CAPTCHA_CODE_Y = 16;
    private static final char[] codeSequence = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    // 过期时间为一分钟
    private static final long EXPIRE_MINUTES = 3L;

    //生成图片验证码
    public void genCaptcha(String token, HttpServletResponse resp) {

        // 定义图像 Buffer
        BufferedImage buffImg = new BufferedImage(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, BufferedImage.TYPE_INT_RGB);
        // 创建一个绘制图像的对象
        Graphics2D g = buffImg.createGraphics();
        // 创建一个随机数生成器类
        Random random = new Random();
        // 将图像填充为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, CAPTCHA_WIDTH, CAPTCHA_HEIGHT);
        // 设置字体
        g.setFont(new Font("Fixedsys", Font.BOLD, CAPTCHA_FONT_HEIGHT));
        // 设置字体边缘光滑
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 画边框
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, CAPTCHA_WIDTH - 1, CAPTCHA_HEIGHT - 1);
        // 随机产生40条干扰线，使图象中的认证码不易被其它程序探测到。
        g.setColor(Color.BLACK);
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(CAPTCHA_WIDTH);
            int y = random.nextInt(CAPTCHA_HEIGHT);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        // 保存随机产生的验证码，以便用户登录后进行验证
        StringBuffer randomCode = new StringBuffer();
        int red = 0, green = 0, blue = 0;
        // 随机产生验证码
        for (int i = 0; i < CAPTCHA_CODECOUNT; i++) {
            // 得到随机产生的验证码数字
            String code = String.valueOf(codeSequence[random.nextInt(36)]);
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            // 用随机产生的颜色将验证码绘制到图像中
            g.setColor(new Color(red, green, blue));
            g.drawString(code, (i + 1) * XX, CAPTCHA_CODE_Y);
            // 将产生的随机数组合在一起
            randomCode.append(code);
        }

        // 禁止图像缓存
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setContentType("image/jpeg");
        // 将图像输出到Servlet输出流中
        ServletOutputStream sos = null;
        try {
            sos = resp.getOutputStream();
            ImageIO.write(buffImg, "jpeg", sos);
        } catch (IOException e) {
            log.error("captcha error :" + e.getMessage(), e);
        } finally {
            try {
                sos.close();
                g.dispose();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.stringRedisTemplate.opsForValue().set(token, randomCode.toString());
        this.stringRedisTemplate.expire(token, EXPIRE_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * 校验图片验证码
     *
     * @param phone
     * @return
     */
    public Result<String> checkCaptcha(String phone, String captchaCode) {

        Result<String> booleanResult = Result.failure();

        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(captchaCode)) {

            booleanResult.code(ResultCode.CLIENT_ERROR.getCode());

            booleanResult.message("手机号或者验证码不正确");

            return booleanResult;
        }

        String codeDest = this.stringRedisTemplate.opsForValue().get(phone);

        if (StringUtils.isEmpty(codeDest)) {

            booleanResult.code(ResultCode.CLIENT_ERROR.getCode());

            booleanResult.message("验证码已失效");

            return booleanResult;

        }

        if (captchaCode.equalsIgnoreCase(codeDest)) {

            booleanResult.data(signatureComponent.genData(phone));

            booleanResult.message("captchaCode checked successfully");

        } else {

            booleanResult.code(ResultCode.CLIENT_ERROR.getCode());

            booleanResult.message("验证码错误");
        }

        return booleanResult;
    }

}