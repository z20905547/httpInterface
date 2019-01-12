package vfh.httpInterface.web.buildings;


import java.util.HashMap;
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
import vfh.httpInterface.commons.RequestParamUtil;
import vfh.httpInterface.service.resource.ResourceService;

/**  
 * TODO 楼盘资源
 * @author harry
 * <b> 有问题请联系qq:359705093</b>   
 * @create 2016年1月11日
 */
@Controller
@RequestMapping("/buildings/resource")
public class BuildingsResourceController {
//    @Autowired
//    private BuildingsPriceService buildingsPriceService;
	
	@Autowired
    private ResourceService resourceService;
    @RequestMapping({"edit","add"})
    public void createOrEdit(@RequestParam(required = false)Long id,@RequestParam(required = false)Long buildingsId,
    		@RequestParam(required = false)String buildingsName,Model model) {
        if (id != null) {
//            model.addAttribute("entity", buildingsPriceService.getBuildingsPrice(id));
       	 model.addAttribute("buildingsId",buildingsId);
       	 model.addAttribute("buildingsName",buildingsName);

        }else{
        	 model.addAttribute("buildingsId",buildingsId);
        	 model.addAttribute("buildingsName",buildingsName);
        }
    }
    @RequestMapping({"list"})
    public Page<Map<String, Object>> list(PageRequest pageRequest, @RequestParam Map<String, Object> filter,Model model) {
    	
 
    	
    	model.addAttribute("buildingsId", filter.get("buildingsId"));
    	model.addAttribute("bigType", filter.get("bigType"));
    	model.addAttribute("smType", filter.get("smType"));
    	
    	return resourceService.findResource(pageRequest,filter,model);
    	
        
    }
    
    @RequestMapping("delete")
    public String delete(@RequestParam List<Long> ids,RedirectAttributes redirectAttributes,@RequestParam Map<String, Object> filter,Model model) {
    	resourceService.deletePics(ids);
        redirectAttributes.addFlashAttribute("success", "删除" + ids.size() + "条信息成功");
        redirectAttributes.addAttribute("buildingsId", filter.get("buildingsId"));
        redirectAttributes.addAttribute("bigType", filter.get("bigType"));
        redirectAttributes.addAttribute("smType", filter.get("smType"));
;

        return "redirect:/buildings/resource/list";
    }

}
