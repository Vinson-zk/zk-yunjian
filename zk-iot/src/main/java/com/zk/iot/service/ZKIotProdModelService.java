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
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.iot.dao.ZKIotProdModelDao;
import com.zk.iot.entity.ZKIotProd;
import com.zk.iot.entity.ZKIotProdAttribute;
import com.zk.iot.entity.ZKIotProdModel;
import com.zk.iot.entity.ZKIotProdModel.ValueKey;
import com.zk.security.principal.ZKSecUserPrincipal;
import com.zk.security.service.ZKSecPrincipalService;
import com.zk.security.utils.ZKSecPrincipalUtils;

/**
 * ZKIotProdModelService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKIotProdModelService extends ZKBaseService<String, ZKIotProdModel, ZKIotProdModelDao> {

    @Autowired
    ZKIotProdService prodService;

    @Autowired
    ZKIotProdAttributeService iotProdAttributeService;

    // 根据代码取实体及明细
    public ZKIotProdModel getByCode(String prodModelCode) {
        if (ZKStringUtils.isEmpty(prodModelCode)) {
            return null;
        }
        return this.dao.getByCode(prodModelCode);
    }
    public ZKIotProdModel getDetailById(String pkId) {
        ZKIotProdModel prodModel = this.get(pkId);
        return this.fullDetail(prodModel);
    }
    public ZKIotProdModel getDetailByCode(String prodCode) {
        ZKIotProdModel prodModel = this.getByCode(prodCode);
        return this.fullDetail(prodModel);
    }

    // 填充产品明细：产品
    public ZKIotProdModel fullDetail(ZKIotProdModel prodModel) {
        if (prodModel == null) {
            return prodModel;
        }
        prodModel.setProd(prodService.getDetailById(prodModel.getProdId()));
        return prodModel;
    }

    // 激活
    @Transactional(readOnly = false)
    public int updateStatusActive(String pkId) {
        return this.dao.updateStatus(ZKIotProdModel.sqlHelper().getTableName(), pkId, ValueKey.Status.normal);
    }

    // 禁用
    @Transactional(readOnly = false)
    public int updateStatusDisable(String pkId) {
        return this.dao.updateStatus(ZKIotProdModel.sqlHelper().getTableName(), pkId, ValueKey.Status.disabled);
    }

    // 取指定父节点下的子节点的最大排序号
    public Integer getMaxSort() {
        return this.dao.getMaxSort();
    }

    // 修改数据处理协议
    @Transactional(readOnly = false)
    public int updateDataProtocol(String pkId, String businessDataProtocolId, String businessDataProtocolCode) {

        // 根据当前用户设置公司信息
        ZKSecPrincipalService secPrincipalService = ZKSecPrincipalUtils.getSecPrincipalService();
        ZKSecUserPrincipal<String> currentPrincipal = secPrincipalService.getUserPrincipal();
        String userId = null;
        if (currentPrincipal == null) {
            log.info("[^_^:20240106-1536-001] 用户信息不存在!");
        }
        else {
            userId = currentPrincipal.getPkId();
        }
        if (ZKStringUtils.isEmpty(pkId)) {
            return 0;
        }
        return this.dao.updateDataProtocol(ZKIotProdModel.sqlHelper().getTableName(), pkId, businessDataProtocolId,
                businessDataProtocolCode, ZKDateUtils.getToday(), userId);

    }

    // 修改、删除数据 ===============================================
    @Transactional(readOnly = false)
    public int save(ZKIotProdModel prodModel) throws ZKValidatorException {
        boolean isNewRecord = prodModel.isNewRecord();
        if (isNewRecord) {
            // 新增 ----------------
            // -- 查询代码是否存在，存在报错
            ZKIotProdModel old = this.getByCode(prodModel.getProdModelCode());
            if (old != null) {
                log.error("[>_<:20240106-1601-001] 产品型号已存在!", prodModel.getProdModelCode());
                throw ZKBusinessException.as("zk.iot.000007", "产品型号已存在", prodModel.getProdModelCode());
            }
            // -- 排序为空，查询最大排序，加1
            if (prodModel.getSort() == null) {
                Integer sort = this.getMaxSort();
                if (sort == null) {
                    sort = 0;
                }
                prodModel.setSort(sort + 10);
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
            prodModel.setGroupCode(currentPrincipal.getGroupCode());
            prodModel.setCompanyId(currentPrincipal.getCompanyId());
            prodModel.setCompanyCode(currentPrincipal.getCompanyCode());
        }

        // 产品信息
        ZKIotProd prod = prodService.getByCode(prodModel.getProdCode());
        if (prod == null) {
            log.error("[>_<:20240106-1858-001] 产品{}不存在!", prodModel.getProdCode());
            throw ZKBusinessException.as("zk.iot.000009", "产品{0}不存在", prodModel.getProdCode());
        }
        else {
            prodModel.setProdId(prod.getPkId());
        }

        // 保存
        int result = super.save(prodModel);
        if (result > 0 && isNewRecord) {
            // 新增时，[将产品的属性同步给产品型号]，修改不再同步。
            this.syncAttrByProd(prodModel);
        }
        return result;
    }

    // 将产品的属性同步给产品型号
    public List<ZKIotProdAttribute> syncAttrByProd(ZKIotProdModel prodModel){
        List<ZKIotProdAttribute> prodAttrs = prodService.findProdAttr(prodModel.getProdId(), null);
        for (ZKIotProdAttribute item : prodAttrs) {
            item.setPkId(null);
            item.setAttrFrom(ZKIotProdAttribute.ValueKey.AttrFrom.prodModel);
            item.setTargetId(prodModel.getPkId());
        }
        iotProdAttributeService.saveBatch(prodAttrs);
        return prodAttrs;
    }

    // 取产品型号属性
    public List<ZKIotProdAttribute> findProdAttr(String prodModelId, Integer attrType) {
        if (ZKStringUtils.isEmpty(prodModelId)) {
            return Collections.emptyList();
        }

        return iotProdAttributeService
                .findProdAttrs(ZKIotProdAttribute.ValueKey.AttrFrom.prodModel, prodModelId, attrType);
    }
	
}