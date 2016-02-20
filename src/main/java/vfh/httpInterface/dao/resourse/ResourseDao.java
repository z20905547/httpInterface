package vfh.httpInterface.dao.resourse;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * 用户数据访问
 *
 * @author maurice
 */
public interface ResourseDao {

    
    public void Insert(@Param("entity")Map<String,Object> entity); 
    /**
     * 获取户型图路径
     *
     * @param id 用户主键 ID
     *
     * @return 用户实体 Map
     */
    public Map<String, Object> get(@Param("id")Long id);
}
