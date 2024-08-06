/**
 * 
 */
package com.zk.sys.res.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.sys.auth.service.ZKSysAuthFuncApiService;
import com.zk.sys.res.dao.ZKSysResFuncApiDao;
import com.zk.sys.res.entity.ZKSysResFuncApi;

/**
 * ZKSysResFuncApiService
 * 
 * @author
 * @version
 */
@Service
@Transactional(readOnly = true)
public class ZKSysResFuncApiService extends ZKBaseService<String, ZKSysResFuncApi, ZKSysResFuncApiDao> {

    @Autowired
    ZKSysResApplicationSystemService sysResApplicationSystemService;

    @Autowired
    ZKSysAuthFuncApiService sysAuthFuncApiService;

    @Override
    @Transactional(readOnly = false)
    public int save(ZKSysResFuncApi sysResFuncApi) {
        if (sysResFuncApi.isNewRecord()) {
            // 新接口，判断 应用项目代码 下 接口代码是否唯一
            ZKSysResFuncApi old = this.getBySysAndCode(sysResFuncApi.getCode());
            if (old != null) {
                if (ZKSysResFuncApi.DEL_FLAG.normal == old.getDelFlag().intValue()) {
                    // 接口代码已存在
                    log.error("[>_<:20211130-1031-001] 接口代码:{} 已存在；", sysResFuncApi.getCode());
                    Map<String, String> validatorMsg = Maps.newHashMap();
                    validatorMsg.put("code", ZKMsgUtils.getMessage("zk.sys.000004", sysResFuncApi.getCode()));
                    throw ZKValidatorException.as(validatorMsg);
                } else {
                    sysResFuncApi.setPkId(old.getPkId());
                    // sysResFuncApi.setDelFlag(ZKSysResFuncApi.DEL_FLAG.normal);
                    super.restore(sysResFuncApi);
                }
            }
        }
        return super.save(sysResFuncApi);
    }

    @Override
    public ZKSysResFuncApi get(ZKSysResFuncApi e) {
        e = super.get(e);
        if (e != null) {
            e.setSysResApplicationSystem(this.sysResApplicationSystemService.getByCode(e.getSystemCode()));
        }
        return e;
    }

    /**
     * 根据应用项目代码和接口代码取接口
     *
     * @Title: getBySysAndCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 30, 2021 10:20:57 AM
     * @param apiCode
     * @return ZKSysResFuncApi
     */
    public ZKSysResFuncApi getBySysAndCode(String apiCode) {
        return this.dao.getBySysAndCode(ZKSysResFuncApi.sqlHelper().getTableName(),
            ZKSysResFuncApi.sqlHelper().getTableAlias(), ZKSysResFuncApi.sqlHelper().getBlockSqlCols(), apiCode);
    }

    @Override
    @Transactional(readOnly = false)
    public int del(ZKSysResFuncApi sysResFuncApi) {
        sysAuthFuncApiService.diskDelByApiId(sysResFuncApi.getPkId());
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return super.del(sysResFuncApi);
    }

    @Override
    @Transactional(readOnly = false)
    public int diskDel(ZKSysResFuncApi sysResFuncApi) {
        sysAuthFuncApiService.diskDelByApiId(sysResFuncApi.getPkId());
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return super.diskDel(sysResFuncApi);
    }

}