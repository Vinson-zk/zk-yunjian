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
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.ZKValidatorException;
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

    // ------------------------------------------------------
    /**
     * 更新表信息
     *
     * @Title: makeTableInfo
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 27, 2024 2:18:57 PM
     * @param isForce
     *            是否强制更新？true-强制更新；false-当表信息已存在时，不强制更新；
     * @param moduleId
     *            模块ID
     * @param targetTableNames
     *            指定生成的目标表，为空或为 null 时，制作模块下所有的表信息
     * @return
     * @return List<ZKTableInfo>
     */
    protected List<ZKTableInfo> makeTableInfo(boolean isForce, String moduleId, String... targetTableNames) {
        // module 是否存在
        ZKModule module = this.zkModuleService.get(new ZKModule(moduleId));
        if (module == null) {
            log.error("[^_^:20230306-1203-001] 功能模块:{}, 不存在；", moduleId);
            throw ZKBusinessException.as("zk.codeGen.000001", null, moduleId);
        }
        return this.makeTableInfo(isForce, module, targetTableNames);
    }

    /**
     * 制作表信息
     *
     * @Title: makeTableInfo
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 6, 2023 11:10:36 PM
     * @param isCover
     *            是否覆盖；true-覆盖，重新生成；false-如果表信息存在，不重新生成；
     * @param module
     *            模块
     * @param targetTableNames
     *            指定生成的目标表，为空或为 null 时，制作模块下所有的表信息
     * @return List<ZKTableInfo>
     */
    protected List<ZKTableInfo> makeTableInfo(boolean isCover, ZKModule module, String... targetTableNames) {

        List<Map<String, Object>> dbTableInfos = ZKGetTableInfoUtils.getDbTableInfos(module, null);
        List<ZKTableInfo> res = new ArrayList<>();
        if (targetTableNames == null || targetTableNames.length < 1) {
            // 目标表名数组为空，生成模板下所有表信息
            for (Map<String, Object> dbTableInfo : dbTableInfos) {
                res.add(this.makeTableInfo(isCover, module, dbTableInfo));
            }
        }
        else {
            // 目标表名数组不为空，仅生成指定表的表信息
            String tempTableName = null;
            ZKTableInfo tableInfo = null;
            for (String tableName : targetTableNames) {
                tableInfo = null;
                for (Map<String, Object> dbTableInfo : dbTableInfos) {
                    tempTableName = (String) dbTableInfo.get(ZKCodeGenConstant.KeyTable.tableName);
                    if (tempTableName.equals(tableName)) {
                        tableInfo = this.makeTableInfo(isCover, module, dbTableInfo);
                        break;
                    }
                }
                if (tableInfo == null) {
                    log.error("[^_^:20230306-1203-003] 模块[{}], 未找到指定表[{}]", module.getModuleName(), tableName);
                    throw ZKBusinessException.as("zk.codeGen.000006", null, module.getModuleName(), tableName);
                }
                else {
                    res.add(tableInfo);
                }
            }
        }
        return res;
    }

    /**
     * 制作表信息
     *
     * @Title: makeTableInfo
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 6, 2023 11:08:04 PM
     * @param isCover
     *            是否覆盖；true-覆盖，重新生成；false-如果表信息存在，不重新生成；
     * @param module
     *            模块
     * @param dbTableInfo
     *            指定生成的目标表，为空或为 null 时，制作模块下所有的表信息
     * @return ZKTableInfo
     */
    protected ZKTableInfo makeTableInfo(boolean isCover, ZKModule module, Map<String, Object> dbTableInfo) {
        if (module == null) {
            log.error("[^_^:20230306-1203-002] 功能模块, 不存在；");
            throw ZKBusinessException.as("zk.codeGen.000001", null);
        }

        List<String> pkList = null;
        String tableName = (String) dbTableInfo.get(ZKCodeGenConstant.KeyTable.tableName);
        ZKTableInfo tableInfo = null;
        // 生成表信息的目标表
        tableInfo = this.getByTableName(module.getPkId(), tableName);
        if (tableInfo == null || isCover) {
            tableInfo = new ZKTableInfo();
            pkList = ZKGetTableInfoUtils.getPkSourceList(module, tableName);
            // 生成表信息
            tableInfo = ZKGetTableInfoUtils.makeTableInfo(dbTableInfo, pkList);
            // 新表，表信息还没生成；转换表信息
            ZKConvertUtils.convertTableInfo(module, tableInfo);
        }
        return tableInfo;
    }

    // ------------------------------------------------------

    // 取模块表的列表信息；不分页，仅查询已更新到代码生成库的表列表，不会自动取模块数据中新创建的表；
    @Transactional(readOnly = false)
    public List<ZKTableInfo> getTablesByModule(String moduleId) {
        ZKTableInfo pti = new ZKTableInfo();
        pti.setModuleId(moduleId);
        List<ZKTableInfo> res = this.findList(pti);
        ZKModule module = this.zkModuleService.get(new ZKModule(moduleId));
        for (ZKTableInfo ti : res) {
            ti.setModule(module);
        }
        return res;
    }

    // 更新表列表；更新表列表；从模块的数据库中读取表列表，更新到代码生成的表列表中；
    @Transactional(readOnly = false)
    public List<ZKTableInfo> updateTableList(String moduleId) {
        List<ZKTableInfo> res = this.makeTableInfo(false, moduleId);
        this.saveBatch(res);
        return res;
    }

    /**
     * 更新表信息
     *
     * @Title: updateTableInfo
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 6, 2023 11:41:51 PM
     * @param isCover
     *            是否覆盖；true-覆盖，重新生成；false-如果表信息存在，不重新生成；
     * @param moduleId
     * @param tableName
     * @return
     * @return ZKTableInfo
     */
    @Transactional(readOnly = false)
    public ZKTableInfo updateTableInfo(boolean isCover, String moduleId, String tableName) {
        ZKModule module = this.zkModuleService.get(new ZKModule(moduleId));
        if (module == null) {
            log.error("[^_^:20210401-0704-003] 功能模块:{}, 不存在；", moduleId);
            throw ZKBusinessException.as("zk.codeGen.000001", null, moduleId);
        }
        List<ZKTableInfo> res = this.makeTableInfo(isCover, module, tableName);
        this.saveBatch(res);
        return res.get(0);
    }

    // 更新表信息
    @Transactional(readOnly = false)
    public ZKTableInfo updateTableInfo(String tableId) {
        ZKTableInfo tableInfo = this.get(new ZKTableInfo(tableId));
        if (tableInfo == null) {
            log.error("[^_^:20210401-0842-003] 表:{}, 信息不存在；", tableId);
            throw ZKBusinessException.as("zk.codeGen.000002", null, tableId);
        }
        ZKModule module = this.zkModuleService.get(new ZKModule(tableInfo.getModuleId()));
        if (module == null) {
            log.error("[^_^:20210401-0704-003] 功能模块:{}, 不存在；", tableInfo.getModuleId());
            throw ZKBusinessException.as("zk.codeGen.000001", null, tableInfo.getModuleId());
        }
        List<ZKTableInfo> res = this.makeTableInfo(true, module, tableInfo.getTableName());
        this.saveBatch(res);
        return res.get(0);
    }

