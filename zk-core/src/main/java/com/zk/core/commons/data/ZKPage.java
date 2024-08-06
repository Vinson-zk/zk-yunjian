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

import org.springframework.web.bind.ServletRequestUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.servlet.http.HttpServletRequest;

/** 
* @ClassName: ZKPage 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZKPage<T> {

    public static interface Param_Name {
        /**
         * 页面码，自然正整数，从 0 开始
         */
        public static final String no = "page.no";

        /**
         * 每页数据条数。默认是 10
         */
        public static final String size = "page.size";
    }

    public static interface Max_value {
        /**
         * 每页数据最大的允许的数量
         */
        public static final int size = 999999;
    }

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
        this.totalCount = 0L;
        this.sorters = null;
        this.result = new ArrayList<T>();
        this.startRow = 0;
    }

    public static <T> ZKPage<T> asPage() {
        return new ZKPage<T>();
    }
    /**
     * 根据请求做 page
     *
     * @Title: asPage
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jan 30, 2023 1:34:51 PM
     * @param <T>
     * @param hReq
     * @return
     * @return ZKPage<T>
     */
    public static <T> ZKPage<T> asPage(HttpServletRequest hReq) {
        ZKPage<T> zkPage = new ZKPage<T>();
        zkPage.setPageNo(ServletRequestUtils.getIntParameter(hReq, ZKPage.Param_Name.no, 0));
        zkPage.setPageSize(ServletRequestUtils.getIntParameter(hReq, ZKPage.Param_Name.size, 10));
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
        this.pageSize = pageSize > Max_value.size ? Max_value.size : pageSize;
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

    /**
     * 是否还有下一页
     *
     * @Title: hasNextPage
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 20, 2023 10:23:55 AM
     * @return boolean true-还有下一页；false-无下一页；
     */
    public boolean hasNextPage() {
        return this.getPageNo() < 0 || (this.getPageNo() + 1) * this.getPageSize() < this.getTotalCount();
    }

}
