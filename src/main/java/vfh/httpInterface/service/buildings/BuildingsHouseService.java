package vfh.httpInterface.service.buildings;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.swing.ImageIcon;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
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
import vfh.httpInterface.dao.buildings.BuildingsHouseMapper;
import vfh.httpInterface.dao.resourse.ResourseDao;
import vfh.httpInterface.service.resource.ResourceService;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

@Service
@Transactional
public class BuildingsHouseService {

	@Autowired
	private BuildingsHouseMapper buildingsHouseMapper;
	@Autowired
	private ResourseDao resourseDao;
	@Autowired
	private ResourceService resourceService;

	/**
	 * 户型图存放目录
	 */
	public static final String DEFAULT_USER_UPLOAD_PORTRAIT_PATH = "."
			+ File.separator + "upload_buildings" + File.separator;

	/**
	 * logo.png 路径  
	 */
	public static final String DEFAULT_LOGO_PATH = "."
			+ File.separator + "src" + File.separator +"main"+ File.separator +"webapp"+ File.separator +"resource"+ File.separator +"image"+ File.separator;
	
	
	public void insertBuildingsHouse(
			@MapValid("insert-house") Map<String, Object> entity) {

		buildingsHouseMapper.insert(entity);

	}

	public void update(@MapValid("update-house") Map<String, Object> entity) {
		buildingsHouseMapper.update(entity);
	}

	public Map<String, Object> get(Long id) {
		return buildingsHouseMapper.get(id);
	}

	public void delete(Map<String, Object> entity) {
		Long id = VariableUtils.typeCast(entity.get("id"), Long.class);
		buildingsHouseMapper.delete(id);
	}

	public void deleteBuildingsHouseList(List<Long> ids) {
		for (Long id : ids) {
			Map<String, Object> entity = get(id);
			if (MapUtils.isNotEmpty(entity)) {
				delete(entity);
			}
		}
	}

	public Page<Map<String, Object>> findBybuildId(PageRequest pageRequest,
			Map<String, Object> filter, Model model) {
		Long buildingsId = null;
		String buildingsName = null;

		if (StringUtil.isNotEmptyObject(filter.get("buildingsId"))) {
			buildingsId = Long.parseLong(filter.get("buildingsId").toString());
		}
		if (StringUtil.isNotEmptyObject(filter.get("buildingsName"))) {
			buildingsName = filter.get("buildingsName").toString();
		}
		model.addAttribute("buildingsName", filter.get("buildingsName"));
		model.addAttribute("buildingsId", filter.get("buildingsId"));

		long total = buildingsHouseMapper.count(filter);
		filter.putAll(pageRequest.getMap());
		List<Map<String, Object>> content = buildingsHouseMapper
				.findBybuildId(filter);

		return new Page<Map<String, Object>>(pageRequest, content, total);
	}

	//
	public Map<String, Object> getHuxingtuById(Long id) {
		return resourseDao.get(id);
	}

	// 添加户型图
	public void insertHuxingtu(HttpServletRequest request) throws IOException {

		Map<String, Object> entity = SessionVariable
				.getCurrentSessionVariable().getUser();
		String originalPicPath = null;
		String id = request.getParameter("id");
		String shi = request.getParameter("shi");
		Long buildings_id = Long
				.parseLong(request.getParameter("buildings_id"));
		MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;// request强制转换注意
		MultipartFile file = mRequest.getFile("image");
		if (!file.isEmpty()) {
			String uuid = UUID.randomUUID().toString();
			String fileName = file.getOriginalFilename();
			if (StringUtils.isNotBlank(fileName)) {// 因为最后一个添加的控件没有上传相应的内容

				String fileType = fileName.substring(fileName.lastIndexOf("."));
				// 使用字符替换图片名称，防止乱码
				String tempName = id + "_" + uuid.substring(1, 10) + fileType;

				File uploadfile = new File(DEFAULT_USER_UPLOAD_PORTRAIT_PATH
						+ buildings_id + File.separator + "huxingtu"
						+ File.separator + id + File.separator + tempName);// 上传地址

				if (!uploadfile.exists() || !uploadfile.isDirectory()) {
					uploadfile.deleteOnExit();
					uploadfile.mkdirs();
				}
				originalPicPath = DEFAULT_USER_UPLOAD_PORTRAIT_PATH
						+ buildings_id + File.separator + "huxingtu"
						+ File.separator + id + File.separator + tempName;
				file.transferTo(uploadfile);// 开始上传
				pressImage(originalPicPath,fileType);
				// 图片信息入库
				Long pid = Long.parseLong(id);
				String big_type = "1";
				String sm_type = shi;

				String resource_path = DEFAULT_USER_UPLOAD_PORTRAIT_PATH + id
						+ File.separator + "huxingtu" + File.separator + shi
						+ File.separator;
				// 要保存文件的文件名
				String resource_name = tempName;

				entity.put("pid", pid);
				entity.put("shi", shi);
				entity.put("big_type", big_type);
				entity.put("sm_type", sm_type);
				entity.put("resource_name", resource_name);
				entity.put("resource_path", resource_path);
				resourseDao.Insert(entity);

				// String portraitPath = DEFAULT_USER_UPLOAD_PORTRAIT_PATH +id+
				// File.separator + "huxingtu" + File.separator+ shi +
				// File.separator;
				// String originalPicPath = uploadfile.getAbsolutePath();
				//
				// scaleImage(originalPicPath, portraitPath,
				// PortraitSize.MIDDLE);
				// scaleImage(originalPicPath, portraitPath,
				// PortraitSize.SMALL);
			}
		}

	}

