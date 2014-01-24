<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<title>广告主广告</title>
<style>
.error{z-index:100;color:red;}
</style>
<div class="container-body">
	<div class="spacer20"></div>
	<div>
		<%@ include file="/jsp/advertiser/settings/LeftMenu.jsp"%>
		<div class="container-right" style="overflow: auto;">
			<form id="myform" name="myform" action="" method="POST">
			<table class="viewTable" width="100%">
                    <tr>
                        <td colspan="2"><h3 class="yellowH3">支付信息</h3></td>
                    </tr>
                    <tr>
                        <td width="100">账单类型：</td>
                        <td><select id="accountStatus"><option>预付款</option></select></td>
                    </tr>
                    <tr>
                        <td>支付平台：</td>
                        <td><select name="paymentForm"><option>支付宝</option></select>&nbsp;<a href="" style="margin-left:66px;text-decoration:underline;">充值 >></a></td>
                    </tr>
                    <tr>
                        <td>姓名：</td>
                        <td><input class="validator" required="required" type="text" name="name" value="" /></td>
                    </tr>
                    <tr>
                        <td>公司名称：</td>
                        <td><input class="validator" required="required" type="text" maxlength="30" name="companyName" /></td>
                    </tr>
                    <tr>
                        <td>电话：</td>
                        <td><input class="validator" required="required" type="text" maxlength="10" name="phone" /></td>
                    </tr>
                    <tr>
                        <td>Email：</td>
                        <td><input class="validator" required="required" type="email" maxlength="30" name="email" /></td>
                    </tr>
                    <tr>
                        <td>账户状态：</td>
                        <td><select name="accountStatus"><option>开启</option></select></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                        <div class="spacer15"></div>
                        <input id="submit" class="bButton center lightOrange" type="submit" value="保存" /> 
                        <input class="bButton center" style="margin-left: 20px;" type="button" value="返回" onclick="history.go(-1)" />
                        </td>
                    </tr>
                </table>
            </form>
		</div>
		<div class="clear spacer20"></div>
	</div>
</div>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.min.js"></script>
<script type="text/javascript">
	$(function(){
		$("#leftmenu-advertiser-paymentInfo").addClass("active");
		$("#topmenu-advertiser-settings").addClass("active");
		
		$.tools.validator.localize('zh', {
    		'[maxlength]'     : '长度超过限制',
    		'[required]': '不能为空',
    		':email'  	: '请填写正确的email地址'
    	});
		
    	$.tools.validator.conf.lang = "zh";
    	
    	
    	
    	$("#myform").validator();
	})
</script>

