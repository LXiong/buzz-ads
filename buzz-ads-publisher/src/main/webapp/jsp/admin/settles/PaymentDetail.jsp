<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>历史支付</title>

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
			<div class="overviewPanel shadow">
				<form id="searchForm" name="searchForm" action="" method="post">
	                <span>账户:</span>
	                <input class="bTextbox" placeholder="Search by email..." style="width:150px;" type="text" name="searchEmail" value="${searchEmail }"/>&nbsp;&nbsp;
	                <span>流水号:</span>
	                <input class="bTextbox" placeholder="Search by receipt no..." style="width:150px;" type="text" name="receiptNo" value="${receiptNo }"/>&nbsp;&nbsp;
	                <input id="paymentSubmit" type="submit" class="bButton bButton24" style="width:80px;" value="查询" />
	            </form> 
			</div>
		    <div class="overview" style="border-top:0px;">
	            <div class="clear spacer10"></div>
				<table class="bTable textRight" >
					<tr class="heading">
						<th>站长信息</th>
						<th>结算周期</th>
						<th class="textRight">应付总额</th>
						<th class="textRight">手续费</th>
						<th class="textRight">税</th>
						<th class="textRight">实付总额</th>
						<th>流水号</th>
						<th>备注</th>
						<th class="textRight">支付时间</th>
						<th style="text-align:center;">操作</th>
					</tr>
					<tr class="heading-float fixed hidden" style="top:0;"></tr>
					
					<s:if test="#request['payments'].size() > 0">
						<s:iterator value="#request['payments']" var="his" status="status" >
						<tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
							<td style="text-align:left">
							    <div class="spacer5"></div>
							    站长账户:&nbsp;<span title="${his.info.email}"><core:partOfText value="${his.info.email}" textmaxlength="30"/></span>
							    <div class="spacer5"></div>
							    收款账户:&nbsp;<span title="${his.info.receiveAccount}"><core:partOfText value="${his.info.receiveAccount}" textmaxlength="30"/></span>
							    <div class="spacer5"></div>
							</td>
							<td style="text-align:left" title="${his.period}">${his.period}</td>
							<td title="¥${his.amountDouble}">¥${his.amountDouble}</td>
							<td title="¥${his.feeDouble}">¥${his.feeDouble}</td>
							<td title="¥${his.taxDouble}">¥${his.taxDouble}</td>
							<td title="¥${his.paidDouble}">¥${his.paidDouble}</td>
							<td style="text-align:left" title="${his.receiptNo}">${his.receiptNo}</td>
							<td style="text-align:left" title="${his.comment}"><core:partOfText value="${his.comment}" textmaxlength="30"/></td>
							<td class="textRight"><s:date name="#his.paymentTime" format="yyyy-MM-dd HH:mm:ss"/></td>
							<td style="text-align:center;">
								<s:if test="%{#request.currentPageRole == 'admin'}">
								    <a class="bLink" href="${cxt_path}/admin/settles/detail?userId=${his.userId}">账户明细</a>&nbsp; 
								</s:if>
								<s:if test="%{#request.currentPageRole == 'finance'}">
								    <span class="bLink hisAction hand" hisId="${his.id}">撤销</span>&nbsp;&nbsp;
		                            <a class="bLink" href="${cxt_path}/finance/settles/detail?userId=${his.userId}">账户明细</a>&nbsp;
		                        </s:if>
							</td>
						</tr>
						</s:iterator>
					</s:if>
					<s:else>
	                    <tr>
	                        <td colspan="10" class="no-data">暂无数据</td>
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

<link rel="stylesheet" href="${css_path}/calendar-date.css" type="text/css" />
<link rel="stylesheet" href="${css_path}/pagination.css" type="text/css" />

<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.toolbox.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.forms.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/jquery.pagination.js"></script>
<script type="text/javascript" charset="utf-8">
$(function(){
	//his action
    $(".hisAction").live("click",function(){
    	var _confirm = confirm("确认撤销?");
    	if(!_confirm) return;
		var hisId = $(this).attr("hisId");
		$.ajax({
			type: "POST",
			url: "${cxt_path}/finance/cancelPay",
			dataType: "json",
			data: "paymentId=" + hisId,
			success: function(callback){
				if(callback.success){
					displayStatusMessage("撤销成功");
					window.location.reload();
				}else{
					displayStatusMessage(callback.message);
				}
			}
		});
    });
	
    //pagination
    function initPage(pageNum, pageSize, totalRecords) {
        var pn = parseInt(pageNum, 10) - 1;
        var ps = parseInt(pageSize, 10);
        var tr = parseInt(totalRecords, 10);
        if (pn < 0) {
            return;
        }
        var opt = {
            form: $("#searchForm"),
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
    //trim input value
    $("form").live("submit",function(){
        $(this).find("input").each(function(){
            $(this).val($.trim($(this).val()));         
        });
        
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
    
    //menu active
    $("#leftmenu-finance-historical").addClass("active");
    $("#topmenu-finance-pay").addClass("active");
});
</script>