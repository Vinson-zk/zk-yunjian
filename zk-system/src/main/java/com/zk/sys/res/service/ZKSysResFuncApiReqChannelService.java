/**
 * Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc. address: All rights reserved.
 *
 * This software is the confidential and proprietary information of ZK-Vinson Technologies, Inc. ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with ZK-Vinson.
 *
 * @Title: ZKSysResFuncApiReqChannelService.java
 * @author Vinson
 * @Package com.zk.sys.res.service
 * @Description: TODO(simple description this file what to do. )
 * @date Nov 30, 2021 10:54:28 AM
 * @version V1.0
 */
package com.zk.sys.res.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKBusinessException;
import com.zk.sys.res.dao.ZKSysResFuncApiReqChannelDao;
import com.zk.sys.res.entity.ZKSysResFuncApi;
import com.zk.sys.res.entity.ZKSysResFuncApiReqChannel;
import com.zk.sys.res.entity.ZKSysResRequestChannel;

/**
 * @ClassName: ZKSysResFuncApiReqChannelService
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class ZKSysResFuncApiReqChannelService
    extends ZKBaseService<String, ZKSysResFuncApiReqChannel, ZKSysResFuncApiReqChannelDao> {

    // public ZKPage<ZKSysResFuncApiReqChannel> findByChannelCode(ZKPage<ZKSysResFuncApiReqChannel> page,
    // String channelCode, String systemCode) {
    // ZKSysResFuncApiReqChannel e = new ZKSysResFuncApiReqChannel();
    // e.setChannelCode(channelCode);
    // e.setSystemCode(systemCode);
    // return super.findPage(page, e);
    // }

    // public ZKPage<ZKSysResFuncApiReqChannel> findByApiCode(ZKPage<ZKSysResFuncApiReqChannel> page, String apiCode) {
    // ZKSysResFuncApiReqChannel e = new ZKSysResFuncApiReqChannel();
    // e.setFuncApiCode(apiCode);
    // return super.findPage(page, e);
    // }

    /********************************************************************************************/
    /*** 取 ***********************************************************************************/
    /********************************************************************************************/

    /**
     * 根据渠道ID和接口ID取 关系实体
     *
     * @Title: getByRelationId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 30, 2021 11:03:47 AM
     * @param channelId
     *            渠道ID
     * @param funcApiId
     *            API功能接口ID
     * @return
     * @return ZKSysResFuncApiReqChannel
     */
    public ZKSysResFuncApiReqChannel getByRelationId(String channelId, String funcApiId) {
        return this.dao.getByRelationId(ZKSysResFuncApiReqChannel.sqlHelper().getTableName(),
            ZKSysResFuncApiReqChannel.sqlHelper().getTableAlias(),
            ZKSysResFuncApiReqChannel.sqlHelper().getBlockSqlCols(), channelId, funcApiId);
    }

    /**
     * 根据 关联关系 代码取 关系实体
     *
     * @Title: getByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 30, 2021 11:03:33 AM
     * @param channelCode
     *            渠道代码
     * @param systemCode
     *            功能API接口所在系统代码
     * @param funcApiCode
     *            API功能接口代码
     * @return
     * @return ZKSysResFuncApiReqChannel
     */
    ZKSysResFuncApiReqChannel getByCode(String channelCode, String systemCode, String funcApiCode) {
        return this.dao.getByCode(ZKSysResFuncApiReqChannel.sqlHelper().getTableName(),
            ZKSysResFuncApiReqChannel.sqlHelper().getTableAlias(),
            ZKSysResFuncApiReqChannel.sqlHelper().getBlockSqlCols(), channelCode, systemCode, funcApiCode);
    }

    /********************************************************************************************/
    /*** 分配，关联 *******************************************************************************/
    /********************************************************************************************/

    /**
     * 通实体关联渠道和接口；单个渠道实体与接口实体关联
     *
     * @Title: save
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 30, 2021 12:13:07 PM
     * @param channel
     *            渠道实体
     * @param funcApi
     *            API功能接口
     * @return int
     */
    @Transactional(readOnly = false)
    public ZKSysResFuncApiReqChannel save(ZKSysResRequestChannel channel, ZKSysResFuncApi funcApi) {
        ZKSysResFuncApiReqChannel funcApiReqChannel =
            new ZKSysResFuncApiReqChannel(channel.getPkId(), funcApi.getPkId());
        // 先查询关联关系是否存在
        ZKSysResFuncApiReqChannel old = this.getByRelationId(channel.getPkId(), funcApi.getPkId());
        if (old != null) {
            if (ZKSysResFuncApiReqChannel.DEL_FLAG.normal == old.getDelFlag().intValue()) {
                // 关系已存在；
                return old;
            } else {
                funcApiReqChannel.setPkId(old.getPkId());
                // funcApiReqChannel.setDelFlag(ZKSysResFuncApiReqChannel.DEL_FLAG.normal);
                super.restore(funcApiReqChannel);
            }
        }
        // 保存关系
        funcApiReqChannel.setChannelCode(channel.getCode());
        funcApiReqChannel.setSystemCode(funcApi.getSystemCode());
        funcApiReqChannel.setFuncApiCode(funcApi.getCode());
        super.save(funcApiReqChannel);
        return funcApiReqChannel;
    }

    /**
     * 通过实体给请求渠道分配 API功能接口；
     *
     * @Title: addByChannel
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 30, 2021 12:04:07 PM
     * @param channel
     *            目标渠道
     * @param addFuncApis
     *            需要关联的 API功能接口
     * @param delFuncApis
     *            需要删除关联的 API功能接口；当为 null 时，删除所有已关联的 API功能接口，重新关联新的 API功能接口；
     * @return List<ZKSysResFuncApiReqChannel>
     */
    @Transactional(readOnly = false)
    public List<ZKSysResFuncApiReqChannel> addByChannel(ZKSysResRequestChannel channel,
        List<ZKSysResFuncApi> addFuncApis, List<ZKSysResFuncApi> delFuncApis) {
        if (channel == null) {
            log.error("[>_<:20220406-1916-002] 请求渠道不存在!");
            throw ZKBusinessException.as("zk.sys.000006", "请求渠道不存在");
        }
        // 先删除 需要删除的关联关系
        if (delFuncApis == null) {
            this.diskDelByChannelId(channel.getPkId());
        } else {
            delFuncApis.forEach(item -> {
                this.diskDelByChannelIdAndApiId(channel.getPkId(), item.getPkId());
            });
        }
        // 保存分配的关联关系
        if (addFuncApis != null && !addFuncApis.isEmpty()) {
            List<ZKSysResFuncApiReqChannel> res = new ArrayList<>();
            addFuncApis.forEach(item -> {
                res.add(this.save(channel, item));
            });
            return res;
        }
        return Collections.emptyList();
    }

    /**
     * 给 API功能接口 分配 请求渠道 关系
     *
     * @Title: addByFuncApi
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 30, 2021 12:13:00 PM
     * @param funcApi
     *            目标 API功能接口
     * @param addChannels
     *            需要新增的渠道关系
     * @param delChannels
     *            需要删除的旧渠道关系；当为 null 时，删除所有已关联的 渠道关系，重新关联新的 渠道关系；
     * @return List<ZKSysResFuncApiReqChannel>
     */
    @Transactional(readOnly = false)
    public List<ZKSysResFuncApiReqChannel> addByFuncApi(ZKSysResFuncApi funcApi,
        List<ZKSysResRequestChannel> addChannels, List<ZKSysResRequestChannel> delChannels) {
        if (funcApi == null) {
            log.error("[>_<:20220406-1916-001] 功能接口API不存在!");
            throw ZKBusinessException.as("zk.sys.000005", "功能接口API不存在");
        }
        // 先删除 需要删除的关联关系
        if (delChannels == null) {
            this.diskDelByApiId(funcApi.getPkId());
        } else {
            delChannels.forEach(item -> {
                this.diskDelByChannelIdAndApiId(item.getPkId(), funcApi.getPkId());
            });
        }
        // 保存分配的关联关系
        if (addChannels != null && !addChannels.isEmpty()) {
            List<ZKSysResFuncApiReqChannel> res = new ArrayList<>();
            addChannels.forEach(item -> {
                res.add(this.save(item, funcApi));
            });
            return res;
        }
        return Collections.emptyList();
    }

    /********************************************************************************************/
    /*** 删除 ***********************************************************************************/
    /********************************************************************************************/

    // /**
    // * 根据 渠道ID 和 API功能接口ID 逻辑删除 关联关系；
    // *
    // * @Title: delByChannelIdAndApiId
    // * @Description: TODO(simple description this method what to do.)
    // * @author Vinson
    // * @date Apr 1, 2022 10:52:19 AM
    // * @param channelPkId
    // * @param apiPkId
    // * @return
    // * @return int
    // */
    // @Transactional(readOnly = false)
    // public int delByChannelIdAndApiId(String channelPkId, String apiPkId) {
    // return this.dao.delByChannelIdAndApiId(ZKSysResFuncApiReqChannel.sqlHelper().getTableName(), channelPkId,
    // apiPkId, ZKSysResFuncApiReqChannel.DEL_FLAG.delete);
    // }
    //
    // /**
    // * 根据 渠道ID，逻辑删除所有关联的 API功能接口关系
    // *
    // * @Title: delByChannelId
    // * @Description: TODO(simple description this method what to do.)
    // * @author Vinson
    // * @date Nov 30, 2021 11:32:27 AM
    // * @param channelPkId
    // * @return
    // * @return int
    // */
    // @Transactional(readOnly = false)
    // public int delByChannelId(String channelPkId) {
    // return this.dao.delByChannelId(ZKSysResFuncApiReqChannel.sqlHelper().getTableName(), channelPkId,
    // ZKSysResFuncApiReqChannel.DEL_FLAG.delete);
    // }
    //
    // /**
    // * 根据 API功能接口ID，逻辑删除所有关联的渠道关系
    // *
    // * @Title: delByApiId
    // * @Description: TODO(simple description this method what to do.)
    // * @author Vinson
    // * @date Nov 30, 2021 11:32:34 AM
    // * @param apiPkId
    // * @return
    // * @return int
    // */
    // @Transactional(readOnly = false)
    // public int delByApiId(String apiPkId) {
    // return this.dao.delByApiId(ZKSysResFuncApiReqChannel.sqlHelper().getTableName(), apiPkId,
    // ZKSysResFuncApiReqChannel.DEL_FLAG.delete);
    // }

    /**
     * 根据 渠道ID和 API功能接口ID 物理删除关联关系
     *
     * @Title: diskDelByChannelIdAndApiId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 1, 2022 10:52:27 AM
     * @param channelPkId
     * @param apiPkId
     * @return
     * @return int
     */
    public int diskDelByChannelIdAndApiId(String channelPkId, String apiPkId) {
        return this.dao.diskDelByChannelIdAndApiId(ZKSysResFuncApiReqChannel.sqlHelper().getTableName(), channelPkId,
            apiPkId);
    }

    /**
     * 根据 渠道ID，物理删除所有关联的 API功能接口关系
     *
     * @Title: diskDelByChannelId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 30, 2021 11:32:30 AM
     * @param channelPkId
     * @return
     * @return int
     */
    @Transactional(readOnly = false)
    public int diskDelByChannelId(String channelPkId) {
        return this.dao.diskDelByChannelId(ZKSysResFuncApiReqChannel.sqlHelper().getTableName(), channelPkId);
    }

    /**
     * 根据 API功能接口ID，物理删除所有关联的渠道关系
     *
     * @Title: diskDelByApiId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 30, 2021 11:32:39 AM
     * @param apiPkId
     * @return
     * @return int
     */
    @Transactional(readOnly = false)
    public int diskDelByApiId(String apiPkId) {
        return this.dao.diskDelByApiId(ZKSysResFuncApiReqChannel.sqlHelper().getTableName(), apiPkId);
    }

}
