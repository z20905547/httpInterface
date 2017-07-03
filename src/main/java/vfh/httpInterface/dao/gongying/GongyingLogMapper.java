package vfh.httpInterface.dao.gongying;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


public interface GongyingLogMapper {
	public Map<String, Object> get(@Param("customer_id")Long id);
	public void delete(@Param("id")Long id);
	public void update(@Param("entity")Map<String,Object> entity);
	public void updateActiveData(@Param("entity")Map<String,Object> entity);	
	public void insert(@Param("entity")Map<String,Object> entity);
	public long count(@Param("filter")Map<String,Object> filter);
	public List<Map<String, Object>> find(@Param("filter")Map<String, Object> filter);

}