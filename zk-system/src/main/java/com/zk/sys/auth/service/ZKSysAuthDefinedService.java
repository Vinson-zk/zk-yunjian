/**
 * 
 */
package com.zk.sys.auth.service;
 
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
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.sys.auth.dao.ZKSysAuthDefinedDao;
import com.zk.sys.auth.entity.ZKSysAuthDefined;

/**
 * ZKSysAuthDefinedService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKSysAuthDefinedService extends ZKBaseService<String, ZKSysAuthDefined, ZKSysAuthDefinedDao> {

    @Autowired
    ZKSysAuthFuncApiService sysAuthFuncApiService;

    @Autowired
    ZKSysAuthMenuService sysAuthMenuService;

    @Autowired
    ZKSysAuthNavService sysAuthNavService;

    @Autowired
    ZKSysAuthCompanyService sysAuthCompanyService;

    @Autowired
    ZKSysAuthDeptService sysAuthDeptService;

    @Autowired
    ZKSysAuthUserService sysAuthUserService;

    @Autowired
    ZKSysAuthRoleService sysAuthRoleService;

    @Autowired
    ZKSysAuthRankService sysAuthRankService;

    @Autowired
    ZKSysAuthUserTypeService sysAuthUserTypeService;

    @Override
    @Transactional(readOnly = false)
    public int save(ZKSysAuthDefined authDefined) throws ZKValidatorException {

        if (authDefined.isNewRecord()) {
            // 新记录，判断代码是否唯一
            ZKSysAuthDefined old = this.getByCode(authDefined.getCode());
            if (old != null) {
                if (old.getDelFlag().intValue() == ZKBaseEntity.DEL_FLAG.normal) {
                    // zk.sys.020010=权限代码[{0}]已存在
                    log.error("[>_<:20220504-0917-001] 权限代码[{}]已存在；", authDefined.getCode());
                    Map<String, String> validatorMsg = Maps.newHashMap();
                    validatorMsg.put("code", ZKMsgUtils.getMessage("zk.sys.020010", authDefined.getCode()));
                    throw ZKValidatorException.as(validatorMsg);
                }
                else {
                    // authDefined.setDelFlag(ZKBaseEntity.DEL_FLAG.normal);
                    authDefined.setPkId(old.getPkId());
                    super.restore(authDefined);
                }
            }
        }
        return super.save(authDefined);
    }
	
    public ZKSysAuthDefined getByCode(String code) {
        if (ZKStringUtils.isEmpty(code)) {
            return null;
        }
        return this.dao.getByCode(ZKSysAuthDefined.sqlHelper().getTableName(),
                ZKSysAuthDefined.sqlHelper().getTableAlias(),
                ZKSysAuthDefined.sqlHelper().getBlockSqlCols(), code);

    }

    @Override
    @Transactional(readOnly = false)
    public int del(ZKSysAuthDefined authDefined) {
        sysAuthCompanyService.diskDelByAuthId(authDefined.getPkId());
        sysAuthDeptService.diskDelByAuthId(authDefined.getPkId());
        sysAuthUserService.diskDelByAuthId(authDefined.getPkId());
        sysAuthRoleService.diskDelByAuthId(authDefined.getPkId());
        sysAuthRankService.diskDelByAuthId(authDefined.getPkId());
        sysAuthUserTypeService.diskDelByAuthId(authDefined.getPkId());

        sysAuthFuncApiService.diskDelByAuthId(authDefined.getPkId());
        sysAuthMenuService.diskDelByAuthId(authDefined.getPkId());
        sysAuthNavService.diskDelByAuthId(authDefined.getPkId());

        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return super.del(authDefined);
    }

    @Override
    @Transactional(readOnly = false)
    public int diskDel(ZKSysAuthDefined authDefined) {
        sysAuthCompanyService.diskDelByAuthId(authDefined.getPkId());
        sysAuthDeptService.diskDelByAuthId(authDefined.getPkId());
        sysAuthUserService.diskDelByAuthId(authDefined.getPkId());
        sysAuthRoleService.diskDelByAuthId(authDefined.getPkId());
        sysAuthRankService.diskDelByAuthId(authDefined.getPkId());
        sysAuthUserTypeService.diskDelByAuthId(authDefined.getPkId());

        sysAuthFuncApiService.diskDelByAuthId(authDefined.getPkId());
        sysAuthMenuService.diskDelByAuthId(authDefined.getPkId());
        sysAuthNavService.diskDelByAuthId(authDefined.getPkId());

        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return super.diskDel(authDefined);
    }
}