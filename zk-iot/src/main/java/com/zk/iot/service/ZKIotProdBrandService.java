/**
 * 
 */
package com.zk.iot.service;
 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.iot.dao.ZKIotProdBrandDao;
import com.zk.iot.entity.ZKIotProdBrand;
import com.zk.iot.entity.ZKIotProdBrand.ValueKey;
import com.zk.security.principal.ZKSecUserPrincipal;
import com.zk.security.service.ZKSecPrincipalService;
import com.zk.security.utils.ZKSecPrincipalUtils;

/**
 * ZKIotProdBrandService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKIotProdBrandService extends ZKBaseService<String, ZKIotProdBrand, ZKIotProdBrandDao> {

    // 根据代码取实体及明细
    public ZKIotProdBrand getByCode(String brandCode) {
        if (ZKStringUtils.isEmpty(brandCode)) {
            return null;
        }
        return this.dao.getByCode(brandCode);
    }

    // 激活
    @Transactional(readOnly = false)
    public int updateStatusActive(String pkId) {
        return this.dao.updateStatus(ZKIotProdBrand.sqlHelper().getTableName(), pkId, ValueKey.Status.normal);
    }

    // 禁用
    @Transactional(readOnly = false)
    public int updateStatusDisable(String pkId) {
        return this.dao.updateStatus(ZKIotProdBrand.sqlHelper().getTableName(), pkId, ValueKey.Status.disabled);
    }

    // 取指定父节点下的子节点的最大排序号
    public Integer getMaxSort() {
        return this.dao.getMaxSort();
    }

    // 修改、删除数据 ===============================================
    @Transactional(readOnly = false)
    public int save(ZKIotProdBrand prodBrand) throws ZKValidatorException {
        if (prodBrand.isNewRecord()) {
            // 新增 ----------------
            // -- 查询代码是否存在，存在报错
            ZKIotProdBrand old = this.getByCode(prodBrand.getBrandCode());
            if (old != null) {
                log.error("[>_<:20240103-1611-001] 产品品牌代码: {} 已存在!", prodBrand.getBrandCode());
                throw ZKBusinessException.as("zk.iot.000002", "产品品牌代码已存在", prodBrand.getBrandCode());
            }
            // -- 排序为空，查询最大排序，加1
            if (prodBrand.getSort() == null) {
                Integer sort = this.getMaxSort();
                if (sort == null) {
                    sort = 0;
                }
                prodBrand.setSort(sort + 10);
            }
        }
        else {
            // 修改 ----------------
            // 啥也不用做
        }
        // 根据当前用户设置公司信息
        ZKSecPrincipalService secPrincipalService = ZKSecPrincipalUtils.getSecPrincipalService();
        ZKSecUserPrincipal<String> currentPrincipal = secPrincipalService.getUserPrincipal();

        if (currentPrincipal == null) {
            log.error("[>_<:20240103-1640-001] 用户信息不存在!");
        }
        else {
            prodBrand.setGroupCode(currentPrincipal.getGroupCode());
            prodBrand.setCompanyId(currentPrincipal.getCompanyId());
            prodBrand.setCompanyCode(currentPrincipal.getCompanyCode());
        }

        // 保存
        return super.save(prodBrand);
    }
	
}
