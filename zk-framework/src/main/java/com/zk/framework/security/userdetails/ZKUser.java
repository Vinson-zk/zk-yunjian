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
* @Title: ZKUser.java 
* @author Vinson 
* @Package com.zk.framework.security.userdetails 
* @Description: TODO(simple description this file what to do. ) 
* @date May 10, 2022 8:55:31 AM 
* @version V1.0 
*/
package com.zk.framework.security.userdetails;

import java.util.Date;

/** 
* @ClassName: ZKUser 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKUser<ID> {

    /**
     * 实体编号（唯一标识）
     */
    public ID getPkId();

    /**
     * 数据版本号
     * 
     * @return
     */
    public Long getVersion();

    /**
     * 创建者, ID
     * 
     * @return
     */
    public ID getCreateUserId();

    /**
     * 创建日期
     * 
     * @return
     */
    public Date getCreateDate();

    /**
     * 修改者ID
     * 
     * @return
     */
    public ID getUpdateUserId();

    /**
     * 修改日期
     * 
     * @return
     */
    public Date getUpdateDate();

    /**
     * 删除标记（0：正常；1：删除;）
     * 
     * @return
     */
    public Integer getDelFlag();

    /**
     * 集团代码
     */
    public String getGroupCode();

    /**
     * 公司ID
     */
    public String getCompanyId();

    /**
     * 公司代码
     */
    public String getCompanyCode();

    /**
     * 用户类型ID
     */
    public String getUserTypeId();

    /**
     * 用户类型代码
     */
    public String getUserTypeCode();

    /**
     * 用户职级ID
     */
    public String getRankId();

    /**
     * 用户职级代码
     */
    public String getRankCode();

    /**
     * 用户部门ID
     */
    public String getDeptId();

    /**
     * 用户部门代码
     */
    public String getDeptCode();

    /**
     * 用户账号
     */
    public String getAccount();

    /**
     * 密码状态；0-系统密码；1-用户密码；
     */
    public Integer getPwdStatus();

    /**
     * 用户状态; 0-正常; 1-禁用; 2-离职; 3-离职中;
     */
    public Integer getStatus();

    /**
     * 生日
     */
    public Date getBirthday();

    /**
     * 性别；unknown-保密；male-男；female-女；third_gender-第三性别; 字典项
     */
    public String getSex();

    /**
     * 手机
     */
    public String getPhoneNum();

    /**
     * 邮箱
     */
    public String getMail();

    /**
     * 用户工号
     */
    public String getJobNum();

    /**
     * 入职日期
     */
    public Date getJoinDate();

}
