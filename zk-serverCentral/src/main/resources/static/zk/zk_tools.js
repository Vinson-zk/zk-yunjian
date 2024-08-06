/*
* @Author: Vinson
* @Date:   2020-01-30 21:34:36
* @Last Modified by:   Vinson
* @Last Modified time: 2020-03-08 17:03:29
* 
* 
* 
*/

function f_zkTools(){

	var _zkTools = {};

    /**
     * 将form表单封装成json对象
     */
    _zkTools.serializeObject = function(serializeArray) {
        var jsonData = {};
        serializeArray.forEach(function(item){
            if (jsonData[item.name]) {
                if (!jsonData[item.name].push) {
                    jsonData[item.name] = [ jsonData[item.name] ];
                }
                jsonData[item.name].push(item.value || '');
            } else {
                jsonData[item.name] = item.value || '';
            }
        });
        return jsonData;
    },

    /** 获取 url 中的参数；
     * @href: 为url;
     * @paramName: 为参数名;
     * @return: 返回参数值，不存在时返回 null 
     */
    _zkTools.queryURL = function(href, paramName){
        let reg = new RegExp(`(^|&)${paramName}=([^&]*)(&|$)`, 'i')
        // let href = window.location.href
        href = href.substr(href.indexOf('?'))
        let r = href.substr(1).match(reg)
        if (r != null) return decodeURI(r[2])
        return null
    },

    /*** 判断对象是否为空，true-是空；flase-不为空； ***/
    _zkTools.isEmpty = function(obj){
        return obj == undefined || obj == '' || obj == null;
    },

	/*** 国际化语言标识转换 ***/
	_zkTools.formatLocale = function(locale){
	    switch(locale){
	        case 'zh':
	        case 'zh-CN':
	        case 'zh_CN':
	            return 'zh_CN';
	            break;
	        case 'en':
	        case 'en-US':
	        case 'en_US':
	            return 'en_US';
	            break;
	        default:
	            return 'en_US';
	            break;
	    }
	};
	/*** 取 url 参数 ***/
	_zkTools.getQueryString = function (name){
        var now_url = document.location.search.slice(1), q_array = now_url.split('&');
        for (var i = 0; i < q_array.length; i++)
        {
            var v_array = q_array[i].split('=');
            if (v_array[0] == name)
            {
                return v_array[1];
            }
        }
        return false;
    };
    /*** 取本地当前语言 ***/
    _zkTools.getLocale = function(){
        var locale = localStorage.getItem("locale") || 'en_US';
        return _zkTools.formatLocale(locale);
    };
    /*** 设置本地国际化语言 */
    _zkTools.setLocale = function(locale){
        locale = _zkTools.formatLocale(locale);
        return localStorage.setItem("locale", locale);
    };
    /*** 取浏览器顶层  window  ***/
	_zkTools.getTopWindow = function(){
	    // var topWindow = window;
	    // while(topWindow != topWindow.parent){
	    //     topWindow = topWindow.parent;
	    // }
	    // return topWindow;
	    return window?window.top:window;
	};
    /*** 从浏览器顶层  window 中取筛选的节点  ***/
	_zkTools.getRootDom = function(domSelector){
        return $(domSelector, _zkTools.getTopWindow().document);
    };

    /**
    事件捕获 <div><p></p></div>
    当你使用事件捕获时，父级元素先触发，子级元素后触发，即div先触发，p后触发。
    事件冒泡
    当你使用事件冒泡时，子级元素先触发，父级元素后触发，即p先触发，div后触发。

    事件的传播是可以阻止的：
    • 在W3c中，使用stopPropagation（）方法
    • 在IE下设置cancelBubble = true；
    在捕获的过程中stopPropagation（）；后，后面的冒泡过程也不会发生了~

    阻止事件的默认行为，例如click <a>后的跳转~
    • 在W3c中，使用preventDefault（）方法；
    • 在IE下设置window.event.returnValue = false;

    DOM Leavl 0 – 事件监听器
    是最早的事件处理形式，它既可以直接写在HTMl上，也可以把一个函数分配给一个事件处理程序。
    然而，这种方式给一个元素的同一事件只允许一个处理器。
    示例：
    element.onclick = function(e){};
    element["onmousemove"] = function(e){};

    W3C DOM Leavl 2 – 事件监听器
    通过W3C DEMO Leavl 2事件处理，我们不会直接把一个函数分配给一个事件处理程序，相反，我们将新函数添加一个事件监听器；
    示例：
    --- W3C模型，三个参数
    事件名称，就是说解除哪个事件呗。
    事件回调，是一个函数，这个函数必须和注册事件的函数是同一个。
    事件类型，布尔值，true=捕获; false=冒泡; false- 默认; 这个参与绑定与移除必须一致。
    element.addEventListener('click',doSomething2,true)
    element.removeEventListener('click',doSomething2,true)
    --- IE

    W3C DOM Leaval 2通用语法
    一些注意事项：
    W3C DOM Leavl 2标准的addEventListener方法执行事件的顺序是按照事件注册的顺序执行的。而IE的attachEvent方法则相反–后注册的事件先觖发，先注册的事件后触发。
    W3C DOM Leavl 2标准的浏览器文本节点也会冒泡，而IE内核的浏览器文本节点不会冒泡。
    W3C DOM Leavl 2浏览器事件对象与IE内核的浏览器事件不同(具体请参阅)。
    DOM标准的浏览器事件卸载方式与IE内核的事件卸载方式不同。
    IE6/7/8 仍然没有遵循标准而使用了自己专有的attachEvent，且不支持事件捕获，所有事件都是发生在冒泡阶段。
    */
    _zkTools.event = {
        /** js 注册事件监听 通用方法 
         * @eventTarget [Object].required: 绑定目标对象
         * @eventType [String].required: 事件标识
         * @evrntHandler [Function].required: 事件处理函数
         * @useCapture [Boolean]: 事件类型，默认 无; true=捕获; false=冒泡; 这个参与绑定与移除必须一致。
         * @return [void]
         */
        binding: function(eventTarget, eventType, evrntHandler, useCapture){
            // if(useCapture == undefined){
            //     useCapture = false;
            // }
            evrntHandler.bind(eventTarget);
            if (eventTarget.addEventListener) { //IE9等其他现代浏览器
                // console.log("[^_^:20190916-1712-001] binding.addEventListener: ", eventTarget);
                eventTarget.addEventListener(eventType, evrntHandler, useCapture);
            } else if (eventTarget.attachEvent) { //IE6、7、8 
                // console.log("[^_^:20190916-1712-002] binding.attachEvent: ", eventTarget);
                eventType = "on" + eventType;
                eventTarget.attachEvent(eventType, evrntHandler);
            } else { //IE5~ 个人觉得不写也罢。
                 // console.log("[^_^:20190916-1712-003] binding: ", eventTarget);
                eventTarget["on" + eventType] = evrntHandler;
            }
        },
        /** js 移除事件监听 通用方法 
         * @eventTarget [Object].required: 绑定目标对象
         * @eventType [String].required: 事件标识
         * @evrntHandler [Function].required: 事件处理函数
         * @useCapture [Boolean]: 事件类型，默认 无; true=捕获; false=冒泡; 这个参与绑定与移除必须一致。
         * @return [void]
         */
        remove: function(eventTarget, eventType, evrntHandler, useCapture){
            // if(useCapture == undefined){
            //     useCapture = false;
            // }
            if (eventTarget.removeEventListener) { //IE9等其他现代浏览器 
                eventTarget.removeEventListener(eventType, evrntHandler, useCapture);
            } else if (eventTarget.attachEvent) { //IE6、7、8
                let detachEvent = "on" + eventType;
                eventTarget.detachEvent(eventType, evrntHandler);
            } else { //IE5~ 个人觉得不写也罢。
                eventTarget["on" + eventType] = null;
            }
        },
        /** js 取消冒泡事件，中断冒泡事件； 
         * @event [Object].required: 事件对象
         * @return [void]
         */
        cancelPropagation: function(event){
            //这里是因为除了IE有event其他浏览器没有所以要做兼容
            event = window.event || event;
            // if (document.all) {
            if (event.stopPropagation instanceof Function) {
                // 阻止冒泡事件
                event.stopPropagation();
            } else {
                // 阻止冒泡事件
                event.cancelBubble = true;
            }
        },
        //  if(window.event){这是IE浏览器}       

        /** js 取消默认事件，中断事件；
         * @event [Object].required: 事件对象
         * @return [void]
         */
        cancelDefault: function(event){
            event = window.event || event;
            if (event.preventDefault instanceof Function) {
                //阻止默认事件
                event.preventDefault();
            } else {
                //阻止默认事件
                event.returnValue=false;
            }
        }
    };

    /*** js enc 编码处理 ***/
    _zkTools.enc = {
        /*** hex 编码，解码 ***/
        hex: {
            source: "0123456789abcdef",
            encode: function(bytes) {
                var source = zkTools.enc.hex.source;
                var res = [];
                var a = '';
                for (var i = 0; i < bytes.length; i++) {
                    a = (bytes[i] & 0xF0) >> 4;
                    // res.push(a);
                    res.push(source[a]);
                    a = bytes[i] & 0x0F;
                    // res.push(a);
                    res.push(source[a]);
                }
                res = res.join("");
                // res = res.toLowerCase();
                return res;
            },
            decode: function(hexStr) {
                var source = zkTools.enc.hex.source;
                var res = [];
                var a = '';
                for (var i = 0, j = 0; j < hexStr.length; i++) {
                    a = a & 0x00;
                    a = a | (source.indexOf(hexStr[j]) & 0x0F);
                    ++j;
                    a = (a << 4) | (source.indexOf(hexStr[j]) & 0x0F);
                    ++j;
                    a = a & 0xFF;
                    res.push(a);
                }
                return res;
            },
        },
        /*** UTF8 字符串转字节序列 ***/
        utf8: {
            encode: function(utf8Str) {
                utf8Str = utf8Str.replace(/rn/g, "n");
                // var res = "";
                var bytes = new Array();
                var c = 0;
                for (var i = 0; i < utf8Str.length; i++) {
                    c = utf8Str.charCodeAt(i);
                    if (c >= 0x010000 && c <= 0x10FFFF) {
                        bytes.push(((c >> 18) & 0x07) | 0xF0);
                        bytes.push(((c >> 12) & 0x3F) | 0x80);
                        bytes.push(((c >> 6) & 0x3F) | 0x80);
                        bytes.push((c & 0x3F) | 0x80);
                    } else if (c >= 0x000800 && c <= 0x00FFFF) {
                        bytes.push(((c >> 12) & 0x0F) | 0xE0);
                        bytes.push(((c >> 6) & 0x3F) | 0x80);
                        bytes.push((c & 0x3F) | 0x80);
                    } else if (c >= 0x000080 && c <= 0x0007FF) {
                        bytes.push(((c >> 6) & 0x1F) | 0xC0);
                        bytes.push((c & 0x3F) | 0x80);
                    } else {
                        bytes.push(c & 0xFF);
                    }
                }
                return bytes
            },
            decode: function(bytes) { // 字节序列转ASCII码  [0x24, 0x26, 0x28, 0x2A] ==> "$&C*";
                if (typeof bytes === 'string') {
                    return bytes;
                }
                var res = "";
                var _bytes = bytes;
                var one, v, bytesLength, store;
                for (var i = 0; i < _bytes.length; ++i) {
                    one = _bytes[i].toString(2);
                    v = one.match(/^1+?(?=0)/);
                    if (v && one.length == 8) {
                        bytesLength = v[0].length;
                        store = _bytes[i].toString(2).slice(7 - bytesLength);
                        for (var st = 1; st < bytesLength; st++) {
                            store += _bytes[st + i].toString(2).slice(2);
                        }
                        res += String.fromCharCode(parseInt(store, 2));
                        i += bytesLength - 1;
                    } else {
                        res += String.fromCharCode(_bytes[i]);
                    }
                }
                return res;
            }
        },
        base64: {
            source: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
            encode: function(bytes) {
                var _keyCharacters = zkTools.enc.base64.source;
                var result = "";
                var sb1, sb2, sb3;
                var tb1, tb2, tb3, tb4;
                var i = 0;

                while (i < bytes.length) {
                    sb1 = bytes[i++];
                    sb2 = bytes[i++];
                    sb3 = bytes[i++];
                    tb1 = sb1 >> 2;
                    tb2 = (sb1 & 0x3) << 4 | sb2 >> 4;
                    tb3 = (sb2 & 0xF) << 2 | sb3 >> 6;
                    tb4 = sb3 & 0x3F;
                    if (isNaN(sb2)) {
                        tb3 = tb4 = 64
                    } else if (isNaN(sb3)) {
                        tb4 = 64
                    }
                    result = result +
                        _keyCharacters.charAt(tb1) +
                        _keyCharacters.charAt(tb2) +
                        _keyCharacters.charAt(tb3) +
                        _keyCharacters.charAt(tb4)
                }
                return result
            },
            decode: function(base64Str) {
                var _keyCharacters = zkTools.enc.base64.source;
                var res = [];
                var sb1, sb2, sb3, sb4;
                var tb1, tb2, tb3;
                var i = 0;
                base64Str = base64Str.replace(/[^A-Za-z0-9+/=]/g, "");
                while (i < base64Str.length) {
                    sb1 = _keyCharacters.indexOf(base64Str.charAt(i++));
                    sb2 = _keyCharacters.indexOf(base64Str.charAt(i++));
                    sb3 = _keyCharacters.indexOf(base64Str.charAt(i++));
                    sb4 = _keyCharacters.indexOf(base64Str.charAt(i++));
                    tb1 = ((sb1 & 0x3F) << 2) | ((sb2 >> 4) & 0x3);
                    tb2 = ((sb2 & 0xF) << 4) | ((sb3 >> 2) & 0xF);
                    tb3 = ((sb3 & 0x3) << 6) | (sb4 & 0x3F);
                    res.push(tb1);
                    if (sb3 != 64) {
                        res.push(tb2);
                    }
                    if (sb4 != 64) {
                        res.push(tb3);
                    }
                }
                return res
            },
        },
    };

    /*** cookie ***/

    /*** jquery easyUI 对接处理中间函数 ***/
    _zkTools.easyUI = {
        parseData:function(page, opt){
            opt = opt | {};
            var keyOpt = {rows:'result', total:'totalCount'};

            $.extend(true, keyOpt, opt);

            var tPage = {};
            // 数据记录行
            tPage.rows = page[keyOpt.rows];
            // 当前页码
            // 每页数据行数
            // 数据总行数
            tPage.total = page[keyOpt.total];
            return tPage;
        },
        formatParam:function(param, opt){
            opt = opt | {};
            var keyOpt = {pageNo:'page', pageSize:'rows'};
            $.extend(true, keyOpt, opt);

            // 数据记录行
            // 页码
            param['page.no'] = param[keyOpt.pageNo] - 1;
            // 每页数据行数
            param['page.size'] = param[keyOpt.pageSize];
            // 数据总行数

            // 移除原分页参数
            delete param[keyOpt.pageNo]; 
            delete param[keyOpt.pageSize]; 

            return param;
        },
    };
    /*** layui 对接中间处理函数 ***/
    _zkTools.layui = {
        parseData:function(res, opt){
            opt = opt || {};
            opt = $.extend({
                code:'code',
                msg:'msg',
                page:'data',
                count:'totalCount',
                data:'result'
            }, opt);

            var tPage = {};
            // 响应码
            tPage.code = page[opt.code];
            // 响应消息
            tPage.msg = page[opt.msg];

            if(res.data){
                // 数据记录行
                tPage.data = page[opt.page][opt.count];
                // 当前页码
                // 每页数据行数
                // 数据总行数
                tPage.count = page[opt.page][opt.data];
            }else{
                tPage.data = [];
                tPage.count = 0;
            }
            return tPage;
        }
    };

    return _zkTools;
};

var _zkTools = f_zkTools();



