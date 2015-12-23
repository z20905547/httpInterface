package vfh.httpInterface.commons.valid;

import vfh.httpInterface.commons.valid.annotation.MapValid;
import net.coding.chenxiaobo.map.validation.MapValidation;
import net.coding.chenxiaobo.map.validation.ValidError;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * 由于 spring mvc 的 argument resolver 已经无法完成 map 的验证，所以，使用 aop 的形式去执行 map 验证。
 *
 * @author maurice
 */
@Aspect
public class MapValidManager {

    @Autowired
    private MapValidation mapValidation;

    @Before("execution(* vfh.httpInterface.service.*..*(..))")
    public void logBefore(JoinPoint joinPoint) throws BindException {

        if (!(joinPoint.getSignature() instanceof MethodSignature)) {
            return ;
        }

        Object[] args = joinPoint.getArgs();

        if (args.length == 0) {
            return ;
        }
        // 循环参数逐个去判断是否存在 MapEntity 的注解
        for (int i = 0; i < args.length; i++) {

            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Annotation[][] annotations = methodSignature.getMethod().getParameterAnnotations();

            MapValid mapValid = getMapValidAnnotation(annotations, i);

            Object o = args[i];

            if (mapValid == null || o == null) {
                continue;
            }

            // 如果参数里存在 MapEntity 并且该参数是 Map 的子类，就进行校验
            if (Map.class.isAssignableFrom(o.getClass())) {
                valid((Map<String, Object>) o,mapValid.value());
            }

        }

    }

    /**
     * 获取 MapValid 注解
     *
     * @param annotations 注解列表
     * @param index 索引位
     *
     * @return MapValid 注解
     */
    private MapValid getMapValidAnnotation(Annotation[][] annotations, int index) {

        for (Annotation annotation : annotations[index]) {
            if (MapValid.class.isInstance(annotation)) {
                return (MapValid) annotation;
            }
        }

        return null;
    }

    /**
     * 校验 map
     *
     * @param map map 对象
     * @param mapperName 交配文件的映射名称
     *
     * @throws BindException
     */
    private void valid(Map<String, Object> map, String mapperName) throws BindException {
        List<ValidError> errorList = mapValidation.valid(map,mapperName);

        if (errorList.isEmpty()) {
            return ;
        }

        MapBindingResult mapBindingResult = new MapBindingResult(map, mapperName);

        for (ValidError ve : errorList) {
            mapBindingResult.addError(new ObjectError(ve.getName(),ve.getMessage()));
        }

        throw new BindException(mapBindingResult);
    }

}
