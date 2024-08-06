/**
 * 
 */
package com.zk.wechat.platformBusiness.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.wechat.platformBusiness.entity.ZKFuncKeyType;
import com.zk.wechat.platformBusiness.service.ZKFuncKeyTypeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ZKFuncKeyTypeController
 * 
 * @author
 * @version
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.wechat}/${zk.wechat.version}/${zk.path.wechat.platformBusiness}/funcKeyType")
public class ZKFuncKeyTypeController extends ZKBaseController {

    @Autowired
    private ZKFuncKeyTypeService funcKeyTypeService;

    // 编辑
    @RequestMapping(value = "funcKeyType", method = RequestMethod.POST)
    public ZKMsgRes funcKeyTypePost(@RequestBody ZKFuncKeyType funcKeyType) {
        this.funcKeyTypeService.save(funcKeyType);
        return ZKMsgRes.asOk(null, funcKeyType);
    }

    // 查询详情
    @RequestMapping(value = "funcKeyType", method = RequestMethod.GET)
    public ZKMsgRes funcKeyTypeGet(@RequestParam("pkId") String pkId) {
        ZKFuncKeyType funcKeyType = this.funcKeyTypeService.get(new ZKFuncKeyType(pkId));
        return ZKMsgRes.asOk(null, funcKeyType);
    }

    // 分页查询
    @RequestMapping(value = "funcKeyTypesPage", method = RequestMethod.GET)
    public ZKMsgRes funcKeyTypesPageGet(ZKFuncKeyType funcKeyType, HttpServletRequest hReq,
            HttpServletResponse hRes) {
        ZKPage<ZKFuncKeyType> resPage = ZKPage.asPage(hReq);
        resPage = this.funcKeyTypeService.findPage(resPage, funcKeyType);
        return ZKMsgRes.asOk(null, resPage);
    }

    // 批量删除
    @RequestMapping(value = "funcKeyType", method = RequestMethod.DELETE)
    public ZKMsgRes funcKeyTypeDel(@RequestParam("pkId[]") String[] pkIds) {
        int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.funcKeyTypeService.del(new ZKFuncKeyType(pkId));
            }
        }
        return ZKMsgRes.asOk(null, count);
    }

}