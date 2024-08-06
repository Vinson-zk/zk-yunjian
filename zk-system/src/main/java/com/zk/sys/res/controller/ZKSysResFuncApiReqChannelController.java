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
* @Title: ZKSysResFuncApiReqChannelController.java 
* @author Vinson 
* @Package com.zk.sys.res.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 28, 2022 10:44:08 AM 
* @version V1.0 
*/
package com.zk.sys.res.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.sys.res.entity.ZKSysResFuncApi;
import com.zk.sys.res.entity.ZKSysResFuncApiReqChannel;
import com.zk.sys.res.entity.ZKSysResRequestChannel;
import com.zk.sys.res.service.ZKSysResFuncApiReqChannelService;
import com.zk.sys.res.service.ZKSysResFuncApiService;
import com.zk.sys.res.service.ZKSysResRequestChannelService;

/** 
* @ClassName: ZKSysResFuncApiReqChannelController 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/${zk.path.sys.res}/sysResFuncApiReqChannel")
public class ZKSysResFuncApiReqChannelController extends ZKBaseController {

    @Autowired
    ZKSysResFuncApiReqChannelService funcApiReqChannelService;

    @Autowired
    ZKSysResFuncApiService funcApiService;

    @Autowired
    ZKSysResRequestChannelService requestChannelService;

    /**
     * 根据请求渠道查询渠道已分配的功能API接口的关联关系；
     *
     * @Title: findRelationByChannel
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 1, 2022 4:49:07 PM
     * @param reqChannelId
     * @param funcApiId
     * @param hReq
     * @return ZKMsgRes
     */
    @RequestMapping(value = "findRelationByChannel", method = RequestMethod.GET)
    public ZKMsgRes findRelationByChannel(@RequestParam(value = "reqChannelId") String reqChannelId,
            HttpServletRequest hReq) {
        ZKPage<ZKSysResFuncApiReqChannel> resPage = ZKPage.asPage(hReq);
        ZKSysResFuncApiReqChannel e = new ZKSysResFuncApiReqChannel();
        e.setChannelId(reqChannelId);
        resPage = this.funcApiReqChannelService.findPage(resPage, e);
        return ZKMsgRes.asOk(null, resPage);
    }

    /**
     * 根据功能API接口查询功能API接口已分配的请求渠道的关联关系；
     *
     * @Title: findRelationByFuncApi
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 7, 2022 2:21:52 PM
     * @param funcApiId
     * @param hReq
     * @return
     * @return ZKMsgRes
     */
    @RequestMapping(value = "findRelationByFuncApi", method = RequestMethod.GET)
    public ZKMsgRes findRelationByFuncApi(@RequestParam(value = "funcApiId") String funcApiId,
            HttpServletRequest hReq) {
        ZKPage<ZKSysResFuncApiReqChannel> resPage = ZKPage.asPage(hReq);
        ZKSysResFuncApiReqChannel e = new ZKSysResFuncApiReqChannel();
        e.setFuncApiId(funcApiId);
        resPage = this.funcApiReqChannelService.findPage(resPage, e);
        return ZKMsgRes.asOk(null, resPage);
    }

    /**
     * 给 API功能接口 配置 渠道的关联关系；
     *
     * @Title: setRelationByFuncApi
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 7, 2022 5:47:04 PM
     * @param funcApiId
     * @param addChannels
     * @param hReq
     * @return
     * @return ZKMsgRes
     * @throws InterruptedException
     */
    @RequestMapping(value = "setRelationByFuncApi/{funcApiId}", method = RequestMethod.POST)
    public ZKMsgRes setRelationByFuncApi(@PathVariable(value = "funcApiId") String funcApiId,
            @RequestBody List<ZKSysResRequestChannel> addChannels, HttpServletRequest hReq)
            throws InterruptedException {
        Thread.sleep(2000);
        ZKSysResFuncApi funcApi = funcApiService.get(new ZKSysResFuncApi(funcApiId));
        return ZKMsgRes.asOk(null, this.funcApiReqChannelService.addByFuncApi(funcApi, addChannels, null));
    }

    /**
     * 给 渠道 配置 API功能接口 的关联关系；
     *
     * @Title: setRelationByReqChannel
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 7, 2022 5:47:09 PM
     * @param reqChannelId
     * @param addFuncApis
     * @param hReq
     * @return
     * @return ZKMsgRes
     */
    @RequestMapping(value = "setRelationByReqChannel/{reqChannelId}", method = RequestMethod.POST)
    public ZKMsgRes setRelationByReqChannel(@PathVariable(value = "reqChannelId") String reqChannelId,
            @RequestBody List<ZKSysResFuncApi> addFuncApis, HttpServletRequest hReq) {
        ZKSysResRequestChannel reqChannel = this.requestChannelService.get(new ZKSysResRequestChannel(reqChannelId));
        return ZKMsgRes.asOk(null, this.funcApiReqChannelService.addByChannel(reqChannel, addFuncApis, null));
    }

}
