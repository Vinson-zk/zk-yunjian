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
 * @Title: ZKSerCenCertificateController.java 
 * @author Vinson 
 * @Package com.zk.server.central.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:19:29 PM 
 * @version V1.0   
*/
package com.zk.server.central.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zk.core.commons.ZKContentType;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKEncodingUtils;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKObjectUtils;
import com.zk.core.utils.ZKStreamUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.server.central.controller.base.ZKSerCenBaseController;
import com.zk.server.central.entity.ZKSerCenCertificate;
import com.zk.server.central.service.ZKSerCenCertificateService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** 
* @ClassName: ZKSerCenCertificateController 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@Controller
@RequestMapping("${zk.path.admin}/${zk.path.serCen}/cer")
public class ZKSerCenCertificateController extends ZKSerCenBaseController {

    @Autowired
    protected ZKSerCenCertificateService serCenCertificateService;

    /***
     * 数据操作
     * 
     * @throws ZKValidatorException
     ***/
    @RequestMapping(value = "sc", method = RequestMethod.POST)
    @ResponseBody
    public ZKMsgRes serviceCertificatePost(@RequestBody ZKSerCenCertificate serCenCertificate)
            throws ZKValidatorException {
        if (serCenCertificateService.save(serCenCertificate) > 0) {
            return ZKMsgRes.asOk(serCenCertificate);
        }
        else {
            log.error("[>_<:20230215-2337-001]证书生成异常。");
            throw ZKBusinessException.as("zk.ser.cen.000011", null);
        }
    }

    @RequestMapping(value = "sc", method = RequestMethod.GET)
    @ResponseBody
    public ZKMsgRes serviceCertificateGet(@RequestParam(value = "pkId") String pkId) {

        ZKMsgRes zkMsgRes = ZKMsgRes.asOk();
        zkMsgRes.setData(serCenCertificateService.get(new ZKSerCenCertificate(pkId)));
        return zkMsgRes;
    }

    @RequestMapping(value = "sc", method = RequestMethod.DELETE)
    @ResponseBody
    public ZKMsgRes serviceCertificateDelete(@RequestParam(value = "pkIds", required = true) List<String> pkIds) {
        int count = 0;
        for (String pkId : pkIds) {
            count += this.serCenCertificateService.del(new ZKSerCenCertificate(pkId));
        }
        ZKMsgRes zkMsgRes = ZKMsgRes.asOk();
        zkMsgRes.setData(count);

        return zkMsgRes;
    }

    /**
     * 查询
     *
     * @Title: serviceCertificatePage
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 10, 2019 9:09:38 AM
     * @param serCenCertificate
     * @param hReq
     * @return
     * @return ZKMsgRes
     */
    @RequestMapping(value = "scs", method = RequestMethod.GET)
    @ResponseBody
    public ZKMsgRes serviceCertificateList(ZKSerCenCertificate serCenCertificate, HttpServletRequest hReq) {
        ZKMsgRes zkMsgRes = ZKMsgRes.asOk();
        zkMsgRes.setData(serCenCertificateService.findList(serCenCertificate));
        return zkMsgRes;
    }

    /**
     * 分布查询
     *
     * @Title: serviceCertificatePage
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 10, 2019 9:09:38 AM
     * @param serCenCertificate
     * @param hReq
     * @return
     * @return ZKMsgRes
     */
    @RequestMapping(value = "scPage", method = RequestMethod.GET)
    @ResponseBody
    public ZKMsgRes serviceCertificatePage(ZKSerCenCertificate serCenCertificate, HttpServletRequest hReq) {

        ZKMsgRes zkMsgRes = ZKMsgRes.asOk();
        ZKPage<ZKSerCenCertificate> page = ZKPage.asPage(hReq);

        zkMsgRes.setData(serCenCertificateService.findPage(page, serCenCertificate));
        return zkMsgRes;
    }

    /*************************************************************/

