package vfh.httpInterface.dao.account;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 组数据访问
 * 
 * @author maurice
 */
public interface GroupDao {
    
    /**
     * 获取组
     *
     * @param id 组主键 ID
     *
     * @return 组实体 Map
     */
    public Map<String, Object> get(@Param("id")Long id);

    /**
     * 获取用户所在的组
     *
     * @param userId 用户主键 ID
     *
     * @return 组实体 Map 集合
     */
    List<Map<String,Object>> getUserGroups(@Param("userId")Long userId);

    /**
     * 删除组
     *
     * @param id 组主键 ID
     */
    public void delete(@Param("id")Long id);

    /**
     * 删除组与资源的关联
     *
     * @param id 组主键 ID
     */
    public void deleteResourceAssociation(@Param("id")Long id);

    /**
     * 删除组与用户的关联
     *
     * @param id 组主键 ID
     */
    public void deleteUserAssociation(@Param("id")Long id);

    /**
     * 更新组
     *
     * @param entity 组实体 Map
     */
    public void update(@Param("entity")Map<String,Object> entity);

    /**
     * 新增组
     *
     * @param entity 组实体 Map
     */
    public void insert(@Param("entity")Map<String,Object> entity);

    /**
     * 新增组与资源的关联
     *
     * @param id 组主键 ID
     * @param resourceIds 关联的组主键 ID 集合
     */
    public void insertResourceAssociation(@Param("id")Long id, @Param("resourceIds")List<Long> resourceIds);

    /**
     * 新增组与用户的关联
     *
     * @param id 组主键 ID
     * @param userIds 关联的组主键 ID 集合
     */
    public void insertUserAssociation(@Param("id")Long id, @Param("userIds")List<Long> userIds);

    /**
     * 统计组
     *
     * @param filter 查询条件
     *
     * @return 统计组总数
     */
    public long count(@Param("filter")Map<String,Object> filter);

    /**
     * 查询组
     *
     * @param filter 查询条件
     *
     * @return 组实体 Map集合
     */
    public List<Map<String, Object>> find(@Param("filter")Map<String, Object> filter);
}
