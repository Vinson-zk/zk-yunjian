/**
 * 
 */
package com.zk.sys.settings.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.sys.sec.common.ZKSysSecConstants;
import com.zk.sys.settings.dao.ZKSysSetItemDao;
import com.zk.sys.settings.entity.ZKSysSetCollection;
import com.zk.sys.settings.entity.ZKSysSetItem;

/**
 * ZKSysSetItemService
 * 
 * @author
 * @version
 */
@Service
@Transactional(readOnly = true)
public class ZKSysSetItemService extends ZKBaseService<String, ZKSysSetItem, ZKSysSetItemDao> {

    @Autowired
    private ZKSysSetCollectionService sysSetCollectionService;

    /**
     * 根据配置组别代码和配置项代码查询配置项
     *
     * @Title: getByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 11, 2022 4:28:11 PM
     * @param collectionCode
     * @param Code
     * @return
     * @return ZKSysSetItem
     */
    public ZKSysSetItem getByCode(String collectionCode, String Code) {
        return this.dao.getByCode(ZKSysSetItem.sqlHelper().getTableName(), ZKSysSetItem.sqlHelper().getTableAlias(),
            ZKSysSetItem.sqlHelper().getBlockSqlCols(), collectionCode, Code);
    }

    @Override
    @Transactional(readOnly = false)
    public int save(ZKSysSetItem setItem) {
        if (setItem.isNewRecord()) {
            // 判断组别
            if (ZKStringUtils.isEmpty(setItem.getCollectionId())) {
                log.error("[>_<:20220510-0105-002] 配置项组别必须选择；");
                Map<String, String> validatorMsg = Maps.newHashMap();
                validatorMsg.put("collectionId", ZKMsgUtils.getMessage("zk.sys.030004"));
                throw ZKValidatorException.as(validatorMsg);
            }
            ZKSysSetCollection setCollection = this.sysSetCollectionService.get(setItem.getCollectionId());
            if (setCollection == null) {
                log.error("[>_<:20220510-0108-001] 配置项组别不存在，组别[{}]", setItem.getPkId());
                throw ZKBusinessException.as("zk.sys.030001", "配置项组别不存在");
            } else {
                setItem.setCollectionCode(setCollection.getCode());
            }

            // 新增，校验代码是否存在
            ZKSysSetItem old = this.getByCode(setItem.getCollectionCode(), setItem.getCode());
            if (old != null) {
                if (ZKSysSetCollection.DEL_FLAG.normal == old.getDelFlag().intValue()) {
                    // 配置项代码已存在
                    log.error("[>_<:20220510-0105-001] 配置项代码已存在；code: {} ", old.getCode());
                    Map<String, String> validatorMsg = Maps.newHashMap();
                    validatorMsg.put("code", ZKMsgUtils.getMessage("zk.sys.030002", setItem.getCode()));
                    throw ZKValidatorException.as(validatorMsg);
                } else {
                    // 逻辑删除，重新启用
                    // setItem.setDelFlag(ZKSysSetItem.DEL_FLAG.normal);
                    setItem.setPkId(old.getPkId());
                    super.restore(setItem);
                }
            }
        }

        if (ZKSysSecConstants.Key_Auth_Strategy.equals(setItem.getCollectionCode())) {
            // 是修改了权限策略; 清空权限缓存
            ZKUserCacheUtils.cleanAllAuth();
        }

        return super.save(setItem);
    }

}
