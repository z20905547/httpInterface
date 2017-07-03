package vfh.httpInterface.web.gongying;

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
import vfh.httpInterface.commons.enumeration.entity.BuildingsState;
import vfh.httpInterface.service.gongying.GongyingTravelService;




@Controller
@RequestMapping("/gongying/travel")
public class GongyingTravelController {
    @Autowired
    private GongyingTravelService gongyingTravelService;
    @RequestMapping("list")
    public Page<Map<String, Object>> list(PageRequest pageRequest, @RequestParam Map<String, Object> filter,Model model) {
    	Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();

    	return gongyingTravelService.findBasicDataList(pageRequest,filter,model);
    	
    }
    @RequestMapping("insert")
    public String insert(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {

    	Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();


    	String last_author = user.get("nickname").toString();

    	
    	entity.put("last_author", last_author);
    	
    	gongyingTravelService.insertBuildingsActive(entity);
    //	gongyingBasicDataService.updateBuildingsActiveUpdata(entity);
        redirectAttributes.addFlashAttribute("success", "新增成功");
        redirectAttributes.addAttribute("buildingsId", entity.get("buildings_id"));
        redirectAttributes.addAttribute("buildingsName", entity.get("buildingsName"));
        
        return "redirect:/gongying/travel/list";
    }
    @RequestMapping(value="update")
    public String update(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {
    	gongyingTravelService.updateBuildingsActive(entity);
    	//gongyingBasicDataService.updateBuildingsActiveUpdata(entity);
        redirectAttributes.addFlashAttribute("success", "修改成功");
        redirectAttributes.addAttribute("buildingsId", entity.get("buildings_id"));
        redirectAttributes.addAttribute("buildingsName", entity.get("buildingsName"));
        return "redirect:/gongying/travel/list";
    }
    @RequestMapping({"edit","add"})
    public void createOrEdit(@RequestParam(required = false)Long id,@RequestParam(required = false)Long buildings_id,
    		@RequestParam(required = false)String buildingsName,Model model) {
        if (id != null) {
         model.addAttribute("entity", gongyingTravelService.getBuildingsActive(id));
       	 model.addAttribute("buildings_id",buildings_id);
       	 model.addAttribute("buildingsName",buildingsName);

        }else{
        	 model.addAttribute("buildings_id",buildings_id);
        	 model.addAttribute("buildingsName",buildingsName);
        }
    }
    @RequestMapping("list2")
    public Page<Map<String, Object>> list2(PageRequest pageRequest, @RequestParam Map<String, Object> filter,Model model,RedirectAttributes redirectAttributes) {
    	Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();
    	 redirectAttributes.addAttribute("travel_id", filter.get("travel_id"));

    	return gongyingTravelService.findBasicDataList2(pageRequest,filter,model);
    	
    }
    @RequestMapping("insert2")
    public String insert2(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {

    	Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();


    	String last_author = user.get("nickname").toString();

    	
    	entity.put("last_author", last_author);
    	
    	gongyingTravelService.insertBuildingsActive2(entity);
    //	gongyingBasicDataService.updateBuildingsActiveUpdata(entity);
        redirectAttributes.addFlashAttribute("success", "新增成功");
        redirectAttributes.addAttribute("travel_id", entity.get("travel_id"));
        redirectAttributes.addAttribute("buildingsName", entity.get("buildingsName"));
        
        return "redirect:/gongying/travel/list2";
    }
    @RequestMapping(value="update2")
    public String update2(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {
    	gongyingTravelService.updateBuildingsActive2(entity);
    	//gongyingBasicDataService.updateBuildingsActiveUpdata(entity);
        redirectAttributes.addFlashAttribute("success", "修改成功");
        redirectAttributes.addAttribute("buildingsId", entity.get("buildings_id"));
        redirectAttributes.addAttribute("buildingsName", entity.get("buildingsName"));
        return "redirect:/gongying/travel/list2";
    }
    @RequestMapping({"edit2","add2"})
    public void createOrEdit2(@RequestParam(required = false)Long id,@RequestParam(required = false)Long travel_id,
    		@RequestParam(required = false)String buildingsName,Model model) {
    	if (id != null) {
         model.addAttribute("entity", gongyingTravelService.getBuildingsActive2(id));

       	 model.addAttribute("buildingsName",buildingsName);
       	 model.addAttribute("travel_id",travel_id);
        }else{
        	 model.addAttribute("travel_id",travel_id);
        	 model.addAttribute("buildingsName",buildingsName);
        }
    }
    
    @RequestMapping("delete2")
    public String delete2(@RequestParam List<Long> ids,RedirectAttributes redirectAttributes,@RequestParam(required = false)Long travel_id,Model model) {
    	gongyingTravelService.deletetravelsList(ids);
        redirectAttributes.addFlashAttribute("success", "删除" + ids.size() + "条楼盘记录成功");
        redirectAttributes.addAttribute("travel_id",travel_id);
        return "redirect:/gongying/travel/list2";
    }
}
