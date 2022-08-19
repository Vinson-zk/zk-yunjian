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
* @Title: ZKTableInfoService.java 
* @author Vinson 
* @Package com.zk.code.generate.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 31, 2021 8:34:52 AM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKCodeException;
import com.zk.devleopment.tool.gen.action.ZKCodeGenConstant;
import com.zk.devleopment.tool.gen.action.ZKConvertUtils;
import com.zk.devleopment.tool.gen.action.tableInfo.ZKGetTableInfoUtils;
import com.zk.devleopment.tool.gen.dao.ZKTableInfoDao;
import com.zk.devleopment.tool.gen.entity.ZKModule;
import com.zk.devleopment.tool.gen.entity.ZKTableInfo;

/**
 * @ClassName: ZKTableInfoService
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class ZKTableInfoService extends ZKBaseService<String, ZKTableInfo, ZKTableInfoDao> {

    @Autowired
    ZKModuleService zkModuleService;

    @Autowired
    ZKColInfoService zkColInfoService;

    // 取模块表的列表信息；不分页，仅查询已更新到代码生成库的表列表，不会自动取模块数据中新创建的表；
    @Transactional(readOnly = false)
    public List<ZKTableInfo> getTablesByModule(String moduleId) {
        ZKTableInfo pti = new ZKTableInfo();
        pti.setModuleId(moduleId);
        List<ZKTableInfo> res = super.findList(pti);
        ZKModule module = this.zkModuleService.get(new ZKModule(moduleId));
        for (ZKTableInfo ti : res) {
            ti.setModule(module);
        }
        return res;
    }

    // 更新表列表；更新表列表；从模块的数据库中读取表列表，更新到代码生成的表列表中；
    @Transactional(readOnly = false)
    public List<ZKTableInfo> updateTableList(String moduleId) {
        List<ZKTableInfo> res = new ArrayList<>();

        ZKModule m = this.zkModuleService.get(new ZKModule(moduleId));
        List<Map<String, Object>> dbTableInfos = ZKGetTableInfoUtils.getDbTableInfos(m, null);
        ZKTableInfo ti = null, resTi;
        String tableName = null;
        List<String> pkList = null;
        for (Map<String, Object> dbTableInfo : dbTableInfos) {
            tableName = (String) dbTableInfo.get(ZKCodeGenConstant.KeyTable.tableName);
            // 根据表名查
            resTi = this.getByTableName(moduleId, tableName);
            if (resTi == null) {
                pkList = ZKGetTableInfoUtils.getPkSourceList(m, tableName);
                // 生成表信息
                ti = ZKGetTableInfoUtils.makeTableInfo(dbTableInfo, pkList);
                // 新表，表信息还没生成；转换表信息
                ZKConvertUtils.convert(m, ti);
                this.save(ti);
                res.add(ti);
            }
            else {
                // 表信息已存在
                res.add(resTi);
            }
        }
        return res;
    }

    // 更新表信息
    @Transactional(readOnly = false)
    public ZKTableInfo updateTableInfo(String tableId) {
        ZKTableInfo ti = this.get(new ZKTableInfo(tableId));
        if (ti == null) {
            log.error("[^_^:20210401-0842-003] 表:{}, 信息不存在；", tableId);
            throw new ZKCodeException("zk.codeGen.000002", "表信息不存在", tableId);
        }
        ZKModule m = this.zkModuleService.get(new ZKModule(ti.getModuleId()));
        if (m == null) {
            log.error("[^_^:20210401-0704-003] 功能模块:{}, 不存在；", ti.getModuleId());
            throw new ZKCodeException("zk.codeGen.000001", "功能模块不存在", ti.getModuleId());
        }
        return updateTableInfo(m, ti);
    }

    @Transactional(readOnly = false)
    public ZKTableInfo updateTableInfo(ZKModule module, ZKTableInfo tableInfo) {
        Map<String, Object> dbTableInfo = ZKGetTableInfoUtils.getDbTableInfo(module, tableInfo.getTableName());
        if (dbTableInfo == null || dbTableInfo.isEmpty()) {
            log.error("[^_^:20210401-0704-003] 表:{}, 信息不存在；", tableInfo.getTableName());
            throw new ZKCodeException("zk.codeGen.000002", "表信息不存在", tableInfo.getTableName());
        }
        List<String> pkList = ZKGetTableInfoUtils.getPkSourceList(module, tableInfo.getTableName());

        ZKGetTableInfoUtils.makeTableInfo(dbTableInfo, pkList, tableInfo);
        // 表信息 转换
        ZKConvertUtils.convert(module, tableInfo);
        this.save(tableInfo);
        return tableInfo;
    }

    /**
     * 根据表名，取列信息；注：不包含表的字段信息
    *
    * @Title: getByTableName 
    * @Description: TODO(simple description this method what to do.) 
    * @author Vinson 
    * @date Mar 31, 2021 9:59:43 AM 
    * @param tb
    * @return
    * @return ZKTableInfoDao
     */
    public ZKTableInfo getByTableName(String moduleId, String tableName) {
        return this.dao.getByTableName(ZKTableInfo.sqlProvider().getTableName(),
                ZKTableInfo.sqlProvider().getSqlBlockSelCols(""), moduleId, tableName);
    }

    @Transactional(readOnly = false)
    public int diskDel(ZKTableInfo ti) {
        this.zkColInfoService.diskDelByTableId(ti.getPkId());
        return super.diskDel(ti);
    }

}