	// 添加效果图
	public void insertPicture(HttpServletRequest request) throws IOException {

		Map<String, Object> entity = SessionVariable
				.getCurrentSessionVariable().getUser();

		Long buildings_id = Long
				.parseLong(request.getParameter("buildings_id"));
		// Long sm_type = Long.parseLong(request.getParameter("sm_type"));
		String big_type = null;
		String resource_path = null;
		String originalPicPath  = null;
		File uploadfile = null;

		MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;// request强制转换注意
		MultipartFile file21 = mRequest.getFile("image21");
		MultipartFile file22 = mRequest.getFile("image22");
		MultipartFile file23 = mRequest.getFile("image23");
		MultipartFile file24 = mRequest.getFile("image24");
		MultipartFile file30 = mRequest.getFile("image30");
		MultipartFile file31 = mRequest.getFile("image31");

		if (null != file21 && !file21.isEmpty()) { // 效果图
			String uuid = UUID.randomUUID().toString();
			String fileName = file21.getOriginalFilename();
			String sm_type = "21";
			big_type = "2";
			if (StringUtils.isNotBlank(fileName)) {// 因为最后一个添加的控件没有上传相应的内容

				String fileType = fileName.substring(fileName.lastIndexOf("."));
				// 使用字符替换图片名称，防止乱码
				String tempName = sm_type + "_" + uuid.substring(1, 10)
						+ fileType;
				uploadfile = new File(DEFAULT_USER_UPLOAD_PORTRAIT_PATH
						+ buildings_id + File.separator + "xgt"
						+ File.separator + tempName);// 上传地址

				if (!uploadfile.exists() || !uploadfile.isDirectory()) {
					uploadfile.deleteOnExit();
					uploadfile.mkdirs();

					// 图片信息入库
					// 图片大类：1：户型图；2：楼盘相册;3：地产logo，4：地产广告，5：banner，6：活动宣传图

					resource_path = DEFAULT_USER_UPLOAD_PORTRAIT_PATH
							+ buildings_id + File.separator + "xgt"
							+ File.separator;
				}

				// 开始上传
				file21.transferTo(uploadfile);
				pressImage(originalPicPath,fileType);
				// 图片加水印

				// 要保存文件的文件名
				String resource_name = tempName;

				entity.put("big_type", big_type);
				entity.put("sm_type", sm_type);
				entity.put("buildings_id", buildings_id);
				entity.put("resource_name", resource_name);
				entity.put("resource_path", resource_path);
				resourseDao.Insert(entity);

				// String portraitPath = DEFAULT_USER_UPLOAD_PORTRAIT_PATH +id+
				// File.separator + "huxingtu" + File.separator+ shi +
				// File.separator;
				// String originalPicPath = uploadfile.getAbsolutePath();
				//
				// scaleImage(originalPicPath, portraitPath,
				// PortraitSize.MIDDLE);
				// scaleImage(originalPicPath, portraitPath,
				// PortraitSize.SMALL);
			}
		}

		if (null != file22 && !file22.isEmpty()) { // 交通图
			String uuid = UUID.randomUUID().toString();
			String fileName = file22.getOriginalFilename();
			String sm_type = "22";
			big_type = "2";
			if (StringUtils.isNotBlank(fileName)) {// 因为最后一个添加的控件没有上传相应的内容

				String fileType = fileName.substring(fileName.lastIndexOf("."));
				// 使用字符替换图片名称，防止乱码
				String tempName = sm_type + "_" + uuid.substring(1, 10)
						+ fileType;
				uploadfile = new File(DEFAULT_USER_UPLOAD_PORTRAIT_PATH
						+ buildings_id + File.separator + "jtt"
						+ File.separator + tempName);// 上传地址

				if (!uploadfile.exists() || !uploadfile.isDirectory()) {
					uploadfile.deleteOnExit();
					uploadfile.mkdirs();

					// 图片信息入库
					// 图片大类：1：户型图；2：楼盘相册;3：地产logo，4：地产广告，5：banner，6：活动宣传图

					resource_path = DEFAULT_USER_UPLOAD_PORTRAIT_PATH
							+ buildings_id + File.separator + "jtt"
							+ File.separator;
				}

				// 开始上传
				file22.transferTo(uploadfile);
				pressImage(originalPicPath,fileType);
				// 要保存文件的文件名
				String resource_name = tempName;

				entity.put("big_type", big_type);
				entity.put("sm_type", sm_type);
				entity.put("buildings_id", buildings_id);
				entity.put("resource_name", resource_name);
				entity.put("resource_path", resource_path);
				resourseDao.Insert(entity);

				// String portraitPath = DEFAULT_USER_UPLOAD_PORTRAIT_PATH +id+
				// File.separator + "huxingtu" + File.separator+ shi +
				// File.separator;
				// String originalPicPath = uploadfile.getAbsolutePath();
				//
				// scaleImage(originalPicPath, portraitPath,
				// PortraitSize.MIDDLE);
				// scaleImage(originalPicPath, portraitPath,
				// PortraitSize.SMALL);
			}
		}
		if (null != file23 && !file23.isEmpty()) { // 实景图
			String uuid = UUID.randomUUID().toString();
			String fileName = file23.getOriginalFilename();
			String sm_type = "23";
			big_type = "2";
			if (StringUtils.isNotBlank(fileName)) {// 因为最后一个添加的控件没有上传相应的内容

				String fileType = fileName.substring(fileName.lastIndexOf("."));
				// 使用字符替换图片名称，防止乱码
				String tempName = sm_type + "_" + uuid.substring(1, 10)
						+ fileType;
				uploadfile = new File(DEFAULT_USER_UPLOAD_PORTRAIT_PATH
						+ buildings_id + File.separator + "sjt"
						+ File.separator + tempName);// 上传地址

				if (!uploadfile.exists() || !uploadfile.isDirectory()) {
					uploadfile.deleteOnExit();
					uploadfile.mkdirs();

					// 图片信息入库
					// 图片大类：1：户型图；2：楼盘相册;3：地产logo，4：地产广告，5：banner，6：活动宣传图

					resource_path = DEFAULT_USER_UPLOAD_PORTRAIT_PATH
							+ buildings_id + File.separator + "sjt"
							+ File.separator;
				}

				// 开始上传
				file23.transferTo(uploadfile);

				// 要保存文件的文件名
				String resource_name = tempName;
				pressImage(originalPicPath,fileType);
				entity.put("big_type", big_type);
				entity.put("sm_type", sm_type);
				entity.put("buildings_id", buildings_id);
				entity.put("resource_name", resource_name);
				entity.put("resource_path", resource_path);
				resourseDao.Insert(entity);

				// String portraitPath = DEFAULT_USER_UPLOAD_PORTRAIT_PATH +id+
				// File.separator + "huxingtu" + File.separator+ shi +
				// File.separator;
				// String originalPicPath = uploadfile.getAbsolutePath();
				//
				// scaleImage(originalPicPath, portraitPath,
				// PortraitSize.MIDDLE);
				// scaleImage(originalPicPath, portraitPath,
				// PortraitSize.SMALL);
			}
		}

		if (null != file24 && !file24.isEmpty()) { // 样板间
			String uuid = UUID.randomUUID().toString();
			String fileName = file24.getOriginalFilename();
			String sm_type = "24";
			big_type = "2";
			if (StringUtils.isNotBlank(fileName)) {// 因为最后一个添加的控件没有上传相应的内容

				String fileType = fileName.substring(fileName.lastIndexOf("."));
				// 使用字符替换图片名称，防止乱码
				String tempName = sm_type + "_" + uuid.substring(1, 10)
						+ fileType;
				uploadfile = new File(DEFAULT_USER_UPLOAD_PORTRAIT_PATH
						+ buildings_id + File.separator + "ybj"
						+ File.separator + tempName);// 上传地址

				if (!uploadfile.exists() || !uploadfile.isDirectory()) {
					uploadfile.deleteOnExit();
					uploadfile.mkdirs();

					// 图片信息入库
					// 图片大类：1：户型图；2：楼盘相册;3：地产logo，4：地产广告，5：banner，6：活动宣传图

					resource_path = DEFAULT_USER_UPLOAD_PORTRAIT_PATH
							+ buildings_id + File.separator + "ybj"
							+ File.separator;
				}

				// 开始上传
				file24.transferTo(uploadfile);
				pressImage(originalPicPath,fileType);
				// 要保存文件的文件名
				String resource_name = tempName;

				entity.put("big_type", big_type);
				entity.put("sm_type", sm_type);
				entity.put("buildings_id", buildings_id);
				entity.put("resource_name", resource_name);
				entity.put("resource_path", resource_path);
				resourseDao.Insert(entity);

				// String portraitPath = DEFAULT_USER_UPLOAD_PORTRAIT_PATH +id+
				// File.separator + "huxingtu" + File.separator+ shi +
				// File.separator;
				// String originalPicPath = uploadfile.getAbsolutePath();
				//
				// scaleImage(originalPicPath, portraitPath,
				// PortraitSize.MIDDLE);
				// scaleImage(originalPicPath, portraitPath,
				// PortraitSize.SMALL);
			}
		}

		if (null != file30 && !file30.isEmpty()) { // logo
			String uuid = UUID.randomUUID().toString();
			String fileName = file30.getOriginalFilename();
			String sm_type = "30";
			big_type = "3";
			if (StringUtils.isNotBlank(fileName)) {// 因为最后一个添加的控件没有上传相应的内容

				String fileType = fileName.substring(fileName.lastIndexOf("."));
				// 使用字符替换图片名称，防止乱码
				String tempName = sm_type + "_" + uuid.substring(1, 10)
						+ fileType;
				uploadfile = new File(DEFAULT_USER_UPLOAD_PORTRAIT_PATH
						+ buildings_id + File.separator + "logo"
						+ File.separator + tempName);// 上传地址

				if (!uploadfile.exists() || !uploadfile.isDirectory()) {
					uploadfile.deleteOnExit();
					uploadfile.mkdirs();

					// 图片信息入库
					// 图片大类：1：户型图；2：楼盘相册;3：地产logo，4：地产广告，5：banner，6：活动宣传图

					resource_path = DEFAULT_USER_UPLOAD_PORTRAIT_PATH
							+ buildings_id + File.separator + "logo"
							+ File.separator;
					originalPicPath = DEFAULT_USER_UPLOAD_PORTRAIT_PATH
							+ buildings_id + File.separator + "logo"
							+ File.separator + tempName;
				}

				// 开始上传
				file30.transferTo(uploadfile);

				// 要保存文件的文件名
				String resource_name = tempName;

				entity.put("big_type", big_type);
				entity.put("sm_type", sm_type);
				entity.put("buildings_id", buildings_id);
				entity.put("resource_name", resource_name);
				entity.put("resource_path", resource_path);
				resourseDao.Insert(entity);

				// String portraitPath = DEFAULT_USER_UPLOAD_PORTRAIT_PATH +id+
				// File.separator + "huxingtu" + File.separator+ shi +
				// File.separator;
				// String originalPicPath = uploadfile.getAbsolutePath();
				//
				// scaleImage(originalPicPath, portraitPath,
				// PortraitSize.MIDDLE);
				// scaleImage(originalPicPath, portraitPath,
				// PortraitSize.SMALL);
			}
		}

		if (null != file31 && !file31.isEmpty()) { // 地产宣传图
			String uuid = UUID.randomUUID().toString();
			String fileName = file31.getOriginalFilename();
			String sm_type = "31";
			big_type = "3";
			if (StringUtils.isNotBlank(fileName)) {// 因为最后一个添加的控件没有上传相应的内容

				String fileType = fileName.substring(fileName.lastIndexOf("."));
				// 使用字符替换图片名称，防止乱码
				String tempName = sm_type + "_" + uuid.substring(1, 10)
						+ fileType;
				uploadfile = new File(DEFAULT_USER_UPLOAD_PORTRAIT_PATH
						+ buildings_id + File.separator + "xct"
						+ File.separator + tempName);// 上传地址

				if (!uploadfile.exists() || !uploadfile.isDirectory()) {
					uploadfile.deleteOnExit();
					uploadfile.mkdirs();

					// 图片信息入库
					// 图片大类：1：户型图；2：楼盘相册;3：地产logo，4：地产广告，5：banner，6：活动宣传图

					resource_path = DEFAULT_USER_UPLOAD_PORTRAIT_PATH
							+ buildings_id + File.separator + "xct"
							+ File.separator;
				}

				// 开始上传
				file31.transferTo(uploadfile);

				// 要保存文件的文件名
				String resource_name = tempName;

				entity.put("big_type", big_type);
				entity.put("sm_type", sm_type);
				entity.put("buildings_id", buildings_id);
				entity.put("resource_name", resource_name);
				entity.put("resource_path", resource_path);
				resourseDao.Insert(entity);

				// String portraitPath = DEFAULT_USER_UPLOAD_PORTRAIT_PATH +id+
				// File.separator + "huxingtu" + File.separator+ shi +
				// File.separator;
				// String originalPicPath = uploadfile.getAbsolutePath();
				//
				// scaleImage(originalPicPath, portraitPath,
				// PortraitSize.MIDDLE);
				// scaleImage(originalPicPath, portraitPath,
				// PortraitSize.SMALL);
			}
		}
	}

