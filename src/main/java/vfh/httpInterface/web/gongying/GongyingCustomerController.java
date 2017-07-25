package vfh.httpInterface.web.gongying;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.commons.SessionVariable;
import vfh.httpInterface.service.gongying.GongyingCustomerService;
import vfh.httpInterface.service.gongying.GongyingLogService;



@Controller
@RequestMapping("/gongying/customer")
public class GongyingCustomerController {
    @Autowired
    private GongyingCustomerService gongyingCustomerService;
	@Autowired 
	private GongyingLogService gongyingLogService;
    @RequestMapping("list")
    public Page<Map<String, Object>> list(PageRequest pageRequest, @RequestParam Map<String, Object> filter,Model model) {
    	Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();

    	return gongyingCustomerService.findBasicDataList(pageRequest,filter,model);
    	
    }
    @RequestMapping("insert")
    public String insert(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {

    	Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();


    	String last_author = user.get("nickname").toString();

    	
    	entity.put("last_author", last_author);
    	
    	gongyingCustomerService.insertCustomer(entity);
    //	gongyingBasicDataService.updateBuildingsActiveUpdata(entity);
        redirectAttributes.addFlashAttribute("success", "新增成功");
        redirectAttributes.addAttribute("buildingsId", entity.get("buildings_id"));
        redirectAttributes.addAttribute("buildingsName", entity.get("buildingsName"));
        
        return "redirect:/gongying/customer/list";
    }
    @RequestMapping(value="update")
    public String update(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {
    	
    	Map<String,Object> user = SessionVariable.getCurrentSessionVariable().getUser();
    	String c_user = user.get("nickname").toString();
    	entity.put("c_user", c_user);

    	gongyingCustomerService.updateBuildingsActive(entity);
    	// 共赢审核人员审核
    	String  log_content = "";
    	String c_jd_name ="";
    	if(null!= entity.get("c_status") && entity.get("c_status").equals("2") ){
    		
    		log_content = "审核通过 已安排置业顾问对接客户";
    	}
    	if(null!= entity.get("c_status") && entity.get("c_status").equals("3") ){
    		
    		log_content = "审核不通过："+entity.get("c_note");
    	}
    	entity.put("log_content", log_content);
    	entity.put("log_type", "2");
    	entity.put("log_userid", user.get("id").toString());
    	entity.put("log_username", c_user);
    	 gongyingLogService.insertLog(entity);
        redirectAttributes.addFlashAttribute("success", "修改成功");
        return "redirect:/gongying/customer/list";
    }
    @RequestMapping({"edit","add"})
    public void createOrEdit(@RequestParam(required = false)Long customer_id,@RequestParam(required = false)Long buildings_id,
    		@RequestParam(required = false)String buildingsName,Model model) {
       
    //	System.out.println("ddddddddd"+customer_id);
    	if (customer_id != null) {
         model.addAttribute("entity", gongyingCustomerService.getBuildingsActive(customer_id));

        }else{
        	
        }
    }
    
   
}
