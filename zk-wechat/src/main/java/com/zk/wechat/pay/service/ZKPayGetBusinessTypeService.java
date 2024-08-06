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
* @Title: ZKPayGetBusinessTypeService.java 
* @author Vinson 
* @Package com.zk.wechat.pay.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 20, 2021 9:03:40 AM 
* @version V1.0 
*/
package com.zk.wechat.pay.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.wechat.pay.dao.ZKPayGetBusinessTypeDao;
import com.zk.wechat.pay.entity.ZKPayGetBusinessType;

/** 
* @ClassName: ZKPayGetBusinessTypeService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
@Transactional(readOnly = true)
public class ZKPayGetBusinessTypeService extends ZKBaseService<String, ZKPayGetBusinessType, ZKPayGetBusinessTypeDao> {

    /**
     * 按 code 查询, code 需要做唯一键
     *
     * @Title: getByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 20, 2021 9:32:35 AM
     * @param code
     * @return
     * @return ZKPayGetBusinessType
     */
    ZKPayGetBusinessType getByCode(String code) {
        if (ZKStringUtils.isEmpty(code)) {
            return null;
        }
        return this.dao.getByCode(ZKPayGetBusinessType.sqlHelper().getTableName(),
                ZKPayGetBusinessType.sqlHelper().getBlockSqlCols(""), code);
    }
        
    @Override
    @Transactional(readOnly = false)
    public int save(ZKPayGetBusinessType zkPayGetBusinessType) throws ZKValidatorException {
        // 如果是新增，判断 code 是否存在
        if (zkPayGetBusinessType.isNewRecord()) {
            ZKPayGetBusinessType oldPayGetBusinessType = this.getByCode(zkPayGetBusinessType.getCode());
            if (oldPayGetBusinessType != null) {
                if (oldPayGetBusinessType.getDelFlag().intValue() == ZKBaseEntity.DEL_FLAG.normal) {
                    // code 已存在；抛出异常；
                    log.error("[>_<:20210220-0935-001] 收款业务类型代码: {} 已存在!", oldPayGetBusinessType.getCode());
                    throw ZKBusinessException.as("zk.wechat.000005", null, oldPayGetBusinessType.getCode());
                }
                else {
                    // code 已存在，只是已逻辑删除；先物理删除已存在的；
                    this.diskDel(oldPayGetBusinessType);
                }
            }
        }
        return super.save(zkPayGetBusinessType);
    }

}
