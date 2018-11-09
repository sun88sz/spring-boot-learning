package com.sun.constant;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : Sun
 * @date : 2018/11/6 11:55
 */

@Service
public class ConstantServiceUtil implements ConstantService, InitializingBean {

    static Logger log = LoggerFactory.getLogger(ConstantServiceUtil.class);

    @Autowired
    private ConstantDao constantDao;

    /**
     * 数据中的配置 缓存在map中
     */
    public final static Map<String, String> constMap = new HashMap<>();

    /**
     * 待刷新的常量类
     */
    private final static Set<Class> constClasses = new HashSet<>();

    /**
     * 更新配置
     */
    @Override
    public synchronized boolean refreshConfig() {
        List<ConstantPo> constantPos = constantDao.selectAll();

        if (CollectionUtils.isNotEmpty(constantPos)) {
            // 重置map
            constMap.clear();
            constantPos.stream().forEach(c -> constMap.put(c.getKey(), c.getValue()));

            // 刷新 刷新过的常量类
            if (CollectionUtils.isNotEmpty(constClasses)) {
                constClasses.stream().forEach(ConstantServiceUtil::initConstantClass);
            }
        }

        return true;
    }


    /**
     * 实例化后 立即初始化配置类
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        refreshConfig();
    }

    /**
     * 初始化一个常量类
     *
     * @param clazz
     */
    public static void initConstantClass(Class clazz) {

        constClasses.add(clazz);

        if (MapUtils.isNotEmpty(constMap)) {
            Field[] fields = clazz.getDeclaredFields();

            if (ArrayUtils.isNotEmpty(fields)) {
                Arrays.stream(fields).forEach(
                        f -> {
                            String fieldName = f.getName();
                            // 只修改静态字段
                            boolean isStatic = Modifier.isStatic(f.getModifiers());

                            Class<?> typeClazz = f.getType();
                            if (isStatic) {
                                try {
                                    // 如果有字段名和配置的字段名相同
                                    if (constMap.keySet().contains(fieldName)) {
                                        Object field = getValue(f.getName(), typeClazz);
                                        f.set(clazz, field);
                                    }
                                } catch (IllegalAccessException e) {
                                    log.info("初始化配置常量错误: {}", e.getMessage());
                                }
                            }
                        }
                );
            }
        }
    }

    /**
     * 根据key获取 数据库中配置的value
     *
     * @param key
     * @return
     */
    public static String getValue(String key) {
        return constMap.get(key);
    }

    /**
     * 根据key获取 数据库中配置的value
     *
     * @param key
     * @param clazz 类型
     * @param <T>   泛型
     * @return 泛型的值
     */
    public static <T> T getValue(String key, Class<T> clazz) {
        String value = constMap.get(key);

        if (StringUtils.isNotEmpty(value)) {
            if (clazz == String.class) {
                return (T) value;
            } else if (clazz == Boolean.class || clazz == boolean.class) {
                return (T) Boolean.valueOf(value);
            } else if (clazz == Integer.class || clazz == int.class) {
                return (T) Integer.valueOf(value);
            } else if (clazz == Long.class || clazz == long.class) {
                return (T) Long.valueOf(value);
            } else if (clazz == Float.class || clazz == float.class) {
                return (T) Float.valueOf(value);
            } else if (clazz == Double.class || clazz == double.class) {
                return (T) Double.valueOf(value);
            } else if (clazz == BigDecimal.class) {
                return (T) new BigDecimal(value);
            }
            return (T) value;
        }
        return null;
    }

}
