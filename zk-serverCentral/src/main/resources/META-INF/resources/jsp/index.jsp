<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/include/tldlib.jsp" %>

<!DOCTYPE html>
<html>

<head>
	<!-- <title>放在引入 head.jsp 前面，可以重新定义 </title> -->
	<%@include file="/jsp/include/head.jsp" %>

	<script type="text/javascript">
		// console.log("---- f_bodyLoad ..... ");
		$zk.zkLoading(1);

		function f_bodyLoad(){
			// console.log("---- f_bodyLoad");
		}

		var currentHash = "";
		// flag 为 0 时表示当前 window 操作 hash ; 非 0 时，表示子 iframe 操作 hash
		function f_setLocationHash(hash, flag){
	    	if(hash === null || hash === undefined || hash === ''){
	    		return false;
	    	}

	    	if($zk.getTopWindow().location == location){
	    		if(flag === 0){
		    		currentHash = hash;
		    		location.hash = hash;
		    	}else{
		    		location.hash = currentHash + "/" + hash;
		    	}
	    	}else{
	    		if(typeof(parent.f_setLocationHash) == 'function'){
	    			parent.f_setLocationHash(currentHash + "/" + hash, 1);
	    		}
	    	}
	    	// console.log("[^_^:20200307-1932-001] index ", currentHash, hash);
	    }
		
		// 功能菜单
		var navDatas = [
			{id:"1", url:"/${_adminPath}/${_modulePath}/home/serCenIndex", text:{'zh_CN':'服务中心', 'en_US':'Server  Central'}, icon:"icon-settings" }
			, {id:"2", url:"/${_adminPath}/${_modulePath}/home/scGuide", text:{'zh_CN':'指引', 'en_US':'Guide'}, icon:"icon-directions" }
			// , {id:"3", url:"/${_adminPath}/${_modulePath}/home/scGuide", text:{'zh_CN':'指引', 'en_US':'Guide'}, icon:"icon-directions" }
			// , {id:"4", url:"/${_adminPath}/${_modulePath}/home/scGuide", text:{'zh_CN':'指引', 'en_US':'Guide'}, icon:"icon-directions" }
			// , {id:"5", url:"/${_adminPath}/${_modulePath}/home/scGuide", text:{'zh_CN':'指引', 'en_US':'Guide'}, icon:"icon-directions" }
			// , {id:"6", url:"/${_adminPath}/${_modulePath}/home/scGuide", text:{'zh_CN':'指引', 'en_US':'Guide'}, icon:"icon-directions" }
			// , {id:"7", url:"/${_adminPath}/${_modulePath}/home/scGuide", text:{'zh_CN':'指引', 'en_US':'Guide'}, icon:"icon-directions" }
			// , {id:"8", url:"/${_adminPath}/${_modulePath}/home/scGuide", text:{'zh_CN':'指引', 'en_US':'Guide'}, icon:"icon-directions" }
			// , {id:"9", url:"/${_adminPath}/${_modulePath}/home/scGuide", text:{'zh_CN':'指引', 'en_US':'Guide'}, icon:"icon-directions" }
		]; 

		// 初始化函数
        $(function() {
        	locale = $zk.getLocale();
        	// console.log("[^_^:20190916-1104-001] locale: ${locale}" );

   //      	window.addEventListener("hashchange", function(){
			//     console.log("index  ", window.location);
			// }, false);

        	// 全屏 节点处理
			$zk("#fullScreen").zkFullScreen();
			/*** dropdown 节点 open 与 close ***/
			$zk(".zk_dropdown").zkDropdown();

        	var navBar = $zk("#nav-bar").zkNavBar({
        		navDatas:navDatas,
        		render:function(navItemData){
        			var navItemA = $('<a href="javascript:" ></a>');
		            // navItemA.attr("href", navItemData.url);
		            navItemA.attr("title", navItemData.text[locale]);

		            navItemA.attr("data-code", navItemData.id);

		    //         $zk.event.binding(navItemA[0], 'click', function(e){
						// // zkTools.event.cancelPropagation(e);
						// f_updateHash(navItemData.id);
			   //  	}, true);

		            $(navItemA).append('<i class="fa fa-fw ' + navItemData.icon + '"></i>');
					$(navItemA).append('<span>' + navItemData.text[locale] + '</span>');

        			return navItemA;
        		},
        		// onBeforeSelect:function(navItemId){
        		// 	console.log("[^_^:20190919-1811-001] onBeforeSelect: ", navItemId, this);
        		// },
        		onSelect:function(navItemData){
        			// console.log("[^_^:20200308-1318-001] navbar onSelect: ", historyHash);
        			if(currentHash == navItemData.id){
        				// 初始选择，
        				url = navItemData.url;
        				if(!$zk.isEmpty(historyHash)){
        					url = url + "#" + historyHash;
        				}
        				$("#home").attr("src", url);
        			}else{
        				$("#home").attr("src", navItemData.url);
        				f_setLocationHash(navItemData.id, 0);
        			}
        		},
        		// onBeforeRemove:function(navItemId){
        		// 	console.log("[^_^:20190919-1811-003]onBeforeRemove: ", navItemId, this);
        		// },
        		// onRemove:function(navItemId){
        		// 	console.log("[^_^:20190919-1811-004] onRemove: ", navItemId, this);
        		// }
        	});

        	// 取历史 hash
			var historyHash = location.hash;
			// console.log("[^_^:20200308-1203-001] index historyHash: ", historyHash);
        	if(!$zk.isEmpty(historyHash)){
    			var historyHash = historyHash.substring(1);
    			historyHash = historyHash.split("/");
    			currentHash = historyHash[0];
    			historyHash = historyHash.slice(1).join("/");
    			navBar.select(currentHash);
    			// console.log("[^_^:20200308-1203-002] historyHash: ", historyHash);
    		}else{
    			navBar.select(1);
    		}
        });


	</script> 
	
