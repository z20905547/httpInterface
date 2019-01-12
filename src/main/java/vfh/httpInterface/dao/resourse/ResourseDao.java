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
    /**
     * TODO
     * @author harry
     * <b> 有问题请联系qq:359705093</b>
     * @param filter
     * @return
     * @create 2016年3月1日
     */
    public long count(@Param("filter")Map<String,Object> filter);
	/**
	 * TODO
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param filter
	 * @return
	 * @create 2016年3月1日
	 */
	public List<Map<String, Object>> find(@Param("filter")Map<String, Object> filter);
	
    public void delete(@Param("id")Long id);
}
