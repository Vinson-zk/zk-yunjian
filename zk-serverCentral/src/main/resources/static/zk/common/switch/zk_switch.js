/*
* @Author: Vinson
* @Date:   2020-01-18 10:03:30
* @Last Modified by:   Vinson
* @Last Modified time: 2020-02-29 22:14:05
* 
* 
* 
*/

(function (_zk){

	var zkSwitchKeys = {
		isInitKey:'_zk_switch_key_isInit'
	}
	// 禁止修改 zkFlag 对象，不能删除，新增，修改属性
	Object.freeze(zkSwitchKeys);

	/**
	 * 设置开关状态
	 * @status: 0-关闭；1-开；
	 * @return: void；
	 */
	function f_setStatus(status, openLabel, closeLabel){ 
		var _this = this;
		var switchDom = _this.switchDom;
		_this.status = status;
		if(status === 1){
			// 设置开关状态为：开
			if(switchDom.hasClass('zk-switch-close')){
				switchDom.removeClass('zk-switch-close');
				switchDom.addClass('zk-switch-open');
				$(".zk-switch-label", switchDom).html(openLabel);
			}
		}else{
			// 设置开关状态为：关
			if(switchDom.hasClass('zk-switch-open')){
				switchDom.removeClass('zk-switch-open');
				switchDom.addClass('zk-switch-close');
				$(".zk-switch-label", switchDom).html(closeLabel);
			}
		}
		$(".zk-switch-action-animating", switchDom).remove();
		switchDom.append($('<div class="zk-switch-action-animating"></div>'));
	}

	/**
	 * 操作开关; 函数 this 指针需要指向本开关节点
	 * @status: 0-关闭；1-开；
	 */
	function f_doSwitch(status, onBeforeSwitch, onSwitch, openLabel, closeLabel){
		var _this = this;
		var switchDom = _this.switchDom;
		var doing = true;

		if(typeof(onBeforeSwitch) === 'function'){
			doing = onBeforeSwitch.call(_this, _this.status);
		}
		// 执行开关操作
		if(doing){
			f_setStatus.call(_this, status, openLabel, closeLabel);
		}
		// 执行开关操作回调
		if(typeof onSwitch === 'function'){	
			// 如果有开关处理事件函数，
			onSwitch.call(_this, status);
		}
	}


	// 创建开关
	function f_createSwitch(opt){
		var _this = this;
		opt = opt || {};

		// var switchDom = $('<button class="zk-switch ' + (opt.defaultStatus == 0?'zk-switch-close':'zk-switch-open') + '"></button>');
		var switchDom = _this.jqDom;

		// 设置开关节点已初始化
		switchDom.attr(zkSwitchKeys.isInitKey, 'true');

		opt.size = opt.size || switchDom.attr('size');
		opt.openLabel = opt.openLabel === undefined?switchDom.attr('openLabel'):opt.openLabel;
		opt.closeLabel = opt.closeLabel === undefined?switchDom.attr('closeLabel'):opt.closeLabel;
		opt.defaultStatus = opt.defaultStatus || switchDom.attr('defaultStatus');
		opt.onSwitch = opt.onSwitch || switchDom.attr('onSwitch');
		
		opt = $.extend(true, {
			size:'normal', // 大小 两种大小 'normal' - 正常大小； 'small' - 小尺寸
			openLabel: undefined, 
			closeLabel: undefined,
			defaultStatus:0, // 默认状态；0-关闭；1-开；
			onBeforeSwitch:undefined, // 开关事件前调用，函数 this 指针，会指向本开关节点；返回 false 可阻止开关操作；
			onSwitch:undefined, // 开关事件，函数 this 指针，会指向本开关节点；操作标识 optFlag：0-关闭；1-开；
		}, opt);

		/*
		<button onclick="f_switch(this)" class="zk-switch zk-switch-close">
		    <span class="zk-switch-label">关</span>
		    <div></div>
		</button>
		*/

		var msg = $zkLocale.msg;

		var locale = _zk.getLocale();
		if(opt.openLabel === undefined){
			opt.openLabel = msg['zk.opt.name.on'];
		}
		if(opt.closeLabel === undefined){
			opt.closeLabel = msg['zk.opt.name.off'];
		}

		if(!switchDom.hasClass('zk-switch')){
			switchDom.addClass('zk-switch');
		}

		if(opt.defaultStatus == 0){
			switchDom.addClass("zk-switch-close");
		}else{
			switchDom.addClass("zk-switch-open");
		}

		if('small' === opt.size){
			switchDom.addClass("zk-switch-small");
		}
		// 开关点击事件
		_zk.event.binding(switchDom[0], 'click', function(e){
    		_zk.event.cancelPropagation(e);
    		f_doSwitch.call(_this, (_this.status === 0?1:0), opt.onBeforeSwitch, opt.onSwitch, opt.openLabel, opt.closeLabel);
        }, false);
		// 开关标签
		switchDom.append($('<span class="zk-switch-label">' + (opt.defaultStatus == 0?opt.closeLabel:opt.openLabel) + '</span>'));
		// _this.jqDom.append(switchDom);
		_this.extend({
        	switchDom:switchDom,
        	opt:opt,
        	status: opt.defaultStatus,
        	switch:function(status){ // optFlag：0-关闭；1-开；
        		f_doSwitch.call(_this, status, opt.onBeforeSwitch, opt.onSwitch, opt.openLabel, opt.closeLabel);
        	},
        	setStatus: function(status){ // 
        		f_setStatus.call(_this, status, opt.openLabel, opt.closeLabel);
        	},
        	getStatus: function(){ // 取开关当前状态；0-关闭；1-开；
        		return _this.status;
        	}
        });

		return _this;
	}

	/*
	参数 opt 说明：
	{
		size:'', // 大小 两种大小 'normal' - 正常大小； 'small' - 小尺寸
		openLabel: undefined, // 开关打开时的文本，默认为 zk.opt.name.on 国际化值
		closeLabel: undefined, // 开关关闭的文本，默认为 zk.opt.name.off 国际化值
		defaultStatus:0, // 默认状态；0-关闭；1-开；
		onBeforeSwitch(status):undefined, // 开关事件前调用，函数 this 指针，会指向本开关节点；返回 false 可阻止开关操作；
			this: this 指针，会指向本开关 $zk 节点对象；
			@status: 当前开关状态，改变前的状态；
			@return: true - 允许开操作；alse 可阻止开关操作；
		onSwitch(status):undefined, // 开关事件，函数 this 指针，会指向本开关节点；操作标识 optFlag：0-关闭；1-开；
			this: this 指针，会指向本开关 $zk 节点对象；
			@status: 当前开关状态，改变前的状态；
			@return: void
	}

	返回对象 zkSwitch 扩展说明：
	{
		switch(status): 执行开关 开或关；会触发 onBeforeSwitch；onSwitch 事件
			@status: 0-关闭；1-开；
			@return: void
		setStatus(status): 设置开关状态；不会触发 onBeforeSwitch；onSwitch 事件
			@status: 0-关闭；1-开；
			@return: void
		getStatus(): 取开关当前状态；0-关闭；1-开；
			@return: 开关当前状态；0-关闭；1-开；			
	}
	*/
	_zk.fn.extend({
		zkSwitch:function(opt){
			return f_createSwitch.call(this, opt);
		}
	});

	/*** 初始化所有含有 class '.zk-switch' 的节点为开关；***/
	$.extend(_zk.zkFunc, {
		zkSwitchInit:function(){
			$('.zk-switch').map(function(index, item){
				if($(item).attr(zkSwitchKeys.isInitKey) !== 'true'){
					// 仅对没初始化的节点进行初始化
					_zk(item).zkSwitch({
						openLabel: '', 
						closeLabel: '',
					});
				}
			});
		}
	});
	_zk.extend(_zk.zkFunc);

	_zk.zkSwitchInit();
	
	
})($zk);








