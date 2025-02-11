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
import com.zk.iot.entity.ZKIotLogEventRecord;
import com.zk.iot.service.ZKIotLogEventRecordService;

/**
 * ZKIotLogEventRecordController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.iot}/${zk.iot.version}/iotLogEventRecord")
public class ZKIotLogEventRecordController extends ZKBaseController {

	@Autowired
	private ZKIotLogEventRecordService iotLogEventRecordService;
	
	// 编辑
	@RequestMapping(value="iotLogEventRecord", method = RequestMethod.POST)
	public ZKMsgRes iotLogEventRecordPost(@RequestBody ZKIotLogEventRecord iotLogEventRecord){
		this.iotLogEventRecordService.save(iotLogEventRecord);
        return ZKMsgRes.asOk(iotLogEventRecord);
	}
	
	// 查询详情
	@RequestMapping(value="iotLogEventRecord", method = RequestMethod.GET)
    public ZKMsgRes iotLogEventRecordGet(@RequestParam("pkId") String pkId) {
		ZKIotLogEventRecord iotLogEventRecord = this.iotLogEventRecordService.get(new ZKIotLogEventRecord(pkId));
        return ZKMsgRes.asOk(iotLogEventRecord);
	}
	
	// 分页查询 
	@RequestMapping(value="iotLogEventRecordsPage", method = RequestMethod.GET)
    public ZKMsgRes iotLogEventRecordsPageGet(ZKIotLogEventRecord iotLogEventRecord,
            ServerWebExchange serverWebExchange) {
        ZKPage<ZKIotLogEventRecord> resPage = ZKPage.asPage(serverWebExchange.getRequest());
        resPage = this.iotLogEventRecordService.findPage(resPage, iotLogEventRecord);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
	@RequestMapping(value="iotLogEventRecord", method = RequestMethod.DELETE)
    public ZKMsgRes iotLogEventRecordDel(@RequestParam("pkId[]") String[] pkIds) {
		int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.iotLogEventRecordService.del(new ZKIotLogEventRecord(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

}