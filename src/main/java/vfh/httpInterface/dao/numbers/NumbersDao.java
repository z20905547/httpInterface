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
     * 统计公客
     *
     * @param filter 查询条件
     *
     * @return 统计用户总数
     */
    public long count_gk(@Param("filter")Map<String,Object> filter);
    /**
     * 查询列表
     *
     * @param entity 组实体 Map
     */
    public List<Map<String, Object>> find(@Param("filter")Map<String, Object> filter);
    
    
    /**
     * 查询公客
     *
     * @param entity 组实体 Map
     */
    public List<Map<String, Object>> find_gk(@Param("filter")Map<String, Object> filter);
    
    public void update(@Param("entity")Map<String,Object> entity);
    public void putNumber(@Param("entity")Map<String,Object> entity);
    public void get(@Param("entity")Map<String,Object> entity);
    public void skTogk(@Param("entity")Map<String,Object> entity);
    public void skTogkByCID(@Param("entity")Map<String,Object> entity);
    //获取全部到期的中间表的数据
    public List<Map<String, Object>> find_gkAll(@Param("entity")Map<String,Object> entity);
    //根据user_id 获取某一个员工到期的客户中间表的数据
    public List<Map<String, Object>> find_gkByudi(@Param("entity")Map<String,Object> entity);
    /**
     * 查询列表 专供管理员查询 staff==“admin”
     *
     * @param entity 组实体 Map
     */
    public List<Map<String, Object>> findall(@Param("filter")Map<String, Object> filter);

}
