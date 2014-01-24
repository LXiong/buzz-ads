<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<div class="overviewPanel details" style="border-top:1px solid #ddd;">
    <div class="left panelTitle">
         <span title="<s:property value="%{#request.campaign.name}" escape="false"/>">广告活动 ： <core:partOfText value="${campaign.name}" textmaxlength="30" escape="false"/></span>
    </div>
    <div class="right">
        <div class="inlineBlock">
             <s:if test="campaign.status.code == 1 || campaign.status.code == 2 ">
                <s:select list="#{1:'启用',2:'暂停'}" value="campaign.status.code" listKey="key" listValue="value" name="campaignStatus" cssClass="bTextbox" headerKey="0">
                </s:select>
            </s:if>
            <s:else>
                <s:select list="#{1:'启用',2:'暂停',3:'禁止',4:'挂起'}" value="campaign.status.code" disabled="true" listKey="key" listValue="value" name="availableStats1" cssClass="bTextbox" headerKey="0" headerValue="---请选择---">
                </s:select>
            </s:else>
        </div>
        <div class="inlineBlock dataSearch spacer_left10">
            <a class="bButton bButton24" href="${cxt_path}/advertiser/campaign/${campaign.id}/settings">编辑</span></a>
        </div>
        
    </div>
</div>
<div class="overview" style="border-top:0px;">
    <table  class="viewTable ">
    <tr>
        <td style="padding-left:0;"><strong>默认投放网络</strong></td>
        <td class="yellowH3">
            <s:iterator value="#request.campaign.network" var="ntwk" status="status">
                <s:if test="#ntwk == @com.buzzinate.buzzads.enums.AdNetworkEnum@LEZHI">
                    乐知
                </s:if>
                <s:elseif test="#ntwk == @com.buzzinate.buzzads.enums.AdNetworkEnum@BSHARE">
                    bShare
                </s:elseif>
                <s:elseif test="#ntwk == @com.buzzinate.buzzads.enums.AdNetworkEnum@BUZZADS">
                    BuzzAds
                </s:elseif>
                <s:else>
                    ??
                </s:else>
                <s:if test="!#status.last">,&nbsp;</s:if>
            </s:iterator>
        </td>
        <td style="padding-left: 100px;"><strong>默认出价类型</strong></td>
        <td class="yellowH3">${campaign.bidType}</td>

    </tr>
    <tr>
        <td style="padding-left:0;"><strong>预算</strong></td>
        <td class="yellowH3">
            <span > <font style="color: #333">日预算：</font>
            <s:if test="campaign.maxDayBudgetDouble == 0.00 ">
            <font >无限制</font>
            </s:if>
            <s:else>
            <font class="yellowH3">¥${campaign.maxDayBudgetDouble }</font>
            </s:else>
            </span> , <span><font style="color: #333">总预算：</font>
            <s:if test="campaign.maxTotalBudgetDouble == 0.00 ">
            <font >无限制</font>
            </s:if>
            <s:else>
            <font class="yellowH3">¥${campaign.maxTotalBudgetDouble}</font>
            </s:else>
            </span> 
        </td>
        <td style="padding-left: 100px;"><strong>日期</strong></td>
        <td class="yellowH3">
            <s:date name="campaign.startDate" format="yyyy-MM-dd"/>
              - 
            <s:if test="campaign.endDate != undefined">
                 <s:date name="campaign.endDate" format="yyyy-MM-dd"/>
            </s:if>
            <s:else>
                <font>无截止日期</font>
            </s:else>
        </td>
    </tr>
    <tr>
        <td style="padding-left:0;"><strong>地理位置</strong></td>
        <td class="yellowH3">
            <s:if test="campaign.viewLocation !='' ">
                <span class="yellowH3">${campaign.viewLocation}</span>
            </s:if>
             <s:else>
                <font >无限制</font>
            </s:else>
        </td>
        <td></td><td></td>
    </tr>
</table>
</div>
<div class="clear spacer10"></div>

<!-- currentPath, which is required by CommSearch.jsp, should be defined when this jsp is imported -->
<%@ include file="/jsp/advertiser/campaigns/CommSearch.jsp"%>
<%@ include file="/jsp/advertiser/campaigns/CommStats.jsp"%>