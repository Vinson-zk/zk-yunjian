/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.mail.dao;

import org.apache.ibatis.annotations.Param;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.mail.entity.ZKMailTemplate;

/**
 * ZKMailTemplateDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKMailTemplateDao extends ZKBaseDao<String, ZKMailTemplate> {

    /**
     * 根据邮件类型，公司代码，语言 取模板实体，因为 sql 较为复杂，写 mapper 文件中
     *
     * @Title: getByTypeCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 26, 2022 4:53:10 PM
     * @param tn
     * @param alias
     * @param sCols
     * @param typeCode
     *            必须
     * @param companyId
     *            为空时，查询字段为 0
     * @param locale
     *            为空时，查询字段为 'default'
     * @param delFlag
     * @return ZKMailTemplate
     */
    ZKMailTemplate getByTypeCode(@Param("tn") String tn, @Param("alias") String alias, @Param("sCols") String sCols,
            @Param("typeCode") String typeCode, @Param("companyId") String companyId,
            @Param("locale") String locale, @Param("delFlag") Integer delFlag);
	
}