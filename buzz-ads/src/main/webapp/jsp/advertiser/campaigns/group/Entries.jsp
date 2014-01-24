<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>全部广告 - ${selectedGroup.name} - ${selectedCampaign.name} - BuzzAds</title>

<div class="container-body">
	<div class="spacer20"></div>
	<div>
        <%@ include file="/jsp/advertiser/campaigns/LeftMenu.jsp"%>
        <div class="container-right" style="overflow: auto;">
            <s:if test="adEntries == null || adEntries.size <= 0">
                <div class="warningMessage div-rounded-5"><p>此广告组还没有广告，请先 <a class="bLink orange " href="${cxt_path}/advertiser/entry/new?campaignId=${campaign.id}">新建广告</a>。</p></div>
                <div class="spacer10"></div>
            </s:if>
            <div class="overview bc">
                <p class="breadcrumb">
                    <a class="bLinkD" style="padding-left:15px;" href="${cxt_path}/advertiser/campaigns">全部活动</a>
                    &gt;&gt;
                    <a class="bLinkD" title="<s:property value="%{#request.campaign.name}" escape="false"/>"  href="${cxt_path}/advertiser/campaign/${campaign.id}/groups">广告活动  - <core:partOfText value="${campaign.name}" textmaxlength="30" escape="false"/></a>
                    &gt;&gt;
                    <a class="orange" href="javascript:void(0)">广告组 - <core:partOfText value="${adOrder.name}" textmaxlength="30" escape="true"/></a> 
                </p>
            </div>
            <div class="clear spacer10"></div>
            
            <div class="overviewPanel details" style="border-top:1px solid #ddd;">
                <div class="left panelTitle">
                     <span title="<s:property value="%{#request.adOrder.name}" escape="false"/>">广告组： <core:partOfText value="${adOrder.name}" textmaxlength="30" escape="true"/></span>
                </div>
                <div class="right">
                    <div class="inlineBlock">
                        <s:if test="adOrder.status.code == 1 || adOrder.status.code == 2 ">
                            <s:select list="#{1:'启用',2:'暂停'}" value="adOrder.status.code" listKey="key" listValue="value" name="orderStatus" cssClass="bTextbox" headerKey="0">
                            </s:select>
                        </s:if>
                        <s:else>
                            <s:select list="#{1:'启用',2:'暂停',3:'禁止',4:'挂起'}" value="adOrder.status.code" disabled="true" listKey="key" listValue="value" name="availableStats1" cssClass="bTextbox" headerKey="0" headerValue="---请选择---">
                            </s:select>
                        </s:else>
                    </div>
                    <div class="inlineBlock dataSearch spacer_left10">
                        <a class="bButton editBtn" href="<s:if test="adOrder.status.code != 3 and campaign.status.code != 3">${cxt_path}/advertiser/group/${adOrder.id}/settings </s:if><s:else>javascript:void(0)</s:else>">编辑</a>
                    </div>
                </div>
            </div>
            <div class="overview" style="border-top:0px;">
                <table  class="viewTable left">
                    <tr>
                        <td style="padding-left: 0px;"><strong>默认出价</strong></td>
                        <td class="yellowH3">¥ ${adOrder.bidPriceDouble} / ${adOrder.campBidType}</td>
                        <td style="padding-left: 100px;"><strong>日      期</strong></td>
                        <td class="yellowH3">
                            <s:date name="adOrder.startDate" format="yyyy-MM-dd"/>
                             - <s:if test="adOrder.endDate != undefined "><s:date name="adOrder.endDate" format="yyyy-MM-dd"/>  </s:if>
                             <s:else>
                                <font >无截止日期</font>
                             </s:else>
                            
                        </td>
                    </tr>
                    <tr>
                        <td style="padding-left: 0px;"><strong>投放时间</strong></td>
                        <td class="yellowH3">
                            <font style="color: #333">特定的天：</font>${adOrder.viewDay}<font style="color: #333" >，时间：</font>
                            <font id="specificTime"></font>
                            
                            
                        </td>
                        <td style="padding-left: 100px;"><strong>关键词</strong></td><td class="yellowH3">
                            <s:if test="adOrder.keywords !='' ">
                                <span title="${adOrder.keywords}"><core:partOfText value="${adOrder.keywords}" textmaxlength="100" escape="true"/></span>
                            </s:if>
                            <s:else>
                                <font >无限制</font>
                            </s:else>
                        </td>
                    </tr>
                    <tr>
                        <td style="padding-left: 0px;"><strong>受众群体</strong></td>
                        <td class="yellowH3">
	                        <s:if test="adOrder.audienceCategoriesName != ''">
	                        	<span>${adOrder.audienceCategoriesName}</span>
	                        </s:if>
	                        <s:else>
	                        	<font >无限制</font>
	                        </s:else>
                        </td>
                        <td style="padding-left: 100px;"><strong>频次上限</strong></td>
	                        <td class="yellowH3">
	                        	<s:if test="adOrder.frequencyCap != 0 && adOrder.orderFrequency != 0">
	                        		${adOrder.frequencyCap} 每广告组 / 天
	                        	</s:if>
	                        	<s:elseif test="adOrder.frequencyCap != 0 && adOrder.entryFrequency != 0">
	                        		${adOrder.frequencyCap} 每广告 / 天
	                        	</s:elseif>
	                        	<s:else>
	                        		<font >无限制</font>
	                        	</s:else>
	                        </td>
                    </tr>
	                    <tr>
	                    	 <s:if test="campaign.maxDayBudgetDouble != '0.00'">
		                        <td style="padding-left: 0px;"><strong>投放方式</strong></td>
		                        <td class="yellowH3">
			                        <span>
			                        	<s:if test="adOrder.adsType.code == 0">
			                        		均匀投放
			                        	</s:if>
			                        	<s:elseif test="adOrder.adsType.code == 1">
			                        		加速投放
			                        	</s:elseif>
			                        </span>
		                        </td>
	                        </s:if>
	                       
	                        <td style='<s:if test="campaign.maxDayBudgetDouble != '0.00'">padding-left: 100px;</s:if><s:else>padding-left: 0px;</s:else>'><strong>媒体定向</strong></td>
	                        <td class="yellowH3">
	                        	<div class="maxHeight100">
		                        	<s:if test="adOrder.channelDomains != null && adOrder.channelDomains.size > 0">
			                        	 <s:iterator value="adOrder.channelDomains" var="channel" status="status" >
			                        	 		<div>
			                        	 			${channel}
			                        	 		</div>
			                        	 </s:iterator>
		                        	 </s:if>
		                        	 <s:else>
		                        	 	<font >无限制</font>
		                        	 </s:else>
	                        	 </div>
	                        </td>
	                    </tr>
                    
                </table>
            </div>
            <div class="clear spacer10"></div>
            
            <div class="overviewPanel shadow">
                <div class="dataSearch">  
                     <form action="entries" method="post">
                         <script type="text/javascript">
                             currentPath = "advertiser/group/${orderId}/entries";
                             formId="";
                         </script>
                         <%@ include file="/jsp/advertiser/campaigns/CommSearch.jsp"%>
                         <div class="clear"></div>
                     </form>   
                </div> 
            </div>
            <%@ include file="/jsp/advertiser/campaigns/CommStats.jsp"%>
            
            <div class="overviewPanel details">
	            <div id="createNew" class="inlineBlock right">
	                <a class="bButton lightOrange" href="${cxt_path}/advertiser/entry/new?campaignId=${adOrder.campaignId}&orderId=${adOrder.id}">新建广告<span class="mSpacer_left10">&gt;&gt;</span></a>
	            </div>
                <div class="clear"></div>
            </div>
			<div class="overview tabBody">
                <div class="clear spacer10"></div>
		        <table class="bTable textRight">
                    <tr class="heading">
                        <th>广告</th>
                        <th style="text-align:center;">状态</th>
                        <th style="text-align:center;">广告类型</th>
                        <th class="textRight">原始展示</th>
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
                                <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if> <s:if test="#adEntry.status.code==3 || adOrder.status.code==3 || campaign.status.code==3">class="disabled"</s:if>>
                                  <td style="text-align: left" title="${adEntry.name}"><a class="bLinkD" href="<s:if test="#adEntry.status.code==3 || adOrder.status.code==3 || campaign.status.code==3">javascript:void(0)</s:if><s:else>${cxt_path}/advertiser/entry/${adEntry.id}/settings </s:else> "><core:partOfText value="${adEntry.name}" textmaxlength="30"/></a></td>
                                    <s:if test="campaign.status.code != 1">
                                        <td style="text-align:center">广告活动已${campaign.statusName}<span class="help-popup-img" 
	                                        <s:if test="#campaign.status.code == 0">title="该广告活动已就绪"</s:if>
	                                        <s:elseif test="#campaign.status.code == 1">title="该广告活动正常投放"</s:elseif>
	                                        <s:elseif test="#campaign.status.code == 2">title="该广告已活动被暂停，您可以再次启用"</s:elseif>
	                                        <s:elseif test="#campaign.status.code == 3">title="该广告活动已被管理员禁用，您无法再次启用"</s:elseif>
	                                        <s:elseif test="#campaign.status.code == 4">title="该广告活动由于超出预算或最大展示数等原因，已被系统自动挂起。完成充值或修改相关设置后广告活动状态会自动变为有效"</s:elseif>
	                                        <s:elseif test="#campaign.status.code == 6">title="该广告活动正在审核中"</s:elseif>
	                                        <s:elseif test="#campaign.status.code == 7">title="该广告活动未通过审核"</s:elseif>
	                                        <s:else>title="${campaign.statusName}"</s:else>></span></td>
                                    </s:if>
                                    <s:elseif test="adOrder.status.code != 1">
                                        <td style="text-align:center">广告组已${adOrder.statusName}<span class="help-popup-img" 
	                                        <s:if test="#adOrder.status.code == 0">title="该广告组已就绪"</s:if>
	                                        <s:elseif test="#adOrder.status.code == 1">title="该广告组正常投放"</s:elseif>
	                                        <s:elseif test="#adOrder.status.code == 2">title="该广告组已被暂停，您可以再次启用"</s:elseif>
	                                        <s:elseif test="#adOrder.status.code == 3">title="该广告组已被管理员禁用，您无法再次启用"</s:elseif>
	                                        <s:elseif test="#adOrder.status.code == 4">title="该广告组由于超出预算或最大展示数等原因，已被系统自动挂起。完成充值或修改相关设置后广告组状态会自动变为有效"</s:elseif>
	                                        <s:elseif test="#adOrder.status.code == 6">title="该广告组正在审核中"</s:elseif>
	                                        <s:elseif test="#adOrder.status.code == 7">title="该广告组未通过审核"</s:elseif>
	                                        <s:else>title="${adOrder.statusName}"</s:else>></span></td>
                                    </s:elseif>
                                    <s:else>
                                        <td style="text-align:center">${adEntry.statusName}<span class="help-popup-img" 
	                                        <s:if test="#adEntry.status.code == 0">title="该广告已就绪"</s:if>
	                                        <s:elseif test="#adEntry.status.code == 1">title="该广告正常投放"</s:elseif>
	                                        <s:elseif test="#adEntry.status.code == 2">title="该广告已被暂停，您可以再次启用"</s:elseif>
	                                        <s:elseif test="#adEntry.status.code == 3">title="该广告已被管理员禁用，您无法再次启用"</s:elseif>
	                                        <s:elseif test="#adEntry.status.code == 4">title="该广告由于超出预算或最大展示数等原因，已被系统自动挂起。完成充值或修改相关设置后广告组状态会自动变为有效"</s:elseif>
	                                        <s:elseif test="#adEntry.status.code == 6">title="该广告正在审核中"</s:elseif>
	                                        <s:elseif test="#adEntry.status.code == 7">title="该广告未通过审核"</s:elseif>
	                                        <s:else>title="${adEntry.statusName}"</s:else>></span></td>
                                    </s:else>
                                    <td style="text-align:center">
                                        <s:if test="#adEntry.resourceType == @com.buzzinate.buzzads.enums.AdEntryTypeEnum@TEXT">
	                                        纯文字
	                                    </s:if>
                                        <s:elseif test="#adEntry.resourceType == @com.buzzinate.buzzads.enums.AdEntryTypeEnum@IMAGE">
                                            图文
                                        </s:elseif>
                                        <s:elseif test="#adEntry.resourceType == @com.buzzinate.buzzads.enums.AdEntryTypeEnum@Flash">
                                            富媒体
                                        </s:elseif>
                                        <s:else>
                                            ??
                                        </s:else>
                                    </td>
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
                                    	<a class="bLink" style="display:none" href="${cxt_path}/advertiser/entry/${adEntry.id}/view">查看</a>
                                        <s:if test="#adEntry.status.code != 3 and campaign.status.code != 3 and adOrder.status.code != 3">
                                        <a class="bLink" href="${cxt_path}/advertiser/entry/${adEntry.id}/settings">编辑</a>
                                        </s:if>
                                        <!-- 
                                        <a class="bLink mSpacer_left10" href="javascript:void(0)">删除</a>
                                         -->
                                    </td>
                                </tr>
                            </s:iterator>
                    </s:if>
                     <s:else>
	                    <tr>
	                        <td colspan="9" class="no-data">暂没有数据</td>
	                    </tr>
	                </s:else>
                </table>
                <div class="clear spacer10"></div>
                <form action="${cxt_path}/advertiser/group/${orderId}/entries" id="filterSearchForm" method="post">
                    <div id="pagination" class="right pagination"></div>
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
        function init(){
        	var adOrderTime="${adOrder.scheduleTimeStr }";
        	if(adOrderTime !=""){
        		var timeTemp=adOrderTime.split("-");
        		$("#specificTime").html(timeTemp[0]+" 至 "+timeTemp[1] + "");
        	}else{
        		$("#specificTime").html("无投放时间限制");
        	}
        	
        }
        init();
    	//更改广告组状态
        $("#orderStatus").change(function(){
            var urltemp="";
            if($(this).val()==1){
                urltemp='${cxt_path}/advertiser/group/${adOrder.id}/enable';
            }else if($(this).val()==2){
                urltemp='${cxt_path}/advertiser/group/${adOrder.id}/pause';
            }
            $.ajax({
                type: "post",
                url: urltemp,
                dataType:"json",
                success: function (data) {
                if(data.success){
                    displayStatusMessage("状态修改成功", "success");
                    window.location="${cxt_path}/advertiser/group/${orderId}/entries";
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
