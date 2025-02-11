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
 * @Title: ZKOrder.java 
 * @author Vinson 
 * @Package com.zk.core.commons 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:16:38 PM 
 * @version V1.0   
*/
package com.zk.core.commons.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.http.server.reactive.ServerHttpRequest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zk.core.web.utils.ZKWebUtils;

import jakarta.servlet.http.HttpServletRequest;

/** 
* @ClassName: ZKOrder 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZKOrder implements Serializable {

    /**
     * @Fields serialVersionUID : TODO(描述变量)
     */
    private static final long serialVersionUID = 3705768353395390489L;

    /**
     * sql 的字段名中是否包含非法字符；字段名由下划线，大小写字母、数字组成；只能以下划线或字母开头。
     */
    private static final Pattern isColumnNamePattern = Pattern.compile("^([A-Za-z_][A-Za-z_0-9.]*)$");

    /**
     * 字段名，如果不做其他转换，直接转入表中字段名，如果使用通 @ZKColumn 做映射转换，则传入实体对应的属性名；
     */
    private String columnName;

    private ZKSortMode sortMode;

    public ZKOrder() {
    }

    public ZKOrder(String columnName, ZKSortMode sortMode) {
        if(!isColumnNamePattern.matcher(columnName).matches()){
            throw new RuntimeException(
                    "ColumnName contains illegal characters.ColumnName is underlined, uppercase and lowercase letters and point, and Numbers; You can only start with an underscore or a letter. ");
        }
        this.columnName = columnName;
        this.sortMode = sortMode;
    }

    public String getColumnName() {
        // 需要判断是否有非法字符
        return columnName;
    }

    public ZKSortMode getSortMode() {
        return sortMode;
    }

    @JsonIgnore
    public String getValue() {
        return sortMode.getValue();
    }

    /****************************************************************/
    public static interface Param_Name {
        /**
         * 排序字段
         */
        public static final String column = "page.sort.col";

        /**
         * 排序方式
         */
        public static final String mode = "page.sort.mode";
    }

    /**
     * 生成一个排序对象；默认为 DESC 升序；
     * 
     * @param columnName
     * @param sort
     * @return
     */
    public static ZKOrder asOrder(String columnName, String sort) {
        ZKSortMode st = ZKSortMode.parseKey(sort);
        return new ZKOrder(columnName, ((st == null) ? ZKSortMode.DESC : st));
    }

    public static ZKOrder asOrder(String columnName, ZKSortMode sort) {
        return new ZKOrder(columnName, ((sort == null) ? ZKSortMode.DESC : sort));
    }

    /**
     * 根据请求做排序列表
     *
     * @Title: asOrder
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jan 30, 2023 1:35:02 PM
     * @param hReq
     * @return
     * @return List<ZKOrder>
     */
    public static List<ZKOrder> asOrder(HttpServletRequest hReq) {
        String[] cols = ZKWebUtils.getStringParameters(hReq, Param_Name.column);
        String[] modes = ZKWebUtils.getStringParameters(hReq, Param_Name.mode);
        return asOrder(Arrays.asList(cols), Arrays.asList(modes));
    }

    public static List<ZKOrder> asOrder(ServerHttpRequest hReq) {
        List<String> cols = ZKWebUtils.getStringParameters(hReq, Param_Name.column);
        List<String> modes = ZKWebUtils.getStringParameters(hReq, Param_Name.mode);
        return asOrder(cols, modes);
    }

    protected static List<ZKOrder> asOrder(List<String> cols, List<String> modes) {
        if (cols != null && cols.size() > 0) {
            List<ZKOrder> orders = new ArrayList<ZKOrder>();
            for (int i = 0; i < cols.size(); ++i) {
                if (i < modes.size()) {
                    orders.add(ZKOrder.asOrder(cols.get(i), modes.get(i)));
                }
                else {
                    // 如果未指定排序模式，默认按升序处理；排序模式与字段按顺序对应
                    orders.add(ZKOrder.asOrder(cols.get(i), ZKSortMode.ASC));
                }
            }
            return orders;
        }
        return null;
    }

}


