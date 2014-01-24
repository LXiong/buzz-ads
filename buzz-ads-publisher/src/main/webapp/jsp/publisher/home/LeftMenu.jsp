<%@ include file="/jsp/common/Init.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<div class="left" style="margin:0 5px;">
	<a id="leftmenu-publisher-stats" class="menu-left-button" href="${cxt_path}/publisher/dashboard">概览</a>
	<div class="clear spacer10"></div>
	<div>
		<a id="leftmenu-publisher-stats-overview" class="parent" href="javascript:void(0)">效果统计</a>
		<div class="clear "></div>
		<a id="leftmenu-publisher-stats-cpm" class="children firstc" href="${cxt_path}/publisher/stats/cpm">CPM明细</a>
	    <div class="clear "></div>
	    <a id="leftmenu-publisher-stats-cpc" class="children" href="${cxt_path}/publisher/stats/cpc">CPC明细</a>
	    <div class="clear "></div>
	    <a id="leftmenu-publisher-stats-cps" class="children lastc" href="${cxt_path}/publisher/stats/cps">CPS明细</a>
		<div class="clear "></div>
	</div>
</div>