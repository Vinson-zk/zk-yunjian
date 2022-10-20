/**
 * 
 */
package com.zk.sys.auth.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.web.ZKMsgRes;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.service.ZKSysAuthDefinedService;
import com.zk.sys.auth.service.ZKSysAuthRankService;
import com.zk.sys.org.entity.ZKSysOrgRank;
import com.zk.sys.org.service.ZKSysOrgRankService;

/**
 * ZKSysAuthRankController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/auth/sysAuthRank")
public class ZKSysAuthRankController extends ZKBaseController {

	@Autowired
	private ZKSysAuthRankService sysAuthRankService;
	
    @Autowired
    private ZKSysOrgRankService sysOrgRankService;

    @Autowired
    private ZKSysAuthDefinedService sysAuthDefinedService;

    // 给职级分配 权限
    @RequestMapping(value = "setRelationByRank/{rankId}", method = RequestMethod.POST)
    public ZKMsgRes setRelationByUser(@PathVariable(value = "rankId") String rankId,
            @RequestBody List<ZKSysAuthDefined> addAuths, HttpServletRequest hReq) {
        ZKSysOrgRank rank = sysOrgRankService.get(rankId);
        return ZKMsgRes.asOk(this.sysAuthRankService.addRelationByRank(rank, addAuths, null));
    }

    // 查询职级拥有的权限ID
    @RequestMapping(value = "findAuthIdsByRankId", method = RequestMethod.GET)
    public ZKMsgRes findAuthIdsByRankId(@RequestParam("rankId") String rankId) {
        return ZKMsgRes.asOk(this.sysAuthRankService.findAuthIdsByRankId(rankId));
    }

    // 分页查询权限
    @RequestMapping(value = "sysAuthDefinedsPage", method = RequestMethod.GET)
    public ZKMsgRes sysAuthDefinedsPageGet(ZKSysAuthDefined sysAuthDefined, HttpServletRequest hReq,
            HttpServletResponse hRes) {
        ZKPage<ZKSysAuthDefined> resPage = ZKPage.asPage(hReq);
        resPage = this.sysAuthDefinedService.findPage(resPage, sysAuthDefined);
        return ZKMsgRes.asOk(resPage);
    }

}