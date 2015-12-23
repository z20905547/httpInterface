package vfh.httpInterface.web.account;

import com.google.common.collect.Lists;
import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组控制器
 *
 * @author maurice
 */
@Controller
@RequestMapping("/account/group")
public class GroupController {

    @Autowired
    private AccountService accountService;

    /**
     * 组列表
     *
     * @param pageRequest 分页请求实体
     * @param filter 查询条件
     *
     * @return 响应页面:WEB-INF/page/account/group/list.html
     */
    @RequestMapping("list")
    public Page<Map<String, Object>> list(PageRequest pageRequest, @RequestParam Map<String, Object> filter) {
        return accountService.findGroups(pageRequest,filter);
    }

    /**
     * 保存组
     *
     * @param entity 组实体 Map
     * @param resourceIds 关联的资源主键 ID 集合
     * @param redirectAttributes spring mvc 重定向属性
     *
     * @return 响应页面:WEB-INF/page/account/group/list.html
     */
    @RequestMapping("save")
    public String save(@RequestParam Map<String, Object> entity,
                       @RequestParam(required=false)List<Long> resourceIds,
                       RedirectAttributes redirectAttributes) {

        accountService.saveGroup(entity,resourceIds == null ? Lists.<Long>newArrayList() : resourceIds);
        redirectAttributes.addFlashAttribute("success", "保存成功");

        return "redirect:/account/group/list";
    }

    /**
     * 删除组
     *
     * @param ids 组主键 ID 集合
     * @param redirectAttributes spring mvc 重定向属性
     *
     * @return 响应页面:WEB-INF/page/account/group/list.html
     */
    @RequestMapping("delete")
    public String delete(@RequestParam List<Long> ids,RedirectAttributes redirectAttributes) {
        accountService.deleteGroups(ids);
        redirectAttributes.addFlashAttribute("success", "删除" + ids.size() + "条信息成功");
        return "redirect:/account/group/list";
    }

    /**
     * 编辑组，响应页面:WEB-INF/page/account/group/edit.html
     *
     * @param id 组主键 ID
     * @param model spring mvc 的 Model 接口，主要是将 http servlet request 的属性返回到页面中
     *
     */
    @RequestMapping("edit")
    public void edit(@RequestParam(required = false)Long id,Model model) {

        model.addAttribute("resources", accountService.mergeResources(accountService.getResources()));
        model.addAttribute("entity", new HashMap<String,Object>());

        if (id != null) {
            model.addAttribute("entity", accountService.getGroup(id));
            model.addAttribute("hasResources", accountService.getGroupResources(id));
        }

    }

}
