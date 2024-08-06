/**
 * 
 */
package com.zk.sys.res.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.sys.res.dao.ZKSysResDictTypeDao;
import com.zk.sys.res.entity.ZKSysResDictType;

/**
 * ZKSysResDictTypeService
 * 
 * @author
 * @version
 */
@Service
@Transactional(readOnly = true)
public class ZKSysResDictTypeService extends ZKBaseService<String, ZKSysResDictType, ZKSysResDictTypeDao> {

    /**
     * 根据字典类型代码查询字典类型
     *
     * @Title: getByTypeCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 12:02:15 AM
     * @param typeCode
     * @return
     * @return ZKSysResDictType
     */
    public ZKSysResDictType getByTypeCode(String typeCode) {
        return this.dao.getByTypeCode(ZKSysResDictType.sqlHelper().getTableName(),
            ZKSysResDictType.sqlHelper().getTableAlias(), ZKSysResDictType.sqlHelper().getBlockSqlCols(), typeCode);
    }

    @Override
    @Transactional(readOnly = false)
    public int save(ZKSysResDictType sysResDictType) {
        if (sysResDictType.isNewRecord()) {
            // 新字典，校验字典类型代码是否存在
            ZKSysResDictType old = this.getByTypeCode(sysResDictType.getTypeCode());
            if (old != null) {
                if (ZKSysResDictType.DEL_FLAG.normal == old.getDelFlag().intValue()) {
                    // 字典代码已存在
                    log.error("[>_<:20220510-0003-001] 字典类型代码已存在；typeCode: {}  ", old.getTypeCode());
                    Map<String, String> validatorMsg = Maps.newHashMap();
                    validatorMsg.put("typeCode", ZKMsgUtils.getMessage("zk.sys.000003", sysResDictType.getTypeCode()));
                    throw ZKValidatorException.as(validatorMsg);
                } else {
                    // 字典代码被逻辑删除，重新启用
                    // sysResDictType.setDelFlag(ZKSysResDictType.DEL_FLAG.normal);
                    sysResDictType.setPkId(old.getPkId());
                    super.restore(sysResDictType);
                }
            }
        }
        return super.save(sysResDictType);
    }

}