package vfh.httpInterface.web.httpjson;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.commons.RequestParamUtil;
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.dao.gongying.GongyingCustomerMapper;
import vfh.httpInterface.service.area.AreaService;
import vfh.httpInterface.service.gongying.GongyingCustomerService;
import vfh.httpInterface.service.gongying.GongyingLogService;
import vfh.httpInterface.service.gongying.GongyingTravelService;
import vfh.httpInterface.service.gongying.GongyingWorkersService;


@Controller
@RequestMapping("/jsondata/gongying")
@SuppressWarnings(value={"rawtypes","unchecked"})
public class HttpgongyingController {
	@Autowired
    private GongyingWorkersService gongyingWorkersService;
	@Autowired 
	private GongyingCustomerService gongyingCustomerService;
	@Autowired 
	private GongyingLogService gongyingLogService;
	@Autowired 
	private GongyingTravelService gongyingTravelService;
	
	

	@RequestMapping("/register")
	@ResponseBody
	public void register(HttpServletRequest request,HttpServletResponse response){

		Map res = new HashMap();
		Map params = RequestParamUtil.requestToMap(request);
		
		int ct = 0;
		int pmark_ct =1;
	
		Map params2 = new HashMap();
		
		String remark = "2";
		String user_name = "";
		String partners_mark = "";
		int id=0;
		user_name = request.getParameter("user_name");
		partners_mark = request.getParameter("partners_mark");
		//查询用户名（手机号是否有重复的）
		if("" != user_name && user_name  != null){
		 ct =	Integer.parseInt(gongyingWorkersService.getByPhone(user_name).get("ct").toString());
		}
		

		
		if(  ct > 0) {
			res.put("statusCode","1111");
			JSONObject jsonObj = JSONObject.fromObject(res); 
			RequestParamUtil.responseWriter(request, response, jsonObj);
			
		}else if ("" != partners_mark && partners_mark  != null && gongyingWorkersService.getByPmark(partners_mark).size()==0){
			//等于 0 则意味着没有该机构编码 则不能注册
		

				
				res.put("statusCode","2222");
				JSONObject jsonObj = JSONObject.fromObject(res); 
				RequestParamUtil.responseWriter(request, response, jsonObj);
				
			
			}
		 else  {
		    if("" != request.getParameter("partners_mark") || request.getParameter("partners_mark") != null){
				if(request.getParameter("partners_mark").equals("GYLM2017")){
					remark = "1";
				}
			}
		    id  =	 Integer.parseInt(gongyingWorkersService.getByPmark(partners_mark).get(0).get("id").toString());
			
		   	params.put("p_id", id);
			params.put("user_name", request.getParameter("user_name"));
			params.put("password", request.getParameter("password"));
			params.put("worker_name", request.getParameter("usname"));
			params.put("partners_mark", request.getParameter("partners_mark"));
			params.put("remark", remark);
			gongyingWorkersService.insertworkers(params);
			res.put("statusCode","0000");
			JSONObject jsonObj = JSONObject.fromObject(res); 
			RequestParamUtil.responseWriter(request, response, jsonObj);
		}
	}
	
		@RequestMapping("/login")
		@ResponseBody
		public void CheckUserAndPassword(HttpServletRequest request,HttpServletResponse response){
			Map res = new HashMap();
			String	user_name = request.getParameter("userName");
			String	passWord = request.getParameter("passWord");
			
			//	Map params = RequestParamUtil.requestToMap(request);
			Map m =  gongyingWorkersService.checkLogin(user_name,passWord);
			
			if("0000".equals(m.get("returnCode"))){
				res.put("data", m.get("data"));
				res.put("statusCode","0000");
			}else{
				res.put("statusCode","1111");
			}
			res.put("returnMsg",m.get("returnMsg"));
			RequestParamUtil.responseWriter(request, response, res);
		}
		
