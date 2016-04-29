package vfh.httpInterface.web.notice;

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
@RequestMapping("/account/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 用户列表
     *
     * @param pageRequest 分页请求实体
     * @param filter 查询条件
     * @param model spring mvc 的 Model 接口，主要是将 http servlet request 的属性返回到页面中
     *
     * @return 响应页面:WEB-INF/page/account/notice/list.html
     */
    @RequestMapping("list")
    public Page<Map<String, Object>> list(PageRequest pageRequest,
                                          @RequestParam Map<String, Object> filter,
                                          Model model) {

        return noticeService.findNotices(pageRequest,filter);
    }

    /**
     * 新增用户
     *
     * @param entity 用户实体 Map
     * @param groupIds 关联的组主键 ID 集合
     * @param redirectAttributes spring mvc 重定向属性
     *
     * @return 响应页面:WEB-INF/page/account/notice/list.html
     */
    @RequestMapping("insert")
    public String insert(@RequestParam Map<String, Object> entity,
                         @RequestParam(required=false)List<Long> groupIds,
                         RedirectAttributes redirectAttributes) {

    	Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();
    	String author = (String) user.get("username");
    	entity.put("author", author);
        noticeService.insertNotice(entity);
        redirectAttributes.addFlashAttribute("success", "新增成功");

        return "redirect:/account/notice/list";
    }
    
    /**
     * 编辑或创建用户，响应页面:WEB-INF/page/account/notice/add.html 或者 WEB-INF/page/account/notice/edit.html
     *
     * @param id 主键id
     * @param model spring mvc 的 Model 接口，主要是将 http servlet request 的属性返回到页面中
     *
     */
    @RequestMapping({"edit","add"})
    public void createOrEdit(@RequestParam(required = false)Long Id,Model model) {

        if (Id != null) {
            model.addAttribute("entity", noticeService.getNotice(Id));
        }

    }

    /**
     * 删除用户
     *
     * @param ids 用户主键 ID 集合
     * @param redirectAttributes spring mvc 重定向属性
     *
     * @return 响应页面:WEB-INF/page/account/user/list.html
     */
    @RequestMapping("delete")
    public String delete(@RequestParam Map<String, Object> entity,RedirectAttributes redirectAttributes) {
        noticeService.deleteNotice(entity);
        redirectAttributes.addFlashAttribute("success", "删除条信息成功");
        return "redirect:/account/notice/list";
    }

    /**
     * 更新公告
     *
     * @param entity 用户实体 Map
     * @param groupIds 关联的组主键 ID 集合
     * @param redirectAttributes spring mvc 重定向属性
     *
     * @return 响应页面:WEB-INF/page/account/user/list.html
     */
    @RequestMapping(value="update")
    public String update(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {
    	
    	
//    	System.out.println(" -------------------------");
//      	System.out.println(entity);
    	
    	Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();
    	String author = (String) user.get("username");
    	entity.put("author", author);
        
    	noticeService.upNoticeByID(entity);
        redirectAttributes.addFlashAttribute("success", "修改成功");
        return "redirect:/account/notice/list";
    }

    /**
     * 单调查询
     *
     * @param entity 用户实体 Map
     * @param groupIds 关联的组主键 ID 集合
     * @param redirectAttributes spring mvc 重定向属性
     *
     * @return 响应页面:WEB-INF/page/account/user/list.html
     */
    @RequestMapping(value="getbyId")
    public String getbyId(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {

        noticeService.getbyId(entity);
        redirectAttributes.addFlashAttribute("success", "修改成功");
        return "redirect:/account/notice/list";
    }

    
    //上传图片
    @ResponseBody
    @RequestMapping({"addNoticePic"})
    public void addNoticePic(HttpServletRequest request) throws Exception {

    	noticeService.addNoticePic(request);   
    }
}
