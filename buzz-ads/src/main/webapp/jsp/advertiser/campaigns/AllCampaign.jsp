<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>全部活动 - BuzzAds</title>

<div class="container-body">
    <div class="spacer20"></div>
    <div>
        <%@ include file="/jsp/advertiser/campaigns/LeftMenu.jsp"%>
        <div class="container-right" style="overflow: auto;">
	        <s:if test="%{campaigns.size ==0}">
	            <div class="warningMessage div-rounded-5" >
	                <p>
	                    您目前还没有活动，请先  <a class="orange " href="${cxt_path }/advertiser/campaign/new">创建活动</a>。
	                </p>
	            </div>
	            <div class="clear spacer10"></div>
	        </s:if> 
            <div class="overviewPanel details" style="border-top:1px solid #ddd;">
                <div class="left panelTitle">
                     <span title="全部活动">全部活动</span>
                </div>
            </div>
            <div class="overview" style="border-top: none;">
                <span>您目前正在投放：</span><span style="color:#f80">${activeCampaigns}</span><span> 个广告活动</span>, 共<span style="color:#f80">${activeGroups}</span><span> 个广告组</span>, <span style="color:#f80">${activeEntries}</span><span> 个广告</span>
            </div>
			
            <div class="clear spacer10"></div>
            <div class="overviewPanel shadow">
                <div class="dataSearch">  
                     <form action="campaigns" method="post">
                         <script type="text/javascript">
                             currentPath = "advertiser/campaigns";
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
	                <a class=" orange" href="javascript:void(0)">广告活动</a>
	                <a  href="${cxt_path}/advertiser/campaigns/groups"> 广告组</a>
	                <a  href="${cxt_path}/advertiser/campaigns/entries">广&nbsp;&nbsp;告</a>                  
	            </div>
	            <div id="createNew" class="inlineBlock right">
	                <a class="bButton lightOrange" href="${cxt_path}/advertiser/campaign/new">新建广告活动<span class="mSpacer_left10">&gt;&gt;</span></a>
	            </div>
                <div class="clear"></div>
            </div>
            <div class="overview tabBody">
                <div class="clear spacer10"></div>
                <table class="bTable textRight">
                    <tr class="heading">
                        <th>广告活动</th>
                        <th>广告活动类型</th>
                        <th class="textRight">日预算</th>
                        <th class="textRight">总预算</th>
                        <th style="text-align:center">状态</th>
                        <th class="textRight">开始时间</th>
                        <th class="textRight">结束时间</th>
                        <th class="textRight">原始展现量</th>
                        <th class="textRight">原始点击量</th>
                        <th class="textRight">支出</th>
                        <th style="text-align:center">操作</th>
                    </tr>
                    <s:if test="#request['campaigns'].size() > 0">
                    <s:iterator value="#request['campaigns']" var="camp" status="status" >
                    <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if> <s:if test="#camp.status.code==3">class='disabled'</s:if>>
                        <td style="display:none">${camp.id }</td>
                        <td style="text-align:left" title="<s:property value="%{#camp.name}" escape="true"/>"><a class="bLinkD" href="${cxt_path }/advertiser/campaign/${camp.id}/groups"> <core:partOfText value="${camp.name}" textmaxlength="30" /></a></td>
                        <td style="text-align:left">
                            <s:if test="#camp.bidType == (@com.buzzinate.buzzads.enums.BidTypeEnum@CPC)">
                                CPC
                            </s:if><s:elseif test="#camp.bidType == (@com.buzzinate.buzzads.enums.BidTypeEnum@CPM)">
                                CPM
                            </s:elseif>
                        </td>
                        <td>
                         <s:if test="#camp.maxDayBudget == 0">
                            <font >无限制</font>
                        </s:if>
                        <s:else>
                            ¥${camp.maxDayBudgetDouble}
                        </s:else>
                        </td>
                        <td>
                        <s:if test="#camp.maxBudgetTotal == 0">
                            <font >无限制</font>
                        </s:if>
                        <s:else>
                            ¥${camp.maxTotalBudgetDouble}
                        </s:else>
                        </td>
                        <td style="text-align:center">${camp.statusName}<span class="help-popup-img" 
                            <s:if test="#camp.status.code == 0">title="该广告活动已就绪"</s:if>
                            <s:elseif test="#camp.status.code == 1">title="该广告活动正常投放"</s:elseif>
                            <s:elseif test="#camp.status.code == 2">title="该广告活动已被暂停，您可以再次启用"</s:elseif>
                            <s:elseif test="#camp.status.code == 3">title="该广告活动已被管理员禁用，您无法再次启用"</s:elseif>
                            <s:elseif test="#camp.status.code == 4">title="该广告活动由于超出预算或最大展示数等原因，已被系统自动挂起。完成充值或修改相关设置后广告活动状态会自动变为有效"</s:elseif>
                            <s:elseif test="#camp.status.code == 6">title="该广告活动正在审核中"</s:elseif>
                            <s:elseif test="#camp.status.code == 7">title="该广告活动未通过审核"</s:elseif>
                            <s:else>title="${camp.statusName}"</s:else>></span></td>
                        <td class="textRight">
                                <s:date name="#camp.startDate" format="yyyy-MM-dd"/>
                        </td>
                        <td class="textRight">
                            <s:if test="%{#camp.endDate == null || #camp.endDate == ''}">
                                   <font >无截止日期</font>
                               </s:if>
                               <s:else>
                                   <s:date name="#camp.endDate" format="yyyy-MM-dd"/>
                               </s:else>
                        </td>
                        <td><s:text name="global.format.number"><s:param value="%{#camp.views}"/></s:text></td>
                        <td><s:text name="global.format.number"><s:param value="%{#camp.clicks}"/></s:text></td>
                        <td>¥${camp.cost}</td>
                        <td style="text-align:center">
                             <a class="bLink" href="${cxt_path }/advertiser/campaign/${camp.id}/groups">查看</a>
                             <s:if test="#camp.status.code != 3">
                                <a class="bLink spacer_left10" href="${cxt_path }/advertiser/campaign/${camp.id}/settings">编辑</a>
                             </s:if>
                        </td>
                    </tr>
                    </s:iterator>
                </s:if>
                <s:else>
                    <tr>
                        <td colspan="11" class="no-data">暂无数据</td>
                    </tr>
                </s:else>
                </table>
                <div class="clear spacer10"></div>
                <form action="${cxt_path}/advertiser/campaigns" id="filterSearchForm" method="post">
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
        RC.topTip();
    });
</script>
