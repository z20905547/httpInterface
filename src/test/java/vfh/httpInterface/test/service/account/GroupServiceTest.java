package vfh.httpInterface.test.service.account;

import com.google.common.collect.Lists;
import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.service.account.AccountService;
import vfh.httpInterface.test.service.ServiceTestCaseSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * 组业务逻辑测试类
 */
public class GroupServiceTest extends ServiceTestCaseSupport {

    @Autowired
    private AccountService accountService;

    @Test
    public void testGetGroup() {
        Map<String, Object> group = accountService.getGroup(1L);

        assertEquals(group.get("id"), 1L);
        assertEquals(group.get("name"), "普通用户");

        group = accountService.getGroup(99L);
        assertNull(group);
    }

    @Test
    public void testGetUserGroups(){
        List<Map<String, Object>> list = accountService.getUserGroups(5L);
        assertEquals(list.size(), 1);

        list = accountService.getUserGroups(99L);
        assertEquals(list.size(), 0);
    }

    @Test
    public void testDeleteGroups() {
        int before = countRowsInTable("tb_group");
        int beforeResourceAssociation = countRowsInTable("tb_group_resource");
        int beforeGroupAssociation = countRowsInTable("tb_group_user");

        accountService.deleteGroups(Lists.newArrayList(1L, 2L));

        int after = countRowsInTable("tb_group");
        int afterResourceAssociation = countRowsInTable("tb_group_resource");
        int afterGroupAssociation = countRowsInTable("tb_group_user");

        assertEquals(before - 2, after);
        assertEquals(beforeResourceAssociation - 21, afterResourceAssociation);
        assertEquals(beforeGroupAssociation - 2, afterGroupAssociation);
    }

    @Test
    public void testSaveGroup() {

        Map<String, Object> group = new HashMap<String, Object>();

        group.put("name", "new group");
        group.put("remark", "test group");

        int before = countRowsInTable("tb_group");
        int beforeResourceAssociation = countRowsInTable("tb_group_resource");

        accountService.saveGroup(group, Lists.newArrayList(1L,2L,3L,4L));

        int after = countRowsInTable("tb_group");
        int afterResourceAssociation = countRowsInTable("tb_group_resource");

        assertEquals(before + 1, after);
        assertEquals(beforeResourceAssociation + 4, afterResourceAssociation);

        Long id = (Long)group.get("id");

        group.put("name","modify name");

        beforeResourceAssociation = countRowsInTable("tb_group_resource");
        before = countRowsInTable("tb_group");

        accountService.saveGroup(group, Lists.<Long>newArrayList());

        after = countRowsInTable("tb_group");
        afterResourceAssociation = countRowsInTable("tb_group_resource");

        assertEquals(before, after);
        assertEquals(beforeResourceAssociation - 4, afterResourceAssociation);

        group = accountService.getGroup(id);

        assertEquals(group.get("name"), "modify name");

    }

    @Test
    public void testFindGroup() {
        Map<String, Object> filter = new HashMap<String, Object>();

        filter.put("name", "超级");

        List<Map<String, Object>> list = accountService.findGroups(filter);

        assertEquals(list.size(),1);
    }

    @Test
    public void testFindGroupPage() {
        Map<String, Object> filter = new HashMap<String, Object>();

        filter.put("name", "超级");

        Page<Map<String, Object>> page = accountService.findGroups(new PageRequest(0, 1), filter);

        assertEquals(page.getTotalElements(),1);
        assertEquals(page.hasNext(), false);
        assertEquals(page.hasContent(), true);
        assertEquals(page.getTotalPages(), 1);
        assertEquals(page.isFirst(), true);
    }
}