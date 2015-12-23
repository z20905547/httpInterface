package vfh.httpInterface.test.service.variable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.service.variable.SystemVariableService;
import vfh.httpInterface.test.service.ServiceTestCaseSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * 字典类别业务逻辑测试类
 */
public class DictionaryCategoryServiceTest extends ServiceTestCaseSupport {

    @Autowired
    private SystemVariableService systemVariableService;

    @Test
    public void testGetDictionaryCategory() {
        Map<String, Object> dictionaryCategory = systemVariableService.getDictionaryCategory(1L);
        assertEquals(dictionaryCategory.get("code"),"state");

        dictionaryCategory = systemVariableService.getDictionaryCategory(99L);
        assertNull(dictionaryCategory);
    }

    @Test
    public void testGetAllDictionaryCategories() {
        List<Map<String, Object>> list = systemVariableService.getAllDictionaryCategories();
        assertEquals(list.size(), 2);

        list = systemVariableService.getAllDictionaryCategories(1L);
        assertEquals(list.size(), 1);

        list = systemVariableService.getAllDictionaryCategories(99L);
        assertEquals(list.size(), 2);
    }

    @Test
    public void testSaveDictionaryCategory() {
        Map<String, Object> dictionaryCategory = systemVariableService.getDictionaryCategory(1L);

        dictionaryCategory.remove("id");
        dictionaryCategory.put("name","国籍");
        dictionaryCategory.put("code","nationality");

        int before = countRowsInTable("tb_dictionary_category");

        systemVariableService.saveDictionaryCategory(dictionaryCategory);

        int after = countRowsInTable("tb_dictionary_category");

        assertEquals(before + 1, after);
        assertEquals(dictionaryCategory.get("id"), 3L);

        dictionaryCategory.put("name","籍贯");
        dictionaryCategory.put("code","native-place");

        before = countRowsInTable("tb_dictionary_category");

        systemVariableService.saveDictionaryCategory(dictionaryCategory);

        after = countRowsInTable("tb_dictionary_category");

        assertEquals(before, after);

        systemVariableService.getDictionaryCategory(3L);

        assertEquals(dictionaryCategory.get("name"), "籍贯");
        assertEquals(dictionaryCategory.get("code"), "native-place");
    }

    @Test
    public void testDeleteDictionaryCategories() {

        int before = countRowsInTable("tb_dictionary_category");
        int beforeAssociation = countRowsInTable("tb_data_dictionary");

        systemVariableService.deleteDictionaryCategories(Lists.newArrayList(1L));

        int after = countRowsInTable("tb_dictionary_category");
        int afterAssociation = countRowsInTable("tb_data_dictionary");

        assertEquals(before - 1, after);
        assertEquals(beforeAssociation - 3, afterAssociation);

    }

    @Test
    public void testFindDictionaryCategories() {
        Map<String, Object> filter = Maps.newHashMap();

        List<Map<String, Object>> list = systemVariableService.findDictionaryCategories(filter);

        assertEquals(list.size(), 2);

        filter.put("name","资源");

        list = systemVariableService.findDictionaryCategories(filter);

        assertEquals(list.size(), 1);

    }

    @Test
    public void testFindDictionaryCategoriesPage() {

        PageRequest pageRequest = new PageRequest(0,1);

        Map<String, Object> filter = Maps.newHashMap();
        filter.put("name","资源");

        Page<Map<String, Object>> page =  systemVariableService.findDataDictionaries(pageRequest, filter);

        assertEquals(page.getTotalElements(),1);
        assertEquals(page.hasNext(), false);
        assertEquals(page.hasContent(), true);
        assertEquals(page.getTotalPages(), 1);
        assertEquals(page.isFirst(), true);
    }
}