<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>全部广告 - BuzzAds</title>

<div class="container-body">
    <div class="spacer20"></div>
    <div>
        <%@ include file="/jsp/advertiser/campaigns/LeftMenu.jsp"%>
        <div class="container-right" style="overflow: auto;">
        	<div class="overview bc">
				<p class="breadcrumb">
					<a class="bLinkD" style="padding-left:15px;" href="${cxt_path}/advertiser/campaigns">全部活动</a> &gt;&gt;
					<a class="orange" style="padding-left:10px;" href="javascript:void(0)">广告</a> 
				</p>
			</div>
            <div class="clear spacer10"></div>
            <div class="overviewPanel details" style="border-top:1px solid #ddd;">
                <div class="left panelTitle">
                     <span title="全部广告">全部广告</span>
                </div>
            </div>
            <div class="overview" style="border-top: none;">
                <span>您目前正在投放：</span><span style="color:#f80">${page.totalRecords}</span><span> 个广告</span>
            </div>
            <div class="clear spacer10"></div>
            
            <div class="overviewPanel shadow">
			    <div class="dataSearch">  
			         <form action="entries" method="post">
			             <script type="text/javascript">
			                 currentPath = "advertiser/campaigns/entries";
			                 formId="";
			             </script>
			             <%@ include file="/jsp/advertiser/campaigns/CommSearch.jsp"%>
			             <div class="clear"></div>
			         </form>  
			    </div> 
			</div>
            <%@ include file="/jsp/advertiser/campaigns/CommStats.jsp"%>
            
            <div class="overviewPanel details">
	            <div class="tabTitle left">
	                    <a  href="${cxt_path}/advertiser/campaigns">广告活动</a>
	                    <a href="${cxt_path}/advertiser/campaigns/groups"> 广告组</a>
	                    <a class=" orange " href="javascript:void(0)">广&nbsp;&nbsp;告</a>                 
	            </div>
	            <div id="createNew" class="inlineBlock right">
	                <a class="bButton lightOrange" href="${cxt_path}/advertiser/entry/new">新建广告<span class="mSpacer_left10">&gt;&gt;</span></a>
	            </div>
                <div class="clear"></div>
            </div>
            <div class="overview tabBody">
                <div class="clear spacer10"></div>
                <table class="bTable textRight">
                    <tr class="heading">
                        <th>广告</th>
                        <th>广告组</th>
                        <th>广告活动</th>
                        <th style="text-align:center">状态</th>
                        <th class="textRight">原始展现量</th>
                        <th class="textRight">原始点击量</th>
                        <th class="textRight">支出</th>
                        <th style="text-align:center">操作</th>
                    </tr>
                    <s:if test="#request['adEntries'].size() > 0">
                    <s:iterator value="#request['adEntries']" var="adEntry" status="status" >
                    <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if> <s:if test="#adEntry.status.code==3">class="disabled"</s:if>>
                         <td style="text-align: left" title="<s:property value="%{#adEntry.name}" escape="true"/>"><a class="bLinkD" href="<s:if test="#adEntry.status.code==3">javascript:void(0)</s:if><s:else>${cxt_path}/advertiser/entry/${adEntry.id}/settings</s:else>"><core:partOfText value="${adEntry.name}" textmaxlength="30" escape="true"/></a></td>
                         <td style="text-align: left" title="<s:property value="%{#adEntry.orderName}" escape="true"/>"><core:partOfText value="${adEntry.orderName}" textmaxlength="30" escape="true"/></td>
                         <td style="text-align: left" title="<s:property value="%{#adEntry.campName}" escape="true"/>"><core:partOfText value="${adEntry.campName}" textmaxlength="30" escape="true"/></td>
                         <td style="text-align: center">${adEntry.statusName }<span class="help-popup-img" 
                             <s:if test="#adEntry.status.code == 0">title="该广告已就绪"</s:if>
                             <s:elseif test="#adEntry.status.code == 1">title="该广告正常投放"</s:elseif>
                             <s:elseif test="#adEntry.status.code == 2">title="该广告已被暂停，您可以再次启用"</s:elseif>
                             <s:elseif test="#adEntry.status.code == 3">title="该广告已被管理员禁用，您无法再次启用"</s:elseif>
                             <s:elseif test="#adEntry.status.code == 4">title="该广告由于超出预算或最大展示数等原因，已被系统自动挂起。完成充值或修改相关设置后广告组状态会自动变为有效"</s:elseif>
                             <s:elseif test="#adEntry.status.code == 6">title="该广告正在审核中"</s:elseif>
                             <s:elseif test="#adEntry.status.code == 7">title="该广告未通过审核"</s:elseif>
                             <s:else>title="${adEntry.statusName}"</s:else>></span></td>
                         <td><s:text name="global.format.number"><s:param value="%{#adEntry.views}"/></s:text></td>
                         <td><s:text name="global.format.number"><s:param value="%{#adEntry.clicks}"/></s:text></td>
                         <td>¥${adEntry.cost }</td>
                         <td style="text-align:center">
                            <a class="bLink" style="display:none" href="${cxt_path}/advertiser/entry/${adEntry.id}/view">查看</a>
                            <s:if test="#adEntry.status.code != 3">
                            <a class="bLink" href="${cxt_path}/advertiser/entry/${adEntry.id}/settings">编辑</a>
                            </s:if>
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
                <div class="clear spacer10"></div>
                <form action="${cxt_path}/advertiser/campaigns/entries" id="filterSearchForm" method="post">
                    <div id="pagination" class="right pagination"></div>
                    <input type="hidden" value="${dateStart}" name="dateStart" />
                    <input type="hidden" value="${dateEnd}" name="dateEnd" />
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
</div>

<link rel="stylesheet" href="${css_path}/pagination.css" type="text/css" />
<!--[if IE]><script type="text/javascript" src="${js_path}/libs/excanvas.js"></script><![endif]-->
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/jquery.pagination.js"></script>
<script type="text/javascript">
	$(function(){
	    $("#topmenu-advertiser-adCampaign").addClass("active");
	    $("#leftmenu-advertiser-campaign").addClass("active");
	    
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
        
        window.setTimeout(function(){
            $("#creationDate").html(RC.getDate());
        }, 0);
    });
</script>