    /**
     * 证书 禁用，启用
     * 
     *
     * @Title: scStatus
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 11, 2019 11:21:45 PM
     * @param pkIds
     *            操作的证书主键数组
     * @param status
     *            状态，0-启用；1-禁用
     * @return
     * @return ZKMsgRes
     */
    @RequestMapping(value = "scStatus", method = RequestMethod.POST)
    @ResponseBody
    public ZKMsgRes scStatus(@RequestParam(value = "pkIds", required = true) List<String> pkIds,
            @RequestParam(value = "status", required = true) int status) {

        if (status != ZKSerCenCertificate.StatusType.Disabled && status != ZKSerCenCertificate.StatusType.Enable) {
            // 上送的状态不对，需要抛出异常
            log.error("[>_<:20230215-2337-002] 上送的状态错误【{}】。", status);
            throw ZKBusinessException.asMsg("zk.000002",
                    ZKMsgUtils.getMessage(ZKWebUtils.getLocale(), "data.validation.rang", 0, 1), null, (Object[]) null);
        }

        ZKMsgRes zkMsgRes = ZKMsgRes.asOk();
        ZKSerCenCertificate zkSc = null;
        int count = 0;
        for (String pkId : pkIds) {
            zkSc = new ZKSerCenCertificate(pkId);
            zkSc.setStatus(status);
            count += this.serCenCertificateService.updateStatus(zkSc);
        }
        zkMsgRes.setData(count);
        return zkMsgRes;
    }

    /**
     * 下载指定证书公钥
     *
     * @Title: download
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 10, 2019 4:53:50 PM
     * @param pkId
     * @param hReq
     * @param hRes
     * @throws IOException
     * @return void
     */
    @RequestMapping(value = "download", method = RequestMethod.GET)
    public void download(@RequestParam(value = "pkId") String pkId, HttpServletRequest hReq, HttpServletResponse hRes)
            throws IOException {

        ZKSerCenCertificate sc = this.serCenCertificateService.get(new ZKSerCenCertificate(pkId));
        if (sc == null) {
            log.error("[>_<:20230215-2337-005] 证书不存在【{}】。", pkId);
            throw ZKBusinessException.as("zk.ser.cen.000005", null); //
        }

//        if (sc.getDelFlag().equals(ZKBaseEntity.DEL_FLAG.delete)) {
//            throw new ZKMsgResException("zk.ser.cen.000006"); // 证书已被删除
//        }
//        if (sc.getStatus().equals(ZKSerCenCertificate.StatusType.Disabled)) {
//            throw new ZKMsgResException("zk.ser.cen.000007"); // 证书已被禁用
//        }

        byte[] publicKeys = ZKEncodingUtils.decodeHex(sc.getPublicKey());

        hRes.reset();
        hRes.setContentType(ZKContentType.DEFUALT.getContentType());

        hRes.setHeader("Content-Disposition",
                "headerIcon;filename=" + ZKStringUtils.toString(sc.getServerName() + ".cer", "ISO-8859-1"));
        hRes.setHeader("Content-Length", "" + publicKeys.length);

        ServletOutputStream sos = hRes.getOutputStream();
        ZKStreamUtils.readAndWrite(new ByteArrayInputStream(publicKeys), sos);
        ZKStreamUtils.closeStream(sos);

    }

    /*** 证书视图 ***/
    @RequestMapping("view/index")
    public ModelAndView vIndex(HttpServletRequest req) {

        ModelAndView mv = new ModelAndView("modules/serCen/cer/cerIndex");

        return mv;
    }

    @RequestMapping(value = "view/edit", method = RequestMethod.GET)
    public ModelAndView vEdit(@RequestParam(value = "pkId", required = false) String pkId, HttpServletRequest req) {

        ModelAndView mv = new ModelAndView("modules/serCen/cer/cerEdit");

        ZKSerCenCertificate cer = this.serCenCertificateService.get(new ZKSerCenCertificate(pkId));

//        cer = new ZKSerCenCertificate();
//        cer.setPkId("123");
//        cer.setStatus(1);
//        cer.setValidStartDate(new Date());

        mv.addObject("model", ZKObjectUtils.entityToMap(cer));
        return mv;
    }

}
