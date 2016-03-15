package vfh.httpInterface.web.buildings;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.commons.SessionVariable;
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.commons.enumeration.FieldType;
import vfh.httpInterface.commons.enumeration.entity.BuildingsState;
import vfh.httpInterface.service.buildings.BuildingsActiveService;
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
    @Autowired
    private BuildingsActiveService buildingsActiveService;
    
    
    
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
    
    @RequestMapping("list2")
    public Page<Map<String, Object>> list2(PageRequest pageRequest, @RequestParam Map<String, Object> filter,Model model) {

    	filter.put("buildings_id", filter.get("buildingsId"));
    	return buildingsActiveService.findBuildingsActiveList(pageRequest,filter,model);
    	
    }
    @RequestMapping("insert")
    public String insert(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {

    	buildingsActiveService.insertBuildingsActive(entity);
        redirectAttributes.addFlashAttribute("success", "新增活动成功");
        redirectAttributes.addAttribute("buildingsId", entity.get("buildings_id"));
        redirectAttributes.addAttribute("buildingsName", entity.get("buildingsName"));
        
        return "redirect:/buildings/developer/list2";
    }
    @RequestMapping(value="update")
    public String update(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {
    	buildingsActiveService.updateBuildingsActive(entity);
        redirectAttributes.addFlashAttribute("success", "修改价格成功");
        redirectAttributes.addAttribute("buildingsId", entity.get("buildings_id"));
        redirectAttributes.addAttribute("buildingsName", entity.get("buildingsName"));
        return "redirect:/buildings/developer/list2";
    }
    @RequestMapping({"edit","add"})
    public void createOrEdit(@RequestParam(required = false)Long id,@RequestParam(required = false)Long buildings_id,
    		@RequestParam(required = false)String buildingsName,Model model) {
        if (id != null) {
            model.addAttribute("entity", buildingsActiveService.getBuildingsActive(id));
       	 model.addAttribute("buildings_id",buildings_id);
       	 model.addAttribute("buildingsName",buildingsName);

        }else{
        	 model.addAttribute("buildings_id",buildings_id);
        	 model.addAttribute("buildingsName",buildingsName);
        }
    }
    
    @RequestMapping({"acPicEdit"})
    public void acPicEdit(@RequestParam Map<String, Object> filter,Model model) {

        if (filter.get("id") != null && filter.get("buildingsName") != null) {

       	model.addAttribute("buildingsName",filter.get("buildingsName").toString());
       	model.addAttribute("buildings_id",filter.get("buildings_id").toString());
    	model.addAttribute("id",filter.get("id").toString());
    //	model.addAttribute("shi",filter.get("shi").toString());
    //	Map<String, Object> hxt = buildingsHouseService.getHuxingtuById(Long.parseLong(filter.get("id").toString()));
//    	if(null != hxt) {
//    		String resource_path = (String) hxt.get("resource_path");
//    		String resource_name = (String) hxt.get("resource_name");
//    		String hxtSrc = resource_path + resource_name;
//    		model.addAttribute("hxtSrc",hxtSrc);
//    	}
    	//model.addAttribute("hxtsrc","");
        }else{
        	model.addAttribute("buildings_id",filter.get("buildings_id").toString());
        	 model.addAttribute("buildingsName",filter.get("buildingsName").toString());
        }
    }
    @ResponseBody
    @RequestMapping({"acPicInsert"})
    public void acPicInsert(HttpServletRequest request) throws Exception {

    	buildingsActiveService.insertAcPic(request);   
    }
}
