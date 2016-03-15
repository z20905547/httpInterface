package vfh.httpInterface.web.buildings;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.commons.SessionVariable;
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.commons.enumeration.FieldType;
import vfh.httpInterface.commons.enumeration.entity.BuildingsState;
import vfh.httpInterface.service.buildings.BuildingsDeveloperService;
import vfh.httpInterface.service.buildings.BuildingsService;

/**
 * TODO 楼盘后台控制类
 * @author harry
 * <b> 有问题请联系qq:359705093</b>
 * @create 2016年1月11日
 */
@Controller
@RequestMapping("/buildings/developer")
public class BuildingsDeveloperController {

    @Autowired
    private BuildingsService buildingsService;
    @Autowired
    private BuildingsDeveloperService buildingsDeveloperService;
     
    @RequestMapping("list")
    public Page<Map<String, Object>> list(PageRequest pageRequest, @RequestParam Map<String, Object> filter,Model model) {
    	 Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();
//    	 Map<String,Object> bid = buildingsDeveloperService.getBidByUname(user.get("username").toString());
    	model.addAttribute("buildingsStates", VariableUtils.getVariables(BuildingsState.class));
//    	 if( bid == null){
//    		 filter.put("id", 0);
//    	 }else {
//    	 filter.put("id", bid.get("bid")); user.get("username").toString()
//    	 }
    	filter.put("username", user.get("username").toString()); 
    	return buildingsDeveloperService.findBuildingsList(pageRequest,filter);
    }
}
