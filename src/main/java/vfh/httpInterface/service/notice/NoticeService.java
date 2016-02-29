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
   
}