</head>
<body onload="f_bodyLoad()">
<div class="zk-loading zk-global-loading"></div>
<div class="zk_layout">
	<div class="zk_header">
		<div class='zk_logo'>
		  <img src='/${ctxStatic}/zk/images/logo_${zkStr:toUpperCase(zkStr:replaceAll(locale, "-", "_"))}.png'>
		</div>
		<div class='zk_setting_bar zk_pull_right'>
		  <ul class="">
		    <li class="">
		      <a id="fullScreen" class='full_screen' href="javascript:" title="" data-original-title="Full Screen">
		        <i class="fa fa-arrows-alt"></i>
		      </a>
		    </li>
		    <li class="zk_dropdown">
		      <a href="javascript:">
		        <!-- 
		        	<i class="fa icon-globe"></i> 
		        	<i class="fa fa-globe"></i>
		        -->
		        <i class="fa icon-globe"></i>
		      </a>
		      <ul class="zk_dropdown_menu">
		        <li class="mt5"></li>
		        <li><a href="javascript:$zk.zkChangeLanguage('/${_adminPath}/${_modulePath}/locale?locale=', 'zh_CN')">简体中文</a></li>
				<li><a href="javascript:$zk.zkChangeLanguage('/${_adminPath}/${_modulePath}/locale?locale=', 'en_US')">English</a></li>
		        <li class="mt10"></li>
		      </ul>
		    </li>
		    <li class="zk_dropdown zk_user_image">
		      <a href="javascript:" >
		        <img src="/${ctxStatic}/zk/images/user/user.jpg" class="user-image">
				<span class="hidden-xs"><spring:message code='view.msg.index.userName' /></span>
		      </a>
		      <ul class="zk_dropdown_menu">
		      		<li>
						<a href="/${_adminPath}/${_modulePath}/l/logout">
						<i class="fa fa-sign-out"></i><spring:message code='view.msg.opt.logout' /></a>
					</li>
					<!-- <li class="mt5">
						<a id="userInfo" href="javascript:" data-href="/js/a/sys/user/info" class="addTabPage">
						<i class="fa fa-user"></i> 个人中心</a>
					</li>
					<li>
						<a id="modifyPassword" href="javascript:" data-href="/js/a/sys/user/info?op=pwd" class="addTabPage">
						<i class="fa fa-key"></i> 修改密码</a>
					</li>
					<li class="zk_divider"></li>
					<li>
						<a href="/${_adminPath}/${_modulePath}/l/logout">
						<i class="fa fa-sign-out"></i> 退出登录</a>
					</li>
					<li class="zk_divider"></li>
					<li class="zk_dropdown_header">系统切换：</li>
					<li clas>
						<a href="/js/a/switch/default">
							<i class="fa fa-check-circle-o"></i> 主导航菜单
						</a>
					</li>
					<li>
						<a href="/js/a/switch/default1">
							<i class="fa fa-circle-o"></i> 演示子系统01
						</a>
					</li>
					<li>
						<a href="/js/a/switch/default2">
							<i class="fa fa-circle-o"></i> 演示子系统02
						</a>
					</li> -->
					<li class="mt10"></li>
					<li class="mt10"></li>
				</ul>
		    </li>
		  </ul>
		</div>
		<div id='nav-bar' class='zk_navbar zk_pull_left'></div>
	</div>
	<div class="zk_layout">
		<iframe class="zk_content" frameborder="0" name="home" id="home" ></iframe>
	</div>
	<div class="zk_floor" >
        <p>opyright © ${_copyright}</p>
    </div>
    <!-- <div style="display:none"></div> -->
</div>

</body>

</html>





