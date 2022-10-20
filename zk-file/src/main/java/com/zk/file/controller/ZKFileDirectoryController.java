/**
 * 
 */
package com.zk.file.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.web.ZKMsgRes;

import com.zk.file.entity.ZKFileDirectory;
import com.zk.file.service.ZKFileDirectoryService;       

/**
 * ZKFileDirectoryController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.file}/${zk.file.version}/fileDirectory")
public class ZKFileDirectoryController extends ZKBaseController {

	@Autowired
	private ZKFileDirectoryService fileDirectoryService;
	
	// 编辑
	@RequestMapping(value="fileDirectory", method = RequestMethod.POST)
	public ZKMsgRes fileDirectoryPost(@RequestBody ZKFileDirectory fileDirectory){
		this.fileDirectoryService.save(fileDirectory);
        return ZKMsgRes.asOk(fileDirectory);
	}
	
	// 查询详情
	@RequestMapping(value="fileDirectory", method = RequestMethod.GET)
	public ZKMsgRes fileDirectoryGet(@RequestParam("pkId") String pkId){
		ZKFileDirectory fileDirectory = this.fileDirectoryService.getDetail(new ZKFileDirectory(pkId));
        return ZKMsgRes.asOk(fileDirectory);
	}
	
	// 分页 树形查询 
	@RequestMapping(value="fileDirectorysTree", method = RequestMethod.GET)
	public ZKMsgRes fileDirectorysTree(ZKFileDirectory fileDirectory, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKFileDirectory> resPage = ZKPage.asPage(hReq);
        resPage = this.fileDirectoryService.findTree(resPage, fileDirectory);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 分页 列表查询 
	@RequestMapping(value="fileDirectorys", method = RequestMethod.GET)
	public ZKMsgRes fileDirectorys(ZKFileDirectory fileDirectory, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKFileDirectory> resPage = ZKPage.asPage(hReq);
        resPage = this.fileDirectoryService.findPage(resPage, fileDirectory);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
	@RequestMapping(value="fileDirectory", method = RequestMethod.DELETE)
	public ZKMsgRes fileDirectoryDel(@RequestParam("pkId[]") String[] pkIds){
		int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.fileDirectoryService.del(new ZKFileDirectory(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

}