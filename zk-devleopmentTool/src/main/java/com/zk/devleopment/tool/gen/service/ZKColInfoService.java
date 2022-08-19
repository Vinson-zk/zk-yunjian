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
* @Title: ZKColInfoService.java 
* @author Vinson 
* @Package com.zk.code.generate.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 31, 2021 11:58:53 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKCodeException;
import com.zk.core.utils.ZKCollectionUtils;
import com.zk.devleopment.tool.gen.action.ZKCodeGenConstant;
import com.zk.devleopment.tool.gen.action.ZKConvertUtils;
import com.zk.devleopment.tool.gen.action.tableInfo.ZKGetTableInfoUtils;
import com.zk.devleopment.tool.gen.dao.ZKColInfoDao;
import com.zk.devleopment.tool.gen.entity.ZKColInfo;
import com.zk.devleopment.tool.gen.entity.ZKModule;
import com.zk.devleopment.tool.gen.entity.ZKTableInfo;

/** 
* @ClassName: ZKColInfoService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
@Transactional(readOnly = true)
public class ZKColInfoService extends ZKBaseService<String, ZKColInfo, ZKColInfoDao> {

    @Autowired
    ZKModuleService zkModuleService;

    @Autowired
    ZKTableInfoService zkTableInfoService;

    // 查询指定表的 字段信息
    public List<ZKColInfo> findByTableId(String tableId) {
        ZKColInfo colInfo = new ZKColInfo();
        colInfo.setTableId(tableId);
        return this.dao.findList(colInfo);
    }

    // 根据字段名查找字段信息；虽然 tableId 和 colName 已做唯一键，懒得写单独查询的方法了；
    public ZKColInfo getByColName(String tableId, String colName) {
        ZKColInfo colInfo = new ZKColInfo();
        colInfo.setTableId(tableId);
        colInfo.setColName(colName);
        List<ZKColInfo> res = this.dao.findList(colInfo);
        if (res != null && !res.isEmpty()) {
            for (ZKColInfo col : res) {
                if (col.getColName().equals(colName)) {
                    return col;
                }
            }
        }
        return null;
    }

    // 增量更新；只更新添加的字段及修改的表信息；不会删除表中不存的字段；
    @Transactional(readOnly = false)
    public List<ZKColInfo> updateAddByTable(String tableId) {
        ZKTableInfo tableInfo = zkTableInfoService.get(new ZKTableInfo(tableId));
        if (tableInfo == null) {
            log.error("[^_^:20210401-0704-002] 表:{}, 信息不存在；", tableId);
            throw new ZKCodeException("zk.codeGen.000002", "表信息不存在", tableId);
        }
        ZKModule module = zkModuleService.get(new ZKModule(tableInfo.getModuleId()));
        if (module == null) {
            log.error("[^_^:20210401-0704-002] 功能模块:{}, 不存在；", tableInfo.getModuleId());
            throw new ZKCodeException("zk.codeGen.000001", "功能模块不存在", tableInfo.getModuleId());
        }
        return this.updateAddByTable(module, tableInfo);
    }

    @Transactional(readOnly = false)
    public List<ZKColInfo> updateAddByTable(ZKModule module, ZKTableInfo tableInfo) {
        List<String> pkList = ZKGetTableInfoUtils.getPkSourceList(module, tableInfo.getTableName());
        List<Map<String, Object>> dbColInfos = ZKGetTableInfoUtils.getDbColInfos(module, tableInfo.getTableName());
        ZKColInfo col = null;
        List<ZKColInfo> newCols = ZKCollectionUtils.newArrayList();
        String colName = null;
        for (Map<String, Object> dbColInfo : dbColInfos) {
            colName = (String) dbColInfo.get(ZKCodeGenConstant.KeyCol.columnName);
            col = this.getByColName(tableInfo.getPkId(), colName);
            if (col == null) {
                col = new ZKColInfo();
            }
            col = ZKGetTableInfoUtils.makeColInfo(dbColInfo, pkList, col);
            // 转换字段信息
            ZKConvertUtils.convertAttrInfo(module, tableInfo, col);
            this.save(col);
            newCols.add(col);
        }
        return newCols;
    }

    // 全量更新；会删除表中不存的字段；
    @Transactional(readOnly = false)
    public List<ZKColInfo> updateAllByTable(String tableId) {
        ZKTableInfo tableInfo = zkTableInfoService.get(new ZKTableInfo(tableId));
        if (tableInfo == null) {
            log.error("[^_^:20210401-0704-001] 表:{}, 信息不存在；", tableId);
            throw new ZKCodeException("zk.codeGen.000002", "表信息不存在", tableId);
        }
        ZKModule module = zkModuleService.get(new ZKModule(tableInfo.getModuleId()));
        if (module == null) {
            log.error("[^_^:20210401-0704-001] 功能模块:{}, 不存在；", tableInfo.getModuleId());
            throw new ZKCodeException("zk.codeGen.000001", "功能模块不存在", tableInfo.getModuleId());
        }
        return this.updateAllByTable(module, tableInfo);
    }

    @Transactional(readOnly = false)
    public List<ZKColInfo> updateAllByTable(ZKModule module, ZKTableInfo tableInfo) {
        this.diskDelByTableId(tableInfo.getPkId());
        List<String> pkList = ZKGetTableInfoUtils.getPkSourceList(module, tableInfo.getTableName());
        List<Map<String, Object>> dbColInfos = ZKGetTableInfoUtils.getDbColInfos(module, tableInfo.getTableName());
        ZKColInfo col = null;
        List<ZKColInfo> newCols = ZKCollectionUtils.newArrayList();
        for (Map<String, Object> dbColInfo : dbColInfos) {
            // 生成字段信息
            col = ZKGetTableInfoUtils.makeColInfo(dbColInfo, pkList);
            // 转换字段信息
            ZKConvertUtils.convertAttrInfo(module, tableInfo, col);
            this.save(col);
            newCols.add(col);
        }
        return newCols;
    }

    // 根据表ID 删除字段信息
    @Transactional(readOnly = false)
    public int diskDelByTableId(String tableId) {
        int count = 0;
        List<ZKColInfo> res = this.findByTableId(tableId);
        if (res != null && !res.isEmpty()) {
            for (ZKColInfo col : res) {
                count += this.diskDel(col);
            }
        }
        return count;
    }

}
