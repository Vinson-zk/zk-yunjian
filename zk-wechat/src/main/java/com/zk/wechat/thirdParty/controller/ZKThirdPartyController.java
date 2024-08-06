/**
 * 
 */
package com.zk.wechat.thirdParty.controller;

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
import com.zk.security.annotation.ZKSecApiCode;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.wechat.thirdParty.entity.ZKThirdParty;
import com.zk.wechat.thirdParty.service.ZKThirdPartyService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ZKThirdPartyController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.wechat}/${zk.wechat.version}/${zk.path.wechat.thirdParty}/thirdParty")
public class ZKThirdPartyController extends ZKBaseController {

	@Autowired
	private ZKThirdPartyService thirdPartyService;
	
	// 编辑
	@RequestMapping(value="thirdParty", method = RequestMethod.POST)
	public ZKMsgRes thirdPartyPost(@RequestBody ZKThirdParty thirdParty){
        thirdParty.setGroupCode(ZKSecSecurityUtils.getGroupCode());
        thirdParty.setCompanyId(ZKSecSecurityUtils.getCompanyId());
        thirdParty.setCompanyCode(ZKSecSecurityUtils.getCompanyCode());
		this.thirdPartyService.save(thirdParty);
        return ZKMsgRes.asOk(thirdParty);
	}
	
	// 查询详情
	@RequestMapping(value="thirdParty", method = RequestMethod.GET)
    @ZKSecApiCode("com_zk_wechat_thirdParty_detail")
	public ZKMsgRes thirdPartyGet(@RequestParam("pkId") String pkId){
		ZKThirdParty thirdParty = this.thirdPartyService.get(new ZKThirdParty(pkId));
        return ZKMsgRes.asOk(thirdParty);
	}
	
	// 分页查询 
	@RequestMapping(value="thirdPartysPage", method = RequestMethod.GET)
	public ZKMsgRes thirdPartysPageGet(ZKThirdParty thirdParty, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKThirdParty> resPage = ZKPage.asPage(hReq);
        resPage = this.thirdPartyService.findPage(resPage, thirdParty);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
	@RequestMapping(value="thirdParty", method = RequestMethod.DELETE)
	@ResponseBody
	public ZKMsgRes thirdPartyDel(@RequestParam("pkId[]") String[] pkIds){
		 int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.thirdPartyService.del(new ZKThirdParty(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

}