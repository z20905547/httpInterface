package vfh.httpInterface.service.gongying;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.dao.gongying.GongyingCustomerMapper;

@Service
@Transactional
public class GongyingCustomerService {
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
	private GongyingCustomerMapper gongyingCustomerMapper;

	
	
	public void insertBuildingsActive(
			 Map<String, Object> entity) {

		gongyingCustomerMapper.insert(entity);
		
	}

	public void updateBuildingsActive(
			Map<String, Object> entity) {
		gongyingCustomerMapper.update(entity);
	}
	

	public Map<String, Object> getBuildingsActive(Long customer_id) {
		return gongyingCustomerMapper.get(customer_id);
	}

	public void deleteBuildingsActive(Map<String, Object> entity) {
		Long id = VariableUtils.typeCast(entity.get("id"), Long.class);
		gongyingCustomerMapper.delete(id);
	}


	public Page<Map<String, Object>> findBasicDataList(
			PageRequest pageRequest, Map<String, Object> filter, Model model) {

		long total = gongyingCustomerMapper.count(filter);
		filter.putAll(pageRequest.getMap());
		List<Map<String, Object>> content = gongyingCustomerMapper
				.find(filter);

		return new Page<Map<String, Object>>(pageRequest, content, total);
	}
	
	}
