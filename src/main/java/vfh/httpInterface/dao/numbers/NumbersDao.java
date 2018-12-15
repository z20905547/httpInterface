package vfh.httpInterface.dao.numbers;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 公告管理
 * 
 * @author MUJX
 */
public interface NumbersDao {
    
    /**
     * 查询单调公告
     *
     * @param id 主键 ID
     *
     * @return 组实体 Map
     */
    public Map<String, Object> getNumByID(@Param("Id")Long Id);
    
    /**
     * 新增订单记录
     *
     * @param entity 组实体 Map
     */
    public Integer insert(@Param("entity")Map<String,Object> entity);
    
    //新增中间表数据
    public void insertMiddle(@Param("entity")Map<String,Object> entity);
    /**
     * 统计订单记录
     *
     * @param filter 查询条件
     *
     * @return 统计用户总数
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
