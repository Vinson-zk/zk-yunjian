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
 * @Title: ZKCollectionUtils.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 3:38:56 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/** 
* @ClassName: ZKCollectionUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCollectionUtils {

    @SuppressWarnings("unchecked")
    public static <E> Set<E> asSet(E... elements) {
        if (elements == null || elements.length == 0) {
            return Collections.emptySet();
        }

        if (elements.length == 1) {
            return Collections.singleton(elements[0]);
        }

        LinkedHashSet<E> set = new LinkedHashSet<E>(elements.length * 4 / 3 + 1);
        Collections.addAll(set, elements);
        return set;
    }

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Collection c) {
        return c == null || c.isEmpty();
    }

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Map m) {
        return m == null || m.isEmpty();
    }

    /**
     * Returns the size of the specified collection or {@code 0} if the
     * collection is {@code null}.
     *
     * @param c
     *            the collection to check
     * @return the size of the specified collection or {@code 0} if the
     *         collection is {@code null}.
     * @since 1.2
     */
    @SuppressWarnings("rawtypes")
    public static int size(Collection c) {
        return c != null ? c.size() : 0;
    }

    /**
     * Returns the size of the specified map or {@code 0} if the map is
     * {@code null}.
     *
     * @param m
     *            the map to check
     * @return the size of the specified map or {@code 0} if the map is
     *         {@code null}.
     * @since 1.2
     */
    @SuppressWarnings("rawtypes")
    public static int size(Map m) {
        return m != null ? m.size() : 0;
    }

//    public static boolean isEmpty(PrincipalCollection principals) {
//        return principals == null || principals.isEmpty();
//    }

    @SuppressWarnings("unchecked")
    public static <E> List<E> asList(E... elements) {
        if (elements == null || elements.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.asList(elements);
    }

    static int computeListCapacity(int zkraySize) {
        return (int) Math.min(5L + zkraySize + (zkraySize / 10), Integer.MAX_VALUE);
    }

    /**
     * 将一个任意的 map 转换为 Map<String, String[]> map
     *
     * @Title: mapToReqParametMap
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jun 26, 2019 5:20:44 PM
     * @param map
     * @return
     * @return Map<String,String[]>
     */
    public static Map<String, String[]> mapToReqParametMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        Map<String, String[]> zkParameterMap = new HashMap<>();
        for (Entry<String, Object> e : map.entrySet()) {

            if (e.getValue() instanceof Collection<?>) {
                Collection<?> values = (Collection<?>) e.getValue();
                String[] sValues = new String[values.size()];
                int index = 0;
                for (Object o : values) {
                    sValues[index] = getStringByObject(o);
                    ++index;
                }
                zkParameterMap.put(e.getKey(), sValues);
            }
            else if (e.getValue().getClass().isArray()) {
                Object[] values = (Object[]) e.getValue();
                String[] sValues = new String[values.length];
                int index = 0;
                for (Object o : values) {
                    sValues[index] = getStringByObject(o);
                    ++index;
                }
                zkParameterMap.put(e.getKey(), sValues);
            }
            else {
                zkParameterMap.put(e.getKey(), new String[] { getStringByObject(e.getValue()) });
            }
        }
        return zkParameterMap;
    }

    private static String getStringByObject(Object o) {
        if (ZKObjectUtils.isBaseType(o) || o instanceof String) {
            return o.toString();
        }
        else {
            return ZKJsonUtils.toJsonStr(o);
        }
    }

    public static <T extends Object> void cutArray(T[] sourecsArray, int begin, T[] zkrays) throws Exception {
        cutArray(sourecsArray, begin, sourecsArray.length, zkrays);
    }

    /**
     * 截取数组；[begin, end) 左闭右开
     *
     * @Title: cutArray
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 1, 2019 11:01:07 AM
     * @param ts
     * @param begin
     * @param end
     * @return
     * @throws Exception
     * @return T[]
     */
    public static <T extends Object> void cutArray(T[] sourecsArray, int begin, int end, T[] zkrays) throws Exception {
        if (begin < 0) {
            throw new Exception("begin cannot be less than 0");
        }
        if (begin >= end) {
            throw new Exception("end must be greater than begin");
        }

        if (end > sourecsArray.length) {
            throw new Exception("end cannot be greater than ts.length");
        }

        for (int i = 0; i < (end - begin); ++i) {
            zkrays[i] = sourecsArray[i + begin];
        }
    }

    /**
     * 创建一个新的 ArrayList
     *
     * @Title: newArrayList
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 3, 2019 3:59:17 PM
     * @return
     * @return ArrayList<E>
     */
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

}
