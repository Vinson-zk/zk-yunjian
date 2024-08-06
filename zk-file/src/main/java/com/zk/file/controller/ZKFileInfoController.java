/**
 * 
 */
package com.zk.file.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.file.entity.ZKFileInfo;
import com.zk.file.entity.ZKFileInfo.ValueKey;
import com.zk.file.service.ZKFileInfoService;
import com.zk.security.utils.ZKSecSecurityUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ZKFileInfoController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.file}/${zk.file.version}/fileInfo")
public class ZKFileInfoController extends ZKBaseController {

	@Autowired
	private ZKFileInfoService fileInfoService;

    // 文件或目录记录处理 ================================================================================================
    // 文件或目录记录处理.目录 -----------------------------------------------------------------------------------
    // 目录 编辑
    @RequestMapping(value = "d/dirInfo", method = RequestMethod.POST)
    public ZKMsgRes fileInfoPost(@RequestBody ZKFileInfo fileInfo) {
        fileInfo.setGroupCode(ZKSecSecurityUtils.getGroupCode());
        fileInfo.setCompanyCode(ZKSecSecurityUtils.getCompanyCode());
        fileInfo.setType(ValueKey.Type.document);
        this.fileInfoService.save(fileInfo);
        return ZKMsgRes.asOk(null, fileInfo);
    }

    // 文件或目录记录处理.文件 -----------------------------------------------------------------------------------

    // 文件或目录记录处理.能用 -----------------------------------------------------------------------------------
    // 分页 列表查询
    @RequestMapping(value = "page", method = RequestMethod.GET)
    public ZKMsgRes fileInfosPageGet(ZKFileInfo fileInfo, HttpServletRequest hReq, HttpServletResponse hRes) {
        fileInfo.setGroupCode(ZKSecSecurityUtils.getGroupCode());
        fileInfo.setCompanyCode(ZKSecSecurityUtils.getCompanyCode());
        ZKPage<ZKFileInfo> resPage = ZKPage.asPage(hReq);
        resPage = this.fileInfoService.findPage(resPage, fileInfo);
        return ZKMsgRes.asOk(null, resPage);
    }

    // 分页 树形查询
    @RequestMapping(value = "pageTree", method = RequestMethod.GET)
    public ZKMsgRes fileInfosPageTree(ZKFileInfo fileInfo, HttpServletRequest hReq, HttpServletResponse hRes) {
        fileInfo.setGroupCode(ZKSecSecurityUtils.getGroupCode());
        fileInfo.setCompanyCode(ZKSecSecurityUtils.getCompanyCode());
        ZKPage<ZKFileInfo> resPage = ZKPage.asPage(hReq);
        resPage = this.fileInfoService.findTree(resPage, fileInfo);
        return ZKMsgRes.asOk(null, resPage);
    }

    /**
     *
     * @Title: getFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 26, 2023 1:59:14 PM
     * @param pkId
     * @param isDetail
     *            是否取详情，true-取详情，包含所有父节点
     * @param rep
     * @return
     * @return void
     */
    @RequestMapping(value = "fileInfo", method = RequestMethod.GET)
    public ZKMsgRes fileInfoGet(@RequestParam("pkId") String pkId,
            @RequestParam(value = "isDetail", required = false, defaultValue = "false") boolean isDetail) {
        if (isDetail) {
            return ZKMsgRes.asOk(null, this.fileInfoService.getDetail(new ZKFileInfo(pkId)));
        }
        else {
            return ZKMsgRes.asOk(null, this.fileInfoService.get(new ZKFileInfo(pkId)));
        }
    }

    // 批量删除
    @RequestMapping(value = "fileInfo", method = RequestMethod.DELETE)
    public ZKMsgRes fileInfoDel(@RequestParam("pkId[]") String[] pkIds) {
        int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.fileInfoService.del(new ZKFileInfo(pkId));
            }
        }
        return ZKMsgRes.asOk(null, count);
    }

    /********************************************************/
    /** 文件上传 - 需要用户身份 ****/
    /********************************************************/
