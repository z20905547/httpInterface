package vfh.httpInterface.service.buildings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.commons.StringUtil;
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.commons.valid.annotation.MapValid;
import vfh.httpInterface.dao.buildings.BuildingsMapper;
import vfh.httpInterface.dao.buildings.BuildingsPriceMapper;
import vfh.httpInterface.service.ServiceException;

/**
 * TODO 楼盘业务逻辑
 * @author harry
 * <b> 有问题请联系qq:359705093</b>
 * @create 2016年1月11日
 */
@Service
@Transactional
public class BuildingsService {
	@Autowired
	private BuildingsMapper buildingsMapper;
	@Autowired
	private BuildingsPriceMapper buildingsPriceMapper;
	Map<String, Object> returnMap=new HashMap<String, Object>();
	/**
	 * TODO 添加楼盘
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param entity
	 * @create 2016年1月11日
	 */
	public void insertBuildings(@MapValid("insert-buildings") Map<String, Object> entity){
		entity.put("status", 0);
        if(entity.get("open_date").equals("")) entity.put("open_date", null);
        if(entity.get("deliver_date").equals("")) entity.put("deliver_date", null);
        if(entity.get("cityId").equals("--请选择--")) entity.put("cityId", null);
        if(entity.get("areaId").equals("--请选择--")) entity.put("areaId", null);
		buildingsMapper.insert(entity);
	}
	/**
	 * TODO 更新楼盘信息
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param entity
	 * @create 2016年1月12日
	 */
	public void updateBuildings(@MapValid("update-buildings")Map<String, Object> entity){
        if(entity.get("open_date").equals("")) entity.put("open_date", null);
        if(entity.get("deliver_date").equals("")) entity.put("deliver_date", null);
        if(entity.get("cityId").equals("--请选择--")) entity.put("cityId", null);
        if(entity.get("areaId").equals("--请选择--")) entity.put("areaId", null);
		buildingsMapper.update(entity);
	}
	/**
	 * TODO 获取用户列表
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param filter
	 * @return
	 * @create 2016年1月12日
	 */
	public List<Map<String, Object>> findBuildings(Map<String, Object> filter) {
		return buildingsMapper.find(filter);
	}
	/**
	 * TODO 分页获取楼盘列表
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param pageRequest
	 * @param filter
	 * @return
	 * @create 2016年1月12日
	 */
	public Page<Map<String, Object>> findBuildingsList(PageRequest pageRequest, Map<String, Object> filter) {
        long total = buildingsMapper.count(filter);
        
        filter.putAll(pageRequest.getMap());
        
        List<Map<String, Object>> content = findBuildings(filter);

        return new Page<Map<String, Object>>(pageRequest, content, total);
    }
	/**
	 * TODO 根据id查询楼盘
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param id
	 * @return
	 * @create 2016年1月12日
	 */
	public Map<String, Object> getBuildings(Long id) { 
		if (null ==buildingsMapper.get(id).get("city_id") || "".equals(buildingsMapper.get(id).get("city_id"))) {
			buildingsMapper.get(id).put("city_id", "wu");
		}
		if (null ==buildingsMapper.get(id).get("area_id") || "".equals(buildingsMapper.get(id).get("area_id"))) {
			buildingsMapper.get(id).put("area_id", "wu");
		}

        return buildingsMapper.get(id);
    }
	/**
	 * TODO 删除楼盘
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param entity
	 * @create 2016年1月12日
	 */
	public void deleteBuildings(Map<String,Object> entity) {

        if ("admin".equals(entity.get("username"))) {
            throw new ServiceException("这是管理员帐号，不允许删除");
        }

        Long id = VariableUtils.typeCast(entity.get("id"),Long.class);
        buildingsMapper.delete(id);
    }
	 /**
	 * TODO 批量删除楼盘
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param ids
	 * @create 2016年1月12日
	 */
	public void deleteBuildingsList(List<Long> ids) {
        for(Long id : ids) {
            Map<String, Object> entity = getBuildings(id);
            if (MapUtils.isNotEmpty(entity)) {
            	deleteBuildings(entity);
            }
        }
    }
	public Page<Map<String, Object>> findBuildingsPriceList(PageRequest pageRequest, Map<String, Object> filter,Model model) {
		Long buildingsId=null;
		if(StringUtil.isNotEmptyObject(pageRequest.getMap().get("buildingsId"))){
			buildingsId=Long.parseLong(pageRequest.getMap().get("buildingsId").toString());
		}
		
		Map buildings=buildingsMapper.get(buildingsId);
		model.addAttribute("buildingsName",buildings.get("buildings_name").toString());
		long total = buildingsPriceMapper.count(filter);
        filter.putAll(pageRequest.getMap());
        List<Map<String, Object>> content = buildingsPriceMapper.find(filter);
        return new Page<Map<String, Object>>(pageRequest, content, total);
    }
	/**
	 * TODO 查询楼盘详情列表
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param params
	 * @return
	 * @create 2016年2月1日
	 */
	public Map<String, Object> findDetailList(Map<String, Object> filter){
		returnMap.clear();
		Map<String, Object> pagedata=new HashMap<String, Object>();
		//查总条数
		if("undefined".equals(filter.get("buildings_name"))  || "请输入楼盘名称".equals(filter.get("buildings_name"))) filter.put("buildings_name", "");
		if("undefined".equals(filter.get("proId"))) filter.put("proId", "");
		if("undefined".equals(filter.get("city_id"))) filter.put("city_id", "");
		if("undefined".equals(filter.get("area_id"))) filter.put("area_id", "");
		
		if("111".equals(filter.get("city_id"))) {
			filter.put("city_id", "");
		}
		if("222".equals(filter.get("active_price"))) {
			filter.put("active_price1", "");
		}else if ("4000".equals(filter.get("active_price"))){
			filter.put("active_price1", "0");
			filter.put("active_price2", "4000");
		}else if ("6000".equals(filter.get("active_price"))){
			filter.put("active_price1", "4000");
			filter.put("active_price2", "6000");
		}
		else if ("8000".equals(filter.get("active_price"))){
			filter.put("active_price1", "6000");
			filter.put("active_price2", "8000");
		}else if ("10000".equals(filter.get("active_price"))){
			filter.put("active_price1", "8000");
			filter.put("active_price2", "10000");
		}else if ("11000".equals(filter.get("active_price"))){
			filter.put("active_price1", "10000");
			filter.put("active_price2", "50000");
		}
		if("333".equals(filter.get("acreage"))) {
			filter.put("acreage1", "");
		}else if ("50".equals(filter.get("acreage"))){
			filter.put("acreage1", "0");
			filter.put("acreage2", "50");
		}else if ("70".equals(filter.get("acreage"))){
			filter.put("acreage1", "50");
			filter.put("acreage2", "70");
		}else if ("90".equals(filter.get("acreage"))){
			filter.put("acreage1", "70");
			filter.put("acreage2", "90");
		}else if ("110".equals(filter.get("acreage"))){
			filter.put("acreage1", "90");
			filter.put("acreage2", "110");
		}else if ("150".equals(filter.get("acreage"))){
			filter.put("acreage1", "110");
			filter.put("acreage2", "150");
		}
		if("444".equals(filter.get("shi"))) {
			filter.put("shi", "");
		}
		

		
		long total = buildingsMapper.findDetailCount(filter);
		if(StringUtil.isNotEmptyObject(filter.get("first"))&&StringUtil.isNotEmptyObject(filter.get("last"))){
			filter.put("first", Integer.parseInt(filter.get("first").toString()));
			filter.put("last", Integer.parseInt(filter.get("last").toString()));
		}
		//输入参数当前页，每页记录数，城市id
		List<Map<String, Object>> buildingsDetailList=buildingsMapper.findDetailList(filter);
		if(StringUtil.isNotEmptyList(buildingsDetailList)){
			pagedata.put("total", total);
			pagedata.put("list", buildingsDetailList);
    		returnMap.put("returnCode", "000000");
    		returnMap.put("data",pagedata);
    		returnMap.put("returnMsg", "获取楼盘列表！");
    	}else{
    		returnMap.put("returnCode", "1111");
    		returnMap.put("returnMsg", "获取楼盘列表失败！");
    	}
		
		
	    return returnMap;
	}
	/**
	 * TODO 查询楼盘详情
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param filter
	 * @return
	 * @create 2016年2月25日
	 */
	public Map<String, Object> findBulidingsDetail(Long buildingsId,Long pid){
		returnMap.clear();
		//输入参数当前页，每页记录数，城市id
		Map<String, Object> buildingsDetail=buildingsMapper.getMoreDetail(buildingsId,pid);
		if(StringUtil.isNotEmptyMap(buildingsDetail)){
    		returnMap.put("returnCode", "000000");
    		returnMap.put("data",buildingsDetail);
    		returnMap.put("returnMsg", "获取楼盘信息成功！");
    	}else{
    		returnMap.put("returnCode", "1111");
    		returnMap.put("returnMsg", "获取楼盘信息失败！");
    	}
	    return returnMap;
	}
	
	
	//按条件查询楼盘坐标
	public Map<String, Object> findZuobiao(Map<String, Object> filter){
		returnMap.clear();
		//输入参数当前页，每页记录数，城市id
		if("111".equals(filter.get("city_id"))) {
			filter.put("city_id", "");
		}
		if("222".equals(filter.get("active_price"))) {
			filter.put("active_price1", "");
		}else if ("4000".equals(filter.get("active_price"))){
			filter.put("active_price1", "0");
			filter.put("active_price2", "4000");
		}else if ("6000".equals(filter.get("active_price"))){
			filter.put("active_price1", "4000");
			filter.put("active_price2", "6000");
		}
		else if ("8000".equals(filter.get("active_price"))){
			filter.put("active_price1", "6000");
			filter.put("active_price2", "8000");
		}else if ("10000".equals(filter.get("active_price"))){
			filter.put("active_price1", "8000");
			filter.put("active_price2", "10000");
		}else if ("11000".equals(filter.get("active_price"))){
			filter.put("active_price1", "10000");
			filter.put("active_price2", "50000");
		}
		if("333".equals(filter.get("acreage"))) {
			filter.put("acreage1", "");
		}else if ("50".equals(filter.get("acreage"))){
			filter.put("acreage1", "0");
			filter.put("acreage2", "50");
		}else if ("70".equals(filter.get("acreage"))){
			filter.put("acreage1", "50");
			filter.put("acreage2", "70");
		}else if ("90".equals(filter.get("acreage"))){
			filter.put("acreage1", "70");
			filter.put("acreage2", "90");
		}else if ("110".equals(filter.get("acreage"))){
			filter.put("acreage1", "90");
			filter.put("acreage2", "110");
		}else if ("150".equals(filter.get("acreage"))){
			filter.put("acreage1", "110");
			filter.put("acreage2", "150");
		}
		if("444".equals(filter.get("shi"))) {
			filter.put("shi", "");
		}
		List<Map<String, Object>> buildingsDetailList=buildingsMapper.findZuobiao(filter);
		if(StringUtil.isNotEmptyList(buildingsDetailList)){
			returnMap.put("returnCode", "000000");
    		returnMap.put("data",buildingsDetailList);
    		returnMap.put("returnMsg", "获取坐标信息成功！");
    	}else{
    		returnMap.put("returnCode", "1111");
    		returnMap.put("returnMsg", "获取坐标信息失败！");
    	}
		
		
	    return returnMap;
	}
}
