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
public interface BuildingsMapper {
	/**
	 * TODO 根据id获取楼盘基本信息
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param id
	 * @return
	 * @create 2016年1月12日
	 */
	public Map<String, Object> get(@Param("id")Long id);
	/**
	 * TODO 根据id删除楼盘信息
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param id
	 * @create 2016年1月12日
	 */
	public void delete(@Param("id")Long id);
	/**
	 * TODO 更新楼盘信息
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param entity
	 * @create 2016年1月12日
	 */
	public void update(@Param("entity")Map<String,Object> entity);
	/**
	 * TODO 创建楼盘信息
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param entity
	 * @create 2016年1月12日
	 */
	public void insert(@Param("entity")Map<String,Object> entity);
	/**
	 * TODO 查询符合条件楼盘数量
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param filter
	 * @return
	 * @create 2016年1月12日
	 */
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
	
	/**
	 * TODO 多表联合查询所有符合条件的楼盘详细信息
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param filter
	 * @return
	 * @create 2016年2月2日
	 */
	public List<Map<String, Object>> findDetailList(@Param("filter")Map<String, Object> filter);
	
	/**
	 * TODO 多表联合查询所有符合条件的详细楼盘的数量
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param filter
	 * @return
	 * @create 2016年2月2日
	 */
	public long findDetailCount(@Param("filter")Map<String,Object> filter);
	/**
	 * TODO 多表查询楼盘信息
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param id
	 * @return
	 * @create 2016年2月25日
	 */
	public Map<String, Object> getMoreDetail(@Param("buildingsId")Long buildingsId,@Param("pid")Long pid);
	
}