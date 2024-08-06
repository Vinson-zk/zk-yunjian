/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKSerCenCertificateDao.java 
 * @author Vinson 
 * @Package com.zk.server.central.dao 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:22:54 PM 
 * @version V1.0   
*/
package com.zk.server.central.dao;

import org.apache.ibatis.annotations.Update;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.server.central.entity.ZKSerCenCertificate;

/** 
* @ClassName: ZKSerCenCertificateDao 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@ZKMyBatisDao
public interface ZKSerCenCertificateDao extends ZKBaseDao<String, ZKSerCenCertificate> {

    /**
     * 修改证书状态
     *
     * @Title: updateStatus
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 23, 2019 5:34:23 PM
     * @param serCenCertificate
     * @return
     * @return int
     */
    @Update(value = { "UPDATE t_sc_server_certificate ",
            "SET c_status = #{status}, c_update_user_id = #{updateUserId}, c_update_date = #{updateDate} ",
            "WHERE c_pk_id = #{pkId}" })
    public int updateStatus(ZKSerCenCertificate serCenCertificate);

}
