package com.zk.demo.web.service;

/**
 * Copyright (c) 2017-2022 Vinson. address: All rights reserved This software is the confidential and proprietary
 * information of Vinson. ("Confidential Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered into with Vinson;
 *
 * @Description: demo service
 * @ClassName ZKJavaDemoService
 * @Package com.zk.demo.service
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-05 09:14:11
 **/

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.zk.core.commons.data.ZKPage;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.demo.web.dao.ZKJavaDemoDao;
import com.zk.demo.web.entity.ZKJavaDemoEntity;

@Service
@Transactional(readOnly = true)
public class ZKJavaDemoService {
    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    @Autowired
    ZKJavaDemoDao dao;

    /**
     * 保存数据（插入或更新）
     *
     * @param entity
     * @throws ZKValidatorException
     */
    @Transactional(readOnly = false)
    public int save(ZKJavaDemoEntity entity) throws ZKValidatorException {
        try {
            if (entity.isNewRecord()) {
                entity.preInsert();
                return dao.insert(entity);
            } else {
                entity.preUpdate();
                return dao.update(entity);
            }
        } catch (UncategorizedSQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private ZKJavaDemoEntity getEntity(String pkId) {
        return new ZKJavaDemoEntity(pkId);
    }

    public ZKJavaDemoEntity get(String pkId) {
        ZKJavaDemoEntity entity = this.getEntity(pkId);
        return dao.get(entity);
    }

    /**
     * 获取单条数据
     *
     * @param entity
     * @return
     */
    public ZKJavaDemoEntity get(ZKJavaDemoEntity entity) {
        return dao.get(entity);
    }

    /**
     * 查询列表数据
     *
     * @param entity
     * @return
     */
    public List<ZKJavaDemoEntity> findList(ZKJavaDemoEntity entity) {
        return dao.findList(entity);
    }

    /**
     * 查询分页数据
     *
     * @param page
     *            分页对象
     * @param entity
     * @return
     */
    public ZKPage<ZKJavaDemoEntity> findPage(ZKPage<ZKJavaDemoEntity> page, ZKJavaDemoEntity entity) {
        entity.setPage(page);
        page.setResult(dao.findList(entity));
        return page;
    }

    /**
     * 物理删除数据
     *
     * @param entity
     * @return
     */
    @Transactional(readOnly = false)
    public int diskDel(ZKJavaDemoEntity entity) {
        return dao.diskDel(entity);
    }

    /*****************/
    /**
     * 测试行锁
     * 
     * @MethodName updateRow
     * @param entity
     * @param sleepTime
     *            休眠时长，毫秒，小于1时，不休眠
     * @return int
     * @throws
     * @Author bs
     * @DATE 2022-09-05 10:15:294
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
    public int updateRowLock(ZKJavaDemoEntity entity, int sleepTime) {
        if (!ZKStringUtils.isEmpty(entity.getPkId())) {
            // id 不为空，添加行锁
            System.out.println("[^_^: 20220905-1454-001] ============================ 添加行锁：" + entity.getPkId());
            this.getForUpdate(entity.getPkId());
        }
        if (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw ZKExceptionsUtils.unchecked(e);
            }
        }
        return this.save(entity);
    }

    // 查询加行锁
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ZKJavaDemoEntity getForUpdate(String pkId) {
        return this.dao.getForUpdate(ZKJavaDemoEntity.sqlHelper().getTableName(),
            ZKJavaDemoEntity.sqlHelper().getTableAlias(), ZKJavaDemoEntity.sqlHelper().getBlockSqlCols(), pkId);
    }

}
