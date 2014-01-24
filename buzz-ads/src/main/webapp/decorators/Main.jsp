<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ include file="/jsp/common/Init.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="Content-Language" content="zh" />
    <meta name="description" content="<decorator:getProperty property="meta.description" default="" />" />
    <meta name="keywords" content="<decorator:getProperty property="meta.keywords" default="" />" />
	<meta name="author" content="Buzzinate" />

    <title><decorator:title default="Buzzinate Ads"/></title>

    <link rel="icon" href="${cxt_path}/favicon.ico" type="image/x-icon" />
    <link rel="shortcut icon" href="${cxt_path}/favicon.ico" type="image/x-icon" />
    <link rel="stylesheet" href="${css_path}/buzzads.css" type="text/css" />
	<script type="text/javascript" charset="utf-8" src="${jquery_src_path}"></script>
	
	<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.tooltip.min.1.2.7.js"></script>
</head>

<body style="background: #f9f9f9;">
	<div class="container-center">
		<noscript>
			<span style="color:red;"><strong>此页面使用Javascript。请在您的浏览器中启用Javascript，以浏览全页面。谢谢！</strong></span>
			<div class="spacer10"></div>
		</noscript>
	</div>
    <div class="loginCon">
    	<sec:authorize ifAnyGranted="ROLE_USER">
            <span class="left welcome">
                Hi, <font style="font-weight:bold;"><sec:authentication property="principal.username" /></font>！欢迎来BuzzAds广告系统！
                <sec:authorize ifAllGranted="ROLE_AD_ADMIN">
                <font style="color:#6a6a6a; margin:0 10px;">|</font><span class="identity" id="identity">您的身份：<font style="color:#f60;">管理员</font>
                </sec:authorize>
                <sec:authorize ifNotGranted="ROLE_AD_ADMIN,ROLE_AD_FINANCE,ROLE_PUBLISHER">
                <font style="color:#6a6a6a; margin:0 10px;">|</font><span class="identity" id="identity">您的身份：<font style="color:#f60;">访客</font>
                </sec:authorize>
                <font style="color:#6a6a6a; margin:0 10px;">|</font><span style="color:#fff;" id="userId">用户ID：<sec:authentication property="principal.userId" /></span></span>
                <span>
                    <sec:authorize ifAnyGranted="ROLE_ADVERTISER">
                        <font style="color:#6a6a6a; margin:0 10px;">|</font> <span>切换到：</span>
                    </sec:authorize>
                    <sec:authorize ifAnyGranted="ROLE_ADVERTISER">
                        <a class="bLink"  href="${cxt_path}/advertiser/dashboard">广告主</a>
                    </sec:authorize> 
               </span>
           </span>
           <span class="right helpCon" >
              <!--  <a class="help" target="_blank" href="http://help.buzzads.com">帮助中心</a> &nbsp; | &nbsp;--> <a class="help" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=800087176&amp;site=qq&amp;menu=yes">在线客服</a> &nbsp; | &nbsp; <a class="help" id="logout" class="" title="注销<sec:authentication property="principal.username" />" href="${cxt_path}/logout" >注销</a> &nbsp; 
           </span>
        </sec:authorize>
    	<sec:authorize ifNotGranted="ROLE_USER">
            <span class="left welcome" >欢迎来BuzzAds广告系统！</span>
            <span class="right helpCon" >
              <!-- <a class="help" target="_blank" href="http://help.buzzads.com">帮助中心</a> &nbsp; | &nbsp; --><a class="help" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=800087176&amp;site=qq&amp;menu=yes">在线客服</a> &nbsp; 
            </span>
        </sec:authorize>
		
    </div>
    <div class="logoContainer">
        <div class="spacer10"></div>
        <div class="logoBg left">
            <a class="bLink inlineBlock" href="${cxt_path}/advertiser/dashboard"><img class="logoImg" src="${image_path}/logo.png"/></a>
            <span class="logoTitle inlineBlock">广告主平台</span>
        </div>
        <div class="spacer10 clear"></div>
    </div>
	<div class="clear"></div>
	
	<decorator:body />
	
	<div class="clear"></div>
	
	<%@ include file="/jsp/common/MainFooter.jsp"%>
	
	<div class="clear"></div>
	
	<!-- Global floating notification system -->
    <%@ include file="/jsp/common/MainNotifications.jsp" %>
    <!-- Global floating notification system -->
    
	<!-- 
	<script type="text/javascript" charset="utf-8">
		var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
		document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
	</script>
	<script type="text/javascript" charset="utf-8">
		try {
			var pageTracker = _gat._getTracker("${googleAnalyticsId}");
			pageTracker._setDomainName("${bshareWwwDomain}");
			pageTracker._trackPageview();
		} catch(err) {}
	</script>
	 -->
	 <script type="text/javascript">
	  RC.topTip();
	 </script>
</body>
</html>
