/*
* @Author: Vinson
* @Date:   2020-01-31 22:12:38
* @Last Modified by:   Vinson
* @Last Modified time: 2020-02-29 22:14:12
* 
* 
* 
*/
/**
 * 下拉菜单，处理
 */
(function(_zk){

	/**
	 * 制作下拉菜单
	 * @return: void
	 */
	function f_makeDropdown(){

		// dropdown 节点 open 与 close
		// var dropdowns = $(".dropdown");

		var dropdowns = this.jqDom;
		if(dropdowns){
			dropdowns.each(function(index, item){
				// 鼠标移出
				_zk.event.binding(item, 'mouseout', function(){
					$(item).removeClass("zk_dropdown_menu_open");
				});

				// 鼠标在节点上
				_zk.event.binding(item, 'mouseover', function(){
					if(!$(item).hasClass("zk_dropdown_menu_open")){
						$(item).addClass("zk_dropdown_menu_open");
					}
				});
			})
		}
	}

	_zk.fn.extend({
		zkDropdown:function(){
			return f_makeDropdown.call(this);
		}
	});

})($zk);