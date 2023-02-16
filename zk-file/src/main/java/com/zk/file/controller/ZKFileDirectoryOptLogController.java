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
import com.zk.core.commons.ZKMsgRes;
import com.zk.file.entity.ZKFileDirectoryOptLog;
import com.zk.file.service.ZKFileDirectoryOptLogService;       

/**
 * ZKFileDirectoryOptLogController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.file}/${zk.file.version}/fileDirectoryOptLog")
public class ZKFileDirectoryOptLogController extends ZKBaseController {

	@Autowired
	private ZKFileDirectoryOptLogService fileDirectoryOptLogService;
	
	// 查询详情
	@RequestMapping(value="fileDirectoryOptLog", method = RequestMethod.GET)
	public ZKMsgRes fileDirectoryOptLogGet(@RequestParam("pkId") String pkId){
		ZKFileDirectoryOptLog fileDirectoryOptLog = this.fileDirectoryOptLogService.get(new ZKFileDirectoryOptLog(pkId));
        return ZKMsgRes.asOk(fileDirectoryOptLog);
	}
	
	// 分页查询 
	@RequestMapping(value="fileDirectoryOptLogsPage", method = RequestMethod.GET)
	public ZKMsgRes fileDirectoryOptLogsPageGet(ZKFileDirectoryOptLog fileDirectoryOptLog, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKFileDirectoryOptLog> resPage = ZKPage.asPage(hReq);
        resPage = this.fileDirectoryOptLogService.findPage(resPage, fileDirectoryOptLog);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
	@RequestMapping(value="fileDirectoryOptLog", method = RequestMethod.DELETE)
	public ZKMsgRes fileDirectoryOptLogDel(@RequestParam("pkId[]") String[] pkIds){
		int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.fileDirectoryOptLogService.del(new ZKFileDirectoryOptLog(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

}