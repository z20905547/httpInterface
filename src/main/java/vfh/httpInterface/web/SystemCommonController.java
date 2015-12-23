package vfh.httpInterface.web;

import vfh.httpInterface.commons.CaptchaUtils;
import vfh.httpInterface.service.account.CaptchaAuthenticationFilter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * 系统公共Controller
 * 
 * @author maurice
 *
 */
@Controller
public class SystemCommonController {

    /**
     * 登录方法签名
     *
     * @return 响应页面为:WEB-INF/page/login.html
     */
    @RequestMapping("/login")
    public String login() {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null || subject.isAuthenticated()) {
            return "redirect:/logout";
        }
        return "login";
    }

    /**
     * 生成验证码方法签名
     *
     * @throws IOException
     *
     * @return 验证码图片的 byte 数组
     */
    @RequestMapping("/get-captcha")
    public ResponseEntity<byte[]> getCaptcha(HttpSession session) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String captcha = CaptchaUtils.getCaptcha(70, 32, 4, outputStream).toLowerCase();

        session.setAttribute(CaptchaAuthenticationFilter.DEFAULT_CAPTCHA_PARAM,captcha);
        byte[] bs = outputStream.toByteArray();
        outputStream.close();
        return new ResponseEntity<byte[]>(bs,headers, HttpStatus.OK);
    }
}
