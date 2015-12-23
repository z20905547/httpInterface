package vfh.httpInterface.service.account;

import vfh.httpInterface.commons.SessionVariable;
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.commons.enumeration.entity.State;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 认证（登录）类，用于 apache shiro 在执行认证（登录）时，通过该类对登录信息认证（登录）是否通过。
 *
 * @author maurice
 */
public class JdbcAuthenticationRealm extends AuthorizationRealm {

    @Autowired
    private AccountService accountService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

        String username = usernamePasswordToken.getUsername();

        Map<String, Object> user = accountService.getUserByUsernameOrEmail(username);

        if (MapUtils.isEmpty(user)) {
            throw new IncorrectCredentialsException();
        }

        if (State.DISABLE.getValue().equals(VariableUtils.typeCast(user.get("state"), Integer.class))) {
            throw new DisabledAccountException();
        }

        SessionVariable model = new SessionVariable(user);

        return new SimpleAuthenticationInfo(model,user.get("password"),getName());
    }

}
