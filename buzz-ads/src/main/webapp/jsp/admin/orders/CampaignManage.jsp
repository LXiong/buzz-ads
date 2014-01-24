<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>Campaigns</title>

<div class="container-body">
	<div class="spacer20"></div>
	
	<div>
		<%@ include file="/jsp/admin/orders/LeftMenu.jsp"%>
		
		<div class="container-right" style="overflow:auto;">
		<div class="overview"><span>广告活动总数：&nbsp;<font class="yellowH3"> ${allAdCampaignsCount}</font> , 有效广告活动数量：&nbsp;<font class="yellowH3"> ${activeAdCampaignsCount}</font></span></div>
		 <div class="clear spacer15"></div>
		<div class="overviewPanel shadow">
		   		<div class="right">
			      <div class="right">
	                <s:form id="filterSearchForm" action="admin/ads/campaign" method="post" name="filterSearchForm">
	                    <div class="left spacer_right5">
	                        <s:select id="filterStatus" cssClass="bTextbox" cssStyle="width:auto;" list="map" name="campaign.status" value="campaign.status">
	                        </s:select>
	                    </div>
                        <div class="left spacer_right5">
                            <s:select id="filterCompany" cssClass="bTextbox" cssStyle="width:auto;" list="advertiserCompanyMap" name="campaign.advertiserId" value="campaign.advertiserId">
                            </s:select>
                        </div>
                        <div class="left spacer_right5">
                            <s:select id="campaignBidType" cssClass="bTextbox" cssStyle="width:auto;min-width:100px;" list="bidTypeSelector" name="campaign.bidType"  value="campaign.bidType">
                            </s:select>
                        </div>
	                    <div class="left">
                            <s:textfield cssClass="bTextbox" id="sName" cssStyle="width:auto;" name="campaign.name" value="%{#request.campaign.name}" placeholder="根据活动名称搜索.."></s:textfield>
	                        <s:submit cssClass="bButton bButton24" onclick="doSearch()" value="查询"></s:submit>
	                    </div>
	                </s:form>
	              </div>
			    </div> 
		</div>
		    <div class="overview" style="border-top:0px;">
	            <div class="clear spacer10"></div>
				<table class="bTable"> 
					<tr class="heading">
                        <th>ID</th>
						<th>创建时间</th>
						<th>广告主</th>
						<th>活动名称</th>
						<th style="text-align:center;">计费类型</th>
						<th style="text-align:center;">订单数量</th>
						<th style="text-align:center;">广告数量</th>
						<th style="text-align:center;">状态</th>
						<th style="text-align:center;">操作</th>
					</tr>
					<tr class="heading-float fixed hidden" style="top:0;"></tr>
					<s:if test="#request['campaignList'].size() > 0">
						<s:iterator value="#request['campaignList']" var="camp" status="status" >
						<tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
                            <td>
                              <s:property value="#camp.id"/>
                            </td>
							<td>
							  <s:date name="#camp.updateAt" format="yyyy-MM-dd HH:mm:ss"/>
							</td>
		                    <td><span title="<s:property value="%{#camp.companyName}" escape="true"/>"><core:partOfText value="${camp.companyName}" textmaxlength="20" escape="true"/></span></td>
		                    <td><span title="<s:property value="%{#camp.name}" escape="true"/>"><core:partOfText value="${camp.name}" textmaxlength="30" escape="true"/></span></td>
		                    <td style="text-align:center;">${camp.bidType}</td>
		                    <td style="text-align:center;">${camp.adOrderCount}</td>
		                    <td style="text-align:center;">${camp.adEntryCount}</td>
		                    <td style="text-align:center;">${camp.statusName}<span class="help-popup-img" 
	                            <s:if test="#camp.status.code == 0">title="该广告活动已就绪"</s:if>
	                            <s:elseif test="#camp.status.code == 1">title="该广告活动正常投放"</s:elseif>
	                            <s:elseif test="#camp.status.code == 2">title="该广告活动已被暂停，您可以再次启用"</s:elseif>
	                            <s:elseif test="#camp.status.code == 3">title="该广告活动已被管理员禁用，您无法再次启用"</s:elseif>
	                            <s:elseif test="#camp.status.code == 4">title="该广告活动由于超出预算或最大展示数等原因，已被系统自动挂起。完成充值或修改相关设置后广告活动状态会自动变为有效"</s:elseif>
	                            <s:elseif test="#camp.status.code == 6">title="该广告活动正在审核中"</s:elseif>
	                            <s:elseif test="#camp.status.code == 7">title="该广告活动未通过审核"</s:elseif>
	                            <s:else>title="${camp.statusName}"</s:else>></span></td>
		                    <td style="text-align:center;">
		                      <a class="bLink" href="${cxt_path}/admin/campaigns/view?id=${camp.id}">查看</a>
		                        <s:if test="#camp.status.code != 1 and #camp.status.code != 4">
		                                <a class="bLink" href="#" onclick="operate(${camp.id},1)">启用</a>
		                        </s:if>
		                        <s:if test="#camp.status.code != 2">
	                               <a class="bLink" href="#" onclick="operate(${camp.id},2)">暂停</a>
	                            </s:if>
	                            <s:if test="#camp.status.code != 3">
	                               <a class="bLink" href="#" onclick="operate(${camp.id},3)">禁用</a>
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
	
	function isScrolledOut($elem) {
	    var docViewTop = $(window).scrollTop();
	    return elemTop <= docViewTop;
	}
	function doSearch() {
		$("#sName").val($.trim($("#sName").val()));
		$("#filterSearchForm").submit();
	}
	//活动操作
	function operate(campId,status) {
		var confirmMsg = "";
		switch (status) {
		  case 1:
			  confirmMsg = "就绪状态将许可此广告活动投放";
              break;
		  case 2:
              confirmMsg = "将暂停此广告活动投放，但广告主可以启用";
              break;
		  case 3:
              confirmMsg = "禁用此活动后，广告将不能投放，广告主也不可修改";
              break;
		  default:
              confirmMsg = "余额不足时可以用这个状态，广告主充值后，活动将会自动启用";
		}
		if(confirm(confirmMsg)) {
			var data = {"campaign.id":campId,"status":status};
			$.ajax({
                type: "post",
                url: "${cxt_path}/admin/ads/operate",
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
	$(function() {
		$("#leftmenu-admin-campaignManage").addClass("active");
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

        $("#filterCompany").change(function(){
            $("#sName").val($.trim($("#sName").val()));
            $("#filterSearchForm").submit();
        });
        $("#campaignBidType").change(function(){
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
    });

</script>
