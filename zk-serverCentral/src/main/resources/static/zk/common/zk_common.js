/*
* @Author: Vinson
* @Date:   2020-01-30 20:37:12
* @Last Modified by:   Vinson
* @Last Modified time: 2021-02-21 23:54:53
* 
* 
* 
*/
(function(_zkTools){

	if(window.$zk){
		return;
	}

	/*** $zk 创建与初始化 ********************************************************************/
	/*** $zk 创建与初始化 ********************************************************************/
	/*** $zk 创建与初始化 ********************************************************************/

	var zkKeys = {
		isInitKey:'_zk_key_isInit'
	};

	// 禁止修改 zkFlag 对象，不能删除，新增，修改属性
	Object.freeze(zkKeys);

	// 禁止对 zkFlag对象 进行扩展
	// Object.preventExtensions(zkConstant);
	// 禁止对 zkFlag对象 属性进行删除
	// Object.seal(zkConstant);
	// 禁止修改 zkFlag对象，不能删除，新增，修改属性
	// Object.freeze(zkConstant);

	var $zk = function( selector ) {
		return new $zk.fn.init( $(selector) );
	};
	var fnPrototype = {
		version:"3.0.0",
		zk:"3.0.0",
		zkKeys:zkKeys,
		constructor: $zk
	}

	$zk.fn = $zk.prototype = fnPrototype;

	// 抄自 jQuery 1.12.4 
	$zk.extend = $zk.fn.extend = function() {
		var src, copyIsArray, copy, name, options, clone,
			target = arguments[ 0 ] || {},
			i = 1,
			length = arguments.length,
			deep = false;

		// Handle a deep copy situation
		if ( typeof target === "boolean" ) {
			deep = target;

			// skip the boolean and the target
			target = arguments[ i ] || {};
			i++;
		}

		// Handle case when target is a string or something (possible in deep copy)
		if ( typeof target !== "object" && !jQuery.isFunction( target ) ) {
			target = {};
		}

		// extend jQuery itself if only one argument is passed
		if ( i === length ) {
			target = this;
			i--;
		}

		for ( ; i < length; i++ ) {

			// Only deal with non-null/undefined values
			if ( ( options = arguments[ i ] ) != null ) {

				// Extend the base object
				for ( name in options ) {
					src = target[ name ];
					copy = options[ name ];

					// Prevent never-ending loop
					// if ( target === copy ) {
					if (name === "__proto__" || target === copy) { // 修复jQuery原型污染漏洞  ThinkGem
						continue;
					}

					// Recurse if we're merging plain objects or zkrays
					if ( deep && copy && ( jQuery.isPlainObject( copy ) ||
						( copyIsArray = jQuery.isArray( copy ) ) ) ) {

						if ( copyIsArray ) {
							copyIsArray = false;
							clone = src && jQuery.isArray( src ) ? src : [];

						} else {
							clone = src && jQuery.isPlainObject( src ) ? src : {};
						}

						// Never move original objects, clone them
						target[ name ] = jQuery.extend( deep, clone, copy );

					// Don't bring in undefined values
					} else if ( copy !== undefined ) {
						target[ name ] = copy;
					}
				}
			}
		}

		// Return the modified object
		return target;
	};

	/*** $zk.fn 扩展 方法 ********************************************************************/
	/*** $zk.fn 扩展 方法 ********************************************************************/
	/*** $zk.fn 扩展 方法 ********************************************************************/
	var extendFn = {
		// 全屏 节点处理
		zkFullScreen:function(){
			// var fullScreen = document.getElementById("fullScreen");

			// var fullScreen = $("#fullScreen");
			// fullScreen = fullScreen[0];

			var fullScreen = this.jqDom;
			// 全屏 节点处理
			if(fullScreen.length > 0){
				fullScreen.each(function(index, item){
					$zk.event.binding(item, 'click', function(){
						if ($(item).data("isOpen") == "true") { 
							$(item).data("isOpen", "false"); 

							if (document.exitFullscreen) { 
								document.exitFullscreen() 
							} else { 
								if (document.msExitFullscreen) { 
									document.msExitFullscreen() 
								} else { 
									if (document.mozCancelFullScreen) { 
										document.mozCancelFullScreen() 
									} else { 
										if (document.webkitCancelFullScreen) { 
											document.webkitCancelFullScreen() 
										} 
									} 
								} 
							}
						} else { 
							$(item).data("isOpen", "true"); 
							var d = document.documentElement; 
							if (d.requestFullscreen) { 
								d.requestFullscreen() 
							} else { 
								if (d.msRequestFullscreen) { 
									d.msRequestFullscreen() 
								} else { 
									if (d.mozRequestFullScreen) { 
										d.mozRequestFullScreen() 
									} else { 
										if (d.webkitRequestFullScreen) { 
											d.webkitRequestFullScreen() 
										}
									}
								} 
							} 
						} 
						// $(window).resize();
						return false 
					});
				});
			}
		}
	};

	/*** common js 函数扩展说明 ********************************************************************/
	/*** common js 函数扩展说明 ********************************************************************/
	/*** common js 函数扩展说明 ********************************************************************/
	/*** 收起 main head ***/
	function f_hideMainHead(){

		var thisDom = null;
		var pDom = window;

		do{
			thisDom  = pDom;
			pDom = pDom == null?null:pDom.parent;
			// main header 隐藏
			var zkMainHeaders = $zk.getRootDom('.zk_header');
			if(zkMainHeaders && zkMainHeaders.length > 0){
				zkMainHeaders.each(function(index, item){
					// $(item).hide();
					$(item).slideUp(300);
				})
			}
			// main floor 隐藏
			var zkMainFloors = $zk.getRootDom('.zk_floor');
			if(zkMainFloors && zkMainFloors.length > 0){
				zkMainFloors.each(function(index, item){
					$(item).slideUp(300);
				})
			}
		}while(pDom != null && thisDom != pDom);
	};
	/*** 打开 main head ***/
	function f_showMainHead(){

		var thisDom = null;
		var pDom = window;

		do{
			thisDom  = pDom;
			pDom = pDom == null?null:pDom.parent;
			// main header 显示
			var zkMainHeaders = $zk.getRootDom('.zk_header');
			if(zkMainHeaders && zkMainHeaders.length > 0){
				zkMainHeaders.each(function(index, item){
					// $(item).show();
					$(item).slideDown(300);
				})
			}
			
			// main floor 显示
			var zkMainFloors = $zk.getRootDom('.zk_floor');
			if(zkMainFloors && zkMainFloors.length > 0){
				zkMainFloors.each(function(index, item){
					$(item).slideDown(300);
				})
			}
		}while(pDom != null && thisDom != pDom);
	};

	// 消息显示
	function f_showMsg(msg, type, showTime, orientation){

		orientation = orientation || 'top-right';
		// success  error  warning  info
		orientation = 'zk-msg-' + orientation;
		// var targetDom = $zk.getRootDom('.' + orientation);
		var targetDom = $('.' + orientation);
		if(targetDom == null || targetDom.length < 1){
			targetDom = $('<div class="zk-msg ' + orientation + '"></div>');
			// 在开头插入节点
			// $zk.getRootDom("body").prepend(targetDom); 
			// $("body", parent.document).prepend(targetDom); 
			$("body").prepend(targetDom); 
		}

		msg = String(msg);
		var typeClass = 'zk-msg-info';
		switch(type){
			case 'error': typeClass = 'zk-msg-error'; break;
			case 'warning': typeClass = 'zk-msg-warning'; break;
			case 'success': typeClass = 'zk-msg-success'; break;
			// case 'info': ; break;
		}

		var msgDomStr = [
		  ' <div>',
		  '		<div class="' + typeClass + '">',
		  '			<button class="zk-msg-close-button">x</button>',
		  '			<div class="zk-msg-message" >' + msg + '</div></div>',
		  '		</div>',
		  '	</div>'].join('');

		var msgDom = $(msgDomStr);
		$("button", msgDom).click(function(){f_closeMsg(msgDom);})
		
		targetDom.append(msgDom);

		showTime = showTime || 3000;
		setTimeout(function(){
		    f_closeMsg(msgDom);
		}, showTime);

		function f_closeMsg(jqDom){
			// 滑动收起
			jqDom.slideUp("msg-close", function(){
				// 收起动画结束后，删除
				jqDom.remove();
			})
		}
	}

	// 显示 模态窗口
	function f_modalShow(contentDom){
		// 模态窗口展示内容
		var targetDom = $("<div class='zk_modal_root'></div>");
		targetDom.append($("<div class='zk_modal_mask' ></div>"));
		targetDom.append($("<div class='zk_modal_window'></div>").append($(contentDom)));
		// 在开头插入节点
		// zkTools.getRootDom("body").prepend(targetDom); 
		$("body").prepend(targetDom);
		return targetDom;
	}

	/**
	 * 模态窗口显示内容
	 * @return: 模态窗口的 jquery dom 对象；
	 */
	 function f_modal(dom){
	 	/*
	 	var iframe = $('<iframe id="" src="" width="100%" height="100%" frameborder="0"></iframe>');
		iframe.attr(zkTabConstant.contentId, tabId);
		iframe.attr("src", url);
		*/
		return f_modalShow(dom);
	 }

	// 确认提示框
	function f_confirm(opt){
		// confirm success warning error info
		opt = opt || {};
		opt = $.extend(true, {
			type: 'info',     // 类型 confirm | success | warning | error | info
			title: undefined, // title 默认为 'zk.confirm.title.' + opt.type 国际化值
			msg: '&nbsp;', // 提示内容；默认为一个空格
			isClose: true, // 是否显示 右上角的关闭按钮；true - 显示；false - 不显示 
			isCancel: true, // 是否显示 取消按钮；true - 显示；false - 不显示 
			onOk: undefined, // OK 按钮处理函数；有设置时，需要返回 true 才会自动关闭 模态窗口；否则需要手动处理关闭；处理函数中的 this 指针，指向此模态窗口；
			onCancel: undefined, // cancel 按钮处理函数；有设置时，需要返回 true 才会自动关闭 模态窗口；否则需要手动处理关闭；处理函数中的 this 指针，指向此模态窗口；
			btnOkTxt: undefined, // OK 按钮 名称; 默认为 zk.opt.name.ok 国际化值;
			btnCancelTxt: undefined, // cancel 按钮 名称; 默认为 zk.opt.name.cancel 国际化值
		}, opt);

		var msg = $zkLocale.msg;
		var locale = _zkTools.getLocale();

		// title 默认值 国际化
		if (!opt.title) {
			opt.title = msg['zk.confirm.title.' + opt.type];
		}
		// ok 按钮名称 默认值 国际化
		if (!opt.btnOkTxt) {
			opt.btnOkTxt = msg['zk.opt.name.ok'];
		}
		// cancel 按钮名称 默认值 国际化
		if (!opt.btnCancelTxt) {
			opt.btnCancelTxt = msg['zk.opt.name.cancel'];
		}

		var htmlStrs = [];
		// title 
		htmlStrs.push("<div class='zk_modal_title'>" + opt.title + "</div>");
		// msg content
		htmlStrs.push("<div class='zk_modal_content'>");
		htmlStrs.push("<i class='zk_modal_content_i zk_modal_content_i_" + opt.type + "' ></i>");
		htmlStrs.push(opt.msg);
		htmlStrs.push("</div>");
		// close 是否展示右上角的关闭按键
		if (opt.isClose) { // glyphicon glyphicon-remove  fa fa-close  layui-icon layui-icon-close 
			htmlStrs.push("<span><a class='layui-icon layui-icon-close zk_modal_title_close close' href='javascript:;'></a></span>");
		}
		// button 按钮
		htmlStrs.push("<div class='zk_modal_btn'>");
		// 如果没传入取消的处理函数，不显示取消按键
		if (opt.isCancel) {
			htmlStrs.push("<a class='zk_modal_btn_1'>" + opt.btnCancelTxt + "</a>");
		}
		htmlStrs.push("<a class='zk_modal_btn_0'>" + opt.btnOkTxt + "</a>");
		htmlStrs.push("</div>");
		// resize
		htmlStrs.push("<span class='zk_modal_resize'></span>");

		var htmlStr = htmlStrs.join("");

		var contentDom = $(htmlStr);
		var modalDom = f_modalShow(contentDom);

		// 关闭事件
		if (opt.isClose) {
			$(".close", contentDom).bind("click", function(e) {
				if (typeof(opt.onClose) === 'function') {
					if (opt.onClose.call(modalDom, e)) {
						// onClose 如果有设置关闭处理函数，需要返回 true 才会关闭 modal 窗口
						modalDom.remove();
					}
				} else {
					modalDom.remove();
				}
			});
		}
		// 取消事件
		if (opt.isCancel) {
			$(".zk_modal_btn_1", contentDom).bind("click", function(e) {
				if (typeof opt.onCancel === 'function') {
					// onCancel 如果有设置取消处理函数返回 true 时，才会关闭 modal 窗口
					if (opt.onCancel.call(modalDom, e)) {
						modalDom.remove();
					}
				} else {
					modalDom.remove();
				}
			});
		}

		// 确认事件
		$(".zk_modal_btn_0", contentDom).bind("click", function(e) {
			if (typeof opt.onOk === 'function') {
				// onOk 如果有设置确认处理函数，需要返回 true 才会关闭 modal 窗口
				if (opt.onOk.call(modalDom, e)) {
					modalDom.remove();
				}
			} else {
				modalDom.remove();
			}

		});
	}

	// 加载显示与关闭；0-关闭；其他显示；默认 0；
	function f_loading(flag=0){
		loadingDom = $(".zk-global-loading");
		if(flag == 0){
    		loadingDom.addClass('zk_display_none');
    	}else{
    		loadingDom.removeClass('zk_display_none');
    	}
	}

	/**
	 * ajax 通过确认框提交
	 * @msgOpt：确认消息；
 		{
			type: 提示类型，默认 'confirm'
			msg: 消息，默认 'Confirm'
 		}
 		@ajaxOpt 请求参数，同 ajax
	 */
	function f_ajaxComfirmSubmit(msgOpt, ajaxOpt){
        msgOpt = msgOpt || {};
        msgOpt = $.extend({
            type: 'confirm',
            msg: 'Confirm'
        }, msgOpt);

        ajaxOpt = ajaxOpt || {};
        ajaxOpt = $.extend({
            async: true,
        }, ajaxOpt);

        var cbf_error = undefined, cbf_success = undefined;
        if(ajaxOpt.error){
            cbf_error = ajaxOpt.error;
            delete ajaxOpt['error'];
        }
        if(ajaxOpt.success){
            cbf_success = ajaxOpt.success;
            delete ajaxOpt['success'];
        }

        $zk.zkConfirm({
            type: msgOpt.type,
            msg: msgOpt.msg,
            onOk:function(){
                var modalWindow = this;
                // 成功处理
                ajaxOpt.success = function(resData, status, xhr){
                    var msgType = "info";
                    if(resData.code == 'zk.0'){
                        msgType = "success";
                    }
                    $zk.zkShowMsg(resData.msg, msgType);
                    if(typeof(cbf_success) === 'function'){
                        cbf_success.call(modalWindow, resData, status, xhr);
                    }
                    modalWindow.remove();
                };
                // 失败处理
                ajaxOpt.error = function(xhr, status, err){
                    // $zk.zkLoading(0);
                    if(console){
                        console.log("[>_<:20200117-1658-001] ajax fail ---", xhr, status, err);
                    }
                    
                    if(typeof(cbf_error) === 'function'){
                        cbf_error.call(modalWindow, xhr, status, err);
                    }
                    // f_search();
                    modalWindow.remove();
                }
                $zk.zkAjax(ajaxOpt);
                return false;
            }
        });
    }

	/**
	 * ajax 请求
	 */
	 function f_ajax(options, isLoding=true) { 
	 	/* options 同 ajax 参数对象 */
	 	options = options||{};
	 	
	 	options.method = options.method||'GET';
        options.async = (options.async == undefined)?true:options.async;
        options.dataType = options.dataType||'json';

        var cbf_error = undefined, cbf_success = undefined;
            
        if(options.error){
        	cbf_error = options.error;
        	delete options['error'];
        }
        if(options.success){
        	cbf_success = options.success;
        	delete options['success'];
        }
        // 开启了显示加载框；打开全局的加载框
        if(isLoding){
        	f_loading(1);
        }
        
	 	$.ajax($.extend(true, {
	 		error:function(xhr, status, error){
	 			// 开启了显示加载框；关闭全局的加载框
	 			if(isLoding){
	 				f_loading(0);
	 			}
            	if(cbf_error instanceof Function){
            		cbf_error(xhr, status, error);
            	}
	 		},
	 		success:function(resultData, status, xhr){
	 			// 开启了显示加载框；关闭全局的加载框
	 			if(isLoding){
	 				f_loading(0);
	 			}
            	if(cbf_success instanceof Function){
            		cbf_success(resultData, status, xhr);
            	}
	 		}
	 	}, options));
	 }

	 /**
	 * ajaxSubmitForm 请求
	 */
	 function f_ajaxSubmitForm(formJqObj, options, isLoding=true) { 
    	options = options||{};
    	/* options
    	{
			method:'POST'、'GET',
			async:
			dataType:
			iframe:
			error:
			success:
    	}
    	*/
    	options.method = options.method||'POST';
    	options.async = (options.async == undefined)?true:options.async;
    	options.dataType = options.dataType||'json';

        // js.loading(message == undefined ? js.text("loading.submitMessage") : message);
        // if (options.downloadFile === true) {
        //     options.iframe = true
        // }

        var cbf_error = undefined, cbf_success = undefined;
        
        if(options.error){
        	cbf_error = options.error;
        	delete options['error'];
        }
        if(options.success){
        	cbf_success = options.success;
        	delete options['success'];
        }

        // 开启了显示加载框；打开全局的加载框
        if(isLoding){
        	f_loading(1);
        }
        formJqObj.ajaxSubmit($.extend(true, {
            type: options.method,
            xhrFields: {
                withCredentials: true
            },
            url: formJqObj.attr("action"),
            dataType: options.dataType,
            async: options.async,
            error: function(xhr) {
            	// 开启了显示加载框；关闭全局的加载框
	 			if(isLoding){
	 				f_loading(0);
	 			}
            	if(cbf_error instanceof Function){
            		cbf_error(xhr);
            	}
            },
            success: function(data, status, xhr) {
            	// 开启了显示加载框；关闭全局的加载框
	 			if(isLoding){
	 				f_loading(0);
	 			}
            	if(cbf_success instanceof Function){
            		cbf_success(data, status, xhr);
            	}
            }
        }, options));
    }

	////////////////////
	var zkFunc = {
		// 显示与隐藏 head 
		zkHideMainHead: f_hideMainHead,
		zkShowMainHead: f_showMainHead,
		// 国际化语言切换
	    zkChangeLanguage: function f_languageChange(url, locale){
	    	locale = _zkTools.formatLocale(locale);
	    	$.ajax({
	    		url: url + locale,
	            method: 'GET',
	            data: {},
	            /* dataType: 'JSON', */
	            async: false,
	            success: function(result, status, xhr){
	            	_zkTools.setLocale(locale);
	            	// 成功，刷新页面
	                window.location.reload();
	            },
	            error: function(xhr, status, err){
	            	if(console){
	            		console.error("languageChange ajax fail ---", status, err);
	            	}
	            }
	        });
    	},
    	zkResMsg:function(resData){
    		// 请求消息处理
    		if(resData.code == 'zk.0'){
				$zk.zkShowMsg(resData.msg, "success");
			}else{
				$zk.zkShowMsg(resData.msg, "error");
			}
    	},
    	zkBreadcrumb:{
    		// 添加一个
    		// 
    	},
		/**
		 * 页面 View 消息提示框显示；
		 * @msg: 要显示的消息; 为空时，不显示；
		 * @type: 'error'-错误, 'info'-信息, 'warning'-警告, 'success'-成功；默认 'info'-信息；
		 * @showTime: 显示时长，单位毫秒；默认 3000 毫秒；
		 * @orientation: 消息方向；top  top-left  top-right  bottom-right; 默认 top-right
		 */
		zkShowMsg: f_showMsg,
		zkAjax: f_ajax,
		zkAjaxSubmitForm: f_ajaxSubmitForm,
		zkLoading: f_loading,
		zkConfirm: f_confirm,
		zkAjaxComfirmSubmit: f_ajaxComfirmSubmit,
		zkModal: f_modal
	};
	$.extend(zkFunc, _zkTools);
	$.extend(zkFunc, fnPrototype);
	$zk.zkFunc = zkFunc;

	/*** common js 初始化脚步 ********************************************************************/
	/*** common js 初始化脚步 ********************************************************************/
	/*** common js 初始化脚步 ********************************************************************/

	/*** $zk.fn 扩展 ********************************************************************/
	var init = $zk.fn.init = function( jqDom ) {
		/***  ***/
		this.jqDom = jqDom;
		return this;
		/***  ***/
		// $.fn.extend(extendFn);
		// return jqDom;
	}
	// 将 $zk.fn 上的方法，赋给 新创建的对象；
	init.prototype = $zk.fn;

	$zk.fn.extend(extendFn);
	/* ===== $zk 对象 fn 方法说明
	{
	}
	*/

	/*** $zk 扩展 ********************************************************************/
	$zk.extend($zk.zkFunc);
	/* ===== $zk 方法说明
	{
	}
	*/

	/* ===== common js 初始化脚本 */

	/**
	 * 一些初始化
	 */
	// 1、加载 loading 初始化
	function f_initLoading(){
		$(".zk-loading").append("<div class='zk-loading-div'> <div></div><div></div><div></div> <div></div><div></div><div></div> <div></div><div></div><div></div> </div>");
		$(".zk-loading").addClass('zk_display_none');
	}

	$(document).ready(function(){

		// 初始化 加载横琴
		f_initLoading();

		// // AJAX 请求开始时，触发
		// $(document).ajaxSend(function(e, xhr, opt){
		//    // alert("AJAX 请求开始。。。 ", opt);
		//    f_loading(1);
		// });

		// // 所有 AJAX 请求完成时，触发
		// $(document).ajaxStop(function(){
		//     // alert("所有 AJAX 请求已完成");
		//     f_loading(0);
		// });
	});

	/*** 定义 $zk ***/
	window.$zk = $zk;

})(_zkTools);







