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
 * @Title: ZKDemoEntity.java 
 * @author Vinson 
 * @Package com.zk.demo.entity 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 4:59:48 PM 
 * @version V1.0   
*/
package com.zk.demo.web.entity;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

/** 
* @ClassName: ZKDemoEntity 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDemoEntity {

    private String name;

    private int age;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ZKDemoEntity> demoEntityList;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ZKDemoEntity[] demoEntityArray;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, ZKDemoEntity> demoEntityMap;

    public ZKDemoEntity() {

    }

    public ZKDemoEntity(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return arge
     */
    public int getAge() {
        return age;
    }

    /**
     * @param arge
     *            the arge to set
     */
    public void setAge(int arge) {
        this.age = arge;
    }

    /**
     * @return demoEntityList
     */
    public List<ZKDemoEntity> getDemoEntityList() {
        return demoEntityList;
    }

    /**
     * @param demoEntityList
     *            the demoEntityList to set
     */
    public void setDemoEntityList(List<ZKDemoEntity> demoEntityList) {
        this.demoEntityList = demoEntityList;
    }

    /**
     * @return demoEntityArray
     */
    public ZKDemoEntity[] getDemoEntityArray() {
        return demoEntityArray;
    }

    /**
     * @param demoEntityArray
     *            the demoEntityArray to set
     */
    public void setDemoEntityArray(ZKDemoEntity[] demoEntityArray) {
        this.demoEntityArray = demoEntityArray;
    }

    /**
     * @return demoEntityMap
     */
    public Map<String, ZKDemoEntity> getDemoEntityMap() {
        return demoEntityMap;
    }

    /**
     * @param demoEntityMap
     *            the demoEntityMap to set
     */
    public void setDemoEntityMap(Map<String, ZKDemoEntity> demoEntityMap) {
        this.demoEntityMap = demoEntityMap;
    }

}
