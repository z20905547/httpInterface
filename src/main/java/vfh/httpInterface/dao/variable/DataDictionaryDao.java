package vfh.httpInterface.dao.variable;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 数据字典数据访问
 *
 * @author maurice
 */
public interface DataDictionaryDao {

    /**
     * 获取数据字典
     *
     * @param id 数据字典主键 ID
     *
     * @return 数据字典实体 Map
     */
    public Map<String, Object> get(@Param("id")Long id);

    /**
     * 获取数据字典
     *
     * @param code 字典类别代码
     *
     * @return 数据字典实体 Map
     */
    public List<Map<String, Object>> getByCode(@Param("code")String code);

    /**
     * 新增数据字典
     *
     * @param entity 数据字典实体 Map
     */
    public void insert(@Param("entity")Map<String, Object> entity);

    /**
     * 更新数据字典
     *
     * @param entity 数据字典实体 Map
     */
    public void update(@Param("entity")Map<String, Object> entity);

    /**
     * 删除数据字典
     *
     * @param id 数据字典主键 ID
     */
    public void delete(@Param("id")Long id);

    /**
     * 查询数据字典
     *
     * @param filter 查询条件
     *
     * @return 数据字典实体 Map 集合
     */
    public List<Map<String, Object>> find(@Param("filter")Map<String, Object> filter);

    /**
     * 统计数据字典
     *
     * @param filter 查询条件
     *
     * @return 统计数据字典总数
     */
    public long count(@Param("filter")Map<String, Object> filter);
}
