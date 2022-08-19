<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/include/tldlib.jsp" %>

<%@ page import="com.zk.core.utils.ZKStringUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="servletPath" value="${pageContext.request.servletPath}"/>
<c:set var="req.attribute" value="${pageContext.request.getAttribute(\"attribute\")}"/>
<c:set var="requestURI" value="${pageContext.request.requestURI}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>AccessReal test jsp</title>
    <%@include file="/jsp/include/head.jsp" %>

	<script type="text/javascript">
		<%
			// 这里不能 类型隐式 转换，类型隐式转换会报错！
			String attribute = (String)request.getAttribute("attribute");
			System.out.println("[^_^:20190820-1752-001] attribute：" + attribute);
		%>
		
		var attribute = '<%=attribute%>';
		
		// 在 html 加载完成后执行；
		window.onload = function(){
			console.log("[^_^:20190827-1027-001] request.getAttribute: ", attribute);
			document.getElementById("reqAttribute").innerHTML = document.getElementById("reqAttribute").innerHTML + attribute;
		}
		
		// 主题改变
        function themeSelect(theme){
        	
        	if (theme){
                // location.href = location.href "index?theme=" + theme;
                localStorage.setItem("theme", theme);
            } else{
                // location.href = "index";
                localStorage.removeItem("theme");
            }
        }
		
     	// 国际化语言切换
        function languageSelect(locale){
        	locale = locale||"zh_CN";
        	$.ajax({
        		url: '/zk/v1.0/serCen/locale?locale=' + locale,
                method: 'GET',
                data: {},
                /* dataType: 'JSON', */
                async: false,
                success: function(result, status, xhr){
                	localStorage.setItem("locale", locale);
                },
                error: function(xhr, status, err){
                	console.log("languageSelect ajax fail ---", status, err);
                }
            })
        }

        var i = 0;
        function f_ajaxTest(){
            // $zk.zkLoading(1);
            // $(".zk-global-loading").removeClass('hide');
            // $(".zk-global-loading").show();

            ++i;
            $zk.zkAjax({
                url: '/zk/serCen/data',
                // method: 'GET',
                data: {},
                /* dataType: 'JSON', */
                async: true,
                success: function(result, status, xhr){
                    // $zk.zkLoading(0);
                    $zk.zkShowMsg(result.msg, "info");
                    $("#ajax-message").html(result.msg);
                },
                error: function(xhr, status, err){
                    // $zk.zkLoading(0);
                    console.log("[>_<:20190102-1658-001] ajax fail ---", xhr, status, err);
                }
            }, i%2 == 0);
        }

        var msgI = 0;
        function f_showMsg(){
            var orientation = ['', 'top', 'top-left', 'top-right', 'bottom-right'];
            var type = ['', 'error', 'info', 'warning', 'success', 'info'];
            $zk.zkShowMsg("测试消息显示", type[msgI%6], 5000, orientation[msgI%5]);
            ++msgI;
        }

        function f_confirm(){
            $zk.zkConfirm({
                type:'warning',
                msg: 'test'
            });
        }
		
	</script>
    <style type="text/css">
        /*.zk_modal_content_i {
            position: absolute;
            top: 16px;
            left: 15px;
            width: 30px;
            height: 30px;
            display: inline-block;
            vertical-align: top;
            font-style: italic;
            background: url(/${ctxStatic}/zk/images/icon.png) no-repeat;
        }*/
    </style>
