package vfh.httpInterface.dao.account;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * 用户数据访问
 *
 * @author maurice
 */
public interface UserDao {

    /**
     * 获取用户
     *
     * @param id 用户主键 ID
     *
     * @return 用户实体 Map
     */
    public Map<String, Object> get(@Param("id")Long id);

    /**
     * 获取用户
     *
     * @param usernameOrEmail 用户登录帐号或电子实体
     *
     * @return 用户实体 Map
     */
    public Map<String, Object> getByUsernameOrEmail(@Param("usernameOrEmail")String usernameOrEmail);

    /**
     * 删除用户
     *
     * @param id 用户主键 ID
     */
    public void delete(@Param("id")Long id);

    /**
     * 删除用户与组的关联
     *
     * @param id 用户主键 ID
     */
    public void deleteGroupAssociation(@Param("id")Long id);

    /**
     * 更新用户
     *
     * @param entity 用户实体 Map
     */
    public void update(@Param("entity")Map<String,Object> entity);

    /**
     * 更新密码
     *
     * @param id 用户主键 ID
     * @param password 密码
     */
    public void updatePassword(@Param("id")Long id, @Param("password")String password);

    /**
     * 新增用户
     *
     * @param entity 用户实体 Map
     */
    public void insert(@Param("entity")Map<String,Object> entity);

    /**
     * 新增用户与组的关联
     *
     * @param id 用户主键 ID
     * @param groupIds 关联的组主键 ID 集合
     */
    public void insertGroupAssociation(@Param("id")Long id, @Param("groupIds")List<Long> groupIds);

    /**
     * 统计用户
     *
     * @param filter 查询条件
     *
     * @return 统计用户总数
     */
    public long count(@Param("filter")Map<String,Object> filter);

    /**
     * 查询用户
     *
     * @param filter 查询条件
     *
     * @return 用户实体 Map集合
     */
    public List<Map<String, Object>> find(@Param("filter")Map<String, Object> filter);
}
