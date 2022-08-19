<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/include/tldlib.jsp" %>

<!DOCTYPE html>
<html>

<head>
	<!-- <title>放在引入 head.jsp 前面，可以重新定义 </title> -->
	<%@include file="/jsp/include/head.jsp" %>

	<script type="text/javascript">
		function f_test(){
			console.log(parent==parent.parent, parent.parent==parent.parent.parent, parent.parent.parent==parent.parent.parent.parent);
		}
	</script>
</head>
<body>
	<h1>sc guide</h1>
	<div onClick="f_test()">f_test</div>
</body>
</html>