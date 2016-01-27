package vfh.httpInterface.service.buildings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import vfh.httpInterface.dao.buildings.BuildingsHouseMapper;


@Service
@Transactional
public class BuildingsHouseService {

	@Autowired
	private BuildingsHouseMapper buildingsHouseMapper;
	public void insertBuildingsHouse(@MapValid("insert-house") Map<String, Object> entity){

			buildingsHouseMapper.insert(entity);

	}
	public void update(@MapValid("update-house")Map<String, Object> entity){
		buildingsHouseMapper.update(entity);
	}
	public Map<String, Object> get(Long id) {
        return buildingsHouseMapper.get(id);
    }
	public void delete(Map<String,Object> entity) {
        Long id = VariableUtils.typeCast(entity.get("id"),Long.class);
        buildingsHouseMapper.delete(id);
    }
	public void deleteBuildingsHouseList(List<Long> ids) {
        for(Long id : ids) {
            Map<String, Object> entity = get(id);
            if (MapUtils.isNotEmpty(entity)) {
            	delete(entity);
            }
        }
    }
	public Page<Map<String, Object>> findBybuildId(PageRequest pageRequest, Map<String, Object> filter,Model model) {
		Long buildingsId=null;
		String buildingsName=null;

		if(StringUtil.isNotEmptyObject(filter.get("buildingsId"))){
			buildingsId=Long.parseLong(filter.get("buildingsId").toString());
		}
		if(StringUtil.isNotEmptyObject(filter.get("buildingsName"))){
			buildingsName=filter.get("buildingsName").toString();
		}
		model.addAttribute("buildingsName",filter.get("buildingsName"));
		model.addAttribute("buildingsId",filter.get("buildingsId"));

		long total = buildingsHouseMapper.count(filter);
        filter.putAll(pageRequest.getMap());
        List<Map<String, Object>> content = buildingsHouseMapper.findBybuildId(filter);

        return new Page<Map<String, Object>>(pageRequest, content, total);
    }
}
