/**
 * 
 */
package com.zk.sys.settings.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.sys.settings.dao.ZKSysSetCollectionDao;
import com.zk.sys.settings.entity.ZKSysSetCollection;

/**
 * ZKSysSetCollectionService
 * 
 * @author
 * @version
 */
@Service
@Transactional(readOnly = true)
public class ZKSysSetCollectionService extends ZKBaseService<String, ZKSysSetCollection, ZKSysSetCollectionDao> {

    /**
     * 根据组别代码查询配置项组别
     *
     * @Title: getByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 9, 2022 11:57:43 PM
     * @param Code
     * @return
     * @return ZKSysSetCollection
     */
    public ZKSysSetCollection getByCode(String Code) {
        return this.dao.getByCode(ZKSysSetCollection.sqlHelper().getTableName(),
            ZKSysSetCollection.sqlHelper().getTableAlias(), ZKSysSetCollection.sqlHelper().getBlockSqlCols(), Code);
    }

    @Override
    @Transactional(readOnly = false)
    public int save(ZKSysSetCollection setCollection) {
        if (setCollection.isNewRecord()) {
            // 新增，校验组别代码是否存在
            ZKSysSetCollection old = this.getByCode(setCollection.getCode());
            if (old != null) {
                if (ZKSysSetCollection.DEL_FLAG.normal == old.getDelFlag().intValue()) {
                    // 组别代码已存在
                    log.error("[>_<:20220510-0007-001] 组别代码已存在；code: {}  ", old.getCode());
                    Map<String, String> validatorMsg = Maps.newHashMap();
                    validatorMsg.put("code", ZKMsgUtils.getMessage("zk.sys.030003", setCollection.getCode()));
                    throw ZKValidatorException.as(validatorMsg);
                } else {
                    // 逻辑删除，重新启用
                    // setCollection.setDelFlag(ZKSysSetCollection.DEL_FLAG.normal);
                    setCollection.setPkId(old.getPkId());
                    super.restore(setCollection);
                }
            }
        }
        return super.save(setCollection);
    }
}