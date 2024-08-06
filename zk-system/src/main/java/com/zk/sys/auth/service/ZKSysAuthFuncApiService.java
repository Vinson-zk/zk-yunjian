/**
 * 
 */
package com.zk.sys.auth.service;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKBusinessException;
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.sys.auth.dao.ZKSysAuthFuncApiDao;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.entity.ZKSysAuthFuncApi;
import com.zk.sys.res.entity.ZKSysResFuncApi;

/**
 * ZKSysAuthFuncApiService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKSysAuthFuncApiService extends ZKBaseService<String, ZKSysAuthFuncApi, ZKSysAuthFuncApiDao> {

    // 给权限代码分析接口
    @Transactional(readOnly = false)
    public ZKSysAuthFuncApi save(ZKSysAuthDefined authDefined, ZKSysResFuncApi funcApi) {

        // 创建新关系
        ZKSysAuthFuncApi newRelation = new ZKSysAuthFuncApi();
        // 设置关系值
        newRelation.setAuthId(authDefined.getPkId());
        newRelation.setFuncApiId(funcApi.getPkId());
        newRelation.setFuncApiCode(funcApi.getCode());
        newRelation.setSystemCode(funcApi.getSystemCode());

        // 先查询关联关系是否存在
        ZKSysAuthFuncApi old = this.getRelationByAuthIdAndApiId(newRelation.getAuthId(), newRelation.getFuncApiId());
        if (old != null) {
            if (ZKBaseEntity.DEL_FLAG.normal == old.getDelFlag().intValue()) {
                // 关系已存在；
                return old;
            }
            else {
                newRelation.setPkId(old.getPkId());
                // newRelation.setDelFlag(ZKBaseEntity.DEL_FLAG.normal);
                super.restore(newRelation);
            }
        }
        super.save(newRelation);
        return newRelation;
    }

    // 给权限分配 菜单关系
    @Transactional(readOnly = false)
    public List<ZKSysAuthFuncApi> addRelationByAuthDefined(ZKSysAuthDefined authDefined,
            List<ZKSysResFuncApi> addFuncApis, List<ZKSysResFuncApi> delFuncApis) {
        if (authDefined == null) {
            // zk.sys.020011=权限不存在
            log.error("[>_<:20220504-1212-001] 权限不存在");
            throw ZKBusinessException.as("zk.sys.020011", "权限不存在");
        }
        // 先删除 需要删除的关联关系
        if (delFuncApis == null) {
            this.diskDelByAuthId(authDefined.getPkId());
        }
        else {
            delFuncApis.forEach(item -> {
                this.diskDelByAuthIdAndApiId(authDefined.getPkId(), item.getPkId());
            });
        }
        // 保存分配的关联关系
        if (addFuncApis != null && !addFuncApis.isEmpty()) {
            List<ZKSysAuthFuncApi> res = new ArrayList<>();
            addFuncApis.forEach(item -> {
                res.add(this.save(authDefined, item));
            });
            ZKUserCacheUtils.cleanAll();
            return res;
        }
        return Collections.emptyList();
    }

    // 查询权限拥有的API接口代码
    public List<String> findApiCodesByAuthId(String authId) {
        return this.dao.findApiCodesByAuthId(ZKSysAuthFuncApi.sqlHelper().getTableName(), authId,
                ZKSysAuthFuncApi.DEL_FLAG.normal);
    }

    // 查询权限拥有的API接口ID
    public List<String> findApiIdsByAuthId(String authId) {
        return this.dao.findApiIdsByAuthId(ZKSysAuthFuncApi.sqlHelper().getTableName(), authId,
                ZKSysAuthFuncApi.DEL_FLAG.normal);
    }

    // 根据关联关系查询 权限关系实体
    public ZKSysAuthFuncApi getRelationByAuthIdAndApiId(String authId, String funcApiId) {
        return this.dao.getRelationByAuthIdAndApiId(ZKSysAuthFuncApi.sqlHelper().getTableName(),
                ZKSysAuthFuncApi.sqlHelper().getTableAlias(),
                ZKSysAuthFuncApi.sqlHelper().getBlockSqlCols(), authId, funcApiId);
    }

    // 根据权限代码ID 删除关联关系
    @Transactional(readOnly = false)
    public int diskDelByAuthId(String authId) {
        return this.dao.diskDelByAuthId(ZKSysAuthFuncApi.sqlHelper().getTableName(), authId);
    }

    // 根据Api接口ID 删除关联关系
    @Transactional(readOnly = false)
    public int diskDelByApiId(String funcApiId) {
        return this.dao.diskDelByApiId(ZKSysAuthFuncApi.sqlHelper().getTableName(), funcApiId);
    }

    // 根据权限代码ID和Api接口ID 物理删除
    @Transactional(readOnly = false)
    public int diskDelByAuthIdAndApiId(String authId,  String funcApiId) {
        return this.dao.diskDelByAuthIdAndApiId(ZKSysAuthFuncApi.sqlHelper().getTableName(), authId, funcApiId);
    }
	
}