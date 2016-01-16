package vfh.httpInterface.service.buildings;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.commons.valid.annotation.MapValid;
import vfh.httpInterface.dao.buildings.BuildingsMapper;
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
	/**
	 * TODO 添加楼盘
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param entity
	 * @create 2016年1月11日
	 */
	public void insertBuildings(@MapValid("insert-buildings") Map<String, Object> entity){
		entity.put("status", 0);
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
	public List<Map<String, Object>> findUsers(Map<String, Object> filter) {
		return buildingsMapper.find(filter);
	}
	/**
	 * TODO 分页获取用户列表
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
        List<Map<String, Object>> content = findUsers(filter);
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
}
