/** 
* Copyright (c) 2012-2023 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKFileUploadApi.java 
* @author Vinson 
* @Package com.zk.framework.file.api 
* @Description: TODO(simple description this file what to do. ) 
* @date Dec 27, 2023 4:42:49 PM 
* @version V1.0 
*/
package com.zk.framework.file.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.zk.core.commons.ZKMsgRes;
import com.zk.framework.common.ZKApiConstants;

import feign.Response;

/** 
* @ClassName: ZKFileUploadApi 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@FeignClient(name = ZKApiConstants.YunJian_App.file.name, contextId = "com.zk.framework.file.api.ZKFileUploadApi")
public interface ZKFileUploadApi {


    /**
     * 
     *
     * @Title: uploadMultipart
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 23, 2024 6:01:30 PM
     * @param parentCode
     *            父目录代码；文件不能做父节点；即 c_type = 0 的数据，不能做为父节点；
     * @param saveGroupCode
     *            文件分组代码，全表唯一，自动生成，UUID；以便附件分组
     * @param name
     *            文件名称
     * @param code
     *            文件代码；公司下唯一;
     * @param status
     *            状态：0-上传、1-正常、2-失效[上传后在指定时间来，没修改为正常时，会自动失效，会删除实际文件]、3-禁用[不能下载，但能查看]
     * @param securityType
     *            文件权限类型：0-私有[需要有身份才获取]，1-开放[可以通过开放的接口获取]
     * @param actionScope
     *            文件作用域：0-普通；1-个人；可以扩展其他作用域，作用域尽量广义一点
     * @param sort
     *            排序号
     * @param mfs
     *            附件列表
     * @return ZKMsgRes
     */
    @RequestMapping(method = RequestMethod.POST, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE }, path = ZKApiConstants.YunJian_App.file.apiPrefix
                    + "/fileInfo/f/upload")
    ZKMsgRes uploadMultipart(@RequestParam(name = "parentCode", required = false) String parentCode,
            @RequestParam(name = "saveGroupCode", required = false) String saveGroupCode,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "status", required = false, defaultValue = "0") int status,
            @RequestParam(name = "securityType", required = false, defaultValue = "0") int securityType,
            @RequestParam(name = "actionScope", required = false, defaultValue = "0") int actionScope,
            @RequestParam(name = "sort", required = false) Integer sort,
            @RequestPart(value = "mfs") List<MultipartFile> mfs);

    @RequestMapping(method = RequestMethod.POST, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE }, path = ZKApiConstants.YunJian_App.file.apiPrefix
                    + "/fileInfo/n/f/upload")
    ZKMsgRes uploadMultipartN(@RequestParam(name = "companyCode", required = true) String companyCode,
            @RequestParam(name = "parentCode", required = false) String parentCode,
            @RequestParam(name = "saveGroupCode", required = false) String saveGroupCode,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "status", required = false, defaultValue = "0") int status,
            @RequestParam(name = "actionScope", required = false, defaultValue = "0") int actionScope,
            @RequestParam(name = "sort", required = false) Integer sort,
            @RequestPart(value = "mfs") List<MultipartFile> mfs);

    @RequestMapping(method = RequestMethod.POST, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE }, path = ZKApiConstants.YunJian_App.file.apiPrefix
                    + "/fileInfo/n/f/uploadTk/{tkid}")
    ZKMsgRes uploadMultipartTK(@PathVariable(name = "tkid") String tkid,
            @RequestParam(name = "parentCode", required = false) String parentCode,
            @RequestParam(name = "saveGroupCode", required = false) String saveGroupCode,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "status", required = false, defaultValue = "0") int status,
            @RequestParam(name = "securityType", required = false, defaultValue = "0") int securityType,
            @RequestParam(name = "actionScope", required = false, defaultValue = "0") int actionScope,
            @RequestParam(name = "sort", required = false) Integer sort,
            @RequestPart(value = "mfs") List<MultipartFile> mfs);

    // 取文件 下载文件 建议通过转发到文件服务器取文件 -----------------------------------------------------------------
    @RequestMapping(method = RequestMethod.GET, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE }, path = ZKApiConstants.YunJian_App.file.apiPrefix
                    + "/fileInfo/f/getFile")
    Response getFile(@RequestParam(value = "pkId", required = false) String pkId,
            @RequestParam(value = "saveUuid", required = false) String saveUuid,
            @RequestParam(value = "isDownload", required = false, defaultValue = "false") boolean isDownload,
            @RequestParam(value = "securityType", required = false, defaultValue = "0") int securityType);

    @RequestMapping(method = RequestMethod.GET, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE }, path = ZKApiConstants.YunJian_App.file.apiPrefix
                    + "/fileInfo/n/f/getFile")
    Response getFileN(@RequestParam(value = "pkId", required = false) String pkId,
            @RequestParam(value = "saveUuid", required = false) String saveUuid,
            @RequestParam(value = "isDownload", required = false, defaultValue = "false") boolean isDownload);

    @RequestMapping(method = RequestMethod.GET, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE }, path = ZKApiConstants.YunJian_App.file.apiPrefix
                    + "/fileInfo/n/f/getFileTk/{tkid}")
    Response getFileTk(@PathVariable(name = "tkid") String tkid,
            @RequestParam(value = "pkId", required = false) String pkId,
            @RequestParam(value = "saveUuid", required = false) String saveUuid,
            @RequestParam(value = "isDownload", required = false, defaultValue = "false") boolean isDownload,
            @RequestParam(value = "securityType", required = false, defaultValue = "0") int securityType);


}
