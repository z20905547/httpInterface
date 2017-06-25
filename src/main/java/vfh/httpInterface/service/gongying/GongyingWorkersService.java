package vfh.httpInterface.service.gongying;

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
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.dao.resourse.ResourseDao;
import vfh.httpInterface.service.resource.ResourceService;
import vfh.httpInterface.dao.gongying.GongyingWorkersMapper;

@Service
@Transactional
public class GongyingWorkersService {
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
	private GongyingWorkersMapper gongyingWorkersMapper;

	
	
	public void insertBuildingsActive(
			 Map<String, Object> entity) {

		gongyingWorkersMapper.insert(entity);
		
	}

	public void updateBuildingsActive(
			Map<String, Object> entity) {
		gongyingWorkersMapper.update(entity);
	}
	

	public Map<String, Object> getBuildingsActive(Long id) {
		return gongyingWorkersMapper.get(id);
	}

	public void deleteBuildingsActive(Map<String, Object> entity) {
		Long id = VariableUtils.typeCast(entity.get("id"), Long.class);
		gongyingWorkersMapper.delete(id);
	}


	public Page<Map<String, Object>> findBasicDataList(
			PageRequest pageRequest, Map<String, Object> filter, Model model) {

		long total = gongyingWorkersMapper.count(filter);
		filter.putAll(pageRequest.getMap());
		List<Map<String, Object>> content = gongyingWorkersMapper
				.find(filter);

		return new Page<Map<String, Object>>(pageRequest, content, total);
	}
	
	}
