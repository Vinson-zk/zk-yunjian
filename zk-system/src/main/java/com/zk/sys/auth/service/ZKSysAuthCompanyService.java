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
import com.zk.sys.auth.dao.ZKSysAuthCompanyDao;
import com.zk.sys.auth.entity.ZKSysAuthCompany;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.entity.org.ZKSysOrgCompany;

/**
 * ZKSysAuthCompanyService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKSysAuthCompanyService extends ZKBaseService<String, ZKSysAuthCompany, ZKSysAuthCompanyDao> {

    // 给公司分配权限代码
    @Transactional(readOnly = false)
    public ZKSysAuthCompany save(ZKSysOrgCompany company, ZKSysAuthDefined authDefined, int ownerType) {

        // 创建新关系
        ZKSysAuthCompany newRelation = new ZKSysAuthCompany();
        // 设置关系值
        newRelation.setAuthId(authDefined.getPkId());
        newRelation.setAuthCode(authDefined.getCode());
        newRelation.setGroupCode(company.getGroupCode());
        newRelation.setCompanyId(company.getPkId());
        newRelation.setCompanyCode(company.getCode());
        newRelation.setOwnerType(ownerType);

        // 先查询关联关系是否存在
        ZKSysAuthCompany old = this.getRelationByAuthIdAndCompanyId(newRelation.getAuthId(),
                newRelation.getCompanyId());
        if (old != null) {
            // 关系已存在；不管删除与否都要保存，因为可能修改拥有方式
            newRelation.setPkId(old.getPkId());
            newRelation.setDelFlag(ZKBaseEntity.DEL_FLAG.normal);
        }
        super.save(newRelation);
        return newRelation;
    }

    // 给公司分配 权限关系
    @Transactional(readOnly = false)
    public List<ZKSysAuthCompany> addRelationByCompany(ZKSysOrgCompany company, List<ZKSysAuthDefined> addAuths,
            List<ZKSysAuthDefined> delAuths) {
        if (company == null) {
            // zk.sys.010006=部门不存在
            log.error("[>_<:20220504-1212-003] 部门不存在");
            throw ZKCodeException.as("zk.sys.010006");
        }
        // 先删除 需要删除的关联关系
        if (delAuths == null) {
            this.diskDelByCompanyId(company.getPkId());
        }
        else {
            delAuths.forEach(item -> {
                this.diskDelByAuthIdAndCompanyId(item.getPkId(), company.getPkId());
            });
        }
        // 保存分配的关联关系
        if (addAuths != null && !addAuths.isEmpty()) {
            List<ZKSysAuthCompany> res = new ArrayList<>();
            addAuths.forEach(item -> {
                res.add(this.save(company, item, item.getAllotType()));
            });
            return res;
        }
        return Collections.emptyList();
    }

    // 查询公司拥有的权限ID
    public List<String> findAuthIdsByCompanyId(String companyId, Integer ownerType) {
        return this.dao.findAuthIdsByCompanyId(ZKSysAuthCompany.sqlHelper().getTableName(), companyId, ownerType,
                ZKSysAuthCompany.DEL_FLAG.normal);
    }

    // 根据关联关系查询 权限关系实体
    public ZKSysAuthCompany getRelationByAuthIdAndCompanyId(String authId, String companyId) {
        return this.dao.getRelationByAuthIdAndCompanyId(ZKSysAuthCompany.sqlHelper().getTableName(),
                ZKSysAuthCompany.sqlHelper().getTableAlias(),
                ZKSysAuthCompany.sqlHelper().getBlockSqlCols(), authId, companyId);
    }

    // 根据权限代码ID 删除关联关系
    @Transactional(readOnly = false)
    public int diskDelByAuthId(String authId) {
        return this.dao.diskDelByAuthId(ZKSysAuthCompany.sqlHelper().getTableName(), authId);
    }

    // 根据公司ID 删除关联关系
    @Transactional(readOnly = false)
    public int diskDelByCompanyId(String companyId) {
        return this.dao.diskDelByCompanyId(ZKSysAuthCompany.sqlHelper().getTableName(), companyId);
    }

    // 根据权限代码ID和菜单ID 物理删除
    @Transactional(readOnly = false)
    public int diskDelByAuthIdAndCompanyId(String authId, String companyId) {
        return this.dao.diskDelByAuthIdAndCompanyId(ZKSysAuthCompany.sqlHelper().getTableName(), authId,
                companyId);
    }
	
}
