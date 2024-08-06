/*
* @Author: Vinson
* @Date:   2020-02-03 08:53:04
* @Last Modified by:   Vinson
* @Last Modified time: 2020-02-29 22:14:07
* 
* 
* 
*/
(function(_zk){

	var zkSlideConstant = {
		isOpen:'zkIsOpen',
		isPushTop: 'isPushTop'
	};
	// 禁止修改 zkFlag 对象，不能删除，新增，修改属性
	Object.freeze(zkSlideConstant);
	
	/** 
	 * 关闭或打开左边滑块 
	 * this 指向本 zk 节点的 jqDom 节点
	 */
	function f_onOrOffSlide(){

		var jqDom = this;
		var switchDom = $('.zk_slide_switch', jqDom);
		if(switchDom.length > 0){
			switchDom.each(function(index, item){
				_zk.event.binding(item, 'click', function(){
					if(jqDom.data(zkSlideConstant.isOpen) == false){
						// 打开滑块
						jqDom.data(zkSlideConstant.isOpen, true);
						jqDom.removeClass('zk_slide_close');
						jqDom.removeClass('zk_slide_collapse');
					}else{
						// 关闭滑块
						jqDom.data(zkSlideConstant.isOpen, false);
						jqDom.addClass('zk_slide_close');
						jqDom.addClass('zk_slide_collapse');
					}
				});
			});
		}
	}
	/** 
	 * 鼠标移入显示左边菜单；但不固定；
	 * this 指向本 zk 节点的 jqDom 节点
	 */
	function f_showSlideByMouseover(){
		var jqDom = this;
		jqDom.each(function(index, item){
			// 鼠标在节点上，左边菜单栏上悬停
			_zk.event.binding(item, 'mouseover', function(){
				// 如果菜单没有打开，才会触发这个操作
				if(jqDom.data(zkSlideConstant.isOpen) == false){
					jqDom.removeClass("zk_slide_collapse");
				}
			});
		});        
	}
	/** 
	 * 鼠标移出关闭左边滑块；但不固定；
	 * this 指向本 zk 节点的 jqDom 节点
	 */
	function f_hideSlideByMouseout(){
		var jqDom = this;
		jqDom.each(function(index, item){
			// 鼠标在节点上，左边菜单栏上悬停
			_zk.event.binding(item, 'mouseout', function(){
				// 如果菜单没有打开，才会触发这个操作
				if(jqDom.data(zkSlideConstant.isOpen) == false){
					jqDom.addClass("zk_slide_collapse");
				}
			});
		});   
	}

	_zk.fn.extend({
		zkSlide:function(){
			var _this = this;
			var jqDom = _this.jqDom;
			// 关闭或打开左边滑块
			f_onOrOffSlide.call(jqDom);
			// 鼠标移入显示左边菜单；但不固定；
			f_showSlideByMouseover.call(jqDom);
			// 鼠标移出关闭左边滑块；但不固定；
			f_hideSlideByMouseout.call(jqDom);

			$('.zk_push_top', jqDom).each(function(index, item){
				// 鼠标点击
				_zk.event.binding(item, 'click', function(){
					if(jqDom.data(zkSlideConstant.isPushTop) == true){
						// 打开头部
						$zk.zkShowMainHead()
						jqDom.data(zkSlideConstant.isPushTop, false);
					}else{
						// 隐藏头部
						$zk.zkHideMainHead();
						jqDom.data(zkSlideConstant.isPushTop, true);
					}
					// console.log("[^_^:20190917-1623-001] $(window).resize() --- ");
					// $(window).resize();
				});
			})

		}
	});
})($zk);


