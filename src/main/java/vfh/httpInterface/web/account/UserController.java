package vfh.httpInterface.web.account;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.commons.SessionVariable;
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.commons.enumeration.entity.PortraitSize;
import vfh.httpInterface.commons.enumeration.entity.State;
import vfh.httpInterface.service.account.AccountService;
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
@RequestMapping("/account/user")
public class UserController {

    @Autowired
    private AccountService accountService;

    /**
     * 用户列表
     *
     * @param pageRequest 分页请求实体
     * @param filter 查询条件
     * @param model spring mvc 的 Model 接口，主要是将 http servlet request 的属性返回到页面中
     *
     * @return 响应页面:WEB-INF/page/account/user/list.html
     */
    @RequestMapping("list")
    public Page<Map<String, Object>> list(PageRequest pageRequest,
                                          @RequestParam Map<String, Object> filter,
                                          Model model) {

        model.addAttribute("states", VariableUtils.getVariables(State.class, State.DELETE.getValue()));
    	if(null != filter.get("page")  && filter.get("page") != "") {
    		pageRequest.setPageNumber(Integer.parseInt((String)filter.get("page")));
	    }
        return accountService.findUsers(pageRequest,filter);
    }

    /**
     * 新增用户
     *
     * @param entity 用户实体 Map
     * @param groupIds 关联的组主键 ID 集合
     * @param redirectAttributes spring mvc 重定向属性
     *
     * @return 响应页面:WEB-INF/page/account/user/list.html
     */
    @RequestMapping("insert")
    public String insert(@RequestParam Map<String, Object> entity,
                         @RequestParam(required=false)List<Long> groupIds,
                         RedirectAttributes redirectAttributes) {

        accountService.insertUser(entity, groupIds);
        redirectAttributes.addFlashAttribute("success", "新增成功");

        return "redirect:/account/user/list";
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
    public String delete(@RequestParam List<Long> ids,RedirectAttributes redirectAttributes) {
        accountService.deleteUsers(ids);
        redirectAttributes.addFlashAttribute("success", "删除" + ids.size() + "条信息成功");
        return "redirect:/account/user/list";
    }

    /**
     * 更新用户
     *
     * @param entity 用户实体 Map
     * @param groupIds 关联的组主键 ID 集合
     * @param redirectAttributes spring mvc 重定向属性
     *
     * @return 响应页面:WEB-INF/page/account/user/list.html
     */
    @RequestMapping(value="update")
    public String update(@RequestParam Map<String, Object> entity,
                         @RequestParam(required=false)List<Long> groupIds,
                         RedirectAttributes redirectAttributes) {

        accountService.updateUser(entity, groupIds == null ? Lists.<Long>newArrayList() : groupIds);
        redirectAttributes.addFlashAttribute("success", "修改成功");
       
        String page = String.valueOf(entity.get("page"));
        return "redirect:/account/user/list?page="+page;
    }

    /**
     * 判断登录帐号是否唯一
     *
     * @param username 用户登录帐号
     *
     * @return true 表示唯一，否则 false
     */
    @ResponseBody
    @RequestMapping("is-username-unique")
    public boolean isUsernameUnique(String username) {
        return accountService.isUsernameUnique(username);
    }

    /**
     * 编辑或创建用户，响应页面:WEB-INF/page/account/user/add.html 或者 WEB-INF/page/account/user/edit.html
     *
     * @param id 主键id
     * @param model spring mvc 的 Model 接口，主要是将 http servlet request 的属性返回到页面中
     *
     */
    @RequestMapping({"edit","add"})
    public void createOrEdit(@RequestParam(required = false)Long id,Model model,Long page) {

        model.addAttribute("states", VariableUtils.getVariables(State.class, State.DELETE.getValue()));
        model.addAttribute("groups", accountService.findGroups(new HashMap<String, Object>()));
//        System.out.println(page);
//        model.addAttribute("page", page);
        if (page != null) {
        model.addAttribute("page", page);
        }
        if (id != null) {
            model.addAttribute("entity", accountService.getUser(id));
            model.addAttribute("hasGroups", accountService.getUserGroups(id));
        }

    }

    /**
     * 获取当前用户头像
     *
     * @throws java.io.IOException
     *
     * @return 用户头像的 byte 数组
     *
     */
    @RequestMapping("get-portrait")
    public ResponseEntity<byte[]> getCurrentUserPortrait(@RequestParam(required = false) String name) throws IOException {

        if (StringUtils.isEmpty(name)) {
            name = PortraitSize.MIDDLE.getName();
        }

        PortraitSize size = PortraitSize.getPortraitSize(name);

        return new ResponseEntity<byte[]>(accountService.getCurrentUserPortrait(size), HttpStatus.OK);

    }

    /**
     * 修改用户头像
     *
     * @param request HttpServletRequest http servlet request 对象，用于获取 FaustCplus 上穿上来的头像
     *
     * @throws IOException
     *
     * @return 上传成功返回 json: {status:"success"}，否则抛出异常。
     */
    @ResponseBody
    @RequestMapping("update-portrait")
    public Map<String, Object> updatePortrait(HttpServletRequest request) throws IOException {
        accountService.updateCurrentPortrait(request.getInputStream());

        Map<String, Object> result = Maps.newHashMap();
        // 设置状态值，让 FaustCplus 再次触发 jsfunc 的 js 函数
        result.put("status", "success");

        return result;
    }

    /**
     * 当前用户修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     *
     * @return 响应页面:系统首页
     */
    @RequestMapping("/update-password")
    public String updatePassword(String oldPassword,String newPassword, RedirectAttributes redirectAttributes) {

        Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();
        accountService.updateUserPassword(user, oldPassword, newPassword);
        redirectAttributes.addFlashAttribute("success","修改密码成功");
        return "redirect:/account/user/list";

    }

    /**
     * 修改用户信息
     *
     * @param entity 用户实体 Map
     *
     * @throws IOException
     *
     * @return 修改后的用户实体 json
     */
    @ResponseBody
    @RequestMapping("/update-profile")
    public Map<String, Object> updateProfile(@RequestParam Map<String, Object> entity) throws IOException {
        Map<String, Object> user = SessionVariable.getCurrentSessionVariable().getUser();
        user.put("nickname",entity.get("nickname"));
        user.put("email",entity.get("email"));
        accountService.updateUser(user, null);
        SessionVariable.getCurrentSessionVariable().setUser(user);
        return entity;
    }
}
