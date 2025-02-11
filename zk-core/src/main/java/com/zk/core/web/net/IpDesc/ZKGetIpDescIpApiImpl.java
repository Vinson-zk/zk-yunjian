/** 
* Copyright (c) 2012-2025 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKGetIpDescIpApiImpl.java 
* @author Vinson 
* @Package com.zk.core.web.net.IpDesc 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 8, 2025 6:01:55 PM 
* @version V1.0 
*/
package com.zk.core.web.net.IpDesc;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson2.JSONObject;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKLocaleUtils;
import com.zk.core.web.net.ZKGetIpDesc;
import com.zk.core.web.net.ZKIpDesc;
import com.zk.core.web.utils.ZKHttpApiUtils;

/**
 * @ClassName: ZKGetIpDescIpApiImpl
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKGetIpDescIpApiImpl implements ZKGetIpDesc {
    
    protected Logger log = LogManager.getLogger(this.getClass());

    @Override
    public ZKIpDesc getIpDesc(String ip, Locale locale) {
        /*
        http://ip-api.com/json/175.8.94.202?lang=zh-CN
        {
            "status":"success",
            "country":"中国",
            "countryCode":"CN",
            "region":"HN",
            "regionName":"湖南",
            "city":"株洲",
            "zip":"",
            "lat":27.8329,
            "lon":113.1454,
            "timezone":"Asia/Shanghai",
            "isp":"Chinanet",
            "org":"Chinanet HN",
            "as":"AS4134 CHINANET-BACKBONE",
            "query":"175.8.94.202"
        }
        {
            "status":"success",
            "country":"香港",
            "countryCode":"HK",
            "region":"HCW",
            "regionName":"中西區",
            "city":"香港",
            "zip":"",
            "lat":22.3193,"lon":114.1693,
            "timezone":"Asia/Hong_Kong",
            "isp":"Eons Data Communications Limited",
            "org":"Eons Data Communications Limited",
            "as":"AS138997 Eons Data Communications Limited",
            "query":"103.152.220.13"
        }
        */

        StringBuffer url = new StringBuffer();
        try {
            if(locale == null) {
                locale = ZKLocaleUtils.getLocale();
            }
            StringBuffer ipInfoSb = new StringBuffer();

            url.append("http://ip-api.com/json/");
            url.append(ip);
            url.append("?lang=");
            url.append(locale.toLanguageTag());
            ZKHttpApiUtils.get(url.toString(), null, ipInfoSb);
            JSONObject json = ZKJsonUtils.parseJSONObject(ipInfoSb.toString());
            return ZKIpDesc.as(json.getString("query"), json.getString("country"), json.getString("countryCode"),
                    json.getString("city"), json.getString("regionName"), json.getString("region"),
                    json.getString("lat"), json.getString("lon"), json.getString("timezone"), json.getString("isp"));
        }catch (Exception e) {
            log.error("[>_<:20250208-1824-001] 读取IP归属地失败；url:{}; errMsg: {}", url.toString(), e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
