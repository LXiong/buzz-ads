<%@ include file="/jsp/common/Init.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<s:if test="%{#request.hasContactInfo != null && #request.hasContactInfo == false}">
</s:if>
<s:else>
    <a id="topmenu-advertiser-home" class="top-menu-button heading3" href="${cxt_path}/advertiser/dashboard">首页</a>
    <a id="topmenu-advertiser-adCampaign" class="top-menu-button heading3" href="${cxt_path}/advertiser/campaigns">广告管理</a>
    <a id="topmenu-advertiser-billing" class="top-menu-button heading3" href="${cxt_path}/advertiser/billing">结算</a>
</s:else>

 <a id="topmenu-advertiser-settings" class="top-menu-button heading3" href="${cxt_path}/advertiser/setting/account">设置</a>
 
 
