/**
 * 
 */
package com.zk.sys.res.controller;

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
import com.zk.sys.res.entity.ZKSysResDict;
import com.zk.sys.res.service.ZKSysResDictService;

import jakarta.servlet.http.HttpServletRequest;       

/**
 * ZKSysResDictController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/${zk.path.sys.res}/sysResDict")
public class ZKSysResDictController extends ZKBaseController {

	@Autowired
	private ZKSysResDictService sysResDictService;
	
	// 编辑
	@RequestMapping(value="sysResDict", method = RequestMethod.POST)
	public ZKMsgRes sysResDictPost(@RequestBody ZKSysResDict sysResDict){
		this.sysResDictService.save(sysResDict);
        return ZKMsgRes.asOk(sysResDict);
	}
	
	// 查询详情
	@RequestMapping(value="sysResDict", method = RequestMethod.GET)
	public ZKMsgRes sysResDictGet(@RequestParam("pkId") String pkId){
        ZKSysResDict sysResDict = this.sysResDictService.getDetail(new ZKSysResDict(pkId));
        return ZKMsgRes.asOk(sysResDict);
	}
	
	// 批量删除
	@RequestMapping(value="sysResDict", method = RequestMethod.DELETE)
	@ResponseBody
	public ZKMsgRes sysResDictDel(@RequestParam("pkId[]") String[] pkIds){
		 int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.sysResDictService.del(new ZKSysResDict(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

    // 分页树形列表查询
    @RequestMapping(value = "sysResDictsTree", method = RequestMethod.GET)
    public ZKMsgRes sysResDictsTree(ZKSysResDict sysResDict, HttpServletRequest hReq) {

        ZKPage<ZKSysResDict> resPage = ZKPage.asPage(hReq);
        resPage = this.sysResDictService.findTree(resPage, sysResDict);
        return ZKMsgRes.asOk(null, resPage);
    }

    // 分页列表查询
    @RequestMapping(value = "sysResDicts", method = RequestMethod.GET)
    public ZKMsgRes sysResDicts(ZKSysResDict sysResDict, HttpServletRequest hReq) {

        ZKPage<ZKSysResDict> resPage = ZKPage.asPage(hReq);
        resPage = this.sysResDictService.findPage(resPage, sysResDict);
        return ZKMsgRes.asOk(null, resPage);
    }

    @RequestMapping(value = "n/sysResDicts", method = RequestMethod.GET)
    public ZKMsgRes nSysResDicts(ZKSysResDict sysResDict, HttpServletRequest hReq) {

        ZKPage<ZKSysResDict> resPage = ZKPage.asPage(hReq);
        resPage = this.sysResDictService.findPage(resPage, sysResDict);
        return ZKMsgRes.asOk(null, resPage);
    }

}