/*
* @Author: Vinson
* @Date:   2020-01-17 12:13:47
* @Last Modified by:   Vinson
* @Last Modified time: 2020-02-29 22:13:59
* 
* 
* 
*/
(function(_zkLocale){

	var locale = 'en_US';

	if(!_zkLocale.msg){
		_zkLocale.msg = {};
	}

	// if(!_zkLocale.msg[locale]){
	// 	_zkLocale.msg[locale] = {};
	// }

	var enUSLocale = {
    // 提示框标题
    'zk.confirm.title.info':'Info',
    'zk.confirm.title.success':'Success',
    'zk.confirm.title.warning':'Warning',
    'zk.confirm.title.error':'Error',
    'zk.confirm.title.confirm':'Confirm',
    // 操作名称
    'zk.opt.name.ok':'Ok',
    'zk.opt.name.cancel':'Cancel',
    'zk.opt.name.on':'On',
    'zk.opt.name.off':'Off',
    // 分页内容 
    'zk.page.beforePageText': 'Page',
    'zk.page.afterPageText': 'of {pages}',
    'zk.page.displayMsg': 'Displaying {from} to {to} of {total} items',
    
  };

  // $.extend(_zkLocale.msg[locale], enUSLocale);
  $.extend(_zkLocale.msg, enUSLocale);

})($zkLocale);