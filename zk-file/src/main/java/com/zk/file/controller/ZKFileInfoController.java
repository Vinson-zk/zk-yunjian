/**
 * 
 */
package com.zk.file.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.web.ZKMsgRes;
import com.zk.file.entity.ZKFileInfo;
import com.zk.file.service.ZKFileInfoService;       

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
	
	// 查询详情
	@RequestMapping(value="fileInfo", method = RequestMethod.GET)
	public ZKMsgRes fileInfoGet(@RequestParam("pkId") String pkId){
		ZKFileInfo fileInfo = this.fileInfoService.get(new ZKFileInfo(pkId));
        return ZKMsgRes.asOk(fileInfo);
	}
	
	// 分页查询 
	@RequestMapping(value="fileInfosPage", method = RequestMethod.GET)
	public ZKMsgRes fileInfosPageGet(ZKFileInfo fileInfo, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKFileInfo> resPage = ZKPage.asPage(hReq);
        resPage = this.fileInfoService.findPage(resPage, fileInfo);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
	@RequestMapping(value="fileInfo", method = RequestMethod.DELETE)
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