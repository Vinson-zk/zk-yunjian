/**
 * 
 */
package com.zk.sys.settings.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.sys.settings.entity.ZKSysSetCollection;
import com.zk.sys.settings.service.ZKSysSetCollectionService;       

/**
 * ZKSysSetCollectionController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/settings/sysSetCollection")
public class ZKSysSetCollectionController extends ZKBaseController {

	@Autowired
	private ZKSysSetCollectionService sysSetCollectionService;
	
	// 编辑
	@RequestMapping(value="sysSetCollection", method = RequestMethod.POST)
	public ZKMsgRes sysSetCollectionPost(@RequestBody ZKSysSetCollection sysSetCollection){
		this.sysSetCollectionService.save(sysSetCollection);
        return ZKMsgRes.asOk(null, sysSetCollection);
	}
	
	// 查询详情
	@RequestMapping(value="sysSetCollection", method = RequestMethod.GET)
	public ZKMsgRes sysSetCollectionGet(@RequestParam("pkId") String pkId){
		ZKSysSetCollection sysSetCollection = this.sysSetCollectionService.get(new ZKSysSetCollection(pkId));
        return ZKMsgRes.asOk(null, sysSetCollection);
	}
	
	// 分页查询 
	@RequestMapping(value="sysSetCollectionsPage", method = RequestMethod.GET)
	public ZKMsgRes sysSetCollectionsPageGet(ZKSysSetCollection sysSetCollection, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKSysSetCollection> resPage = ZKPage.asPage(hReq);
        resPage = this.sysSetCollectionService.findPage(resPage, sysSetCollection);
        return ZKMsgRes.asOk(null, resPage);
	}
	
	// 批量删除
	@RequestMapping(value="sysSetCollection", method = RequestMethod.DELETE)
	public ZKMsgRes sysSetCollectionDel(@RequestParam("pkId[]") String[] pkIds){
		int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.sysSetCollectionService.del(new ZKSysSetCollection(pkId));
            }
        }
        return ZKMsgRes.asOk(null, count);
	}

}