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
			<div class="overviewPanel details" style="border-top-width:1px;">
			    <div class="inlineBlock panelTitle">网站信息</div>
			</div>
			<div class="overview noBT">
				<div class="clear spacer10"></div>
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
			</div>
			<div class="clear spacer15"></div> 
			<div class="overviewPanel details" style="border-top-width:1px;">
			    <div class="inlineBlock panelTitle">联系人</div>
			</div>
			<div class="overview noBT">
		   		<div class="clear spacer10"></div>
				<table class="viewTable">
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
                    <tr>
                        <td>分成比例：<td>
                        <td><input class="validator bTextbox" required="required" type="Number" step="1" min="1" id="proportion" value="${publisher.proportion }" style="width:70px;"> %
                        <span class="primary mSpacer_left10" id="proportion_msg"></span>
                        </td>
                    </tr>	        	
		        </table>
			</div>
			<div class="overviewPanel details" style="border-top-width:0px;">
			    <div class="inlineBlock panelTitle">收款信息</div>
			</div>
            <div class="overview noBT">
                <div class="clear spacer10"></div>
                <table class="viewTable">
                    <tr>
                        <td style="width:100px">财务性质：<td>
                        <td>
                            <s:radio list="#request.financeTypeSelector" name="financeType" value="%{publisher.financeType}"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:100px">收款银行：<td>
                       
                        <td>
                            <s:select cssClass="bTextbox left" list="#request.bankSelector" name="receiveBankCode" value="%{publisher.receiveBankCode}"></s:select>
                            <div class="left spacer_left15">
                            	<span style="vertical-align: middle;height:32px;line-height:32px;display:inline-block;margin-right:10px;" >开户支行:</span><s:textfield  cssClass="bTextbox " cssStyle="width:165px;" id="receiveBank" value="%{publisher.receiveBank}"></s:textfield>
                            </div>
                            
                        </td>
                    </tr>
                    <tr>
                        <td>收款人姓名：<td>
                        <td><input class="validator bTextbox" required="required" maxlength="10" type="text" id="receiveName" value="${publisher.receiveName }" /></td>
                    </tr>
                    <tr>
                        <td>银行账号：<td>
                        <td><input class="validator bTextbox" required="required" maxlength="30" type="text" id="receiveAccount" value="${publisher.receiveAccount }" /></td>
                    </tr>
                    <tr>
                        <td>
                            <input type="hidden" id="userIdPub" value="${publisher.userId}" />
                        <td>
                            <s:if test="%{#request.hasContactInfo != null && #request.hasContactInfo == false}">
                        <td><input id="submit" class="bButton center lightOrange" type="button" value="保存" /> </td>
                        </s:if>
                        <s:else>
                            <td><input id="submit" class="bButton center lightOrange" type="button" value="修改" /> <input class="bButton center" style="margin-left: 20px;" type="button" value="返回" onclick="window.history.go(-1)" /></td>
                        </s:else>
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
	$("#proportion").change(function(){
		
	});
	$("#submit").click(function(){
		var name = $("#name").val(), mobile = $("#mobile").val(), qq = $("#qq").val(), email = $("#email").val(), receiveName = $("#receiveName").val(), proportion = $("#proportion").val(),
		receiveAccount = $("#receiveAccount").val(), userId = $("#userIdPub").val(), receiveBank=$("#receiveBank").val(), receiveBankCode=$("select[name='receiveBankCode']").val();
		if(!RC.numInteger(proportion) || RC.numFloat(proportion) >100){
			$('#proportion_msg').html("请输入0-100之间的整数！");
			return false;
		}else{
			$('#proportion_msg').html("");
		}
        var financeType=$("input[name='financeType']:checked").val();
		var inputs = $(".validator").validator(),
		isPass = inputs.data("validator").checkValidity();
		if(!isPass) return;
		$.ajax({
			type: "POST",
			url: "${cxt_path}/admin/publisher/update",
			dataType: "json",
			data: "publisher.name=" + name + "&publisher.mobile=" + mobile + "&publisher.qq=" + qq + "&publisher.email=" +
                    email + "&publisher.receiveName=" + receiveName + "&publisher.receiveAccount=" + receiveAccount +
                    "&publisher.userId=" + userId + "&publisher.financeType=" + financeType + "&publisher.receiveBank=" +
                    receiveBank + "&publisher.receiveBankCode=" + receiveBankCode + "&publisher.proportion=" + proportion,
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
