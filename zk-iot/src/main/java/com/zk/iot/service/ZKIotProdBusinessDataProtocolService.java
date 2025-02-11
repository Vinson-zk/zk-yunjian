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
import com.zk.iot.dao.ZKIotProdBusinessDataProtocolDao;
import com.zk.iot.entity.ZKIotProdBusinessDataProtocol;
import com.zk.iot.entity.ZKIotProdBusinessDataProtocol.ValueKey;

/**
 * ZKIotProdBusinessDataProtocolService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKIotProdBusinessDataProtocolService
        extends ZKBaseService<String, ZKIotProdBusinessDataProtocol, ZKIotProdBusinessDataProtocolDao> {

    // 根据代码取实体及明细
    public ZKIotProdBusinessDataProtocol getByCode(String protocolCode) {
        if (ZKStringUtils.isEmpty(protocolCode)) {
            return null;
        }
        return this.dao.getByCode(protocolCode);
    }

    // 激活
    @Transactional(readOnly = false)
    public int updateStatusActive(String pkId) {
        return this.dao.updateStatus(ZKIotProdBusinessDataProtocol.sqlHelper().getTableName(), pkId,
                ValueKey.Status.normal);
    }

    // 禁用
    @Transactional(readOnly = false)
    public int updateStatusDisable(String pkId) {
        return this.dao.updateStatus(ZKIotProdBusinessDataProtocol.sqlHelper().getTableName(), pkId,
                ValueKey.Status.disabled);
    }

    // 取指定父节点下的子节点的最大排序号
    public Integer getMaxSort() {
        return this.dao.getMaxSort();
    }

    // 修改、删除数据 ===============================================
    @Transactional(readOnly = false)
    public int save(ZKIotProdBusinessDataProtocol dataProtocol) throws ZKValidatorException {
        if (dataProtocol.isNewRecord()) {
            // 新增 ----------------
            // -- 查询代码是否存在，存在报错
            ZKIotProdBusinessDataProtocol old = this.getByCode(dataProtocol.getProtocolCode());
            if (old != null) {
                log.error("[>_<:20250106-1142-001] 业务数据处理协议代码: {} 已存在!", dataProtocol.getProtocolCode());
                throw ZKBusinessException.as("zk.iot.000003", "业务数据处理协议代码已存在", dataProtocol.getProtocolCode());
            }
            // -- 排序为空，查询最大排序，加1
            if (dataProtocol.getSort() == null) {
                Integer sort = this.getMaxSort();
                if (sort == null) {
                    sort = 0;
                }
                dataProtocol.setSort(sort + 10);
            }
        }
        else {
            // 修改 ----------------
            // 啥也不用做
        }

        // 保存
        return super.save(dataProtocol);
    }

	
	
}