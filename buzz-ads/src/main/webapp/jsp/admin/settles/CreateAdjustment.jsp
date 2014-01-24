<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>Create Adjustment</title>

<div class="container-body">
	<div class="spacer20"></div>
	<div>
        <%@ include file="/jsp/finance/LeftMenu.jsp"%>
		<div class="container-right" style="overflow:auto;">
			<div class="overview">
				<h1 class="heading1"><s:text name="buzzads.admin.adjustment.form.title"/></h1>
	            <div class="clear spacer15"></div>
	            <table class="viewTable" style="width:100%;">
	                <tr>
	                    <td width="100px"><s:text name="buzzads.admin.adjustment.form.account.title"/></td>
	                    <td><input class="validator bTextbox" placeholder="User ID..." required="required" value="" type="number" min="1" type="text" id="receiverId" style="width:100px;" /></td>
	                </tr>
	                <tr>
	                    <td><s:text name="buzzads.admin.adjustment.form.amount.title"/></td>
	                    <td>¥&nbsp;&nbsp;<input class="validator bTextbox" placeholder="Amount to adjust (can be negative)..." required="required" type="number" step="1" max="10000000" min="-10000000" id="receiveAmount" value="" style="width:200px;" /></td>
	                </tr>
	                <tr>
	                    <td style="vertical-align:top"><s:text name="buzzads.admin.adjustment.form.detail.title"/></td>
	                    <td><textarea class="validator bTextbox" required="required" type="text" maxlength="200" style="width:250px;height:100px;" id="detail" ></textarea></td>
	                </tr>
	                <tr>
	                    <td></td>
	                    <td>
	                        <input id="submit" class="bButton center lightOrange" type="button" value="<s:text name='buzzads.admin.adjustment.form.save.text'/>" />
	                        <input class="bButton center" style="margin-left: 20px;" type="button" value="<s:text name='buzzads.admin.adjustment.form.back.text'/>" onclick="history.go(-1)" />
	                    </td>
	                </tr>
	            </table>
			</div>
            
		</div>
		<div class="clear spacer20"></div>
	</div>
</div>

<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.min.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.forms.min.1.2.6.js"></script>
<script type="text/javascript">
	$(function(){
	    $.tools.validator.localize('zh', {
	        '*': '<s:text name="buzzads.field.error.general"/>',
            ':number'   : '<s:text name="buzzads.field.error.number"/>',
            '[maxlength]'     : '<s:text name="buzzads.field.error.max3"/>',
            '[min]'     : '<s:text name="buzzads.field.error.min"/>',
            '[required]': '<s:text name="buzzads.field.error.required"/>'
	        });
	    $.tools.validator.conf.lang = "zh";
	    
	    $("#submit").click(function(){
	        var receiverId = $("#receiverId").val(), receiveAmount = $("#receiveAmount").val(), detail = $("#detail").val();
	        var inputs = $(".validator").validator(),
	        isPass = inputs.data("validator").checkValidity();
	        if(!isPass) return;
	        $.ajax({
	            type: "POST",
	            url: "saveAdjustment",
	            dataType: "json",
	            data: {
	                "adjustment.receiverId":receiverId,
	                "adjustment.receiveAmount":receiveAmount,
	                "adjustment.detail": detail
	            },
	            success: function(callback){
	                if(callback.success){
	                    displayStatusMessage("<s:text name='buzzads.admin.adjustment.submit.success.message'/>");
	                    setTimeout(function(){window.location.href='${cxt_path}/settlement/adjustments';},1000);
	                }else{
	                    displayStatusMessage(callback.message);
	                }
	            }
	        });
	    });
	    
	    //menu active
	    $("#leftmenu-finance-adjustments").addClass("active");
		$("#topmenu-finance-pay").addClass("active");
	});
</script>
