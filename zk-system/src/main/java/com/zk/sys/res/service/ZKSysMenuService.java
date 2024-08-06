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
* @Title: ZKSysMenuService.java 
* @author Vinson 
* @Package com.zk.sys.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 4, 2020 11:03:52 PM 
* @version V1.0 
*/
package com.zk.sys.res.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.base.service.ZKBaseTreeService;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.sys.auth.service.ZKSysAuthMenuService;
import com.zk.sys.res.dao.ZKSysMenuDao;
import com.zk.sys.res.entity.ZKSysMenu;

/** 
* @ClassName: ZKSysMenuService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
@Transactional(readOnly = true)
public class ZKSysMenuService extends ZKBaseTreeService<String, ZKSysMenu, ZKSysMenuDao> {

    @Autowired
    ZKSysNavService sysNavService;

    @Autowired
    ZKSysAuthMenuService sysAuthMenuService;

    @Override
    @Transactional(readOnly = false)
    public int save(ZKSysMenu zkSysMenu) throws ZKValidatorException {

        // 如果是新增，判断 code 是否存在
        if (zkSysMenu.isNewRecord()) {
            ZKSysMenu oldSysMenu = this.getByCode(zkSysMenu.getCode());
            if (oldSysMenu != null) {
                if (oldSysMenu.getDelFlag().intValue() == ZKBaseEntity.DEL_FLAG.normal) {
                    // code 已存在；抛出异常；
                    log.error("[>_<:20200805-1216-001] 菜单代码: {} 已存在!", zkSysMenu.getCode());
                    throw ZKBusinessException.as("zk.sys.000001", "菜单代码已存在", zkSysMenu.getCode());
                }
                else {
                    // code 已存在，只是已逻辑删除；先物理删除已存在的；
                    this.diskDel(oldSysMenu);
//                    // code 已存在，只是已逻辑删除；恢复与修改这个菜单；
//                    zkSysMenu.setPkId(oldSysMenu.getPkId());
//                    zkSysMenu.setDelFlag(ZKBaseEntity.DEL_FLAG.normal);
                }
            }
        }
        return super.save(zkSysMenu);
    }

    /**
     * 按 code 查询, code 需要做唯一键
     *
     * @Title: getByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 5, 2020 12:24:03 PM
     * @param code
     * @return ZKSysMenu
     */
    public ZKSysMenu getByCode(String code) {
        if (ZKStringUtils.isEmpty(code)) {
            return null;
        }
        return this.dao.getByCode(ZKSysMenu.sqlHelper().getTableName(),
                ZKSysMenu.sqlHelper().getBlockSqlCols(""), code);
    }

    public ZKSysMenu getDetail(ZKSysMenu sysMenu) {
        ZKSysMenu e = this.dao.getDetail(sysMenu);
        if (e != null) {
            e.setSysNav(sysNavService.getByNavCode(e.getNavCode()));
        }
        return e;
    }

    @Override
    @Transactional(readOnly = false)
    public int del(ZKSysMenu menu) {
        sysAuthMenuService.diskDelByMenuId(menu.getPkId());
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        // 是否需要级联删除子项
        return super.del(menu);
    }

    @Override
    @Transactional(readOnly = false)
    public int diskDel(ZKSysMenu menu) {
        sysAuthMenuService.diskDelByMenuId(menu.getPkId());
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        // 是否需要级联删除子项
        return super.diskDel(menu);
    }

}
