package vfh.httpInterface.dao.account;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 资源数据访问
 *
 * @author maurice
 */
public interface ResourceDao {

    /**
     * 获取资源
     *
     * @param id 资源主键 ID
     *
     * @return 资源实体 Map
     */
    public Map<String, Object> get(@Param("id")Long id);

    /**
     * 获取所有资源
     *
     * @param ignore 忽略的 ID 值
     *
     * @return 资源实体集合
     */
    public List<Map<String,Object>> getAll(@Param("ignore")Long... ignore);

    /**
     * 获取相关联的子资源
     *
     * @param id 资源主键 ID
     *
     * @return 资源实体 Map 集合
     */
    public List<Map<String, Object>> getChildren(@Param("id")Long id);

    /**
     * 获取用户资源
     *
     * @param userId 用户主键 ID
     *
     * @return 资源实体 Map 集合
     */
    public List<Map<String, Object>> getUserResources(@Param("userId")Long userId);

    /**
     * 获取组资源
     *
     * @param groupId 组主键 ID
     *
     * @return 资源实体 Map 集合
     */
    public List<Map<String, Object>> getGroupResources(@Param("groupId")Long groupId);

    /**
     * 删除资源
     *
     * @param id 资源主键 ID
     */
    public void delete(@Param("id")Long id);

    /**
     * 删除资源与组的关联
     *
     * @param id 资源主键 ID
     */
    public void deleteGroupAssociation(@Param("id")Long id);

    /**
     * 更新资源
     *
     * @param entity 资源实体 Map
     */
    public void update(@Param("entity")Map<String,Object> entity);

    /**
     * 新增资源
     *
     * @param entity 资源实体 Map
     */
    public void insert(@Param("entity")Map<String,Object> entity);

    /**
     * 统计资源数量
     *
     * @return 资源总数量
     */
    public long count();
}
