<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/include/tldlib.jsp" %>

<!DOCTYPE html>
<html>

<head>
	<!-- <title>放在引入 head.jsp 前面，可以重新定义 </title> -->
	<%@include file="/jsp/include/head.jsp" %>	

	<script type="text/javascript">
		
		// var secretKey = '${_userSecretKey}';
	    // console.log("[^_^:20200224-0809-001] secretKey: ", secretKey);

		// 初始化函数
        $(function() {
        	/*** dropdown 节点 open 与 close ***/
			$zk(".zk_dropdown").zkDropdown();
			var validator = $("#loginForm").validate({
				rules: { 
					captcha: { 
						remote: {
							url:"/${_adminPath}/${_modulePath}/l/doingCaptcha",
							dataFilter: function (data, type) {
								// 判断控制器返回的内容，此方法收到的 响应数据是 字符串，返回数据也必输是字符串；
								// 暂不修改远程验证失败时的消息；
								// return false;
								// return '{"code":"1", "msg":"ssss"}';

								if(data == 'true'){
								/* 	$("#captcha-group").hide(); */
									f_captchaOk();
								}
								return data;
							}
						}
					} 
				}, 
				submitHandler:function(jqDom) {
           			f_submitHandler($(jqDom));
           			return false; // 返回 false form 表单不用提交，由自行处理提交。 
			    }
			});
			validator.resetForm();
			
			f_initPage();
			f_initCaptcha();
			f_exceptionMsg(${_exception_key_});
        });

        /*** 提交 form 以 ajax 提交  ***/
        function f_submitHandler(jqForm) { 

	        $zk.zkLoading(1);

	        /*** 1、字段加密 ***/
	        var account = $("#account").val(), password = $("#password").val();
	        var secretKey = '${_userSecretKey}';
	        // console.log("[^_^:20200111-1252-001] secretKey: ", secretKey, account, password);
	        if(secretKey != ''){
	        	$("#account").val($zk.AES.encrypt(account, secretKey, $zk.AES.ivKey));
	        	$("#password").val($zk.AES.encrypt(password, secretKey, $zk.AES.ivKey));
	        }
	        /*** 2、ajax 请求 ***/
	        $zk.zkAjaxSubmitForm(jqForm, {
	        	error:function(xhr){
	        		// console.log("[>_<:20200102-1520-001] ", xhr);
	        		$zk.zkLoading(0);
	        		f_exceptionMsg(data)
	        	},
	        	success:function(resData, status, xhr){
	        		// console.log("[^_^:20200102-1520-002] login success: ", resData);
	        		$("#account").val(account);
	        		$("#password").val(password).select().focus();
	        		
	        		var hash = location.hash;
					if(resData.code == 'zk.0'){
						if (resData.data && resData.data.__url != "") { 
							location.href = resData.data.__url + hash; 
							// console.log(location, window.parent.location, resData);
						} else { 
							location.href = "/${_adminPath}/${_modulePath}" + hash;
						} 
					}else{
						if(resData.data && resData.data.isCaptcha == true){
							// 显示验证码
							f_captchaShow();
						}
						f_exceptionMsg(resData);
						$zk.zkLoading(0);
					}
	        	}
	        });
	        $("#account").val(account);
	        $("#password").val(password).select().focus();
	    } 

        /**
         * 异常消息处理
         */
        function f_exceptionMsg(eMsg){
        	eMsg = eMsg || undefined;
        	if(eMsg){
        		if(eMsg.msg){
	        		$zk.zkShowMsg(eMsg.msg, "error");
	        	}else{
	        		$zk.zkShowMsg(eMsg, "error");
	        	}  
        	}    	
        }

        /**
         * 初始化，处理记住了账号
         */
        function f_initPage(){
        	// 验证码动画监听；暂未启用；
        	// f_captchaListener();
        }

        /**
         * 验证码初始化
         */
        function f_initCaptcha(){
        	$('#captchaRefresh').click(function(){
        		$('#captchaImg').attr('src', "");
				f_zkCaptchaLoading(1);
				var src = '/${_adminPath}/${_modulePath}/l/captcha?'+new Date().getTime();
				$('#captchaImg').attr('src', src);
			});
			$('#captchaImg').click(function(){
				$('#captchaRefresh').click();
				$('#captcha').focus().val('');
			});
			$('#captcha').focus(function(){
				if($('#captchaImg').attr('src') == ''){
					$('#captchaRefresh').click();
				}
			});
        }
        
    //     // 验证正确动画结束监听；暂未启用
    //     function f_captchaListener(){
    //     	var targetDom = document.getElementById('captcha-group');
    //     	// 兼容性写法
    //    	    var animationends = {
    //    			// 标准语法
    //    			"animation":"animationend",
				// // Chrome, Safari 和 Opera 代码
				// 'webkitAnimation':"webkitAnimationEnd",
    //    	    };
        	
    //     	// 取当前浏览器兼容的事件添加
    //    	    function fc_getAnimationend() {
				// var env;
				// for (env in animationends) {
				// 	// t即transition，OTransition，MozTransition，WebkitTransition
				// 	if (targetDom.style[env] !== undefined) {
				// 		return animationends[env];
				// 	}
				// }
    //    	    }
        	
    //     	var targetEvent = fc_getAnimationend();
    //     	// console.log("[^_^:20191223-1558-001] targetEvent: " + targetEvent)
    //     	if(targetEvent){
	   //      	targetDom.addEventListener(targetEvent, function(e){
	   //      		// 如果是最后结束时收起的动画结束，隐藏验证码输入节点
	   //      		if(e.animationName == "captcha-end"){
	   //      			$(targetDom).hide();
	   //      		}
	   //      	});
	   //      }
    //     }
	    
	    /** 验证成功，后的验证动画 */
	    function f_captchaOk(){
	    	// 添加动画 css
	    	targetDom = $("#captcha-group");
	    	targetDom.addClass("zk_captcha_group_ok");
	    	if(targetDom.hasClass("zk_captcha_group")){
	    		targetDom.removeClass("zk_captcha_group");
	    	}
	    	$('input', targetDom).attr("disabled", true);
	    	// $('input', targetDom).attr("readonly", true);
	    }
	    
	    // 验证码显示处理；
	    function f_captchaShow(){
	    	// 验证显示
	    	var targetDom = $("#captcha-group");
	    	targetDom.show();
	    	targetDom.addClass("zk_captcha_group");
	    	if(targetDom.hasClass("zk_captcha_group_ok")){
	    		targetDom.removeClass("zk_captcha_group_ok");
	    		$('#captchaImg', targetDom).click();
	    	}
	    	$('input', targetDom).removeAttr("disabled");
	    }

	    // 验证码 loading 加载关闭与开启；flag 开头标识；0-关闭；1-开启；默认关闭 
	    function f_zkCaptchaLoading(flag=0){
	    	// console.log("------- 验证码加载完成。");
	    	if(flag == 0){
	    		$("#img-captcha-loading").hide();
	    	}else{
	    		$("#img-captcha-loading").show();
	    	}
	    	
	    }
    </script>
    <style type="text/css">
    	.login_box {
			/*border: 1px solid;*/
			margin: 7% auto;
		    margin-bottom: 20px;
		    display: block;
		}
		.login_box_copyright {
			margin-top: 80px;
			margin-bottom: 6px;
			color: var(--font-color);
			text-align: center;

		}
		.login_box_body {
			width: 1000px;
			margin: auto;
		    padding: 0;
		    display: -ms-flexbox;
		    display: flex;
		    margin-top: 50px;
		    box-shadow: 0 20px 80px 0 rgba(0,0,0,0.1);
		    position: relative;
		    background: #fff;
		    border-radius: 5px;
		    border-top: 0;
		    color: #666;
		}

		/*** */
		.login_box_body_left {
			color: #fff;
		    position: relative;
		    padding: 60px 50px 40px 80px;
		    background: linear-gradient(0deg,#2378ca 0, var(--primary-color) 100%);
		    width: 50%;
		    /*padding: 40px 80px 48px;*/
		    display: inline-block;
		    font-size: 14px;
		}
		.login_box_body_left:before, .login_box_body_left:after{
			content: '';
		    position: absolute;
		    top: 0;
		    left: 0;
		    bottom: 0;
		    right: 0;
		    -webkit-box-sizing: border-box;
		    -moz-box-sizing: border-box;
		    box-sizing: border-box;
		}
		.login_box_body_left:before{
		    background: url(/${ctxStatic}/zk/images/login/login_left_1.png) no-repeat 0 0;
		}
		.login_box_body_left:after{
		    background: url(/${ctxStatic}/zk/images/login/login_left_2.png) no-repeat right bottom;
		}
		.login_left_content{
			position: relative;
		    z-index: 1;
		    height: 100%;
		}
		.login_left_content .login_title_1 {
			padding-bottom: 5px;
		}
		.login_left_content .login_title_2 {
			padding-top: 10px;
		}
		.login_left_content .login_info {
			padding-top: 20px;
		    padding-left: 20px;
		    line-height: 30px;
		}
		.login_left_content .login_info ul{
			margin-top: 0;
		    margin-bottom: 10px;
		    list-style-type: disc;
		}
		.login_left_content .login_info ul li{
			display: list-item;
		    text-align: -webkit-match-parent;
		    padding-top: 40px;
		    padding-left: 20px;
		    line-height: 30px;
		}
		.login_left_content .login_bottom{
			/*padding-top: 50px;*/
			position: absolute;
			bottom: 18px;
		}
		.login_left_content a {
			color: #fff;
		}

		/*** */
		.login_box_body_right {
			width: 50%;
		    padding: 40px 80px 48px 60px;
		    display: inline-block;
		}
		.login_box_body_right .login_tools{
			float: right;
		    padding-top: 14px;
		    font-size: 15px;
		}
		.login_box_body_form {
			width: 360px;
		    margin: 28px auto auto auto;
		    padding: 28px 28px 28px 58px;
		    background: #fff;
		    box-shadow: 0 2px 6px #999;
		    border-radius: 5px;
		    position: relative;
		}
		.login_box_body_form_divider {
			margin: 18px 9px
		}

		/* Chrome, Safari, Opera */
		@-webkit-keyframes captcha-check-right{
			0%   { width:0px; }
			100% { width: 100px; }
		}
		/* Standard syntax */
		@keyframes captcha-check-right {
			0%   { width:0px; }
			100% { width: 100px; }
		}
		@-webkit-keyframes captcha-end{
			from { }
			to { 
				height:0px; 
				margin-bottom: 0px;
			} 
		}
		@keyframes captcha-end {
			from { }
			to { 
				height:0px; 
				margin-bottom: 0px;
			} 
		}

		.zk_captcha_group_ok{
			/*height:38px; */
			/*overflow:hidden;*/
			/*-webkit-animation: captcha-end 0.5s forwards;*/
			/* animation-timing-function: linear; */
			/*animation-delay: 1s;*/
			/*animation-iteration-count: 1;*/
			/*-webkit-animation: captcha-end 0.5s forwards; *//* Chrome, Safari, Opera */
			/* -webkit-animation-timing-function: linear; */
			/*-webkit-animation-delay: 1s;*/
			/*-webkit-animation-iteration-count: 1;*/
		}

		.img-checkmark-div{
			width:100px;
			overflow:hidden;
			border-left: 0px;
		}
		.img-checkmark-div div{
			width:0px;
			height: 32px;
			overflow:hidden;
			text-align: left;
			display: flex;
		}
		.img-checkmark-div div img {
			width: 25px;
			height: 25px; 
			margin-left: 6px;
			align-self: center;
		}
		.zk_captcha_group .img-checkmark-div{
			display: none;
		}
		.zk_captcha_group_ok .img-checkmark-div > div {
			width:0px;
			/* height:50px; */
			padding-left: 23px;
			-webkit-animation: captcha-check-right 1s forwards; /* Chrome, Safari, Opera */
			-webkit-animation-timing-function: ease-in-out;
			animation: captcha-check-right 1s forwards;
			animation-timing-function: ease-in-out;
		}
		.zk_captcha_group_ok .img-checkmark-div + img{
			display:none;
		}

		.zk-captcha-loading>div{
				width: 20px;
		    height: 20px;
		    /*position: absolute;
		    top: 6px;
		    margin: 0px 0px 0px 12px;*/
		}
    </style>
</head>
<body>

	<div class="login_box">
        <div class="login_box_body">
            <div class="login_box_body_left">
                <div class="login_left_content">
                    <h2 class="login_title_1"><b><spring:message code="view.msg.application.name" /></b></h2>
                    <div class="login_title_2">${_version}
                    	<!-- ${zkEnv:getString("zk.ser.cen.version")} -->
					</div>
                    <ul class="login_info">
                        <li><spring:message code="view.msg.info.introduction.1" /></li>
                        <li><spring:message code="view.msg.info.introduction.2" /></li>
                        <li><spring:message code="view.msg.info.introduction.3" /></li>
                        <li><spring:message code="view.msg.info.introduction.4" /></li>
                        <li><spring:message code="view.msg.info.introduction.5" /></li>
                    </ul>
                    <div class="login_bottom">
                        <spring:message code="view.msg.info.contact.us" />:&nbsp;<a href="http://www.zk.com" target="_blank">http://www.zk.com</a>
                    </div>
                </div>
            </div>
            <div class="login_box_body_right">
                <!-- <h2>
					<b><spring:message code="view.msg.info.login.func" /></b>
				</h2> -->
                <div class="login_box_body_form">
                	<div class="zk-loading zk-global-loading"></div>
                    <form id="loginForm" class="zk_form" action="/${_adminPath}/${_modulePath}/l/login" method="post">
                        <div class="form-group zk_form_has_feedback">
                            <!-- is-valid is-invalid; data-msg-required='原始数据验证失败时显示；'-->
                            <input type="text" class="form-control" 
                            	id="account" name="account" 
	                            value='${zkStr:isEmpty(account)?_cookie_account:account}'
								placeholder='<spring:message code="view.msg.login.account" />'
								required="true"
								data-msg-required='<spring:message code="view.msg.login.account.required" />'
                            	title="" />
                            <!-- <div class="invalid-feedback">请输入用户名；</div> -->
                            <!-- <div class="valid-feedback">Looks good!</div> -->
                            <span class="glyphicon glyphicon-user zk_form_control_feedback"
								title='<spring:message code="view.msg.login.account" />' >
							</span>
                        </div>
                        <div class="form-group zk_form_has_feedback">
                            <!-- is-valid is-invalid -->
                            <input type="password" class="form-control" 
                            	id="password" name="password" 
                            	value='admin' 
                            	placeholder="<spring:message code='view.msg.login.pwd' />"
								required="true"
								data-msg-required='<spring:message code="view.msg.login.pwd.required" />'
								title="" />
                            <span class="glyphicon glyphicon-lock zk_form_control_feedback"
								title="<spring:message code='view.msg.login.pwd.alert' />" 
								onmousedown="$('#password').attr('type','text')" 
								onmouseup="$('#password').attr('type','password')" >
							</span>
                        </div>
                        <!-- 验证码 -->
                        <div id = "captcha-group" class="form-group zk_captcha_group" 
								style="display:${isCaptcha == 'true'?'blank':'none'};" >
							<div class="input-group">
							  	<input id="captcha" type="text" class="form-control" 
									name="captcha" placeholder="<spring:message code='view.msg.login.valideCode' />"
									required="true" 
									data-msg-required="<spring:message code='view.msg.login.valideCode.required' />" 
									data-msg-remote="<spring:message code='view.msg.login.valideCode.error' />"
								>
								<div id="img-captcha-loading" class="zk-loading zk-captcha-loading zk_display_none" onload="f_zkCaptchaLoading(0)"></div>
								<div class="input-group-append">
									<span class="input-group-text" id="basic-addon2">
										<div id = "img_checkmark" class="img-checkmark-div" >
											<div class=" ">
												<img id="checkmarkImg-test" src="/${ctxStatic}/zk/images/checkmark.ico" alt=" " >
											</div>
										</div>
										<img id="captchaImg"  
											title="<spring:message code='view.msg.login.valideCode.alert' />" 
											src="/${_adminPath}/${_modulePath}/l/captcha"
											alt=" " style="width:100px;">
										<button id="captchaRefresh" class="zk_display_none" type="button"></button>
									</span>
						        </div>
						        
							</div>
						</div>
                        <div class="form-group">
                            <div class="col-sm-12 pl-0">
                                <div class="form-check zk_form_check zk_pull_left">
                                    <input class="form-check-input zk_form_checkbox" 
                                    	type="checkbox" id="rememberAccount" name="rememberAccount" ${rememberAccount == "true"?"checked":""} >
                                    <label class="form-check-label" for="rememberAccount"
                                    	title='<spring:message code="view.msg.login.rememberAccount.alert" />' >
                                    	&nbsp;<spring:message code="view.msg.login.button.rememberAccount" />
                                    </label>
                                </div>
                                <div class="form-check zk_form_check zk_pull_left">
                                    <input class="form-check-input zk_form_checkbox"
                                    	type="checkbox" id="rememberMe" name="rememberMe" ${rememberMe == "true"?"checked":""} >
                                    <label class="form-check-label" for="rememberMe"
                                    	title='<spring:message code="view.msg.login.rememberMe.alert" />' >
                                    	&nbsp;<spring:message code="view.msg.login.button.rememberMe" />
                                    </label>
                                </div>
                                <dir class="zk_clear"></dir>
                            </div>
                        </div>
                        <!-- 按钮 -->
						<div class="form-group">
							<!-- <input type="hidden" name="__url" value=""> -->
							<input type="hidden" name="_zk_req_sign_" value="${_zk_req_sign_}">
							<button type="submit" class="btn btn-primary btn-block btn-flat" 
								data-loading="<spring:message code='view.msg.login.loading' />" 
								data-login-valid="<spring:message code='view.msg.login.valid' />"
							>
								<spring:message code='view.msg.login.button.login' />
							</button>
						</div>
                    </form>
                    <dir class="login_box_body_form_divider zk_divider"></dir>
                    <div class="form-group">
                    	<div class="col-sm-12 pl-0">
                    		<div class="zk_dropdown zk_pull_right">
	                            <a href="javascript:" >
									<i class="fa icon-globe"></i>
									<c:choose>
									    <c:when test="${locale == 'zh_CN'}">
									        简体中文
									    </c:when>
									    <c:otherwise>
									        English
									    </c:otherwise>
									</c:choose>
								</a>
	                            <ul class="zk_dropdown_menu">
	                                <li class="mt5"></li>
	                                <li><a href="javascript:$zk.zkChangeLanguage('/${_adminPath}/${_modulePath}/locale?locale=', 'zh_CN')">简体中文</a></li>
	                                <li><a href="javascript:$zk.zkChangeLanguage('/${_adminPath}/${_modulePath}/locale?locale=', 'en_US')">English</a></li>
	                                <li class="mt10"></li>
	                            </ul>
	                        </div>
	                        <dir class="zk_clear"></dir>
	                    </div> 
                    </div>
                </div>
            </div>
            <div class="zk_clearfix"></div>
        </div>
        <div class="login_box_copyright">opyright © zk platform - zk.xxx.com V1.0.0</div>
    </div>

</body>
</html>




