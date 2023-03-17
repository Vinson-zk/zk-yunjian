/**
 * 
 */
package com.zk.file.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.exception.ZKCodeException;
import com.zk.file.entity.ZKFileInfo;
import com.zk.file.entity.ZKFileInfo.ValueKey;
import com.zk.file.service.ZKFileInfoService;
import com.zk.security.utils.ZKSecSecurityUtils;

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
	
    // 文件 功能接口 -------------------------------------------------

    // 目录 功能接口 -------------------------------------------------
    // 目录 编辑
    @RequestMapping(value = "dirInfo", method = RequestMethod.POST)
	public ZKMsgRes fileInfoPost(@RequestBody ZKFileInfo fileInfo){
        fileInfo.setGroupCode(ZKSecSecurityUtils.getGroupCode());
        fileInfo.setCompanyCode(ZKSecSecurityUtils.getCompanyCode());
        fileInfo.setType(ValueKey.Type.document);
		this.fileInfoService.save(fileInfo);
        return ZKMsgRes.asOk(fileInfo);
	}
	
    // 目录 详情
    @RequestMapping(value = "dirInfo", method = RequestMethod.GET)
	public ZKMsgRes fileInfoGet(@RequestParam("pkId") String pkId){
		ZKFileInfo fileInfo = this.fileInfoService.get(new ZKFileInfo(pkId));
        if (fileInfo != null) {
            if (fileInfo.getType() != null && ValueKey.Type.document != fileInfo.getType().intValue()) {
                log.error("[>_<:20230308-1939-001] zk.file.000005=文件[{}]不是目录", fileInfo.getCode());
                throw ZKCodeException.as("zk.file.000005", null, null, fileInfo.getCode());
            }
        }
        else {
            // 目录不存在
            log.error("[>_<:20230308-1939-002] zk.file.000006=文件或目录[{}]不存在", pkId);
            throw ZKCodeException.as("zk.file.000006");
        }
        return ZKMsgRes.asOk(fileInfo);
	}
	
    // 文件和目录 通用功能接口 -------------------------------------------------
    // 分页 列表查询
    @RequestMapping(value = "page", method = RequestMethod.GET)
	public ZKMsgRes fileInfosPageGet(ZKFileInfo fileInfo, HttpServletRequest hReq, HttpServletResponse hRes){
        fileInfo.setGroupCode(ZKSecSecurityUtils.getGroupCode());
        fileInfo.setCompanyCode(ZKSecSecurityUtils.getCompanyCode());
		ZKPage<ZKFileInfo> resPage = ZKPage.asPage(hReq);
        resPage = this.fileInfoService.findPage(resPage, fileInfo);
        return ZKMsgRes.asOk(resPage);
	}
	
    // 分页 树形查询
    @RequestMapping(value = "pageTree", method = RequestMethod.GET)
    public ZKMsgRes fileInfosPageTree(ZKFileInfo fileInfo, HttpServletRequest hReq, HttpServletResponse hRes) {
        fileInfo.setGroupCode(ZKSecSecurityUtils.getGroupCode());
        fileInfo.setCompanyCode(ZKSecSecurityUtils.getCompanyCode());
        ZKPage<ZKFileInfo> resPage = ZKPage.asPage(hReq);
        resPage = this.fileInfoService.findTree(resPage, fileInfo);
        return ZKMsgRes.asOk(resPage);
    }

	// 批量删除
    @RequestMapping(value = "fileInfo", method = RequestMethod.DELETE)
	public ZKMsgRes fileInfoDel(@RequestParam("pkId[]") String[] pkIds){
		int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.fileInfoService.del(new ZKFileInfo(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

}