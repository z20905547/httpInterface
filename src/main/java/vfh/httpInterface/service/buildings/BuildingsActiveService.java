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
import vfh.httpInterface.dao.buildings.BuildingsMapper;
import vfh.httpInterface.dao.buildings.BuildingsPriceMapper;
import vfh.httpInterface.dao.resourse.ResourseDao;
import vfh.httpInterface.service.resource.ResourceService;

@Service
@Transactional
public class BuildingsActiveService {
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
	private BuildingActiveMapper buildingActiveMapper;
	@Autowired
	private ResourseDao resourseDao;  
	@Autowired
	private ResourceService resourceService;
	
	public void insertBuildingsActive(
			@MapValid("insert-active") Map<String, Object> entity) {

		try { 
			// 如果上一条特价记录存在特价且时间尚未结束，则先更新上条记录的结束日期为这条记录开始的前一天
			// 如果上一条特价记录已经截止，则不做任何处理
			Map<String, Object> filter = new HashMap<String, Object>();
			filter.put("buildings_id", entity.get("buildings_id"));
			List<Map<String, Object>> activeList = buildingActiveMapper
					.find(filter);

			if (activeList.size() > 0 && activeList.get(0).get("end_date") != null) {
				Map<String, Object> preActive = activeList.get(0);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String startstr = entity.get("start_date").toString();
				String endstr = activeList.get(0).get("end_date").toString();
				Date startData = sdf.parse(startstr);
				Date endData = sdf.parse(endstr);


			//本次特价开始时间和上次特价截止时间做比较
		        Calendar c1=Calendar.getInstance();  
		        Calendar c2=Calendar.getInstance();  
		        try {  
		            c1.setTime(sdf.parse(startstr));  
		            c2.setTime(sdf.parse(endstr));  
		        } catch (ParseException e) {  
		            e.printStackTrace();  
		        }  
		        int result=c1.compareTo(c2); 				
				
		     // 如果上一条特价记录存在特价且时间尚未结束，则先更新上条记录的结束日期为这条记录开始的前一天		
				if (result==0 || result==-1) {
					startData.setTime(startData.getTime() - 24 * 60 * 60 * 1000);
					String preend = sdf.format(startData);
					Map<String, Object> updateParams = new HashMap<String, Object>();
					updateParams.put("id", preActive.get("id"));
					updateParams.put("end_date", preend);
					buildingActiveMapper.update(updateParams);
				}
			}

			// entity.put("endDate", "2116-12-12 00:00:00");
//			entity.put("activeId", 0);
//			if (entity.get("discount_price") == null
//					|| entity.get("discount_price").equals(""))
//				entity.put("discount_price", null);
//			if (entity.get("first_price") == null
//					|| entity.get("first_price").equals(""))
//				entity.put("first_price", null);
//			if (entity.get("nomal_price") == null
//					|| entity.get("nomal_price").equals(""))
//				entity.put("nomal_price", null);
//			if (entity.get("nomal_price") == null
//					|| entity.get("nomal_price").equals(""))
//				entity.put("nomal_price", null);
//			if (entity.get("start_date") == null
//					|| entity.get("start_date").equals(""))
//				entity.put("start_date", null);
//			if (entity.get("end_date") == null
//					|| entity.get("end_date").equals(""))
//				entity.put("end_date", null);

//            entity.put("active_type", "1");
//            entity.put("active_status", "1");
			buildingActiveMapper.insert(entity);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateBuildingsActive(
			@MapValid("update-active") Map<String, Object> entity) {
		buildingActiveMapper.update(entity);
	}

	public Map<String, Object> getBuildingsActive(Long id) {
		return buildingActiveMapper.get(id);
	}

	public void deleteBuildingsActive(Map<String, Object> entity) {
		Long id = VariableUtils.typeCast(entity.get("id"), Long.class);
		buildingActiveMapper.delete(id);
	}

//	public void deleteBuildingsPriceList(List<Long> ids) {
//		for (Long id : ids) {
//			Map<String, Object> entity = getBuildingsPrice(id);
//			if (MapUtils.isNotEmpty(entity)) {
//				deleteBuildingsPrice(entity);
//			}
//		}
//	}

	public Page<Map<String, Object>> findBuildingsActiveList(
			PageRequest pageRequest, Map<String, Object> filter, Model model) {
		Long buildings_id = null;
		String buildingsName = null;


			buildings_id = Long.parseLong(filter.get("buildings_id").toString());

		
		
		if (StringUtil.isNotEmptyObject(filter.get("buildingsName"))) {
			buildingsName = filter.get("buildingsName").toString();
		}
		model.addAttribute("buildingsName", buildingsName);
		model.addAttribute("buildings_id", buildings_id);
		long total = buildingActiveMapper.count(filter);
		filter.putAll(pageRequest.getMap());
		List<Map<String, Object>> content = buildingActiveMapper
				.find(filter);

		return new Page<Map<String, Object>>(pageRequest, content, total);
	}
	
	//添加活动图
		public void insertAcPic (HttpServletRequest request) throws IOException {

			Map<String, Object> entity = SessionVariable.getCurrentSessionVariable().getUser();
			String originalPicPath  = null;
			String resource_path = null;
	      //  String id = request.getParameter("id");
	       // String buildings_id = request.getParameter("buildings_id");
	        Long buildings_id = Long.parseLong( request.getParameter("buildings_id"));
			MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//request强制转换注意
	    	MultipartFile file = mRequest.getFile("image");
	        if (!file.isEmpty()) {
	        	
	          String fileName = file.getOriginalFilename();
	          if (StringUtils.isNotBlank(fileName)) {// 因为最后一个添加的控件没有上传相应的内容

	            String fileType = fileName.substring(fileName.lastIndexOf("."));
	            // 使用字符替换图片名称，防止乱码
	            String tempName = "hdt" + fileType;

	            File uploadfile = new File(DEFAULT_USER_UPLOAD_PORTRAIT_PATH +buildings_id + "/hdt/" + tempName);// 上传地址

				if (uploadfile.isFile() && uploadfile.exists()) {  //文件夹中存在文件则表示已经入库过，则不再入库记录
					
				}else {
					
					// 图片信息入库
		          //Long pid = Long.parseLong( id);
					String big_type = "6";
					resource_path = DEFAULT_USER_UPLOAD_PORTRAIT_PATH2 +buildings_id+ "/hdt/" ;
					// 要保存文件的文件名
					String resource_name = tempName;


				//	entity.put("pid", pid);
					entity.put("buildings_id", buildings_id);
					entity.put("big_type", big_type);
					entity.put("sm_type", "33");
					entity.put("resource_name", resource_name);
					entity.put("resource_path", resource_path);
					resourseDao.Insert(entity); 
				}
	            
	            if (!uploadfile.exists() || !uploadfile.isDirectory()) {
	            
	            	uploadfile.mkdirs();
	            	
	            	resource_path = DEFAULT_USER_UPLOAD_PORTRAIT_PATH2
							+ buildings_id + "/hdt/" ;
					originalPicPath = DEFAULT_USER_UPLOAD_PORTRAIT_PATH
							+ buildings_id+ "/hdt/" 
							+ tempName;
	            }
	          
	            file.transferTo(uploadfile);// 开始上传
	            //添加水印
				pressImage(originalPicPath,fileType);
	            
				//每上传一次活动图 就更新一次更新时间
				//entity.clear();
			//	entity.put("id", id);
				buildingActiveMapper.update(entity);
//	            String portraitPath = DEFAULT_USER_UPLOAD_PORTRAIT_PATH +id+ File.separator + "huxingtu" + File.separator+ shi + File.separator;
//	            String originalPicPath = uploadfile.getAbsolutePath();
//	            
//	            scaleImage(originalPicPath, portraitPath, PortraitSize.MIDDLE);
//	            scaleImage(originalPicPath, portraitPath, PortraitSize.SMALL);
	          }}
		}
		   /**
	     * 辅助方法，缩小图片像素
	     *
	     * @param sourcePath 源图片路径
	     * @param targetPath 缩小后的图片路径
	     * @param portraitSize 头像尺寸
	     *
	     * @throws IOException
	     */
	    private String scaleImage(String sourcePath, String targetPath, PortraitSize portraitSize) throws IOException {
	        InputStream inputStream = new FileInputStream(sourcePath);

	        BufferedImage source = ImageIO.read(inputStream);
	        ColorModel targetColorModel = source.getColorModel();

	        inputStream.close();

	        int width = portraitSize.getWidth();
	        int height = portraitSize.getHeight();

	        BufferedImage target = new BufferedImage(
	                targetColorModel,
	                targetColorModel.createCompatibleWritableRaster( width, height ),
	                targetColorModel.isAlphaPremultiplied(),
	                null
	        );

	        Image scaleImage = source.getScaledInstance(width, height, Image.SCALE_SMOOTH);

	        Graphics2D g = target.createGraphics();
	        g.drawImage(scaleImage, 0, 0, width, height, null);
	        g.dispose();

	        String result = targetPath + portraitSize.getName();
	        ImageIO.write(target,"jpeg", new FileOutputStream(result));

	        return result;
	    }
	    
	  //添加水印
		@SuppressWarnings("restriction")
		public void pressImage(String targetImg, String fileType) throws FileNotFoundException {
			OutputStream os = null;
			try {
				InputStream inputStream = new FileInputStream(targetImg);

				BufferedImage buffImg = ImageIO.read(inputStream);
				   int wideth = buffImg.getWidth(null); 
				   int height = buffImg.getHeight(null);
				   
		        //得到画笔对象
		        Graphics g = buffImg.getGraphics();
		        
		        //创建你要附加的图象。
		        //2.jpg是你的小图片的路径
		        ImageIcon imgIcon = new ImageIcon(DEFAULT_LOGO_PATH
						+ "logo.png"); 
		      
		        //得到Image对象。
		        Image img = imgIcon.getImage(); 
		        int wideth_biao = img.getWidth(null); 
		        int height_biao = img.getHeight(null); 
		        //将小图片绘到大图片上。
		        //5,300 .表示你的小图片在大图片上的位置。
		        g.drawImage(img,(wideth - wideth_biao) ,(height - height_biao) , wideth_biao, height_biao, null);
		      
		        
		        
		        g.dispose();
		        
		        
		        //创键编码器，用于编码内存中的图象数据。
		        os = new FileOutputStream(targetImg); 
		        
		        ImageIO.write(buffImg, "jpeg", os );
		        
		        inputStream.close();
		        
		         
		      
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {     
	            try {     
	                if (null != os)     
	                    os.close();     
	            } catch (Exception e) {     
	                e.printStackTrace();     
	            } 
			}
		}
}
