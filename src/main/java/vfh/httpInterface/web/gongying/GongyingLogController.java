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
import vfh.httpInterface.service.gongying.GongyingLogService;



@Controller
@RequestMapping("/gongying/log")
public class GongyingLogController {
    @Autowired
    private GongyingLogService gongyingLogService;
    @RequestMapping("list")
    public Page<Map<String, Object>> list(PageRequest pageRequest, @RequestParam Map<String, Object> filter,Model model) {
    	Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();

    	return gongyingLogService.findBasicDataList(pageRequest,filter,model);
    	
    }
    @RequestMapping("insert")
    public String insert(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {

    	Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();


    	String last_author = user.get("nickname").toString();

    	
    	entity.put("last_author", last_author);
    	
    	gongyingLogService.insertLog(entity);
    //	gongyingBasicDataService.updateBuildingsActiveUpdata(entity);
        redirectAttributes.addFlashAttribute("success", "新增成功");
        redirectAttributes.addAttribute("buildingsId", entity.get("buildings_id"));
        redirectAttributes.addAttribute("buildingsName", entity.get("buildingsName"));
        
        return "redirect:/gongying/log/list";
    }
    @RequestMapping(value="update")
    public String update(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {
    	
    	Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();
    	String c_user = user.get("nickname").toString();
    	entity.put("c_user", c_user);
    	
    	gongyingLogService.updateBuildingsActive(entity);
        redirectAttributes.addFlashAttribute("success", "修改成功");
        return "redirect:/gongying/log/list";
    }
    @RequestMapping({"edit","add"})
    public void createOrEdit(@RequestParam(required = false)Long customer_id,@RequestParam(required = false)Long buildings_id,
    		@RequestParam(required = false)String buildingsName,Model model) {
       
    	if (customer_id != null) {
         model.addAttribute("entity", gongyingLogService.getBuildingsActive(customer_id));

        }else{
        	
        }
    }
    
   
}
