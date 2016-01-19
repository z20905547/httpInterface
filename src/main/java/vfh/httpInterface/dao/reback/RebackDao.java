package vfh.httpInterface.dao.reback;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 公告管理
 * 
 * @author MUJX
 */
public interface RebackDao {
    
    /**
     * 单条查询
     *
     * @param id 主键 ID
     *
     * @return 组实体 Map
     */
    public Map<String, Object> getOneByID(@Param("Id")Long Id);
    
    /**
     * 新增记录
     *
     * @param entity 组实体 Map
     */
    public void insert(@Param("entity")Map<String,Object> entity);
    /**
     * 统计订单记录
     *
     * @param filter 查询条件
     *
     * @return 统计总数
     */
    public long count(@Param("filter")Map<String,Object> filter);
    
    
    /**
     * 查询列表
     *
     * @param entity 组实体 Map
     */
    public List<Map<String, Object>> find(@Param("filter")Map<String, Object> filter);
    
    /**
     * 查询列表 专供管理员查询 staff==“admin”
     *
     * @param entity 组实体 Map
     */
    public List<Map<String, Object>> findall(@Param("filter")Map<String, Object> filter);

}
