package vfh.httpInterface.service.gongying;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.commons.StringUtil;
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.dao.gongying.GongyingLogMapper;

@Service
@Transactional
public class GongyingLogService {
	/**
     * 活动图存放目录 
     */
 
	//正式环境
	public static final String DEFAULT_USER_UPLOAD_PORTRAIT_PATH = "/vfh/apache-tomcat-7.0.67/webapps/management/resource/upload_buildings/" ;
	public static final String DEFAULT_USER_UPLOAD_PORTRAIT_PATH2 = 
			 "/resource/upload_buildings/" ;
	 //水印logo 
	public static final String DEFAULT_LOGO_PATH ="/vfh/apache-tomcat-7.0.67/webapps/management/resource/upload_buildings/";


	
	@Autowired 
	private GongyingLogMapper gongyingLogMapper;
	Map<String, Object> returnMap=new HashMap<String, Object>();
	
	
	public void insertLog(Map<String, Object> entity) {

		gongyingLogMapper.insert(entity);
		
	}

	public void updateBuildingsActive(
			Map<String, Object> entity) {
		gongyingLogMapper.update(entity);
	}
	

	public Map<String, Object> getBuildingsActive(Long customer_id) {
		return gongyingLogMapper.get(customer_id);
	}

	public void deleteBuildingsActive(Map<String, Object> entity) {
		Long id = VariableUtils.typeCast(entity.get("id"), Long.class);
		gongyingLogMapper.delete(id);
	}
	
	public  Map<String, Object> findLogList(
			Map<String, Object> filter) {
		returnMap.clear();
		Map<String, Object> pagedata=new HashMap<String, Object>();

		List<Map<String, Object>> LogList=gongyingLogMapper
				.find(filter);;
		if(StringUtil.isNotEmptyList(LogList)){
			//pagedata.put("total", total);
			pagedata.put("list", LogList);
    		returnMap.put("returnCode", "000000");
    		returnMap.put("data",pagedata);
    		returnMap.put("returnMsg", "获取日志成功！");
    	}else{
    		returnMap.put("returnCode", "1111");
    		returnMap.put("returnMsg", "获取日志失败！");
    	}
		
		
	    return returnMap;
	}

	public Page<Map<String, Object>> findBasicDataList(
			PageRequest pageRequest, Map<String, Object> filter, Model model) {

		long total = gongyingLogMapper.count(filter);
		filter.putAll(pageRequest.getMap());
		List<Map<String, Object>> content = gongyingLogMapper
				.find(filter);

		return new Page<Map<String, Object>>(pageRequest, content, total);
	}
	
	}
