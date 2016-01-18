package vfh.httpInterface.service.numbers;

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
import vfh.httpInterface.dao.numbers.NumbersDao;
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
public class NumbersService {

    @Autowired
    private NumbersDao NumbersDao;
    Map<String, Object> returnMap=new HashMap<String, Object>();
 
    //----------------------------------- 订单管理 ----------------------------------------//


    /**
     * 新增公告
     *
     * @param entity 用户实体 Map
     * @param groupIds 关联的组主键 ID 集合
     */
    public void insertNumber(@MapValid("insert-numbers") Map<String, Object> entity) {

    	NumbersDao.insert(entity);
    }

    /**
     * 查询用户
     *
     * @param pageRequest 分页请求参数
     * @param filter 查询条件
     *
     * @return 用户实体 Map 的分页对象
     */
    public Page<Map<String, Object>> findnumbers(PageRequest pageRequest, Map<String, Object> filter) {
    	
    	
        long total = NumbersDao.count(filter);
        filter.putAll(pageRequest.getMap());
        List<Map<String, Object>> content = findnumbers(filter);
        return new Page<Map<String, Object>>(pageRequest, content, total);
    }
    
    
    /**
     * 查询用户
     *
     * @param filter 查询条件
     *
     * @return 用户实体 Map 集合
     */
    public List<Map<String, Object>> findnumbers(Map<String, Object> filter) {
    	Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();
    	String staff = (String) user.get("username");
    	if(staff != null && staff.equals("admin")) {
    		 return NumbersDao.findall(filter);
    	} else
    		filter.put("staff", staff);
        return NumbersDao.find(filter);
    }
   
    
    /**
     * 获取记录
     *
     * @param id 用户主键 ID
     *
     * @return 用户实体 Map
     */
    public Map<String, Object> getNumByID(Long id) {
        return NumbersDao.getNumByID(id);
    }
}
