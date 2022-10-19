/*
* @Author: Vinson
* @Date:   2020-02-14 18:04:54
* @Last Modified by:   Vinson
* @Last Modified time: 2020-03-08 13:48:10
* 
* 
* 
*/
(function(_zk){

	var zkMenuConstant = {
		data:'zk-menu-data',
		id:'zk-menu-id',
	};
	// 禁止修改 zkFlag对象，不能删除，新增，修改属性
	Object.freeze(zkMenuConstant);

	/**
	 * 是否是叶子节点；
	 * @menuData: 菜单树形数据;
	 * @return: true-是叶子菜单结点；false-非叶子菜单结点，此时有 child 子菜单数组；
	 */
	function f_isLeaf(menuData){

		if(menuData && menuData.child && menuData.child.length > 0){
			for(var cMenuData of menuData.child){
				// 只要有一个显示的子菜单节点，就不是叶子菜单节点
				if(f_isShow(cMenuData)){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 是否显示；
	 * @menuData: 菜单树形数据;
	 * @return: true-显示；false-不显示；
	 */
	function f_isShow(menuData){
		if(menuData && (menuData.isShow != undefined)){
			if(menuData.isShow === 0 || menuData.isShow === false || menuData.isShow === '0'){
				return false;
			}
		}
		return true;
	}

///////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////
	
	/* ********************************************************/
	/* 下面的函数 this 指针需要指向 $zk 对象 ************************/
	/* ********************************************************/
	
	/**
     * 打开菜单，级联打开；
     * @itemDom: 菜单节点的 Dom 对象；
     * @isLeaf: 是否是叶子菜单；true-是；false-不是；
     * @return: ovid
     */
    function f_openMenu(itemDom, isLeaf){
    	var _this = this;

    	if(isLeaf){ 
    		/** 叶子菜单节点  */
    		// 关闭所有菜单；取消所有激活菜单
    		$('ul[class~="zk_treeview_ul"] li', _this.jqDom).removeClass("zk_treeview_item_active zk_treeview_open zk_treeview_active");
    		// 激活此菜单
		    itemDom.addClass('zk_treeview_item_active');
		    // 打开并激活所有父菜单；
	    	var parentDoms = itemDom.parents('li[class~="zk_treeview_item"]');
	    	if(parentDoms){
	    		parentDoms.each(function(index, item){
	    			$(item).addClass("zk_treeview_open zk_treeview_active");
	    		})
	    	}
    	}else{
    		var isOpen = itemDom.hasClass('zk_treeview_open');
    		// 关闭所有菜单；
    		$('li[class~="zk_treeview_open"]', _this.jqDom).removeClass("zk_treeview_open");

    		/** 非 叶子菜单节点，即是需要展开的父节点菜单 */
    		if(isOpen){
    			// 如果已打开，关闭；在上面的关所有中已关闭，故不处理；
    		}else{
    			// 打开菜单
    			itemDom.addClass('zk_treeview_open');
    		}
    		// 打开父菜单；仅打开，不激活；
	    	var parentDoms = itemDom.parents('li[class~="zk_treeview_item"]');
	    	if(parentDoms){
	    		parentDoms.each(function(index, item){
	    			$(item).addClass("zk_treeview_open");
	    		})
	    	}
    	}
    }

	/**
     * 选择菜单；
     * @itemDom: 菜单节点的 Dom 对象；
     * @menuItemData: 菜单节点的数据对象；
     * @isLeaf: 是否是叶子菜单；true-是；false-不是；
     * del @onBeforeSelect: 选择前事件回调函数；返回 false 可以阻止选择；
     * del @onSelect: 选择事件回调函数；
     * @...args: 参数，传递给 onSelect 的参数；
     * @return: 无返回值；
     */
    function  f_select(itemDom, menuItemData, isLeaf, ...args){
    	var _this = this;
    	var sel = true;
    	// 1、触发选择菜单前: onBeforeSelect
		if(_this.paramObj.onBeforeSelect instanceof Function){
			sel = _this.paramObj.onBeforeSelect.call(itemDom, menuItemData);
		}

		if(sel != false){
			// 2、选择菜单
			f_openMenu.call(_this, itemDom, isLeaf);
			// 3、触发选择菜单后
			if(_this.paramObj.onSelect instanceof Function){
				_this.paramObj.onSelect.call(itemDom, menuItemData, ...args);
			}
		}
    }

	/**
	 * 制作菜单 Dom 节点；this 指向 $zk 节点
	 * @menuData: 菜单树形数据;
	 * @render: 函数；返回菜单标签；示例：<a href="" class="addTabPage" target = ""><i class="fa fa-fw icon-settings"></i><span>menuName</span></a>,
	 			this 指向 目标节点 <li>xxx</li>；
	 			第一个参数传出 菜单树形数据;
	 			第二参数标识是否是叶子节点，true-是叶子菜单结点；false-非叶子菜单结点
	 * @itemClickCallBack: 菜单节点点击回调函数：
	 			this 指向 目标节点 <li>xxx</li>；
	 			第一个参数传出 菜单树形数据;
	 			第二参数标识是否是叶子节点，true-是叶子菜单结点；false-非叶子菜单结点
	 * @return: 菜单 Dom 节点；
	 */
	function f_makeMenuDom(menuDatas, render){

		var _this = this;
		if(menuDatas.length < 1){
			return null;
		}
		var zkMenuDom = $('<ul class="zk_treeview_ul"></ul>');
		menuDatas.forEach(function(itemData){
			if(f_isShow(itemData)){
				// 菜单需要显示
				var itemDom = $('<li class="zk_treeview_item"></li>');
				itemDom.attr(zkMenuConstant.id, itemData.id);
				itemDom.data(zkMenuConstant.data, itemData);

				if(!f_isLeaf(itemData)){
					// 非叶子结点
					itemDom.addClass('zk_treeview');
					
					if(render instanceof Function){
						var itemLiChild = render.call(itemDom, itemData, false);
						itemLiChild.append($('<span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>'));
						itemDom.append(itemLiChild);
					}

					// 添加子菜单
					itemDom.append(f_makeMenuDom.call(_this, itemData.child, render));
					_zk.event.binding(itemDom[0], 'click', function(e){
						_zk.event.cancelPropagation(e);
						f_select.call(_this, itemDom, itemData, false);
			    	}, false);	

				}else{
					// 叶子结点
					if(render instanceof Function){
						var itemLiChild = render.call(itemDom, itemData, true);
						itemDom.append(itemLiChild);
					}
					_zk.event.binding(itemDom[0], 'click', function(e){
						_zk.event.cancelPropagation(e);
						f_select.call(_this, itemDom, itemData, true);
			    	}, false);	
				}
				zkMenuDom.append(itemDom);
			}

		});

    	return zkMenuDom;
    }

    function f_makeZkMenu(paramObj){
    	var _this = this;
    	paramObj = paramObj || {};
    	var menus = paramObj.menus || [];
    	var menuTargetDom = _this.jqDom;

    	menuDom = f_makeMenuDom.call(_this, menus, paramObj.render);
    	if(menuDom != null){
    		menuTargetDom.append(menuDom);
    	}

    	_this.extend({
    		menuDom:menuDom,
    		paramObj:paramObj,
    		// 选择菜单节点
    		select:function(menuId, ...args){
    			var targetMenuDom = $('li[' + zkMenuConstant.id + '="' + menuId + '"]', _this.menuDom);
    			if(targetMenuDom && targetMenuDom.length > 0){
    				var menuItemData = targetMenuDom.data(zkMenuConstant.data);
    				f_select.call(_this, targetMenuDom, menuItemData, f_isLeaf(menuItemData), ...args);
    				return true;
    			}
    			return false;
    		},
			// // 添加菜单
			// addMenu:function(menuDatas, parentMenuId, location){
			// 	// 
			// 	if(parentMenuId && parentMenuId != null && parentMenuId != ""){
			// 		// 添加到指定菜单下；
			// 		var targetMenuDom = $('li[' + zkMenuConstant.id + '="' + parentMenuId + '"]');
			// 		if(targetMenuDom && targetMenuDom.length > 0){
			// 			// 找到父菜单，如果父菜单不是叶子节点，直接添加，如果是叶子节点，
			// 		}else{
			// 			// 指定父节点不存在，添加到根菜单下；
			// 			if(console){
			// 				console.err("[>_<:20190920-1130-001] 未找到指定的父菜单进行添加，添加到根菜单下；");
			// 			}
			// 			menuDatas.forEach(function(item){
			// 				menuUl.append(f_makeLeftMenu(item, paramObj.render, f_menuItemClick));
			// 			})
			// 		}
			// 	}else{
			// 		// 添加根菜单
			// 		menuDatas.forEach(function(item){
			// 			menuUl.append(f_makeLeftMenu(item, paramObj.render, f_menuItemClick));
			// 		})
			// 	}
			// },
    		// // 删除菜单
    		// removeMenu:function(menuId){
    		// 	var targetMenuDom = $('li[' + zkMenuConstant.id + '="' + parentMenuId + '"]');
    		// 	if(targetMenuDom && targetMenuDom.length > 0){
    		// 		var menuItemData = targetLi.data(zkMenuConstant.data);
    		// 		// 1、触发删除菜单前；onBeforeRemove
	    	// 		// 2、删除菜单
	    	// 		// 3、触发删除菜单后
    		// 	}
    		// }
    	});

    	return _this;
    }

	/*
	参数 paramObj 说明：
	{
		menus: 必填；数组；菜单树形数据，包含一个唯一ID,
			{
				id: 唯一键,
				name: 国际化名称, {'zh_CN':'服务管理', 'en_US':'Server Manage'}
				iconType: icon 类型, iconType：0-css class；1-自定义图片；
				icon: 图标，'icon-trophy' 或 'zk/images/menu/certificate.png';
				url: 请求地址
				isShow: 是否显示
				child: [] 子菜单
			}
		render(menuData, isLeaf): 必填；函数；
			this: 指向 目标节点 <li>xxx</li>,
			@menuData: 第一个参数，为对应的菜单数据；
			@isLeaf: 第二个参数，标识是否是叶子节点，true-是叶子菜单结点；false-非叶子菜单结点
			@return:  返回导航标签；示例：<a href="" class="addTabPage" target = ""><i class="fa fa-fw icon-settings"></i><span>navName</span></a>,
		onBeforeSelect(menuData)：选填；函数；选择事情发生前
			this: 指向 目标节点 <li>xxx</li>
			@menuData: 第一个参数，为对应的菜单数据；
			@return: 返回 false 可以阻止 select;
		onSelect(menuData)：选填；函数；选择事情发生后
			this 指向 目标节点 <li>xxx</li>
			@menuData: 第一个参数，为对应的菜单数据；
			@return: void
	}

	返回对象 zkMenu 扩展说明：
	{
		select(menuId, ...args): 选择某个菜单 Item 节点,
			@menuId: 为菜单数据ID；
			@...args: 参数，传递给 onSelect 的参数；
			@return: boolean, true-找到了对应菜单；false-未找到对应菜单；	
	}
	*/

	$zk.fn.extend({
		zkMenu:function(paramObj){
			return f_makeZkMenu.call(this, paramObj);
		}
	});

})($zk);



