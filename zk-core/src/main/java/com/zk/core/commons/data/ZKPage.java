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
 * @Title: ZKPage.java 
 * @author Vinson 
 * @Package com.zk.core.commons 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:19:08 PM 
 * @version V1.0   
*/
package com.zk.core.commons.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** 
* @ClassName: ZKPage 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZKPage<T> {

    /**
     * 查询页码; 从 0 开始是第一页；
     */
    protected int pageNo;

    /**
     * 每页显示条数
     */
    protected int pageSize;

    /**
     * 结果集总数目
     */
    protected long totalCount;

    /**
     * 分页后，单页的结果集
     */
    protected List<T> result;

    /**
     * 查询起始行
     */
    protected int startRow;

    @JsonIgnore
    protected List<ZKOrder> sorters;

    public ZKPage() {
        this.pageNo = 0;
        this.pageSize = 10;
        this.totalCount = -1L;
        this.sorters = null;
        this.result = new ArrayList<T>();
        this.startRow = 0;
    }

    public static interface Param_Name {
        /**
         * 页面码，默认是 1；自然正整数，从 1 开始；小 1 会默认从 1 开始。
         */
        public static final String no = "page.no";

        /**
         * 每页数据条数。默认是 10
         */
        public static final String size = "page.size";
    }

    public static <T> ZKPage<T> asPage(HttpServletRequest hReq) {
        ZKPage<T> zkPage = new ZKPage<T>();
        zkPage.setPageNo(ServletRequestUtils.getIntParameter(hReq, Param_Name.no, 0));
        zkPage.setPageSize(ServletRequestUtils.getIntParameter(hReq, Param_Name.size, 10));
        zkPage.setSorters(ZKOrder.asOrder(hReq));
        return zkPage;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    // 分页限制一页最多查询：9999
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize > 9999 ? 9999 : pageSize;
    }

//    public String getOrderBySql() {
//        if (this.sorters != null) {
//            return ZKOrder.toOrderBySql(sorters);
//        }
//        return null;
//    }

    public List<ZKOrder> getSorters() {
        return (sorters == null || sorters.size() < 1) ? null : sorters;
    }

    public void setSorters(List<ZKOrder> sorters) {
        this.sorters = sorters;
    }

    public void setSorters(ZKOrder... sorters) {
        this.sorters = Arrays.asList(sorters);
    }

    /**
     * @return 返回分页起始
     */
    public int getStartRow() {
        if (pageNo > 0) {
            this.startRow = pageNo * pageSize;
        }
        else {
            this.startRow = 0;
        }
        return this.startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

}
