package vfh.httpInterface.service.buildings;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.swing.ImageIcon;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.commons.SessionVariable;
import vfh.httpInterface.commons.StringUtil;
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.commons.enumeration.entity.PortraitSize;
import vfh.httpInterface.commons.valid.annotation.MapValid;
import vfh.httpInterface.dao.buildings.BuildingActiveMapper;
import vfh.httpInterface.dao.buildings.BuildingLatestMapper;
import vfh.httpInterface.dao.buildings.BuildingsMapper;
import vfh.httpInterface.dao.buildings.BuildingsPriceMapper;
import vfh.httpInterface.dao.resourse.ResourseDao;
import vfh.httpInterface.service.resource.ResourceService;

@Service
@Transactional
public class BuildingsLatestService {
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
	private BuildingLatestMapper buildingLatestMapper;
	@Autowired
	private ResourseDao resourseDao;  
	@Autowired
	private ResourceService resourceService;
	
	
	public void insertBuildingsActive(
			@MapValid("insert-latest") Map<String, Object> entity) {

			buildingLatestMapper.insert(entity);
		
	}

	public void updateBuildingsActive(
			@MapValid("update-latest") Map<String, Object> entity) {
		buildingLatestMapper.update(entity);
	}
	
	public void updateBuildingsActiveUpdata(
			@MapValid("update-latest") Map<String, Object> entity) {
		buildingLatestMapper.updateActiveData(entity);
	}

	public Map<String, Object> getBuildingsActive(Long id) {
		return buildingLatestMapper.get(id);
	}

	public void deleteBuildingsActive(Map<String, Object> entity) {
		Long id = VariableUtils.typeCast(entity.get("id"), Long.class);
		buildingLatestMapper.delete(id);
	}


	public Page<Map<String, Object>> findBuildingsLatestList(
			PageRequest pageRequest, Map<String, Object> filter, Model model) {
		Long buildings_id = null;
		String buildingsName = null;


			buildings_id = Long.parseLong(filter.get("buildings_id").toString());

		
		
		if (StringUtil.isNotEmptyObject(filter.get("buildingsName"))) {
			buildingsName = filter.get("buildingsName").toString();
		}
		model.addAttribute("buildingsName", buildingsName);
		model.addAttribute("buildings_id", buildings_id);
		long total = buildingLatestMapper.count(filter);
		filter.putAll(pageRequest.getMap());
		List<Map<String, Object>> content = buildingLatestMapper
				.find(filter);

		return new Page<Map<String, Object>>(pageRequest, content, total);
	}
	
	}
