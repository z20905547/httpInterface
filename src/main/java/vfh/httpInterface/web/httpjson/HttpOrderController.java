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
import vfh.httpInterface.service.notice.NoticeService;
import vfh.httpInterface.service.order.OrderService;

@Controller
@RequestMapping("/jsondata/order")
@SuppressWarnings(value={"rawtypes","unchecked"})
public class HttpOrderController {
	@Autowired
    private OrderService orderService;
	@RequestMapping("/addOrder")
	@ResponseBody
	public void addOrder(HttpServletRequest request,HttpServletResponse response){
		String citys = "";
		String housetypes="";
		Map res = new HashMap();
		Map params = new HashMap();
		//用户选择多个区域的时候做一个拼装
		String[] city = request.getParameterValues("areaname[]");
		if(null!=city && city.length>0){
			for(int i = 0; i < city.length; i++){
				citys+=city[i]+"、"; 
		    }
		}
		//用户选择多个房屋类型的时候做一个拼装
		String[] housetype = request.getParameterValues("HousesType[]");
		if(null!=housetype && housetype.length>0){
			for(int i = 0; i < housetype.length; i++){
				housetypes+=housetype[i]+"、"; 
		    }
		}
		params.put("city", citys);
		params.put("customer_name", request.getParameter("Name"));
		params.put("customer_num", request.getParameter("Phone"));
		params.put("housetype", housetypes);
		params.put("huxing", request.getParameter("HousesIntention[]"));
		params.put("mianji", request.getParameter("HousesArea"));
		params.put("yusuan", request.getParameter("yusuan"));
		params.put("zhuangxiu", request.getParameter("Decoration"));
		params.put("other_Req", request.getParameter("Message"));
		params.put("sexy", request.getParameter("Namedd"));
		params.put("ordertype", request.getParameter("ordertype"));
		orderService.insert(params);	
	}
	

}
