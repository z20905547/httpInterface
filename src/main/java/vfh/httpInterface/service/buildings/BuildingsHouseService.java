package vfh.httpInterface.service.buildings;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

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
    public static final String DEFAULT_USER_UPLOAD_PORTRAIT_PATH = "." + File.separator + "upload_huxingtu" + File.separator;
	
	public void insertBuildingsHouse(@MapValid("insert-house") Map<String, Object> entity){

			buildingsHouseMapper.insert(entity);

	}
	public void update(@MapValid("update-house")Map<String, Object> entity){
		buildingsHouseMapper.update(entity);
	}
	public Map<String, Object> get(Long id) {
        return buildingsHouseMapper.get(id);
    }
	public void delete(Map<String,Object> entity) {
        Long id = VariableUtils.typeCast(entity.get("id"),Long.class);
        buildingsHouseMapper.delete(id);
    }
	public void deleteBuildingsHouseList(List<Long> ids) {
        for(Long id : ids) {
            Map<String, Object> entity = get(id);
            if (MapUtils.isNotEmpty(entity)) {
            	delete(entity);
            }
        }
    }
	public Page<Map<String, Object>> findBybuildId(PageRequest pageRequest, Map<String, Object> filter,Model model) {
		Long buildingsId=null;
		String buildingsName=null;

		if(StringUtil.isNotEmptyObject(filter.get("buildingsId"))){
			buildingsId=Long.parseLong(filter.get("buildingsId").toString());
		}
		if(StringUtil.isNotEmptyObject(filter.get("buildingsName"))){
			buildingsName=filter.get("buildingsName").toString();
		}
		model.addAttribute("buildingsName",filter.get("buildingsName"));
		model.addAttribute("buildingsId",filter.get("buildingsId"));

		long total = buildingsHouseMapper.count(filter);
        filter.putAll(pageRequest.getMap());
        List<Map<String, Object>> content = buildingsHouseMapper.findBybuildId(filter);

        return new Page<Map<String, Object>>(pageRequest, content, total);
    }
	
	
	public Page<Map<String, Object>> insertHuxingtu (InputStream is, Map<String, Object> filter,Model model) throws IOException {
        Map<String, Object> entity = SessionVariable.getCurrentSessionVariable().getUser();
		

        File file = new File(DEFAULT_USER_UPLOAD_PORTRAIT_PATH + File.separator);


			if (filter !=null) {
				
				Long pid = Long.parseLong( filter.get("id").toString());
				String shi = filter.get("shi").toString();
				String big_type = "1";
				String sm_type = shi;
				String fileName = filter.get("images[]").toString();
				String resource_path = DEFAULT_USER_UPLOAD_PORTRAIT_PATH;
				// 要保存文件的文件名
				String resource_name = resourceService.generateFileName() + "u" + pid
						+ resourceService.getFileNamePostfix(fileName);

				SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMdd" );
				String startstr=sdf.format(new Date()).toString();

				entity.put("pid", pid);
				entity.put("shi", shi);
				entity.put("big_type", big_type);
				entity.put("sm_type", sm_type);
				entity.put("resource_name", resource_name);
				entity.put("resource_path", resource_path);
				
				
				resourseDao.hxtInsert(entity); 
				String portraitPath = file.getAbsolutePath() + File.separator;
				  String originalPicPath = portraitPath + resource_name;
		        System.out.println("ooooooooooooooooooooooo");
		        System.out.println(portraitPath);
		        System.out.println(originalPicPath);
		        
		       // if(is.available()!=0){
		        	  //System.out.println(is.available());
		        //	}
				  int ret = IOUtils.copy(is, new FileOutputStream(originalPicPath));
				  System.out.println(ret);

		      //  scaleImage(originalPicPath, portraitPath, PortraitSize.MIDDLE);
		      //  scaleImage(originalPicPath, portraitPath, PortraitSize.SMALL);
				// 生成一个以时间命名的文件夹
				//String dateFolder = "/" + startstr + "/";

				// 要保存的文件路径名
				//saveFileFolder = EbankConstants.EFILE_TOC_PATH + dateFolder;
				

				//Long photo_id = photoDAO.insert(photo);
				
//			if(photo_id!=0){
//				photo =  photoDAO.selectByPrimaryKey(photo_id);
//			}
//			
//			String photoUploadPath = path + saveFileFolder;
//			try {
//				// 上传 文件
////				resourceService.saveFile(resource_path, resource_name, uploadFile);
////				for (int i = 0; i < EbankConstants.PHOTO_SIZE.length; i++) {
////					String targetPhotoName = EbankConstants.PHOTO_SIZE[i][0] + EbankConstants.PHOTO_SPILT
////							+ EbankConstants.PHOTO_SIZE[i][1] + EbankConstants.PHOTO_SPILT + saveFileName;
////					// 缩放图片
////					PictureUtils.resize(uploadFile, photoUploadPath, targetPhotoName, EbankConstants.PHOTO_SIZE[i][0],
////							EbankConstants.PHOTO_SIZE[i][1]);
////				}
//			} catch (IOException e) {
//				log.throwing(Level.ERROR, e);
//			}
				
			}

		return null;
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


}
