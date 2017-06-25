package vfh.httpInterface.web.gongying;

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
import vfh.httpInterface.service.gongying.GongyingWorkersService;



@Controller
@RequestMapping("/gongying/workers")
public class GongyingWorkersController {
    @Autowired
    private GongyingWorkersService gongyingWorkersService;
    @RequestMapping("list")
    public Page<Map<String, Object>> list(PageRequest pageRequest, @RequestParam Map<String, Object> filter,Model model) {
    	Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();

    	return gongyingWorkersService.findBasicDataList(pageRequest,filter,model);
    	
    }
    @RequestMapping("insert")
    public String insert(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {

    	Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();


    	String last_author = user.get("nickname").toString();

    	
    	entity.put("last_author", last_author);
    	
    	gongyingWorkersService.insertBuildingsActive(entity);
    //	gongyingBasicDataService.updateBuildingsActiveUpdata(entity);
        redirectAttributes.addFlashAttribute("success", "新增成功");
        redirectAttributes.addAttribute("buildingsId", entity.get("buildings_id"));
        redirectAttributes.addAttribute("buildingsName", entity.get("buildingsName"));
        
        return "redirect:/gongying/workers/list";
    }
    @RequestMapping(value="update")
    public String update(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {
    	gongyingWorkersService.updateBuildingsActive(entity);
    	//gongyingBasicDataService.updateBuildingsActiveUpdata(entity);
        redirectAttributes.addFlashAttribute("success", "修改成功");
        redirectAttributes.addAttribute("buildingsId", entity.get("buildings_id"));
        redirectAttributes.addAttribute("buildingsName", entity.get("buildingsName"));
        return "redirect:/gongying/workers/list";
    }
    @RequestMapping({"edit","add"})
    public void createOrEdit(@RequestParam(required = false)Long id,@RequestParam(required = false)Long buildings_id,
    		@RequestParam(required = false)String buildingsName,Model model) {
        if (id != null) {
         model.addAttribute("entity", gongyingWorkersService.getBuildingsActive(id));
       	 model.addAttribute("buildings_id",buildings_id);
       	 model.addAttribute("buildingsName",buildingsName);

        }else{
        	 model.addAttribute("buildings_id",buildings_id);
        	 model.addAttribute("buildingsName",buildingsName);
        }
    }
    
   
}
