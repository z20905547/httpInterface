package vfh.httpInterface.service.account;

import com.google.common.collect.Lists;
import vfh.httpInterface.commons.SessionVariable;
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.commons.enumeration.entity.ResourceType;
import vfh.httpInterface.service.ServiceException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 公用授权抽象类，用于 apache shrio 在执行授权操作时，通过该类去统一对本系统的功能进行授权。
 * 
 * @author maurice
 *
 */
public abstract class AuthorizationRealm extends AuthorizingRealm{

	@Autowired
	private AccountService accountService;
	
	private List<String> defaultPermission = Lists.newArrayList();
	
	/**
	 * 设置默认的 permission
	 * 
	 * @param defaultPermissionString permission 如果存在多个值，使用逗号","分割
	 */
	public void setDefaultPermissionString(String defaultPermissionString) {
		String[] perms = StringUtils.split(defaultPermissionString,",");
		CollectionUtils.addAll(defaultPermission, perms);
	}
	
	/**
	 * 设置默认的 permission
	 * 
	 * @param defaultPermission permission 集合
	 */
	public void setDefaultPermission(List<String> defaultPermission) {
		this.defaultPermission = defaultPermission;
	}
	
	/**
	 * 
	 * 当用户进行访问链接时的授权方法
	 * 
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        SessionVariable sv = principals.oneByType(SessionVariable.class);

        if (sv == null) {
            throw new ServiceException("session 变量对象不存在");
        }

        Map<String, Object> currentUser = sv.getUser();
        
        Long id = VariableUtils.typeCast(currentUser.get("id"), Long.class);
        
        //加载用户资源信息
        List<Map<String, Object>> authorizationInfo = accountService.getUserResources(id);
        List<Map<String, Object>> menuList = accountService.mergeResources(authorizationInfo, ResourceType.SECURITY);
        
        //添加用户拥有的permission
        addPermissions(info,authorizationInfo);

        sv.setAuthorizationInfo(authorizationInfo);
        sv.setMenusList(menuList);

        SecurityUtils.getSubject().getSession().setAttribute(SessionVariable.DEFAULT_SESSION_KEY, sv);

        return info;
	}
	
	/**
	 * 通过资源集合，将集合中的 permission 字段内容解析后添加到 SimpleAuthorizationInfo 授权信息中
	 * 
	 * @param info SimpleAuthorizationInfo
	 * @param authorizationInfo 资源集合
	 */
	private void addPermissions(SimpleAuthorizationInfo info, List<Map<String, Object>> authorizationInfo) {

        List<String> permissions = Lists.newArrayList();
       
        //添加默认的permissions到permissions
        if (CollectionUtils.isNotEmpty(defaultPermission)) {
        	CollectionUtils.addAll(permissions, defaultPermission.iterator());
        }

        //解析当前用户资源中的permissions
        for (Map<String, Object> m : authorizationInfo) {
            Object permission = m.get("permission");

            if (permission == null || StringUtils.isEmpty(permission.toString())) {
                continue;
            }

            permissions.add(StringUtils.substringBetween(permission.toString(), "perms[", "]"));
        }
        
        //将当前用户拥有的permissions设置到SimpleAuthorizationInfo中
        info.addStringPermissions(permissions);
		
	}
}