</head>
<body style="position: relative;">
    <div class="zk_wrapper" style="position: relative;">
    	<h1>Hello Spring boot JSP</h1>
    	<h1>欢迎来到 AccessReal Server Central</h1>
    	======================================================<br />
    	<h1>1、Java 返回的属性：${testName}</h1>
    	<h1 id="reqAttribute" >2、reqAttribute：</h1>
    	<h1>3、ARStringUtils 将 "ZKStringUtils" 转为小写：<%=ZKStringUtils.toLowerCase("ZKStringUtils") %></h1>
    	<br />
    	<label> theme </label>
    	<select onchange="themeSelect(this.options[this.options.selectedIndex].value)" >
            <option value="aqua">aqua</option> 
            <option value="silvery">silvery</option>
            <option value="gray">gray</option>
            <option value="gray2014">gray2014</option>
        </select>
        
        &nbsp;&nbsp;&nbsp;&nbsp;
        <label>language：</label>
        <select id="languageSelect" onchange="languageSelect(this.options[this.options.selectedIndex].value)" >
            <option value="zh_CN">简体中文</option> 
            <option value="en_US">English</option>
        </select>
        <br /><br />
        contextPath:&nbsp;&nbsp;${(contextPath == null || contextPath == "")?"根路径":contextPath}<br />
        servletPath:&nbsp;&nbsp;${servletPath}<br />
        request.servletPath:&nbsp;&nbsp;${request.servletPath}<br />
    	request.requestURI:&nbsp;&nbsp;${requestURI}<br />
        <br /><br />
        view.msg.test.base:&nbsp;&nbsp;<spring:message code="view.msg.test.base" /><br />
        view.msg.test.msg:&nbsp;&nbsp;<spring:message code="view.msg.test.msg" /><br /> 
        <br /><br />
        <div class="zk-loading zk-global-loading"></div>

        <div id="ajax-message"></div><br />
        <button onclick="f_ajaxTest()">ajax-test</button><br />
        <br />
        <button onclick="f_showMsg()">showMsg</button><br />
        <br />
        <button onclick="f_confirm()">confirm</button><br />


         <br /><br /><br /><br /><br /><br /><br />
    </div>
</body>
</html>
<!-- jsp 执行顺序

1.java是在服务器端运行的代码，而javascript和html都是在浏览器端运行的代码。所以加载执行顺序是是java>js=html。即写在<%-- <%%> --%>中的Java代码优先加载
2.jsp中页面从上到下执行。
3.js加载的顺序也就是页面中<script>标签出现的顺序。<script>标签里面的或者是引入的外部js文件的执行顺序都是其语句出现的顺序，其中js执行的过程也是页面装载的一部分。
4.onload指示页面包含图片等文件在内的所有元素都加载完成。
 -->
 
<!-- JSP中一共预先定义了9个对象
	分别为：request、response、session、application、out、pagecontext、config、page、exception

内置对象（又叫隐含对象)特点: 
1. 由JSP规范提供,不用编写者实例化。 
2. 通过Web容器实现和管理 
3. 所有JSP页面均可使用 
4. 只有在脚本元素的表达式或代码段中才可使用(<%-- <%=使用内置对象%>或<%使用内置对象%> --%>)

1、request对象
request 对象是 javax.servlet.httpServletRequest类型的对象。 该对象代表了客户端的请求信息，主要用于接受通过HTTP协议传送到服务器的数据。（包括头信息、系统信息、请求方式以及请求参数等）。request对象的作用域为一次请求。

2、response对象
response 代表的是对客户端的响应，主要是将JSP容器处理过的对象传回到客户端。response对象也具有作用域，它只在JSP页面内有效。

3、session对象
session 对象是由服务器自动创建的与用户请求相关的对象。服务器为每个用户都生成一个session对象，用于保存该用户的信息，跟踪用户的操作状态。session对象内部使用Map类来保存数据，因此保存数据的格式为 “Key/value”。 session对象的value可以使复杂的对象类型，而不仅仅局限于字符串类型。

4、application对象
 application 对象可将信息保存在服务器中，直到服务器关闭，否则application对象中保存的信息会在整个应用中都有效。与session对象相比，application对象生命周期更长，类似于系统的“全局变量”。

5、out 对象
out 对象用于在Web浏览器内输出信息，并且管理应用服务器上的输出缓冲区。在使用 out 对象输出数据时，可以对数据缓冲区进行操作，及时清除缓冲区中的残余数据，为其他的输出让出缓冲空间。待数据输出完毕后，要及时关闭输出流。

6、pageContext 对象
pageContext 对象的作用是取得任何范围的参数，通过它可以获取 JSP页面的out、request、reponse、session、application 等对象。pageContext对象的创建和初始化都是由容器来完成的，在JSP页面中可以直接使用 pageContext对象。

