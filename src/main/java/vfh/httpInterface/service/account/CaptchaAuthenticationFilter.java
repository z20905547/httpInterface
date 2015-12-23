package vfh.httpInterface.service.account;

import vfh.httpInterface.commons.SessionVariable;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 验证码登录认证 Filter
 * 
 * @author maurice
 *
 */
@Component
public class CaptchaAuthenticationFilter extends FormAuthenticationFilter{
	
	/**
	 * 默认验证码参数名称
	 */
	public static final String DEFAULT_CAPTCHA_PARAM = "captcha";
	
	/**
	 * 默认在 session 中存储的登录错误次数的名称
	 */
	private static final String DEFAULT_LOGIN_INCORRECT_NUMBER_KEY_ATTRIBUTE = "incorrectNumber";
	
	//验证码参数名称
    private String captchaParam = DEFAULT_CAPTCHA_PARAM;
    //在session中的存储验证码的key名称
    private String sessionCaptchaKeyAttribute = DEFAULT_CAPTCHA_PARAM;
    //在session中存储的登录错误次数名称
    private String loginIncorrectNumberKeyAttribute = DEFAULT_LOGIN_INCORRECT_NUMBER_KEY_ATTRIBUTE;
    //允许登录错误次数，当登录次数大于该数值时，会在页面中显示验证码
    private Integer allowIncorrectNumber = 1;
    
    /**
     * 重写父类方法，在 shiro 执行登录时先对比验证码，正确后在登录，否则直接登录失败
     */
	@Override
	protected boolean executeLogin(ServletRequest request,ServletResponse response) throws Exception {
		
		Session session = SecurityUtils.getSubject().getSession();
		//获取登录错误次数
		Integer number = (Integer) session.getAttribute(getLoginIncorrectNumberKeyAttribute());
		
		//首次登录，将该数量记录在session中
		if (number == null) {
			number = 1;
			session.setAttribute(getLoginIncorrectNumberKeyAttribute(), number);
		}
		
		//如果登录次数大于allowIncorrectNumber，需要判断验证码是否一致
		if (number > getAllowIncorrectNumber()) {
			//获取当前验证码
			String currentCaptcha = (String) session.getAttribute(getSessionCaptchaKeyAttribute());
			//获取用户输入的验证码
			String submitCaptcha = getCaptcha(request);
			//如果验证码不匹配，登录失败
			if (StringUtils.isEmpty(submitCaptcha) || !StringUtils.equals(currentCaptcha,submitCaptcha.toLowerCase())) {
				return onLoginFailure(this.createToken(request, response), new CaptchaNotMatchException(), request, response);
			}
		
		}
		
		return super.executeLogin(request, response);
	}


	/**
	 * 重写父类方法，当登录失败将异常信息设置到 request 的 attribute 中
	 */
	@Override
	protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
        if(ae instanceof CaptchaNotMatchException) {
            request.setAttribute(getFailureKeyAttribute(), "验证码不正确");
        } else if (ae instanceof IncorrectCredentialsException) {
            request.setAttribute(getFailureKeyAttribute(), "用户名或密码错误");
        } else if (ae instanceof DisabledAccountException) {
            request.setAttribute(getFailureKeyAttribute(), "你的账户已被禁用");
        } else {
            request.setAttribute(getFailureKeyAttribute(), "服务器出现异常");
        }
	}
	
	/**
	 * 重写父类方法，当登录失败后，将 allowIncorrectNumber（允许登错误录次） + 1
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token,AuthenticationException e, ServletRequest request,ServletResponse response) {
		
		Session session = SecurityUtils.getSubject().getSession();
		Integer number = (Integer) session.getAttribute(getLoginIncorrectNumberKeyAttribute());
		session.setAttribute(getLoginIncorrectNumberKeyAttribute(),++number);
		
		return super.onLoginFailure(token, e, request, response);
	}
	
	/**
	 * 重写父类方法，当登录成功后，将 allowIncorrectNumber（允许登错误录次）设置为 0，重置下一次登录的状态
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
		Session session = subject.getSession();

        session.removeAttribute(getLoginIncorrectNumberKeyAttribute());
        session.setAttribute(SessionVariable.DEFAULT_SESSION_KEY, subject.getPrincipal());
		
		return super.onLoginSuccess(token, subject, request, response);
	}

	/**
	 * 设置验证码提交的参数名称
	 * 
	 * @param captchaParam 验证码提交的参数名称
	 */
	public void setCaptchaParam(String captchaParam) {
		this.captchaParam = captchaParam;
	}

	/**
	 * 获取验证码提交的参数名称
	 * 
	 * @return String
	 */
	public String getCaptchaParam() {
		return captchaParam;
	}

	/**
	 * 设置在 session 中的存储验证码的 key 名称
	 * 
	 * @param sessionCaptchaKeyAttribute 存储验证码的 key 名称
	 */
	public void setSessionCaptchaKeyAttribute(String sessionCaptchaKeyAttribute) {
		this.sessionCaptchaKeyAttribute = sessionCaptchaKeyAttribute;
	}
	
	/**
	 * 获取设置在 session 中的存储验证码的 key 名称
	 * 
	 * @return Sting
	 */
	public String getSessionCaptchaKeyAttribute() {
		return sessionCaptchaKeyAttribute;
	}

	/**
	 * 获取登录错误次数的 key 属性名称
	 * 
	 * @return String
	 */
	public String getLoginIncorrectNumberKeyAttribute() {
		return loginIncorrectNumberKeyAttribute;
	}

	/**
	 * 设置登录错误次数的 key 属性名称
	 * 
	 * @param loginIncorrectNumberKeyAttribute 属性名称
	 */
	public void setLoginIncorrectNumberKeyAttribute(String loginIncorrectNumberKeyAttribute) {
		this.loginIncorrectNumberKeyAttribute = loginIncorrectNumberKeyAttribute;
	}

	/**
	 * 获取用户输入的验证码
	 * 
	 * @param request ServletRequest
	 * 
	 * @return String
	 */
	public String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}
	
	/**
	 * 获取允许登录次数
	 * 
	 * @return Integer
	 */
	public Integer getAllowIncorrectNumber() {
		return allowIncorrectNumber;
	}

	/**
	 * 设置允许登录次数，当登录次数大于该数值时，会在页面中显示验证码
	 * 
	 * @param allowIncorrectNumber 允许登录次数
	 */
	public void setAllowIncorrectNumber(Integer allowIncorrectNumber) {
		this.allowIncorrectNumber = allowIncorrectNumber;
	}
	
}