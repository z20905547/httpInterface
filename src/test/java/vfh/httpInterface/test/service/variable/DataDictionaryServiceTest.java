package vfh.httpInterface.test.service.variable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.test.service.ServiceTestCaseSupport;
import vfh.httpInterface.service.variable.SystemVariableService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

/**
 * 数据字典业务逻辑测试类
 */
public class DataDictionaryServiceTest extends ServiceTestCaseSupport {

    @Autowired
    private SystemVariableService systemVariableService;

    @Test
    public void testGetDataDictionary() {
        Map<String, Object> dataDictionary = systemVariableService.getDataDictionary(1L);
        assertEquals(dataDictionary.get("name"),"启用");

        dataDictionary = systemVariableService.getDataDictionary(99L);
        assertNull(dataDictionary);
    }

    @Test
    public void testGetDataDictionaries() {
        List<Map<String, Object>> dataDictionaries = systemVariableService.getDataDictionaries("state");
        assertEquals(dataDictionaries.size(), 3);

        dataDictionaries = systemVariableService.getDataDictionaries("xxx");
        assertEquals(dataDictionaries.size(), 0);
    }

    @Test
    public void testDeleteDataDictionaries() {

        int before = countRowsInTable("tb_data_dictionary");

        systemVariableService.deleteDataDictionaries(Lists.newArrayList(1L, 2L, 3L, 4L));

        int after = countRowsInTable("tb_data_dictionary");

        assertEquals(before - 3, after);
    }

    @Test
    public void testSaveDataDictionary() {
        Map<String, Object> dataDictionary = systemVariableService.getDataDictionary(1L);

        dataDictionary.remove("id");
        dataDictionary.put("name", "重置");
        dataDictionary.put("state", "reset");

        int before = countRowsInTable("tb_data_dictionary");
        systemVariableService.saveDataDictionary(dataDictionary);
        int after = countRowsInTable("tb_data_dictionary");

        assertEquals(before + 1, after);
        assertEquals(dataDictionary.get("id"), 12L);

        dataDictionary.put("name", "黑名单");
        dataDictionary.put("value", "4");

        before = countRowsInTable("tb_data_dictionary");
        systemVariableService.saveDataDictionary(dataDictionary);
        after = countRowsInTable("tb_data_dictionary");

        assertEquals(before, after);

        dataDictionary = systemVariableService.getDataDictionary(12L);

        assertEquals(dataDictionary.get("name"), "黑名单");
        assertEquals(dataDictionary.get("value"), "4");

    }

    @Test
    public void testFindDataDictionaries() {

        Map<String, Object> filter = Maps.newHashMap();

        List<Map<String, Object>> list = systemVariableService.findDataDictionaries(filter);

        assertEquals(list.size(), 5);

        filter.put("name", "类型");
        list = systemVariableService.findDataDictionaries(filter);
        assertEquals(list.size(), 2);

        filter.put("fk_category_id", 2L);
        list = systemVariableService.findDataDictionaries(filter);
        assertEquals(list.size(), 2);

        filter.put("value", "2");
        list = systemVariableService.findDataDictionaries(filter);
        assertEquals(list.size(), 1);
    }

    @Test
    public void testFindDataDictionariesPage() {
        Map<String, Object> filter = Maps.newHashMap();
        PageRequest pageRequest = new PageRequest(0, 1);

        filter.put("name", "类型");
        filter.put("fk_category_id", 2L);
        filter.put("value", "2");

        Page<Map<String, Object>> page = systemVariableService.findDataDictionaries(pageRequest, filter);

        assertEquals(page.getTotalElements(),1);
        assertEquals(page.hasNext(), false);
        assertEquals(page.hasContent(), true);
        assertEquals(page.getTotalPages(), 1);
        assertEquals(page.isFirst(), true);
    }
}