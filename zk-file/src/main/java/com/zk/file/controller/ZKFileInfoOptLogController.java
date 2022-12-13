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

import com.zk.file.entity.ZKFileInfoOptLog;
import com.zk.file.service.ZKFileInfoOptLogService;       

/**
 * ZKFileInfoOptLogController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.file}/${zk.file.version}/fileInfoOptLog")
public class ZKFileInfoOptLogController extends ZKBaseController {

	@Autowired
	private ZKFileInfoOptLogService fileInfoOptLogService;
	
	// 编辑
	@RequestMapping(value="fileInfoOptLog", method = RequestMethod.POST)
	public ZKMsgRes fileInfoOptLogPost(@RequestBody ZKFileInfoOptLog fileInfoOptLog){
		this.fileInfoOptLogService.save(fileInfoOptLog);
        return ZKMsgRes.asOk(fileInfoOptLog);
	}
	
	// 查询详情
	@RequestMapping(value="fileInfoOptLog", method = RequestMethod.GET)
	public ZKMsgRes fileInfoOptLogGet(@RequestParam("pkId") String pkId){
		ZKFileInfoOptLog fileInfoOptLog = this.fileInfoOptLogService.get(new ZKFileInfoOptLog(pkId));
        return ZKMsgRes.asOk(fileInfoOptLog);
	}
	
	// 分页查询 
	@RequestMapping(value="fileInfoOptLogsPage", method = RequestMethod.GET)
	public ZKMsgRes fileInfoOptLogsPageGet(ZKFileInfoOptLog fileInfoOptLog, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKFileInfoOptLog> resPage = ZKPage.asPage(hReq);
        resPage = this.fileInfoOptLogService.findPage(resPage, fileInfoOptLog);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
	@RequestMapping(value="fileInfoOptLog", method = RequestMethod.DELETE)
	public ZKMsgRes fileInfoOptLogDel(@RequestParam("pkId[]") String[] pkIds){
		int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.fileInfoOptLogService.del(new ZKFileInfoOptLog(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

}