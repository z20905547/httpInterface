package vfh.httpInterface.dao.buildings;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * TODO 楼盘访问持久层
 * @author harry
 * <b> 有问题请联系qq:359705093</b>
 * @create 2016年1月12日
 */
public interface BuildingsDeveloperMapper {
	/**
	 * TODO 根据id获取楼盘基本信息
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param id
	 * @return
	 * @create 2016年1月12日
	 */
	public Map<String, Object> get(@Param("username")String username);
	
	public long count(@Param("filter")Map<String,Object> filter);
	/**
	 * TODO 查询符合条件楼盘列表
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param filter
	 * @return
	 * @create 2016年1月12日
	 */
	public List<Map<String, Object>> find(@Param("filter")Map<String, Object> filter);

	
}