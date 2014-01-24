<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>Ad Orders</title>

<div class="container-body">
	<div class="spacer20"></div>
	
	<div>
		<%@ include file="/jsp/admin/orders/LeftMenu.jsp"%>
		
		<div class="container-right" style="overflow:auto;">
		<div class="overview"><span>广告组总数：&nbsp; <font class="yellowH3">${allAdOrdersCount}</font> ， 有效广告组数量：&nbsp; <font class="yellowH3">${activeAdOrdersCount}</font></span></div>
		 <div class="clear spacer15"></div>
		 <div class="overviewPanel shadow">
		 	<div class="right">
			   	    <s:form id="filterSearchForm" action="admin/orders" method="post" name="filterSearchForm">
						<div class="left spacer_right5">
						    <span>Campaign Status:&nbsp;</span>
						    <s:select id="filterStatusCam" cssClass="bTextbox" cssStyle="width:auto;" list="#{'0': 'ALL', '1':'ENABLED'}" name="upperStatus" value="%{#request.upperStatus}"></s:select>
						    <span>Status:&nbsp;</span>
						    <s:select id="filterStatus" cssClass="bTextbox" cssStyle="width:auto;" list="%{#request.orderStatuses}" name="adOrder.status" value="%{#request.adOrder.status}"></s:select>
                            <s:select id="filterAdvertiser" cssClass="bTextbox" cssStyle="width:auto;" list="%{#request.advertisers}" name="adOrder.advertiserId" value="%{#request.adOrder.advertiserId}"></s:select>
                            <s:if test="#request.campaigns.size > 1">
                                <s:select id="filterCampaign"  cssClass="bTextbox" cssStyle="width:auto;" list="%{#request.campaigns}" name="adOrder.campaignId" value="%{#request.adOrder.campaignId}"></s:select>
                            </s:if>
                        </div>
						<div class="left">
						   	<s:textfield cssClass="bTextbox" id="sName" cssStyle="width:auto;" name="adOrder.name" value="%{#request.adOrder.name}" placeholder="Search by order name..."></s:textfield>
							<s:submit cssClass="bButton bButton24" onclick="doSearch()" value="查询"></s:submit>
					   	</div>
				   	</s:form>
			   	</div>
		 </div>
		 <div class="overview" style="border-top:0px;">
	            <div class="clear spacer10"></div>
				<table class="bTable"> 
					<tr class="heading">
						<th>ID</th>
                        <th>编辑时间</th>
                        <th>广告主</th>
						<th>订单名称</th>
						<th style="text-align:center;">状态</th>
						<th style="text-align:center;">操作</th>
					</tr>
					<tr class="heading-float fixed hidden" style="top:0;"></tr>
					
					<s:if test="#request['adOrderList'].size() > 0">
						<s:iterator value="#request['adOrderList']" var="order" status="status" >
						<tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
							<td>${order.id}</td>
                            <td><s:date name="#order.updateAt" format="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><s:property value="#order.companyName" /></td>
							<td><span title="<s:property value="%{#order.name}" escape="true"/>"><core:partOfText value="${order.name}" textmaxlength="20" escape="true"/></span></td>
							<td style="text-align:center;">${order.statusName}</td>
							<td style="text-align:center;">
	                            <a class="bLink" href="${cxt_path}/admin/orders/view?id=${order.id}">查看</a>

	                            <s:if test="#order.status.code != 1">
	                                <a class="bLink" href="javascript:void(0);" onclick="operate(${order.id},1)">启用</a>
	                            </s:if>
	                            <s:if test="#order.status.code != 2">
	                                <a class="bLink" href="javascript:void(0);" onclick="operate(${order.id},2)">暂停</a>
	                            </s:if>
	                            <s:if test="#order.status.code != 3">
	                                <a class="bLink" href="javascript:void(0);" onclick="operate(${order.id},3)">禁用</a>
	                            </s:if>
	                        </td>
	                        </td>
						</tr>
						</s:iterator>
					</s:if>
	                <s:else>
	                    <tr>
	                        <td colspan="8" class="no-data">暂无数据</td>
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

<link rel="stylesheet" href="${css_path}/pagination.css" type="text/css" />

<script type="text/javascript" charset="utf-8" src="${js_path}/libs/jquery.pagination.js"></script>
<script type="text/javascript" charset="utf-8">
	var elemTop = $(".bTable .heading").offset().top;
    var $pagination = $("#pagination");
    var $panel = $("#pagination.right");
    var opt;
	function isScrolledOut($elem) {
	    var docViewTop = $(window).scrollTop();
	    return elemTop <= docViewTop;
	}
	function doSearch() {
		$("#sName").val($.trim($("#sName").val()));
		$("#filterSearchForm").submit();
	}
    //广告组操作
    function operate(orderId,statusCode) {
        var confirmMsg = "";
        switch (statusCode) {
            case 1:
                confirmMsg = "就绪状态将许可此广告组投放";
                break;
            case 2:
                confirmMsg = "将暂停此广告组投放，但广告主可以启用";
                break;
            case 3:
                confirmMsg = "禁用此广告组后，广告将不能投放，广告主也不可修改";
                break;
            case 4:
                confirmMsg = "是否确定挂起该广告组？余额不足时可以使用这个状态，广告主充值后，广告组将会自动启用";
                break;
            default:
                confirmMsg = "余额不足时可以用这个状态，广告主充值后，广告组将会自动启用";
        }
        if(confirm(confirmMsg)) {
            var data = {"id":orderId,"statusCode":statusCode};
            displayStatusMessage("正在修改状态...请稍候...","success");
            $.ajax({
                type: "post",
                url: "${cxt_path}/admin/orders/updateStatus",
                data:data,
                dataType:"json",
                success: function (result) {
                    if(result.success){
                        displayStatusMessage("状态修改成功", "success");
                        $panel[0].selectPage(parseInt("${page.pageNum}")-1);
                    } else {
                        displayStatusMessage("状态修改失败:" + result.message, "error");
                    }
                },
                error: function (msg) {
                    displayStatusMessage("操作失败" + msg, "error");
                }
            });
        }
    }
	$(function() {
		$("#leftmenu-admin-orders").addClass("active");
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
        
        $("#filterStatus").change(function() {
        	$("#sName").val($.trim($("#sName").val()));
        	$("#filterSearchForm").submit();
        });
        $("#filterStatusCam").change(function() {
        	$("#sName").val($.trim($("#sName").val()));
        	$("#filterSearchForm").submit();
        });
        
        $("#filterAdvertiser").change(function() {
            $("#sName").val($.trim($("#sName").val()));
            $("#filterCampaign").val("");
            $("#filterSearchForm").submit();
        });
        $("#filterCampaign").change(function() {
            $("#sName").val($.trim($("#sName").val()));
            $("#filterSearchForm").submit();
        });

		// pagination
        function initPage(pageNum, pageSize, totalRecords) {
            var pn = parseInt(pageNum, 10) - 1;
            var ps = parseInt(pageSize, 10);
            var tr = parseInt(totalRecords, 10);
            if (pn < 0) {
                return;
            }
            opt = {
                form: $("#filterSearchForm"),
				pageDesCon:$("#pageDesCon"),
                next_text: "下一页",
                current_page: pn,
                items_per_page: ps,
                num_display_entries: 5,
                num_edge_entries: 2,
                prev_text: "上一页"
            };
            $pagination.pagination(tr, opt);
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


    });

</script>
