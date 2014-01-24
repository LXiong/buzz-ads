<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>ChaNet Campaigns</title>

<div class="container-body">
    <div class="spacer20"></div>
    
    <div>
        <%@ include file="/jsp/admin/orders/LeftMenu.jsp"%>
        
        <div class="container-right" style="overflow:auto;">
        	<div class="overview">
        		成果网广告活动数：<span style="color:#f80;"><s:property value="#request['chanetCampaigns'].size()"/></span>
        	</div>
        	 <div class="clear spacer15"></div>
			 <div class="overviewPanel details" style="border-top-width:1px;">
			 	<div class="panelTitle">数据汇总</div>
			 </div>
          	 <div class="overview" style="border-top:0px;">
	            <div class="clear spacer10"></div>
	            <table class="bTable ellipsis">
	                <tr class="heading">
	                    <th></th>
	                    <th>Order ID</th>
	                    <th>Advertiser</th>
	                    <th>Campaign ID</th>
	                    <th>Campaign Name</th>
	                    <th>Campaign Domain</th>
	                    <th>Site ID</th>
	                    <th>Site Name</th>
	                    <th>Advertiser Link</th>
	                    <th>Chanet Link</th>
	                    <th>Campaign Rules</th>
	                    <th>Status</th>
	                    <th style="text-align:center;">Action</th>
	                </tr>
	                <tr class="heading-float fixed hidden" style="top:0;"></tr>
	                
	                <s:if test="#request['chanetCampaigns'].size() > 0">
		                <s:iterator value="#request['chanetCampaigns']" var="iterObj" status="status" >
			                <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
			                    <td><input type="checkbox"/></td>
			                    <td>${iterObj.orderId}</td>
			                    <td><span title="${iterObj.advertiser}"><core:partOfText value="${iterObj.advertiser}" textmaxlength="10"/></span></td>
			                    <td>${iterObj.campaignId}</td>
			                    <td><span title="${iterObj.campaignName}"><core:partOfText value="${iterObj.campaignName}" textmaxlength="10"/></span></td>
			                    <td><a target="_blank" class="bLinkU" title="${iterObj.campaignDomain}" href="<core:getHref value="${iterObj.campaignDomain}"/>"><core:partOfText value="${iterObj.campaignDomain}" textmaxlength="20"/></a></td>
			                    <td>${iterObj.siteId}</td>
			                    <td><span title="${iterObj.siteName}"><core:partOfText value="${iterObj.siteName}" textmaxlength="15"/></span></td>
			                    <td><a target="_blank" class="bLinkU" title="${iterObj.advertiserLink}" href="<core:replaceStrings value="${iterObj.advertiserLink}" propertiesList="[UID],,[EID],,[BACKURL],"/>"><core:partOfText value="${iterObj.advertiserLink}" textmaxlength="25"/></a></td>
			                    <td><a target="_blank" class="bLinkU" title="${iterObj.chanetLink}" href="<core:replaceStrings value="${iterObj.chanetLink}" propertiesList="[UID],,[EID],,[BACKURL],"/>"><core:partOfText value="${iterObj.chanetLink}" textmaxlength="25"/></a></td>
	                            <td><span title="${iterObj.ruleXml}"><core:partOfText value="${iterObj.ruleXml}" textmaxlength="10"/></span></td>
			                    <td>${iterObj.status}</td>
			                    <td style="text-align:center;">-</td>
			                </tr>
		                </s:iterator>
	                </s:if>
	                <s:else>
	                    <tr>
	                        <td colspan="13" class="no-data">暂无数据</td>
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
