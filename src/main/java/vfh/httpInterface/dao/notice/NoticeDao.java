package vfh.httpInterface.dao.notice;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 公告管理
 * 
 * @author MUJX
 */
public interface NoticeDao {
    
    /**
     * 查询单调公告
     *
     * @param id 主键 ID
     *
     * @return 组实体 Map
     */
    public Map<String, Object> getNoticeByID(@Param("Id")Long Id);


    /**
     * 删除公告
     *
     * @param id 组主键 ID
     */
    public void delNoticeByID(@Param("Id")Long Id);


    /**
     * 更新组
     *
     * @param entity 组实体 Map
     */
    public void upNoticeByID(@Param("entity")Map<String,Object> entity);

    /**
     * 更新组
     *
     * @param entity 组实体 Map
     */
    public void getByID(@Param("entity")Map<String,Object> entity);
    
    /**
     * 新增组
     *
     * @param entity 组实体 Map
     */
    public void insert(@Param("entity")Map<String,Object> entity);
    /**
     * 统计用户
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
