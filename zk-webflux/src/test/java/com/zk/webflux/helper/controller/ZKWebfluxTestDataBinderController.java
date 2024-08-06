/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKWebfluxTestDataBinderController.java 
* @author Vinson 
* @Package com.zk.webflux.helper.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 25, 2024 12:15:36 AM 
* @version V1.0 
*/
package com.zk.webflux.helper.controller;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.utils.ZKHtmlUtils;
import com.zk.webflux.helper.entity.ZKWebfluxTestEntity;

import reactor.core.publisher.Mono;

/** 
* @ClassName: ZKWebfluxTestDataBinderController 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RestController
@RequestMapping("${zk.path.admin:zk}/${zk.path.webflux}/dataBinder")
public class ZKWebfluxTestDataBinderController {

    /**
     * @RequestParam name 与 value 只能用一个 不设置 @RequestParam 或 不设置@RequestParam的name与value 默认以参数名做为绑定名，去绑定对应类型的参数； 当绑定名找不到参数，也没找到相关类型的参数 时；出现的异常：Required [String
     *               类型] parameter '[notPresentParam 绑定名]' is not present 如果绑定名的类型为 map 会把所有参数的map绑定此绑定名上。
     */

    // 路径参数名 1
    public static final String pn_pathVariable1 = "pVariable1";

    // 路径参数名 2
    public static final String pn_pathVariable2 = "pVariable2";

    // @RequestParam 字符 参数名
    public static final String pn_str = "pStr";

    // @RequestParam 字符 Array 或 List 参数名
    public static final String pn_strs = "pStrs";

    // 所有参数 Map 参数名
    public static final String pn_allParamMap = "pAllParamMap";

    // 字符 List 参数名
    public static final String pn_strList = "pStrList";

    // @RequestParam 实体 参数名
    public static final String pn_entity = "pEntity";

    // @RequestParam 实体 List 参数名
    public static final String pn_entityList = "pEntityList";

    // @RequestParam 实体 Map 参数名
    public static final String pn_entityMap = "pEntityMap";

    // RequestBody 参数
    public static final String pn_bodyParam = "pBodyParam";

    public static boolean initBinderFlagStr = false;

    public static boolean initBinderFlagEntity = false;

    public static boolean initBinderFlagEntityList = false;

    public static boolean initBinderFlagEntityMap = false;

    /**
     * 路径、@RequestParam 参数、 @RequestBody 1、路径参数 1、路径参数 1 2、路径参数 2 2、@RequestParam 参数 3、所有参数 Map 4、字符 5、字符 Array 6、字符 List 7、Map 8、实体 9、实体 List 3、@RequestBody
     * 10、RequestBody
     * 
     * @RequestParam 说明： name 与 value 只能用一个 不设置 @RequestParam 默认以参数名做为绑定名，去绑定对应类型的参数； 不设置 @RequestParam的name与value 如果参数类型不为 Map, 默认以参数名做为绑定名，去绑定对应类型的参数；
     *               不设置 @RequestParam的name与value 如果参数类型为 Map, 接收所有的请求参数，但当参数是数组时，只会有一个值；
     * 
     *               当绑定名找不到参数，也没找到相关类型的参数 时；出现的异常：Required [String 类型] parameter '[notPresentParam 绑定名]' is not present
     * 
     *               键值对 请求，能解析一级键值的基本数据类型； 如果键是多级的就解析不了，比如 map、指定了参数名[即指定了键]的自定义实体、自定义实体的 Array 与 List
     * 
     * @return
     */
    @RequestMapping("hybridParam/{" + pn_pathVariable1 + "}/{" + pn_pathVariable2 + "}")
    public Mono<ZKMsgRes> hybridParam( //
            @PathVariable(pn_pathVariable1) String variable1,  // 
            @PathVariable(name = pn_pathVariable2) String variable2, //
            @RequestParam Map<String, ?> allParamMap, // 类型为 Map<String, ?>，可不设置@RequestParam的name与value；
            @RequestParam(pn_str) String pStr, // 必须设置 @RequestParam的name与value
            @RequestParam(pn_strs) String[] pStrs, // 必须设置 @RequestParam的name与value
            @RequestParam(pn_strs) List<String> strList,
            @RequestParam(name = pn_entity, required = false) ZKWebfluxTestEntity entity, // 默认情况下是解析不到的；
            @RequestParam(value = pn_entityList, required = false) List<ZKWebfluxTestEntity> entityList, // 默认情况下是解析不到的；
            @RequestParam(name = pn_entityMap, required = false) Map<String, ZKWebfluxTestEntity> entityMap, // 默认情况下是解析不到的；
////            @RequestBody Map<String, ?> bodyParam,
            @RequestBody(required = false) ZKWebfluxTestEntity bodyParam,
            // @RequestBody 只有一个；以上两者任选期一
            ServerHttpRequest req) {
        pStr = ZKHtmlUtils.urlDecode(pStr);

        Map<String, Object> resMap = new HashMap<>();

        System.out.println("[^_^:20190319-1422-001] ==============================" + req.getMethod());
        System.out.println("[^_^:20190319-1422-001] hybridParam ");

        resMap.put(pn_pathVariable1, variable1);
        System.out.println("[^_^:20190319-1422-001] 1 variable1:" + variable1);
        resMap.put(pn_pathVariable2, variable2);
        System.out.println("[^_^:20190319-1422-001] 2 variable2:" + variable2);
        resMap.put(pn_allParamMap, allParamMap);
        System.out.println("[^_^:20190319-1422-001] 3 map:" + ZKJsonUtils.toJsonStr(allParamMap));
        resMap.put(pn_str, pStr);
        System.out.println("[^_^:20190319-1422-001] 4 pStr:" + pStr);
        resMap.put(pn_strs, pStrs);
        System.out.println("[^_^:20190319-1422-001] 5 strArray:" + ZKJsonUtils.toJsonStr(pStrs));
        resMap.put(pn_strList, strList);
        System.out.println("[^_^:20190319-1422-001] 6 strList:" + ZKJsonUtils.toJsonStr(strList));
        resMap.put(pn_entity, entity);
        System.out.println("[^_^:20190319-1422-001] 7 pEntity:" + ZKJsonUtils.toJsonStr(entity));
        System.out.println("[^_^:20190319-1422-001] 7.1 pEntity.list.0:" + entity.getList().get(0).getName());
        System.out.println("[^_^:20190319-1422-001] 7.2 pEntity.map.key1:" + entity.getMap().get("key1").getName());
        resMap.put(pn_entityMap, entityMap);
        System.out.println("[^_^:20190319-1422-001] 8 pMap:" + ZKJsonUtils.toJsonStr(entityMap));
        resMap.put(pn_entityList, entityList);
        System.out.println("[^_^:20190319-1422-001] 9 pEntityList:" + ZKJsonUtils.toJsonStr(entityList));
        resMap.put(pn_bodyParam, bodyParam);
        if (!"GET".equals(req.getMethod().toString())) {
            System.out.println("[^_^:20190319-1422-001] 10 bodyParam:" + ZKJsonUtils.toJsonStr(bodyParam));
            System.out.println("[^_^:20190319-1422-001] 10.1 bodyParam.list.0:" + bodyParam.getList().get(0).getName());
            System.out.println(
                    "[^_^:20190319-1422-001] 10.2 bodyParam.map.key1:" + bodyParam.getMap().get("key1").getName());
        }
        System.out.println("[^_^:20190319-1422-001] ==============================");

        return Mono.just(ZKMsgRes.asOk(resMap));

    }

    /**
     * 初始化数据绑定； 1、按数据类型绑定； 2、按参数名绑定；
     * 
     * 一般使用场景 1. 将所有传递进来的String进行HTML编码，防止XSS攻击 2. 将字段中Date类型转换为String类型
     * 
     * @InitBinder() 中间的value，用于指定命令/表单属性或请求参数的名字，符合该名字的将使用此处的DataBinder， 如我们的@ModelAttribute("user1") User user1
     *               将使用@InitBinder("user1")指定的DataBinder绑定；如果不指定value值，那么所有的都将使用。
     * 
     */
    @InitBinder
    protected void webDataBinder(WebDataBinder webDataBinder) {
        System.out.println("[^_^:20190319-1748-001] 进行数据绑定 webDataBinder：" + webDataBinder.getObjectName());
        if (pn_entity.equals(webDataBinder.getObjectName())) {
            webDataBinder.registerCustomEditor(ZKWebfluxTestEntity.class, new PropertyEditorSupport() {
                @Override
                public void setAsText(String text) {
                    initBinderFlagEntity = true;
                    if (ZKStringUtils.isEmpty(text)) {
                        setValue(null);
                    }
                    else {
                        text = ZKHtmlUtils.urlDecode(text);
                        ZKWebfluxTestEntity myEntity = ZKJsonUtils.parseObject(text, ZKWebfluxTestEntity.class);
                        setValue(myEntity);
                    }
                }

//                        @Override
//                        public String getAsText() {
//                            Object value = getValue();
//                            return value != null ? value.toString() : null;
//                        }
            });
        }
        else if (pn_entityList.equals(webDataBinder.getObjectName())) {
            webDataBinder.registerCustomEditor(List.class, new PropertyEditorSupport() {
                @SuppressWarnings("unchecked")
                @Override
                public void setAsText(String text) {
                    initBinderFlagEntityList = true;
                    if (ZKStringUtils.isEmpty(text)) {
                        setValue(null);
                    }
                    else {
                        text = ZKHtmlUtils.urlDecode(text);
                        List<Object> ls = ZKJsonUtils.parseObject(text, List.class);
                        List<ZKWebfluxTestEntity> myEntityList = new ArrayList<ZKWebfluxTestEntity>();
                        for (Object o : ls) {
                            myEntityList.add(ZKJsonUtils.parseObject(ZKJsonUtils.toJsonStr(o),
                                    ZKWebfluxTestEntity.class));
                        }

                        setValue(myEntityList);
                    }
                }
            });
        }
        else if (pn_entityMap.equals(webDataBinder.getObjectName())) {
            webDataBinder.registerCustomEditor(Map.class, new PropertyEditorSupport() {
                @SuppressWarnings("unchecked")
                @Override
                public void setAsText(String text) {
                    initBinderFlagEntityMap = true;
                    if (ZKStringUtils.isEmpty(text)) {
                        setValue(null);
                    }
                    else {
                        text = ZKHtmlUtils.urlDecode(text);
                        Map<String, Object> myEntityMap = ZKJsonUtils.parseObject(text, Map.class);
                        for (String key : myEntityMap.keySet()) {
                            myEntityMap.put(key, ZKJsonUtils.parseObject(
                                    ZKJsonUtils.toJsonStr(myEntityMap.get(key)), ZKWebfluxTestEntity.class));
                        }
                        setValue(myEntityMap);
                    }
                }
            });
        }
        else {
            webDataBinder.registerCustomEditor(String.class, new PropertyEditorSupport() {
                @Override
                public void setAsText(String text) {
                    initBinderFlagStr = true;
                    setValue(text);
                }

                @Override
                public String getAsText() {
                    Object value = getValue();
                    return value != null ? value.toString() : null;
                }
            });
        }

    }

}
