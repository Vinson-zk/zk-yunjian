/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSysOrgUserEditLogDao.java 
* @author Vinson 
* @Package com.zk.sys.org.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 21, 2024 7:13:36 PM 
* @version V1.0 
*/
package com.zk.sys.org.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.sys.org.entity.ZKSysOrgUserEditLog;

/** 
* @ClassName: ZKSysOrgUserEditLogDao 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKMyBatisDao
public interface ZKSysOrgUserEditLogDao extends ZKBaseDao<String, ZKSysOrgUserEditLog> {


    @Select(value = {
            "SELECT ${sCols} FROM ${tn} ${alias} WHERE ${alias}.c_user_id = #{userId} AND ${alias}.c_edit_type = #{editType} ORDER BY ${alias}.c_create_date DESC " })
    List<ZKSysOrgUserEditLog> findByEditType(@Param("tn") String tn, @Param("alias") String alias,
            @Param("sCols") String sCols, @Param("userId") String userId, @Param("editType") int editType);

    // 根据用户做逻辑删除
    @Delete("UPDATE ${tn} SET c_del_flag = #{delFlag}, c_update_user_id = #{updateUserId}, c_update_date = #{updateDate} WHERE c_user_id = #{userId} ")
    int delByUserId(@Param("tn") String tn, @Param("userId") String userId, @Param("delFlag") int delFlag,
            @Param("updateUserId") String updateUserId, @Param("updateDate") Date updateDate);

    // 根据用户做物理删除
    @Delete("DELETE FROM ${tn} WHERE c_user_id = #{userId}")
    int diskDelByUserId(@Param("tn") String tn, @Param("userId") String userId);

}
