package vfh.httpInterface.web.httpjson;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vfh.httpInterface.commons.RequestParamUtil;
import vfh.httpInterface.service.buildings.BuildingsService;

@Controller
@RequestMapping("/jsondata/buildings")
@SuppressWarnings(value={"rawtypes","unchecked"})
public class HttpBuildingsController {
	@Autowired
    private BuildingsService buildingsService;
	@RequestMapping("/getBuildingsDetailList")
	@ResponseBody
	public void getBuildingsDetailList(HttpServletRequest request,HttpServletResponse response){
		Map res = new HashMap();
		Map params = RequestParamUtil.requestToMap(request);
		Map m =  buildingsService.findDetailList(params);
		if("000000".equals(m.get("returnCode"))){
			res.put("data", m.get("data"));
			res.put("statusCode","0000");
		}else{
			res.put("statusCode","1111");
		}
		res.put("returnMsg",m.get("returnMsg"));
		RequestParamUtil.responseWriter(request, response, res);
	}
	@RequestMapping("/getBuildingsDetail")
	@ResponseBody
	public void getBuildingsDetail(HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false)Long buildingsId,@RequestParam(required = false)Long pid){
		
		Map params = RequestParamUtil.requestToMap(request);
		
		Map res = new HashMap();
		Map m =  buildingsService.findBulidingsDetail(buildingsId,pid);
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
