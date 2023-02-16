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
import com.zk.core.exception.ZKCodeException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.sys.auth.dao.ZKSysAuthRankDao;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.entity.ZKSysAuthFuncApi;
import com.zk.sys.auth.entity.ZKSysAuthRank;
import com.zk.sys.auth.entity.ZKSysAuthUserType;
import com.zk.sys.org.entity.ZKSysOrgRank;

/**
 * ZKSysAuthRankService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKSysAuthRankService extends ZKBaseService<String, ZKSysAuthRank, ZKSysAuthRankDao> {

    // 给职级分配权限
    @Transactional(readOnly = false)
    public ZKSysAuthRank save(ZKSysOrgRank rank, ZKSysAuthDefined authDefined) {

        // 创建新关系
        ZKSysAuthRank newRelation = new ZKSysAuthRank();
        // 设置关系值
        newRelation.setAuthId(authDefined.getPkId());
        newRelation.setAuthCode(authDefined.getCode());
        newRelation.setRankId(rank.getPkId());
        newRelation.setRankCode(rank.getCode());
        // 设置公司信息
        newRelation.setGroupCode(rank.getGroupCode());
        newRelation.setCompanyId(rank.getCompanyId());
        newRelation.setCompanyCode(rank.getCompanyCode());

        // 先查询关联关系是否存在
        ZKSysAuthRank old = this.getRelationByAuthIdAndRankId(newRelation.getAuthId(), newRelation.getRankId());
        if (old != null) {
            if (ZKBaseEntity.DEL_FLAG.normal == old.getDelFlag().intValue()) {
                // 关系已存在；
                return old;
            }
            else {
                newRelation.setPkId(old.getPkId());
                newRelation.setDelFlag(ZKBaseEntity.DEL_FLAG.normal);
            }
        }
        super.save(newRelation);
        return newRelation;
    }

    // 给部门分配 权限关系
    @Transactional(readOnly = false)
    public List<ZKSysAuthRank> addRelationByRank(ZKSysOrgRank rank, List<ZKSysAuthDefined> addAuths,
            List<ZKSysAuthDefined> delAuths) {
        if (rank == null) {
            // zk.sys.010013=职级不存在
            log.error("[>_<:20220504-1212-002] zk.sys.010013=职级不存在");
            throw ZKCodeException.as("zk.sys.010013");
        }
        // 先删除 需要删除的关联关系
        if (delAuths == null) {
            this.diskDelByRankId(rank.getPkId());
        }
        else {
            delAuths.forEach(item -> {
                this.diskDelByAuthIdAndRankId(item.getPkId(), rank.getPkId());
            });
        }
        // 保存分配的关联关系
        if (addAuths != null && !addAuths.isEmpty()) {
            List<ZKSysAuthRank> res = new ArrayList<>();
            addAuths.forEach(item -> {
                res.add(this.save(rank, item));
            });
            return res;
        }
        return Collections.emptyList();
    }

    // 查询职级拥有的权限代码
    public List<String> findAuthCodesByRankId(String rankId) {
        if (ZKStringUtils.isEmpty(rankId)) {
            return Collections.emptyList();
        }
        return this.dao.findAuthCodesByRankId(ZKSysAuthRank.sqlHelper().getTableName(), rankId,
                ZKSysAuthRank.DEL_FLAG.normal);
    }

    // 查询职级拥有的权限ID
    public List<String> findAuthIdsByRankId(String rankId) {
        if (ZKStringUtils.isEmpty(rankId)) {
            return Collections.emptyList();
        }
        return this.dao.findAuthIdsByRankId(ZKSysAuthRank.sqlHelper().getTableName(), rankId,
                ZKSysAuthRank.DEL_FLAG.normal);
    }

    // 通过职级拥有的 API 接口代码
    public List<String> findApiCodesByRankId(String rankId) {
        if (ZKStringUtils.isEmpty(rankId)) {
            return Collections.emptyList();
        }
        return this.dao.findApiCodesByRankId(ZKSysAuthRank.sqlHelper().getTableName(),
                ZKSysAuthFuncApi.sqlHelper().getTableName(), rankId, ZKSysAuthUserType.DEL_FLAG.normal);
    }

    // 根据关联关系查询 权限关系实体
    public ZKSysAuthRank getRelationByAuthIdAndRankId(String authId, String rankId) {
        return this.dao.getRelationByAuthIdAndRankId(ZKSysAuthRank.sqlHelper().getTableName(),
                ZKSysAuthRank.sqlHelper().getTableAlias(), ZKSysAuthRank.sqlHelper().getBlockSqlCols(),
                authId, rankId);
    }

    // 根据权限代码ID 删除关联关系
    @Transactional(readOnly = false)
    public int diskDelByAuthId(String authId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByAuthId(ZKSysAuthRank.sqlHelper().getTableName(), authId);
    }

    // 根据职级ID 删除关联关系
    @Transactional(readOnly = false)
    public int diskDelByRankId(String rankId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByRankId(ZKSysAuthRank.sqlHelper().getTableName(), rankId);
    }

    // 根据权限代码ID和职级ID 物理删除
    @Transactional(readOnly = false)
    public int diskDelByAuthIdAndRankId(String authId, String rankId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByAuthIdAndRankId(ZKSysAuthRank.sqlHelper().getTableName(), authId, rankId);
    }
	
}
