<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>Ad Entries</title>

<div class="container-body">
	<div class="spacer20"></div>
	
	<div>
		<%@ include file="/jsp/admin/orders/LeftMenu.jsp"%>
		<div class="container-right" style="overflow:auto;">
			<div class="overview">
				<span>广告总数：&nbsp; <font class="yellowH3">${allEntriesCount}</font> ， 有效广告数量：&nbsp; <font class="yellowH3">${activeEntriesCount}</font></span>
			</div>
			<div class="clear spacer15"></div>
			 <div class="overviewPanel shadow">
			 	<div class="right">
	                    <s:form id="filterSearchForm" action="admin/orders/entry" method="post" name="filterSearchForm">
	                        <div class="left spacer_right5">
	                            <span>Campaign&Order Status:&nbsp;</span>
                                <s:select id="filterStatusCam" cssClass="bTextbox" cssStyle="width:auto;" list="#{'0': 'ALL', '1':'ENABLED'}" name="upperStatus" value="%{#request.upperStatus}"></s:select>
	                            <span>Status:&nbsp;</span>
	                            <s:select id="filterStatus" cssClass="bTextbox" cssStyle="width:auto;" list="%{#request.entryStatus}" name="adEntry.status" value="%{#request.adEntry.status}"></s:select>
	                            <s:select id="filterAdvertiser" cssClass="bTextbox" cssStyle="width:auto;" list="%{#request.advertisers}" name="adEntry.advertiserId" value="%{#request.adEntry.advertiserId}"></s:select>
	                            <s:if test="#request.campaigns.size > 1">
	                                <s:select id="filterCampaign"  cssClass="bTextbox" cssStyle="width:auto;" list="%{#request.campaigns}" name="adEntry.campaignId" value="%{#request.adEntry.campaignId}"></s:select>
	                            </s:if>
	                            <s:if test="#request.adOrders.size > 1">
	                                <s:select id="filterAdOrder"  cssClass="bTextbox" cssStyle="width:auto;" list="%{#request.adOrders}" name="adEntry.orderId" value="%{#request.adEntry.orderId}"></s:select>
	                            </s:if>
	                        </div>
	                        <div class="left">
	                            <s:textfield cssClass="bTextbox" id="sName" cssStyle="width:auto;" name="adEntry.title" value="%{#request.adEntry.title}" placeholder="根据广告标题查询..."></s:textfield>
	                            <s:submit cssClass="bButton bButton24" onclick="doSearch()" value="查询"></s:submit>
	                        </div>
	                    </s:form>
	                </div>
			 </div>
            <div class="overview" style="border-top:0px;">
            	<div class="clear spacer10"></div>
				<table class="bTable ellipsis">
					<tr class="heading">
						<th>ID</th>
						<th>广告组ID</th>
                        <th>更新时间</th>
						<th>目标URL</th>
						<th>广告类型</th>
                        <th>广告项目名</th>
						<th>标题</th>
						<th>状态</th>
						<th style="text-align:center;">操作</th>
					</tr>
					<s:if test="#request['adEntryList'].size() > 0">
					<tr class="heading-float fixed hidden" style="top:0;"></tr>
					
					<s:iterator value="#request['adEntryList']" var="ad" status="status" >
					<tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
						<td>${ad.id}</td>
						<td>${ad.orderId}</td>
                        <td title="<s:date name="#ad.updateAt" format="yyyy-MM-dd HH:mm:ss"/>"><s:date name="#ad.updateAt" format="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><a target="_blank" class="bLinkU" title="<s:property value="%{#adEntry.link}" escape="true"/>" href="<core:replaceStrings value="${ad.link}" propertiesList="[UID],,[EID],,[BACKURL]," escape="true"/>"><core:partOfText value="${ad.link}" textmaxlength="30" escape="true"/></a></td>
						<td>${ad.resourceType}</td>
                        <td><span title="<s:property value="%{#ad.name}" escape="true"/>"><core:partOfText value="${ad.name}" textmaxlength="15" escape="true"/></span></td>
                        <td><span title="<s:property value="%{#ad.title}" escape="true"/>"><core:partOfText value="${ad.title}" textmaxlength="15" escape="true"/></span></td>
						<td>${ad.statusName}<span class="help-popup-img"
                            <s:if test="#ad.status.code == 0">title="该广告已就绪"</s:if>
                            <s:elseif test="#ad.status.code == 1">title="该广告正常投放"</s:elseif>
                            <s:elseif test="#ad.status.code == 2">title="该广告已被暂停，您可以再次启用"</s:elseif>
                            <s:elseif test="#ad.status.code == 3">title="该广告已被管理员禁用，您无法再次启用"</s:elseif>
                            <s:elseif test="#ad.status.code == 4">title="该广告由于超出预算或最大展示数等原因，已被系统自动挂起。完成充值或修改相关设置后广告状态会自动变为有效"</s:elseif>
                            <s:elseif test="#ad.status.toString() == 'VERIFYING'">title="该广告正在审核中"</s:elseif>
                            <s:elseif test="#ad.status.code == 7">title="该广告未通过审核"</s:elseif>
                            <s:else>title="${ad.statusName}"</s:else>></span></td>
						<td style="text-align:center;">
						   <a class="bLink" href="${cxt_path}/admin/orders/entry/view?id=${ad.id}">查看</a>
						   <s:if test="#ad.status.code != 6 && #ad.status.code != 7">
	                            <s:if test="#ad.status.code != 1">
	                               <a class="bLink" href="#" onclick="operate(${ad.id},1)">启用</a>
	                            </s:if>
	                            <s:if test="#ad.status.code != 2">
	                               <a class="bLink" href="#" onclick="operate(${ad.id},2)">暂停</a>
	                            </s:if>
	                            <s:if test="#ad.status.code != 3">
	                               <a class="bLink" href="#" onclick="operate(${ad.id},3)">禁用</a>
	                            </s:if>
						   </s:if>
						</td>
					</tr>
					</s:iterator>
					</s:if>
	                <s:else>
	                    <tr>
	                        <td colspan="9" class="no-data">暂无数据</td>
	                    </tr>
	                </s:else>
				</table>
				<div class="clear spacer5"></div>
	            <form id="paginationForm" action="${cxt_path}/admin/orders/entry" method="post" name="paginationForm">
	                <div id="pagination" class="right pagination"></div>
	            </form>
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
	//广告操作
    function operate(adId,status) {
        var confirmMsg = "";
        switch (status) {
          case 1:
              confirmMsg = "就绪状态将许可此广告投放";
              break;
          case 2:
              confirmMsg = "将暂停此广告投放，但广告主可以启用";
              break;
          case 3:
              confirmMsg = "禁用此广告后，广告将不能投放，广告主也不可修改";
              break;
          default:
              confirmMsg = "";
        }
        if(confirm(confirmMsg)) {
            var data = {"adEntry.id":adId,"status":status};
            $.ajax({
                type: "post",
                url: "${cxt_path}/admin/ads/adentry/operate",
                data:data,
                dataType:"json",
                success: function (result) {
                    if(result.success){
                        displayStatusMessage("状态修改成功", "success");
                        window.location.reload();
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
	function isScrolledOut($elem) {
	    var docViewTop = $(window).scrollTop();
	    return elemTop <= docViewTop;
	}

	$(function() {
		$("#leftmenu-admin-ads").addClass("active");
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
            $("#filterAdOrder").val("");
            $("#filterSearchForm").submit();
        });
        $("#filterCampaign").change(function() {
            $("#sName").val($.trim($("#sName").val()));
            $("#filterAdOrder").val("");
            $("#filterSearchForm").submit();
        });
        $("#filterAdOrder").change(function() {
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
            var opt = {
            	form:$("#filterSearchForm"),
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
	});
</script>
