/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKObjectUtils.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 3:35:28 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

import com.alibaba.fastjson2.JSONObject;
import com.zk.core.utils.ZKClassUtils.Param_Name;

/** 
* @ClassName: ZKObjectUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKObjectUtils extends org.apache.commons.lang3.ObjectUtils {

//    /**
//     * 注解到对象复制，只复制能匹配上的方法。
//     * 
//     * @param annotation
//     * @param object
//     */
//    public static void annotationToObject(Object annotation, Object object) {
//        if (annotation != null) {
//            Class<?> annotationClass = annotation.getClass();
//            if (null == object) {
//                return;
//            }
//            Class<?> objectClass = object.getClass();
//            for (Method m : objectClass.getMethods()) {
//                if (StringUtils.startsWith(m.getName(), "set")) {
//                    try {
//                        String s = StringUtils.uncapitalize(StringUtils.substring(m.getName(), 3));
//                        Object obj = annotationClass.getMethod(s).invoke(annotation);
//                        if (obj != null && !"".equals(obj.toString())) {
//                            m.invoke(object, obj);
//                        }
//                    }
//                    catch(Exception e) {
//                        // 忽略所有设置失败方法
//                    }
//                }
//            }
//        }
//    }

    public static boolean isSerialize(byte[] bytes) {
        if (bytes != null && bytes.length > 1) {
            if ((bytes[0] & 0xac) == 0xac && (bytes[1] & 0xed) == 0xed) {
                return true;
            }
        }
        return false;
    }

    /**
     * 序列化对象
     * 
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            if (object != null) {
//                ObjectMapper mapper = new ObjectMapper(); 
//                mapper.writeValueAsBytes(object);

                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                return baos.toByteArray();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                ZKStreamUtils.closeStream(baos);
            }
            if (baos != null) {
                ZKStreamUtils.closeStream(baos);
            }
        }
        return null;
    }

    /**
     * 反序列化对象
     * 
     * @param bytes
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <V> V unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            if (bytes != null && bytes.length > 0) {
                bais = new ByteArrayInputStream(bytes);
                ois = new ObjectInputStream(bais);
                return (V) ois.readObject();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (bais != null) {
                ZKStreamUtils.closeStream(bais);
            }
            if (ois != null) {
                ZKStreamUtils.closeStream(ois);
            }
        }
        return null;
    }

    /**
     * 参数类型转换
     * 
     * @param parameterObject
     * @param objClass
     * @param fieldName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T converParameter(final Object parameterObject, Class<?> objClass, final String fieldName) {
        try {

            if (objClass.isInstance(parameterObject)) {
                return (T) parameterObject;
            }
            else {
                return (T) ZKClassUtils.getFieldValue(parameterObject, fieldName);
            }
        }
        catch(Exception e) {
            return null;
        }
    }

    /**
     * 判断object是否为基本类型
     * 
     * @param object
     * @return
     */
    public static boolean isBaseType(Object object) {
        Class<?> className = object.getClass();
        if (className.equals(java.lang.Integer.class) || className.equals(java.lang.Byte.class)
                || className.equals(java.lang.Long.class) || className.equals(java.lang.Double.class)
                || className.equals(java.lang.Float.class) || className.equals(java.lang.Character.class)
                || className.equals(java.lang.Short.class) || className.equals(java.lang.Boolean.class)) {
            return true;
        }
        return false;
    }

    /**
     * 实体转为 map
     *
     * @Title: objectToMap
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 6, 2020 9:26:25 AM
     * @param entity
     * @return
     * @return Map<?,?>
     */
    public static Map<?, ?> entityToMap(Object entity) {
        return ZKJsonUtils.parseObject(ZKJsonUtils.toJsonStr(entity), Map.class);
    }

    public static JSONObject entityToJSONObject(Object entity) {
        return ZKJsonUtils.parseJSONObject(ZKJsonUtils.toJsonStr(entity));
    }

    /**
     * 根据 map 中属性的值设置实体
     * 
     * @param entity
     * @param pMap
     */
    public static void mapFillEntity(Object entity, Map<String, ?> pMap) {
        try {
            if (entity == null || pMap == null) {
                return;
            }
            else {
                Class<?> entityClass = entity.getClass();
                for (String key : pMap.keySet()) {
                    if (key.indexOf(Param_Name.attributeSeparator) != -1) {

                    }
                    else {
                        Field f = ReflectionUtils.findField(entityClass, key);
                        if (f != null) {
                            Object value = pMap.get(key);
                            Class<?> pType = f.getType();
                            f.setAccessible(true);

                            if (value != null)
                                value = ZKClassUtils.getValueByClass(pType, value);
                            if ("int".equals(pType.getSimpleName()) || "float".equals(pType.getSimpleName())
                                    || "double".equals(pType.getSimpleName()) || "long".equals(pType.getSimpleName())) {

                                if (value == null || "".equals(value)) {

                                }
                                else {
                                    f.set(entity, value);
                                }
                            }
                            else {
                                f.set(entity, value);
                            }
                            f.setAccessible(false);
                        }
                    }

                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}
