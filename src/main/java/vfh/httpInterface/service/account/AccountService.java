package vfh.httpInterface.service.account;

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
import vfh.httpInterface.service.ServiceException;

import com.google.common.collect.Lists;

/**
 * 账户业务逻辑
 *
 * @author maurice
 */
@Service
@Transactional
@SuppressWarnings({"SpringJavaAutowiringInspection", "ResultOfMethodCallIgnored"})
public class AccountService implements InitializingBean{

    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private ResourceDao resourceDao;
    Map<String, Object> returnMap=new HashMap<String, Object>();
    /**
     * 默认的用户上传头像的文件夹路径
     */
    public static final String DEFAULT_USER_UPLOAD_PORTRAIT_PATH = "." + File.separator + "upload_portrait" + File.separator;

    
    /**
     * 同时上传一份至前台页面可以显示的图片的文件夹路径  本地路径 调试通过后 要将本地路径换成正式环境路径
     */
   // public static final String NEW_USER_UPLOAD_PORTRAIT_PATH = "./src/main/webapp/resource/upload_buildings/user/" ;
   
  //正式环境 路径
  	 public static final String NEW_USER_UPLOAD_PORTRAIT_PATH = "/vfh/apache-tomcat-7.0.67/webapps/management/resource/upload_buildings/user/" ;
  	 
    
    
    //----------------------------------- 用户管理 ----------------------------------------//

    /**
     * 获取用户
     *
     * @param id 用户主键 ID
     *
     * @return 用户实体 Map
     */
    public Map<String, Object> getUser(Long id) {
        return userDao.get(id);
    }

    /**
     * 获取当前用户头像
     *
     * @param portraitSize 头像尺寸枚举
     *
     * @throws IOException
     *
     * @return 头像二进制 byte 数组
     *
     */
    public byte[] getCurrentUserPortrait(PortraitSize portraitSize) throws IOException {
        Map<String,Object> entity = SessionVariable.getCurrentSessionVariable().getUser();

        String path = DEFAULT_USER_UPLOAD_PORTRAIT_PATH + entity.get("id") + File.separator + portraitSize.getName();

        File f = new File(path);

        if (!f.exists()) {
            return null;
        }

        return FileUtils.readFileToByteArray(new File(path));
    }

    /**
     * 新增用户
     *
     * @param entity 用户实体 Map
     * @param groupIds 关联的组主键 ID 集合
     */
    public void insertUser(@MapValid("insert-user") Map<String, Object> entity, List<Long> groupIds) {

        if (!isUsernameUnique(entity.get("username").toString())) {
            throw new ServiceException("登录账户已存在");
        }

        Md5Hash md5Hash = new Md5Hash(entity.get("password"));
        entity.put("password", md5Hash.toHex());

        userDao.insert(entity);

        if (CollectionUtils.isNotEmpty(groupIds)) {
            Long id = VariableUtils.typeCast(entity.get("id"), Long.class);
            userDao.insertGroupAssociation(id, groupIds);
        }
    }

    /**
     * 更新用户
     *
     * @param entity 用户实体 Map
     * @param groupIds 关联的组主键 ID 集合，如果为 null 或 size 等于 0 表示删除所有关联
     */
    @CacheEvict(value="shiroAuthenticationCache",key="#entity.get('username')")
    public void updateUser(@MapValid("update-user")Map<String, Object> entity, List<Long> groupIds) {

        Long id = VariableUtils.typeCast(entity.get("id"), Long.class);

        userDao.update(entity);

        if (groupIds != null) {
            userDao.deleteGroupAssociation(id);
            if (!groupIds.isEmpty()) {
                userDao.insertGroupAssociation(id, groupIds);
            }
        }

    }

    /**
     * 更新用户密码
     *
     * @param entity 用户实体 Map
     * @param oldPassword 当前密码
     * @param newPassword 新imia
     */
    @CacheEvict(value="shiroAuthenticationCache",key="#entity.get('username')")
    public void updateUserPassword(Map<String, Object> entity, String oldPassword, String newPassword) {

        Md5Hash md5Hash = new Md5Hash(oldPassword);

        if (!StringUtils.equals(entity.get("password").toString(),md5Hash.toHex())) {
            throw new ServiceException("当前密码不正确");
        }

        if (StringUtils.isEmpty(newPassword)) {
            throw new ServiceException("新密码不能为空");
        }

        md5Hash = new Md5Hash(newPassword);
        Long id = VariableUtils.typeCast(entity.get("id"), Long.class);
        userDao.updatePassword(id, md5Hash.toHex());
    }

