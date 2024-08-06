/**
 * 
 */
package com.zk.sys.res.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.zk.base.service.ZKBaseTreeService;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.sys.res.dao.ZKSysResDictDao;
import com.zk.sys.res.entity.ZKSysResDict;
import com.zk.sys.res.entity.ZKSysResDictType;

/**
 * ZKSysResDictService
 * 
 * @author
 * @version
 */
@Service
@Transactional(readOnly = true)
public class ZKSysResDictService extends ZKBaseTreeService<String, ZKSysResDict, ZKSysResDictDao> {

    @Autowired
    ZKSysResDictTypeService sysResDictTypeService;

    @Override
    @Transactional(readOnly = false)
    public int save(ZKSysResDict sysResDict) {
        if (sysResDict.isNewRecord()) {
            // 新字典，校验字典代码是否存在
            ZKSysResDict oldDict = this.getByTypeCodeAndDictCode(sysResDict.getTypeCode(), sysResDict.getDictCode());
            if (oldDict != null) {
                if (ZKSysResDict.DEL_FLAG.normal == oldDict.getDelFlag().intValue()) {
                    // 字典代码已存在
                    log.error("[>_<:20211114-2130-001] 字典代码已存在；typeCode: {} dictCode: {} ", sysResDict.getTypeCode(),
                        sysResDict.getDictCode());
                    Map<String, String> validatorMsg = Maps.newHashMap();
                    validatorMsg.put("dictCode", ZKMsgUtils.getMessage("zk.sys.000003", sysResDict.getDictCode()));
                    throw ZKValidatorException.as(validatorMsg);
                } else {
                    // 字典代码被逻辑删除，重新启用
                    // sysResDict.setDelFlag(ZKSysResDict.DEL_FLAG.normal);
                    sysResDict.setPkId(oldDict.getPkId());
                    super.restore(sysResDict);
                }
            }
        }
        return super.save(sysResDict);
    }

    /**
     * 根据字典类型代码和字典代码取字典
     *
     * @Title: getByTypeCodeAndDictCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 14, 2021 9:17:35 PM
     * @param typeCode
     * @param dictCode
     * @return
     * @return ZKSysResDict
     */
    public ZKSysResDict getByTypeCodeAndDictCode(String typeCode, String dictCode) {
        if (ZKStringUtils.isEmpty(typeCode)) {
            log.info("[^_^:20211114-2118-001] 字典类型代码为空；");
            return null;
        }
        if (ZKStringUtils.isEmpty(dictCode)) {
            log.info("[^_^:20211114-2118-002] 字典代码为空；");
            return null;
        }
        return this.dao.getByTypeCodeAndDictCode(ZKSysResDict.sqlHelper().getTableName(),
            ZKSysResDict.sqlHelper().getTableAlias(), ZKSysResDict.sqlHelper().getBlockSqlCols(), typeCode, dictCode);
    }

    /**
     * 包含父节点；但不包含父节点的关联字段的明细；
     *
     * @Title: getDetail
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 13, 2021 3:41:08 PM
     * @param sysResDic
     * @return
     * @return ZKSysResDict
     */
    public ZKSysResDict getDetail(ZKSysResDict sysResDic) {
        ZKSysResDict e = this.dao.getDetail(sysResDic);
        if (e != null) {
            ZKSysResDictType et = sysResDictTypeService.getByTypeCode(e.getTypeCode());
            e.setDictType(et);
        }
        return e;
    }

}
