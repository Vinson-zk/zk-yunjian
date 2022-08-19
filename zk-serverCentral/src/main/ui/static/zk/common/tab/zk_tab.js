/*
* @Author: Vinson
* @Date:   2020-02-21 11:15:37
* @Last Modified by:   Vinson
* @Last Modified time: 2020-03-08 14:58:37
* 
* 
* 
*/
(function(_zk){

	var zkTabConstant = {
		tabId:"zk-tab-id",
		data:"zk-tab-data",
	}
	// 禁止修改 zkFlag对象，不能删除，新增，修改属性
	Object.freeze(zkTabConstant);

	/**
	 * 取 dom 左边的距离宽度; 
	 * 不需要 this 指针;
	 * @jqDom: jquery dom 对象；
	 * @return: 返回距离
	 */
	function f_getDomLeftWidth(jqDom){
		var mf = parseInt(jqDom.css("margin-left"))||0;
		var pf = parseInt(jqDom.css("padding-left"))||0;
		var bf = parseInt(jqDom.css("border-left"))||0;
		return mf + pf + bf;
	}
	/**
	 * 取 dom 左边的距离宽度; 
	 * 不需要 this 指针;
	 * @jqDom: jquery dom 对象；
	 * @return: 返回距离
	 */
	function f_getDomRightWidth(jqDom){
		var mr = parseInt(jqDom.css("margin-right"))||0;
		var pr = parseInt(jqDom.css("padding-right"))||0;
		var br = parseInt(jqDom.css("border-right"))||0;
		return mr + pr + br;
	}
	/**
	 * 取 tab title item 长度; 
	 * 不需要 this 指针;
	 * @jqDom: jquery dom 对象；
	 * @return: 返回长度；
	 */
	function f_getTitleItemLength(jqDom){
		var le = parseInt(jqDom.width())||0;
		le = le + f_getDomLeftWidth(jqDom) + f_getDomRightWidth(jqDom);
		return le||0;
	}

	/**
	 * 移动 tab title 滚动条; 
	 * 不需要 this 指针;
	 * @flag: 表示左移还是右移： 小于0 - 左移一格；大于0 - 右移一格。
	 * @tabTitleDom: tab title 容器 dom jquery 节点对象
	 */
	function f_tabTitleScrollMove(flag, tabTitleDom){
		flag = flag || -1;

		var titleScrollDom = $('.zk_tab_title_content', tabTitleDom);
		// 取第一个 Tab item title 的长度；
		var l = f_getTitleItemLength($('.zk_tab_title_item', titleScrollDom));
		if(l == 0 ){
			return;
		}

		// 目标滚动条位置，初始为当前的滚动条位置
		var tScrollLeft = titleScrollDom[0].scrollLeft;

		if(flag < 0){ // 左移
			// 滚去量左边余数，不为 0 表示正好有 tab title item 停在左边边界上。
			var remainder = tScrollLeft%l;
			if(remainder > 0){
				tScrollLeft -= remainder;
			}else{
				tScrollLeft -= l;
			}
			// 判断滚动条是否移动到了左边边界，小0时，表示滚去条已经移动了左边边界
			if(tScrollLeft < 0){
				tScrollLeft = 0;
				// 禁用左边滚去条；在方法最后统一处理；
			}
		}else{
			// 滚动条长度
			var scrollWidth = titleScrollDom[0].scrollWidth; // jqDom 没有 scrollWidth() 函数
			var width = titleScrollDom[0].offsetWidth; // 节点宽度
			// 滚去量右边余数，不为 0 表示正好有 tab title item 停在右边边界上。
			var remainder = (tScrollLeft + width)%l;
			if(remainder > 0){
				tScrollLeft -= remainder;
			}

			tScrollLeft += l;
			// 判断是否滚动条是否移动到了右边边界，滚去位置大于滚去条总宽度与可见宽度差时，表示滚去条已经移动了右边边界
			if(scrollWidth - width < tScrollLeft){
				tScrollLeft = scrollWidth - width;
				// 禁用右边滚去条；在方法最后统一处理；
			}
		}
		titleScrollDom[0].scrollLeft = tScrollLeft;
		// 重置滚去条是否可见，可用
		f_resetTabTitleScroll(tabTitleDom);
	}

	/**
	 * 移动指定 tab title item 到显示；在可见区域时，不处理
	 * 不需要 this 指针;
	 * @tabTitleDom: tab title 容器 dom jquery 节点对象；
	 * @titleIItemDom: tab item 节点 jquery 对象；
	 */
	function f_moveTabTitleItemToSee(tabTitleDom, titleIItemDom){
		var _this = this;
		// 取第一个 Tab item title 的长度；
		var l = f_getTitleItemLength(titleIItemDom);
		if(l == 0 ){
			return;
		}
		// 目标 item 本身偏离位置；
		var offsetLeft = titleIItemDom[0].offsetLeft || 0;

		// 滚动节点
		var titleScrollDom = $('.zk_tab_title_content', tabTitleDom);
		// 目标滚动位置，初始为当前的滚去位置
		var tScrollLeft = titleScrollDom[0].scrollLeft;

		// // 第一个 tab title item 偏移量
		// /**
		//  * 取 dom 下，第一个子节点，偏移位置; 不需要 this 指向 tab 对象；
		//  * @jqDom: jquery dom 对象；
		//  * @return: 返回长度；
		//  */
		// function f_getFirstChildLocation(jqDom){
		// 	var location = parseInt(jqDom.children(":eq(0)")[0].offsetLeft)||0;
		// 	return location;
		// }
		// var firstOffsetLeft = f_getFirstChildLocation(tabTitleDom);
		// offsetLeft = offsetLeft - firstOffsetLeft;

		if(offsetLeft - l < tScrollLeft){
			// 目标 item 在当前滚动位置的左边
			tScrollLeft = offsetLeft;
			if(tScrollLeft < 0){
				tScrollLeft = 0;
			}
			titleScrollDom[0].scrollLeft = tScrollLeft;
			// 重置滚去条是否可见，可用
			f_resetTabTitleScroll(tabTitleDom);
		}else{
			// 滚动条长度
			var scrollWidth = titleScrollDom[0].scrollWidth;
			// 可见宽度
			var width = titleScrollDom.width();

			if(offsetLeft + l > tScrollLeft +  width){
				tScrollLeft = offsetLeft - width + l;
				if(scrollWidth - width < tScrollLeft){
					tScrollLeft = scrollWidth - width;
				}
				titleScrollDom[0].scrollLeft = tScrollLeft;
				// 重置滚去条是否可见，可用
				f_resetTabTitleScroll(tabTitleDom);
			}else{
				// 目标 item 在可见区域中，不需要移动。
			}
		}
	}

	/**
	 * 重置 title 滚动条[移动区域]的大小; 
	 * 不需要 this 指针;
	 * @tabTitleDom: tab title 容器 dom jquery 节点对象
	 */
	function f_resetTabTitleContentWidth(tabTitleDom){
		var _this = this;
		var titleScrollDom = $('.zk_tab_title_content', tabTitleDom);
		var ul = $('ul', titleScrollDom);
		var childs = ul.children();
		var itemLength = f_getTitleItemLength(childs);
		ul.width(itemLength*childs.length);
		// 重置滚去条是否可见，可用
		f_resetTabTitleScroll(tabTitleDom);
	}

	/**
	 * 重置 title 滚动条；自动判断是否需要显示; 
	 * 不需要 this 指针;
	 * @tabTitleDom: tab title 容器 dom jquery 节点对象
	 */
	function f_resetTabTitleScroll(tabTitleDom){

		var titleScrollDom = $('.zk_tab_title_content', tabTitleDom);
		// 滚动条长度
		var scrollWidth = titleScrollDom[0].scrollWidth; // jqDom 没有 scrollWidth() 函数
		var width = titleScrollDom[0].offsetWidth; // 节点宽度

		if(scrollWidth > width){
			// 需要显示滚去条
			tabTitleDom.addClass('zk_tab_title_scroll_active');
			// 当前滚动条位置
			var scrollLeft = titleScrollDom[0].scrollLeft;
			var scrollLeftDom = $('.zk_tab_title_scroll_left', tabTitleDom);
			var scrollRightDom = $('.zk_tab_title_scroll_right', tabTitleDom);
			if(scrollLeft > 0){
				if(scrollWidth > (width + scrollLeft)){
					// 两边滚去条都可用
					scrollLeftDom.removeClass('zk_tab_title_scroll_disabled');
					scrollLeftDom.removeAttr("disabled");
					scrollRightDom.removeClass('zk_tab_title_scroll_disabled');
					scrollRightDom.removeAttr("disabled");
				}else{
					// 已滚动到最右边
					// 启用左边
					scrollLeftDom.removeClass('zk_tab_title_scroll_disabled');
					scrollLeftDom.removeAttr("disabled");
					// 禁用右边
					scrollRightDom.addClass('zk_tab_title_scroll_disabled');
					scrollRightDom.attr("disabled", true);
				}
			}else{
				// 已滚动到最左边
				// 启用右边
				scrollRightDom.removeClass('zk_tab_title_scroll_disabled');
				scrollRightDom.removeAttr("disabled");
				// 禁用左边
				scrollLeftDom.addClass('zk_tab_title_scroll_disabled');
				scrollLeftDom.attr("disabled", true);
			}
			tabTitleDom.addClass('zk_tab_title_scroll_active');
		}else{
			tabTitleDom.removeClass('zk_tab_title_scroll_active');
		}
	}

	/**
	 * 制作右键显示菜单；
	 * 不需要 this 指针
	 * @menuClickFun: 菜单操作时回调函数，并传出 菜单 key
	 */
	function f_makeTabRightMenuDom(closeMenuNames, menuClickFun){
		
		var tabRightMenuDom = $(['<div class="zk_tab_right_menu">',  
			'    <div >',  
			'        <ul class="ico">',  
			'            <li class="zk_tab_right_menu_item" menukey="k_close_current" >' + closeMenuNames['close_current'] + '</li>',   // 关闭当前
			'            <li class="zk_tab_right_menu_item" menukey="k_close_other" >' + closeMenuNames['close_other'] + '</li>',       // 关闭其他
			'            <li class="zk_tab_right_menu_item" menukey="k_close_all" >' + closeMenuNames['close_all'] + '</li>',           // 关闭所有
			'            <li class="zk_tab_right_menu_item" menukey="k_close_left" >' + closeMenuNames['close_left'] + '</li>',         // 关闭左侧
			'            <li class="zk_tab_right_menu_item" menukey="k_close_right" >' + closeMenuNames['close_right'] + '</li>',       // 关闭右侧
			'        </ul>',  
			'    </div>',  
			'    <div></div>',  
			'</div>'].join('')
		);

		tabRightMenuDom.find("li").bind('click', function(e){
			if(menuClickFun instanceof Function){
				menuClickFun.call($(this), $(this).attr("menukey"), e);
			}
			return false;
    	});

		// 鼠标移出隐藏
		_zk.event.binding(tabRightMenuDom[0], 'mouseleave', function(e){
			tabRightMenuDom.css("display", "none");
    	}, false);

		return tabRightMenuDom;
	}

	/**
	 * 显示右键菜单；
	 * 不需要 this 指针
	 * @e: 事件对象
	 * @tabRightMenuDom: 右键菜单节点对象
	 * @tabPanel: tab 容器节点对象
	 * @tabItemData: tabItem 数据对象；{id: url: }
	 * @isClose: 是不洗澡可以关闭；true 是；false 允许关闭；
	 */
	function f_showTabRightMenuDom(e, tabRightMenuDom, tabPanel, tabItemData, isClose){
		var rightMenuLocationPoint = f_resolveLocation(tabPanel, e, tabRightMenuDom);
		tabRightMenuDom[0].style.left = rightMenuLocationPoint.x + 'px';
		tabRightMenuDom[0].style.top = rightMenuLocationPoint.y + 'px';
		tabRightMenuDom.css("display", "block");
		tabRightMenuDom.data(zkTabConstant.tabId, tabItemData.id);
		if(!isClose){
			// 不允许关闭，则禁用掉关闭当前选项；
			$('li[menukey="k_close_current"]', tabRightMenuDom).addClass('zk_tab_right_menu_item_disabled');
		}else{
			$('li[menukey="k_close_current"]', tabRightMenuDom).removeClass('zk_tab_right_menu_item_disabled');
		}
	}

	/**
     * 计划鼠标在指定窗口中的相对位置; 
     * 不需要 this；
     * @parentPanel: 相对的窗口; 为空时，以 body 为相对窗口；
     * @e: 事件
     * @jqDom: jqDom 目标对象
     */
    function f_resolveLocation(parentPanel, e, jqDom){
		var point = {x:e.clientX, y:e.clientY};
		// point = {x:e.offsetX, y:e.offsetY};

		// console.log("[^_^:20191008-1219-001] point: ", point);
		if(parentPanel.length < 1){
			parentPanel = $("body");
		}
		var offsetLeft = parentPanel[0].offsetLeft;
		var offsetTop = parentPanel[0].offsetTop;
		
		point.x -= offsetLeft;
		point.y -= offsetTop;
		var panalWidth = parentPanel.width();
		var width = jqDom.width();
		
		var panalHeight = parentPanel.height();
		var height = jqDom.height();
		
		if(point.x + width > panalWidth){
			if(point.x - width >= 0){
				point.x -= width
			}
		}
		
		if(point.y + height > panalHeight){
			if(point.y - height >= 0){
				point.y -= height
			}
		}		
		return point;
	}

	/**
	 * 查找最近或最晚操作过的 tab Item;
	 * @tabItems: tabItem 数组；
	 * @falg: 0-查找最近操作过的 tabItem; 其他-查找最晚操作过的 tabItem; 默认为 0;
	 * @filterItems: 需要过滤 tabItem 对象；
	 */
	function f_getFirstOrLastTabItem(tabItems, flag=0, filterItems){

		filterItems = filterItems || [];

		if(tabItems.length < 1){
			return null;
		}
	
		var targetTabItem = null;
		for(var item of tabItems){
			for(var filterItem of filterItems){
				if(filterItem.itemData.id === item.itemData.id){
					// 需要过滤掉这个 tabItem;
					item = null;
					break;
				}
			}
			if(item == null){
				// item 为需要过虑的，不参与到筛选的 tab item
				continue;
			}

			if(targetTabItem == null){
				targetTabItem = item;
			}else{
				if(flag == 0){
					// 查找最近操作过的 tabItem
					if(item.updateDate > targetTabItem.updateDate){
						targetTabItem = item;
					}
				}else{
					// 查找最晚操作过的 tabItem
					if(item.updateDate < targetTabItem.updateDate){
						targetTabItem = item;
					}
				}
			}
		}
		return targetTabItem;
	}

	/**
	 *
	 */
	function f_getCloseMenuNames(locale){
		var closeMenuNames = {};
		if('zh_CN' == locale){
			closeMenuNames['close_current'] = '关闭当前';
			closeMenuNames['close_other'] = '关闭其他';
			closeMenuNames['close_all'] = '关闭所有';
			closeMenuNames['close_left'] = '关闭左边';
			closeMenuNames['close_right'] = '关闭右边';
		}else{
			closeMenuNames['close_current'] = 'Close Current';
			closeMenuNames['close_other'] = 'Close Other';
			closeMenuNames['close_all'] = 'Close All';
			closeMenuNames['close_left'] = 'Close Left';
			closeMenuNames['close_right'] = 'Close Right';
		}
		return closeMenuNames;
	}

	var zkTabConstant = {
		tabId:"zk-tab-id",
		data:"zk-tab-data",
	}
	// 禁止修改 zkFlag对象，不能删除，新增，修改属性
	Object.freeze(zkTabConstant);

	/**************************************************************/
	/** zk tab 方法函数，this 指针，需要指向 $zk 节点对象 */
	/**************************************************************/

	/**
	 * 创建 zkTab 
	 * this 指针指向 $zk 对象
	 */
	function f_createzkTab(paramObj){
		var _this = this;
		paramObj = paramObj || {};

		// 添加默认 tab content 以 itemData url 添加 iframe 函数 
		paramObj.contentRender = paramObj.contentRender || function(itemData){
			var iframe = $('<iframe id="" name="" src="" frameborder="0"></iframe>');
			iframe.attr("id", itemData.id);
			iframe.attr("name", itemData.id);
			iframe.attr("src", itemData.url);
			return iframe;
		};

		/*** tab panel 容器 jquery 节点 */
		var tabPanelDom = _this.jqDom;
		tabPanelDom.addClass('zk_tab_panel');
		/*** tab 标题 jquery 节点 */
		var tabTitleDom = $([
				'<div class="zk_tab_title">', 
				'	<div class="zk_tab_title_scroll zk_tab_title_scroll_left"></div>', 
				'	<div class="zk_tab_title_scroll zk_tab_title_scroll_right"></div>', 
				'	<div class="zk_tab_title_content"><ul></ul></div>', 
				'	<div class="zk_tab_spacer"></div>', 
				'</div>', 
			].join(''));
		// scroll 操作事件
		$('.zk_tab_title_scroll_left', tabTitleDom).bind('click', function(){
			// 左移
			f_tabTitleScrollMove(-1, tabTitleDom);
		});

		$('.zk_tab_title_scroll_right', tabTitleDom).bind('click', function(){
			// 右移
			f_tabTitleScrollMove(1, tabTitleDom);
		});

		/*** tab 内容 jquery 节点 */
		var tabContentDom = $('<div class="ba_tab_content"><div class="zk_tab_spacer"></div></div>');
		/*** 右键菜单制作 */
		var closeMenuNames = f_getCloseMenuNames(_zk.getLocale());
		closeMenuNames = paramObj.closeMenuNames || closeMenuNames;
		// 右键菜单处理函数
		var fClickTabRightMenuDom = paramObj.clickTabRightMenuDom || f_defaultClickTabRightMenuDom;
		var tabRightMenuDom = f_makeTabRightMenuDom(closeMenuNames, function(menukey, e){
			f_defaultClickTabRightMenuDom.call(_this, menukey, e);
			tabRightMenuDom.css("display", "none");
		});

		// 将 tab title, tab content, tab right menu 都添加到 页面中；
		tabPanelDom.append(tabTitleDom);
		tabPanelDom.append(tabContentDom);
		tabPanelDom.append(tabRightMenuDom);

		// 重置 title 滚动条[移动区域]的大小; 
		f_resetTabTitleContentWidth(tabTitleDom);
		// 浏览器窗口大小改变时
		$(window).resize(function(){
			// 重置 title 滚动条；自动判断是否需要显示; 
		    f_resetTabTitleScroll(tabTitleDom);
		    if(_this.currentSelectedTabId){
		    	_this.select(_this.currentSelectedTabId);
		    }

		});

		_this.extend({
			/** tabId 与 tabItetm 映射对象；
			 * @tabItem: {itemData: 数据对象, titleItemDom: title 节点对象, contentItemDom: item 内容节点对象, updateDate: 更新时间, isClose:是否可以关闭} 
			 */
			tabMapping: {}, 
			tabItems:[],          // 所有 tabItem 对象；
			allowCloseTabItems:[], // 允许关闭的 tabItem 对象；
			tabPanelDom: tabPanelDom,          // tab 容器节点对象
			tabTitleDom: tabTitleDom,          // tab title 容器节点对象
			tabContentDom: tabContentDom,      // tab 内容容器节点对象
			tabRightMenuDom: tabRightMenuDom,  // tab 右键菜单节点对象

			maxTabCount: paramObj.maxTabCount || 0,  // 最多可打开的 tab 数量，不允许关闭的 tab 不在计数范围内；小于等于0，不限制。
			currentSelectedTabId:undefined,          // 当前选择的 tab Item Id
			// 添加一个 tab
			addTab: function(tabItemData, isClose){
				if(isClose != false){
					isClose = true;
				}
				return f_addTabItem.call(_this, tabItemData, isClose, paramObj.titleRender, paramObj.contentRender,
					paramObj.onBeforeSelect, paramObj.onSelect, paramObj.onBeforeClose, paramObj.onClose);
			},
			// 选择指定 tabItem
			select:function(tabId){
				var tabItem = _this.tabMapping[tabId];
				if(tabItem){
					return f_seletedTabItem.call(_this, paramObj.onBeforeSelect, paramObj.onSelect);
				}else{
					if(console){
						console.error('[>_<:20200222-2055-002] 选择 tab 页失败; tabId: ', tabId);
					}
				}
				return null;
			},
			// 关闭指定的 tabItem, 等价于删除一个 tab Item
			close:function(tabId){
				var tabItems = [];
				if(tabId == null){
					tabItems = this.allowCloseTabItems;
				}else{
					if(Array.isArray(tabId)){
						tabId.forEach(function(currentValue, index, array){
							if(_this.tabMapping[currentValue]){
								tabItems.push(_this.tabMapping[currentValue]);
							}
						});
					}else{
						if(_this.tabMapping[tabId]){
							tabItems.push(_this.tabMapping[tabId]);
						}
					}
				}
				return f_closeTabItem.call(_this, tabItems, false, paramObj.onBeforeClose, paramObj.onClose, paramObj.onBeforeSelect, paramObj.onSelect);
			}
		});
		return _this;
	}

	/**
	 * 右键菜单处理；
	 * this 指针，指向 $zk 节点对象
	 * @menukey: k_close_current-关闭当前；k_close_other-关闭其他；k_close_all-关闭所有；k_close_left-关闭左边；k_close_right-关闭右边；
	 * @zkTab: 
	 */
	function f_defaultClickTabRightMenuDom(menukey){
		var _this = this;
		var tabItemId = _this.tabRightMenuDom.data(zkTabConstant.tabId);
		if(tabItemId){
			switch(menukey){
				case 'k_close_current':
					_this.close(tabItemId); 
					break;  // 关闭当前
				case 'k_close_other': // 关闭其他
					var tabItems = $('li[' + zkTabConstant.tabId + '!=' + tabItemId +  ']', _this.tabTitleDom);
					var closeTabItemIds = [];
					tabItems.each(function(index, currentItem){
						var tabItemId = $(currentItem).attr(zkTabConstant.tabId);
						closeTabItemIds.push(tabItemId);
					});
					_this.close(closeTabItemIds); 
					break; 
				case 'k_close_all': 
					_this.close(null); 
					break;       // 关闭所有
				case 'k_close_left':  // 关闭左边
					var tabItems = $('li[' + zkTabConstant.tabId + '=' + tabItemId +  ']', _this.tabTitleDom);
					tabItems = $('li:lt(' + tabItems.index() + ')', _this.tabTitleDom);
					var closeTabItemIds = [];
					tabItems.each(function(index, currentItem){
						var tabItemId = $(currentItem).attr(zkTabConstant.tabId);
						closeTabItemIds.push(tabItemId);
					});
					_this.close(closeTabItemIds); 
					break; 
				case 'k_close_right':  // 关闭右边
					var tabItems = $('li[' + zkTabConstant.tabId + '=' + tabItemId +  ']', _this.tabTitleDom);
					tabItems = $('li:gt(' + tabItems.index() + ')', _this.tabTitleDom);
					var closeTabItemIds = [];
					tabItems.each(function(index, currentItem){
						var tabItemId = $(currentItem).attr(zkTabConstant.tabId);
						closeTabItemIds.push(tabItemId);
					});
					_this.close(closeTabItemIds); 
					break;
				default: 
					if(console){
						console.error("[>_<:20191009-1049-002] 此右键菜单 menukey 没有对应处理方案 "); 
					}
					break;
			}
		}else{
			if(console){
				console.error("[>_<:20191009-1049-001] 未找到操作的 tabItemId ");
			}
		}
	}

	/**
	 * 创建一个新的 Tab Item
	 * this 指针，指向 $zk 节点对象
	 * @tabItemData: {id: url: }
	 * @isClose: 是不洗澡可以关闭；true 是；false 允许关闭；
	 * @titleRender: title 内容返回回调；回调 this 指针指向 tabTitleItemDom 节点，tabItemData 做为参数，函数返回 jquery 节点；返回示例：<i class="fa icon-fire"></i><span>Test</span>
	 * @contentRender: content 内容返回回调；回调 this 指针指向 tabContentItemDom 节点，tabItemData 做为参数，函数返回 jquery 节点；
	 * @onBeforeSelect: 见 f_seletedTabItem 函数；
	 * @onSelect: 见 f_seletedTabItem 函数；
	 * @onBeforeClose: 见 f_closeTabItem 函数；
	 * @onClose: 见 f_closeTabItem 函数；
	 * @return: tabItem 对象；{itemData: 数据对象, titleItemDom: title 节点对象, contentItemDom: item 内容节点对象, updateDate: 更新时间, isClose:是否可以关闭} 
	 */
	function f_makeTabItem(tabItemData, isClose, titleRender, contentRender, onBeforeSelect, onSelect, onBeforeClose, onClose){
		var _this = this;

		var tabTitleItemDom = $('<li ' + zkTabConstant.tabId + '="' + tabItemData.id + '" class="zk_tab_title_item"></li>');
		if(titleRender instanceof Function){
			tabTitleItemDom.append($('<div></div>').append(titleRender.call(tabTitleItemDom, tabItemData)));
		}
		tabTitleItemDom.append($('<div class="zk_tab_title_item_refresher"></div>'));
		if(isClose){
			tabTitleItemDom.append($('<div class="zk_tab_title_item_close"></div>'));
		}
		// tabTitle Item 添加到 页面中
		$('ul', _this.tabTitleDom).append(tabTitleItemDom);

		var tabContentItemDom = $('<div class="ba_tab_content_item zk_display_none" ></div>');
		tabContentItemDom.attr(zkTabConstant.tabId, tabItemData.id);
		tabContentItemDom.append(contentRender(tabItemData));
		// tabContent Item 添加到 页面中
		_this.tabContentDom.prepend(tabContentItemDom);

		// 重置 tab title 宽度
		f_resetTabTitleContentWidth(_this.tabTitleDom);

		// tabItem: {itemData: 数据对象, titleItemDom: title 节点对象, contentItemDom: item 内容节点对象, updateDate: 更新时间, isClose:是否可以关闭} 
		var tabItem = { itemData:tabItemData, titleItemDom: tabTitleItemDom, contentItemDom: tabContentItemDom, updateDate:new Date(), isClose:isClose };

		/*** tab item 事件处理 */
		// 添加 单击 click 事件
		tabTitleItemDom.bind('click', function(e){
			// var tabId = $(this).attr(zkTabConstant.tabId);
			// var tabContentItemDom = $('.ba_tab_content_item[' + zkTabConstant.tabId + '=' + tabId + ']', tabContentDom);
			f_seletedTabItem.call(_this, tabItem, onBeforeSelect, onSelect);
		});

		// 添加 右键 事件
		tabTitleItemDom.bind('contextmenu', function(e){
			f_showTabRightMenuDom(e, _this.tabRightMenuDom, _this.tabPanelDom, tabItemData, isClose);
			return false;
		});

		// 添加 关闭事件
		if(isClose){
			$(".zk_tab_title_item_close", tabTitleItemDom).bind('click', function(){
				f_closeTabItem.call(_this, [tabItem], false, onBeforeClose, onClose);
			});
		}

		return tabItem;
	}

	/**
	 * 添加一个 tab
	 * this 指针，指向 $zk 节点对象
	 * @titleRender: title 内容返回回调；回调 this 指针指向 tabTitleItemDom 节点，tabItemData 做为参数，函数返回 jquery 节点；返回示例：<i class="fa icon-fire"></i><span>Test</span>
	 * @contentRender: content 内容返回回调；回调 this 指针指向 tabContentItemDom 节点，tabItemData 做为参数，函数返回 jquery 节点；
	 * @onBeforeSelect: 见 f_seletedTabItem 函数；
	 * @onSelect: 见 f_seletedTabItem 函数；
	 * @onBeforeClose: 见 f_closeTabItem 函数；
	 * @onClose: 见 f_closeTabItem 函数；
	 * @return: tabItem 对象；null - 当前选中失败；
	 */
	function f_addTabItem(tabItemData, isClose, titleRender, contentRender, onBeforeSelect, onSelect, onBeforeClose, onClose){
		var _this = this;

		var tabItem = _this.tabMapping[tabItemData.id];
		if(!tabItem){
			// tabItem 不存在，创建 tabItem, 已存在，则直接使用已存在的 tabItem
			// 判断 tab 数量是否达到上 限
			if(_this.maxTabCount > 0){
				var needCloseTabItems = [];
				var needClodeTabItemConunt = _this.allowCloseTabItems.length - _this.maxTabCount + 1;
				for(var i = 0; i < needClodeTabItemConunt; ++i){
					// 找出一个最晚操作过的 tabItem 关闭
					var tabItem = f_getFirstOrLastTabItem(_this.allowCloseTabItems, 1, needCloseTabItems);
					if(tabItem){
						needCloseTabItems.push(tabItem);
					}else{
						if(console){
							console.error('[>_<:20200222-2255-001] 查找最晚操作的 tabItem 失败；', _this.allowCloseTabItems, needCloseTabItems);
						}
					}
				}
				// 关闭找出的需要关闭的 tabItem 数组；
				f_closeTabItem.call(_this, needCloseTabItems, true, onBeforeClose, onClose);
			}

			if(_this.maxTabCount == 0 || _this.maxTabCount > _this.allowCloseTabItems.length){
				var tabItem = f_makeTabItem.call(_this, tabItemData, isClose, titleRender, contentRender, onBeforeSelect, onSelect, onBeforeClose, onClose);
				_this.tabMapping[tabItemData.id] = tabItem;
				if(isClose){
					// 新增的 tabItem 允许关闭，添加允许关闭的数组中
					_this.allowCloseTabItems.push(tabItem);
				}
				// 新增的 tabItem 添加的所有 tabItem 数组记录中
				_this.tabItems.push(tabItem);
			}else{
				if(console){
					console.error('[>_<:20200222-2055-001] 添加 tab 页失败，当前允许关闭的 tab 数量达到上限: ', _this.maxTabCount, _this.allowCloseTabItems);
				}
				return null;
			}
		}
		// 选择新添加的 tabItem
		f_seletedTabItem.call(_this, tabItem, onBeforeSelect, onSelect);
		return tabItem;
	}

	/** 
	 * 关闭指定的 tab Item
	 * this 指针，指向 $zk 节点对象
	 * @tabItems: tabItem 对象数组; {itemData: 数据对象, titleItemDom: title 节点对象, contentItemDom: item 内容节点对象, updateDate: 更新时间, isClose:是否可以关闭} 
	 * @isForce: 是否强制关闭，true-强制关闭，不受 onBeforeClose 函数中断;
	 * @onBeforeClose: 关闭前置处理，返回 false 时，可阻止选择；参数：tab item 数据对象；
	 * @onClose: 关闭事件；参数：tab item 数据对象
	 * @onBeforeSelect: 见 f_seletedTabItem 函数
	 * @onSelect: 见 f_seletedTabItem 函数
	 * @return: 被关闭的 tabItem 数据对象数组；
	 */
	function f_closeTabItem(tabItems, isForce, onBeforeClose, onClose, onBeforeSelect, onSelect){
		var _this = this;
		// 被关闭的 tabItem 数组
		var closes = [];
		if(tabItems == null){
			// 关闭所有
			if(_this.allowCloseTabItems != null){
				closes = f_closeTabItem.call(_this, _this.allowCloseTabItems, isForce, onBeforeClose, onClose);
			}
		}else{
			
			for(var tabItem of tabItems){
				if(isForce || tabItem.isClose){
					var sel = true;
					if(onBeforeClose instanceof Function){
						sel = onBeforeClose.call(tabItem, tabItem.itemData);
					}

					if(sel){
						// 关闭 tabItem
						// 删除 tab title dom
						tabItem.titleItemDom.remove();
						// 删除 tab content dom
						tabItem.contentItemDom.remove();
						// 删除映射数据
						delete _this.tabMapping[tabItem.itemData.id];
						// 添加到被关闭的 tabItem 数组中
						closes.push(tabItem);
						// 关闭事件
						if(onClose instanceof Function){
							onClose.call(tabItem, tabItem.itemData);
						}
					}else{
						// 当前不允许关闭 tabItem；不操作；
					}
				}else{
					// tabItem 不允许关闭; 不操作；
				}
			}

			// 将已关闭的 tabItem 从 允许关闭 tabItem 数组 allowCloseTabItems 中移除
			var f_isInClose = function(item){
				for(var closeItem of closes){
					if(closeItem.itemData.id == item.itemData.id){
						return false;
					}
				}
				return true;
			}
			_this.allowCloseTabItems = _this.allowCloseTabItems.filter(f_isInClose);
			// 将已关闭的 tabItem 从已打开的 tabItem 数组 tabItems 中移除
			_this.tabItems = _this.tabItems.filter(f_isInClose);
			// 如果当前选中的 tabItem 在关闭的 tabItem 中；选择最近操作过的 tabItem 打开；
			if(_this.tabItems.length > 0){
				// 找出一个最近操作过的 tabItem 并选中他
				var tabItem = f_getFirstOrLastTabItem(_this.tabItems, 0);
				f_seletedTabItem.call(_this, tabItem, onBeforeSelect, onSelect);
			}else{
				_this.currentSelectedTabId = undefined;
			}

			// 重置 tab title 宽度
			f_resetTabTitleContentWidth(_this.tabTitleDom);
		}
		return closes;
	}

	/** 
	 * 选择一个 tab Item
	 * this 指针，指向 $zk 节点对象
	 * @tabItems: tabItem 对象数组; {itemData: 数据对象, titleItemDom: title 节点对象, contentItemDom: item 内容节点对象, updateDate: 更新时间, isClose:是否可以关闭} 
	 * @onBeforeSelect: 选择前置处理，返回 false 时，可阻止选择；参数 tab item 数据对象；
	 * @onSelect: 选择事件；参数 tab item 数据对象；
	 * @return: tabItem 对象；null - 当前选中失败；
	 */
	function f_seletedTabItem(tabItem, onBeforeSelect, onSelect){
		var _this = this;
		var sel = true;
		if(tabItem){
			if(onBeforeSelect instanceof Function){
				sel = onBeforeSelect.call(tabItem, tabItem.itemData);
			}
			if(sel){
				// tab Item dom 处理
				tabItem.titleItemDom.siblings().removeClass("zk_tab_title_item_active");
				tabItem.titleItemDom.addClass("zk_tab_title_item_active");
				tabItem.contentItemDom.siblings().addClass("zk_display_none");
				tabItem.contentItemDom.removeClass('zk_display_none');

				// 移动 title item 到可见区域，如果已可见，不移动
				f_moveTabTitleItemToSee(_this.tabTitleDom, tabItem.titleItemDom);

				// 记录下当前选择的 tab item Id
				_this.currentSelectedTabId = tabItem.itemData.id;
				// 更新 tab item 操作时间
				tabItem.updateDate = new Date();

				// 选择 tab item 事件
				if(onSelect instanceof Function){
					onSelect.call(tabItem, tabItem.itemData);
				}
				return tabItem;
			}
		}
		return null;
	}

	/*************************************************************************/
	/*************************************************************************/
	/*************************************************************************/

	/** paramObj 说明
	{
		titleRender(tabItemData): 函数.title 内容返回回调；
			this: this 指针指向 tabTitleItemDom 节点
			@tabItemData: 第一个参数，对应 tab item 页数据对象；
			@return: 返回 title 标签；示例：<i class="fa fa-home"><span>Test</span></i>
		contentRender(tabItemData): 选填；函数。content 内容返回回调；默认取 tabItemData 中的 url 添加 iframe
	 		this: this 指针指向 tabContentItemDom 节点;
	 		@tabItemData: 第一个参数，对应 tab item 页数据对象；
			@return: jquery 节点；
		onBeforeSelect(tabItemData)：选填；函数；选择事情发生前
			this: 指向 tabItem 对象
			@tabItemData: 第一个参数，对应 tab item 页数据对象；
			@return: 返回 false 可以阻止 select;
		onSelect(tabItemData)：选填；函数；选择事情发生后
			this 指向 tabItem 对象
			@tabItemData: 第一个参数，对应 tab item 页数据对象；
			@return: void
		onBeforeClose(tabItemData)：选填；函数；删除民航Item 节点发生前,
			this: 指向 tabItem 对象
			@tabItemData: 第一个参数，对应 tab item 页数据对象；
			@return: 返回 false 可以阻止 remove;
		onClose(tabItemData)：选填；函数；删除民航Item 节点发生后,
			this: 指向 指向 tabItem 对象
			@tabItemData: 第一个参数，对应 tab item 页数据对象；
			@return: void
	}
	 */

	/* 返回对象 zkTab 扩展说明：
	{ 
		addTab(tabItemData, isClose): // 添加一个 tab
			@tabItemData: tab 页数据对象；
				{
					id: 唯一标识，会做为 tabId
					url: 页面 url 
				}
			@isClose: 是否允许关闭；true-允许关闭；false-不允许关闭；默认 true
			@return: tabItem 对象；
		select(tabId): 选择一个 tab
			@tabId: tab id
			@return: tabItem 对象；null - 当前选中失败；
		close(tabId): 关闭一个 tab; 等价于删除一个 tab
			@tabId: tab id
			@return: 被关闭的 tabItem 数据对象数组；
	}
	*/
	_zk.fn.extend({
		zkTab:function(paramObj){
			return f_createzkTab.call(this, paramObj);
		}
	});

})($zk);


