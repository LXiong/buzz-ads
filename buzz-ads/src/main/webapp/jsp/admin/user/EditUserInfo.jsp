<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<link rel="stylesheet" href="${css_path}/pagination.css" type="text/css" />

<title>站长信息</title>

<div class="container-body">
	<div class="spacer20"></div>
	
		<%@ include file="/jsp/admin/user/LeftMenu.jsp"%>
		<div class="container-right" style="overflow:auto;">
		   	<div class="overview">
		   		 <h3 class="yellowH3 heading1">网站信息</h3>
			    <div class="clear spacer15"></div> 
		   		<table class="bTable">
		        	<tr class="heading">
		        		<th>网站名称</th>
		        		<th>域名地址</th> 
		        		<th>UUID</th> 
		        	</tr>
		            <s:if test="#request['publisher.sites'].size() > 0">
	                    <s:iterator value="#request['publisher.sites']" var="site" status="status">
	                        <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
	                            <td title="${site.name}"><core:partOfText value="${site.name}" textmaxlength="30" escape="true"/></td>
	                            <td title="${site.url}"><a class="bLinkU" href="<core:getHref value="${site.url}" escape="true"/>" target="_blank"><core:partOfText value="${site.url}" textmaxlength="30" escape="true"/></a></td>
	                            <td>${site.uuid}</td>
	                        </tr>
	                    </s:iterator>
	                </s:if>
	                <s:else>
	                    <tr>
	                        <td colspan="5" class="no-data">暂无数据</td>
	                    </tr>
	                </s:else>
		        </table>
		        <div class="clear spacer15" style="border-bottom:1px solid #eee;"></div>
		   		<div class="clear spacer10"></div>
		        <table class="viewTable">
		        	<tr>
		        		<td colspan="2"><h3 class="yellowH3 heading1">联系人</h3></td>
		        	</tr>
		        	<tr>
		        		<td style="width:100px">联系人：<td>
		        		<td><input class="validator bTextbox" required="required" maxlength="10" type="text" id="name" value="${publisher.name }" /></td>
		        	</tr>
		        	<tr>
		        		<td>手机：<td>
		        		<td><input class="validator bTextbox" type="mobile" id="mobile" value="${publisher.mobile }"  /></td>
		        	</tr>
		        	<tr>
		        		<td>QQ：<td>
		        		<td><input class="validator bTextbox" required="required" maxlength="30" type="text" id="qq" value="${publisher.qq }" /></td>
		        	</tr>
	                   <tr>
	                       <td>Email：<td>
	                       <td><input class="validator bTextbox" required="required" maxlength="30" type="email" id="email" value="${publisher.email }" /></td>
	                   </tr>		        	
		        </table>
		   		<div class="clear spacer10" style="border-bottom:1px solid #eee;"></div>
		   		<div class="clear spacer10"></div>
		        <table class="viewTable">
		        	<tr>
		        		<td colspan="2"><h3 class="yellowH3 heading1">收款信息</h3></td>
		        	</tr>
		        	<tr>
		        		<td style="width:100px">收款方式：<td>
		        		<td>
		        		    <select class="bTextbox">
	                            <option>支付宝</option>
	                        </select>
	                    </td>
		        	</tr>
		        	<tr>
		        		<td>收款人姓名：<td>
		        		<td><input class="validator bTextbox" required="required" maxlength="10" type="text" id="receiveName" value="${publisher.receiveName }" /></td>
		        	</tr>
		        	<tr>
		        		<td>收款账号：<td>
		        		<td><input class="validator bTextbox" required="required" maxlength="30" type="text" id="receiveAccount" value="${publisher.receiveAccount }" /></td>
		        	</tr>
		        	<tr>
		        		<td>
		        		    <input type="hidden" id="userIdPub" value="${publisher.userId }" />
		        		<td>
		        		<td><input id="submit" class="bButton center lightOrange" type="button" value="修改" /> <input class="bButton center" style="margin-left: 20px;" type="button" value="返回" onclick="history.go(-1)" /></td>
		        	</tr>
		        </table>
		   	</div>
		</div>
		<div class="clear spacer20"></div>
	</div>
	
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.forms.min.1.2.6.js"></script>
<script type="text/javascript">
$(function(){
	$.tools.validator.localize('zh', {
		'[maxlength]'     : '长度超过限制',
		'[required]': '不能为空',
        ':email': 'Email无效'
	    });
	$.tools.validator.conf.lang = "zh";

	$.tools.validator.fn("[type=mobile]", function(input, value) {
	    return /^((1[3-9]{1})+\d{9})$/.test(value) ? true : {
		zh: "请填写正确的手机号"
	    };
	});
	$("#submit").click(function(){
		var name = $("#name").val(), mobile = $("#mobile").val(), qq = $("#qq").val(), email = $("#email").val(), receiveName = $("#receiveName").val(),
		receiveAccount = $("#receiveAccount").val(), userId = $("#userIdPub").val();
		var inputs = $(".validator").validator(),
		isPass = inputs.data("validator").checkValidity();
		if(!isPass) return;
		$.ajax({
			type: "POST",
			url: "${cxt_path}/admin/publisher/update",
			dataType: "json",
			data: "publisher.name=" + name + "&publisher.mobile=" + mobile + "&publisher.qq=" + qq + "&publisher.email=" + email + "&publisher.receiveName=" + receiveName + "&publisher.receiveAccount=" + receiveAccount + "&publisher.userId=" + userId,
			success: function(callback){
				if(callback.success){
					displayStatusMessage("修改成功");
					setTimeout(function(){window.location.href='${cxt_path}/admin/publisher';},1000);
				}else{
					displayStatusMessage(callback.message);
				}
			}
		});
	});
	
	//menu active
	$("#leftmenu-admin-manage").addClass("active");
    $("#topmenu-admin-manage").addClass("active");
});
</script>


