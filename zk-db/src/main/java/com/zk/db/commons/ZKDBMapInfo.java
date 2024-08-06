package com.zk.db.commons;

import com.zk.core.utils.ZKClassUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKTable;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: 实体的注解对应库表信息
 * @ClassName ZKDBMapInfo
 * @Package com.zk.db.annotation
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-08 09:50:16
 **/
public class ZKDBMapInfo {

    private Class<?> classz;

    /**
     * 类上注解的表名
     */
    private ZKTable table;

    /**
     * 字段或方法上注解的 column 映射；以 属性名为 key
     *
     * 注意: 这里在 get 方法上注解的优先级大于在字段上注解的优先级；
     */
    private Map<PropertyDescriptor, ZKColumn> columnInfo;

    public ZKDBMapInfo(Class<?> classz) {
        this.classz = classz;
        try {
            this.table = classz.getAnnotation(ZKTable.class);
            this.initSettingColumns();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void initSettingColumns() throws IntrospectionException {

        this.columnInfo = new HashMap<PropertyDescriptor, ZKColumn>();

        PropertyDescriptor[] propertyDescriptors = ZKClassUtils.getAllProperty(this.classz);
        List<Field> fields = ZKClassUtils.getAllField(this.classz);
        Method m = null;
        Field filed = null;
        ZKColumn zkc = null;
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

            m = null;
            filed = null;
            zkc = null;
            m = propertyDescriptor.getReadMethod();
            // 优先从 bean 的 getter 方法中取；
            if (m != null) {
//                zkc = AnnotationUtils.getAnnotation(m, ZKColumn.class);
                zkc = m.getAnnotation(ZKColumn.class);
            }
            if (zkc == null) {
                // 再从类定义的字段上取；如果已在 bean 的 getter 方法中取到注解，则不在从定义的字段上取；
                filed = ZKClassUtils.getField(fields, propertyDescriptor.getName());
                if (filed != null) {
//                    zkc = AnnotationUtils.getAnnotation(filed, ZKColumn.class);
                    zkc = filed.getAnnotation(ZKColumn.class);
                }
            }

            this.put(propertyDescriptor, zkc);
        }

    }

    private void put(PropertyDescriptor propertyDescriptor, ZKColumn column) {
        // 如果字段注解的名称为空，不处理，丢弃；
        if (column != null && !ZKStringUtils.isEmpty(column.name())) {
            if (!this.isExist(propertyDescriptor)) {
                this.columnInfo.put(propertyDescriptor, column);
            }
        }
    }

    private boolean isExist(PropertyDescriptor propertyDescriptor) {
        return this.columnInfo.containsKey(propertyDescriptor);
    }

    public ZKTable getTable() {
        return this.table;
    }

    /**
     * @return classz sa
     */
    public Class<?> getClassz() {
        return classz;
    }

    /***********************************************************************/
    public boolean isEmpty(){
        return this.getColumnInfo().isEmpty();
    }

    public Iterator<Map.Entry<PropertyDescriptor, ZKColumn>> getColumnsIterator(){
        return this.getColumnInfo().entrySet().iterator();
    }

    public Set<Map.Entry<PropertyDescriptor, ZKColumn>> getColumnSet(){
        return this.getColumnInfo().entrySet();
    }

    /**
     * @return columnInfo sa
     */
    public Map<PropertyDescriptor, ZKColumn> getColumnInfo() {
        if (columnInfo == null) {
            this.columnInfo = new HashMap<>();
        }
        return columnInfo;
    }

    public ZKColumn getColumn(String attrName) {
        for (Map.Entry<PropertyDescriptor, ZKColumn> e : this.columnInfo.entrySet()) {
            if (e.getKey().getName().equals(attrName)) {
                return e.getValue();
            }
        }
        return null;
    }

    /**
     * 取表名
     *
     * @Title: getTableName
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 15, 2022 9:54:51 AM
     * @return
     * @return String
     */
    public String getTableName() {
        return this.getTable().name();
    }

    /**
     * 取表别名
     *
     * @Title: getAlias
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 15, 2022 9:54:48 AM
     * @return
     * @return String
     */
    public String getAlias() {
        return this.getTable().alias();
    }

}
