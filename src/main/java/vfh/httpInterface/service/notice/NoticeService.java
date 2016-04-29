package vfh.httpInterface.service.notice;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.commons.SessionVariable;
import vfh.httpInterface.commons.StringUtil;
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.commons.enumeration.entity.PortraitSize;
import vfh.httpInterface.commons.enumeration.entity.ResourceType;
import vfh.httpInterface.commons.valid.annotation.MapValid;
import vfh.httpInterface.dao.account.GroupDao;
import vfh.httpInterface.dao.account.ResourceDao;
import vfh.httpInterface.dao.account.UserDao;
import vfh.httpInterface.dao.notice.NoticeDao;
import vfh.httpInterface.service.ServiceException;

import com.google.common.collect.Lists;

/**
 * 公告业务逻辑
 *
 * @author maurice
 */
@Service
@Transactional
@SuppressWarnings({"SpringJavaAutowiringInspection", "ResultOfMethodCallIgnored"})
public class NoticeService {
	// 本地
//	    public static final String DEFAULT_USER_UPLOAD_PORTRAIT_PATH = "./src/main/webapp/resource/upload_buildings/news/" ;
//	    public static final String DEFAULT_USER_UPLOAD_PORTRAIT_PATH2 =  "/resource/upload_buildings/news/" ;
	  
		//正式环境
		public static final String DEFAULT_USER_UPLOAD_PORTRAIT_PATH = "/vfh/apache-tomcat-7.0.67/webapps/management/resource/upload_buildings/news/" ;
		public static final String DEFAULT_USER_UPLOAD_PORTRAIT_PATH2 = "/resource/upload_buildings/news/" ;
    @Autowired
    private NoticeDao noticeDao;
    Map<String, Object> returnMap=new HashMap<String, Object>();
 
    //----------------------------------- 公告管理 ----------------------------------------//

    /**
     * 获取单条公告（前台用）
     *
     * @param id 用户主键 ID
     *
     * @return 用户实体 Map
     */
    public Map<String, Object> getNotice(Long id) {
        return noticeDao.getNoticeByID(id);
    }

    /**
     * 新增公告
     *
     * @param entity 用户实体 Map
     * @param groupIds 关联的组主键 ID 集合
     */
    public void insertNotice(@MapValid("insert-notice") Map<String, Object> entity) {

        noticeDao.insert(entity);
    }

    /**
     * 修改公告
     *
     * @param entity 用户实体 Map
     */

    public void upNoticeByID(@MapValid("update-notice")Map<String, Object> entity) {
        noticeDao.upNoticeByID(entity);


    }
    /**
     * 单条查询
     *
     * @param entity 用户实体 Map
     */

    public void getbyId(@MapValid("update-notice")Map<String, Object> entity) {

    	Long id = VariableUtils.typeCast(entity.get("id"), Long.class);

        noticeDao.getByID(entity);


    }
 



    /**
     * 删除公告
     *
     * @param entity 用户实体 Map
     */
    public void deleteNotice(Map<String,Object> entity) {


    	Long Id = VariableUtils.typeCast(entity.get("Id"),Long.class);
    	
        noticeDao.delNoticeByID(Id);
    }

    /**
     * 查询用户
     *
     * @param pageRequest 分页请求参数
     * @param filter 查询条件
     *
     * @return 用户实体 Map 的分页对象
     */
    public Page<Map<String, Object>> findNotices(PageRequest pageRequest, Map<String, Object> filter) {
        long total = noticeDao.count(filter);
        filter.putAll(pageRequest.getMap());
        List<Map<String, Object>> content = findNotices(filter);
        return new Page<Map<String, Object>>(pageRequest, content, total);
    }
    
    
    /**
     * 查询用户
     *
     * @param filter 查询条件
     *
     * @return 用户实体 Map 集合
     */
    public List<Map<String, Object>> findNotices(Map<String, Object> filter) {
        return noticeDao.find(filter);
    }
    
    
	/**
	 * TODO 查询公告列表
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param params
	 * @return
	 * @create 2016年2月1日
	 */
	public Map<String, Object> findNoticeList(Map<String, Object> filter){
		returnMap.clear();
		Map<String, Object> pagedata=new HashMap<String, Object>();

		//输入参数当前页，每页记录数，城市id
		List<Map<String, Object>> noticeList5=noticeDao.findnoticeListFive(filter);
		if(StringUtil.isNotEmptyList(noticeList5)){
			
			pagedata.put("list", noticeList5);
    		returnMap.put("returnCode", "000000");
    		returnMap.put("data",pagedata);
    		returnMap.put("returnMsg", "获取楼盘列表！");
    	}else{
    		returnMap.put("returnCode", "1111");
    		returnMap.put("returnMsg", "获取楼盘列表失败！");
    	}
		
		
	    return returnMap;
	}
   
	
	
	public Map<String, Object> getNoticeforWeb(Long Id){
		returnMap.clear();
		
		Map<String, Object> noticeDetail=noticeDao.getNoticeByID(Id);
		if(StringUtil.isNotEmptyMap(noticeDetail)){
    		returnMap.put("returnCode", "000000");
    		returnMap.put("data",noticeDetail);
    		returnMap.put("returnMsg", "获取楼盘信息成功！");
    	}else{
    		returnMap.put("returnCode", "1111");
    		returnMap.put("returnMsg", "获取楼盘信息失败！");
    	}
	    return returnMap;
	}
	//添加文章图片
	public void addNoticePic (HttpServletRequest request) throws IOException {


        Long id = Long.parseLong( request.getParameter("id"));
		MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//request强制转换注意
    	MultipartFile file = mRequest.getFile("image");
        if (!file.isEmpty()) {
        	
          String fileName = file.getOriginalFilename();
          if (StringUtils.isNotBlank(fileName)) {// 因为最后一个添加的控件没有上传相应的内容

            String fileType = fileName.substring(fileName.lastIndexOf("."));
            // 使用字符替换图片名称，防止乱码
            String tempName = "fm" + fileType;

            File uploadfile = new File(DEFAULT_USER_UPLOAD_PORTRAIT_PATH +id +"/"+ tempName);// 上传地址
            
            if (!uploadfile.exists() || !uploadfile.isDirectory()) {
            
            	uploadfile.mkdirs();
            	
            }
          
            file.transferTo(uploadfile);// 开始上传
            
            
//            String portraitPath = DEFAULT_USER_UPLOAD_PORTRAIT_PATH +id+ File.separator + "huxingtu" + File.separator+ shi + File.separator;
//            String originalPicPath = uploadfile.getAbsolutePath();
//            
//            scaleImage(originalPicPath, portraitPath, PortraitSize.MIDDLE);
//            scaleImage(originalPicPath, portraitPath, PortraitSize.SMALL);
          }}
	}
}
