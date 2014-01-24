
<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>广告管理</title>

<div class="container-body">
	<div class="spacer20"></div>
	<%@ include file="/jsp/publisher/ads/LeftMenu.jsp"%>
    <div class="container-right" style="overflow:auto;">
        <div class="overview">
            <p>广告管理</p>
        </div>
        <div class="clear spacer15"></div>
    </div>
    <div class="clear spacer20"></div>
</div>
<script type="text/javascript">
	//menu active
	$("#topmenu-publisher-ads").addClass("active");
	$("#leftmenu-publisher-ads").addClass("active");
</script>