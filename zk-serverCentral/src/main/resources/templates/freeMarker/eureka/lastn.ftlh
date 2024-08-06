<#import "/spring.ftl" as spring />
<#import "/freeMarker/zkGlobal.ftlh" as _zk />  
<!doctype html>
<html lang="en">
  <head>
    <base href="<@spring.url basePath/>">
    <title>Eureka - Last N events</title>
    <link rel="stylesheet" type="text/css" href="eureka/css/wro.css">
    
    <#include "/freeMarker/include.ftlh">  
    
  </head>
  <body id="three" class="zk_layout_body_scroll">
    <script type="text/javascript">
      if ( document.documentMode ) {
         document.write('<p>You are using an <strong>outdated</strong> browser. Please upgrade your browser to improve your experience.</p>');
      }
    </script>
	<!-- Vinson
    <#include "header.ftlh"> -->

    <div class="container-fluid xd-container">
      <#include "navbar.ftlh">

    <div id="xd-jobs" class="tab-pane col-md-12">
        <ul class="nav nav-tabs" role="tablist" id="myTab">
          <li class="nav-item"       ><a href="javaScript:void(0);" onClick="selTab(this, 'cancelled');return false;"  class="p-2"  data-bs-toggle="tab">Last 1000 cancelled leases</a></li>
          <li class="nav-item active"><a href="javaScript:void(0);" onClick="selTab(this, 'registered');return false;" class="p-2 active" data-bs-toggle="tab">Last 1000 newly registered leases</a></li>
        </ul>
        <div class="tab-content">
          <div class="tab-pane" id="cancelled">
            <table id='lastNCanceled' class="table table-striped table-hover">
              <thead>
                <tr><th>Timestamp 1</th><th>Lease</th></tr>
              </thead>
              <tbody>
                <#if lastNCanceled?has_content>
	                <#list lastNCanceled as entry>
	                  <tr><td>${entry.date?datetime}</td><td>${entry.id}</td></tr>
	                </#list>
	            <#else>
	              <tr><td colspan="2">No results available</td></tr>
	            </#if>
              <tbody>
            </table>
          </div>
          <div class="tab-pane active" id="registered">
            <table id='lastNRegistered' class="table table-striped table-hover">
              <thead>
                <tr><th>Timestamp 2</th><th>Lease</th></tr>
              </thead>
              <tbody>
                <#if lastNRegistered?has_content>
                  <#list lastNRegistered as entry>
                    <tr><td>${entry.date?datetime}</td><td>${entry.id}</td></tr>
                  </#list>
                <#else>
	              <tr><td colspan="2">No results available</td></tr>
	            </#if>
              </tbody>
            </table>
          </div>
      </div>
    </div>
    </div>
    <script type="text/javascript" src="eureka/js/wro.js" ></script>
    <script type="text/javascript">
          
      function selTab(_this, tabId){
      	var liDom = $(_this).parent();
      	liDom.addClass("active");
      	liDom.siblings().removeClass("active");
      	var tabDom = $("#" + tabId);
      	tabDom.addClass("active");
      	tabDom.siblings().removeClass("active");
      }
       
    </script>
  </body>
</html>


