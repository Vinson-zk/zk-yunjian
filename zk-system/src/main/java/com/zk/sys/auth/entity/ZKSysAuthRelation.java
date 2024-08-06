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
* @Title: ZKSysAuthRelation.java 
* @author Vinson 
* @Package com.zk.sys.auth.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 4, 2024 11:10:27 PM 
* @version V1.0 
*/
package com.zk.sys.auth.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.base.entity.ZKBaseEntity;

/** 
* @ClassName: ZKSysAuthRelation 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
// 记录权限关系，查询、与前端传参 时的过渡
public class ZKSysAuthRelation implements Serializable {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    String pkId;

//    // authCompany-公司权限；authDept-部门权限; authRank-职级权限; authRole-角色权限; authUser-用户权限;
//    // authUserType-用户类型权限; userRole-用户角色; authFuncApi-权限接口; authMenu-权限菜单; authNav-权限导航;
//    String relationCode;

    // 关系双方ID
    String leftPkId;

    // 关系双方ID
    String rightPkId;

    // 删除标记（0：正常；1：删除;）
    Integer delFlag;

    // 拥有方式；0-使用权；1-所有权(可以分配给下级公司使用)；
    Integer ownerType;

    // 是否默认传递给子公司；0-不传递；1-传递；
    Integer defaultToChild;

    /**
     * @return pkId sa
     */
    public String getPkId() {
        return pkId;
    }

    /**
     * @param pkId
     *            the pkId to set
     */
    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    /**
     * // * @return relationCode sa //
     */
//    public String getRelationCode() {
//        return relationCode;
//    }
//
//    /**
//     * @param relationCode
//     *            the relationCode to set
//     */
//    public void setRelationCode(String relationCode) {
//        this.relationCode = relationCode;
//    }

    /**
     * @return leftPkId sa
     */
    public String getLeftPkId() {
        return leftPkId;
    }

    /**
     * @param leftPkId
     *            the leftPkId to set
     */
    public void setLeftPkId(String leftPkId) {
        this.leftPkId = leftPkId;
    }

    /**
     * @return rightPkId sa
     */
    public String getRightPkId() {
        return rightPkId;
    }

    /**
     * @param rightPkId
     *            the rightPkId to set
     */
    public void setRightPkId(String rightPkId) {
        this.rightPkId = rightPkId;
    }

    /**
     * @return delFlag sa
     */
    public Integer getDelFlag() {
        return delFlag;
    }

    /**
     * @param delFlag
     *            the delFlag to set
     */
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * @return ownerType sa
     */
    public Integer getOwnerType() {
        return ownerType;
    }

    /**
     * @param ownerType
     *            the ownerType to set
     */
    public void setOwnerType(Integer ownerType) {
        this.ownerType = ownerType;
    }

    @JsonIgnore
    public boolean isDel() {
        if (this.getDelFlag() != null && this.getDelFlag().intValue() == ZKBaseEntity.DEL_FLAG.delete) {
            return true;
        }
        return false;
    }

    /**
     * @return defaultToChild sa
     */
    public Integer getDefaultToChild() {
        return defaultToChild;
    }

    /**
     * @param defaultToChild
     *            the defaultToChild to set
     */
    public void setDefaultToChild(Integer defaultToChild) {
        this.defaultToChild = defaultToChild;
    }

}

