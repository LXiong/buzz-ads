<%@ include file="/jsp/common/Init.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>Ad Networks</title>

<div class="container-body">
	<div class="spacer20"></div>
	
	<div>
		<%@ include file="/jsp/admin/orders/LeftMenu.jsp"%>
		
		<div class="container-right">
		    <div class="overview">
		    	<a class="bLinkU" href="${cxt_path}/admin/networks/chanet/campaigns">ChaNet Campaigns (Original data from us calling their APIs)</a>
			    <div class="clear spacer5"></div>
			    <a class="bLinkU" href="${cxt_path}/admin/networks/chanet/transactions">ChaNet Transactions (From them calling our APIs)</a>
		
				<div class="clear"></div>
		    </div>
		</div>
		<div class="clear"></div>
	</div>
	<div class="clear spacer20"></div>
</div>
<div class="clear"></div>

<script type="text/javascript" charset="utf-8">
	$(function() {
		$("#leftmenu-admin-networks").addClass("active");
		$("#topmenu-admin-ads").addClass("active");
	});
</script>
