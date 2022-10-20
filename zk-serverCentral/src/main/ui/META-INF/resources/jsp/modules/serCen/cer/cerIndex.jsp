<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/jsp/include/tldlib.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
    <%@include file="/jsp/include/head.jsp" %>

    <link rel="stylesheet" type="text/css" href="/${ctxStatic}/lib/jquery-easyui/${_v_jqEasyui}/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/${ctxStatic}/lib/jquery-easyui/${_v_jqEasyui}/themes/icon.css">

    <link rel="stylesheet" type="text/css" href="/${ctxStatic}/zk/easyui/zk_datagrid.css">

    <script type="text/javascript" src="/${ctxStatic}/lib/jquery-easyui/${_v_jqEasyui}/jquery.easyui.min.js"></script>

	<script type="text/javascript">

        var cerIndexKeys = {
            dataIndex:'data-index'
        }
        // 禁止修改 cerIndexKeys 对象，不能删除，新增，修改属性
        Object.freeze(cerIndexKeys);
		
        var gridDom = undefined;
        $(function () {
            // 当前语言
            var locale = $zk.getLocale();
            // 表格工具栏
            var toolbar = [{
                text: "<spring:message code='view.msg.opt.add'/>", 
                iconCls: 'icon-add', 
                handler: function() { 
                    // var editDom = '<iframe id="" src="/${_adminPath}/${_modulePath}/cer/view/edit?pkId=" width="500px" height="700px" frameborder="0"></iframe>';
                    // $zk.zkModal(editDom);
                    parent.f_addTab("serCen-cerEdit", true);
                } 
            }, '-', { 
                text: "<spring:message code='view.msg.opt.delete'/>", 
                iconCls: 'icon-remove', 
                handler: function(){ 
                    f_deleteCer();
                }
            }];

            var columns = [[
                { title:"<spring:message code='view.msg.opt.checkbox'/>", field:'ck', checkbox:true },
                { title:"<spring:message code='view.msg.cer.field.serverName'/>", field:'serverName', width:120, align:'left' },
                { title:"<spring:message code='view.msg.cer.field.status'/>", field:'status', width:100, align:'center', formatter: function(value, row, index){
                    // 正常 Enable = 0; 禁用 Disabled = 1;
                    // return '<button zkKey="status" ' + cerIndexKeys.dataIndex + '="' + index + '" class="zk-status_' + locale + '" ></button>';
                    return '<button zkKey="status" ' + cerIndexKeys.dataIndex + '="' + index + '" size="small" ></button>';
                }},
                { title:"<spring:message code='view.msg.cer.field.publicKey'/>", field:'publicKey', width:300, align:'left' },
                { title:"<spring:message code='view.msg.cer.field.validStartDate'/>", field:'validStartDate', width:120, align:'center' },
                { title:"<spring:message code='view.msg.cer.field.validEndDate'/>", field:'validEndDate', width:120, align:'center' },
                { title:"<spring:message code='view.msg.opt.name'/>", field:'opt', width:180, align:'center', formatter:function(value, row, index){
                    return [
                        '<button zkKey="down" ' + cerIndexKeys.dataIndex + '="' + index + '" class="zk_opt_item"><spring:message code="view.msg.opt.download"/></button>',
                        '<button zkKey="edit" ' + cerIndexKeys.dataIndex + '="' + index + '" class="zk_opt_item"><spring:message code="view.msg.opt.edit"/></button>'
                    ].join("");
                }},
            ]];

            gridDom = $("#dg");
            gridDom.datagrid({
                columns:columns,
                toolbar:toolbar,
                method:'get',
                url:'/${_adminPath}/${_modulePath}/cer/scPage',
                queryParams:{
                    // 额外的固定参数
                },
                onBeforeLoad:function(param){ // 加载时请求参数预处理
                    // 请求前处理参数
                    return $zk.easyUI.formatParam(param);
                },
                loadFilter: function(data){ // 加载数据预处理
                    if (data.data){
                        return $zk.easyUI.parseData(data.data);
                    } else {
                        return {rows:[], total:0};
                    }
                },
                onLoadSuccess: function(page){
                    var rowDatas = page.rows;
                    // 状态开关处理
                    var zkSwis = $('button[zkKey="status"]', gridDom.parent()).each(function(index, item){
                        var itemDom = $(item);
                        var rowData = rowDatas[itemDom.attr(cerIndexKeys.dataIndex)];
                        if(rowData){
                            $zk(item).zkSwitch({
                                defaultStatus: rowData.status === 0?1:0, // rowData.status 转为开关对应值
                                // openLabel:'<spring:message code="view.msg.enable"/>',
                                // closeLabel:'<spring:message code="view.msg.disable"/>',
                                onBeforeSwitch:function(status){
                                    var _this = this;
                                    var tStatus = status == 0?1:0;
                                    f_enableDisableCer(rowData, tStatus, function(){
                                        _this.setStatus(tStatus);
                                    });
                                    return false;
                                }
                            });
                        }else{
                            if(console){
                                console.error("[>_<:20200120-1107-001] 状态开关数据设置有误");
                            }
                        }
                    });

                    // 下载操作按钮
                    $('button[zkKey="down"]', gridDom.parent()).map(function(index, item){
                        var itemDom = $(item);
                        var rowData = rowDatas[itemDom.attr(cerIndexKeys.dataIndex)];
                        if(rowData){
                            $zk.event.binding(item, 'click', function(e){
                                $zk.event.cancelPropagation(e);
                                // 下载
                                f_downloadCer(rowData);
                            }, false);
                        }else{
                            if(console){
                                console.error("[>_<:20200120-1107-002] 下载按钮数据设置有误");
                            }
                        }
                    });
                    // 编辑操作按钮
                    $('button[zkKey="edit"]', gridDom.parent()).map(function(index, item){
                        var itemDom = $(item);
                        var rowData = rowDatas[itemDom.attr(cerIndexKeys.dataIndex)];
                        if(rowData){
                            $zk.event.binding(item, 'click', function(e){
                                $zk.event.cancelPropagation(e);
                                // 编辑 
                                parent.f_addTab("serCen-cerEdit", true, "pkId=" + rowData.pkId);
                            }, false);
                        }else{
                            if(console){
                                console.error("[>_<:20200120-1107-003] 下载按钮数据设置有误");
                            }
                        }
                    });
                },
                pagination: true, // 分页
                rownumbers: true, // 序号
                singleSelect: false, // 允许选择多行
                ctrlSelect: true,
                autoRowHeight: false,
                fitColumns: true,
            });
            
            var msg = $zkLocale.msg;
            // 分页国际化
            gridDom.datagrid("getPager").pagination({ // 分页栏下方文字显示
                beforePageText: msg['zk.page.beforePageText'],
                afterPageText: msg['zk.page.afterPageText'],
                displayMsg: msg['zk.page.displayMsg'],
            });
        });
        // 查询
        function f_search(){
            gridDom.datagrid('reload', f_getWhere());
        }
        // 取参数
        function f_getWhere(){
            return {
                serverName:$("#serverName").val()
            }; 
        }

        // 下载
        function f_downloadCer(row) {
            var url = '/${_adminPath}/${_modulePath}/cer/download?pkId=' + row.pkId;

            // ====================
            var downloadLink = document.createElement('a');
            downloadLink.setAttribute("download", "");
            downloadLink.href = url;
            downloadLink.click();

            // ====================
            // 如果文件是 txt 或者 img 的话，那么谷歌浏览器就会直接打开这些文件
            // window.open(url);
        };
        // 删除
        function f_deleteCer(row){

            var datas = [];
            if(!row){
                datas = gridDom.datagrid("getSelections").map(function(currentValue, index, arr){
                    return currentValue.pkId;
                });
            }else{
                datas.push(row.pkId);
            }
            // 判断是否有数据
            if(!f_isHasData(datas)){
                // 没有选择数据 
                return;
            }

            var msgInfo = '<spring:message code="view.msg.alert.info.delete"/>';
            // 提交
            $zk.zkAjaxComfirmSubmit({msg:msgInfo}, {
                method:'DELETE',
                url: '/${_adminPath}/${_modulePath}/cer/sc',
                data: "pkIds=" + datas.join('&pkIds='),
                success: f_search
            });
        }
        // 禁用
        function f_enableDisableCer(row, status, callBack){

            // status 转为后对应值
            status = status == 0?1:0;

            var datas = [];
            if(!row){
                datas = gridDom.datagrid("getSelections").map(function(currentValue, index, arr){
                    return currentValue.pkId;
                });
            }else{
                datas.push(row.pkId);
            }
            // 判断是否有数据
            if(!f_isHasData(datas)){
                // 没有选择数据 
                return;
            }

            var msgInfo = " ";
            if(status == 0){
                // 启用证书
                msgInfo =  '<spring:message code="view.msg.alert.info.enable"/>';
            }else{
                msgInfo =  '<spring:message code="view.msg.alert.info.disable"/>';
            }

            // 提交
            $zk.zkAjaxComfirmSubmit({msg:msgInfo}, {
                method:'POST',
                url: '/${_adminPath}/${_modulePath}/cer/scStatus',
                data: 'status=' + status + '&pkIds=' + datas.join('&pkIds='),
                success: callBack
            });
        }
        // 判断 数组 是否有数据；返回 true-有；false-没有
        function f_isHasData(datas){
            if(datas.length < 1){
                msgInfo = '<spring:message code="view.msg.alert.info.data.empty"/>';
                $zk.zkConfirm({
                    type:'warning',
                    msg: msgInfo,
                    isCancel:false
                });
                return false;
            }
            return true;
        }

    </script>
<style type="text/css">
    .zk_modal_content_i {
        background: url(/${ctxStatic}/zk/images/icon.png) no-repeat;
    }
    .zk-status_zh_CN {
        width: 53px;
    }
    .zk-status_en_US {
        width: 63px;
    }
</style>

</head>
<body style="padding: 6px; overflow: hidden; display: flex; flex-direction: column; ">

	<div id="searchbar" style="padding:6px; flex:0 0 auto;" >
	    <spring:message code="view.msg.cer.field.serverName" />：<input id="serverName" name="serverName" class="zk_input" type="text" />
	    <input id="btnOK" type="button" class="zk_btn" value="<spring:message code='view.msg.opt.search'/>" onclick="f_search()" />
	</div>
    <div id="gridDiv" style="margin:0; padding:0; flex: 1 1 auto; " >
        <table id="dg" class="easyui-datagrid" style="height:100%;" ></table>   

    </div>
    <div style="display:none;">
		<!-- g data total ttt -->
	</div>

    <!-- <script type="text/javascript">
        // $zk.zkSwitchInit();
    </script> -->
</body>
</html>


