package vfh.httpInterface.web.buildings;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;

import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.commons.enumeration.entity.BuildingsState;
import vfh.httpInterface.service.buildings.BuildingsHouseService;
import vfh.httpInterface.service.buildings.BuildingsPriceService;
import vfh.httpInterface.service.buildings.BuildingsService;

/**  
 * TODO 楼盘后台控制类
 * @author harry
 * <b> 有问题请联系qq:359705093</b>   
 * @create 2016年1月11日
 */
@Controller
@RequestMapping("/buildings/house")
public class BuildingsHouseController {

    @Autowired
    private BuildingsHouseService buildingsHouseService;
    @RequestMapping("list")
    public Page<Map<String, Object>> list(PageRequest pageRequest, @RequestParam Map<String, Object> filter,Model model) {

    	return buildingsHouseService.findBybuildId(pageRequest,filter,model);
    	
    }
    @RequestMapping("insert")
    public String insert(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {

    	buildingsHouseService.insertBuildingsHouse(entity);
        redirectAttributes.addFlashAttribute("success", "新增价格成功");
        redirectAttributes.addAttribute("buildingsId", entity.get("building_id"));
        redirectAttributes.addAttribute("buildingsName", entity.get("buildingsName"));
        
        return "redirect:/buildings/house/list";
    }
    @RequestMapping(value="update")
    public String update(@RequestParam Map<String, Object> entity,
                         RedirectAttributes redirectAttributes) {
    	buildingsHouseService.update(entity);
        redirectAttributes.addFlashAttribute("success", "修改户型成功");
        redirectAttributes.addAttribute("buildingsId", entity.get("buildingsId"));
       redirectAttributes.addAttribute("buildingsName", entity.get("buildingsName"));
        return "redirect:/buildings/house/list?buildingsId="+entity.get("buildingsId");
    }
    @RequestMapping({"edit"})
    public void createOrEdit(@RequestParam Map<String, Object> filter,Model model) {

        if (filter.get("id") != null ) {

        	Long id = Long.parseLong(filter.get("id").toString());
            model.addAttribute("entity", buildingsHouseService.get(id));
       	 model.addAttribute("buildingsId",filter.get("id"));
       	 model.addAttribute("buildingsName",filter.get("buildingsName"));


        }else{
          	 model.addAttribute("building_id",filter.get("id"));
           	 model.addAttribute("buildingsName",filter.get("buildingsName"));
        }
    }
    @RequestMapping({"add"})
    public void create(@RequestParam Map<String, Object> filter,Model model) {

        if (filter.get("bid") != null ) {

        //	Long id = Long.parseLong(filter.get("bid").toString());
         //   model.addAttribute("entity", buildingsHouseService.get(id));
       	 model.addAttribute("building_id",filter.get("bid"));
       	 model.addAttribute("buildingsName",filter.get("buildingsName"));


        }else{
          	 model.addAttribute("buildingsId",filter.get("id"));
           	 model.addAttribute("buildingsName",filter.get("buildingsName"));
        }
    }
    @RequestMapping({"hxtEdit"})
    public void hxtEdit(@RequestParam Map<String, Object> filter,Model model) {

        if (filter.get("id") != null && filter.get("buildingsName") != null) {

       	model.addAttribute("buildingsName",filter.get("buildingsName").toString());
    	model.addAttribute("id",filter.get("id").toString());
    	model.addAttribute("shi",filter.get("shi").toString());
    	model.addAttribute("buildings_id",filter.get("buildings_id").toString());
//    	Map<String, Object> hxt = buildingsHouseService.getHuxingtuById(Long.parseLong(filter.get("id").toString()));
//    	if(null != hxt) {
//    		String resource_path = (String) hxt.get("resource_path");
//    		String resource_name = (String) hxt.get("resource_name");
//    		String hxtSrc = resource_path + resource_name;
//    		model.addAttribute("hxtSrc",hxtSrc);
//    	}
    	//model.addAttribute("hxtsrc","");
        }else{
        	model.addAttribute("buildings_id",filter.get("buildings_id").toString());
        	 model.addAttribute("buildingsName",filter.get("buildingsName").toString());
        }
    }
    @ResponseBody
    @RequestMapping({"hxtInsert"})
    public void hxtInsert(HttpServletRequest request) throws Exception {

    		buildingsHouseService.insertHuxingtu(request);   
    }
    
    @RequestMapping({"picEdit"})
    public void picEdit(@RequestParam Map<String, Object> filter,Model model) {

        if (filter.get("id") != null && filter.get("buildingsName") != null) {

       	model.addAttribute("buildingsName",filter.get("buildingsName").toString());
    	model.addAttribute("id",filter.get("id").toString());
       	model.addAttribute("buildings_id",filter.get("buildings_id").toString());
//      查询图片 展示出来  有问题暂时注释
//    	Map<String, Object> hxt = buildingsHouseService.getHuxingtuById(Long.parseLong(filter.get("id").toString()));
//    	if(null != hxt) {
//    		String resource_path = (String) hxt.get("resource_path");
//    		String resource_name = (String) hxt.get("resource_name");
//    		String hxtSrc = resource_path + resource_name;
//    		model.addAttribute("hxtSrc",hxtSrc);
//    	}
        }else{
            	model.addAttribute("buildings_id",filter.get("buildings_id").toString());
        	 model.addAttribute("buildingsName",filter.get("buildingsName").toString());
        }
    }
    @ResponseBody
    @RequestMapping({"picInsert"})
    public void picInsert(HttpServletRequest request) throws Exception {

    		buildingsHouseService.insertPicture(request);   
    }
}
