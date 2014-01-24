<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>账户信息</title>

<style>
    .viewTable tr td:first-child { color:#666; }
</style>

<div class="container-body">
    <div class="spacer20"></div>
    <div >
        <%@ include file="/jsp/advertiser/settings/LeftMenu.jsp"%>
        <div class="container-right" style="overflow:auto;">
       		 <div class="overviewPanel shadow">
				<span class="panelTitle" style="display: inline-block;line-height: 32px;">广告主ID：<s:property value="advertiserAccount.advertiserId"/></span>
				<div class="right">
					<a class="bButton lightOrange" href="${cxt_path}/advertiser/setting/account/edit">修改账号信息</a>
				</div>
			</div>
        	<div class="overview noBT">
	            <table class="viewTable">
	                <tr>
	                    <td>公司名称：</td>
	                    <td><s:property value="%{#request.advertiserAccount.companyName}" /></td>
	                </tr>
	                <tr>
	                    <td>网站名称：</td>
	                    <td><s:property value="%{#request.advertiserAccount.websiteName}" /></td>
	                </tr>
	                <tr>
	                    <td>网站URL：</td>
	                    <td><s:property value="%{#request.advertiserAccount.websiteUrl}" /></td>
	                </tr>
	                <tr>
                        <td>用户秘钥：</td>
                        <td><s:property value="%{#request.advertiserAccount.secret}" /></td>
                    </tr>
	                <tr style="display:none;">
	                    <td>Business Type：</td>
	                    <td><s:property value="%{#request.advertiserAccount.businessType}" /></td>
	                </tr>
	                <tr>
	                    <td>状态：</td>
	                    <s:if test="%{#request.advertiserAccount.statusValue == 0}">
	                        <td style="color:green;">已开启</td>
	                    </s:if>
	                    <s:elseif test="%{#request.advertiserAccount.statusValue == 1}">
	                        <td style="color:red;">已冻结</td>
	                    </s:elseif>
	                    <s:else>
	                        <td style="color:red;">???</td>
	                    </s:else>
	                </tr>
	            </table>
	        </div>
	        <div class="clear spacer15"></div>
	            <div class="overviewPanel shadow">
	            	 <div class="right">
	            	 	<a class="bButton lightOrange" href="${cxt_path}/advertiser/setting/account/contact/new">新建联系人<span class="mSpacer_left10"></span></a>
	            	 </div>
	        </div>
	        <div class="overview noBT">
                <div class="clear spacer10"></div>
	            <table class="bTable">
	                <tbody>
	                <tr class="heading">
	                    <th>联系人</th>
	                    <th>Email</th>
	                    <th>手机</th>
	                    <th>QQ</th>
	                    <th>地址</th>
	                    <th>操作</th>
	                </tr>
	                <s:if test="#request.advertiserContactInfos != null && #request['advertiserContactInfos'].size() > 0">
	                    <s:iterator value="#request['advertiserContactInfos']" var="contact" status="status" >
	                        <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
	                            <td class="aci-id" style="display:none;"><s:property value="%{#contact.id}" /></td>
	                            <td class="aci-aid" style="display:none;"><s:property value="%{#contact.advertiserId}" /></td>
	                            <td><s:property value="%{#contact.name}" /></td>
	                            <td><s:property value="%{#contact.email}" /></td>
	                            <td><s:property value="%{#contact.mobile}" /></td>
	                            <td><s:property value="%{#contact.qq}" /></td>
	                            <td><s:property value="%{#contact.address}" /></td>
	                            <td><a href="${cxt_path}/advertiser/setting/account/contact?contactId=${contact.id}" class="bLink">编辑</a></td>
	                        </tr>
	                    </s:iterator>
	                </s:if>
	                <s:else>
	                    <tr>
	                        <td colspan="6" class="no-data">暂无数据</td>
	                    </tr>
	                </s:else>
	                </tbody>
	            </table>
	        </div>
        </div>
        <div class="clear spacer20"></div>
    </div>
</div>

<script type="text/javascript">
    $(function(){
        $("#leftmenu-advertiser-accountInfo").addClass("active");
        $("#topmenu-advertiser-settings").addClass("active");
    });
</script>
