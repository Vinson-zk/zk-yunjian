/**
 * 
 */
package com.zk.sys.res.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.sys.res.dao.ZKSysResApplicationSystemDao;
import com.zk.sys.res.entity.ZKSysResApplicationSystem;

/**
 * ZKSysResApplicationSystemService
 * 
 * @author
 * @version
 */
@Service
@Transactional(readOnly = true)
public class ZKSysResApplicationSystemService
    extends ZKBaseService<String, ZKSysResApplicationSystem, ZKSysResApplicationSystemDao> {

    public ZKSysResApplicationSystem getByCode(String code) {
        return this.dao.getByCode(ZKSysResApplicationSystem.sqlHelper().getTableName(),
            ZKSysResApplicationSystem.sqlHelper().getTableAlias(),
            ZKSysResApplicationSystem.sqlHelper().getBlockSqlCols(), code);
    }

}