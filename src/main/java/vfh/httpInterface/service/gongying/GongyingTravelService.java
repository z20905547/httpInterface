package vfh.httpInterface.service.gongying;


import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;


import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.dao.gongying.GongyingTravelMapper;


@Service
@Transactional
public class GongyingTravelService {
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
	private GongyingTravelMapper gongyingTravelMapper;

	
	
	public void insertBuildingsActive(
			 Map<String, Object> entity) {

		gongyingTravelMapper.insert(entity);
		
	}

	public void updateBuildingsActive(
			Map<String, Object> entity) {
		gongyingTravelMapper.update(entity);
	}
	

	public Map<String, Object> getBuildingsActive(Long id) {
		return gongyingTravelMapper.get(id);
	}

	public void deleteBuildingsActive(Map<String, Object> entity) {
		Long id = VariableUtils.typeCast(entity.get("id"), Long.class);
		gongyingTravelMapper.delete(id);
	}


	public Page<Map<String, Object>> findBasicDataList(
			PageRequest pageRequest, Map<String, Object> filter, Model model) {

		long total = gongyingTravelMapper.count(filter);
		filter.putAll(pageRequest.getMap());
		List<Map<String, Object>> content = gongyingTravelMapper
				.find(filter);

		return new Page<Map<String, Object>>(pageRequest, content, total);
	}
	// 2
	public void insertBuildingsActive2(
			 Map<String, Object> entity) {

		gongyingTravelMapper.insert2(entity);
		
	}

	public void updateBuildingsActive2(
			Map<String, Object> entity) {
		gongyingTravelMapper.update2(entity);
	}
	

	public Map<String, Object> getBuildingsActive2(Long id) {
		return gongyingTravelMapper.get2(id);
	}

	public void deleteBuildingsActive2(Map<String, Object> entity) {
		Long id = VariableUtils.typeCast(entity.get("id"), Long.class);
		gongyingTravelMapper.delete2(id);
	}


	public Page<Map<String, Object>> findBasicDataList2(
			PageRequest pageRequest, Map<String, Object> filter, Model model) {
		model.addAttribute("travel_id", filter.get("travel_id"));
		
		long total = gongyingTravelMapper.count2(filter);
		filter.putAll(pageRequest.getMap());
		List<Map<String, Object>> content = gongyingTravelMapper
				.find2(filter);

		return new Page<Map<String, Object>>(pageRequest, content, total);
	}
	
	public void deletetravelsList(List<Long> ids) {

		for(Long id : ids) {
          //  Map<String, Object> entity = gongyingTravelMapper.get2(id);
          //  if (MapUtils.isNotEmpty(entity)) {
            	//deleteBuildings(entity);
            	
            	//  Long id = VariableUtils.typeCast(entity.get("id"),Long.class);
        	gongyingTravelMapper.delete2(id);
             // }
            }
        }
  
}
	
