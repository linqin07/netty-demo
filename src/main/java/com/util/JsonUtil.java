package com.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @Description:
 * @author: LinQin
 * @date: 2019/02/23
 */
public class JsonUtil {
    private JsonUtil() {
    }

    public static class SingletonHolder {
        public static final ObjectMapper MAPPER;

        static {
            MAPPER = new ObjectMapper();
            // 设置日期对象的输出格式
            MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE));
            // 设置输入是忽略在json字符串中存在但java对象实际没有属性
            MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        }

        private static ObjectMapper getMapper() {
            return SingletonHolder.MAPPER;
        }

        /**
         * 将对象转化为json字符串
         *
         * @param pojo
         * @return
         */
        public static String toJsonString(Object pojo) {
            if (pojo == null) {
                return null;
            }
            try {
                return getMapper().writeValueAsString(pojo);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * 将字符串转化json对象
         *
         * @param input
         * @return
         */
        public static JsonNode parseJson(String input) {
            if (input == null) {
                return null;
            }
            try {
                return getMapper().readTree(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static JsonNode getJsonNodefromStream(InputStream in) {
            try {
                return getMapper().readTree(in);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        /**
         * 将json字符串转化为java对象，只支持返回简单对象（非集合类）
         *
         * @param jsonString
         * @param valueType
         * @param <T>
         * @return
         */
        public static <T> T jsonToObject(String jsonString, Class<T> valueType) {
            if (StringUtils.isNotEmpty(jsonString)) {
                try {
                    return getMapper().readValue(jsonString, valueType);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        /**
         * 将json字符串转换为集合类型 LIst map等
         * @param jsonStr json字符串
         * @param collectionClass 集合类型 如List.class
         * @param elementClass 集合内bean类型，String.class
         * @param <T>
         * @return
         */
        public static <T> T jsonToObject(String jsonStr, Class<?> collectionClass, Class<?>... elementClass) {
            if (!StringUtils.isNotEmpty(jsonStr)) {
                return null;
            }
            JavaType javaType = getMapper().getTypeFactory().constructParametrizedType(collectionClass, collectionClass, elementClass);
            try {
                return getMapper().readValue(jsonStr, javaType);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * 创建一个空的json对象
         * @return
         */
        public static ObjectNode createObjectNode() {
            return getMapper().createObjectNode();
        }

        /**
         * 创建一个空的json数组对象
         * @return
         */
        public static ObjectNode createArrayNode() {
            return getMapper().createObjectNode();
        }

        // public static <T> T convert(Object pojo, Class<T> target) {
        //     if (pojo == null) {
        //         return null;
        //     }
        //     return getMapper().
        // }
    }
}
