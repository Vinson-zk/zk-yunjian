/**
 * 
 */
package com.zk.iot.service;
 
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.entity.ZKBaseEntity.DEL_FLAG;
import com.zk.base.service.ZKBaseService;
import com.zk.core.commons.data.ZKOrder;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.commons.data.ZKSortMode;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.iot.dao.ZKIotProdAttributeDao;
import com.zk.iot.entity.ZKIotProdAttribute;
import com.zk.iot.entity.ZKIotProdInstance.ValueKey;

/**
 * ZKIotProdAttributeService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKIotProdAttributeService extends ZKBaseService<String, ZKIotProdAttribute, ZKIotProdAttributeDao> {

    @Value("${zk.default.max.page.size:9999}")
    int defaultMaxPageSize = 9999;

    // 激活
    @Transactional(readOnly = false)
    public int updateStatusActive(String pkId) {
        return this.dao.updateStatus(ZKIotProdAttribute.sqlHelper().getTableName(), pkId, ValueKey.Status.normal);
    }

    // 禁用
    @Transactional(readOnly = false)
    public int updateStatusDisable(String pkId) {
        return this.dao.updateStatus(ZKIotProdAttribute.sqlHelper().getTableName(), pkId, ValueKey.Status.disabled);
    }

    // 取指定目标ID 下指定类型属性的 最大排序号
    public Integer getMaxSort(Integer attrFrom, String targetId, Integer attrType) {
        if (attrFrom == null || attrType == null || ZKStringUtils.isEmpty(targetId)) {
            return null;
        }
        return this.dao.getMaxSort(attrFrom, targetId, attrType);
    }

    // 根据代码取实体及明细
    public ZKIotProdAttribute getByCode(Integer attrFrom, String targetId, String attrCode) {
        if (attrFrom == null || ZKStringUtils.isEmpty(targetId) || ZKStringUtils.isEmpty(attrCode)) {
            return null;
        }
        return this.dao.getByCode(attrFrom, targetId, attrCode);
    }

    // 取指定目标下属性，可以指定类型或不指定类型
    public List<ZKIotProdAttribute> findProdAttrs(Integer attrFrom, String targetId, Integer attrType) {
        return this.findProdAttrs(attrFrom, targetId, attrType, ValueKey.Status.normal, DEL_FLAG.normal);
    }

    public List<ZKIotProdAttribute> findProdAttrs(Integer attrFrom, String targetId, Integer attrType, Integer status,
            Integer delFlag) {
        if (attrFrom == null || ZKStringUtils.isEmpty(targetId)) {
            return Collections.emptyList();
        }
        ZKIotProdAttribute pa = new ZKIotProdAttribute();
        pa.setAttrFrom(attrFrom);
        pa.setTargetId(targetId);
        pa.setAttrType(attrType);
        pa.setStatus(status);
        pa.setDelFlag(delFlag);

        ZKPage<ZKIotProdAttribute> page = ZKPage.asPage();
        page.setPageSize(defaultMaxPageSize);
        page.setSorters(ZKOrder.asOrder("sort", ZKSortMode.ASC), ZKOrder.asOrder("createDate", ZKSortMode.DESC));

        pa.setPage(page);
        return dao.findList(pa);
    }

    // 修改、删除数据 ===============================================
    @Transactional(readOnly = false)
    public int save(ZKIotProdAttribute prodAttr) throws ZKValidatorException {
        if (prodAttr.isNewRecord()) {
            // 新增 ----------------
            // -- 查询代码是否存在，存在报错
            ZKIotProdAttribute old = this.getByCode(prodAttr.getAttrFrom(), prodAttr.getTargetId(),
                    prodAttr.getAttrCode());
            if (old != null) {
                log.error("[>_<:20240107-1408-001] 产品设备属性代码[{}-{}-{}]已存在!", prodAttr.getAttrFrom(),
                        prodAttr.getTargetId(), prodAttr.getAttrCode());
                throw ZKBusinessException.as("zk.iot.000011", "产品设备属性代码已存在", prodAttr.getAttrFrom(),
                        prodAttr.getTargetId(), prodAttr.getAttrCode());
            }
            // -- 排序为空，查询最大排序，加1
            if (prodAttr.getSort() == null) {
                Integer sort = this.getMaxSort(prodAttr.getAttrFrom(), prodAttr.getTargetId(), prodAttr.getAttrType());
                if (sort == null) {
                    sort = 0;
                }
                prodAttr.setSort(sort + 10);
            }
        }
        else {
            // 修改 ----------------
            // 啥也不用做
        }
        // 保存
        return super.save(prodAttr);
    }
	
}