//    protected static interface ParamName {
//        public static final String pFileName = "fileName";
//
//        public static final String pParentId = "parentId";
//    }
//    /***
//     * 二进制流上传，不需要 MultipartResolver 适配器
//     * 
//     * @throws FileUploadException
//     * @throws IOException
//     ***/
//    @RequestMapping(path = "stream")
//    public ZKMsgRes uploadStream(HttpServletRequest req)
//            throws FileUploadException, IOException {
//
//        String fileName = req.getHeader("fileName");
//        String fileName = req.getHeader("fileName");
//        if (StringUtils.isEmpty(fileName)) {
//            fileName = UUID.randomUUID().toString();
//        }
//        File file = FileUtils.createFile(path_uploads, fileName, false);
//        FileUtils.writeFile(req.getInputStream(), file, false);
//    } 

    /**
     * MultipartFile multipart File 单个与多个同时上传，需要 MultipartResolver 适配器
     *
     * @Title: uploadMultipart
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 29, 2023 9:35:58 AM
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
     * @param mfs
     * @return
     * @throws IOException
     * @return ZKMsgRes
     */
    @RequestMapping(path = "f/upload", method = RequestMethod.POST)
    public ZKMsgRes uploadMultipart(
//            @RequestParam(name = "companyCode", required = true) String companyCode,
            @RequestParam(name = "parentCode", required = false) String parentCode,
            @RequestParam(name = "saveGroupCode", required = false) String saveGroupCode,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "status", required = false, defaultValue = "0") int status,
            @RequestParam(name = "securityType", required = false, defaultValue = "1") int securityType,
            @RequestParam(name = "actionScope", required = false, defaultValue = "0") int actionScope,
            @RequestParam(name = "sort", required = false) Integer sort,
            @RequestParam(value = "mfs") List<MultipartFile> mfs) throws IOException {
        String companyCode = ZKSecSecurityUtils.getCompanyCode();
        List<ZKFileInfo> res = this.fileInfoService.uploadFileBatch(companyCode, parentCode, saveGroupCode, name, code,
                status, securityType, actionScope, sort, mfs.toArray(new MultipartFile[mfs.size()]));
        return ZKMsgRes.asOk(null, res);
    }

    /**
     * 下载或查看文件详情(获取文件的输出流);
     *
     * @Title: getFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 25, 2023 5:44:57 PM
     * @param pkId
     * @param isDownload
     * @param res
     * @return void
     */
    @RequestMapping(value = "f/getFile", method = RequestMethod.GET) //
    public void getFile(@RequestParam("pkId") String pkId,
            @RequestParam(value = "isDownload", required = false, defaultValue = "false") boolean isDownload,
            HttpServletResponse res) {
        try {
            this.fileInfoService.getFile(isDownload, pkId, res, null, ZKFileInfo.ValueKey.SecurityType.limit);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /********************************************************/
    /** 文件上传 - 不需要用户身份 ****/
    /********************************************************/
    @RequestMapping(path = "n/f/upload", method = RequestMethod.POST)
    public ZKMsgRes uploadMultipartN(@RequestParam(name = "companyCode", required = true) String companyCode,
            @RequestParam(name = "parentCode", required = false) String parentCode,
            @RequestParam(name = "saveGroupCode", required = false) String saveGroupCode,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "status", required = false, defaultValue = "0") int status,
            @RequestParam(name = "securityType", required = false, defaultValue = "1") int securityType,
            @RequestParam(name = "actionScope", required = false, defaultValue = "0") int actionScope,
            @RequestParam(name = "sort", required = false) Integer sort,
            @RequestParam(value = "mfs") List<MultipartFile> mfs) throws IOException {
        List<ZKFileInfo> res = this.fileInfoService.uploadFileBatch(companyCode, parentCode, saveGroupCode, name, code,
                status, securityType, actionScope, sort, mfs.toArray(new MultipartFile[mfs.size()]));
        return ZKMsgRes.asOk(null, res);
    }
    @RequestMapping(value = "n/f/getFile", method = RequestMethod.GET) //
    public void getFileN(@RequestParam("pkId") String pkId,
            @RequestParam(value = "isDownload", required = false, defaultValue = "false") boolean isDownload,
            HttpServletResponse res) {
        try {
            this.fileInfoService.getFile(isDownload, pkId, res, null, ZKFileInfo.ValueKey.SecurityType.limit);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}


