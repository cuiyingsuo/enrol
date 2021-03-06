package com.itcast.enrol.common.utils;

import com.itcast.enrol.common.exception.BusinessException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by liuzhongshuai on 2017/12/22.
 */
public class JsonUtil {


    private final static BusLogUtil LOGGER = new BusLogUtil(HttpUtil.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * json 格式字符串 转 对象
     *
     * @param jsonStr
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T jsonToObject(String jsonStr, Class<T> tClass) {

        if (StringUtils.isEmpty(jsonStr)) {
            return null;
        }
        try {
            return mapper.readValue(jsonStr, tClass);
        } catch (IOException e) {
            LOGGER.error(String.format("json=[%s]", jsonStr), e);
            throw new BusinessException("解析json 异常!");
        }
    }

    /**
     * 将对象转化为json
     *
     * @param obj 待转化的对象
     * @return 当转化发生异常时返回null
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            LOGGER.error(String.format("obj=[%s]", obj.toString()), e);
        }
        return null;
    }


    /**
     * 将json对象转化为集合类型
     *
     * @param json            json对象
     * @param collectionClazz 具体的集合类的class，如：ArrayList.class
     * @param clazz           集合内存放的对象的class
     * @return
     * @author yangwenkui
     * @time 2017年3月16日 下午2:57:15
     */
    @SuppressWarnings("rawtypes")
    public static <T> Collection<T> fromJson(String json, Class<? extends Collection> collectionClazz, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        try {
            Collection<T> collection = mapper.readValue(json, getCollectionType(collectionClazz, clazz));
            return collection;
        } catch (IOException e) {
            LOGGER.error(String.format("json=[%s]", json), e);
        }
        return null;
    }

    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }


}
