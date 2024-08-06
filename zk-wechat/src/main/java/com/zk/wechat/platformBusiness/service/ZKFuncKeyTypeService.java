/**
 * 
 */
package com.zk.wechat.platformBusiness.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.wechat.platformBusiness.dao.ZKFuncKeyTypeDao;
import com.zk.wechat.platformBusiness.entity.ZKFuncKeyType;

/**
 * ZKFuncKeyTypeService
 * 
 * @author
 * @version
 */
@Service
@Transactional(readOnly = true)
public class ZKFuncKeyTypeService extends ZKBaseService<String, ZKFuncKeyType, ZKFuncKeyTypeDao> {

    /**
     * 根据功能类型代码取功能类型实体
     *
     * @Title: getByFuncTypeCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 24, 2022 11:41:52 AM
     * @param funcTypeCode
     * @param delFlag
     *            删除标识，为null时，查询所有
     * @return
     * @return ZKFuncKeyType
     */
    ZKFuncKeyType getByFuncTypeCode(String funcTypeCode, Integer delFlag) {
        if (ZKStringUtils.isEmpty(funcTypeCode)) {
            return null;
        }
        return this.dao.getByFuncTypeCode(ZKFuncKeyType.sqlHelper().getTableName(),
            ZKFuncKeyType.sqlHelper().getTableAlias(), ZKFuncKeyType.sqlHelper().getBlockSqlCols(), funcTypeCode,
            delFlag);
    }

    @Override
    @Transactional(readOnly = false)
    public int save(ZKFuncKeyType funcKeyType) {
        if (funcKeyType.isNewRecord()) {
            // 新记录，判断代码是否唯一
            ZKFuncKeyType old = this.getByFuncTypeCode(funcKeyType.getFuncTypeCode(), null);
            if (old != null) {
                if (old.getDelFlag().intValue() == ZKBaseEntity.DEL_FLAG.normal) {
                    log.error("[>_<:20220524-1510-001] zk.wechat.110003=功能类型代码[{}]已存在；", funcKeyType.getFuncTypeCode());
                    Map<String, String> validatorMsg = Maps.newHashMap();
                    validatorMsg.put("funcTypeCode",
                        ZKMsgUtils.getMessage("zk.wechat.110002", funcKeyType.getFuncTypeCode()));
                    throw ZKValidatorException.as(validatorMsg);
                } else {
                    // funcKeyType.setDelFlag(ZKBaseEntity.DEL_FLAG.normal);
                    funcKeyType.setPkId(old.getPkId());
                    super.restore(funcKeyType);
                }
            }
        }

        return super.save(funcKeyType);
    }

}