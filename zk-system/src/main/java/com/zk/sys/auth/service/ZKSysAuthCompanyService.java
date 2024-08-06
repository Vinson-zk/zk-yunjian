/**
 * 
 */
package com.zk.sys.auth.service;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.base.service.ZKBaseService;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.sys.auth.dao.ZKSysAuthCompanyDao;
import com.zk.sys.auth.entity.ZKSysAuthCompany;
import com.zk.sys.auth.entity.ZKSysAuthCompany.KeyDefaultToChild;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.entity.ZKSysAuthFuncApi;
import com.zk.sys.org.entity.ZKSysOrgCompany;

/**
 * ZKSysAuthCompanyService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKSysAuthCompanyService extends ZKBaseService<String, ZKSysAuthCompany, ZKSysAuthCompanyDao> {

//    @Autowired
//    private ZKSysOrgCompanyService sysOrgCompanyService;

    // 给公司分配权限
    @Transactional(readOnly = false)
    public ZKSysAuthCompany allotAuthToCompany(ZKSysOrgCompany company, ZKSysAuthDefined authDefined) {
        // 先查询关联关系是否存在
        ZKSysAuthCompany old = this.getRelationByAuthIdAndCompanyId(authDefined.getPkId(), company.getPkId());
        ZKSysAuthCompany optRelation = null;
        if (old != null) {
            // 修改授权或删除授权
            if (authDefined.getAuthRelation() != null && authDefined.getAuthRelation().isDel()) {
                // 删除授权
                super.del(old);
                return old;
            }
            else {
                // 修改权限
                if (old.isDel()) {
                    // 已删除的权限恢复
                    super.restore(old);
                }
                optRelation = old;
            }
        }
        else {
            // 新增授权; 创建新关系
            optRelation = new ZKSysAuthCompany();
        }
        // 设置关系值
        optRelation.setAuthId(authDefined.getPkId());
        optRelation.setAuthCode(authDefined.getCode());
        optRelation.setGroupCode(company.getGroupCode());
        optRelation.setCompanyId(company.getPkId());
        optRelation.setCompanyCode(company.getCode());
        if (authDefined.getAuthRelation() != null) {
            optRelation.setOwnerType(authDefined.getAuthRelation().getOwnerType());
        }
        else {
            optRelation.setOwnerType(ZKSysAuthCompany.KeyOwnerType.normal);
        }

        if (optRelation.getDefaultToChild() == null) {
            optRelation.setDefaultToChild(ZKSysAuthCompany.KeyDefaultToChild.notTransfer);
        }
        super.save(optRelation);
        return optRelation;
    }

    // 给公司分配权限
    @Transactional(readOnly = false)
    public List<ZKSysAuthCompany> allotAuthToCompany(ZKSysOrgCompany company, List<ZKSysAuthDefined> allotAuths) {
        if (company == null) {
            // zk.sys.010006=部门不存在
            log.error("[>_<:20220504-1212-003] 公司不存在");
            throw ZKBusinessException.as("zk.sys.010003", null);
        }
        // 保存分配的关联关系；如果包含前面删除的权限，这里会重新分配给公司
        if (allotAuths != null && !allotAuths.isEmpty()) {
            List<ZKSysAuthCompany> res = new ArrayList<>();
            ZKSysAuthCompany ac = null;
            for (ZKSysAuthDefined item : allotAuths) {
                ac = this.allotAuthToCompany(company, item);
                if (ac != null) {
                    res.add(ac);
                }
            }
            return res;
        }
        return Collections.emptyList();
    }

    // 查询指定公司的权限及拥有权限的方式
    public List<ZKSysAuthDefined> findAuthByCompanyId(String companyId, Integer ownerType, String searchValue,
            ZKPage<ZKSysAuthDefined> page) {
        return this.dao.findAuthByCompanyId(
                ZKSysAuthDefined.sqlHelper().getTableName(), ZKSysAuthDefined.sqlHelper().getTableAlias(),
                ZKSysAuthDefined.sqlHelper().getBlockSqlCols(), ZKSysAuthCompany.sqlHelper().getTableName(),
                companyId, ownerType, searchValue, page);
    }

    // 根据授权公司与被授权公司，查询可授权的权限列表及被授权公司已拥有的权限状态
    public List<ZKSysAuthDefined> findAllotAuthByCompanyId(String fromCompanyId, String toCompanyId,
            String searchValue, ZKPage<ZKSysAuthDefined> page) {
        return this.dao.findAllotAuthByCompanyId(ZKSysAuthDefined.sqlHelper().getTableName(),
                ZKSysAuthDefined.sqlHelper().getTableAlias(), ZKSysAuthDefined.sqlHelper().getBlockSqlCols(),
                ZKSysAuthCompany.sqlHelper().getTableName(), fromCompanyId, toCompanyId, searchValue, page);
    }

//    // 查询公司拥有的权限ID
//    public List<String> findAuthIdsByCompanyId(String companyId, Integer ownerType) {
//        return this.dao.findAuthIdsByCompanyId(ZKSysAuthCompany.sqlHelper().getTableName(), companyId, ownerType,
//                ZKSysAuthCompany.DEL_FLAG.normal);
//    }

    // 根据关联关系查询 权限关系实体
    public ZKSysAuthCompany getRelationByAuthIdAndCompanyId(String authId, String companyId) {
        return this.dao.getRelationByAuthIdAndCompanyId(ZKSysAuthCompany.sqlHelper().getTableName(),
                ZKSysAuthCompany.sqlHelper().getTableAlias(),
                ZKSysAuthCompany.sqlHelper().getBlockSqlCols(), authId, companyId);
    }

    /**
     * 设置权限是否默认传递给子公司；
     * 
     * 先处理 需要默认传递给子公司的公司权限关系
     * 
     * 再处理 取消默认传递给子公司的公司权限关系
     *
     * @Title: setAuthsDefaultTransfer
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 8, 2024 4:55:16 PM
     * @param currentCompanyId
     *            // 当前公司即目标公司
     * @param transferPkIds
     *            // 需要默认传递给子公司的公司权限关系ID
     * @param notTransferPkIds
     *            // 取消默认传递给子公司的公司权限关系ID
     * @return int
     */
    @Transactional(readOnly = false)
    public int setAuthsDefaultTransfer(String currentCompanyId, String[] transferPkIds, String[] notTransferPkIds) {
        if (ZKStringUtils.isEmpty(currentCompanyId)) {
            log.error("[>_<:20220412-1649-001] 公司不存在! companyId:{}", currentCompanyId);
            throw ZKBusinessException.as("zk.sys.010003");
        }

        int count = 0;

//        if ((transferPkIds == null || transferPkIds.length < 1)
//                && (notTransferPkIds == null || notTransferPkIds.length < 1)) {
//            return count;
//        }
//        ZKSysOrgCompany currentCompany = this.sysOrgCompanyService.get(currentCompanyId);
//        if (currentCompany == null) {
//            log.error("[>_<:20220412-1649-001] 公司不存在! companyId:{}", currentCompany);
//            throw ZKBusinessException.as("zk.sys.010003");
//        }
        ZKSysAuthCompany authCompany = null;
        // 需要默认传递给子公司的公司权限关系
        if (transferPkIds != null && transferPkIds.length > 0) {
            for (String pkId : transferPkIds) {
                authCompany = this.get(pkId);
                if (authCompany != null) {
                    if (currentCompanyId.equals(authCompany.getCompanyId())) {
                        authCompany.setDefaultToChild(KeyDefaultToChild.transfer);
                        count += this.save(authCompany);
                    }
                    else {
                        log.error("[>_<:20220412-1649-002] 非本公司权限关系，不能设置；CompanyId: {}；authCompanyId:{}",
                                currentCompanyId, authCompany.getPkId());
                    }
                }
            }
        }
        // 取消默认传递给子公司的公司权限关系
        if (notTransferPkIds != null && notTransferPkIds.length > 0) {
            for (String pkId : notTransferPkIds) {
                authCompany = this.get(pkId);
                if (authCompany != null) {
                    if (currentCompanyId.equals(authCompany.getCompanyId())) {
                        authCompany.setDefaultToChild(KeyDefaultToChild.notTransfer);
                        count += this.save(authCompany);
                    }
                    else {
                        log.error("[>_<:20220412-1649-002] 非本公司权限关系，不能设置；CompanyId: {}；authCompanyId:{}",
                                currentCompanyId, authCompany.getPkId());
                    }
                }
            }
        }
        return count;
    }

    // 查询指定公司 拥有的 API 接口代码
    public Set<String> findApiCodesByCompanyId(String companyId, String systemCode) {
        return this.dao.findApiCodesByCompanyId(ZKSysAuthCompany.sqlHelper().getTableName(),
                ZKSysAuthFuncApi.sqlHelper().getTableName(), companyId, systemCode, ZKBaseEntity.DEL_FLAG.normal);
    }

    // 查询指定公司拥有权限ID
    public List<String> findAuthIdsByCompanyId(String companyId) {
        return this.dao.findAuthIdsByCompanyId(ZKSysAuthCompany.sqlHelper().getTableName(), companyId,
                ZKBaseEntity.DEL_FLAG.normal);
    }

    // 查询指定公司拥有权限代码
    public Set<String> findAuthCodesByCompanyId(String companyId) {
        return this.dao.findAuthCodesByCompanyId(ZKSysAuthCompany.sqlHelper().getTableName(), companyId,
                ZKBaseEntity.DEL_FLAG.normal);
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
