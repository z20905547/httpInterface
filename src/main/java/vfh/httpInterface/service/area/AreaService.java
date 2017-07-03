package vfh.httpInterface.service.area;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vfh.httpInterface.commons.StringUtil;
import vfh.httpInterface.dao.area.AreaMapper;

/**
 * TODO 资源管理表
 * @author harry
 * <b> 有问题请联系qq:359705093</b>
 * @create 2016年1月11日
 */
@Service
@Transactional
public class AreaService {
	@Autowired
	private AreaMapper areaMapper;
	
	Map<String, Object> returnMap=new HashMap<String, Object>();
	public Map<String, Object> getSubList(Map params) {
		returnMap.clear();
    	if(StringUtil.isNotEmptyObject(params.get("parentId"))){
    		long parentId=Long.parseLong(params.get("parentId").toString());
    		List<Map<String, Object>> areaList=areaMapper.findByParentId(parentId);
    		if(StringUtil.isNotEmptyList(areaList)){
        		returnMap.put("returnCode", "000000");
        		returnMap.put("data",areaList);
        		returnMap.put("returnMsg", "获取城市列表成功！");
        	}else{
        		returnMap.put("returnCode", "1111");
        		returnMap.put("returnMsg", "获取用户信息失败！");
        	}
    	}else{
    		returnMap.put("returnMsg","父类id不能为空!");
    		returnMap.put("statusCode","1111");
    	}
    	
    	
        return returnMap;
        
    }
	
	public Map<String, Object> getUserList(Map params) {
		returnMap.clear();
		long parentId=1;
    		List<Map<String, Object>> areaList=areaMapper.findUser(parentId);
    		if(StringUtil.isNotEmptyList(areaList)){
        		returnMap.put("returnCode", "000000");
        		returnMap.put("data",areaList);
        		returnMap.put("returnMsg", "获取城市列表成功！");
        	}else{
        		returnMap.put("returnCode", "1111");
        		returnMap.put("returnMsg", "获取用户信息失败！");
        	}
    	
    	
    	
        return returnMap;
        
    }
}
