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
import vfh.httpInterface.service.area.AreaService;

@Controller
@RequestMapping("/jsondata/area")
@SuppressWarnings(value={"rawtypes","unchecked"})
public class HttpAreaController {
	@Autowired
    private AreaService areaService;
	@RequestMapping("/getSubAreaList")
	@ResponseBody
	public void getSubAreaList(HttpServletRequest request,HttpServletResponse response){
		Map res = new HashMap();
		Map params = RequestParamUtil.requestToMap(request);
		Map m =  areaService.getSubList(params);
		if("000000".equals(m.get("returnCode"))){
			res.put("data", m.get("data"));
			res.put("statusCode","0000");
		}else{
			res.put("statusCode","1111");
		}
		res.put("returnMsg",m.get("returnMsg"));
		RequestParamUtil.responseWriter(request, response, res);
	}
	@RequestMapping("/getSubUserList")
	@ResponseBody
	public void getSubUserList(HttpServletRequest request,HttpServletResponse response){
		Map res = new HashMap();
		Map params = RequestParamUtil.requestToMap(request);
		Map m =  areaService.getUserList(params);
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
