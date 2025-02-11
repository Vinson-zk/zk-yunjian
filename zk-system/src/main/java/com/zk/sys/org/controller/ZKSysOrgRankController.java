/**
 * 
 */
package com.zk.sys.org.controller;

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
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.sys.org.entity.ZKSysOrgRank;
import com.zk.sys.org.service.ZKSysOrgRankService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;       

/**
 * ZKSysOrgRankController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/${zk.path.sys.org}/sysOrgRank")
public class ZKSysOrgRankController extends ZKBaseController {

	@Autowired
	private ZKSysOrgRankService sysOrgRankService;
	
	// 编辑
    @RequestMapping(value = "sysOrgRank", method = RequestMethod.POST)
    public ZKMsgRes sysOrgRankPost(@RequestBody ZKSysOrgRank sysOrgRank) {
        sysOrgRank.setCompanyId(ZKSecSecurityUtils.getCompanyId());
		this.sysOrgRankService.save(sysOrgRank);
        return ZKMsgRes.asOk(null, sysOrgRank);
	}
	
	// 查询详情
	@RequestMapping(value="sysOrgRank", method = RequestMethod.GET)
	public ZKMsgRes sysOrgRankGet(@RequestParam("pkId") String pkId){
		ZKSysOrgRank sysOrgRank = this.sysOrgRankService.get(new ZKSysOrgRank(pkId));
        return ZKMsgRes.asOk(null, sysOrgRank);
	}
	
	// 分页查询 
    @RequestMapping(value = "sysOrgRanksPage", method = RequestMethod.GET)
    public ZKMsgRes sysOrgRanksPageGet(ZKSysOrgRank sysOrgRank,
            HttpServletRequest hReq, HttpServletResponse hRes) {
        sysOrgRank.setCompanyId(ZKSecSecurityUtils.getCompanyId());
		ZKPage<ZKSysOrgRank> resPage = ZKPage.asPage(hReq);
        resPage = this.sysOrgRankService.findPage(resPage, sysOrgRank);
        return ZKMsgRes.asOk(null, resPage);
	}
	
	// 批量删除
	@RequestMapping(value="sysOrgRank", method = RequestMethod.DELETE)
	@ResponseBody
	public ZKMsgRes sysOrgRankDel(@RequestParam("pkId[]") String[] pkIds){
		 int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.sysOrgRankService.del(new ZKSysOrgRank(pkId));
            }
        }
        return ZKMsgRes.asOk(null, count);
	}

}