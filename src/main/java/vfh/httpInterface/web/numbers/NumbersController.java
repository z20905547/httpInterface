package vfh.httpInterface.web.numbers;

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
 * 用户控制器
 *
 * @author maurice
 */
@Controller
@RequestMapping("/account/numbers")
public class NumbersController {

    @Autowired
    private NumbersService numbersService;

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
                                          Model model) {

        return numbersService.findnumbers(pageRequest,filter);
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
                         @RequestParam(required=false)List<Long> groupIds,
                         RedirectAttributes redirectAttributes) {

    	Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();
    	String staff = (String) user.get("username");
    	String u_id = String.valueOf(user.get("id"));
    	entity.put("staff", staff);
    	entity.put("u_id", u_id);
    	if(numbersService.insertNumber(entity)>0){numbersService.insertMiddle(entity);};
    	
        redirectAttributes.addFlashAttribute("success", "新增成功");

        return "redirect:/account/numbers/list";
    }
    
    /**
     * 新增信息  修改信息  页面
     *
     * @param id 用户主键 ID
     *
     * @return 用户实体 Map
     */
    @RequestMapping({"edit","add"})
    public void createOrEdit(@RequestParam(required = false)Long Id,Model model) {

        if (Id != null) {
            model.addAttribute("entity", numbersService.getNumByID(Id));
        }

    }
    
    

}