		@RequestMapping("/customer")
		@ResponseBody
		public void getOrderListByworkerId(HttpServletRequest request,HttpServletResponse response){
			Map res = new HashMap();

			int	c_tj_id=0;
			int	c_jd_id=0;
			Map params = RequestParamUtil.requestToMap(request);
			if(null !=request.getParameter("worker_id") && "" !=request.getParameter("worker_id")
				&&	null !=request.getParameter("remark") && "" !=request.getParameter("remark")
				&& request.getParameter("remark").toString().equals("2")){// 非共赢员工能看见他推荐的客户  共赢员工只能看见他接待的客户
				c_tj_id= Integer.parseInt(request.getParameter("worker_id"));
				params.put("c_tj_id", c_tj_id);
			}else
			if(null !=request.getParameter("worker_id") && "" !=request.getParameter("worker_id")
					&&	null !=request.getParameter("remark") && "" !=request.getParameter("remark")
					&& request.getParameter("remark").toString().equals("1")){// 非共赢员工能看见他推荐的客户  共赢员工只能看见他接待的客户
				     c_jd_id= Integer.parseInt(request.getParameter("worker_id"));
					params.put("c_jd_id", c_jd_id);
				}else {
					params.put("c_jd_id", c_jd_id);
					params.put("c_tj_id", c_tj_id);
				}
				
			Map m =  gongyingCustomerService.findOrderList(params);
			
			if("000000".equals(m.get("returnCode"))){
				res.put("data", m.get("data"));
				res.put("statusCode","0000");
			}else{
				res.put("statusCode","1111");
			}
			res.put("returnMsg",m.get("returnMsg"));
			RequestParamUtil.responseWriter(request, response, res);
		}
		@RequestMapping("/customerById")
		@ResponseBody
		public void getOrderdetailByCid(HttpServletRequest request,HttpServletResponse response){
			Map res = new HashMap();


			Map params = RequestParamUtil.requestToMap(request);
			
				
			Map m =  gongyingCustomerService.findOrderList(params);
			
			if("000000".equals(m.get("returnCode"))){
				res.put("data", m.get("data"));
				res.put("statusCode","0000");
			}else{
				res.put("statusCode","1111");
			}
			res.put("returnMsg",m.get("returnMsg"));
			RequestParamUtil.responseWriter(request, response, res);
		}
		
		
		

@RequestMapping("/order")
@ResponseBody
public void getOrderDetailByCustomerId(HttpServletRequest request,HttpServletResponse response){
	Map res = new HashMap();

	int	customer_id = Integer.parseInt(request.getParameter("customer_id"));
	
//	String	passWord = request.getParameter("passWord");
	
		Map params = RequestParamUtil.requestToMap(request);
		params.put("customer_id", customer_id);
		
	Map m =  gongyingLogService.findLogList(params);
	
	if("000000".equals(m.get("returnCode"))){
		res.put("data", m.get("data"));
		res.put("statusCode","0000");
	}else{
		res.put("statusCode","1111");
	}
	res.put("returnMsg",m.get("returnMsg"));
	RequestParamUtil.responseWriter(request, response, res);
}


@RequestMapping("/log")
@ResponseBody
public void insertLog(HttpServletRequest request,HttpServletResponse response){
	Map res = new HashMap();

	String log_type = "2"; //用户日志
//	int	log_userid = Integer.parseInt(request.getParameter("log_userid"));
//	int	log_username = Integer.parseInt(request.getParameter("log_username"));
//	int	log_phone = Integer.parseInt(request.getParameter("log_phone"));
	
//	String	passWord = request.getParameter("passWord");
	
		Map params = RequestParamUtil.requestToMap(request);
		params.put("log_type", log_type);
		
		  gongyingLogService.insertLog(params);
	
		  res.put("statusCode","0000");
	RequestParamUtil.responseWriter(request, response, res);
}


@RequestMapping("/addc")
@ResponseBody
public void insertCustomer(HttpServletRequest request,HttpServletResponse response){
	Map res = new HashMap();

	String order_id= "";
	//生成不重复订单号
	long nowDate = new Date().getTime();  
     order_id = Integer.toHexString((int)nowDate);  

    
		Map params = RequestParamUtil.requestToMap(request);
		params.put("order_id", order_id);
		params.put("c_status", "1");

		gongyingCustomerService.insertCustomer(params);
		
		
		//根据order_id 查询customer_id
		
		Map m =  gongyingCustomerService.findOrderList(params);
		
	  String customer_id = m.get("customer_id").toString();
		
		
		Map param2 = new HashMap();
		//新增客户 插入日志——推荐客户成功 
		String log_type = "1"; //系统日志
		String log_content = "推荐成功 审核中...";
		int	log_userid = 0;
		String	log_username = "系统日志";
		String	log_phone = "00000000000";
		
		param2.put("log_type", log_type);
		param2.put("log_content", log_content);
		param2.put("log_userid", log_userid);
		param2.put("log_username", log_username);
		param2.put("log_phone", log_phone);
		param2.put("customer_id", customer_id);
		param2.put("order_id", order_id);
		gongyingLogService.insertLog(param2);
		
		
		
		

		  
		  res.put("statusCode","0000");
	RequestParamUtil.responseWriter(request, response, res);
}

@RequestMapping("/gy_workerInfo")
@ResponseBody
public void gy_workerInfo(HttpServletRequest request,HttpServletResponse response){
	Map res = new HashMap();

	Long worker_id = VariableUtils.typeCast(request.getParameter("worker_id"), Long.class);
	
	//	Map params = RequestParamUtil.requestToMap(request);
	Map m =  gongyingWorkersService.getWorkerInfoById(worker_id);
	
	if("0000".equals(m.get("returnCode"))){
		res.put("data", m.get("data"));
		res.put("statusCode","0000");
	}else{
		res.put("statusCode","1111");
	}
	res.put("returnMsg",m.get("returnMsg"));
	RequestParamUtil.responseWriter(request, response, res);
}


@RequestMapping("/gy_roadlist")
@ResponseBody
public void getRoadList(HttpServletRequest request,HttpServletResponse response){
	Map res = new HashMap();

//	int	customer_id = Integer.parseInt(request.getParameter("customer_id"));
	
//	String	passWord = request.getParameter("passWord");
	
		Map params = RequestParamUtil.requestToMap(request);
	//	params.put("customer_id", customer_id);
		
	Map m =  gongyingTravelService.getRoadList(params);
	
	if("000000".equals(m.get("returnCode"))){
		res.put("data", m.get("data"));
		res.put("statusCode","0000");
	}else{
		res.put("statusCode","1111");
	}
	res.put("returnMsg",m.get("returnMsg"));
	RequestParamUtil.responseWriter(request, response, res);
}


@RequestMapping("/getRoadDetail")
@ResponseBody
public void gy_getRoadDetail(HttpServletRequest request,HttpServletResponse response){
	Map res = new HashMap();

	Long travel_id = VariableUtils.typeCast(request.getParameter("travel_id"), Long.class);
	//System.out.println("33333333"+travel_id);
	//	Map params = RequestParamUtil.requestToMap(request);
	Map m =  gongyingTravelService.getRoadDetailById(travel_id);
	
	if("0000".equals(m.get("returnCode"))){
		res.put("data", m.get("data"));
		res.put("statusCode","0000");
	}else{
		res.put("statusCode","1111");
	}
	res.put("returnMsg",m.get("returnMsg"));
	RequestParamUtil.responseWriter(request, response, res);
}


}



