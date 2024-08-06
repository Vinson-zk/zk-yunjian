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
import com.zk.core.exception.ZKBusinessException;
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

    // ---------------------------------------------------------------
    /**
     * 制作表字段信息; 不会删除已存在的表字段信息；
     *
     * @Title: makeColInfo
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 6, 2023 11:05:56 PM
     * @param isCover
     * @param module
     * @param tableInfo
     * @return
     * @return List<ZKColInfo>
     */
    protected List<ZKColInfo> makeColInfo(boolean isCover, ZKModule module, ZKTableInfo tableInfo) {
        List<String> pkList = ZKGetTableInfoUtils.getPkSourceList(module, tableInfo.getTableName());
        List<Map<String, Object>> dbColInfos = ZKGetTableInfoUtils.getDbColInfos(module, tableInfo.getTableName());
        List<ZKColInfo> cols = ZKCollectionUtils.newArrayList();
        String colName = null;
        ZKColInfo col = null;
        for (Map<String, Object> dbColInfo : dbColInfos) {
            col = null;
            colName = (String) dbColInfo.get(ZKCodeGenConstant.KeyCol.columnName);
            col = this.getByColName(tableInfo.getPkId(), colName);
            if (col == null || isCover) {
                // 表字段信息不存在，或覆盖重新生成
                col = new ZKColInfo();
                col = ZKGetTableInfoUtils.makeColInfo(dbColInfo, pkList, col);
                // 转换字段信息
                ZKConvertUtils.convertAttrInfo(module, tableInfo, col);
            }
            cols.add(col);
        }
        return cols;
    }
    // ---------------------------------------------------------------

    // 查询指定表的 字段信息；这里不会通过数据库表生成表字段信息
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
            throw ZKBusinessException.as("zk.codeGen.000002", null, tableId);
        }
        ZKModule module = zkModuleService.get(new ZKModule(tableInfo.getModuleId()));
        if (module == null) {
            log.error("[^_^:20210401-0704-002] 功能模块:{}, 不存在；", tableInfo.getModuleId());
            throw ZKBusinessException.as("zk.codeGen.000001", null, tableInfo.getModuleId());
        }
        return this.updateAddByTable(module, tableInfo);
    }

    @Transactional(readOnly = false)
    private List<ZKColInfo> updateAddByTable(ZKModule module, ZKTableInfo tableInfo) {
        List<ZKColInfo> cols = this.makeColInfo(false, module, tableInfo);
        this.saveBatch(cols);
        return cols;
    }

    // 全量更新；会删除表中不存的字段；
    @Transactional(readOnly = false)
    public List<ZKColInfo> updateAllByTable(String tableId) {
        ZKTableInfo tableInfo = zkTableInfoService.get(new ZKTableInfo(tableId));
        if (tableInfo == null) {
            log.error("[^_^:20210401-0704-001] 表:{}, 信息不存在；", tableId);
            throw ZKBusinessException.as("zk.codeGen.000002", null, tableId);
        }
        ZKModule module = zkModuleService.get(new ZKModule(tableInfo.getModuleId()));
        if (module == null) {
            log.error("[^_^:20210401-0704-001] 功能模块:{}, 不存在；", tableInfo.getModuleId());
            throw ZKBusinessException.as("zk.codeGen.000001", null, tableInfo.getModuleId());
        }
        return this.updateAllByTable(module, tableInfo);
    }

    @Transactional(readOnly = false)
    private List<ZKColInfo> updateAllByTable(ZKModule module, ZKTableInfo tableInfo) {
        this.diskDelByTableId(tableInfo.getPkId());

        List<ZKColInfo> cols = this.makeColInfo(true, module, tableInfo);
        this.saveBatch(cols);
        return cols;
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
