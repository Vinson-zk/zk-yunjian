/**
 * 
 */
package com.zk.iot.service;
 
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseTreeService;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.iot.dao.ZKIotProdCategoresDao;
import com.zk.iot.entity.ZKIotProdCategores;
import com.zk.iot.entity.ZKIotProdCategores.ValueKey;

/**
 * ZKIotProdCategoresService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKIotProdCategoresService extends ZKBaseTreeService<String, ZKIotProdCategores, ZKIotProdCategoresDao> {

	/**
	 * 查询详情，包含父节点
	 */
    public ZKIotProdCategores getDetail(ZKIotProdCategores iotProdCategores) {
        return this.dao.getDetail(iotProdCategores);
    }

    // 根据代码取实体及明细
    public ZKIotProdCategores getByCode(String prodCategoresCode) {
        if (ZKStringUtils.isEmpty(prodCategoresCode)) {
            return null;
        }
        return this.dao.getByCode(prodCategoresCode);
    }

    // 激活
    @Transactional(readOnly = false)
    public int updateStatusActive(String pkId) {
        return this.dao.updateStatus(ZKIotProdCategores.sqlHelper().getTableName(), pkId, ValueKey.Status.normal);
    }

    // 禁用
    @Transactional(readOnly = false)
    public int updateStatusDisable(String pkId) {
        return this.dao.updateStatus(ZKIotProdCategores.sqlHelper().getTableName(), pkId, ValueKey.Status.disabled);
    }

    // 取指定父节点下的子节点的最大排序号
    public int getMaxSort(String parentId) {
        return this.dao.getMaxSort(parentId);
    }

    // 修改、删除数据 ===============================================
    @Transactional(readOnly = false)
    public int save(ZKIotProdCategores iotProdCategores) throws ZKValidatorException {
        if (iotProdCategores.isNewRecord()) {
            // 新增 ----------------
            // -- 查询代码是否存在，存在报错
            ZKIotProdCategores old = this.getByCode(iotProdCategores.getProdCategoresCode());
            if (old != null) {
                log.error("[>_<:20240103-1400-001] 场景代码: {} 已存在!", iotProdCategores.getProdCategoresCode());
                throw ZKBusinessException.as("zk.iot.000001", "场景代码已存在", iotProdCategores.getProdCategoresCode());
            }
            // -- 排序为空，查询最大排序，加1
            if (iotProdCategores.getSort() == null) {
                Integer sort = this.getMaxSort(iotProdCategores.getParentId());
                iotProdCategores.setSort(sort + 10);
            }
        }
        else {
            // 修改 ----------------
            // 啥也不用做
        }
        // 保存
        return super.save(iotProdCategores);
    }

    /**
     * 删除数据
     * 
     * @param entity
     */
    @Transactional(readOnly = false)
    public int del(ZKIotProdCategores iotProdCategores) {
        // 级联删除子节点
        ZKIotProdCategores p = new ZKIotProdCategores();
        p.setParentId(iotProdCategores.getPkId());
        List<ZKIotProdCategores> ls = this.findList(p);
        int result = 0;
        for (ZKIotProdCategores e : ls) {
            result += this.del(e);
        }
        result += super.del(iotProdCategores);
        return result;
    }

    /**
     * 物理删除数据
     * 
     * @param entity
     * @return
     */
    @Transactional(readOnly = false)
    public int diskDel(ZKIotProdCategores iotProdCategores) {
        // 级联删除子节点
        ZKIotProdCategores p = new ZKIotProdCategores();
        p.setParentId(iotProdCategores.getPkId());
        List<ZKIotProdCategores> ls = this.findList(iotProdCategores);
        int result = 0;
        for (ZKIotProdCategores e : ls) {
            result += this.del(e);
        }
        result += super.diskDel(iotProdCategores);
        return result;
    }
	
}