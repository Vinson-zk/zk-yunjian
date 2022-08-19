<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/jsp/include/tldlib.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
    <%@include file="/jsp/include/head.jsp" %>
 
	<script type="text/javascript">
		$(function(){
            // WdatePicker({el:$('#validStartDate')[0], qsEnabled:false});
            $('#validStartDate').bind('click', function(){
                var maxDate = $('#validEndDate').val();
                var wdatePickerOpt = {
                    el:this, 
                    qsEnabled:false
                }
                if(maxDate){
                    wdatePickerOpt.maxDate = maxDate;
                }
                WdatePicker(wdatePickerOpt);
            });
            $('#validEndDate').bind('click', function(){
                var minDate = $('#validStartDate').val();
                var wdatePickerOpt = {
                    el:this, 
                    qsEnabled:false
                }
                if(minDate){
                    wdatePickerOpt.minDate = minDate;
                }
                WdatePicker(wdatePickerOpt);
            });

            var validator = $("#form").validate({
				submitHandler:function(jqDom) {
           			f_submitHandler($(jqDom));
           			return false; // 返回 false form 表单不用提交，由自行处理提交。 
			    }
			});
			validator.resetForm();

            // console.log("--------- model: ", '${model}', '${model.pkId != ''}', '${model.pkId1 == ''}', '${model.pkId1 == null}');
        });

        /*** 提交 form 以 ajax 提交  ***/
        function f_submitHandler(jqForm) { 
        	 /*
        	 	当前台需要向后台提交多条数据时，一般采用from表单中的name属性提交
				（1）$("#id").serialize();
				获得数据的格式：naem=xiaoming&sex=1 键值对之间用&连接
				（2）$("#id").serializeArray();
        	 */

        	// console.log("[^_^:20200305-2058-001] jqForm.serialize: ", jqForm.serialize());
        	// console.log("[^_^:20200305-2058-001] jqForm.serializeArray: ", jqForm.serializeArray());
            
	        $zk.zkLoading(1);
            var jsonData = $zk.serializeObject(jqForm.serializeArray());
            // console.log("[^_^:20200305-2058-001] jsonData: ", jsonData, JSON.stringify(jsonData));

            /*** 2、ajax 请求 ***/
            $zk.zkAjax({
                url: jqForm.attr("action"),
                method: jqForm.attr("method"),
                contentType: "application/json;charset=utf-8",
                data: JSON.stringify(jsonData),
                error:function(xhr){
                    console.log("[>_<:20200102-1520-001] ", xhr);
                    $zk.zkLoading(0);
                    // $zk.zkShowMsg(xhr, "error");
                    parent.f_resMsg(resData, "error");
                },
                success:function(resData, status, xhr){
                    // console.log("[^_^:20200305-2053-001] cer/sc success: ", resData);
                    // $zk.zkResMsg(resData);
                    parent.f_resMsg(resData);
                    $zk.zkLoading(0);
                    f_cancelEdit();
                }
            });
	    } 

        /*** 取消编辑 ***/
        function f_cancelEdit(){
            parent.f_closeTab("serCen-cerEdit");
        }
	</script>
</head>
<body>

    <div class="zk_form zk_form_has_floor">
    	<div class="zk_form_head"><i class="glyphicon glyphicon-edit"></i>&nbsp;
            <c:choose>
                <c:when test="${model.pkId != null && model.pkId != ''}">
                    <spring:message code='view.msg.opt.edit'/>
                </c:when>
                <c:otherwise>
                    <spring:message code='view.msg.opt.add'/>
                </c:otherwise>
            </c:choose>
    	</div>
        <form id="form" class="zk_form_label_right" action="/${_adminPath}/${_modulePath}/cer/sc" 
            method="post" 
            contentType="application/json;charset=utf-8" >
        	<input type="hidden" name="pkId" value="${model.pkId}" >
        	<div class="form-group row">
                <label for="serverName" class="col-sm-4 col-form-label"><spring:message code='view.msg.cer.field.serverName'/>:</label>
                <div class="col-sm-7">
                    <input id="serverName" name="serverName" value="${model.serverName}" type="text" class="form-control" 
                    	placeholder='<spring:message code="view.msg.cer.field.serverName"/>'
						required="true"
						data-msg-required='<spring:message code="view.msg.alert.field.required" />'
                    >
                </div>
            </div>
            <fieldset class="form-group">
                <div class="row">
                    <legend class="col-form-label col-sm-4 pt-0"><spring:message code="view.msg.cer.field.status"/>:</legend>
                    <div class="col-sm-7">
                        <div class="form-check">
                            <input id="status-0" name="status" value="0" ${model.status==0?"checked":""}
                            	class="form-check-input" type="radio" >
                            <label class="form-check-label" for="status-0">
                                <spring:message code='view.msg.enable'/>
                            </label>
                        </div>
                        <div class="form-check">
                            <input id="status-1" name="status" value="1" ${model.status==1?"checked":""}
                            	class="form-check-input" type="radio" >
                            <label class="form-check-label" for="status-1">
                                <spring:message code='view.msg.disable'/>
                            </label>
                        </div>
                    </div>
                </div>
            </fieldset>
             <div class="form-group row">
                <legend class="col-form-label col-sm-4 pt-0"><spring:message code='view.msg.cer.field.validDateRang'/>:</legend>
                <div class="col-sm-8 zk_row">
                    <input id="validStartDate" name="validStartDate" value="${model.validStartDate}"
                    	class="form-control Wdate zk_date_1 mr-2" type="text" autocomplete="off"
                    	required="true"
						data-msg-required='<spring:message code="view.msg.alert.field.required" />'
                    >
                ~
                    <input id="validEndDate" name="validEndDate" value="${model.validEndDate}"
                    	class="form-control Wdate zk_date_1 ml-2" type="text" autocomplete="off"
                    	required="true"
						data-msg-required='<spring:message code="view.msg.alert.field.required" />'
                    >
                </div>
            </div>
            <div class="zk_form_clean"></div>
            <div class="form-group row zk_form_floor zk_form_floor_right">
                <div class="col-sm-12 ">
                    <!-- btn btn-primary -->
                    <button type="submit" class="zk_btn zk_primary"><spring:message code='view.msg.opt.submit'/></button>
                    <button type="button" class="zk_btn" onclick="f_cancelEdit()"><spring:message code='view.msg.opt.cancel'/></button>
                </div>
            </div>
        </form>
    </div>

    <div style="display:none;">
		<!-- g data total ttt -->
	</div>
</body>
</html>