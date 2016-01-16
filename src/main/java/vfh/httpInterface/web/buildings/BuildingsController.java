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
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.commons.enumeration.entity.BuildingsState;
import vfh.httpInterface.service.buildings.BuildingsService;

/**
 * TODO 楼盘后台控制类
 * @author harry
 * <b> 有问题请联系qq:359705093</b>
 * @create 2016年1月11日
 */
@Controller
@RequestMapping("/buildings/buildings")
public class BuildingsController {

    @Autowired
    private BuildingsService buildingsService;

    @RequestMapping("list")
    public Page<Map<String, Object>> list(PageRequest pageRequest, @RequestParam Map<String, Object> filter,Model model) {
    	model.addAttribute("buildingsStates", VariableUtils.getVariables(BuildingsState.class));
    	return buildingsService.findBuildingsList(pageRequest,filter);
    }
    @RequestMapping("insert")
    public String insert(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {
    	buildingsService.insertBuildings(entity);
        redirectAttributes.addFlashAttribute("success", "新增楼盘成功");
        return "redirect:/buildings/buildings/list";
    }
    @RequestMapping("delete")
    public String delete(@RequestParam List<Long> ids,RedirectAttributes redirectAttributes) {
    	buildingsService.deleteBuildingsList(ids);
        redirectAttributes.addFlashAttribute("success", "删除" + ids.size() + "条楼盘记录成功");
        return "redirect:/buildings/buildings/list";
    }
    @RequestMapping(value="update")
    public String update(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {
    	buildingsService.updateBuildings(entity);
        redirectAttributes.addFlashAttribute("success", "修改楼盘信息成功");
        return "redirect:/buildings/buildings/list";
    }
    @RequestMapping({"edit","add"})
    public void createOrEdit(@RequestParam(required = false)Long id,Model model) {
        if (id != null) {
            model.addAttribute("entity", buildingsService.getBuildings(id));
        }
    }
}
