/**
 * 
 */
package com.zk.wechat.platformBusiness.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.wechat.platformBusiness.dao.ZKFuncKeyConfigDao;
import com.zk.wechat.platformBusiness.entity.ZKFuncKeyConfig;
import com.zk.wechat.platformBusiness.entity.ZKFuncKeyType;

/**
 * ZKFuncKeyConfigService
 * 
 * @author
 * @version
 */
@Service
@Transactional(readOnly = true)
public class ZKFuncKeyConfigService extends ZKBaseService<String, ZKFuncKeyConfig, ZKFuncKeyConfigDao> {

    @Autowired
    ZKFuncKeyTypeService funcKeyTypeService;

    /**
     * 根据 FuncKey 取，功能配置实体
     *
     * @Title: getByFuncKey
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 24, 2022 11:42:50 AM
     * @param funcKey
     * @param delFlag
     *            删除标识，为null时，查询所有
     * @return
     * @return ZKFuncKeyConfig
     */

    public ZKFuncKeyConfig getByFuncKey(String funcKey, Integer delFlag) {
        if (ZKStringUtils.isEmpty(funcKey)) {
            return null;
        }
        return this.dao.getByFuncKey(ZKFuncKeyConfig.sqlHelper().getTableName(),
            ZKFuncKeyConfig.sqlHelper().getTableAlias(), ZKFuncKeyConfig.sqlHelper().getBlockSqlCols(), funcKey,
            delFlag);
    }

    @Override
    @Transactional(readOnly = false)
    public int save(ZKFuncKeyConfig funcKeyConfig) {
        if (funcKeyConfig.isNewRecord()) {
            // 新记录，判断代码是否唯一
            ZKFuncKeyConfig old = this.getByFuncKey(funcKeyConfig.getFuncKey(), null);
            if (old != null) {
                if (old.getDelFlag().intValue() == ZKBaseEntity.DEL_FLAG.normal) {
                    log.error("[>_<:20220524-1510-002] zk.wechat.110002=功能配置key[{}]已存在；", funcKeyConfig.getFuncKey());
                    Map<String, String> validatorMsg = Maps.newHashMap();
                    validatorMsg.put("funcKey", ZKMsgUtils.getMessage("zk.wechat.110002", funcKeyConfig.getFuncKey()));
                    throw ZKValidatorException.as(validatorMsg);
                } else {
                    // funcKeyConfig.setDelFlag(ZKBaseEntity.DEL_FLAG.normal);
                    funcKeyConfig.setPkId(old.getPkId());
                    super.restore(funcKeyConfig);
                }
            }

            ZKFuncKeyType funcKeyType = funcKeyTypeService.get(funcKeyConfig.getFuncTypeId());
            if (funcKeyType != null) {
                funcKeyConfig.setFuncTypeId(funcKeyType.getPkId());
                funcKeyConfig.setFuncTypeCode(funcKeyType.getFuncTypeCode());
            }
        }

        return super.save(funcKeyConfig);
    }

}