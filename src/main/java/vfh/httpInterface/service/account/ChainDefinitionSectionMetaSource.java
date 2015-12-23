package vfh.httpInterface.service.account;

import vfh.httpInterface.commons.VariableUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


/**
 * 借助spring {@link org.springframework.beans.factory.FactoryBean} 对apache shiro的premission进行动态创建
 * 
 * @author maurice
 *
 */
public class ChainDefinitionSectionMetaSource implements FactoryBean<Section>{

	@Autowired
	private AccountService accountService;
	
	//shiro默认的链接定义
	private String filterChainDefinitions;
	
	/**
	 * 通过filterChainDefinitions对默认的链接过滤定义
	 * 
	 * @param filterChainDefinitions 默认的接过滤定义
	 */
	public void setFilterChainDefinitions(String filterChainDefinitions) {
		this.filterChainDefinitions = filterChainDefinitions;
	}
	
	@Override
	public Section getObject() throws BeansException {
		Ini ini = new Ini();
        //加载默认的url
        ini.load(filterChainDefinitions);
        
        Section section = ini.getSection(IniFilterChainResolverFactory.URLS);
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        }
        List<Map<String, Object>> resources = accountService.getResources();

        //循环数据库资源的url
        for (Map<String, Object> map : resources) {

            String value = VariableUtils.typeCast(map.get("value"), String.class);
            String permission = VariableUtils.typeCast(map.get("permission"), String.class);

        	if(StringUtils.isNotEmpty(value) && StringUtils.isNotEmpty(permission)) {
        		section.put(value, permission);
        	}
        }
        
        return section;
	}
	
	@Override
	public Class<Section> getObjectType() {
		return Section.class;
	}
	
	@Override
	public boolean isSingleton() {
		return true;
	}

}
