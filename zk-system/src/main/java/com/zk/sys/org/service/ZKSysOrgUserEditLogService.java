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
* @Title: ZKSysOrgUserEditLogService.java 
* @author Vinson 
* @Package com.zk.sys.org.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 21, 2024 7:17:04 PM 
* @version V1.0 
*/
package com.zk.sys.org.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.base.service.ZKBaseService;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.security.utils.ZKSecPrincipalUtils;
import com.zk.sys.org.dao.ZKSysOrgUserEditLogDao;
import com.zk.sys.org.entity.ZKSysOrgUserEditLog;
import com.zk.sys.org.entity.ZKSysOrgUserEditLog.ZKUserEditType;

/** 
* @ClassName: ZKSysOrgUserEditLogService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
@Transactional(readOnly = true)
public class ZKSysOrgUserEditLogService extends ZKBaseService<String, ZKSysOrgUserEditLog, ZKSysOrgUserEditLogDao> {

    // 修改日志

    public ZKSysOrgUserEditLog editBase(String userId, int editFlag) {
        return this.editLog(userId, ZKUserEditType.base, editFlag);
    }

    public ZKSysOrgUserEditLog editAccount(String userId, int editFlag) {
        return this.editLog(userId, ZKUserEditType.account, editFlag);
    }

    public ZKSysOrgUserEditLog editJobnum(String userId, int editFlag) {
        return this.editLog(userId, ZKUserEditType.jobnum, editFlag);
    }

    public ZKSysOrgUserEditLog editMail(String userId, int editFlag) {
        return this.editLog(userId, ZKUserEditType.mail, editFlag);
    }

    public ZKSysOrgUserEditLog editPhone(String userId, int editFlag) {
        return this.editLog(userId, ZKUserEditType.phone, editFlag);
    }

    public ZKSysOrgUserEditLog editPwd(String userId, int editFlag) {
        return this.editLog(userId, ZKUserEditType.pwd, editFlag);
    }

    public ZKSysOrgUserEditLog editStatus(String userId, int editFlag) {
        return this.editLog(userId, ZKUserEditType.status, editFlag);
    }

    private ZKSysOrgUserEditLog editLog(String userId, int editType, int editFlag) {
        ZKSysOrgUserEditLog el = ZKSysOrgUserEditLog.as(userId, editType, editFlag);
        if (super.save(el) > 0) {
            return el;
        }
        else {
            return null;
        }
    }

    // 查询指定修改类型的修改记录列表
    public List<ZKSysOrgUserEditLog> findByEditType(String userId, int editType) {
        if (ZKStringUtils.isEmpty(userId)) {
            return Collections.emptyList();
        }
        return this.dao.findByEditType(ZKSysOrgUserEditLog.sqlHelper().getTableName(),
                ZKSysOrgUserEditLog.sqlHelper().getTableAlias(), ZKSysOrgUserEditLog.sqlHelper().getBlockSqlCols(),
                userId, editType);
    }

    // 查询指定修改类型的最新修改记录
    public ZKSysOrgUserEditLog getLatestEditLog(String userId, int editType) {
        List<ZKSysOrgUserEditLog> els = this.findByEditType(userId, editType);
        if (els == null || els.isEmpty()) {
            return null;
        }
        return els.get(0);
    }

    // 根据用户做逻辑删除
    public int delByUserId(String userId) {
        return this.dao.delByUserId(ZKSysOrgUserEditLog.sqlHelper().getTableName(), userId,
                ZKBaseEntity.DEL_FLAG.delete, ZKSecPrincipalUtils.getSecPrincipalService().getUserId(),
                ZKDateUtils.getToday());
    }

    // 根据用户做物理删除
    public int diskDelByUserId(String userId) {
        return this.dao.diskDelByUserId(ZKSysOrgUserEditLog.sqlHelper().getTableName(), userId);
    }

}
