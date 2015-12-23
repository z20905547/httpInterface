package vfh.httpInterface.web.variable;

import com.google.common.collect.Maps;
import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.commons.enumeration.FieldType;
import vfh.httpInterface.service.variable.SystemVariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

/**
 * 数据字典控制器
 *
 * @author maurice
 */
@Controller
@RequestMapping("/variable/data-dictionary")
public class DataDictioanryController {

    @Autowired
    private SystemVariableService systemVariableService;

    /**
     * 数据字典列表
     *
     * @param pageRequest 分页请求实体
     * @param filter 查询条件
     * @param model spring mvc model 接口
     *
     * @return 响应页面:WEB-INF/page/variable/data-dictionary/list.html
     */
    @RequestMapping("list")
    public Page<Map<String, Object>> list(PageRequest pageRequest,
                                          @RequestParam Map<String, Object> filter,
                                          Model model) {

        model.addAttribute("valueTypes", VariableUtils.getVariables(FieldType.class));
        model.addAttribute("dictionaryCategories", systemVariableService.getAllDictionaryCategories());

        return systemVariableService.findDataDictionaries(pageRequest,filter);
    }


    /**
     * 保存数据字典
     *
     * @param entity 数据字典实体 Map
     * @param redirectAttributes spring mvc 重定向属性
     *
     * @return 响应页面:WEB-INF/page/variable/data-dictionary/list.html
     */
    @RequestMapping("save")
    public String save(@RequestParam Map<String, Object> entity,RedirectAttributes redirectAttributes) {

        systemVariableService.saveDataDictionary(entity);
        redirectAttributes.addFlashAttribute("success", "保存成功");

        return "redirect:/variable/data-dictionary/list";
    }

    /**
     * 编辑数据字典,响应页面:WEB-INF/page/variable/data-dictionary/edit.html
     *
     * @param id 数据字典主键 ID
     * @param model spring mvc 的 Model 接口，主要是将 http servlet request 的属性返回到页面中
     *
     */
    @RequestMapping("edit")
    public void edit(Long id,Model model) {

        model.addAttribute("valueTypes", VariableUtils.getVariables(FieldType.class));
        model.addAttribute("dictionaryCategories", systemVariableService.getAllDictionaryCategories());

        Map<String, Object> entity = Maps.newHashMap();

        if (id != null) {
            entity = systemVariableService.getDataDictionary(id);
        }

        model.addAttribute("entity",entity);

    }

    /**
     * 删除数据字典
     *
     * @param ids 数据字典主键 ID 集合
     * @param redirectAttributes spring mvc 重定向属性
     *
     * @return 响应页面:WEB-INF/page/variable/data-dictionary/list.html
     */
    @RequestMapping("delete")
    public String delete(@RequestParam List<Long> ids,RedirectAttributes redirectAttributes) {
        systemVariableService.deleteDataDictionaries(ids);
        redirectAttributes.addFlashAttribute("success", "删除" + ids.size() + "条信息成功");
        return "redirect:/variable/data-dictionary/list";
    }
}
