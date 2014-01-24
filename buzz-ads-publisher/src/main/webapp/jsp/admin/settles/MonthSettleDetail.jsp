<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>Monthly Settlement Details</title>

<div class="container-body">
	<div class="spacer20"></div>
	
	<div>
	    <s:if test="%{#request.currentPageRole == 'admin'}">
            <%@ include file="/jsp/finance/LeftMenu.jsp"%>
        </s:if>
        <s:elseif test="%{#request.currentPageRole == 'finance'}">
            <%@ include file="/jsp/finance/LeftFinanceMenu.jsp"%>
        </s:elseif>
        <s:else>
        </s:else>
        
		<div class="container-right" style="overflow:auto;">
		   	<div class="overview">
		   		 <table class="bTable info" style="width:auto;">
			    	<tr>
	                    <td>账户名:</td>
	                    <td style="padding-left:0px;"><span class="heading1">${account}</span></td>
	                </tr>
	                <tr>
	                    <td>已支付佣金:</td>
	                    <td style="padding-left:0px;"><span class="heading1">¥${totalCommissionDouble}</span></td>
	                </tr>
	            </table>
	            <div class="clear spacer15"></div>
	            
	            <div>显示 <span style="color:red;font-weight:bold;"><s:property value="#request['monthSettles'].size()"/></span> 条结算详情.</div>
	            <div class="clear spacer15"></div>
	            
				<table class="bTable">
					<tr class="heading">
						<th>月份</th>
						<th class="textRight">CPS计次</th>
						<th class="textRight">CPS收入</th>
						<th class="textRight">CPC计次</th>
						<th class="textRight">CPC收入</th>
						<th class="textRight">CPM计次</th>
                        <th class="textRight">CPM收入</th>
						<th class="textRight">站长佣金</th>
						<th style="text-align:center">支付状态</th>
						<th style="text-align:left">支付时间</th>
						<th style="text-align:left">支付流水号</th>
						<th style="text-align:left">支付备注</th>
					</tr>
					<tr class="heading-float fixed hidden" style="top:0;"></tr>
					
					<s:if test="#request['monthSettles'].size() > 0">
						<s:iterator value="#request['monthSettles']" var="st" status="status" >
						<tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
							<td><s:date name="#st.month" format="yyyy-MM"/></td>
							<td class="textRight"><s:text name="global.format.number"><s:param value="%{#st.cpsNo}"/></s:text></td>
							<td class="textRight">¥${st.cpsCommissionDouble}</td>
							<td class="textRight"><s:text name="global.format.number"><s:param value="%{#st.cpcNo}"/></s:text></td>
							<td class="textRight">¥${st.cpcCommissionDouble}</td>
							<td class="textRight"><s:text name="global.format.number"><s:param value="%{#st.cpmNo}"/></s:text></td>
                            <td class="textRight">¥${st.cpmCommissionDouble}</td>
                            <td class="textRight">¥${st.totalCommissionDouble}</td>
							<td style="text-align:center">
							  <s:if test="#st.status.code == 0">
							      <span style="color:red;">未支付</span>
							  </s:if>
							  <s:else>
							      <span style="color:green;">已支付</span>
							  </s:else>
							</td>
							<td style="text-align:left"><s:date name="#st.payTime" format="yyyy-MM-dd HH:mm:ss"/></td>
							<td style="text-align:left" title="${st.receiptNo}"><core:partOfText value="${st.receiptNo}" textmaxlength="30"/></td>
							<td style="text-align:left" title="${st.comment}"><core:partOfText value="${st.comment}" textmaxlength="30"/></td>
						</tr>
						</s:iterator>
					</s:if>
					<s:else>
	                    <tr>
	                        <td colspan="10" class="no-data">暂无数据</td>
	                    </tr>
	                </s:else>
				</table>
		        <div class="clear spacer15"></div>
				<a class="bButton" href="javascript:history.back(-1)">返回</a>
	            <div class="clear"></div>
		   	</div>
		</div>
		<div class="clear"></div>
	</div>
	<div class="clear spacer20"></div>
</div>
<div class="clear"></div>

<script type="text/javascript" charset="utf-8">
	var elemTop = $(".bTable .heading").offset().top;
	
	function isScrolledOut($elem) {
	    var docViewTop = $(window).scrollTop();
	    return elemTop <= docViewTop;
	}

	$(function() {
		$("#leftmenu-finance-historical").addClass("active");
        $("#topmenu-finance-pay").addClass("active");
		
		$(window).resize(function() {
            var html = "";
            $(".bTable .heading th").each(function() {
                html += "<th style='font-size:12px;text-align:" + $(this).css("text-align") + ";width:" + $(this).width() + "px;'>" + $(this).text() + "</th>";
            });
            $(".heading-float").html(html);
            $(".heading-float").css("left", $(".bTable .heading th").offset().left);
        }).resize();
        $(document).scroll(function() {
            if(isScrolledOut($(".bTable.heading"))) {
                $(".bTable .heading-float").show();
            } else {
                $(".bTable .heading-float").hide();
            }
        });
	});
</script>
