/**
 * 
 */
package com.zk.sys.res.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.sys.res.entity.ZKSysResDictType;
import com.zk.sys.res.service.ZKSysResDictTypeService;       

/**
 * ZKSysResDictTypeController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/${zk.path.sys.res}/sysResDictType")
public class ZKSysResDictTypeController extends ZKBaseController {

	@Autowired
	private ZKSysResDictTypeService sysResDictTypeService;
	
	// 编辑
	@RequestMapping(value="sysResDictType", method = RequestMethod.POST)
	public ZKMsgRes sysResDictTypePost(@RequestBody ZKSysResDictType sysResDictType){
		this.sysResDictTypeService.save(sysResDictType);
        return ZKMsgRes.asOk(sysResDictType);
	}
	
	// 查询详情
	@RequestMapping(value="sysResDictType", method = RequestMethod.GET)
	public ZKMsgRes sysResDictTypeGet(@RequestParam("pkId") String pkId){
		ZKSysResDictType sysResDictType = this.sysResDictTypeService.get(new ZKSysResDictType(pkId));
        return ZKMsgRes.asOk(sysResDictType);
	}
	
	// 分页查询 
	@RequestMapping(value="sysResDictTypesPage", method = RequestMethod.GET)
	public ZKMsgRes sysResDictTypesPageGet(ZKSysResDictType sysResDictType, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKSysResDictType> resPage = ZKPage.asPage(hReq);
        resPage = this.sysResDictTypeService.findPage(resPage, sysResDictType);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
	@RequestMapping(value="sysResDictType", method = RequestMethod.DELETE)
	@ResponseBody
	public ZKMsgRes sysResDictTypeDel(@RequestParam("pkId[]") String[] pkIds){
		 int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.sysResDictTypeService.del(new ZKSysResDictType(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

    // 根据字段代码查询
    @RequestMapping(value = "getByTypeCode", method = RequestMethod.GET)
    public ZKMsgRes getByTypeCode(@RequestParam("typeCode") String typeCode) {
        ZKSysResDictType sysResDictType = this.sysResDictTypeService.getByTypeCode(typeCode);
        return ZKMsgRes.asOk(null, sysResDictType);
    }

}