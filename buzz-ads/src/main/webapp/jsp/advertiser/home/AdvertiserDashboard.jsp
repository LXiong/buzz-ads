<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>广告主数据概览</title>

<div class="container-body">
	<div class="spacer20"></div>
	<%@ include file="/jsp/advertiser/home/LeftMenu.jsp"%>
    <%@ include file="/jsp/advertiser/payment/TopupDialog.jsp"%>
    <div class="container-right" style="overflow: auto;"> 
        <s:if test="#request.balance <= 0">
              <div class="warningMessage div-rounded-5" style="margin:0;"><p>您广告没有投放，因为您账户余额为 ¥ <font class="errorMsg">${balance}</font>，请<a class="advertiser_recharge bLink" href="javascript:void(0)">充值</a>！</p></div>
              <div class="clear spacer10"></div>
        </s:if>
        <s:elseif test="#request.suspendedCampaigns > 0">
              <div class="warningMessage div-rounded-5" style="margin:0;"><p>您当前有超量的广告，请检查您的广告活动的预算以及您的帐户余额！</p></div>
              <div class="clear spacer10"></div>
        </s:elseif>
        <s:elseif test="#request.activeCampaigns == 0">
              <div class="warningMessage div-rounded-5" >
                  <p>您当前没有投放的广告，请&nbsp;<a 
                  class="bLink" href="${cxt_path }/advertiser/campaign/new">创建</a>&nbsp;或&nbsp;<a 
                  class="bLink" href="${cxt_path }/advertiser/campaigns">开启广告</a></p>
              </div>
              <div class="clear spacer10"></div>
        </s:elseif>
        <div class="overview"  >
            <div class="left topUp">
                <span>账户余额：</span>
                <span ><font style="vertical-align:middle;" class="unit  orange">¥</font> <font style="vertical-align:middle;" class=orangeNumber>${request.balance}</font></span>
                <span><a class="bButton lightOrange advertiser_recharge"  style="margin:0px 15px;vertical-align:middle;"  href="javascript:void(0)">充&nbsp;值</a></span>
            </div>
            <div class="left" style="margin-left:40px; border-left: 1px solid #dadada; padding-left: 40px;" >
                <table class="bTableInfo"  style="font-size:14px;">
                    <tbody>
                        <tr>
                            <td>上月的消费</td><td ><font class="unit">¥</font> <font class="number" >${lastTotalStats.totalCommissionDouble }</font></td><td></td>
                        </tr>
                        <tr>
                            <td>当月的消费</td><td><font class="unit">¥</font> <font class="number" >${currentTotalStats.totalCommissionDouble }</font></td>
                            <td >
                                <a class="bLink orange spacer_left50"  href="${cxt_path }/advertiser/billing">查看明细 &gt;&gt;</a>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="clear spacer15"></div>
        <div class="overviewPanel shadow">
            <div class="dataSearch formDate">
                 <form action="dashboard" method="post" id="dashboardForm">
                     <div class="left">
                         <s:select onchange="$('#dashboardForm').submit()" list="camps" listKey="id" listValue="name" value="campaignId" name="campaignId" headerKey="0" headerValue="全部活动" cssClass="bTextbox"></s:select>
                     </div>
                     <script type="text/javascript">
                         currentPath = "advertiser/dashboard";
                         formId="dashboardForm";
                     </script>
                     <%@ include file="/jsp/advertiser/campaigns/CommSearch.jsp"%>
                     <div class="clear"></div>
                 </form>  
            </div> 
        </div>
        <div class="overviewPanel details" style="border-top:0px;">
            <div class="panelTitle">数据汇总</div>
        </div>
        <div class="overview" style="border-top:0px;" >
           <table class="bTableInfo" style="width:100%;" cellpadding="0px" cellspacing="0px">
                <tbody>
                    <tr>
                        <td class="panelTitle">展现量</td>
                        <td class="panelTitle" >点击量</td>
                        <td class="panelTitle" >点击率</td>
                        <td class="panelTitle last" >支出</td>
                    </tr>
                    <tr>
                        <td class="panelCon" style="with:25%;"><s:text name="global.format.number"><s:param value="%{#request.totalStats['views']}"/></s:text></td>
                        <td class="panelCon" style="with:25%;"><s:text name="global.format.number"><s:param value="%{#request.totalStats['clicks']}"/></s:text></td>
                        <td class="panelCon" style="with:25%;">${totalStats.clickToView }</td>
                        <td class="panelCon last" style="with:25%;"><font class="unit">¥</font> ${totalStats.totalCommissionDouble }</td>
                    </tr>
                    
                </tbody>
            </table>
        </div>
    
        <%@ include file="/jsp/advertiser/campaigns/CommStats.jsp"%>
    
        <div class="overviewPanel details" style="border-top:0px;">
            <div class="panelTitle">详细数据</div>
        </div>
        <div class="overview" style="border-top:0px;" >
            <div class="clear spacer10"></div> 
            <table class="bTable textRight">
                <tr class="heading">
                    <th>时间</th>
                    <th class="textRight">展现量</th>
                    <th class="textRight">点击量</th>
                    <th class="textRight">点击率</th>
                    <th class="textRight">支出</th>
                </tr>
                <s:if test="#request['statlist'].size() > 0">
                        <s:iterator value="#request['statlist']" var="stat" status="status" >
                            <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
                                <td style="text-align: left">
                                    ${stat.dateDay}
                                </td> 
                                <td><s:text name="global.format.number"><s:param value="%{#stat.views}"/></s:text></td>
                                <td><s:text name="global.format.number"><s:param value="%{#stat.clicks}"/></s:text></td>
                                <td>${stat.clickToView}</td>
                                <td>¥${stat.totalCommissionDouble}</td>
                            </tr>
                        </s:iterator>
                 </s:if>
                 <s:else>
                        <tr>
                            <td colspan="5" class="no-data">暂无数据</td>
                        </tr>
                 </s:else>
            </table>
            <div class="clear spacer10"></div>
            <form action="${cxt_path}/advertiser/dashboard" id="filterSearchForm" method="post">
                <div id="pagination" class="right pagination"></div>
                <input type="hidden" value="${dateStart}" name="dateStart" />
                <input type="hidden" value="${dateEnd}" name="dateEnd"/>
                <input type="hidden" value="${availableStats}" name="availableStats" />
            </form>
           <div id="pageDesCon" class="right"></div>
           <div class="clear spacer15"></div>
        </div>
        <div><font class="disabled textRight">数据生成时间&nbsp;&nbsp;<span id="creationDate"></span></font></div>
        <div class="clear spacer15"></div>
    </div>
    <div class="clear spacer20"></div>
</div>
<link rel="stylesheet" href="${css_path}/pagination.css" type="text/css" />
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/jquery.pagination.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.toolbox.min.1.2.6.js"></script>

<script type="text/javascript">
	$(function(){
		RC.CutSelect("campaignId");
		
		$("#leftmenu-advertiser-dashboard").addClass("active");
		$("#topmenu-advertiser-home").addClass("active");
		
	    // pagination
	    function initPage(pageNum, pageSize, totalRecords) {
	        var pn = parseInt(pageNum, 10) - 1;
	        var ps = parseInt(pageSize, 10);
	        var tr = parseInt(totalRecords, 10);
	        if (pn < 0) {
	            return;
	        }
	        var opt = {
	            form: $("#filterSearchForm"),
				pageDesCon:$("#pageDesCon"),
	            next_text: "下一页",
	            current_page: pn,
	            items_per_page: ps,
	            num_display_entries: 5,
	            num_edge_entries: 2,
	            prev_text: "上一页"
	        };
	        $("#pagination").pagination(tr, opt);
	    }
	    initPage("${page.pageNum}","${page.pageSize}", "${page.totalRecords}");
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

		$(".advertiser_recharge").click(function(){
		    $("#dialogTopup").data("overlay").load();
		});
	});
</script>
