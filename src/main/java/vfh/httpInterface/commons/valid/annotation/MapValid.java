package vfh.httpInterface.commons.valid.annotation;

import java.lang.annotation.*;

/**
 * Map 对象 校验注解
 *
 * @author maurice
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapValid {

    /**
     * xml 文件映射名称
     *
     * @return 名称
     */
    String value();
}
