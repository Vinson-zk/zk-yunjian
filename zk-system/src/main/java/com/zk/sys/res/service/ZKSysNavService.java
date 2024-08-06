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
* @Title: ZKSysNavService.java 
* @author Vinson 
* @Package com.zk.sys.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 20, 2020 1:57:25 PM 
* @version V1.0 
*/
package com.zk.sys.res.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.sys.auth.service.ZKSysAuthNavService;
import com.zk.sys.res.dao.ZKSysNavDao;
import com.zk.sys.res.entity.ZKSysNav;

/**
 * @ClassName: ZKSysNavService
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class ZKSysNavService extends ZKBaseService<String, ZKSysNav, ZKSysNavDao> {

    @Autowired
    ZKSysAuthNavService sysAuthNavService;

    @Override
    @Transactional(readOnly = false)
    public int save(ZKSysNav zkSysNav) throws ZKValidatorException {
        // 如果是新增，判断 code 是否存在
        if (zkSysNav.isNewRecord()) {
            ZKSysNav oldSysNav = this.getByNavCode(zkSysNav.getCode());
            if (oldSysNav != null) {
                if (oldSysNav.getDelFlag().intValue() == ZKBaseEntity.DEL_FLAG.normal) {
                    // navCode 已存在；抛出异常；
                    log.error("[>_<:20200820-1400-001] 导航栏目代码: {} 已存在!", zkSysNav.getCode());
                    throw ZKBusinessException.as("zk.sys.000002", "导航栏目代码已存在", zkSysNav.getCode());
                }
                else {
                    // navCode 已存在，只是已逻辑删除；先物理删除已存在的；
                    this.diskDel(oldSysNav);
                    // navCode 已存在，只是已逻辑删除；恢复与修改这个菜单；
//                    zkSysNav.parseByOld(oldSysNav);
////                    zkSysNav.setPkId(oldSysNav.getPkId());
//                    zkSysNav.setDelFlag(ZKBaseEntity.DEL_FLAG.normal);

                }
            }
        }
        return super.save(zkSysNav);
    }

    /**
     * 按 navCode 查询, navCode 需要做唯一键
     *
     * @Title: getByNavCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 20, 2020 1:58:36 PM
     * @param navCode
     * @return ZKSysNav
     */
//    public ZKSysNav getByNavCode(String navCode) {
//        if (ZKStringUtils.isEmpty(navCode)) {
//            return null;
//        }
//        return this.dao.getByNavCode(navCode);
//    }

    public ZKSysNav getByNavCode(String navCode) {
        if (ZKStringUtils.isEmpty(navCode)) {
            return null;
        }
        return this.dao.getByNavCode(ZKSysNav.sqlHelper().getTableName(),
                ZKSysNav.sqlHelper().getBlockSqlCols(""), navCode);
    }

    @Override
    @Transactional(readOnly = false)
    public int del(ZKSysNav nav) {
        sysAuthNavService.diskDelByNavId(nav.getPkId());
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return super.del(nav);
    }

    @Override
    @Transactional(readOnly = false)
    public int diskDel(ZKSysNav nav) {
        sysAuthNavService.diskDelByNavId(nav.getPkId());
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return super.diskDel(nav);
    }

}
