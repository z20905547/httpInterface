package vfh.httpInterface.dao.order;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 公告管理
 * 
 * @author MUJX
 */
public interface OrderDao {
    

    public void insert(@Param("entity")Map<String,Object> entity);
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
    


}
