package vfh.httpInterface.web.buildings;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**  
 * TODO 楼盘资源
 * @author harry
 * <b> 有问题请联系qq:359705093</b>   
 * @create 2016年1月11日
 */
@Controller
@RequestMapping("/buildings/resource")
public class BuildingsResourceController {
//    @Autowired
//    private BuildingsPriceService buildingsPriceService;
    @RequestMapping({"edit","add"})
    public void createOrEdit(@RequestParam(required = false)Long id,@RequestParam(required = false)Long buildingsId,
    		@RequestParam(required = false)String buildingsName,Model model) {
        if (id != null) {
//            model.addAttribute("entity", buildingsPriceService.getBuildingsPrice(id));
       	 model.addAttribute("buildingsId",buildingsId);
       	 model.addAttribute("buildingsName",buildingsName);

        }else{
        	 model.addAttribute("buildingsId",buildingsId);
        	 model.addAttribute("buildingsName",buildingsName);
        }
    }
}
