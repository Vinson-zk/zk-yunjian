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
* @Title: ZKCodeFileUtils.java 
* @author Vinson 
* @Package com.zk.code.generate.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 25, 2021 8:15:40 AM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.action;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.core.utils.ZKFileUtils;
import com.zk.core.utils.ZKFreeMarkersUtils;
import com.zk.devleopment.tool.gen.entity.ZKModule;
import com.zk.devleopment.tool.gen.entity.ZKTableInfo;

/** 
* @ClassName: ZKCodeFileUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCodeFileUtils {

    /**
     * 日志对象
     */
    protected static Logger log = LogManager.getLogger(ZKCodeFileUtils.class);

    /**
     * 取代码生成的文件的根路径
     *
     * @Title: getCodeRootPath
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 25, 2021 8:33:24 AM
     * @param rootPath
     *            以 / 结尾；不会自动添加 /
     * @param template
     * @param zkModule
     * @return String
     */
    public static String getCodeRootPath(String rootPath, ZKModule zkModule) {
        return getCodeRootPath(rootPath, null, zkModule);
    }

    public static String getCodeRootPath(String rootPath, Date date, ZKModule zkModule) {
        String fileRootPath = rootPath + zkModule.getModuleNameCap();
        if (date != null) {
            fileRootPath += "_" + date.getTime();
        }
        fileRootPath += File.separator;
        // fileRootPath += zkTableInfo.getTableName() + File.separator;
        return fileRootPath;
    }

    public static File genCodeFile(String filePath, String fileName, String content, ZKModule zkModule,
            ZKTableInfo zkTableInfo)
            throws Exception {
        try {
            /*** 制作替换内容参数 ***/
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("zkModule", zkModule);
            paramsMap.put("zkTableInfo", zkTableInfo);
            /*** 生成文件内容 ***/
//          System.out.println("[^_^:201705271521-001] " + StringUtils.trimToEmpty(templateInfo.getContent()));
            content = ZKFreeMarkersUtils.renderString(StringUtils.trimToEmpty(content), paramsMap);
            /*** 生成文件【写文件】 ***/
            File f = ZKFileUtils.createFile(filePath, fileName, true);
            ZKFileUtils.writeFile(content, f, false);

            return f;
        }
        catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
