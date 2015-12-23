package vfh.httpInterface.dao.variable;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 字典类别数据访问
 *
 * @author maurice
 */
public interface DictionaryCategoryDao {

    /**
     * 获取字典类别
     *
     * @param id 字典类别主键ID
     *
     * @return 字典类别 Map 实体
     */
    public Map<String, Object> get(@Param("id")Long id);

    /**
     * 获取所有字典类别
     *
     * @param ignore 忽略的 ID 值
     *
     * @return 字典类别 Map 实体集合
     */
    public List<Map<String, Object>> getAll(@Param("ignore")Long... ignore);

    /**
     * 新增字典类别
     *
     * @param entity 字典类别 Map 实体
     */
    public void insert(@Param("entity")Map<String, Object> entity);

    /**
     * 更新字典类别
     *
     * @param entity 字典类别 Map 实体
     */
    public void update(@Param("entity")Map<String, Object> entity);

    /**
     * 删除字典类别
     *
     * @param id 字典类别主键 ID
     */
    public void delete(@Param("id")Long id);

    /**
     * 删除字典类别关联的数据字典
     *
     * @param id 字典类别主键 ID
     */
    public void deleteDataDictionaryAssociation(@Param("id")Long id);

    /**
     * 查询字典类别
     *
     * @param filter 查询条件
     *
     * @return 字典类别实体 Map 集合
     */
    public List<Map<String, Object>> find(@Param("filter")Map<String, Object> filter);

    /**
     * 统计字典类别
     *
     * @param filter 查询条件
     *
     * @return 统计字典类别总数
     */
    public long count(@Param("filter")Map<String, Object> filter);

}
