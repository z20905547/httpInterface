package vfh.httpInterface.web.reback;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.commons.SessionVariable;
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.commons.enumeration.entity.PortraitSize;
import vfh.httpInterface.commons.enumeration.entity.State;
import vfh.httpInterface.service.account.AccountService;
import vfh.httpInterface.service.notice.NoticeService;
import vfh.httpInterface.service.numbers.NumbersService;
import vfh.httpInterface.service.reback.RebackService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 回访记录控制器
 *
 * @author maurice
 */
@Controller
@RequestMapping("/account/reback")
public class RebackController {

    @Autowired
    private RebackService rebackService;

    /**
     * 来电用户列表
     *
     * @param pageRequest 分页请求实体
     * @param filter 查询条件
     * @param model spring mvc 的 Model 接口，主要是将 http servlet request 的属性返回到页面中
     *
     * @return 响应页面:WEB-INF/page/account/numbers/list.html
     */
    @RequestMapping("list")
    public Page<Map<String, Object>> list(PageRequest pageRequest,
                                          @RequestParam Map<String, Object> filter,
                                          @RequestParam(required = false)Long id,
                                          Model model) {
    	model.addAttribute("id", id);
        return rebackService.find(pageRequest,filter);
    }

    /**
     * 新增用户
     *
     * @param entity 用户实体 Map
     * @param groupIds 关联的组主键 ID 集合
     * @param redirectAttributes spring mvc 重定向属性
     *
     * @return 响应页面:WEB-INF/page/account/numbers/list.html
     */
    @RequestMapping("insert")
    public String insert(@RequestParam Map<String, Object> entity,
                         @RequestParam(required=false)List<Long> id,
                         RedirectAttributes redirectAttributes) {

    	rebackService.insert(entity);
        redirectAttributes.addFlashAttribute("success", "新增成功");
        String pid = (String) entity.get("pid");
        return "redirect:/account/reback/list?id="+pid;
    }
    
    /**
     * 新增信息  修改信息  页面
     *
     * @param id 用户主键 ID
     *
     * @return 用户实体 Map
     */
    @RequestMapping({"edit"})
    public void createOrEdit(@RequestParam(required = false)Long id,@RequestParam(required = false)Long pid,Model model) {

        if (id != null) {
            model.addAttribute("entity", rebackService.getOneByID(id));
        }

    }
    
    /**
     * 新增信息  修改信息  页面
     *
     * @param id 用户主键 ID
     *
     * @return 用户实体 Map
     */
    @RequestMapping({"add"})
    public void create(@RequestParam(required = false)Long id,@RequestParam(required = false)Long pid,Model model) {
   		 model.addAttribute("id", id);

    }

}
