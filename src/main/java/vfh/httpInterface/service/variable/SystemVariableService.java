package vfh.httpInterface.service.variable;

import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.commons.valid.annotation.MapValid;
import vfh.httpInterface.dao.variable.DataDictionaryDao;
import vfh.httpInterface.dao.variable.DictionaryCategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 系统变量业务逻辑
 *
 * @author maurice
 */
@Service
@Transactional
@SuppressWarnings("SpringJavaAutowiringInspection")
public class SystemVariableService {

    @Autowired
    private DataDictionaryDao dataDictionaryDao;

    @Autowired
    private DictionaryCategoryDao dictionaryCategoryDao;

    //----------------------------------- 数据字典管理 ----------------------------------------//

    /**
     * 获取数据字典
     *
     * @param id 数据字典主键 ID
     *
     * @return 数据字典 Map 实体
     */
    public Map<String, Object> getDataDictionary(Long id) {
        return dataDictionaryDao.get(id);
    }

    /**
     * 获取数据字典
     *
     * @param code 字典类别代码
     *
     * @return 数据字典 Map 实体集合
     */
    @Cacheable(value="dataDictionaryCache",key="#code")
    public List<Map<String, Object>> getDataDictionaries(String code) {
        return dataDictionaryDao.getByCode(code);
    }

    /**
     * 删除数据字典
     *
     * @param ids 数据字典主键 ID
     */
    @CacheEvict(value="dataDictionaryCache",allEntries=true)
    public void deleteDataDictionaries(List<Long> ids) {
        for (Long id : ids) {
            dataDictionaryDao.delete(id);
        }
    }

    /**
     * 保存数据字典
     *
     * @param entity 数据字典 Map 实体
     */
    @CacheEvict(value="dataDictionaryCache",allEntries=true)
    public void saveDataDictionary(@MapValid("data-dictionary")Map<String, Object> entity) {
        if (entity.containsKey("id")) {
            dataDictionaryDao.update(entity);
        } else {
            dataDictionaryDao.insert(entity);
        }
    }

    /**
     * 查询数据字典
     *
     * @param filter 查询条件
     *
     * @return 数据字典 Map 实体集合
     */
    public List<Map<String, Object>> findDataDictionaries(Map<String, Object> filter) {
        return dataDictionaryDao.find(filter);
    }

    /**
     * 查询数据字典
     *
     * @param pageRequest 分页请求参数
     * @param filter 查询条件
     *
     * @return 数据字典实体 Map 的分页对象
     */
    public Page<Map<String, Object>> findDataDictionaries(PageRequest pageRequest, Map<String, Object> filter) {
        long total = dataDictionaryDao.count(filter);
        filter.putAll(pageRequest.getMap());
        List<Map<String, Object>> content = findDataDictionaries(filter);
        return new Page<Map<String, Object>>(pageRequest,content,total);
    }

    //----------------------------------- 字典类别管理 ----------------------------------------//

    /**
     * 获取字典类别
     *
     * @param id 字典类别主键ID
     *
     * @return 字典类别 Map 实体
     */
    public Map<String, Object> getDictionaryCategory(Long id) {
        return dictionaryCategoryDao.get(id);
    }

    /**
     * 获取所有字典类别
     *
     * @param ignore 忽略的 ID 值
     *
     * @return 字典类别 Map 实体集合
     */
    public List<Map<String, Object>> getAllDictionaryCategories(Long... ignore) {
        return dictionaryCategoryDao.getAll(ignore);
    }

    /**
     * 保存字典类别
     *
     * @param entity 字典类别 Map 实体
     */
    public void saveDictionaryCategory(@MapValid("dictionary-category")Map<String, Object> entity) {
        if (entity.containsKey("id")) {
            dictionaryCategoryDao.update(entity);
        } else {
            dictionaryCategoryDao.insert(entity);
        }
    }

    /**
     * 删除字典类别
     *
     * @param ids 字典类别主键 ID 集合
     */
    @CacheEvict(value="dataDictionaryCache",allEntries=true)
    public void deleteDictionaryCategories(List<Long> ids) {
        for (Long id : ids) {
            dictionaryCategoryDao.deleteDataDictionaryAssociation(id);
            dictionaryCategoryDao.delete(id);
        }
    }


    /**
     * 查询字典类别
     *
     * @param filter 查询条件
     *
     * @return 字典类别实体 Map 集合
     */
    public List<Map<String, Object>> findDictionaryCategories(Map<String, Object> filter) {
        return dictionaryCategoryDao.find(filter);
    }

    /**
     * 查询字典类别
     *
     * @param pageRequest 分页请求参数
     * @param filter 查询条件
     *
     * @return 字典类别实体 Map 的分页对象
     */
    public Page<Map<String, Object>> findDictionaryCategories(PageRequest pageRequest, Map<String, Object> filter) {
        long total = dictionaryCategoryDao.count(filter);
        filter.putAll(pageRequest.getMap());
        List<Map<String, Object>> content = findDictionaryCategories(filter);
        return new Page<Map<String, Object>>(pageRequest,content,total);
    }

}
