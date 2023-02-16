/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSecTicket.java 
* @author Vinson 
* @Package com.zk.security.ticket 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 14, 2021 6:29:04 PM 
* @version V1.0 
*/
package com.zk.security.ticket;

import java.io.Serializable;
import java.util.Date;

import com.zk.security.principal.pc.ZKSecPrincipalCollection;

/**
 * 令牌，包含有对应的身份令牌
 * 
 * @ClassName: ZKSecTicket
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public interface ZKSecTicket extends Serializable {

    /**
     * 令牌信息 KEY
     */
//    public static interface TICKET_INFO_KEY {
    public static interface KeyTicketInfo {
        /**
         * 令牌停止原因代码
         */
        public static final String stop_info_code = "stop_info_code";
    }

    /**
     * 令牌状态
     */
//    public static interface STATUS {
    public static interface KeyStatus {
        /**
         * 令牌状态为启用
         */
        public static final int Start = 0;

        /**
         * 令牌状态为禁用
         */
        public static final int Stop = 1;
    }

    /**
     * 令牌类型
     */
//    public static interface TYPE {
    public static interface KeyType {
        /**
         * 令牌类型：普通令牌
         */
        public static final int General = 0;

        /**
         * 令牌类型：security 权限令牌
         */
        public static final int Security = 1;

    }

    /**
     * 令牌的权限类型；0-用户权限；1-服务器权限令牌，微服务器间的访问时使用；
     * 
     * @ClassName: KeySecurityType
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface KeySecurityType {

        /**
         * 令牌类型：用户权限令牌
         */
        public static final int User = 0;

        /**
         * 令牌类型：服务器权限令牌
         */
        public static final int Server = 1;
    }

    /**
     * 取拥有的身分
     * 
     * @return
     */
    <ID> ZKSecPrincipalCollection<ID> getPrincipalCollection();

    /**
     * 设置拥有者标识
     */
    <ID> void setPrincipalCollection(ZKSecPrincipalCollection<ID> principalCollection);

    /**
     * 取令牌唯一ID
     * 
     * @return
     */
    public Serializable getTkId();

    /**
     * 取令牌类型, 0 普通令牌；1-security令牌
     * 
     * @return
     */
    public int getType();

    /**
     * 权限令牌
     *
     * @Title: getSecType
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 16, 2022 3:34:42 PM
     * @return
     * @return int
     */
    public int getSecurityType();

    /**
     * 取令牌状态 0-正常 1-未启用
     */
    public int getStatus();
    
    /**
     * 启用令牌，设置状态为 0
     */
    public void start();

    /**
     * 停用令牌，设置状态为 0
     */
    public void stop();

    /**
     * 最后更新时间戳
     */
    public Date getLastTime();

    /**
     * 更新最后修改时间
     */
    public void updateLastTime();
    
    /**
     * 有效时长，毫秒
     * 
     * @return
     */
    public long getValidTime();
    
    /**
     * 设置令牌有效时长，毫秒
     */
    public void setValidTime(long validTime);

    /**
     * 判断令牌是否有效，过期与未起用为无效
     * 
     * @return true-有效；false-无效；
     */
    public boolean isValid();

    /**
     * 销毁令牌
     */
    public int drop();

//  /**
//   * 触发更新
//   */
//  public void touchUpdate();

    /**
     * 设置令牌所携带的信息
     */
    public <V> boolean put(String key, V value);

    /**
     * 取令牌所携带的信息
     */
    public <V> V get(String key);

    /**
     * 删除一个令牌所携带的信息
     * 
     * @param key
     */
    public boolean remove(String key);

//  /**
//   * 清理令牌所携带的令牌
//   */
//  public void cleanValue();

}
