package vfh.httpInterface.service.buildings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import vfh.httpInterface.dao.buildings.BuildingActiveMapper;
import vfh.httpInterface.dao.buildings.BuildingsMapper;
import vfh.httpInterface.dao.buildings.BuildingsPriceMapper;

@Service
@Transactional
public class BuildingsActiveService {

	@Autowired
	private BuildingActiveMapper buildingActiveMapper;

	public void insertBuildingsActive(
			@MapValid("insert-active") Map<String, Object> entity) {

		try {
			// 如果上一条特价记录存在特价且时间尚未结束，则先更新上条记录的结束日期为这条记录开始的前一天
			// 如果上一条特价记录已经截止，则不做任何处理
			Map<String, Object> filter = new HashMap<String, Object>();
			filter.put("buildings_id", entity.get("buildings_id"));
			List<Map<String, Object>> activeList = buildingActiveMapper
					.find(filter);

			if (activeList.size() > 0 && activeList.get(0).get("end_date") != null) {
				Map<String, Object> preActive = activeList.get(0);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String startstr = entity.get("start_date").toString();
				String endstr = activeList.get(0).get("end_date").toString();
				Date startData = sdf.parse(startstr);
				Date endData = sdf.parse(endstr);


			//本次特价开始时间和上次特价截止时间做比较
		        Calendar c1=Calendar.getInstance();  
		        Calendar c2=Calendar.getInstance();  
		        try {  
		            c1.setTime(sdf.parse(startstr));  
		            c2.setTime(sdf.parse(endstr));  
		        } catch (ParseException e) {  
		            e.printStackTrace();  
		        }  
		        int result=c1.compareTo(c2); 				
				
		     // 如果上一条特价记录存在特价且时间尚未结束，则先更新上条记录的结束日期为这条记录开始的前一天		
				if (result==0 || result==-1) {
					startData.setTime(startData.getTime() - 24 * 60 * 60 * 1000);
					String preend = sdf.format(startData);
					Map<String, Object> updateParams = new HashMap<String, Object>();
					updateParams.put("id", preActive.get("id"));
					updateParams.put("end_date", preend);
					buildingActiveMapper.update(updateParams);
				}
			}

			// entity.put("endDate", "2116-12-12 00:00:00");
//			entity.put("activeId", 0);
//			if (entity.get("discount_price") == null
//					|| entity.get("discount_price").equals(""))
//				entity.put("discount_price", null);
//			if (entity.get("first_price") == null
//					|| entity.get("first_price").equals(""))
//				entity.put("first_price", null);
//			if (entity.get("nomal_price") == null
//					|| entity.get("nomal_price").equals(""))
//				entity.put("nomal_price", null);
//			if (entity.get("nomal_price") == null
//					|| entity.get("nomal_price").equals(""))
//				entity.put("nomal_price", null);
//			if (entity.get("start_date") == null
//					|| entity.get("start_date").equals(""))
//				entity.put("start_date", null);
//			if (entity.get("end_date") == null
//					|| entity.get("end_date").equals(""))
//				entity.put("end_date", null);

//            entity.put("active_type", "1");
//            entity.put("active_status", "1");
			buildingActiveMapper.insert(entity);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateBuildingsActive(
			@MapValid("update-active") Map<String, Object> entity) {
		buildingActiveMapper.update(entity);
	}

	public Map<String, Object> getBuildingsActive(Long id) {
		return buildingActiveMapper.get(id);
	}

	public void deleteBuildingsActive(Map<String, Object> entity) {
		Long id = VariableUtils.typeCast(entity.get("id"), Long.class);
		buildingActiveMapper.delete(id);
	}

//	public void deleteBuildingsPriceList(List<Long> ids) {
//		for (Long id : ids) {
//			Map<String, Object> entity = getBuildingsPrice(id);
//			if (MapUtils.isNotEmpty(entity)) {
//				deleteBuildingsPrice(entity);
//			}
//		}
//	}

	public Page<Map<String, Object>> findBuildingsActiveList(
			PageRequest pageRequest, Map<String, Object> filter, Model model) {
		Long buildings_id = null;
		String buildingsName = null;


			buildings_id = Long.parseLong(filter.get("buildings_id").toString());

		
		
		if (StringUtil.isNotEmptyObject(filter.get("buildingsName"))) {
			buildingsName = filter.get("buildingsName").toString();
		}
		model.addAttribute("buildingsName", buildingsName);
		model.addAttribute("buildings_id", buildings_id);
		long total = buildingActiveMapper.count(filter);
		filter.putAll(pageRequest.getMap());
		List<Map<String, Object>> content = buildingActiveMapper
				.find(filter);

		return new Page<Map<String, Object>>(pageRequest, content, total);
	}
}
