package vfh.httpInterface.dao.area;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * TODO
 * @author harry
 * <b> 有问题请联系qq:359705093</b>
 * @create 2016年1月30日
 */
public interface AreaMapper {
	public Map<String, Object> get(@Param("id")Long id);
	public void delete(@Param("id")Long id);
	public void update(@Param("entity")Map<String,Object> entity);
	public void insert(@Param("entity")Map<String,Object> entity);
	public long count(@Param("filter")Map<String,Object> filter);
	public List<Map<String, Object>> find(@Param("filter")Map<String, Object> filter);
	public List<Map<String, Object>> findByParentId(@Param("parentId")Long parentId);
}