7、config 对象
config 对象的主要作用是取得服务器的配置信息。通过 pageConext对象的 getServletConfig() 方法可以获取一个config对象。当一个Servlet 初始化时，容器把某些信息通过 config对象传递给这个 Servlet。 开发者可以在web.xml 文件中为应用程序环境中的Servlet程序和JSP页面提供初始化参数。

8、page 对象
page 对象代表JSP本身，只有在JSP页面内才是合法的。 page隐含对象本质上包含当前 Servlet接口引用的变量，类似于Java编程中的 this 指针。

9、exception 对象
exception 对象的作用是显示异常信息，只有在包含 isErrorPage="true" 的页面中才可以被使用，在一般的JSP页面中使用该对象将无法编译JSP文件。excepation对象和Java的所有对象一样，都具有系统提供的继承结构。exception 对象几乎定义了所有异常情况。在Java程序中，可以使用try/catch关键字来处理异常情况； 如果在JSP页面中出现没有捕获到的异常，就会生成 exception 对象，并把 exception 对象传送到在page指令中设定的错误页面中，然后在错误页面中处理相应的 exception 对象。
=====================================
对象名	功能	类型	作用域
request	向客户端请求数据	javax.servlet.ServletRequest	Request
response	封装了jsp产生的响应,然后被发送到客户端以响应客户的请求	javax.servlet.SrvletResponse	Page
pageContext	为JSP页面包装页面的上下文。管理对属于JSP中特殊可见部分中己经命名对象的该问	javax.servlet.jsp.PageContext	Page
session	用来保存每个用户的信息,以便跟踪每个用户的操作状态	javax.servlet.http.HttpSession	Session
application	应用程序对象	javax.servlet.ServletContext	Application
out	向客户端输出数据	javax.servlet.jsp.JspWriter	Page
config	表示Servlet的配置,当一个Servlet初始化时,容器把某些信息通过此对象传递给这个Servlet	javax.servlet.ServletConfig	Page
page	Jsp实现类的实例,它是jsp本身,通过这个可以对它进行访问	javax.lang.Object	Page
exception	反映运行的异常	javax.lang.Throwable	Page
二,解析几个内置对象
1)out对象对象,对象类型是JspWriter类,相当于带缓存的PrintWriter(不带缓存)
PrintWriter:write("内容")           直接向浏览器输出内容

JspWriter:writer("内容")            向jsp缓冲区写出内容

JspWriter当满足以下条件时之一时,缓冲区的内容写出:

a,缓冲区满了

b,刷新缓冲区

c,关闭缓冲区

d,jsp页面执行完毕

2)pageContext对象
pageContext的对象类型是PageContext,叫jsp的上下文对象.

pageContext作用:可以获取其他八个内置对象

//示例:  
pageContext.getOut();  
pageContext.getServletConfig()  
使用场景:在自定义标签时会频繁使用到PageContext对象;或者是定义一个方法需要用到多个对象时,传一个pageContext对象就能解决问题.

三,JSP中四大域对象
分类:
ServletContext     context域  
HttpServletRequet  request域  
HttpSession        session域     --前三种在学习Servlet时就能接触到  
PageContext        page域     --jsp学习的  
域对象的作用:保存数据,获取数据,共享数据.

保存数据:
Context.setAttribute("内容");//默认保存到page域  
pageContext.setAttribute("内容",域范围常量);//保存到指定域中  
//四个域常量  
PageContext.PAGE_SCOPE  
PageContext.REQUEST_SCOPE  
PageContext..SESSION_SCOPE  
PageContext.APPLICATION_SCOPE  
获取数据:
pageContext.getAttribute("内容");  
pageContext.getAttribute("name",域范围常量);

//自动在四个域中搜索数据 pageContext.findAttribute("内容");//在四个域中自动搜索数据,顺序:page域->request域->session域->application域(context域)

域作用范围:
page域:    只能在当前jsp页面使用                (当前页面)  
request域: 只能在同一个请求中使用               (转发)  
session域: 只能在同一个会话(session对象)中使用  (私有的)  
context域: 只能在同一个web应用中使用            (全局的)

 -->