//    @Transactional(readOnly = false)
//    public ZKTableInfo updateTableInfo(ZKModule module, ZKTableInfo tableInfo) {
//        List<ZKTableInfo> res = this.makeTableInfo(true, module, tableInfo.getTableName());
//        this.saveBatch(res);
//        return res.get(0);
//    }

    /**
     * 保存数据（插入或更新）
     * 
     * @param entity
     * @throws ZKValidatorException
     */
    @Transactional(readOnly = false)
    public int save(ZKTableInfo entity) throws ZKValidatorException {
        ZKTableInfo old = this.getByTableName(entity.getModuleId(), entity.getTableName());
        if (old != null) {
            entity.setPkId(old.getPkId());
        }
        return super.save(entity);
    }

    /**
     * 根据表名，取表信息；注：不包含表的字段信息
     *
     * @Title: getByTableName
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 31, 2021 9:59:43 AM
     * @param moduleId
     * @param tableName
     * @return
     * @return ZKTableInfoDao
     */
    public ZKTableInfo getByTableName(String moduleId, String tableName) {
        return this.dao.getByTableName(ZKTableInfo.sqlHelper().getTableName(),
                ZKTableInfo.sqlHelper().getBlockSqlCols(""), moduleId, tableName);
    }

    @Override
    @Transactional(readOnly = false)
    public int diskDel(ZKTableInfo ti) {
        this.zkColInfoService.diskDelByTableId(ti.getPkId());
        return super.diskDel(ti);
    }

}
