/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKBaseService.java 
 * @author Vinson 
 * @Package com.zk.base.service 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 2:23:47 PM 
 * @version V1.0   
*/
package com.zk.base.service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.dao.ZKBaseDao;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.ZKValidationGroup;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.exception.base.ZKUnknownException;
import com.zk.core.utils.ZKClassUtils;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.utils.ZKValidatorsBeanUtils;
import com.zk.security.service.ZKSecPrincipalService;
import com.zk.security.utils.ZKSecPrincipalUtils;

import jakarta.validation.Validator;

/** 
* @ClassName: ZKBaseService 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKBaseService<ID extends Serializable, E extends ZKBaseEntity<ID, E>, D extends ZKBaseDao<ID, E>> {
    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    /**
     * 验证Bean实例对象
     */
    @Autowired
    protected Validator validator;

    /**
     * 持久层对象
     */
    @Autowired
    protected D dao;

    @Autowired(required = false)
    ZKSecPrincipalService zkSecPrincipalService;

    /**
     * 插入之前执行方法，子类实现
     */
    public void preInsert(E entity) {
        // 不限制ID为UUID，调用 setIsNewRecord() 使用自定义ID
        if (entity.getPkId() == null || "".equals(entity.toString())) {
            entity.setPkId(entity.genId());
        }
        if (zkSecPrincipalService != null) {
            try {
                entity.setCreateUserId(zkSecPrincipalService.getUserId());
            }
            catch(ZKUnknownException e) {
                log.error("[>_<:20240618-0034-001] 插入数据时，获取当前用户信息失败，但不影响运行！");
                e.printStackTrace();
            }
        }
        entity.setUpdateUserId(entity.getCreateUserId());
        entity.setCreateDate(ZKDateUtils.getToday());
        entity.setUpdateDate(entity.getCreateDate());
        entity.setVersion(0L);
    }

    /**
     * 更新之前执行方法，子类实现
     */
    public void preUpdate(E entity) {
        ZKSecPrincipalService zkSecPrincipalService = ZKSecPrincipalUtils.getSecPrincipalService();
        if (zkSecPrincipalService != null) {
            try {
                entity.setUpdateUserId(zkSecPrincipalService.getUserId());
            }
            catch(ZKUnknownException e) {
                log.error("[>_<:20240618-0034-002] 修改数据时，获取当前用户信息失败，但不影响运行！");
                e.printStackTrace();
            }
        }
        entity.setUpdateDate(ZKDateUtils.getToday());
    }

    /**
     * 服务端参数有效性验证
     * 
     * @param returnMsgList
     *            返回信息的输出参数
     * @param object
     *            验证的实体对象
     * @param groups
     *            验证组
     * @return 验证成功：返回true；严重失败：将错误信息添加到 returnMsgList 中
     */
    public boolean beanValidator(List<String> returnMsgList, Object object, Class<?>... groups) {
        try {
            ZKValidatorsBeanUtils.validateWithException(validator, object, groups);
        }
        catch(ZKValidatorException ex) {
            List<String> list = ex.getMessagePropertyAndMessageAsList();
            if (returnMsgList != null) {
                returnMsgList.addAll(list);
            }
            return false;
        }
        return true;
    }

    /**
     * 服务端参数有效性验证
     * 
     * @param object
     *            验证的实体对象
     * @param groups
     *            验证组，不传入此参数时，同@Valid注解验证
     * @return 验证成功：继续执行；验证失败：抛出异常跳转400页面。
     */
    protected void beanValidator(Object object, Class<?>... groups) throws ZKValidatorException {
        ZKValidatorsBeanUtils.validateWithException(validator, object, groups);
    }

    protected void beanValidator(Object object, String propertyName, Class<?>... groups) throws ZKValidatorException {
        ZKValidatorsBeanUtils.validateWithException(validator, object, propertyName, groups);
    }

    /**
     * 保存数据（插入或更新）
     * 
     * @param entity
     * @throws ZKValidatorException
     */
    @Transactional(readOnly = false)
    public int save(E entity) throws ZKValidatorException {
        try {
            if (entity.isNewRecord()) {
//                entity.preInsert();
                this.preInsert(entity);
                this.beanValidator(entity);
                this.beanValidator(entity, ZKValidationGroup.Insert.class);
                this.beanValidator(entity, ZKValidationGroup.Update.class);
                return dao.insert(entity);
            }
            else {
//                entity.preUpdate();
                this.preUpdate(entity);
                this.beanValidator(entity);
                this.beanValidator(entity, ZKValidationGroup.Update.class);
                return dao.update(entity);
            }
        }
        catch(UncategorizedSQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = false)
    public int saveBatch(E... entitys) throws ZKValidatorException {
        int res = 0;
        for (E e : entitys) {
            res += this.save(e);
        }
        return res;
    }

    @Transactional(readOnly = false)
    public int saveBatch(List<E> entitys) throws ZKValidatorException {
        int res = 0;
        for (E e : entitys) {
            res += this.save(e);
        }
        return res;
    }

    private E getEntity(ID pkId) {
        Class<E> classz = ZKClassUtils.getSuperclassByName(ZKBaseService.class, this.getClass(), "E");
        try {
            return ZKClassUtils.newInstance(classz, pkId);
        }
        catch(InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw ZKExceptionsUtils.unchecked(e);
        }
    }

    public E get(ID pkId) {
        E entity = this.getEntity(pkId);
        return dao.get(entity);
    }

    /**
     * 获取单条数据
     * @param entity
     * @return
     */
    public E get(E entity) {
        return dao.get(entity);
    }

    /**
     * 查询列表数据
     * @param entity
     * @return
     */
    public List<E> findList(E entity) {
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
    public ZKPage<E> findPage(ZKPage<E> page, E entity) {
        entity.setPage(page);
        page.setResult(dao.findList(entity));
        return page;
    }

    /**
     * 删除数据
     * 
     * @param entity
     */
    @Transactional(readOnly = false)
    public int del(E entity) {
        entity.setDelFlag(ZKBaseEntity.DEL_FLAG.delete);
//        entity.preUpdate();
        this.preUpdate(entity);
        return dao.del(entity);
    }

    /**
     * 恢复数据
     * 
     * @param entity
     */
    @Transactional(readOnly = false)
    public int restore(E entity) {
        entity.setDelFlag(ZKBaseEntity.DEL_FLAG.normal);
//        entity.preUpdate();
        this.preUpdate(entity);
        return dao.del(entity);
    }

    /**
     * 物理删除数据
     * 
     * @param entity
     * @return
     */
    @Transactional(readOnly = false)
    public int diskDel(E entity) {
        return dao.diskDel(entity);
    }

}
