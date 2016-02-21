package vfh.httpInterface.service.buildings;


import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.dao.buildings.BuildingsDeveloperMapper;

/**
 * TODO 楼盘业务逻辑
 * @author harry
 * <b> 有问题请联系qq:359705093</b>
 * @create 2016年1月11日
 */
@Service
@Transactional
public class BuildingsDeveloperService {

	@Autowired
	private BuildingsDeveloperMapper buildingsDeveloperMapper;
	
	/**
	 * TODO 根据id查询楼盘
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param id
	 * @return
	 * @create 2016年1月12日
	 */
	public Map<String, Object> getBidByUname (String username) { 

        return buildingsDeveloperMapper.get(username);
    }
	
	public Page<Map<String, Object>> findBuildingsList(PageRequest pageRequest, Map<String, Object> filter) {
        long total = buildingsDeveloperMapper.count(filter);
        filter.putAll(pageRequest.getMap());
        List<Map<String, Object>> content = findBuildings(filter);

        return new Page<Map<String, Object>>(pageRequest, content, total);
    }
	
	public List<Map<String, Object>> findBuildings(Map<String, Object> filter) {
		return buildingsDeveloperMapper.find(filter);
	}
}