    /**
     * 更新用户头像
     *
     * @param is 头像输入流
     *
     * @throws IOException
     */
    public void updateCurrentPortrait(InputStream is) throws IOException {
        Map<String, Object> entity = SessionVariable.getCurrentSessionVariable().getUser();

        File file = new File(DEFAULT_USER_UPLOAD_PORTRAIT_PATH + entity.get("id") + File.separator);

        if (!file.exists() || !file.isDirectory()) {
            file.deleteOnExit();
            file.mkdirs();
        }

        String portraitPath = file.getAbsolutePath() + File.separator;
        String originalPicPath = portraitPath + PortraitSize.BIG.getName();

     //   IOUtils.copy(is, new FileOutputStream(originalPicPath));

      //  scaleImage(originalPicPath, portraitPath, PortraitSize.MIDDLE);
      //  scaleImage(originalPicPath, portraitPath, PortraitSize.SMALL);
        
        //再传一份至前台能显示的文件夹
        String	originalPicPath2="";
        File uploadfile = new File(NEW_USER_UPLOAD_PORTRAIT_PATH +entity.get("id")  + File.separator);// 上传地址
        
        if (!uploadfile.exists() || !uploadfile.isDirectory()) {
            
        	uploadfile.mkdirs();
        	

			
        }
        originalPicPath2 = NEW_USER_UPLOAD_PORTRAIT_PATH +entity.get("id")  +"/"+ "tx.jpg";
        IOUtils.copy(is, new FileOutputStream(originalPicPath2));
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

    /**
     * 删除用户
     *
     * @param ids 用户主键 ID 集合
     */
    public void deleteUsers(List<Long> ids) {
        for(Long id : ids) {
            Map<String, Object> entity = getUser(id);
            if (MapUtils.isNotEmpty(entity)) {
                deleteUser(entity);
            }
        }
    }

    /**
     * 删除用户
     *
     * @param entity 用户实体 Map
     */
    @CacheEvict(value="shiroAuthenticationCache",key="#entity.get('username')")
    public void deleteUser(Map<String,Object> entity) {

        if ("admin".equals(entity.get("username"))) {
            throw new ServiceException("这是管理员帐号，不允许删除");
        }

        Long id = VariableUtils.typeCast(entity.get("id"),Long.class);
        userDao.deleteGroupAssociation(id);
        userDao.delete(id);
    }

    /**
     * 获取用户
     *
     * @param usernameOrEmail 登录帐号或电子邮件
     *
     * @return 用户实体 Map
     */
    public Map<String, Object> getUserByUsernameOrEmail(String usernameOrEmail) {
        return userDao.getByUsernameOrEmail(usernameOrEmail);
    }

    /**
     * 判断用户登录帐号是否唯一
     *
     * @param username 登录帐号
     *
     * @return true 表示唯一，否则 false
     */
    public boolean isUsernameUnique(String username) {
        return getUserByUsernameOrEmail(username) == null;
    }

    /**
     * 判断用户电子邮件是否唯一
     *
     * @param email 电子邮件
     *
     * @return true 表示唯一，否则 false
     */
    public boolean isEmailUnique(String email) {
        return getUserByUsernameOrEmail(email) == null;
    }

    /**
     * 查询用户
     *
     * @param filter 查询条件
     *
     * @return 用户实体 Map 集合
     */
    public List<Map<String, Object>> findUsers(Map<String, Object> filter) {
        return userDao.find(filter);
    }

    /**
     * 查询用户
     *
     * @param pageRequest 分页请求参数
     * @param filter 查询条件
     *
     * @return 用户实体 Map 的分页对象
     */
    public Page<Map<String, Object>> findUsers(PageRequest pageRequest, Map<String, Object> filter) {
        long total = userDao.count(filter);
        filter.putAll(pageRequest.getMap());
        List<Map<String, Object>> content = findUsers(filter);
        return new Page<Map<String, Object>>(pageRequest, content, total);
    }

    //----------------------------------- 组管理 ----------------------------------------//

    /**
     * 获取组
     *
     * @param id 组主键 ID
     *
     * @return 组实体 Map
     */
    public Map<String, Object> getGroup(Long id){
        return groupDao.get(id);
    }

    /**
     * 获取用户所在的组
     *
     * @param userId 用户主键 ID
     *
     * @return 组实体 Map 集合
     */
    public List<Map<String, Object>> getUserGroups(Long userId) {
        return groupDao.getUserGroups(userId);
    }

    /**
     * 删除组
     *
     * @param ids 组主键 ID 集合
     */
    @CacheEvict(value="shiroAuthorizationCache",allEntries=true)
    public void deleteGroups(List<Long> ids) {
        for (Long id : ids) {
            groupDao.deleteResourceAssociation(id);
            groupDao.deleteUserAssociation(id);
            groupDao.delete(id);
        }
    }

    /**
     * 保存组
     *
     * @param entity 组实体 Map
     * @param resourceIds 关联的资源主键 ID 集合，如果为 null 或 size 等于 0 表示删除所有关联
     */
    @CacheEvict(value="shiroAuthorizationCache",allEntries=true)
    public void saveGroup(@MapValid("group")Map<String, Object> entity, List<Long> resourceIds) {
        if (entity.containsKey("id")) {
            Long id = VariableUtils.typeCast(entity.get("id"), Long.class);

            groupDao.update(entity);

            if (resourceIds != null) {
                groupDao.deleteResourceAssociation(id);
                if (!resourceIds.isEmpty()) {
                    groupDao.insertResourceAssociation(id, resourceIds);
                }
            }
        } else {

            groupDao.insert(entity);
            Long id = VariableUtils.typeCast(entity.get("id"), Long.class);

            if (CollectionUtils.isNotEmpty(resourceIds)) {
                groupDao.insertResourceAssociation(id, resourceIds);
            }
        }
    }

    /**
     * 查询组
     *
     * @param filter 查询条件
     *
     * @return 组实体 Map 集合
     */
    public List<Map<String, Object>> findGroups(Map<String, Object> filter) {
        return groupDao.find(filter);
    }

    /**
     * 查询组
     *
     * @param filter 查询条件
     *
     * @return 组实体 Map 的分页对象
     */
    public Page<Map<String, Object>> findGroups(PageRequest pageRequest, Map<String, Object> filter) {
        long total = groupDao.count(filter);
        filter.putAll(pageRequest.getMap());
        List<Map<String, Object>> content = findGroups(filter);
        return new Page<Map<String, Object>>(pageRequest, content, total);
    }

    //----------------------------------- 资源管理 ----------------------------------------//

    /**
     * 获取资源
     *
     * @param id 资源主键 ID
     *
     * @return 资源实体 Map
     */
    public Map<String, Object> getResource(Long id) {
        return resourceDao.get(id);
    }

    /**
     * 获取所有资源
     *
     * @return 资源实体 Map 集合
     */
    public List<Map<String, Object>> getResources(Long... ignore) {
        return resourceDao.getAll(ignore);
    }

    /**
     * 获取用户资源
     *
     * @param userId 用户主键 ID
     *
     * @return 资源实体 Map 集合
     */
    public List<Map<String, Object>> getUserResources(Long userId) {
        return resourceDao.getUserResources(userId);
    }

    /**
     * 获取组资源
     *
     * @param groupId 组主键 ID
     *
     * @return 资源实体 Map 集合
     */
    public List<Map<String, Object>> getGroupResources(Long groupId) {
        return resourceDao.getGroupResources(groupId);
    }

    /**
     * 删除资源
     *
     * @param ids 资源主键 ID 集合
     */
    @CacheEvict(value="shiroAuthorizationCache",allEntries=true)
    public void deleteResources(List<Long> ids) {
        for (Long id : ids) {
            deleteResource(id);
        }
    }

    /**
     * 删除资源
     *
     * @param id 资源主键 ID
     */
    private void deleteResource(Long id) {
        List<Map<String, Object>> children = resourceDao.getChildren(id);

        if (children.isEmpty()) {
            resourceDao.deleteGroupAssociation(id);
            resourceDao.delete(id);
            return ;
        }

        for (Map<String, Object> c : children) {
            Long temp = VariableUtils.typeCast(c.get("id"), Long.class);
            deleteResource(temp);
        }
    }

    /**
     * 保存资源
     *
     * @param entity 资源实体 Map
     */
    public void saveResource(@MapValid("resource")Map<String, Object> entity) {

        if (!entity.containsKey("sort") || "".equals(entity.get("sort"))) {
            entity.put("sort",resourceDao.count());
        }

        if (entity.containsKey("id")) {
            resourceDao.update(entity);
        } else {
            resourceDao.insert(entity);
        }
    }

    /**
     * 合并资源，要获取资源的父类通过 "parent" key 来获取，如果不存在 "parent" key 表示该资源 Map 为根节点，
     * 要获取资源的子类通过 "children" key 来获取
     *
     * @param resources 要合并的资源实体 Map 集合
     *
     * @return 合并好的树形资源实体 Map 集合
     */
    public List<Map<String, Object>> mergeResources(List<Map<String, Object>> resources) {
        return mergeResources(resources, null);
    }

    /**
     * 合并资源，要获取资源的父类通过 "parent" key 来获取，如果不存在 "parent" key 表示该资源 Map 为根节点，
     * 要获取资源的子类通过 "children" key 来获取
     *
     * @param resources 要合并的资源实体 Map 集合
     * @param ignoreType 要忽略资源类型
     *
     * @return 合并好的树形资源实体 Map 集合
     */
    public List<Map<String, Object>> mergeResources(List<Map<String, Object>> resources, ResourceType ignoreType) {

        List<Map<String, Object>> result = Lists.newArrayList();

        for (Map<String, Object> entity : resources) {

            Long parentId = VariableUtils.typeCast(entity.get("fk_parent_id"), Long.class);
            Integer type = VariableUtils.typeCast(entity.get("type"), Integer.class);

            if (parentId == null && (ignoreType == null || !ignoreType.getValue().equals(type))) {
                recursionRessource(entity, resources, ignoreType);
                result.add(entity);
            }

        }

        return result;
    }

    /**
     * 递归并合并资源到指定的父类
     *
     * @param parent 父类
     * @param resources 资源实体 Map 集合
     * @param ignoreType 要忽略不合并的资源类型
     */
    private void recursionRessource(Map<String, Object> parent,
                                    List<Map<String, Object>> resources,
                                    ResourceType ignoreType) {

        parent.put("children",Lists.newArrayList());

        for (Map<String, Object> entity: resources) {
            Long parentId = VariableUtils.typeCast(entity.get("fk_parent_id"), Long.class);

            if(parentId == null) {
                continue;
            }

            Integer type = VariableUtils.typeCast(entity.get("type"), Integer.class);
            Long id = VariableUtils.typeCast(parent.get("id"), Long.class);

            if((ignoreType == null || !ignoreType.getValue().equals(type)) && parentId.equals(id)) {
                recursionRessource(entity, resources, ignoreType);
                List<Map<String, Object>> children = VariableUtils.typeCast(parent.get("children"));
                entity.put("parent",parent);
                children.add(entity);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        File uploadPortrait = new File(DEFAULT_USER_UPLOAD_PORTRAIT_PATH);
        if (!uploadPortrait.exists()) {
            uploadPortrait.mkdirs();
        }
    }
    /***接口调用返回json**/
    public Map<String, Object> getUserInterface(Map params){
    	returnMap.clear();
    	if(StringUtil.isNotEmptyObject(params.get("userId"))){
    		long userId=Long.parseLong(params.get("userId").toString());
    		Map<String, Object> userInfo=userDao.get(userId);
    		if(StringUtil.isNotEmptyMap(userInfo)){
        		returnMap.put("returnCode", "000000");
        		returnMap.put("data",userInfo);
        		returnMap.put("returnMsg", "获取用户信息成功！");
        	}else{
        		returnMap.put("returnCode", "1111");
        		returnMap.put("returnMsg", "获取用户信息失败！");
        	}
    	}else{
    		returnMap.put("returnMsg","用户id不能为空!");
    		returnMap.put("statusCode","1111");
    	}
    	
    	
        return returnMap;
    }
}
