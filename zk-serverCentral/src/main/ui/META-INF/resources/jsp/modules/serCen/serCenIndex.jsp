<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/include/tldlib.jsp" %>

<!DOCTYPE html>
<html>

<head>
	<!-- <title>放在引入 head.jsp 前面，可以重新定义 </title> -->
	<%@include file="/jsp/include/head.jsp" %>

	<script type="text/javascript">

        /*** 处理 hash 值*/
        var currentHash = "";
        function f_setLocationHash(hash, params){
            if(hash === null || hash === undefined || hash === ''){
                return false;
            }
            // hash = hash + "?" + params;
            if($zk.getTopWindow().location == location){
                currentHash = hash;
                location.hash = hash;
            }else{
                if(typeof(parent.f_setLocationHash) == 'function'){
                    parent.f_setLocationHash(hash, 1);
                }
            }
            // console.log("[^_^:20200308-1231-001] home ", location);
        }

		var leftMenuData = [
			// iconType：0-css class；1-自定义图片； 
			{code:"serCen", id:"1", url:'', name:{'zh_CN':'服务管理', 'en_US':'Server Manage'}, iconType:0, icon:'icon-layers', child:[
				// {code:"serCen-serInfo", id:"1-1", url:'/${_adminPath}/${_modulePath}/info/serCenInfo', name:{'zh_CN':'服务信息', 'en_US':'Server Info'}, iconType:0, icon:'icon-trophy', isClose:false, child:[]},
                {code:"serCen-serInfo", id:"1-1", url:'/${_adminPath}/${_modulePath}/zkEureka', name:{'zh_CN':'服务动态', 'en_US':'Server Real Info'}, iconType:0, icon:'icon-event', isClose:true, child:[]},
				{code:"serCen-lastn", id:"1-2", url:'/${_adminPath}/${_modulePath}/zkEureka/lastn', name:{'zh_CN':'服务信息', 'en_US':'LAST Server Info'}, iconType:0, icon:'icon-eyeglass'}
			]},
			{code:"serCen-cerIndex", id:"2", url:'/${_adminPath}/${_modulePath}/cer/view/index', name:{'zh_CN':'证书管理', 'en_US':'Certificate'}, iconType:1, icon:'zk/images/menu/certificate.png'},
			{code:"c3", id:"3", url:'/${_adminPath}/${_modulePath}/test', name:{'zh_CN':'Test', 'en_US':'Test'}, iconType:0, icon:'icon-fire', isClose:false, child:[]},
		];

        /**
         * 制作一个 服务中心的 功能映射；供前端 tab 添加使用；
         */
        var funMapping  = {
            // code:{id:'', name:'', url:'', icon:'', iconType:''}
            'serCen-cerEdit':{
                'id':'serCen-cerEdit', 
                'code':'serCen-cerIndex/serCen-cerEdit',
                'name':{'zh_CN':'证书编辑', 'en_US':'cer Edit'}, 
                'url':'/${_adminPath}/${_modulePath}/cer/view/edit', 
                'icon':'edit', 
                'iconType':0
            }
        }
        function makeFunByMenus(menus){
            menus.forEach(function(itemData){
                if(itemData.child && itemData.child.length > 0){
                    makeFunByMenus(itemData.child);
                }
                funMapping[itemData.code] = {
                    "id":itemData.id,
                    "name":itemData.name,
                    "code":itemData.code,
                    "url":itemData.url,
                    "params":"",
                    "icon":itemData.icon,
                    "iconType":itemData.iconType
                }
            });
        }
        makeFunByMenus(leftMenuData);

        // 添加一个 table
        function f_addTab(code, isClose, params){
            params = params || "";
            var tabData = funMapping[code];
            tabData.params = params;
            zkSCTab.addTab(tabData, isClose);
        }

        // 关闭一个 table
        function f_closeTab(code){
            var tabData = funMapping[code];
            tabData.params = "";
            zkSCTab.close(tabData.id);
        }
        // 消息处理
        function f_resMsg(resData, type){
             $zk.zkResMsg(resData, type);
        }
		
		var zkSCTab = null;
		$(function(){
			locale = $zk.getLocale();

			// 左边滑块初始化
			$zk(".zk_slide").zkSlide();

			zkSCTab = $zk("#zk-tab").zkTab({
				maxTabCount:6,
				titleRender:function(menuData){
					if(menuData.iconType == 0){
						return '<i class="fa ' + menuData.icon + '"></i><span>' + menuData.name[locale] + '</span>';
					}
					return '<span " >' + menuData.name[locale] + '</span>';
				},
                contentRender: function(tabItemData){
                    var iframe = $('<iframe id="" name="" src="" frameborder="0"></iframe>');
                    iframe.attr("id", tabItemData.id);
                    iframe.attr("name", tabItemData.id);
                    iframe.attr("src", tabItemData.url + "?" + tabItemData.params);
                    return iframe;
                },
				onSelect:function(tabItemData){
                    // console.log("[^_^:20200308-1508-001] onSelect tabItemData:", tabItemData);
                    f_setLocationHash(tabItemData.code);
				}
			});

			var zkMenu = $zk("#zk-menu").zkMenu({
				menus:leftMenuData,
				render:function(menuData, isLeaf){
					var menuA = $('<a href="javascript:" ></a>');
					// $(menuA).attr('data-href', mData.url);
					$(menuA).attr('title', menuData.name['${locale}']);
					// $(menuA).attr('data-code', mData.id);

					if(menuData.iconType == 0){
						menuA.append('<i class="fa fa-fw ' + menuData.icon + '"></i>');
					}else{
						menuA.append('<img src="/${ctxStatic}/' + menuData.icon + '" width="20" height="20" />');
					}
					menuA.append("<span>&nbsp;&nbsp;" + menuData.name[locale] + "</span>");

					return menuA;
				},
				onSelect:function(menuData, isInit){
					// console.log("[^_^:20200308-1349-001] zkMenu onSelect: ", isInit);
                    if(!menuData.child || menuData.child.length < 1){
                        f_addTab(menuData.code, menuData.isClose);
                    }
				},
				onBeforeSelect:function(menuData){
					// console.log("-------------- onBeforeSelect: ", menuData);
					// return true;
				}
			})

			// 取历史 hash
            var historyHash = location.hash;
            // console.log("[^_^:20200308-1353-001] home historyHash: ", historyHash);
            if(!$zk.isEmpty(historyHash)){
                var historyHash = historyHash.substring(1);
                historyHash = historyHash.split("/");
                for(var hCode of historyHash){
                    if(funMapping[hCode] && !zkMenu.select(funMapping[hCode].id, true)){
                        // f_addTab(hCode, true);
                    }
                }
            }else{
                zkMenu.select('1');
            }

    		// 自定义滚去条样式
			$('#zk-slimScroll').slimScroll({
				height: '100%',
				width: '200px',
				color: "#aaa",
				size: "3px"
			});

		});

	</script>

</head>
<body>
<div class="zk_layout zk_layout_row">
	
	<div class="zk_slide zk_min_slide" data-zkIsOpen="true">
		<section id='zk-slimScroll'>
			<div class="zk_slide_header">
				<div id="push-top-menu" class="zk_icon_block zk_push_top">
	    			<small>&nbsp; &nbsp;<i class="fa icon-grid"></i></small> <!-- fa-toggle-down -->
	    		</div>
	            <div id="pull-left-menu" class="zk_icon_block zk_slide_switch">
	                <small>&nbsp; &nbsp;<i class="fa icon-pin"></i></small> <!-- fa-thumb-tack -->
	            </div>
			</div>
			<div class='zk_slide_panel'>
				<div id="zk-menu" class="zk_menu">
						
				</div>
			</div>
		</section>
	</div>
	<div class="zk_content">
		<div id="zk-tab" class="zk_tab_panel">
		</div>
		
	</div>
    <!-- <div style="display:none"></div> -->
</div>
</body>
</html>


