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
* @Title: ZKSysOrgUserApiImpl.java 
* @author Vinson 
* @Package com.zk.file.helper.sysApi 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 22, 2024 11:38:32 AM 
* @version V1.0 
*/
package com.zk.file.helper.sysApi;

import java.util.Date;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.framework.security.userdetails.ZKUser;
import com.zk.sys.org.api.ZKSysOrgUserApi;
import com.zk.sys.org.entity.ZKSysOrgCompany;

/**
 * @ClassName: ZKSysOrgUserApiImpl
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKSysOrgUserApiImpl implements ZKSysOrgUserApi {

    @Override
    public ZKMsgRes getUserByPkId(String pkId) {
        return ZKMsgRes.asOk(ZKJsonUtils.toJsonStr(new ZKUserImpl()));
    }

    @Override
    public ZKMsgRes getUserByPhnoeNum(String companyId, String phoneNum) {
        return ZKMsgRes.asOk(ZKJsonUtils.toJsonStr(new ZKUserImpl()));
    }

    public static class ZKUserImpl implements ZKUser<String> {

        @Override
        public String getPkId() {
            return "-1";
        }

        @Override
        public Long getVersion() {
            return -1l;
        }

        @Override
        public String getCreateUserId() {
            return "-1";
        }

        @Override
        public Date getCreateDate() {
            return ZKDateUtils.getToday();
        }

        @Override
        public String getUpdateUserId() {
            return "-1";
        }

        @Override
        public Date getUpdateDate() {
            return ZKDateUtils.getToday();
        }

        @Override
        public Integer getDelFlag() {
            return 0;
        }

        @Override
        public String getGroupCode() {
            return "-1";
        }

        @Override
        public String getCompanyId() {
            return "-1";
        }

        @Override
        public String getCompanyCode() {
            return "-1";
        }

        @Override
        public String getUserTypeId() {
            return "-1";
        }

        @Override
        public String getUserTypeCode() {
            return "-1";
        }

        @Override
        public String getRankId() {
            return "-1";
        }

        @Override
        public String getRankCode() {
            return "-1";
        }

        @Override
        public String getDeptId() {
            return "-1";
        }

        @Override
        public String getDeptCode() {
            return "-1";
        }

        @Override
        public String getAccount() {
            return "-1";
        }

        @Override
        public Integer getStatus() {
            return -1;
        }

        @Override
        public Date getBirthday() {
            return ZKDateUtils.getToday();
        }

        @Override
        public String getSex() {
            return "-1";
        }

        @Override
        public String getPhoneNum() {
            return "-1";
        }

        @Override
        public String getMail() {
            return "-1";
        }

        @Override
        public String getJobNum() {
            return "-1";
        }

        @Override
        public Date getJoinDate() {
            return ZKDateUtils.getToday();
        }

        @Override
        public ZKSysOrgCompany getCompany() {
            return null;
        }

    }

}