	/**
	 * 辅助方法，缩小图片像素
	 * 
	 * @param sourcePath
	 *            源图片路径
	 * @param targetPath
	 *            缩小后的图片路径
	 * @param portraitSize
	 *            头像尺寸
	 * 
	 * @throws IOException
	 */
	private String scaleImage(String sourcePath, String targetPath,
			PortraitSize portraitSize) throws IOException {
		InputStream inputStream = new FileInputStream(sourcePath);

		BufferedImage source = ImageIO.read(inputStream);
		ColorModel targetColorModel = source.getColorModel();

		inputStream.close();

		int width = portraitSize.getWidth();
		int height = portraitSize.getHeight();

		BufferedImage target = new BufferedImage(targetColorModel,
				targetColorModel.createCompatibleWritableRaster(width, height),
				targetColorModel.isAlphaPremultiplied(), null);

		Image scaleImage = source.getScaledInstance(width, height,
				Image.SCALE_SMOOTH);

		Graphics2D g = target.createGraphics();
		g.drawImage(scaleImage, 0, 0, width, height, null);
		g.dispose();

		String result = targetPath + portraitSize.getName();
		ImageIO.write(target, "jpeg", new FileOutputStream(result));

		return result;
	}

//添加水印
	@SuppressWarnings("restriction")
	public void pressImage(String targetImg, String fileType) throws FileNotFoundException {

		try {
			InputStream inputStream = new FileInputStream(targetImg);

			//通过JPEG图象流创建JPEG数据流解码器
	        JPEGImageDecoder jpegDecoder = JPEGCodec.createJPEGDecoder(inputStream);
	        //解码当前JPEG数据流，返回BufferedImage对象
	        BufferedImage buffImg = jpegDecoder.decodeAsBufferedImage();
	        //得到画笔对象
	        Graphics g = buffImg.getGraphics();
	        
	        //创建你要附加的图象。
	        //2.jpg是你的小图片的路径
	        ImageIcon imgIcon = new ImageIcon(DEFAULT_LOGO_PATH
					+ "logo.png"); 
	        
	        //得到Image对象。
	        Image img = imgIcon.getImage(); 
	        
	        //将小图片绘到大图片上。
	        //5,300 .表示你的小图片在大图片上的位置。
	        g.drawImage(img,10,10,132,51,null);
	      
	       
	        
	        //创键编码器，用于编码内存中的图象数据。
	        
	        ImageIO.write(buffImg, "jpeg", new FileOutputStream(targetImg));
	        
	        inputStream.close();
	      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
