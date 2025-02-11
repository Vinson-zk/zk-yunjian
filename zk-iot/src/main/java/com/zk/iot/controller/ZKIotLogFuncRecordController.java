/**
 * 
 */
package com.zk.iot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.iot.entity.ZKIotLogFuncRecord;
import com.zk.iot.service.ZKIotLogFuncRecordService;

/**
 * ZKIotLogFuncRecordController
 * 
 * @author
 * @version
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.iot}/${zk.iot.version}/iotLogFuncRecord")
public class ZKIotLogFuncRecordController extends ZKBaseController {

	@Autowired
	private ZKIotLogFuncRecordService iotLogFuncRecordService;
	
	// 编辑
	@RequestMapping(value="iotLogFuncRecord", method = RequestMethod.POST)
	public ZKMsgRes iotLogFuncRecordPost(@RequestBody ZKIotLogFuncRecord iotLogFuncRecord){
		this.iotLogFuncRecordService.save(iotLogFuncRecord);
        return ZKMsgRes.asOk(iotLogFuncRecord);
	}
	
	// 查询详情
	@RequestMapping(value="iotLogFuncRecord", method = RequestMethod.GET)
    public ZKMsgRes iotLogFuncRecordGet(@RequestParam("pkId") String pkId) {
		ZKIotLogFuncRecord iotLogFuncRecord = this.iotLogFuncRecordService.get(new ZKIotLogFuncRecord(pkId));
        return ZKMsgRes.asOk(iotLogFuncRecord);
	}
	
	// 分页查询 
	@RequestMapping(value="iotLogFuncRecordsPage", method = RequestMethod.GET)
    public ZKMsgRes iotLogFuncRecordsPageGet(ZKIotLogFuncRecord iotLogFuncRecord, ServerWebExchange serverWebExchange) {
        ZKPage<ZKIotLogFuncRecord> resPage = ZKPage.asPage(serverWebExchange.getRequest());
        resPage = this.iotLogFuncRecordService.findPage(resPage, iotLogFuncRecord);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
	@RequestMapping(value="iotLogFuncRecord", method = RequestMethod.DELETE)
    public ZKMsgRes iotLogFuncRecordDel(@RequestParam("pkId[]") String[] pkIds) {
		int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.iotLogFuncRecordService.del(new ZKIotLogFuncRecord(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

}