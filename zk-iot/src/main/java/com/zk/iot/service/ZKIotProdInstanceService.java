/**
 * 
 */
package com.zk.iot.service;
 
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.iot.dao.ZKIotProdInstanceDao;
import com.zk.iot.entity.ZKIotProdAttribute;
import com.zk.iot.entity.ZKIotProdInstance;
import com.zk.iot.entity.ZKIotProdInstance.ValueKey;
import com.zk.iot.entity.ZKIotProdModel;
import com.zk.security.principal.ZKSecUserPrincipal;
import com.zk.security.service.ZKSecPrincipalService;
import com.zk.security.utils.ZKSecPrincipalUtils;

/**
 * ZKIotProdInstanceService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKIotProdInstanceService extends ZKBaseService<String, ZKIotProdInstance, ZKIotProdInstanceDao> {

    @Autowired
    ZKIotProdModelService prodModelService;

    @Autowired
    ZKIotProdAttributeService iotProdAttributeService;

    // 根据型号和序列号取实体
    public ZKIotProdInstance getBySnAndModelCode(String prodModelCode, String snNum) {
        if (ZKStringUtils.isEmpty(prodModelCode) || ZKStringUtils.isEmpty(snNum)) {
            return null;
        }
        return this.dao.getBySnAndModelCode(prodModelCode, snNum);
    }

    public ZKIotProdInstance getBySnAndModelId(String prodModelId, String snNum) {
        if (ZKStringUtils.isEmpty(prodModelId) || ZKStringUtils.isEmpty(snNum)) {
            return null;
        }
        return this.dao.getBySnAndModelId(prodModelId, snNum);
    }

    // 根据 mac 地址取实体
    public ZKIotProdInstance getByMacAddr(String macAddr) {
        if (ZKStringUtils.isEmpty(macAddr)) {
            return null;
        }
        return this.dao.getByMacAddr(macAddr);
    }

    public ZKIotProdInstance getDetailById(String pkId) {
        ZKIotProdInstance prodInstance = this.get(pkId);
        return this.fullDetail(prodInstance);
    }

    // 填充产品明细：产品型号
    public ZKIotProdInstance fullDetail(ZKIotProdInstance prodInstance) {
        if (prodInstance == null) {
            return prodInstance;
        }
        prodInstance.setProdModel(prodModelService.getDetailById(prodInstance.getProdModelId()));
        return prodInstance;
    }

    // 激活
    @Transactional(readOnly = false)
    public int updateStatusActive(String pkId) {
        return this.dao.updateStatus(ZKIotProdInstance.sqlHelper().getTableName(), pkId, ValueKey.Status.normal);
    }

    // 禁用
    @Transactional(readOnly = false)
    public int updateStatusDisable(String pkId) {
        return this.dao.updateStatus(ZKIotProdInstance.sqlHelper().getTableName(), pkId, ValueKey.Status.disabled);
    }

    // 取指定父节点下的子节点的最大排序号
    public Integer getMaxSort() {
        return this.dao.getMaxSort();
    }

    // 修改最后心跳时间
    @Transactional(readOnly = false)
    public int updateLastInTime(String pkId, Date lastInTime) {
        if (ZKStringUtils.isEmpty(pkId)) {
            return 0;
        }
        if (lastInTime == null) {
            lastInTime = ZKDateUtils.getToday();
        }
        return this.dao.updateLastInTime(ZKIotProdInstance.sqlHelper().getTableName(), pkId, lastInTime);
    }

    @Transactional(readOnly = false)
    public int updateLastInTimeByMacAddr(String macAddr, Date lastInTime) {
        if (ZKStringUtils.isEmpty(macAddr)) {
            return 0;
        }
        if (lastInTime == null) {
            lastInTime = ZKDateUtils.getToday();
        }
        return this.dao.updateLastInTimeByMacAddr(ZKIotProdInstance.sqlHelper().getTableName(), macAddr, lastInTime);
    }

    @Transactional(readOnly = false)
    public int updateLastInTimeBySn(String prodModelCode, String snNum, Date lastInTime) {
        if (ZKStringUtils.isEmpty(prodModelCode) || ZKStringUtils.isEmpty(snNum)) {
            return 0;
        }
        if (lastInTime == null) {
            lastInTime = ZKDateUtils.getToday();
        }
        return this.dao.updateLastInTimeBySn(ZKIotProdInstance.sqlHelper().getTableName(), prodModelCode, snNum,
                lastInTime);
    }

    // 修改、删除数据 ===============================================
    @Transactional(readOnly = false)
    public int save(ZKIotProdInstance prodInstance) throws ZKValidatorException {
        boolean isNewRecord = prodInstance.isNewRecord();
        if (isNewRecord) {
            // 新增 ----------------
            // -- 查询代码是否存在，存在报错
            ZKIotProdInstance old = this.getBySnAndModelCode(prodInstance.getProdModelCode(), prodInstance.getSnNum());
            if (old != null) {
                log.error("[>_<:20240106-1628-001] 产品设备序列号[{}-{}]已存在!", prodInstance.getProdModelCode(),
                        prodInstance.getSnNum());
                throw ZKBusinessException.as("zk.iot.000008", "设备序列号已存在", prodInstance.getProdModelCode(),
                        prodInstance.getSnNum());
            }
            // -- 排序为空，查询最大排序，加1
            if (prodInstance.getSort() == null) {
                Integer sort = this.getMaxSort();
                if (sort == null) {
                    sort = 0;
                }
                prodInstance.setSort(sort + 10);
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
            prodInstance.setGroupCode(currentPrincipal.getGroupCode());
            prodInstance.setCompanyId(currentPrincipal.getCompanyId());
            prodInstance.setCompanyCode(currentPrincipal.getCompanyCode());
        }
        // 型号信息
        ZKIotProdModel prodModel = this.prodModelService.getByCode(prodInstance.getProdModelCode());
        if (prodModel == null) {
            log.error("[>_<:20240106-1652-001] 产品型号{}不存在!", prodInstance.getProdModelCode());
            throw ZKBusinessException.as("zk.iot.000010", "产品型号{0}不存在", prodInstance.getProdModelCode());
        }
        else {
            prodInstance.setProdId(prodModel.getProdId());
            prodInstance.setProdCode(prodModel.getProdCode());
            prodInstance.setProdModelId(prodModel.getPkId());
        }
        // 保存
        int result = super.save(prodInstance);
        if (result > 0 && isNewRecord) {
            // 新增时，[将产品型号的属性同步给产品设备]，修改不再同步。
            this.syncAttrByProdModel(prodInstance);
        }
        return result;
    }

    // 将产品型号的属性同步给产品设备
    public List<ZKIotProdAttribute> syncAttrByProdModel(ZKIotProdInstance prodInstance) {
        List<ZKIotProdAttribute> prodAttrs = prodModelService.findProdAttr(prodInstance.getProdModelId(), null);
        for (ZKIotProdAttribute item : prodAttrs) {
            item.setPkId(null);
            item.setAttrFrom(ZKIotProdAttribute.ValueKey.AttrFrom.prodInstance);
            item.setTargetId(prodInstance.getPkId());
        }
        iotProdAttributeService.saveBatch(prodAttrs);
        return prodAttrs;
    }

    // 取产品设备属性
    public List<ZKIotProdAttribute> findProdAttr(String prodInstanceId, Integer attrType) {
        if (ZKStringUtils.isEmpty(prodInstanceId)) {
            return Collections.emptyList();
        }
        return iotProdAttributeService.findProdAttrs(ZKIotProdAttribute.ValueKey.AttrFrom.prodInstance, prodInstanceId,
                attrType);
    }
	
}








