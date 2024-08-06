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
* @Title: ZKPlatformCertService.java 
* @author Vinson 
* @Package com.zk.wechat.pay.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 22, 2021 6:33:57 PM 
* @version V1.0 
*/
package com.zk.wechat.pay.service;

import java.io.File;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.core.commons.data.ZKOrder;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.commons.data.ZKSortMode;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKFileUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.wechat.common.ZKWechatUtils;
import com.zk.wechat.pay.dao.ZKPlatformCertDao;
import com.zk.wechat.pay.entity.ZKPlatformCert;

/** 
* @ClassName: ZKPlatformCertService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
@Transactional(readOnly = true)
public class ZKPlatformCertService extends ZKBaseService<String, ZKPlatformCert, ZKPlatformCertDao> {
    
    /**
     * 根据商户号和平台证书序列，取平台证书信息
     *
     * @Title: getBySerial
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 22, 2021 6:44:30 PM
     * @param mchid
     * @param serialNo
     * @return
     * @return ZKPlatformCert
     */
    public ZKPlatformCert getBySerial(String mchid, String serialNo) {
        return this.dao.getBySerial(ZKPlatformCert.sqlHelper().getTableName(),
                ZKPlatformCert.sqlHelper().getBlockSqlCols(""), mchid, serialNo, ZKPlatformCert.DEL_FLAG.normal);
    }
    
    // 取最近生效的证书，有两个时，取失效日期最晚的；
    public ZKPlatformCert getCertByMchid(String mchid) {
        ZKPlatformCert q = new ZKPlatformCert();
        ZKPage<ZKPlatformCert> page = new ZKPage<ZKPlatformCert>();
        page.setSorters(ZKOrder.asOrder("certEffectiveTime", ZKSortMode.DESC),
                ZKOrder.asOrder("certExpirationTime", ZKSortMode.DESC));
        page.setPageSize(1);
        q.setMchid(mchid);

        page = this.findPage(page, q);

        if (page.getTotalCount() > 0) {
            return page.getResult().get(0);
        }
        return null;
    }

    // 创建保存新下载的微信平台证书
    @Transactional(readOnly = false)
    public ZKPlatformCert create(byte[] certBytes, String mchid, String serialNo, String effectiveTime,
            String expireTime) throws Exception {

        File file = null;
        ZKPlatformCert platformCert = new ZKPlatformCert();
        platformCert.setMchid(mchid);
        platformCert.setCertSerialNo(serialNo);
        platformCert.setCertEffectiveTime(ZKDateUtils.parseDate(effectiveTime, ZKDateUtils.DF_yyyy_MM_ddTHH_mm_ssZZ));
        platformCert.setCertExpirationTime(ZKDateUtils.parseDate(expireTime, ZKDateUtils.DF_yyyy_MM_ddTHH_mm_ssZZ));
        platformCert.setCertPath(ZKStringUtils.replaceByPoint(ZKPlatformCert.defaultCertPath, mchid, serialNo));

        // 将证书写到本地
        file = new File(ZKWechatUtils.getFilePath(platformCert.getCertPath()));
        // 证书文件存在，先删除已存在证书文件；坑，不能用 file.deleteOnExit(); 来删除；文件倒是可以正常写和读取，但
        // spring boot 关闭时，文件会被删除掉；
        // file.deleteOnExit();
        // 证书文件存在，先删除已存在证书文件；不删除也没关系，但是我习惯删除一下，以保证正确
        if (ZKFileUtils.isFileExists(file)) {
            file.delete();
        }
        ZKFileUtils.createFile(file);
        ZKFileUtils.writeFile(certBytes, file, false);

        // 如果微信平台证书已存在，删除已有的平台证书
        ZKPlatformCert pCert = this.getBySerial(mchid, serialNo);
        if (pCert != null) {
            // 逻辑删除
            this.del(pCert);
        }

        // 保存证书数据
        this.save(platformCert);

        return platformCert;

    }

}
