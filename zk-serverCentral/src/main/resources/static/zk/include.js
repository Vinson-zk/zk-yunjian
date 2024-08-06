/*
* @Author: Vinson-1
* @Date:   2020-03-10 08:44:44
* @Last Modified by:   Vinson
* @Last Modified time: 2020-03-10 09:01:10
* 
* 
* 
*/

// var ctxStatic = 'static';
if(!ctxStatic){
	ctxStatic = 'static';
}

if(!locale){
	var locale = localStorage.getItem("locale") || 'en_US';
}

var _v_jquery = "3.4.1";
var _v_jqValidation = "1.16";
var _v_jqForm = "3.51.0";
var _v_jqEasyui = "1.9.2";
var _v_jqSlimscroll = "1.3.8";
var _v_cryptoJS = "sytelus/3.1.2";
var _v_bootstrap = "4.4.1";
var _v_awesome = "4.7.0";
var _v_My97DatePicker = "4.8";

document.write('<link rel="stylesheet" href="/' + ctxStatic + '/lib/bootstrap/' + _v_bootstrap + '/css/bootstrap.min.css">');
document.write('<link rel="stylesheet" href="/' + ctxStatic + '/lib/font-awesome/' + _v_awesome + '/css/font-awesome.min.css">');

document.write('<script src="/' + ctxStatic + '/lib/jquery/' + _v_jquery + '/jquery.min.js"></script>');
document.write('<script src="/' + ctxStatic + '/lib/jquery-validation/' + _v_jqValidation + '/jquery.validate.js"></script>');
document.write('<script src="/' + ctxStatic + '/lib/jquery-validation/' + _v_jqValidation + '/localization/messages_en_US.js"></script>');
document.write('<script src="/' + ctxStatic + '/lib/jquery-validation/' + _v_jqValidation + '/jquery.validate.extend.js"></script>');
document.write('<script src="/' + ctxStatic + '/lib/jquery-form/' + _v_jqForm + '/jquery.form.js"></script>');
document.write('<script src="/' + ctxStatic + '/lib/jquery-slimscroll/' + _v_jqSlimscroll + '/jquery.slimscroll.js"></script>');
document.write('<script src="/' + ctxStatic + '/lib/bootstrap/' + _v_bootstrap + '/js/bootstrap.min.js"></script>');
document.write('<script src="/' + ctxStatic + '/lib/cryptoJS/' + _v_cryptoJS + '/components/core.js"></script>');
document.write('<script src="/' + ctxStatic + '/lib/cryptoJS/' + _v_cryptoJS + '/components/cipher-core.js"></script>');
document.write('<script src="/' + ctxStatic + '/lib/cryptoJS/' + _v_cryptoJS + '/components/aes.js"></script>');
document.write('<script src="/' + ctxStatic + '/lib/my97/' + _v_My97DatePicker + '/WdatePicker.js"></script>');

document.write('<script src="/' + ctxStatic + '/zk/common/zk_validate.extend.js"></script>');

document.write('<link rel="stylesheet" href="/' + ctxStatic + '/zk/themes/default/fonts/simple-icons.min.css">');
document.write('<link rel="stylesheet" href="/' + ctxStatic + '/zk/themes/default/fonts/glyphicon-icons.min.css">');
document.write('<link rel="stylesheet" href="/' + ctxStatic + '/zk/themes/default/colors.css">');

document.write('<link rel="stylesheet" href="/' + ctxStatic + '/zk/common/zk_common.css">');
document.write('<link rel="stylesheet" href="/' + ctxStatic + '/zk/common/navbar/zk_navbar.css">');
document.write('<link rel="stylesheet" href="/' + ctxStatic + '/zk/common/dropdown/zk_dropdown.css">');
document.write('<link rel="stylesheet" href="/' + ctxStatic + '/zk/common/setting/zk_setting.css">');
document.write('<link rel="stylesheet" href="/' + ctxStatic + '/zk/common/slide/zk_slide.css">');
document.write('<link rel="stylesheet" href="/' + ctxStatic + '/zk/common/menu/zk_menu.css">');
document.write('<link rel="stylesheet" href="/' + ctxStatic + '/zk/common/tab/zk_tab.css">');
document.write('<link rel="stylesheet" href="/' + ctxStatic + '/zk/common/switch/zk_switch.css">');

document.write('<script src="/' + ctxStatic + '/zk/zk_tools.js"></script>');
document.write('<script src="/' + ctxStatic + '/zk/common/zk_common.js"></script>');
document.write('<script src="/' + ctxStatic + '/zk/common/zk_crypto.js"></script>');
document.write('<script src="/' + ctxStatic + '/zk/common/navbar/zk_navbar.js"></script> ');
document.write('<script src="/' + ctxStatic + '/zk/common/dropdown/zk_dropdown.js"></script> ');
document.write('<script src="/' + ctxStatic + '/zk/common/slide/zk_slide.js"></script> ');
document.write('<script src="/' + ctxStatic + '/zk/common/menu/zk_menu.js"></script> ');
document.write('<script src="/' + ctxStatic + '/zk/common/tab/zk_tab.js"></script> ');
document.write('<script src="/' + ctxStatic + '/zk/common/switch/zk_switch.js"></script> ');

document.write('<script src="/' + ctxStatic + '/zk/localization/localeData.js"></script>');
document.write('<script src="/' + ctxStatic + '/zk/localization/locale/msg_' + locale + '.js"></script>');



