package vfh.httpInterface.web.order;

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
import vfh.httpInterface.commons.enumeration.FieldType;
import vfh.httpInterface.commons.enumeration.entity.BuildingsState;
import vfh.httpInterface.service.buildings.BuildingsService;
import vfh.httpInterface.service.order.OrderService;

/**
 * TODO 楼盘后台控制类
 * @author harry
 * <b> 有问题请联系qq:359705093</b>
 * @create 2016年1月11日
 */
@Controller
@RequestMapping("/order/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @RequestMapping("list")
    public Page<Map<String, Object>> list(PageRequest pageRequest, @RequestParam Map<String, Object> filter,Model model) {
    	
    	return orderService.findOrderList(pageRequest,filter);
    }
    @RequestMapping("insert")
    public String insert(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {
    	orderService.insert(entity);
        redirectAttributes.addFlashAttribute("success", "新增预约成功");
        return "redirect:/order/order/list";
    }

}
