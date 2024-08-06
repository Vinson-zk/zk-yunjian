/**
 * 
 */
package com.zk.sys.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.service.ZKSysAuthRankService;
import com.zk.sys.org.entity.ZKSysOrgRank;
import com.zk.sys.org.service.ZKSysOrgRankService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

    // 给职级分配 权限
    @RequestMapping(value = "grantAuths/{rankId}", method = RequestMethod.POST)
    public ZKMsgRes grantAuths(@PathVariable(value = "rankId") String rankId,
            @RequestBody List<ZKSysAuthDefined> allotAuths, HttpServletRequest hReq) {
        ZKSysOrgRank rank = sysOrgRankService.get(rankId);
        return ZKMsgRes.asOk(null, this.sysAuthRankService.allotAuthToRank(rank, allotAuths));
    }

    // 查询给指定职级授权时，公司可分配的权限以及职级已拥有的权限
    @RequestMapping(value = "findAllotAuthPage", method = RequestMethod.GET)
    public ZKMsgRes findAllotAuthPageGet(@RequestParam("rankId") String rankId,
            @RequestParam(value = "searchValue", required = false) String searchValue, HttpServletRequest hReq,
            HttpServletResponse hRes) {
        ZKPage<ZKSysAuthDefined> resPage = ZKPage.asPage(hReq);
        ZKSysOrgRank rank = this.sysOrgRankService.get(rankId);
        List<ZKSysAuthDefined> resList = this.sysAuthRankService.findAllotAuthByRank(rank, searchValue, resPage);
        resPage.setResult(resList);
        return ZKMsgRes.asOk(resPage);
    }

}