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
* @Title: ZKSampleAuthPermission.java 
* @author Vinson 
* @Package com.zk.framework.security 
* @Description: TODO(simple description this file what to do. ) 
* @date May 10, 2022 2:22:06 PM 
* @version V1.0 
*/
package com.zk.framework.security;

import java.util.HashSet;
import java.util.Set;

/** 
* @ClassName: ZKSampleAuthPermission 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSampleAuthPermission implements ZKAuthPermission {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    /*** Api 接口代码 *****************************************************************/
    Set<String> apiCodeByDept = new HashSet<>();

    Set<String> apiCodeByRole = new HashSet<>();

    Set<String> apiCodeByUser = new HashSet<>();

    Set<String> apiCodeByRank = new HashSet<>();

    Set<String> apiCodeByUserType = new HashSet<>();

    /*** 权限代码 *****************************************************************/
    Set<String> authCodeByDept = new HashSet<>();

    Set<String> authCodeByRole = new HashSet<>();

    Set<String> authCodeByUser = new HashSet<>();

    Set<String> authCodeByRank = new HashSet<>();

    Set<String> authCodeByUserType = new HashSet<>();

    /**
     * (not Javadoc)
     * <p>
     * Title: getApiCodeByDept
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see com.zk.framework.security.ZKAuthPermission#getApiCodeByDept()
     */
    @Override
    public Set<String> getApiCodeByDept() {
        return this.apiCodeByDept;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getApiCodeByRole
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see com.zk.framework.security.ZKAuthPermission#getApiCodeByRole()
     */
    @Override
    public Set<String> getApiCodeByRole() {
        return this.apiCodeByRole;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getApiCodeByUser
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see com.zk.framework.security.ZKAuthPermission#getApiCodeByUser()
     */
    @Override
    public Set<String> getApiCodeByUser() {
        return this.apiCodeByUser;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getApiCodeByRank
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see com.zk.framework.security.ZKAuthPermission#getApiCodeByRank()
     */
    @Override
    public Set<String> getApiCodeByRank() {
        return this.apiCodeByRank;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getApiCodeByUserType
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see com.zk.framework.security.ZKAuthPermission#getApiCodeByUserType()
     */
    @Override
    public Set<String> getApiCodeByUserType() {
        return this.apiCodeByUserType;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getAuthCodeByDept
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see com.zk.framework.security.ZKAuthPermission#getAuthCodeByDept()
     */
    @Override
    public Set<String> getAuthCodeByDept() {
        return this.authCodeByDept;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getAuthCodeByRole
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see com.zk.framework.security.ZKAuthPermission#getAuthCodeByRole()
     */
    @Override
    public Set<String> getAuthCodeByRole() {
        return this.authCodeByRole;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getAuthCodeByUser
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see com.zk.framework.security.ZKAuthPermission#getAuthCodeByUser()
     */
    @Override
    public Set<String> getAuthCodeByUser() {
        return this.authCodeByUser;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getAuthCodeByRank
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see com.zk.framework.security.ZKAuthPermission#getAuthCodeByRank()
     */
    @Override
    public Set<String> getAuthCodeByRank() {
        return this.authCodeByRank;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getAuthCodeByUserType
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see com.zk.framework.security.ZKAuthPermission#getAuthCodeByUserType()
     */
    @Override
    public Set<String> getAuthCodeByUserType() {
        return this.authCodeByUserType;
    }

}
