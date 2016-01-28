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

    
    public void hxtInsert(@Param("entity")Map<String,Object> entity);

}
