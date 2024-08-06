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
 * @Title: ZKBaseMongoExpireDoc.java 
 * @author Vinson 
 * @Package com.zk.base.mongo.doc 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 1:49:15 PM 
 * @version V1.0   
*/
package com.zk.base.mongo.doc;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;

import com.zk.core.utils.ZKDateUtils;

/**
 * 有过期索引，会自动过期
 * 
 * @ClassName: ZKBaseMongoExpireDoc
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKBaseMongoExpireDoc<ID extends Serializable> extends ZKBaseMongoDoc<ID> {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    /**
     * 默认 过期时间；单位 毫秒；默认 30000 毫秒；
     */
    private static final int defaultValidTime = 30000;

    /***
     * 数据过期时间;
     * 
     * 在到达这个时间的，【expireAfterSeconds 秒后】1 秒后，会在下一次 mongo check 过期数据时，删除。mongo
     * check 过数数据周期为 60 秒；
     */
    @Indexed(name = "_indexed_base_expire_time_", expireAfterSeconds = 1)
    private Date expireTime;

    /**
     * 有效时长，单位 毫秒；小于 0 时，不过期；默认 30000 毫秒；
     */
    private int validTime = 0;

    public ZKBaseMongoExpireDoc() {
        this(defaultValidTime);
    }

    public ZKBaseMongoExpireDoc(int validTime) {
        this.validTime = validTime;
    }

    /**
     * @return validTime
     */
    public int getValidTime() {
        return validTime;
    }

    /**
     * @param validTime
     *            the validTime to set; 单位 毫秒
     */
    public void setValidTime(int validTime) {
        this.validTime = validTime;
    }

    /**
     * @return expireTime
     */
    public Date getExpireTime() {
        return expireTime;
    }

    /**
     * @param expireTime
     *            the expireTime to set
     */
    protected void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * 插入前预处理
     *
     * @Title: preInsert
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 20, 2019 9:50:16 AM
     * @return void
     */
    public void preInsert() {
        // 更新过期时间
        updateExpireTime();
    }

    /**
     * 修改前预处理
     *
     * @Title: preUpdate
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 20, 2019 9:50:32 AM
     * @return void
     */
    public void preUpdate() {
        // 更新过期时间
        updateExpireTime();
    }

    /**
     * 更新过期时间
     *
     * @Title: updateExpireTime
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 20, 2019 11:22:27 AM
     * @return void
     */
    protected void updateExpireTime() {
        if (this.validTime < 0) {
            this.expireTime = null;
        }
        else {
            this.expireTime = ZKDateUtils.addMilliseconds(new Date(), validTime);
        }
    }

}
