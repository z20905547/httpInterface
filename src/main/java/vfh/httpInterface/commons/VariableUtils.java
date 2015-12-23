package vfh.httpInterface.commons;

import com.google.common.collect.Lists;
import vfh.httpInterface.commons.enumeration.FieldType;
import vfh.httpInterface.commons.enumeration.ValueEnum;
import vfh.httpInterface.service.variable.SystemVariableService;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 变量工具类，该类封装对系统变量的一些静态获取方法
 *
 * @author maurice
 */
@Component
public class VariableUtils {

    /**
     * 默认字典值的值名称
     */
    public static final String DEFAULT_VALUE_NAME = "value";
    /**
     * 默认字典值的键名称
     */
    public static final String DEFAULT_KEY_NAME = "key";

    // 系统变量业务逻辑
    private static SystemVariableService systemVariableService;

    /**
     * 设置系统变量业务逻辑
     *
     * @param systemVariableService 系统变量业务逻辑类
     */
    @Autowired
    public void setSystemVariableService(SystemVariableService systemVariableService) {
        VariableUtils.systemVariableService = systemVariableService;
    }

    /**
     * 通过{@link vfh.httpInterface.commons.enumeration.ValueEnum} 接口子类 class 获取数据字典集合
     *
     * @param enumClass 枚举 class
     * @param ignore 要忽略的值
     *
     * @return key value 数据字典 Map 集合
     */
    public static List<Map<String, Object>> getVariables(Class<? extends Enum<? extends ValueEnum<?>>> enumClass, Object... ignore) {

        List< Map<String, Object>> result = Lists.newArrayList();
        Enum<? extends ValueEnum<?>>[] values = enumClass.getEnumConstants();

        for (Enum<? extends ValueEnum<?>> o : values) {
            ValueEnum<?> ve = (ValueEnum<?>) o;
            Object value = ve.getValue();

            if(!ArrayUtils.contains(ignore, value)) {
                Map<String, Object> dictionary = new HashMap<String, Object>();

                dictionary.put(DEFAULT_VALUE_NAME, value);
                dictionary.put(DEFAULT_KEY_NAME, ve.getName());

                result.add(dictionary);
            }

        }

        return result;
    }

    /**
     * 通过字典类别获取数据字典集合
     *
     * @param code 字典类别代码
     * @param ignore 要忽略的值
     *
     * @return key value 数据字典 Map 集合
     */
    public static List<Map<String, Object>> getVariables(String code, Object... ignore) {
        List<Map<String, Object>> result = Lists.newArrayList();
        List<Map<String,Object>> dataDictionaries = systemVariableService.getDataDictionaries(code);

        for (Map<String, Object> data : dataDictionaries) {

            String value = typeCast(data.get("value"));

            if (!ArrayUtils.contains(ignore, value)) {
                continue;
            }

            String type = data.get("type").toString();

            Map<String, Object> dictionary = new HashMap<String, Object>();

            dictionary.put(DEFAULT_VALUE_NAME, data.get("name"));
            dictionary.put(DEFAULT_KEY_NAME, typeCast(value, type));
            result.add(data);

        }

        return result;
    }

    /**
     * 将值转换为对应的类型
     *
     * @param value 值
     *
     * @return 转换好的值
     */
    public static <T> T typeCast(Object value) {
        return (T)value;
    }

    /**
     * 将值转换为对应的类型
     *
     * @param value 值
     * @param typeClass 类型 Class
     *
     * @return 转换好的值
     */
    public static <T> T typeCast(Object value, Class<T> typeClass) {
        return (T)(value == null ? value : ConvertUtils.convert(value,typeClass));
    }

    /**
     * 将 String 类型的值转换为对应的类型
     *
     * @param value 值
     * @param className 类名称,参考{@link vfh.httpInterface.commons.enumeration.FieldType}
     *
     * @return 转换好的值
     */
    public static <T> T typeCast(String value, String className) {
        Class<?> type = FieldType.getClass(className);
        return (T) typeCast(value,type);
    }
}
