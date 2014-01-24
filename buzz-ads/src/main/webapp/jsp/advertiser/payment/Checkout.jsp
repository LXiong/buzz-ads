<%@ include file="/jsp/common/Init.jsp" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<title><s:text name="buzzads.recharge.checkout.confirm"/> - <s:text name="buzzads.recharge.name"/></title>

<style>
    .orderInfoTable, .orderInfoItemTable { width: 100%; border-collapse: collapse; text-align: left; }
    .ordbuzzads.recharge.checkout.confirmerInfoTable th, .orderInfoItemTable th { padding: 10px; border-bottom: 1px solid #f90; background: #ffeac0; }
    .orderInfoTable th { padding: 4px 10px; height: 26px; line-height: 26px; }
    .orderInfoItemTable th { background: #F2F6F9; }
    .orderInfoTable td, .orderInfoItemTable td { padding: 10px; border-bottom: 1px solid #eee; }
    .productPrice { font-family: Arial,宋体,sans-serif; }
    #priceTr td { background: #F2F6F9; }
    #discountedPriceTr td { background: #ddf8cc; }
</style>
<div style="background:#fff;">
<div class="container-center" style="width:auto;">
	    <div class="clear spacer20"></div>
	    <div style="padding:0 20px;">
	        <h1 class="heading1 text-blue"><s:text name="buzzads.recharge.checkout.confirm" /></h1>
	        <div class="spacer5 clear"></div>
	        <div style="color:#666;">
	            <s:text name="buzzads.recharge.order.order.no" />${request.paymentOrderInfo.tradeNo}</div>
	    </div>
	    <div class="clear spacer20"></div>
	    <div class="div-gradient-light-top" style="height:20px;"></div>
	    <div style="padding:0 20px;">
	        <div><s:text name="buzzads.recharge.checkout.top" /></div>
	        <div class="spacer5 clear"></div>
	        <div>
	            <p><s:text name="buzzads.recharge.checkout.top.note" /></p>
	        </div>
	        <div class="spacer20"></div>
	        <form id="orderInfoForm" name="orderInfo" action="${cxt_path}/advertiser/checkout">
	            <input type="hidden" id="paymentOrderInfoId" name="paymentOrderInfoId"
	            value="${request.paymentOrderInfo.id}" />
	            <input type="hidden" id="paygateId" name="paygateId" value="" />
	            <table class="orderInfoTable mp0" style="max-width:1000px;">
	                <tr>
	                    <th class="heading3" style="vertical-align:middle;">
	                        <s:text name="buzzads.recharge.checkout.order.no" />
	                    </th>
	                    <th>
	                        <div class="left floatFix">${request.orderNo}</div>
	                        <div class="right floatFix" style="padding:5px;border:1px solid #bbb;background:#eee;height:16px;line-height:16px;">
	                            <a target="_blank" href="mailto:vipsales@${bshareDomain}" class="bLink"
	                            style="font-weight:normal;"><s:text name="buzzads.recharge.checkout.need.receipt"/></a>
	                        </div>
	                    </th>
	                </tr>
	                <tr>
	                    <td class="heading3"><s:text name="buzzads.recharge.checkout.order.time" /></td>
	                    <td>
	                        <s:text name="buzzads.recharge.number.format"><s:param value="%{#request.paymentOrderInfo.createTime}" /></s:text>
	                    </td>
	                </tr>
	                <tr>
	                    <td class="heading3">
	                        <s:text name="buzzads.recharge.checkout.order.details" />
	                    </td>
	                    <td>
	                        <table class="orderInfoItemTable">
	                            <tr>
	                                <th>&nbsp;</th>
	                                <th align="center"><s:text name="buzzads.recharge.order.amount" /></th>
	                                <th align="right"><s:text name="buzzads.recharge.order.total" /></th>
	                            </tr>
	                            <tr>
	                                <td><s:text name="buzzads.recharge.topup.name" /></td>
	                                <td align="center">
	                                    ￥<s:text name="global.format.money"><s:param value="%{amount}" /></s:text>
	                                </td>
	                                <td align="right" class="productPrice">￥<s:text name="buzzads.recharge.number.format">
	                                        <s:param value="%{#request.paymentOrderInfo.amountValue}" /></s:text>
	                                </td>
	                            </tr>
	                        </table>
	                    </td>
	                </tr>
	                <tr id="discountedPriceTr">
	                    <td class="heading3"><s:text name="buzzads.recharge.order.total" /></td>
	                    <td class="productPrice" style="font-size:18px;">
	                        <strong>￥<s:text name="buzzads.recharge.number.format"><s:param value="%{#request.paymentOrderInfo.amountValue}" /></s:text>
	                        </strong>
	                    </td>
	                </tr>
	                <tr style="border-bottom:2px solid #ddd;">
	                    <td class="heading3"><s:text name="buzzads.recharge.checkout.payment.status" /></td>
	                    <td>
	                        <s:if test="%{#request.paymentOrderInfo.tradeStatus == @com.buzzinate.buzzads.domain.enums.TradeStatus@SUCCESS}">
	                            <font style="color:green;"><s:text name="buzzads.recharge.checkout.payment.complete" /></font>
	                        </s:if>
	                        <s:elseif test="%{#request.paymentOrderInfo.tradeStatus == @com.buzzinate.buzzads.domain.enums.TradeStatus@WAITPAY}">
	                            <font style="color:#ff5c00;">
	                                <s:text name="buzzads.recharge.checkout.payment.waiting" />
	                            </font>
	                        </s:elseif>
	                        <s:else>
	                            <font style="color:red;"><s:text name="buzzads.recharge.checkout.payment.failed" /></font>(
	                            <s:text name="buzzads.recharge.order.fail.note" />)</s:else>
	                    </td>
	                </tr>
	            </table>
	        </form>
	        <div class="clear spacer20"></div>
	        <div>
	            <p><s:text name="buzzads.recharge.checkout.bottom.note" /></p>
	        </div>
	        <div class="clear spacer20"></div>
	        <div>
	            <div style="text-align:left;">
	                <span class="heading3"><s:text name="buzzads.recharge.checkout.bottom.note2" /></span>
	                <br /><br />
	                <a class="submitPayButton" onclick="javascript:gotoPaygate('1');return false;"
	                    href="javascript:;"><img src="${image_path}/paygate/alipay_logo.gif"></a>
	            </div>
	        </div>
	    </div>
	    <div class="clear spacer20"></div>
	    <div class="div-gradient-light-top" style="height:20px;"></div>
	    <div class="clear spacer20"></div>
</div>
</div>
<script type="text/javascript" charset="utf-8">
    var isSubmitting = false;
    function gotoPaygate(paygateId) {
        if (isSubmitting) return false;
        
        $("#paygateId").val(paygateId);

        $(".submitPayButton").attr("onclick", ""); //disable from submitting twice
        $("#orderInfoForm").submit();
    }
</script>
