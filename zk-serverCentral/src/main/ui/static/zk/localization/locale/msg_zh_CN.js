/*
* @Author: Vinson
* @Date:   2020-01-17 12:13:35
* @Last Modified by:   Vinson
* @Last Modified time: 2020-02-29 22:13:58
* 
* 
* 
*/
(function(_zkLocale){

	var locale = 'zh_CN';

	if(!_zkLocale.msg){
		_zkLocale.msg = {};
	}

	// if(!_zkLocale.msg[locale]){
	// 	_zkLocale.msg[locale] = {};
	// }

	var zhCNLocale = {
    // 提示框标题
    'zk.confirm.title.info':'信息',
    'zk.confirm.title.success':'成功',
    'zk.confirm.title.warning':'警告',
    'zk.confirm.title.error':'错误',
    'zk.confirm.title.confirm':'确认',
    // 操作名称
    'zk.opt.name.ok':'确认',
    'zk.opt.name.cancel':'取消',
    'zk.opt.name.on':'开',
    'zk.opt.name.off':'关',
    // 分页内容 
    'zk.page.beforePageText': '第',
    'zk.page.afterPageText': ' 页，共 {pages} 页',
    'zk.page.displayMsg': '当前显示第 {from} 到 {to} 条 共 {total} 条',
              
  };

  // $.extend(_zkLocale.msg[locale], zhCNLocale);
    
    $.extend(_zkLocale.msg, zhCNLocale);
})($zkLocale);