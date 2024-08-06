
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<% /*
<%@ taglib prefix="c" uri="jakarta.tags.core" 
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" 
<%@ taglib prefix="fn" uri="jakarta.tags.functions" 
<%@ taglib prefix="sql" uri="jakarta.tags.sql" 
<%@ taglib prefix="x" uri="jakarta.tags.xml" 
*/%>

<%@ page import="com.zk.core.utils.ZKStringUtils"%>
<%@ page import="com.zk.core.utils.ZKLocaleUtils"%>
<%@ page import="com.zk.core.web.utils.ZKWebUtils"%>
<%@ page import="com.zk.core.utils.ZKEnvironmentUtils"%>

<%@ taglib prefix="zkLocale" uri="/tlds/zkLocale.tld" %>
<%@ taglib prefix="zkStr" uri="/tlds/zkStr.tld" %>
<%@ taglib prefix="zkEnv" uri="/tlds/zkEnv.tld" %>
<%@ taglib prefix="zkCookie" uri="/tlds/zkCookie.tld" %>

<%
	// 这里不能 类型隐式 转换，类型隐式转换会报错！
	// String localeStr = request.getLocale()==null?"en_US":request.getLocale().toLanguageTag();
	java.util.Locale locale = ZKWebUtils.getLocale();
	String localeStr = locale == null?"en_US":locale.toLanguageTag();
	localeStr = localeStr.replaceAll("-", "_");

	String _adminPath = ZKEnvironmentUtils.getString("zk.path.admin");
	String _modulePath = ZKEnvironmentUtils.getString("zk.path.serCen");
	String _version = ZKEnvironmentUtils.getString("zk.ser.cen.version");
	String _copyright = ZKEnvironmentUtils.getString("zk.ser.cen.copyright");
%>

<c:set var = "ctxStatic" value = "static" />
<c:set var = "locale" value = "<%=localeStr%>" />
<c:set var = "_adminPath" value = "<%=_adminPath%>" />
<c:set var = "_modulePath" value = "<%=_modulePath%>" />
<c:set var = "_version" value = "<%=_version%>" />
<c:set var = "_copyright" value = "<%=_copyright%>" />

