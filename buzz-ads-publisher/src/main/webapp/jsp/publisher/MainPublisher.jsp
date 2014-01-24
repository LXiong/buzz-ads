<%@ include file="/jsp/common/Init.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<s:if test="%{#request.hasContactInfo != null && #request.hasContactInfo == false}">
</s:if>
<s:else>
    <a id="topmenu-publisher-home" class="top-menu-button heading3" href="${cxt_path}/publisher/home">首页</a>
	<!-- <a id="topmenu-publisher-stats" class="top-menu-button heading3" href="${cxt_path}/publisher/stats">效果报告</a>  -->
	<a id="topmenu-publisher-ads" class="top-menu-button heading3" href="${cxt_path}/publisher/ads">广告管理</a>
	<a id="topmenu-publisher-stats" class="top-menu-button heading3" target="_blank" href="http://www.lezhi.me/user/manage">乐知管理</a>
	<a id="topmenu-publisher-settlement" class="top-menu-button heading3" href="${cxt_path}/publisher/settlement">财务管理</a>
</s:else>
 <a id="topmenu-publisher-settings" class="top-menu-button heading3" href="${cxt_path}/publisher/settings">账户设置</a>