/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSysResFuncApiReqChannelDao.java 
* @author Vinson 
* @Package com.zk.sys.res.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Nov 30, 2021 10:54:18 AM 
* @version V1.0 
*/
package com.zk.sys.res.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.sys.res.entity.ZKSysResFuncApiReqChannel;

/** 
* @ClassName: ZKSysResFuncApiReqChannelDao 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKMyBatisDao
public interface ZKSysResFuncApiReqChannelDao extends ZKBaseDao<String, ZKSysResFuncApiReqChannel> {

    // 根据渠道ID和api接口ID 逻辑删除
    @Update(" UPDATE ${tn} SET c_del_flag = ${delFlag} WHERE c_channel_id = ${channelPkId} and c_func_api_id = ${apiPkId} ")
    int delByChannelIdAndApiId(@Param("tn") String tn, @Param("channelPkId") String channelPkId,
            @Param("apiPkId") String apiPkId, @Param("delFlag") int delFlag);

    // 根据渠道ID，逻辑删除所有关联的接口
    @Update(" UPDATE ${tn} SET c_del_flag = ${delFlag} WHERE c_channel_id = ${channelPkId} ")
    int delByChannelId(@Param("tn") String tn, @Param("channelPkId") String channelPkId,
            @Param("delFlag") int delFlag);

    // 根据API接口ID，逻辑删除所有关联的接口
    @Update(" UPDATE ${tn} SET c_del_flag = ${delFlag} WHERE c_func_api_id = ${apiPkId} ")
    int delByApiId(@Param("tn") String tn, @Param("apiPkId") String apiPkId, @Param("delFlag") int delFlag);

    // 根据渠道ID和api接口ID 物理删除
    @Delete(" DELETE FROM ${tn} WHERE c_channel_id = ${channelPkId} and c_func_api_id = ${apiPkId} ")
    int diskDelByChannelIdAndApiId(@Param("tn") String tn, @Param("channelPkId") String channelPkId,
            @Param("apiPkId") String apiPkId);

    // 根据渠道ID，物理删除所有关联的接口
    @Delete(" DELETE FROM ${tn} WHERE c_channel_id = ${channelPkId} ")
    int diskDelByChannelId(@Param("tn") String tn, @Param("channelPkId") String channelPkId);

    // 根据API接口ID，物理删除所有关联的接口
    @Delete(" DELETE FROM ${tn} WHERE c_func_api_id = ${apiPkId} ")
    int diskDelByApiId(@Param("tn") String tn, @Param("apiPkId") String apiPkId);

    /**
     * 根据渠道ID和接口ID取 关系实体
     *
     * @Title: getByRelationId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 30, 2021 10:56:45 AM
     * @param tn
     * @param tAlias
     * @param sCols
     * @param channelId
     * @param funcApiId
     * @return
     * @return ZKSysResFuncApiReqChannel
     */
    @Select("SELECT ${sCols} FROM ${tn} ${tAlias} WHERE c_channel_id = #{channelId} and c_func_api_id = #{funcApiId} ")
    ZKSysResFuncApiReqChannel getByRelationId(@Param("tn") String tn, @Param("tAlias") String tAlias,
            @Param("sCols") String sCols, @Param("channelId") String channelId, @Param("funcApiId") String funcApiId);

    /**
     * 根据 关联关系 代码取 关系实体
     *
     * @Title: getByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 30, 2021 11:00:33 AM
     * @param tn
     * @param tAlias
     * @param sCols
     * @param channelCode
     * @param systemCode
     * @param funcApiCode
     * @return
     * @return ZKSysResFuncApiReqChannel
     */
    @Select("SELECT ${sCols} FROM ${tn} ${tAlias} WHERE c_channel_code = #{channelCode} and c_system_code = #{systemCode} and c_func_api_code = #{funcApiCode} ")
    ZKSysResFuncApiReqChannel getByCode(@Param("tn") String tn, @Param("tAlias") String tAlias,
            @Param("sCols") String sCols, @Param("channelCode") String channelCode,
            @Param("systemCode") String systemCode, @Param("funcApiCode") String funcApiCode);
}

