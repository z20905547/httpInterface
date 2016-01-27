package vfh.httpInterface.web.buildings;

import java.io.UnsupportedEncodingException;
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
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.commons.enumeration.entity.BuildingsState;
import vfh.httpInterface.service.buildings.BuildingsHouseService;
import vfh.httpInterface.service.buildings.BuildingsPriceService;
import vfh.httpInterface.service.buildings.BuildingsService;

/**  
 * TODO 楼盘后台控制类
 * @author harry
 * <b> 有问题请联系qq:359705093</b>   
 * @create 2016年1月11日
 */
@Controller
@RequestMapping("/buildings/house")
public class BuildingsHouseController {

    @Autowired
    private BuildingsHouseService buildingsHouseService;
    @RequestMapping("list")
    public Page<Map<String, Object>> list(PageRequest pageRequest, @RequestParam Map<String, Object> filter,Model model) {

    	return buildingsHouseService.findBybuildId(pageRequest,filter,model);
    	
    }
    @RequestMapping("insert")
    public String insert(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {

    	buildingsHouseService.insertBuildingsHouse(entity);
        redirectAttributes.addFlashAttribute("success", "新增价格成功");
        redirectAttributes.addAttribute("buildingsId", entity.get("buildingsId"));
        redirectAttributes.addAttribute("buildingsName", entity.get("buildingsName"));
        
        return "redirect:/buildings/house/list";
    }
    @RequestMapping(value="update")
    public String update(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {
    	buildingsHouseService.update(entity);
        redirectAttributes.addFlashAttribute("success", "修改户型成功");
        redirectAttributes.addAttribute("buildingsId", entity.get("buildingsId"));
       redirectAttributes.addAttribute("buildingsName", entity.get("buildingsName"));
        return "redirect:/buildings/house/list?buildingsId="+entity.get("buildingsId");
    }
    @RequestMapping({"edit","add"})
    public void createOrEdit(@RequestParam(required = false)Long id,@RequestParam(required = false)Long buildingsId,
    		@RequestParam(required = false)String buildingsName,Model model) {
        if (id != null) {
            model.addAttribute("entity", buildingsHouseService.get(id));
       	 model.addAttribute("buildingsId",buildingsId);
       	 model.addAttribute("buildingsName",buildingsName);


        }else{
        	 model.addAttribute("buildingsId",buildingsId);
        	 model.addAttribute("buildingsName",buildingsName);
        }
    }
}
