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
import vfh.httpInterface.service.gongying.GongyingPartnersService;

/**
 * TODO 楼盘后台控制类
 * @author harry
 * <b> 有问题请联系qq:359705093</b>
 * @create 2016年1月11日
 */
@Controller
@RequestMapping("/gongying/partners")
public class GongyingPartnersController {

    @Autowired
    private GongyingPartnersService  gongyingPartnersService;
    @RequestMapping("list")
    public Page<Map<String, Object>> list(PageRequest pageRequest, @RequestParam Map<String, Object> filter,Model model) {

    		if(null != filter.get("city_id") || "" != filter.get("city_id")){
    		model.addAttribute("city_id", filter.get("city_id"));
    	}else{
    		model.addAttribute("city_id", "wu");
    	}
    	if(null != filter.get("area_id") || "" != filter.get("area_id")){
    		 model.addAttribute("area_id", filter.get("area_id"));

    	}else{
    		model.addAttribute("area_id", "wu");
    	}
       
    	return gongyingPartnersService.findBuildingsList(pageRequest,filter);
    }
    @RequestMapping("insert")
    public String insert(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {
    	gongyingPartnersService.insertBuildings(entity);
        redirectAttributes.addFlashAttribute("success", "新增机构成功");
        return "redirect:/gongying/partners/list";
    }
    @RequestMapping("delete")
    public String delete(@RequestParam List<Long> ids,RedirectAttributes redirectAttributes) {
    	gongyingPartnersService.deleteBuildingsList(ids);
        redirectAttributes.addFlashAttribute("success", "删除" + ids.size() + "条楼盘记录成功");
        return "redirect:/gongying/partners/list";
    }
    @RequestMapping(value="update")
    public String update(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {
    	gongyingPartnersService.updateBuildings(entity);
        redirectAttributes.addFlashAttribute("success", "修改机构信息成功");
        return "redirect:/gongying/partners/list";
    }
    @RequestMapping({"edit","add"})
    public void createOrEdit(@RequestParam(required = false)Long id,Model model) {
        if (id != null) {
       
        	
            model.addAttribute("entity", gongyingPartnersService.getBuildings(id));
        }
    }
}
