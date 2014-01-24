<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>广告主管理</title>

<div class="container-body">
	<div class="spacer20"></div>
		<%@ include file="/jsp/admin/user/LeftMenu.jsp"%>
		<div class="container-right" style="overflow:auto;">
			<div class="overview">
				<span>广告主总数：</span><span class="yellowH3">${page.totalRecords }</span>
			</div>
			<div class="clear spacer15"></div>
			<div class="overviewPanel shadow">
				<div class="right">
		   		<form id="userInfo" name="userInfo" method="POST" action="${cxt_path}/admin/advertiser">
					广告主公司名:&nbsp;<input class="bTextbox" style="width:150px;" type="text" name="companyName" value="${companyName}" placeholder="按广告主公司名搜索..."/>&nbsp;&nbsp;
		   			联系人姓名:&nbsp;<input class="bTextbox" style="width:150px;" type="text" name="name" value="${name}" placeholder="按联系人姓名搜索..."/>&nbsp;&nbsp;
		   			Email:&nbsp;<input class="bTextbox" style="width:150px;" type="text" name="email" value="${email}" placeholder="按email搜索..."/>&nbsp;&nbsp;
					<input class="bButton bButton24" type="submit" value="搜索" />    
				</form>
		   		</div>
			</div>
		   	<div class="overview" style="border-top:0px;">
		   		
				<div class="clear spacer10"></div>
		        <table class="bTable">
		        	<tr class="heading">
		        		<th>广告主ID</th>
		        		<th>公司名</th>
		        		<th>网站名</th>
		        		<th>网站URL</th>
		        		<th>账户余额</th>
		        		<th>投放金额</th>
		        		<th>投放活动数量</th> 
		        		<th>业务类型</th> 
		        		<th style="text-align:center;">状态</th> 
		        		<th style="text-align:center;">操作</th>
		        	</tr>
		        	<s:if test="#request.accounts != null && #request['accounts'].size() > 0">
			        	<s:iterator value="#request['accounts']" var="account" status="status" >
				        	<tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
				                <td>${account.advertiserId}</td>
				                <td title="<s:property value="%{#account.companyName}"/>"><core:partOfText value="${account.companyName}" textmaxlength="30"/></td>
		                        <td title="<s:property value="%{#account.websiteName}"/>"><core:partOfText value="${account.websiteName}" textmaxlength="30"/></td>
		                        <td title="${account.websiteUrl}"><a class="bLinkU" href="${account.websiteUrl}" target="_blank"><core:partOfText value="${account.websiteUrl}" textmaxlength="30"/></a></td>
		                        
		                        <td title="<s:property value="%{#account.balanceDouble}"/>"><core:partOfText value="${account.balanceDouble}" textmaxlength="30"/></td>
		                        <td title="<s:property value="%{#account.debitsTotalDouble}"/>"><core:partOfText value="${account.debitsTotalDouble}" textmaxlength="30"/></td>
		                        <td title="<s:property value="%{#account.liveCampCount}"/>"><core:partOfText value="${account.liveCampCount}" textmaxlength="30"/></td>
		                        
		                        <td title="<s:property value="%{#account.businessType}"/>"><core:partOfText value="${account.businessType}" textmaxlength="30"/></td>
		                        <s:if test="#account.status.code == 0">
		                            <td style="text-align:center;color:green;">正常</td>
		                        </s:if>
		                        <s:else>
		                            <td style="text-align:center;color:red;">已冻结</td>
		                        </s:else>
		                        <td align="center"><a href="${cxt_path}/admin/advertiser/view?advertiserId=${account.advertiserId}" class="bLink">查看</a>
		                        <a class="bLink frozen" advertiserId="${account.advertiserId}" 
		                            <s:if test="#account.status.code == 1">
		                                style="display:none;"
		                            </s:if>
		                            >冻结</a>
		                        <a class="bLink unfrozen" advertiserId="${account.advertiserId}"
		                            <s:if test="#account.status.code == 0">
		                                style="display:none;"
		                            </s:if>
		                            >解冻</a>
		                        <sec:authorize ifAnyGranted="ROLE_AD_ADMIN,ROLE_AD_FINANCE">
		                            <a class="bLink view" href="${cxt_path}/admin/advertiser/billing?adId=${account.advertiserId}">账户明细</a>
		                        </sec:authorize>
		                        </td>
		                    </tr>
			        	</s:iterator>
		        	</s:if>
		        	<s:else>
	                    <tr>
	                        <td colspan="7" class="no-data">暂无数据</td>
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
		<div class="clear spacer20"></div>
</div>


<div id="adsWindowPopDiv"></div>
<div id="popWin" class="popWin" style="display:none"></div>

<div id="frozen" class="hidden">
	<div class="reset">确定要将此用户冻结？</div>

	<div class="spacer15"></div>
	<div class="lezhiGetCodeBtn" style="text-align:center;">
		<input type="submit" value="保存" class="frozenBtn bButton lightOrange">&nbsp;&nbsp;
		<input type="button" class="bButton backBtn" value="返回">
	</div>
</div>
<div id="unfrozen" class="hidden">
	<div class="reset">确定要为此用户解冻？</div>
	<div class="spacer15"></div>
	<div class="lezhiGetCodeBtn" style="text-align:center;">
		<input type="submit" value="保存" class="unfrozenBtn bButton lightOrange">&nbsp;&nbsp;
		<input type="button" class="bButton backBtn" value="返回">
	</div>
</div>

<link rel="stylesheet" href="${css_path}/pagination.css" type="text/css" />
<link rel="stylesheet" href="${css_path}/calendar-date.css" type="text/css" />
<link rel="stylesheet" href="${css_path}/popWin.css" />
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/popWin.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/jquery.tools.toolbox.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.forms.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/jquery.pagination.js"></script>
<script type="text/javascript" charset="utf-8">
$(function(){
	//create popWin
	var data={
			container:$("#popWin"),
			width:300,
			height:150,
			title:"确认操作",
			content:$("#adsWindowPopDiv"),
			closeCallback:function(){},
			afterCallback:function(){}
		};
	var popwin=new bshare.popWin(data);
	
	// pagination
    function initPage(pageNum, pageSize, totalRecords) {
        var pn = parseInt(pageNum, 10) - 1;
        var ps = parseInt(pageSize, 10);
        var tr = parseInt(totalRecords, 10);
        if (pn < 0) {
            return;
        }
        var opt = {
            form: $("#userInfo"),
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
	
	$(".frozen").live("click",function(){
		var advertiserId = $(this).attr("advertiserId"),
		    index = $(".frozen").index(this);
		$(".frozenBtn").attr("advertiserId",advertiserId);
		$(".frozenBtn").attr("index",index);
		$("#adsWindowPopDiv").html($("#frozen").html());
		popwin.show();
	});
	
	$(".unfrozen").live("click",function(){
		var advertiserId = $(this).attr("advertiserId"),
		    index = $(".unfrozen").index(this);
		$(".unfrozenBtn").attr("advertiserId",advertiserId);
		$(".unfrozenBtn").attr("index",index);
		$("#adsWindowPopDiv").html($("#unfrozen").html());
		popwin.show();
	});
	
	
	
	//back btn
	$(".backBtn").live("click",function(){
		popwin.close();
		$("#closePicBtn").click();
	});
	
	//frozen action
	$(".frozenBtn").live("click",function(){
		popwin.close();
		var advertiserId = $(this).attr("advertiserId"),
		    index = $(this).attr("index"),
		    frozen = $($(".frozen").get(index));
		$.ajax({
			type: "POST",
			url: "${cxt_path}/admin/advertiser/frozen",
			dataType: "json",
			data: "advertiserId=" + advertiserId,
			success: function(callback){
				if(callback.success){
					frozen.hide();
					frozen.next().show();
					frozen.parent().prev().html("已冻结").css("color", "red");
					displayStatusMessage("冻结成功");
				}else{
					displayStatusMessage(callback.message);
				}
			}
		});
	});
	
	//unfrozen action
	$(".unfrozenBtn").live("click",function(){
		popwin.close();
		var advertiserId = $(this).attr("advertiserId"),
        index = $(this).attr("index"),
        unfrozen = $($(".unfrozen").get(index));
		$.ajax({
			type: "POST",
			url: "${cxt_path}/admin/advertiser/unfrozen",
			dataType: "json",
			data: "advertiserId=" + advertiserId,
			success: function(callback){
				if(callback.success){
					unfrozen.hide();
					unfrozen.prev().show();
                    unfrozen.parent().prev().html("正常").css("color", "green");
					displayStatusMessage("成功解冻");
				}else{
					displayStatusMessage(callback.message);
				}
			}
		});
	});
	
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
	$("#leftmenu-admin-advertiserManage").addClass("active");
    $("#topmenu-admin-manage").addClass("active");
});
</script>

