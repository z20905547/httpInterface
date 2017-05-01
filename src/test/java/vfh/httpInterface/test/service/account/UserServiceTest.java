package vfh.httpInterface.test.service.account;

import com.google.common.collect.Lists;
import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.commons.enumeration.entity.State;
import vfh.httpInterface.service.ServiceException;
import vfh.httpInterface.service.account.AccountService;
import vfh.httpInterface.test.service.ServiceTestCaseSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * 用户业务逻辑测试类
 */
public class UserServiceTest extends ServiceTestCaseSupport{

    @Autowired
    private AccountService accountService;

    @Test
    public void testGetUser() {
        Map<String, Object> user = accountService.getUser(5L);

        assertEquals(user.get("id"), 5L);
        assertEquals(user.get("username"), "admin");

        user = accountService.getUser(10L);
        assertNull(user);
    }

    @Test(expected = ServiceException.class)
    public void testInsertUser() {
        Map<String, Object> user = accountService.getUser(5L);

        user.put("username", "chenxiaobo");
        user.put("email", "dci.maurice@gmail.com");
        user.remove("id");

        int before = countRowsInTable("tb_user");

    //    accountService.insertUser(user, null);

        int after = countRowsInTable("tb_user");

        assertEquals(before + 1, after);

        user.put("username", "cxb.com");
        user.put("email", "chenxiaobo@gmail.com");
        user.remove("id");

        int beforeAssociation = countRowsInTable("tb_group_user");

    //    accountService.insertUser(user,Lists.newArrayList(1L));

        int afterAssociation = countRowsInTable("tb_group_user");

        assertEquals(beforeAssociation + 1, afterAssociation);
        assertEquals(after + 1, countRowsInTable("tb_user"));

        user.put("username", "chenxiaobo");

  //      accountService.insertUser(user,null);

    }

    @Test
    public void testUpdateUser() {
        Map<String, Object> user = accountService.getUser(5L);

        String password = user.get("password").toString();
        String username = user.get("username").toString();

        user.put("password", "123456");
        user.put("nickname", "小苹果");
        user.put("username", "chenxiaobo");

 //       accountService.updateUser(user, Lists.newArrayList(1L));

        user = accountService.getUser(5L);

        assertEquals(user.get("nickname"), "小苹果");
        assertEquals(user.get("username"), username);
        assertEquals(user.get("password"), password);
        assertEquals(user.get("state"), State.ENABLE.getValue());
    }

    @Test(expected = ServiceException.class)
    public void testUpdateUserPassword() {
        Map<String, Object> user = accountService.getUser(5L);

        accountService.updateUserPassword(user, "123456", "admin");

        user = accountService.getUser(5L);

        assertEquals(user.get("password"), "21232f297a57a5a743894a0e4a801fc3");

        accountService.updateUserPassword(user, "admin", null);
        accountService.updateUserPassword(user, null, null);
    }

    @Test
    public void testDeleteUsers() {

        int before = countRowsInTable("tb_user");
        int beforeAssociation = countRowsInTable("tb_group_user");

        accountService.deleteUsers(Lists.newArrayList(3L,4L));

        int after = countRowsInTable("tb_user");
        int aftereAssociation = countRowsInTable("tb_group_user");

        assertEquals(before - 2, after);
        assertEquals(beforeAssociation - 2, aftereAssociation);

        accountService.deleteUsers(Lists.newArrayList(99L));

        assertEquals(countRowsInTable("tb_user"), after);
        assertEquals(countRowsInTable("tb_group_user"), aftereAssociation);
    }

    @Test
    public void testGetUserByUsernameOrEmail() {

        Map<String, Object> user = accountService.getUserByUsernameOrEmail("admin");
        assertEquals(user.get("id"), 5L);

        user = accountService.getUserByUsernameOrEmail("administrator@baseframework.com");
        assertEquals(user.get("id"), 5L);

        user = accountService.getUserByUsernameOrEmail("XXXXX");

        assertNull(user);
    }

    @Test
    public void testIsUsernameUnique() {
        assertFalse(accountService.isUsernameUnique("admin"));
        assertTrue(accountService.isUsernameUnique("maurice"));
    }

    @Test
    public void testIsEmailUnique() {
        assertFalse(accountService.isEmailUnique("administrator@baseframework.com"));
        assertTrue(accountService.isEmailUnique("maurice@baseframework.com"));
    }

    @Test
    public void testFindUser() {
        Map<String, Object> filter = new HashMap<String, Object>();
        List<Map<String,Object>> list;

        filter.put("username", "a");
        list = accountService.findUsers(filter);
        assertEquals(list.size(), 2);

        filter.put("state", 2);
        list = accountService.findUsers(filter);
        assertEquals(list.size(), 0);

        filter.put("state", 1);
        filter.put("email", "admin");
        list = accountService.findUsers(filter);
        assertEquals(list.size(), 1);

        filter.put("nickname", "超级");
        list = accountService.findUsers(filter);
        assertEquals(list.size(), 1);
    }

    @Test
    public void testFindUserPage() {

        Map<String, Object> filter = new HashMap<String, Object>();

        filter.put("username", "a");
        filter.put("state", 1);
        filter.put("email", "admin");
        filter.put("nickname", "超级");

        Page<Map<String,Object>> page = accountService.findUsers(new PageRequest(0, 2), filter);

        assertEquals(page.getTotalElements(),1);
        assertEquals(page.hasNext(), false);
        assertEquals(page.hasContent(), true);
        assertEquals(page.getTotalPages(), 1);
        assertEquals(page.isFirst(), true);

    }
}