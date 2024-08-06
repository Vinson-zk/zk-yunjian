/*
* @Author: Vinson
* @Date:   2020-01-30 22:12:00
* @Last Modified by:   Vinson
* @Last Modified time: 2020-03-08 13:13:19
* 
* 
* 
*/
(function(_zk){

	var zkNavConstant = {
		data:'zk-nav-data',
		id:'zk-nav-id',
	};
	// 禁止修改 zkFlag 对象，不能删除，新增，修改属性
	Object.freeze(zkNavConstant);
	
	/**
	 * 制作导航栏目 Dom 节点；
	 * @navItemData: 民航栏目数据
	 * @render: 返回导航标签；示例：<a href="" class="addTabPage" target = ""><i class="fa fa-fw icon-settings"></i><span>navName</span></a>,
	 		this this 指向 目标节点 <li>xxx</li>
	 		第一个参数，为对应的导航数据
	 * @return: 导航栏目 Dom 节点；
	 */
	function f_makeNavItem(navItemData, render){

		var itemDom = $('<li></li>');
		// <a href="" data-href="blank" class="addTabPage" target = "home" ></a>

       	itemDom.data(zkNavConstant.data, navItemData);
	    itemDom.attr(zkNavConstant.id, navItemData.id);

	    if(render instanceof Function){
	    	var itemA = render.call(itemDom, navItemData);
	    	itemDom.append(itemA);
	    }else{
	    	itemDom.append("_blank");
	    }
		return itemDom;
	}

	/**
	 * 选择 导航栏；this 指针，指向本 导航栏 节点
	 * @navItemData: 民航栏目数据
	 * @onBeforeSelect: 选择前事件
	 * @onSelect: 选择事件
	 * @return
	 */
	 function f_selectNavBar(navItemData, onBeforeSelect, onSelect, ...args){

	 	var targetLi = $(this);

		// 处理 nav item 选中状态
		$('li', targetLi.parent()).removeClass('zk_navbar_item_activate');
    	targetLi.addClass('zk_navbar_item_activate');

    	var sel = true;
		if(onBeforeSelect instanceof Function){
			sel =  onBeforeSelect.call(targetLi, navItemData);
		}
		if(sel != false){
			if(onSelect instanceof Function){
				onSelect.call(targetLi, navItemData, ...args);
			}
		}
	 }

	/**
	 * 制作导航栏
	 * @paramObj:
	 * @return: zkNavBar
	 */
	function f_makeNavBar(paramObj){
		var _this = this;

		paramObj = paramObj || {};

		var navBarDiv = _this.jqDom;
		var navBarUl = $('<ul></ul>');
		navBarDiv.append(navBarUl);

        _this.extend({
        	// paramObj:paramObj,
        	navBarUl:navBarUl,
        	// 选择某个导航Item 节点,
        	select:function(navItemId, ...args){
        		var targetLi = $('li[' + zkNavConstant.id + '="' + navItemId + '"]', _this.navBarUl);
        		if(targetLi && targetLi.length > 0){
        			var navItemData = targetLi.data(zkNavConstant.data);
        			f_selectNavBar.call(targetLi, navItemData, paramObj.onBeforeSelect, paramObj.onSelect, ...args);
        			return true;
        		}
        		return false;
        	},
        	// 添加一个导航Item 节点,
        	addNavItem:function(navItemData){
        		var navItem = f_makeNavItem(navItemData, paramObj.render);
	        	_zk.event.binding(navItem[0], 'click', function(e){
	        		_zk.event.cancelPropagation(e);
	        		f_selectNavBar.call(navItem, navItemData, paramObj.onBeforeSelect, paramObj.onSelect);
		        }, false);
	        	navBarUl.append(navItem);
        	},
        	// 删除一个导航Item 节点,
        	removeNavItem:function(navItemId){
        		var targetLi = $('li[' + zkNavConstant.id + '="' + navItemId + '"]', _this.navBarUl);
        		if(targetLi && targetLi.length > 0){
        			var navItemData = targetLi.data(zkNavConstant.data);
        			var sel = true;
        			if(paramObj.onBeforeRemove instanceof Function){
        				sel = paramObj.onBeforeRemove.call(targetLi, navItemData);
        			}
        			if(sel != false){
        				targetLi.remove();
	        			if(paramObj.onRemove instanceof Function){
	        				paramObj.onRemove.call(targetLi, navItemData);
	        			}
        			}
        			return navItemData;
        		}
        		return null;
        	}
        })

        for(var navItemData of paramObj.navDatas){
        	_this.addNavItem(navItemData);
        }

        return _this;
	}

/*
参数 paramObj 说明：
{
	navDatas: 必填；数组；导航数据，包含一个唯一ID,
	render(navItemData): 必填；函数；
		this: 指向 目标节点 <li>xxx</li>,
		@navItemData: 第一个参数，为对应的导航数据；
		@return:  返回导航标签；示例：<a href="" class="addTabPage" target = ""><i class="fa fa-fw icon-settings"></i><span>navName</span></a>,
	onBeforeSelect(navItemData)：选填；函数；选择事情发生前
		this: 指向 目标节点 <li>xxx</li>
		@navItemData: 第一个参数，为对应的导航数据；
		@return: 返回 false 可以阻止 select;
	onSelect(navItemData, ...args)：选填；函数；选择事情发生后
		this 指向 目标节点 <li>xxx</li>
		@navItemData: 第一个参数，为对应的导航数据；
		@...args: 参数，select 传递的参数；
		@return: void
	onBeforeRemove(navItemData)：选填；函数；删除民航Item 节点发生前,
		this: 指向 目标节点 <li>xxx</li>
		@navItemData: 第一个参数，为对应的导航数据；
		@return: 返回 false 可以阻止 remove;
	onRemove(navItemData)：选填；函数；删除民航Item 节点发生后,
		this: 指向 目标节点 <li>xxx</li>
		@navItemData: 第一个参数，为对应的导航数据,
		@return: void
}

返回对象 zkNavBar 扩展说明：
{
	select(navItemId, ...args): 选择某个导航Item 节点,
		@navItemId: 为导航数据ID；
		@...args: 参数，传给 onSelect 的参数；
		@return: boolean, true-找到了对应导航；false-未找到对应导航；
	addNavItem(navItemData): 添加一个导航Item 节点,
		@navItemData: 为导航数据；
		@return: void
	removeNavItem(navItemId): 删除一个导航Item 节点,
		@navItemData: 为导航数据；
		@return: 为导航数据
		
}
*/
	_zk.fn.extend({
		zkNavBar:function(paramObj){
			return f_makeNavBar.call(this, paramObj);
		}
	});

})($zk);


