<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>全部广告 - ${campaign.name} - BuzzAds</title>

<div class="container-body">
    <div class="spacer20"></div>
    <div>
        <%@ include file="/jsp/advertiser/campaigns/LeftMenu.jsp"%>
        <div class="container-right" style="overflow: auto;">
            <s:if test="adEntries == null || adEntries.size <= 0">
                <div class="warningMessage div-rounded-5"><p>这个广告活动没有广告，请先 <a class="bLink orange " href="${cxt_path}/advertiser/entry/new?campaignId=${campaign.id}">新建广告</a>。</p></div>
                <div class="spacer10"></div>
            </s:if>
            <div class="overview bc">
                <p class="breadcrumb">
                    <a class="bLinkD" style="padding-left:15px;" href="${cxt_path}/advertiser/campaigns">全部活动</a> &gt;&gt;
                    <a class="bLinkD" title="<s:property value="%{#request.campaign.name}" escape="false"/>" style="padding-left:10px;" href="${cxt_path}/advertiser/campaign/${campaign.id}/groups">广告活动  - <core:partOfText value="${campaign.name}" textmaxlength="30" escape="false"/></a> &gt;&gt; 
                    <a class="orange" title="所有广告" style="padding-left:10px;" href="javascript:void(0)">所有广告</a>
                </p>
            </div>
            <div class="clear spacer10"></div>
            
            <div class="overviewPanel shadow">
                <div class="dataSearch">  
                     <form action="entries" method="post">
                         <script type="text/javascript">
                             currentPath = "advertiser/campaign/${campaignId}/entries";
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
	                <a href="${cxt_path}/advertiser/campaign/${campaign.id}/groups"> 广告组</a>
	                <a class=" orange" href="javascript:void(0)">广&nbsp;&nbsp;告</a>                  
	            </div>
	            <div id="createNew" class="inlineBlock right">
	                <a class="bButton lightOrange" href="${cxt_path}/advertiser/entry/new?campaignId=${campaign.id}">新建广告<span class="mSpacer_left10">&gt;&gt;</span></a>
	            </div>
                <div class="clear"></div>
            </div>
            <div class="overview tabBody">
                <div class="clear spacer10"></div>
                <table class="bTable textRight">
                    <tr class="heading">
                        <th><input type="checkbox" /></th>
                        <th>广告</th>
                        <th>广告组</th>
                        <th style="text-align:center">状态</th>
                        <th class="textRight">原始展示量</th>
                        <th class="textRight">原始点击量</th>
                        <s:if test="campaign.bidType == (@com.buzzinate.buzzads.enums.BidTypeEnum@CPC)">
                                <th class="textRight">有效点击量</th>
                                <th class="textRight">有效点击/展示量</th>
                        </s:if>
                        <s:elseif test="campaign.bidType == (@com.buzzinate.buzzads.enums.BidTypeEnum@CPM)">
                                <th class="textRight">有效展示量</th>
                        </s:elseif>
                        <th class="textRight">支出</th>
                        <th style="text-align:center">操作</th>
                    </tr>
                    <s:if test="adEntries != null && adEntries.size > 0">
                        <s:iterator value="adEntries" var="adEntry" status="status">
                            <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if> <s:if test="#adEntry.status.code == 3 or #campaign.status.code == 3">class="disabled"</s:if>>
                                <td style="text-align: left"><input type="checkbox" /></td>
                                <td style="text-align: left" title="${adEntry.name}"><a class="bLinkD" href="<s:if test="#adEntry.status.code != 3 and campaign.status.code !=3">${cxt_path}/advertiser/entry/${adEntry.id}/settings</s:if><s:else>javascript:void(0)</s:else>"><core:partOfText value="${adEntry.name}" textmaxlength="30"/></a></td>
                                <td style="text-align:left" title="${adEntry.orderName}"><core:partOfText value="${adEntry.orderName}" textmaxlength="30" /></td>
                                <s:if test="campaign.status.code != 1">
                                    <td style="text-align:center">广告活动已${campaign.statusName }<span class="help-popup-img" 
			                            <s:if test="#campaign.status.code == 0">title="该广告活动已就绪"</s:if>
			                            <s:elseif test="#campaign.status.code == 1">title="该广告活动正常投放"</s:elseif>
			                            <s:elseif test="#campaign.status.code == 2">title="该广告活动已被暂停，您可以再次启用"</s:elseif>
			                            <s:elseif test="#campaign.status.code == 3">title="该广告活动已被管理员禁用，您无法再次启用"</s:elseif>
			                            <s:elseif test="#campaign.status.code == 4">title="该广告活动由于超出预算或最大展示数等原因，已被系统自动挂起。完成充值或修改相关设置后广告活动状态会自动变为有效"</s:elseif>
			                            <s:elseif test="#campaign.status.code == 6">title="该广告活动正在审核中"</s:elseif>
			                            <s:elseif test="#campaign.status.code == 7">title="该广告活动未通过审核"</s:elseif>
			                            <s:else>title="${campaign.statusName}"</s:else>></span></td>
                                </s:if>
                                <s:else>
                                    <td style="text-align:center"><core:partOfText value="${adEntry.statusName}" textmaxlength="30"/><span class="help-popup-img" 
			                            <s:if test="#adEntry.status.code == 0">title="该广告已就绪"</s:if>
			                            <s:elseif test="#adEntry.status.code == 1">title="该广告正常投放"</s:elseif>
			                            <s:elseif test="#adEntry.status.code == 2">title="该广告已被暂停，您可以再次启用"</s:elseif>
			                            <s:elseif test="#adEntry.status.code == 3">title="该广告已被管理员禁用，您无法再次启用"</s:elseif>
			                            <s:elseif test="#adEntry.status.code == 4">title="该广告由于超出预算或最大展示数等原因，已被系统自动挂起。完成充值或修改相关设置后广告组状态会自动变为有效"</s:elseif>
			                            <s:elseif test="#adEntry.status.code == 6">title="该广告正在审核中"</s:elseif>
			                            <s:elseif test="#adEntry.status.code == 7">title="该广告未通过审核"</s:elseif>
			                            <s:else>title="${adEntry.statusName}"</s:else>></span></td>
                                </s:else>
                                <td><s:text name="global.format.number"><s:param value="%{#adEntry.views}"/></s:text></td>
                                <td><s:text name="global.format.number"><s:param value="%{#adEntry.clicks}"/></s:text></td>
                                <s:if test="campaign.bidType == (@com.buzzinate.buzzads.enums.BidTypeEnum@CPC)">
                                    <td>${adEntry.cpcClicks}</td>
                                    <td class="textRight">${adEntry.cpcClickToView}</td>
                                </s:if>
                                <s:elseif test="campaign.bidType == (@com.buzzinate.buzzads.enums.BidTypeEnum@CPM)">
                                        <td>${adEntry.cpmViews}</td>
                                </s:elseif>
                                <td>¥${adEntry.cost}</td>
                                <td style="text-align:center">
                                    <s:if test="#adEntry.status.code != 3 and campaign.status.code !=3">
                                    <a class="bLink" href="${cxt_path}/advertiser/entry/${adEntry.id}/settings">编辑</a>
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
                <form action="${cxt_path}/advertiser/campaign/${campaignId}/entries" id="filterSearchForm" method="post">
                    <div id="pagination" class="right pagination"></div>
                    <input type="hidden" name="queryRange" value="${queryRange}" />
                    <input type="hidden" name="availableStats" value="${availableStats}"/>
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
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/jquery.pagination.js"></script>
<script type="text/javascript">
    $(function(){
        $("#topmenu-advertiser-adCampaign").addClass("active");
        $("#leftmenu-advertiser-campaign").addClass("active");
        
        //更改活动状态
        $("#campaignStatus").change(function(){
            var urltemp="";
            if($(this).val()==1){
                urltemp='${cxt_path}/advertiser/campaign/${campaign.id}/enable';
            }else if($(this).val()==2){
                urltemp='${cxt_path}/advertiser/campaign/${campaign.id}/pause';
            }
            $.ajax({
                type: "post",
                url: urltemp,
                dataType:"json",
                success: function (data) {
                if(data.success){
                    displayStatusMessage("状态修改成功", "success");
                    window.location="${cxt_path}/advertiser/campaign/${campaignId}/groups";
                } else {
                    displayStatusMessage("状态修改失败", "error");  
                }
                },
                error: function (msg) {
                    displayStatusMessage("修改失败", "error");   
                }
            });
        
        });
        window.setTimeout(function(){
            $("#creationDate").html(RC.getDate());
        }, 0);
        
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
        initPage("${page.pageNum}", "${page.pageSize}", "${page.totalRecords}");
    })
</script>
