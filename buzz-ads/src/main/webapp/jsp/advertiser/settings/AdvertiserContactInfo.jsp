<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>广告主账户信息</title>

<style>
    .viewTable tr td:first-child { color:#666; }
</style>

<div class="container-body">
	<div class="spacer20"></div>
	<div>
	    <%@ include file="/jsp/admin/user/LeftMenu.jsp"%>
		<div class="container-right " style="overflow:auto;">
			<div class="overview bc">
                <p class="breadcrumb">
                    <a class="bLinkD" style="padding-left:15px;" href="${cxt_path}/admin/advertiser">广告主管理</a>
                    &gt;&gt;
                    <a class="orange" title="<s:property value="%{#request.advertiserAccount.companyName}" escape="false"/>"  href="${cxt_path}/admin/advertiser/view?advertiserId=${request.advertiserAccount.advertiserId}">广告主  - <core:partOfText value="${request.advertiserAccount.companyName}" textmaxlength="30" escape="false"/></a>
                </p>
            </div>
            <div class="clear spacer10"></div>
			<div class="overviewPanel shadow">
				<span class="panelTitle" style="display: inline-block;line-height: 32px;">广告主ID：<s:property value="%{#request.advertiserAccount.advertiserId}"/></span>
				<div class="right">
					<a class="bButton lightOrange" href="${cxt_path}/admin/advertiser/action/edit?advertiserId=${advertiserAccount.advertiserId}">修改账号信息</a>
				</div>
			</div>
			<div class="overviewPanel details" >
            	 <div class="inlineBlock panelTitle">
				    	广告主信息
		         </div>
            </div>
		    <div class="overview noBT">
		    	<table class="viewTable" >
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
            	 	<a class="bButton lightOrange" href="${cxt_path}/admin/advertiser/action/contact/new?advertiserId=${advertiserAccount.advertiserId}">新建联系人<span class="mSpacer_left10"></span></a>
            	 </div>
            </div>
            <div class="overviewPanel details" >
            	 <div class="inlineBlock panelTitle">
				    	联系人列表
		         </div>
            </div>
         	<div class="overview noBT">
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
			                    <td><a href="${cxt_path}/admin/advertiser/action/contact?contactId=${contact.id}&advertiserId=${advertiserAccount.advertiserId}" class="bLink">编辑</a></td>
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
         	<div class="clear spacer15"></div>
         	<div class="overviewPanel details" style="border-top:1px solid #ddd;">
            	 <div class="inlineBlock panelTitle">
				    	特殊权限列表
		         </div>
            </div>
         	<div class="overview noBT">
         		<table  class="bTable">
         			<tr class="heading">
         				<th>特殊权限</th>
         				<th style="text-align:center">操作</th>
         			</tr>
         			<tr>
         				<td>媒体定向</td>
         				<td style="text-align:center" class="aperate">
         					<s:if test="userAuthority == null || userAuthority.channelTarget==0">
         						<a class="bLink frozen" href="javascript:void(0)" advertiserId="${advertiserAccount.advertiserId }">启用</a>
         					</s:if>	
         					<s:elseif test="userAuthority.channelTarget==1">
         						<a class="bLink unfrozen" href="javascript:void(0)" advertiserId="${advertiserAccount.advertiserId }">停用</a>
         					</s:elseif>
         				</td>
         			</tr>
         		</table>
         	</div>
		</div>
		<div class="clear spacer20"></div>
	</div>
</div>
<div id="adsWindowPopDiv"></div>
<div id="popWin" class="popWin" style="display:none"></div>

<div id="frozen" class="hidden">
	<div class="reset">确定要启用此权限？</div>

	<div class="spacer15"></div>
	<div class="lezhiGetCodeBtn" style="text-align:center;">
		<input type="submit" value="保存" class="frozenBtn bButton lightOrange">&nbsp;&nbsp;
		<input type="button" class="bButton backBtn" value="返回">
	</div>
</div>
<div id="unfrozen" class="hidden">
	<div class="reset">确定要停用此权限？</div>
	<div class="spacer15"></div>
	<div class="lezhiGetCodeBtn" style="text-align:center;">
		<input type="submit" value="保存" class="unfrozenBtn bButton lightOrange">&nbsp;&nbsp;
		<input type="button" class="bButton backBtn" value="返回">
	</div>
</div>
<link rel="stylesheet" href="${css_path}/popWin.css" />
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/popWin.js"></script>
<script>
$(function() {
	//create popWin
	var data={
			container:$("#popWin"),
			width:300,
			height:150,
			title:"确认操作",
			content:$("#adsWindowPopDiv"),
			closeCallback:function(){},
			afterCallback:function(){}
		};
	var popwin=new bshare.popWin(data);
	
	$(".frozen").live("click",function(){
		var advertiserId = $(this).attr("advertiserId"),
		    index = $(".frozen").index(this);
		$(".frozenBtn").attr("advertiserId",advertiserId);
		$(".frozenBtn").attr("index",index);
		$("#adsWindowPopDiv").html($("#frozen").html());
		popwin.show();
	});
	
	$(".unfrozen").live("click",function(){
		var advertiserId = $(this).attr("advertiserId"),
		    index = $(".unfrozen").index(this);
		$(".unfrozenBtn").attr("advertiserId",advertiserId);
		$(".unfrozenBtn").attr("index",index);
		$("#adsWindowPopDiv").html($("#unfrozen").html());
		popwin.show();
	});
	
	$("#leftmenu-admin-advertiserManage").addClass("active");
	$("#topmenu-admin-manage").addClass("active");
	
	//back btn
	$(".backBtn").live("click",function(){
		popwin.close();
		$("#closePicBtn").click();
	});
	
	//frozen action
	$(".frozenBtn").live("click",function(){
		popwin.close();
		var advertiserId = $(this).attr("advertiserId"),
		    index = $(this).attr("index"),
		    frozen = $($(".frozen").get(index));
		$.ajax({
			type: "POST",
			url: "${cxt_path}/admin/channel/"+advertiserId+"/open",
			dataType: "json",
			success: function(callback){
				if(callback.success){
					frozen.hide();
					frozen.next().show();
					$(".aperate").get(index).innerHTML='<a class="bLink unfrozen" href="javascript:void(0)" advertiserId="${advertiserAccount.advertiserId }">停用</a>';
					displayStatusMessage("启用成功");
				}else{
					displayStatusMessage(callback.message);
				}
			}
		});
	});
	
	//unfrozen action
	$(".unfrozenBtn").live("click",function(){
		popwin.close();
		var advertiserId = $(this).attr("advertiserId"),
        index = $(this).attr("index"),
        unfrozen = $($(".unfrozen").get(index));
		$.ajax({
			type: "POST",
			url: "${cxt_path}/admin/channel/"+advertiserId+"/close",
			dataType: "json",
			success: function(callback){
				if(callback.success){
					unfrozen.hide();
					unfrozen.prev().show();
					$(".aperate").get(index).innerHTML='<a class="bLink frozen" href="javascript:void(0)" advertiserId="${advertiserAccount.advertiserId }">启用</a>';
					displayStatusMessage("成功停用");
				}else{
					displayStatusMessage(callback.message);
				}
			}
		});
	});
});
</script>
