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
* @Title: ZKPayGroupService.java 
* @author Vinson 
* @Package com.zk.wechat.pay.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 22, 2021 4:47:48 PM 
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
import com.zk.wechat.pay.dao.ZKPayGroupDao;
import com.zk.wechat.pay.entity.ZKPayGroup;

/** 
* @ClassName: ZKPayGroupService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
@Transactional(readOnly = true)
public class ZKPayGroupService extends ZKBaseService<String, ZKPayGroup, ZKPayGroupDao> {

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
    public ZKPayGroup getByCode(String code) {
        if (ZKStringUtils.isEmpty(code)) {
            return null;
        }
        return this.dao.getByCode(ZKPayGroup.sqlHelper().getTableName(),
                ZKPayGroup.sqlHelper().getBlockSqlCols(""),
                code);
    }

    @Override
    @Transactional(readOnly = false)
    public int save(ZKPayGroup zkPayGroup) throws ZKValidatorException {
        // 如果是新增，判断 code 是否存在
        if (zkPayGroup.isNewRecord()) {
            ZKPayGroup oldPayGroup = this.getByCode(zkPayGroup.getCode());
            if (oldPayGroup != null) {
                if (oldPayGroup.getDelFlag().intValue() == ZKBaseEntity.DEL_FLAG.normal) {
                    // code 已存在；抛出异常；
                    log.error("[>_<:20210221-1015-001] 后台系统的支付关联组: {} 已存在!", oldPayGroup.getCode());
                    throw ZKBusinessException.as("zk.wechat.000011", null, oldPayGroup.getCode());
                }
                else {
                    // code 已存在，只是已逻辑删除；先物理删除已存在的；
                    this.diskDel(oldPayGroup);
                }
            }
        }
        return super.save(zkPayGroup);
    }

}
