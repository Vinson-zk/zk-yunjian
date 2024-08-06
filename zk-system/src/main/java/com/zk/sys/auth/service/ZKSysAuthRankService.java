/**
 * 
 */
package com.zk.sys.auth.service;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.sys.auth.dao.ZKSysAuthRankDao;
import com.zk.sys.auth.entity.ZKSysAuthCompany;
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
    public ZKSysAuthRank allotAuthToRank(ZKSysOrgRank rank, ZKSysAuthDefined authDefined) {

        // 先查询关联关系是否存在
        ZKSysAuthRank old = this.getRelationByAuthIdAndRankId(authDefined.getPkId(), rank.getPkId());
        ZKSysAuthRank optRelation;
        if (old == null) { // 新增授权
            // 创建新关系
            optRelation = new ZKSysAuthRank();
        }
        else { // 修改或删除授权
            if (authDefined.getAuthRelation() != null && authDefined.getAuthRelation().isDel()) {
                // 删除授权
                super.del(old);
                return old;
            }
            else {
                if (old.isDel()) {
                    // 恢复授权
                    super.restore(old);
                }
                optRelation = old;
            }
        }
        // 设置关系值
        optRelation.setAuthId(authDefined.getPkId());
        optRelation.setAuthCode(authDefined.getCode());
        optRelation.setRankId(rank.getPkId());
        optRelation.setRankCode(rank.getCode());
        // 设置公司信息
        optRelation.setGroupCode(rank.getGroupCode());
        optRelation.setCompanyId(rank.getCompanyId());
        optRelation.setCompanyCode(rank.getCompanyCode());

        super.save(optRelation);
        return optRelation;
    }

    // 给部门分配 权限关系
    @Transactional(readOnly = false)
    public List<ZKSysAuthRank> allotAuthToRank(ZKSysOrgRank rank, List<ZKSysAuthDefined> allotAuths) {
        if (rank == null) {
            // zk.sys.010013=职级不存在
            log.error("[>_<:20220504-1214-001] zk.sys.010013=职级不存在");
            throw ZKBusinessException.as("zk.sys.010013");
        }
        // 保存分配的关联关系
        if (allotAuths != null && !allotAuths.isEmpty()) {
            List<ZKSysAuthRank> res = new ArrayList<>();
            for (ZKSysAuthDefined item : allotAuths) {
                res.add(this.allotAuthToRank(rank, item));
            }
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
    public List<String> findApiCodesByRankId(String rankId, String systemCode) {
        if (ZKStringUtils.isEmpty(rankId)) {
            return Collections.emptyList();
        }
        return this.dao.findApiCodesByRankId(ZKSysAuthRank.sqlHelper().getTableName(),
                ZKSysAuthFuncApi.sqlHelper().getTableName(), rankId, systemCode, ZKSysAuthUserType.DEL_FLAG.normal);
    }

    // 根据关联关系查询 权限关系实体
    public ZKSysAuthRank getRelationByAuthIdAndRankId(String authId, String rankId) {
        return this.dao.getRelationByAuthIdAndRankId(ZKSysAuthRank.sqlHelper().getTableName(),
                ZKSysAuthRank.sqlHelper().getTableAlias(), ZKSysAuthRank.sqlHelper().getBlockSqlCols(),
                authId, rankId);
    }

    // 查询给指定职级授权时，公司可分配的权限以及职级已拥有的权限
    public List<ZKSysAuthDefined> findAllotAuthByRank(ZKSysOrgRank rank, String searchValue,
            ZKPage<ZKSysAuthDefined> page) {
        if (rank == null) {
            log.error("[>_<:20220504-1214-002] zk.sys.010013=职级不存在");
            throw ZKBusinessException.as("zk.sys.010013");
        }
        return this.dao.findAllotAuthByRankId(ZKSysAuthDefined.sqlHelper().getTableName(),
                ZKSysAuthDefined.sqlHelper().getTableAlias(), ZKSysAuthDefined.sqlHelper().getBlockSqlCols(),
                ZKSysAuthCompany.sqlHelper().getTableName(), ZKSysAuthRank.sqlHelper().getTableName(),
                rank.getCompanyId(), rank.getPkId(), searchValue, page);
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
