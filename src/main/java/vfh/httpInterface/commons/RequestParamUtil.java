package vfh.httpInterface.commons;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class RequestParamUtil {

	public static Map<String, Object> pageMap = new HashMap<String, Object>();// 这里面存了前台分页的Map

	/**
	 * 获取request对象中的分页参数
	 * 
	 * @request request对象
	 * @defindex 没有找到index参数时，指定的默认值
	 * @defsize 没有找到pagesize参数时，指定的默认值
	 */
	public static void setRequestLimit(Map param, HttpServletRequest request,
			int defindex, int defsize) {
		String pageNumstr = null;
		String sizestr = null;

		if (request != null) {
			pageNumstr = request.getParameter("pageNum");
			sizestr = request.getParameter("numPerPage");
		}

		int pageNum = defindex;
		if (StringUtil.isNotEmpty(pageNumstr)) {
			pageNum = Integer.parseInt(pageNumstr);
		}
		int size = defsize;
		if (StringUtil.isNotEmpty(sizestr)) {
			size = Integer.parseInt(sizestr);
		}
		if (pageNum < 1) {
			pageNum = defindex;
			
		}
		if (size < 1) {
			size = defsize;
		}
		// 根据页码计算当前记录行
		int start = (pageNum - 1) * size;
		param.put("pageNum", pageNum);
		param.put("start", start);
		param.put("size", size);

	}
	
	/**
	 * 获取Map对象中的分页参数
	 * @param param
	 * @param defindex 没有找到index参数时，指定的默认值
	 * @param defsize 没有找到pagesize参数时，指定的默认值
	 * @return void
	 * @author qzy
	 */
	public static void setParamLimit(Map param, int defindex, int defsize){
		String pageNumstr = null;
		String sizestr = null;

		if (param != null) {
			pageNumstr = (String) param.get("pageNum");
			sizestr = (String) param.get("numPerPage");
		}

		int pageNum = defindex;
		if (StringUtil.isNotEmpty(pageNumstr)) {
			pageNum = Integer.parseInt(pageNumstr);
		}
		int size = defsize;
		if (StringUtil.isNotEmpty(sizestr)) {
			size = Integer.parseInt(sizestr);
		}
		if (pageNum < 1) {
			pageNum = defindex;
			
		}
		if (size < 1) {
			size = defsize;
		}
		// 根据页码计算当前记录行
		int start = (pageNum - 1) * size;
		param.put("pageNum", pageNum);
		param.put("start", start);
		param.put("size", size);
	}


	/**
	 * 把request的参数封装到map中
	 * @param request
	 * @return
	 */
	public static Map requestToMap(HttpServletRequest request){
		Map<String, Object> params = new HashMap<String, Object>();
		Iterator<?> iterator = request.getParameterMap().keySet().iterator();
		while(iterator.hasNext()){
			String key = (String)iterator.next();
			params.put(key,request.getParameter(key) );
		}
		return params;
	}

	/**
	 * 返回
	 * @param request
	 * @param response
	 * @param res
	 */
	public static void responseWriter(HttpServletRequest request, HttpServletResponse response,Map res){
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		String callback = request.getParameter("callback");
		try {
			if(StringUtil.isEmptyOfObject(callback)){
				response.getWriter().write(JsonUtils.convertToString(res));
			}else{
				response.getWriter().write(callback+"("+JsonUtils.convertToString(res)+")");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
