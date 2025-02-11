/**
 * 
 */
package com.zk.iot.service;
 
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.iot.dao.ZKIotProdDao;
import com.zk.iot.entity.ZKIotProd;
import com.zk.iot.entity.ZKIotProd.ValueKey;
import com.zk.iot.entity.ZKIotProdAttribute;
import com.zk.iot.entity.ZKIotProdBrand;
import com.zk.iot.entity.ZKIotProdCategores;
import com.zk.security.principal.ZKSecUserPrincipal;
import com.zk.security.service.ZKSecPrincipalService;
import com.zk.security.utils.ZKSecPrincipalUtils;

/**
 * ZKIotProdService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKIotProdService extends ZKBaseService<String, ZKIotProd, ZKIotProdDao> {

    @Autowired
    ZKIotProdCategoresService iotProdCategoresService;

    @Autowired
    ZKIotProdBrandService iotProdBrandService;

    @Autowired
    ZKIotProdAttributeService iotProdAttributeService;

    // 根据代码取实体及明细
    public ZKIotProd getByCode(String prodCode) {
        if (ZKStringUtils.isEmpty(prodCode)) {
            return null;
        }
        return this.dao.getByCode(prodCode);
    }

    public ZKIotProd getDetailById(String pkId) {
        ZKIotProd prod = this.get(pkId);
        return this.fullDetail(prod);
    }
    public ZKIotProd getDetailByCode(String prodCode) {
        ZKIotProd prod = this.getByCode(prodCode);
        return this.fullDetail(prod);
    }
    // 填充产品明细：产品场景、产品品牌
    public ZKIotProd fullDetail(ZKIotProd prod) {
        if (prod == null) {
            return prod;
        }
        prod.setProdCate(iotProdCategoresService.get(prod.getCateId()));
        prod.setProdBrand(iotProdBrandService.get(prod.getBrandId()));
        return prod;
    }

    // 激活
    @Transactional(readOnly = false)
    public int updateStatusActive(String pkId) {
        return this.dao.updateStatus(ZKIotProd.sqlHelper().getTableName(), pkId, ValueKey.Status.normal);
    }

    // 禁用
    @Transactional(readOnly = false)
    public int updateStatusDisable(String pkId) {
        return this.dao.updateStatus(ZKIotProd.sqlHelper().getTableName(), pkId, ValueKey.Status.disabled);
    }

    // 取指定父节点下的子节点的最大排序号
    public Integer getMaxSort() {
        return this.dao.getMaxSort();
    }

    // 修改、删除数据 ===============================================
    @Transactional(readOnly = false)
    public int save(ZKIotProd prod) throws ZKValidatorException {
        if (prod.isNewRecord()) {
            // 新增 ----------------
            // -- 查询代码是否存在，存在报错
            ZKIotProd old = this.getByCode(prod.getProdCode());
            if (old != null) {
                log.error("[>_<:20240106-1457-001] 产品代码: {} 已存在!", prod.getProdCode());
                throw ZKBusinessException.as("zk.iot.000004", "产品代码已存在", prod.getProdCode());
            }
            // -- 排序为空，查询最大排序，加1
            if (prod.getSort() == null) {
                Integer sort = this.getMaxSort();
                if (sort == null) {
                    sort = 0;
                }
                prod.setSort(sort + 10);
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
            prod.setGroupCode(currentPrincipal.getGroupCode());
            prod.setCompanyId(currentPrincipal.getCompanyId());
            prod.setCompanyCode(currentPrincipal.getCompanyCode());
        }

        // 判断场景是否存在
        ZKIotProdCategores iotProdCategores = iotProdCategoresService.getByCode(prod.getCateCode());
        if (iotProdCategores == null) {
            log.error("[>_<:20240106-1526-001] 产品场景: {} 不存在!", prod.getCateCode());
            throw ZKBusinessException.as("zk.iot.000005", "产品场景不存在", prod.getCateCode());
        }
        else {
            prod.setCateId(iotProdCategores.getPkId());
        }
        // 判断产品品牌是否存在
        ZKIotProdBrand iotProdBrand = iotProdBrandService.getByCode(prod.getBrandCode());
        if (iotProdBrand == null) {
            log.error("[>_<:20240106-1529-001] 产品品牌: {} 不存在!", prod.getBrandCode());
            throw ZKBusinessException.as("zk.iot.000006", "产品品牌不存在", prod.getBrandCode());
        }
        else {
            prod.setBrandId(iotProdBrand.getPkId());
        }

        // 保存
        return super.save(prod);
    }

    // 取产品属性
    public List<ZKIotProdAttribute> findProdAttr(String prodId, Integer attrType) {
        if (ZKStringUtils.isEmpty(prodId)) {
            return Collections.emptyList();
        }
        return iotProdAttributeService.findProdAttrs(ZKIotProdAttribute.ValueKey.AttrFrom.prod, prodId,
                attrType);
    }
	
}


