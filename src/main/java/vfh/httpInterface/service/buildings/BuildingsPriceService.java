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
import vfh.httpInterface.dao.buildings.BuildingsMapper;
import vfh.httpInterface.dao.buildings.BuildingsPriceMapper;

@Service
@Transactional
public class BuildingsPriceService {
	@Autowired
	private BuildingsMapper buildingsMapper;
	@Autowired
	private BuildingsPriceMapper buildingsPriceMapper;

	public void insertBuildingsPrice(
			@MapValid("insert-price") Map<String, Object> entity) {

		try {
			// 如果上一条特价记录存在特价且时间尚未结束，则先更新上条记录的结束日期为这条记录开始的前一天
			// 如果上一条特价记录已经截止，则不做任何处理
			Map<String, Object> filter = new HashMap<String, Object>();
			filter.put("buildingsId", entity.get("buildingsId"));
			List<Map<String, Object>> priceList = buildingsPriceMapper
					.findBybuildId(filter);

			if (priceList.size() > 0 && priceList.get(0).get("end_date") != null) {
				Map<String, Object> prePrice = priceList.get(0);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String startstr = entity.get("start_date").toString();
				String endstr = priceList.get(0).get("end_date").toString();
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
					updateParams.put("id", prePrice.get("id"));
					updateParams.put("end_date", preend);
					buildingsPriceMapper.update(updateParams);
				}
			}

			// entity.put("endDate", "2116-12-12 00:00:00");
			entity.put("activeId", 0);
			if (entity.get("discount_price") == null
					|| entity.get("discount_price").equals(""))
				entity.put("discount_price", null);
			if (entity.get("first_price") == null
					|| entity.get("first_price").equals(""))
				entity.put("first_price", null);
			if (entity.get("nomal_price") == null
					|| entity.get("nomal_price").equals(""))
				entity.put("nomal_price", null);
			if (entity.get("nomal_price") == null
					|| entity.get("nomal_price").equals(""))
				entity.put("nomal_price", null);
			if (entity.get("start_date") == null
					|| entity.get("start_date").equals(""))
				entity.put("start_date", null);
			if (entity.get("end_date") == null
					|| entity.get("end_date").equals(""))
				entity.put("end_date", null);

			buildingsPriceMapper.insert(entity);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateBuildingsPrice(
			@MapValid("update-price") Map<String, Object> entity) {
		buildingsPriceMapper.update(entity);
	}

	public Map<String, Object> getBuildingsPrice(Long id) {
		return buildingsPriceMapper.get(id);
	}

	public void deleteBuildingsPrice(Map<String, Object> entity) {
		Long id = VariableUtils.typeCast(entity.get("id"), Long.class);
		buildingsPriceMapper.delete(id);
	}

	public void deleteBuildingsPriceList(List<Long> ids) {
		for (Long id : ids) {
			Map<String, Object> entity = getBuildingsPrice(id);
			if (MapUtils.isNotEmpty(entity)) {
				deleteBuildingsPrice(entity);
			}
		}
	}

	public Page<Map<String, Object>> findBuildingsPriceList(
			PageRequest pageRequest, Map<String, Object> filter, Model model) {
		Long buildingsId = null;
		String buildingsName = null;
		if (StringUtil.isNotEmptyObject(filter.get("buildingsId"))) {
			buildingsId = Long.parseLong(filter.get("buildingsId").toString());
		}
		if (StringUtil.isNotEmptyObject(filter.get("buildingsName"))) {
			buildingsName = filter.get("buildingsName").toString();
		}
		model.addAttribute("buildingsName", buildingsName);
		model.addAttribute("buildingsId", buildingsId);
		long total = buildingsPriceMapper.count(filter);
		filter.putAll(pageRequest.getMap());
		List<Map<String, Object>> content = buildingsPriceMapper
				.findBybuildId(filter);

		return new Page<Map<String, Object>>(pageRequest, content, total);
	}
}
