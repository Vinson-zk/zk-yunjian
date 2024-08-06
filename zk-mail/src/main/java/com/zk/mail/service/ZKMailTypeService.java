/**
 * 
 */
package com.zk.mail.service;
 
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.mail.dao.ZKMailTypeDao;
import com.zk.mail.entity.ZKMailType;

/**
 * ZKMailTypeService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKMailTypeService extends ZKBaseService<String, ZKMailType, ZKMailTypeDao> {

    /**
     * 根据邮件类型代码查询实体对象
     *
     * @Title: getByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 26, 2022 3:46:44 PM
     * @param typeCode
     * @param delFlag
     * @return
     * @return ZKMailType
     */
    public ZKMailType getByCode(String typeCode, Integer delFlag) {
        if (ZKStringUtils.isEmpty(typeCode)) {
            return null;
        }
        return this.dao.getByCode(ZKMailType.sqlHelper().getTableName(),
                ZKMailType.sqlHelper().getTableAlias(), ZKMailType.sqlHelper().getBlockSqlCols(),
                typeCode, delFlag);
    }

    @Override
    @Transactional(readOnly = false)
    public int save(ZKMailType mailType) {
        if (mailType.isNewRecord()) {
            // 新记录，判断代码是否唯一
            ZKMailType old = this.getByCode(mailType.getTypeCode(), null);
            if (old != null) {
                if (old.getDelFlag().intValue() == ZKBaseEntity.DEL_FLAG.normal) {
                    log.error("[>_<:20220526-1634-001] zk.mail.000001=邮件类型[{}]已存在；", mailType.getTypeCode());
                    Map<String, String> validatorMsg = Maps.newHashMap();
                    validatorMsg.put("typeCode", ZKMsgUtils.getMessage("zk.mail.000001", mailType.getTypeCode()));
                    throw ZKValidatorException.as(validatorMsg);
                }
                else {
                    // mailType.setDelFlag(ZKBaseEntity.DEL_FLAG.normal);
                    mailType.setPkId(old.getPkId());
                    super.restore(mailType);
                }
            }
        }
        return super.save(mailType);
    }
	
}