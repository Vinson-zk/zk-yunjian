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
* @Title: ZKSysOrgUserOptLogService.java 
* @author Vinson 
* @Package com.zk.sys.org.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 21, 2024 7:17:04 PM 
* @version V1.0 
*/
package com.zk.sys.org.service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.base.service.ZKBaseService;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.configuration.ZKCoreThreadPoolProperties;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.security.utils.ZKSecPrincipalUtils;
import com.zk.sys.org.dao.ZKSysOrgUserOptLogDao;
import com.zk.sys.org.entity.ZKSysOrgUserOptLog;
import com.zk.sys.org.entity.ZKSysOrgUserOptLog.ZKUserOptType;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @ClassName: ZKSysOrgUserOptLogService
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class ZKSysOrgUserOptLogService extends ZKBaseService<String, ZKSysOrgUserOptLog, ZKSysOrgUserOptLogDao> {

//    @Autowired
//    @Qualifier("userOptLogThreadPoolProperties")
    @Resource(name = "userOptLogThreadPoolProperties")
    ZKCoreThreadPoolProperties userOptLogThreadPoolProperties;

    private ExecutorService optLogExecutorService = null;

    public ExecutorService getLoExecutorService() {
        if (optLogExecutorService == null) {
            /*
             * int corePoolSize, int maximumPoolSize, long keepAliveTime,
             * 
             * new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10))
             */
            optLogExecutorService = new ThreadPoolExecutor(userOptLogThreadPoolProperties.getCorePoolSize(),
                    userOptLogThreadPoolProperties.getMaximumPoolSize(),
                    userOptLogThreadPoolProperties.getKeepAliveTime(), userOptLogThreadPoolProperties.getUnit(),
                    new ArrayBlockingQueue<Runnable>(userOptLogThreadPoolProperties.getWorkQueueCount()));
        }
        return optLogExecutorService;
    }

    // 创建日志实体
    public static ZKSysOrgUserOptLog as(String userId, int optType, int optTypeFlag, ZKJson optContent,
            HttpServletRequest req) {
        ZKSysOrgUserOptLog optLog = ZKSysOrgUserOptLog.as(userId, ZKWebUtils.getRemoteAddr(req), null, optType,
                optTypeFlag, optContent);
        return optLog;
    }

    // 操作日志
    @Transactional(readOnly = false)
    public ZKSysOrgUserOptLog optLogEditBase(String userId, int optTypeFlag, ZKJson optContent,
            HttpServletRequest req) { // , HttpServletRequest req
        return this.saveOptLog(userId, ZKUserOptType.base, optTypeFlag, optContent, req);
    }

    @Transactional(readOnly = false)
    public ZKSysOrgUserOptLog optLogEditAccount(String userId, int optTypeFlag, ZKJson optContent,
            HttpServletRequest req) {
        return this.saveOptLog(userId, ZKUserOptType.account, optTypeFlag, optContent, req);
    }

    @Transactional(readOnly = false)
    public ZKSysOrgUserOptLog optLogEditJobnum(String userId, int optTypeFlag, ZKJson optContent,
            HttpServletRequest req) {
        return this.saveOptLog(userId, ZKUserOptType.jobnum, optTypeFlag, optContent, req);
    }

    @Transactional(readOnly = false)
    public ZKSysOrgUserOptLog optLogEditMail(String userId, int optTypeFlag, ZKJson optContent,
            HttpServletRequest req) {
        return this.saveOptLog(userId, ZKUserOptType.mail, optTypeFlag, optContent, req);
    }

    @Transactional(readOnly = false)
    public ZKSysOrgUserOptLog optLogEditPhone(String userId, int optTypeFlag, ZKJson optContent,
            HttpServletRequest req) {
        return this.saveOptLog(userId, ZKUserOptType.phone, optTypeFlag, optContent, req);
    }

    @Transactional(readOnly = false)
    public ZKSysOrgUserOptLog optLogEditPwd(String userId, int optTypeFlag, ZKJson optContent, HttpServletRequest req) {
        return this.saveOptLog(userId, ZKUserOptType.pwd, optTypeFlag, optContent, req);
    }

    @Transactional(readOnly = false)
    public ZKSysOrgUserOptLog optLogEditStatus(String userId, int optTypeFlag, ZKJson optContent,
            HttpServletRequest req) {
        return this.saveOptLog(userId, ZKUserOptType.status, optTypeFlag, optContent, req);
    }

    @Transactional(readOnly = false)
    public ZKSysOrgUserOptLog optLogLogin(String userId, int optTypeFlag, ZKJson optContent, HttpServletRequest req) {
        return this.saveOptLog(userId, ZKUserOptType.login, optTypeFlag, optContent, req);
    }

    @Transactional(readOnly = false)
    private ZKSysOrgUserOptLog saveOptLog(String userId, int optType, int optTypeFlag, ZKJson optContent,
            HttpServletRequest req) {
        // 创建操作日志，使用到了 request，所以不能放到子线程中，存在 request 被销毁的风险
        ZKSysOrgUserOptLog optLog = as(userId, optType, optTypeFlag, optContent, req);

//        saveOptLog(optLog);
        this.getLoExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                saveOptLog(optLog);
            }
        });
        return optLog;
    }

    @Transactional(readOnly = false)
    private ZKSysOrgUserOptLog saveOptLog(ZKSysOrgUserOptLog optLog) {
        if (super.save(optLog) > 0) {
            return optLog;
        }
        else {
            return null;
        }
    }

    // 查询指定修改类型的修改记录列表
    public List<ZKSysOrgUserOptLog> findByEditType(String userId, int editType) {
        if (ZKStringUtils.isEmpty(userId)) {
            return Collections.emptyList();
        }
        return this.dao.findByEditType(ZKSysOrgUserOptLog.sqlHelper().getTableName(),
                ZKSysOrgUserOptLog.sqlHelper().getTableAlias(), ZKSysOrgUserOptLog.sqlHelper().getBlockSqlCols(),
                userId, editType);
    }

    // 查询指定修改类型的最新修改记录
    public ZKSysOrgUserOptLog getLatestEditLog(String userId, int editType) {
        List<ZKSysOrgUserOptLog> els = this.findByEditType(userId, editType);
        if (els == null || els.isEmpty()) {
            return null;
        }
        return els.get(0);
    }

    // 根据用户做逻辑删除
    @Transactional(readOnly = false)
    public int delByUserId(String userId) {
        return this.dao.delByUserId(ZKSysOrgUserOptLog.sqlHelper().getTableName(), userId,
                ZKBaseEntity.DEL_FLAG.delete, ZKSecPrincipalUtils.getSecPrincipalService().getUserId(),
                ZKDateUtils.getToday());
    }

    // 根据用户做物理删除
    @Transactional(readOnly = false)
    public int diskDelByUserId(String userId) {
        return this.dao.diskDelByUserId(ZKSysOrgUserOptLog.sqlHelper().getTableName(), userId);
    }

}


