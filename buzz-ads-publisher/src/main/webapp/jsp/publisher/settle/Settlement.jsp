
<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>结算记录 - 结算中心</title>

<div class="container-body">
	<div class="spacer20"></div>
	<%@ include file="/jsp/publisher/settle/LeftMenu.jsp"%>
    <div class="container-right" style="overflow:auto;">
        <div class="overview">
            <p class="heading4">付款规则说明</p>
            <div class="spacer10"></div>
			<p>1. 佣金每月支付一次</p>
			<div class="spacer10"></div>
			<p>2. 最低支付额为100元（含）；如果您当月佣金未达到100元，则自动累计到下个月，直至余额累计达到最低支付额100元</p>
        </div>
        <div class="clear spacer15"></div>
        <div class="overviewPanel details" style="border-top-width: 1px;">
            <div class="panelTitle">结算记录</div>
        </div>
        <div class="overview" style="border-top:0px">
            <div class="clear spacer10"></div>
            <table class="bTable">
                <tr class="heading">
                    <th>结算月份</th> 
                    <th>支付时间</th> 
                    <th class="textRight">应付金额</th>
                    <th class="textRight">税</th>
                    <th class="textRight">手续费</th>
                    <th class="textRight">实付金额</th>
                    <th>流水号</th> 
                    <th>备注</th>
                </tr>
                <s:if test="#request.payments != null && #request.payments.size > 0">
                    <s:iterator value="#request.payments" var="payment" status="status">
                      <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
                          <td style="text-align:left">${payment.period}</td>
                          <td style="text-align:left"><s:date name="#payment.paymentTime" format="yyyy-MM-dd" /></td>
                          <td style="text-align:right">￥${payment.amountDouble}</td>
                          <td style="text-align:right">￥${payment.taxDouble}</td>
                          <td style="text-align:right">￥${payment.feeDouble}</td>
                          <td style="text-align:right">￥${payment.paidDouble}</td>
                          <td style="text-align:left" title="${payment.receiptNo}"><core:partOfText value="${payment.receiptNo}" textmaxlength="15" escape="true"/></td>
                          <td style="text-align:left" title="${payment.comment}"><core:partOfText value="${payment.comment}" textmaxlength="30" escape="true"/></td>
                     </tr>
                   </s:iterator>
                </s:if>
                <s:else>
                    <tr>
                        <td colspan="8" class="no-data">暂无数据</td>
                    </tr>
                </s:else>
            </table>
            <div class="clear spacer15"></div>
        </div>
        <div class="overviewPanel details">
            <div class="panelTitle">收入记录</div>
        </div>
        <div class="overview" style="border-top:0px">
            <div class="clear spacer10"></div>
            <table class="bTable">
                <tr class="heading">
                    <th>结算月份</th> 
                    <th class="textRight">收入金额</th> 
                    <th style="text-align:center">状态</th>
                    <th>支付时间</th>
                </tr>
                <s:if test="#request.settlements != null && #request.settlements.size > 0">
                    <s:iterator value="#request.settlements" var="settlement" status="status">
                      <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
                        <td><s:date name="#settlement.month" format="yyyy-MM" /></td>
                        <td class="textRight">¥${settlement.totalCommissionDouble}</td>
                        <td style="text-align:center">
                            <s:if test ="#settlement.status.code != 0"><font style="color:green;">已支付</font></s:if>
                            <s:else><font style="color:red;">未支付</font></s:else>
                        </td>
                        <td><s:date name="#settlement.payTime" format="yyyy-MM-dd" /></td>
                     </tr>
                    </s:iterator>
                </s:if>
                <s:else>
                    <tr>
                        <td colspan="4" class="no-data">暂无数据</td>
                    </tr>
                </s:else>
            </table>
            <div class="clear spacer15"></div>
        </div>
        <div class="clear spacer5"></div>
        <div class="textRight"><font class="disabled">数据生成时间&nbsp;&nbsp;<span id="creationDate"></span></font></div>
        <div class="clear spacer15"></div>
    </div>
    <div class="clear spacer20"></div>
</div>
<script type="text/javascript">
	//menu active
	$("#topmenu-publisher-settlement").addClass("active");
	$("#leftmenu-publisher-settlement").addClass("active");
	
	$(function() {
		function getDate(){
	        var dateTemp=new Date();
	        return dateTemp.getFullYear() 
	            + "-" + ('0'+(dateTemp.getMonth()+1)).slice(-2)
	            + "-" + ('0'+dateTemp.getDate()).slice(-2)
	            + " " + ('0'+dateTemp.getHours()).slice(-2)
	            + ":" + ('0'+dateTemp.getMinutes()).slice(-2)
	            + ":" + ('0'+dateTemp.getSeconds()).slice(-2);
	    }
	    $("#creationDate").html(getDate());
	});
</script>