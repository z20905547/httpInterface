package vfh.httpInterface.service.gongying;


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
import vfh.httpInterface.dao.gongying.GongyingBasicDataMapper;
import vfh.httpInterface.dao.resourse.ResourseDao;
import vfh.httpInterface.service.resource.ResourceService;

@Service
@Transactional
public class GongyingBasicDataService {
	/**
     * 活动图存放目录 
     */
   // 本地
   // public static final String DEFAULT_USER_UPLOAD_PORTRAIT_PATH = "./src/main/webapp/resource/upload_buildings/" ;
   // public static final String DEFAULT_USER_UPLOAD_PORTRAIT_PATH2 =  "/resource/upload_buildings/" ;
  
	//正式环境
	public static final String DEFAULT_USER_UPLOAD_PORTRAIT_PATH = "/vfh/apache-tomcat-7.0.67/webapps/management/resource/upload_buildings/" ;
	public static final String DEFAULT_USER_UPLOAD_PORTRAIT_PATH2 = 
			 "/resource/upload_buildings/" ;
	 //水印logo 
	public static final String DEFAULT_LOGO_PATH ="/vfh/apache-tomcat-7.0.67/webapps/management/resource/upload_buildings/";

	//本地路径
//	public static final String DEFAULT_USER_UPLOAD_PORTRAIT_PATH = "./upload_buildings/" ;
//
//	public static final String DEFAULT_USER_UPLOAD_PORTRAIT_PATH2 = 
//			 "./resource/upload_buildings/" ;
//	//logo
//	public static final String DEFAULT_LOGO_PATH ="./upload_buildings/";
//	
	
	@Autowired 
	private GongyingBasicDataMapper gongyingBasicDataMapper;

	
	
	public void insertBuildingsActive(
			 Map<String, Object> entity) {

		gongyingBasicDataMapper.insert(entity);
		
	}

	public void updateBuildingsActive(
			Map<String, Object> entity) {
		gongyingBasicDataMapper.update(entity);
	}
	

	public Map<String, Object> getBuildingsActive(Long id) {
		return gongyingBasicDataMapper.get(id);
	}

	public void deleteBuildingsActive(Map<String, Object> entity) {
		Long id = VariableUtils.typeCast(entity.get("id"), Long.class);
		gongyingBasicDataMapper.delete(id);
	}


	public Page<Map<String, Object>> findBasicDataList(
			PageRequest pageRequest, Map<String, Object> filter, Model model) {
		Long buildings_id = null;
		String buildingsName = null;


			buildings_id = Long.parseLong(filter.get("buildings_id").toString());

		
		
		if (StringUtil.isNotEmptyObject(filter.get("buildingsName"))) {
			buildingsName = filter.get("buildingsName").toString();
		}
		model.addAttribute("buildingsName", buildingsName);
		model.addAttribute("buildings_id", buildings_id);
		long total = gongyingBasicDataMapper.count(filter);
		filter.putAll(pageRequest.getMap());
		List<Map<String, Object>> content = gongyingBasicDataMapper
				.find(filter);

		return new Page<Map<String, Object>>(pageRequest, content, total);
	}
	
	}
