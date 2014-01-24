<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>ChaNet Transactions</title>

<div class="container-body">
    <div class="spacer20"></div>
    
    <div>
        <%@ include file="/jsp/admin/orders/LeftMenu.jsp"%>
        
        <div class="container-right" style="overflow:auto;">
        	<div class="overview">
        		成果网交易记录：<span style="color:#f80;"><s:property value="#request['chanetTransactions'].size()"/></span>
        	</div>
        	 <div class="clear spacer15"></div>
        	<div class="overview">
        		<table class="bTableInfo" style="font-size:14px;">
	                <tr>
	                    <td>总佣金</td>
	                    <td><font class="unit">¥</font><font class="number"> <s:text name="global.format.money"><s:param value="%{#request.totalCommission/100}"/></s:text></font></td>
	                </tr>
	                <tr>
	                    <td>总价格</td>
	                    <td><font class="unit">¥</font><font class="number"> <s:text name="global.format.money"><s:param value="%{#request.totalPrice/100}"/></s:text></font></td>
	                </tr>
	            </table>
        	</div>
        	<div class="spacer15" ></div>
        	<div class="overviewPanel details" style="border-top-width:1px;">
			 	<div  class="panelTitle">数据汇总</div>
			</div>
           <div class="overview" style="border-top:0px;">
	            <div class="clear spacer10"></div>
	            <table class="bTable textRight">
	                <tr class="heading">
	                    <th></th>
	                    <th style="text-align:center">ID</th>
	                    <th class="textRight">Date Time</th>
	                    <th class="textRight">Billing Type</th>
	                    <th class="textRight">Campaign ID</th>
	                    <th style="text-align:left">Campaign Name</th>
	                    <th style="text-align:left">Ext Info</th>
	                    <th style="text-align:left">User Info</th>
	                    <th class="textRight">Commission</th>
	                    <th class="textRight">Total Price</th>
	                    <th style="text-align:left">OCD</th>
	                    <th class="textRight">Status</th>
	                    <th class="textRight">Paid</th>
	                    <th class="textRight">Confirm</th>
	                    <th style="text-align:center;">Action</th>
	                </tr>
	                <tr class="heading-float fixed hidden" style="top:0;"></tr>
	                
	                <s:if test="#request['chanetTransactions'].size() > 0">
		                <s:iterator value="#request['chanetTransactions']" var="iterObj" status="status" >
			                <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
			                    <td><input type="checkbox"/></td>
			                    <td style="text-align:center">${iterObj.id}</td>
			                    <td><s:date name="#iterObj.datetime" format="yyyy-MM-dd HH:mm:ss"/></td>
			                    <td>${iterObj.billingType}</td>
			                    <td>${iterObj.campaignId}</td>
			                    <td style="text-align:left"><span title="${iterObj.campaignName}"><core:partOfText value="${iterObj.campaignName}" textmaxlength="10"/></span></td>
			                    <td style="text-align:left"><span title="${iterObj.extinfo}"><core:partOfText value="${iterObj.extinfo}" textmaxlength="10"/></span></td>
			                    <td style="text-align:left"><span title="${iterObj.userinfo}"><core:partOfText value="${iterObj.userinfo}" textmaxlength="10"/></span></td>
			                    <td>¥<s:text name="global.format.money"><s:param value="%{#iterObj.commission/100}"/></s:text></td>
			                    <td>¥<s:text name="global.format.money"><s:param value="%{#iterObj.totalPrice/100}"/></s:text></td>
			                    <td style="text-align:left"><span title="${iterObj.ocd}"><core:partOfText value="${iterObj.ocd}" textmaxlength="15"/></span></td>
			                    <td>${iterObj.status}</td>
			                    <td>${iterObj.paid}</td>
			                    <td>${iterObj.confirm}</td>
			                    <td style="text-align:center;">-</td>
			                </tr>
		                </s:iterator>
	                </s:if>
	                <s:else>
	                    <tr>
	                        <td colspan="17" class="no-data">暂无数据</td>
	                    </tr>
	                </s:else>
	            </table>
			    <div class="clear spacer5"></div>
	            <div id="pagination" class="right pagination"></div>
	            <div id="pageDesCon" class="right"></div>
	            <div class="clear spacer15"></div>
	        </div>
	        <div><font class="disabled textRight">数据生成时间&nbsp;&nbsp;<span id="creationDate"></span></font></div>
	        <div class="clear spacer15"></div>
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
		$("#leftmenu-admin-networks").addClass("active");
		$("#topmenu-admin-ads").addClass("active");
	    
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
