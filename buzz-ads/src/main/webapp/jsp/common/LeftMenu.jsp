<%@ include file="/jsp/common/Init.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<div class="left" style="margin:0 5px;">
	<sec:authorize ifAnyGranted="ROLE_PUBLISHER">
		<%@ include file="/jsp/publisher/home/LeftMenu.jsp"%>
	</sec:authorize>
	<sec:authorize ifAnyGranted="ROLE_AD_ADMIN">
		<a id="leftmenu-admin-orders" class="menu-left-button" href="${cxt_path}/admin/orders">订单管理</a>
		<div class="clear spacer10"></div>
		<a id="leftmenu-admin-orders-entry" class="menu-left-button" href="${cxt_path}/admin/orders/entry">广告管理</a>
		<div class="clear spacer10"></div>
	    <a id="leftmenu-admin-networks" class="menu-left-button" href="${cxt_path}/admin/networks">广告联盟管理</a>
		<div class="clear spacer10"></div>
		<a id="leftmenu-admin-manage" class="menu-left-button" href="${cxt_path}/admin/publisher">会员管理</a>
	    <div class="clear spacer10"></div>
	    <a id="leftmenu-admin-stats" class="menu-left-button" href="${cxt_path}/admin/stats">数据统计</a>
		<div class="clear spacer10"></div>
	</sec:authorize>
	<sec:authorize ifAnyGranted="ROLE_AD_FINANCE,ROLE_AD_ADMIN">
	   <a id="leftmenu-admin-settles" class="menu-left-button" href="${cxt_path}/settlement/settles">结账数据</a>
        <div class="clear spacer10"></div>
	</sec:authorize>
</div>
