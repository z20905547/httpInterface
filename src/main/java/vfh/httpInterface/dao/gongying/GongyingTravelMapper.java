package vfh.httpInterface.dao.gongying;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


public interface GongyingTravelMapper {
	public Map<String, Object> get(@Param("id")Long id);
	public void delete(@Param("id")Long id);
	public void update(@Param("entity")Map<String,Object> entity);
	public void updateActiveData(@Param("entity")Map<String,Object> entity);	
	public void insert(@Param("entity")Map<String,Object> entity);
	public long count(@Param("filter")Map<String,Object> filter);
	public List<Map<String, Object>> find(@Param("filter")Map<String, Object> filter);
	public List<Map<String, Object>> find3(@Param("filter")Map<String, Object> filter);
	public List<Map<String, Object>> find4(@Param("filter")Map<String, Object> filter);
	public Map<String, Object> get2(@Param("id")Long id);
	public List<Map<String, Object>>  get3(@Param("travel_id")Long travel_id);
	public void delete2(@Param("id")Long id);
	public void update2(@Param("entity")Map<String,Object> entity);
	public void updateActiveData2(@Param("entity")Map<String,Object> entity);	
	public void insert2(@Param("entity")Map<String,Object> entity);
	public long count2(@Param("filter")Map<String,Object> filter);
	public List<Map<String, Object>> find2(@Param("filter")Map<String, Object> filter);
}