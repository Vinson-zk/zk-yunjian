/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKWebfluxTestEntity.java 
* @author Vinson 
* @Package com.zk.webflux.helper.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 24, 2024 6:35:33 PM 
* @version V1.0 
*/
package com.zk.webflux.helper.entity;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

/** 
* @ClassName: ZKWebfluxTestEntity 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWebfluxTestEntity {

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String name;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private int age;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String remark;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<String> strList;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String[] strArray;

    // 为 null 时，转换为 json 字符串时忽略；
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, ZKWebfluxTestEntity> map;

    // 为 null 时，转换为 json 字符串时忽略；
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ZKWebfluxTestEntity> list;

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
     * @return age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age
     *            the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     *            the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return strList sa
     */
    public List<String> getStrList() {
        return strList;
    }

    /**
     * @return strArray sa
     */
    public String[] getStrArray() {
        return strArray;
    }

    /**
     * @param strList
     *            the strList to set
     */
    public void setStrList(List<String> strList) {
        this.strList = strList;
    }

    /**
     * @param strArray
     *            the strArray to set
     */
    public void setStrArray(String[] strArray) {
        this.strArray = strArray;
    }

    /**
     * @return map sa
     */
    public Map<String, ZKWebfluxTestEntity> getMap() {
        return map;
    }

    /**
     * @return list sa
     */
    public List<ZKWebfluxTestEntity> getList() {
        return list;
    }

    /**
     * @param map
     *            the map to set
     */
    public void setMap(Map<String, ZKWebfluxTestEntity> map) {
        this.map = map;
    }

    /**
     * @param list
     *            the list to set
     */
    public void setList(List<ZKWebfluxTestEntity> list) {
        this.list = list;
    }

}
