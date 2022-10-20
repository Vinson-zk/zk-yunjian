
<meta charset="UTF-8" />
<meta name="decorator" content="blank" />
<title> <spring:message code="view.msg.application.name" /> </title>
<link rel="shortcut icon" type="image/x-icon" href="/${ctxStatic}/zk/images/favicon_zk_128x128.ico?v=${_version}" media="screen" />

<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10" />
<meta http-equiv="Expires" content="0">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Cache-Control" content="no-store">

<script type="text/javascript">
	/*
	var _hmt = _hmt || [];
	(function() {
		var hm = document.createElement("script");
		hm.src = "https://hm.baidu.com/hm.js?104e825088869ff9c5855f24ab8204c2";
		var s = document.getElementsByTagName("script")[0];
		s.parentNode.insertBefore(hm, s);})();
	*/
	
	// 监听本地存储的变化；Storage 发生变化（增加、更新、删除）时的 触发，同一个页面发生的改变不会触发，只会监听同一域名下其他页面改变 Storage
	window.addEventListener('storage', function (e) {
		// console.log("[^_^:20190827-1128-001] e: ", e);
		// 主题或语言变化时，重新加载
		if(e.key == 'theme' || e.key == 'locale'){
			// 刷新页面
	        window.location.reload();
		}
	})
	
	var ctxStatic='${ctxStatic}';
	
	var _version='${_version}';

</script> 

<c:set var = "_v_jquery" value = "3.4.1" />
<c:set var = "_v_jqValidation" value = "1.16" />
<c:set var = "_v_jqForm" value = "3.51.0" />
<c:set var = "_v_jqEasyui" value = "1.9.2" />
<c:set var = "_v_jqSlimscroll" value = "1.3.8" />
<c:set var = "_v_cryptoJS" value = "sytelus/3.1.2" />
<c:set var = "_v_bootstrap" value = "4.4.1" />
<c:set var = "_v_awesome" value = "4.7.0" />
<c:set var = "_v_My97DatePicker" value = "4.8" />

<!-- <c:set var = "_v_adminlte" value = "4.7.0" />
 -->
<link rel="stylesheet" href="/${ctxStatic}/lib/bootstrap/${_v_bootstrap}/css/bootstrap.min.css">
<link rel="stylesheet" href="/${ctxStatic}/lib/font-awesome/${_v_awesome}/css/font-awesome.min.css">

<!-- js 引用 -->
<script src="/${ctxStatic}/lib/jquery/${_v_jquery}/jquery.min.js"></script>
<script src="/${ctxStatic}/lib/jquery-validation/${_v_jqValidation}/jquery.validate.js"></script>
<script src="/${ctxStatic}/lib/jquery-validation/${_v_jqValidation}/localization/messages_en_US.js"></script>
<script src="/${ctxStatic}/lib/jquery-validation/${_v_jqValidation}/jquery.validate.extend.js"></script>
<script src="/${ctxStatic}/lib/jquery-form/${_v_jqForm}/jquery.form.js"></script>
<script src="/${ctxStatic}/lib/jquery-slimscroll/${_v_jqSlimscroll}/jquery.slimscroll.js"></script>
<script src="/${ctxStatic}/lib/bootstrap/${_v_bootstrap}/js/bootstrap.min.js"></script>
<script src="/${ctxStatic}/lib/cryptoJS/${_v_cryptoJS}/components/core.js"></script>
<script src="/${ctxStatic}/lib/cryptoJS/${_v_cryptoJS}/components/cipher-core.js"></script>
<script src="/${ctxStatic}/lib/cryptoJS/${_v_cryptoJS}/components/aes.js"></script>
<script src="/${ctxStatic}/lib/my97/${_v_My97DatePicker}/WdatePicker.js"></script>

<script src="/${ctxStatic}/zk/common/zk_validate.extend.js"></script>

<link rel="stylesheet" href="/${ctxStatic}/zk/themes/default/fonts/simple-icons.min.css">
<link rel="stylesheet" href="/${ctxStatic}/zk/themes/default/fonts/glyphicon-icons.min.css">
<link rel="stylesheet" href="/${ctxStatic}/zk/themes/default/colors.css">

<link rel="stylesheet" href="/${ctxStatic}/zk/common/zk_common.css">
<link rel="stylesheet" href="/${ctxStatic}/zk/common/navbar/zk_navbar.css">
<link rel="stylesheet" href="/${ctxStatic}/zk/common/dropdown/zk_dropdown.css">
<link rel="stylesheet" href="/${ctxStatic}/zk/common/setting/zk_setting.css">
<link rel="stylesheet" href="/${ctxStatic}/zk/common/slide/zk_slide.css">
<link rel="stylesheet" href="/${ctxStatic}/zk/common/menu/zk_menu.css">
<link rel="stylesheet" href="/${ctxStatic}/zk/common/tab/zk_tab.css">
<link rel="stylesheet" href="/${ctxStatic}/zk/common/switch/zk_switch.css">

<script src="/${ctxStatic}/zk/zk_tools.js"></script>
<script src="/${ctxStatic}/zk/common/zk_common.js"></script>
<script src="/${ctxStatic}/zk/common/zk_crypto.js"></script>
<script src="/${ctxStatic}/zk/common/navbar/zk_navbar.js"></script> 
<script src="/${ctxStatic}/zk/common/dropdown/zk_dropdown.js"></script> 
<script src="/${ctxStatic}/zk/common/slide/zk_slide.js"></script> 
<script src="/${ctxStatic}/zk/common/menu/zk_menu.js"></script> 
<script src="/${ctxStatic}/zk/common/tab/zk_tab.js"></script> 
<script src="/${ctxStatic}/zk/common/switch/zk_switch.js"></script> 

<script src="/${ctxStatic}/zk/localization/localeData.js"></script>
<script src="/${ctxStatic}/zk/localization/locale/msg_${locale}.js"></script>

<script type="text/javascript">
	/*** js 前端的一些初始化设置 ***/
	// 设置国际化语言
	var locale = '${locale}';
	// zkTools.setLocale(locale);

</script>













