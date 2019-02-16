package vfh.httpInterface.web.buildings;

import java.io.UnsupportedEncodingException;
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
import vfh.httpInterface.commons.enumeration.entity.BuildingsState;
import vfh.httpInterface.service.buildings.BuildingsActiveService;
import vfh.httpInterface.service.buildings.BuildingsLatestService;
import vfh.httpInterface.service.buildings.BuildingsPriceService;
import vfh.httpInterface.service.buildings.BuildingsService;

/**  
 * TODO 楼盘后台控制类
 * @author harry
 * <b> 有问题请联系qq:359705093</b>   
 * @create 2016年1月11日
 */
@Controller
@RequestMapping("/buildings/latest")
public class BuildingsLatestController {

    @Autowired
    private BuildingsLatestService buildingsLatestService;
    @Autowired
    private BuildingsService buildingsService;
    @RequestMapping("list2")
    public Page<Map<String, Object>> list(PageRequest pageRequest, @RequestParam Map<String, Object> filter,Model model) {
    	Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();
    	String user_id= user.get("id").toString();
    	String username = user.get("username").toString();
    	String nickname = user.get("nickname").toString();
    	filter.put("username", username);
    	filter.put("nickname", nickname);
    	filter.put("user_id", user_id);
    	filter.put("buildings_id", filter.get("buildingsId"));
    	return buildingsLatestService.findBuildingsLatestList(pageRequest,filter,model);
    	
    }
    @RequestMapping("insert")
    public String insert(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {

    	Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();

    	String user_id = user.get("id").toString();
    	String username = user.get("username").toString();
    	String nickname = user.get("nickname").toString();
    	String user_phone = user.get("email").toString();
    	String buildings_id = entity.get("buildings_id").toString();
    	String buildingsName = entity.get("buildingsName").toString();
    	
    	entity.put("nickname", nickname);
    	entity.put("username", username);
    	entity.put("user_id", user_id);
    	entity.put("user_phone", user_phone);
    	
    	buildingsLatestService.insertBuildingsActive(entity);
    	buildingsLatestService.updateBuildingsActiveUpdata(entity);
    	entity.clear();
    	entity.put("id", buildings_id);

    	buildingsService.updateBuildings(entity);
        redirectAttributes.addFlashAttribute("success", "新增活动成功");
        redirectAttributes.addAttribute("buildingsId", buildings_id);
        redirectAttributes.addAttribute("buildingsName", buildingsName);
        
        return "redirect:/buildings/latest/list2";
    }
    @RequestMapping(value="update")
    public String update(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {
    	buildingsLatestService.updateBuildingsActive(entity);
    	buildingsLatestService.updateBuildingsActiveUpdata(entity);
    	String buildings_id = entity.get("buildings_id").toString();
    	String buildingsName = entity.get("buildingsName").toString();
    	entity.clear();
    	entity.put("id", buildings_id);
    	buildingsService.updateBuildings(entity);
        redirectAttributes.addFlashAttribute("success", "修改价格成功");
        redirectAttributes.addAttribute("buildingsId", buildings_id);
        redirectAttributes.addAttribute("buildingsName", buildingsName);
        return "redirect:/buildings/latest/list2";
    }
    @RequestMapping({"edit","add"})
    public void createOrEdit(@RequestParam(required = false)Long id,@RequestParam(required = false)Long buildings_id,
    		@RequestParam(required = false)String buildingsName,Model model) {
        if (id != null) {
            model.addAttribute("entity", buildingsLatestService.getBuildingsActive(id));
       	 model.addAttribute("buildings_id",buildings_id);
       	 model.addAttribute("buildingsName",buildingsName);

        }else{
        	 model.addAttribute("buildings_id",buildings_id);
        	 model.addAttribute("buildingsName",buildingsName);
        }
    }
    
   

}