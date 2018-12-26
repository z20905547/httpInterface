package vfh.httpInterface.web.httpjson;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vfh.httpInterface.commons.RequestParamUtil;
import vfh.httpInterface.commons.StringUtil;
import vfh.httpInterface.service.account.AccountService;

@Controller
@RequestMapping("/jsondata/account")
@SuppressWarnings(value={"rawtypes","unchecked"})
public class HttpAccountController {
	@Autowired
    private AccountService accountService;
	/**
	 * TODO 获取账户信息
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param request
	 * @param response
	 * @create 2016年1月7日
	 */
	
	@RequestMapping("/getAccount")
	@ResponseBody
	public void getAccount(HttpServletRequest request,HttpServletResponse response){
		Map res = new HashMap();
		Map params = RequestParamUtil.requestToMap(request);
		
		Map m =  accountService.getUserInterface(params);
		if("000000".equals(m.get("returnCode"))){
			res.put("data", m.get("data"));
			res.put("statusCode","0000");
		}else{
			res.put("statusCode","1111");
		}
		res.put("returnMsg",m.get("returnMsg"));
		RequestParamUtil.responseWriter(request, response, res);
	}
    
	
	@RequestMapping("/list")
	@ResponseBody
	public void list(HttpServletRequest request,HttpServletResponse response){
		Map res = new HashMap();
		Map params = RequestParamUtil.requestToMap(request);
		
		Map m =  accountService.findUsers2(params);

		if("000000".equals(m.get("returnCode"))){
			res.put("data", m.get("data"));
			res.put("statusCode","0000");
		}else{
			res.put("statusCode","1111");
		}
		res.put("returnMsg",m.get("returnMsg"));
		RequestParamUtil.responseWriter(request, response, res);
	}
}
