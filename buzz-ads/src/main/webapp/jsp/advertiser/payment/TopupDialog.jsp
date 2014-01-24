<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.min.js"></script>
<style type="text/css">
    #dialogTopup .body { *width: 470px; }
    #dialogTopup input { font-size: 18px; margin: 0 10px 0 0; padding: 1px; border: 1px solid #666; }
    #dialogTopup .note { color: #999; font-weight: bold; padding: 0 10px; }
    #dialogSelectMode .close,#dialogSelectMode .close:hover{ background: #fff;}
    .bOverlay_chargeall{ z-index: 9999;}
    .errorInputSelf{ background-color: #FFD9D9; height: 20px; margin-left: 5px; padding: 3px 4px; z-index: 10001; display: none; position: absolute; top: 0px; left: 305px; _left: 177px; }
    .errorInputSelf p{background: url(${image_path}/error.gif) no-repeat 0 0; display: inline-block !important; line-height: 20px; padding-left: 24px; width: auto; float: left; }
    .rTable { border-collapse: collapse; text-align: left; vertical-align: middle; font-size: 16px; width: 100%; border: 1px solid #e9e9e9;}
    .rTable td { height: 30px; line-height: 25px; padding: 5px 20px; vertical-align: middle; white-space: normal; }

    /* MODAL AND OVERLAY DIALOGS */
    .bOverlay2,.bOverlay { background:url("${image_path}/background-opaque-dark.png");*background:#666;width:500px;padding:10px;
        -moz-border-radius: 3px; -webkit-border-radius: 3px; border-radius: 3px; display:none; opacity:1; }
    .bOverlay2 .body,.bOverlay .body { padding: 15px; *padding: 10px 15px; background: #fff; }
    .bOverlay2 .header,.bOverlay .header { color: #fff; background:#000056; height: 45px; line-height: 45px; padding: 0 25px 0 15px; font-size: 16px;}
    .bOverlay2 .header a,.bOverlay .header a { color:#fff; }
    
    .bOverlay2 .close:hover,.bOverlay .close:hover { color: #666; }

    .errorInput {background-color: #FFD9D9;height: 20px;margin-left: 5px;padding: 3px 4px;z-index: 10001;}
    .errorInput p {background: url("${image_path}/error.gif") no-repeat scroll 0 0 transparent;display: inline-block !important;line-height: 20px;padding-left: 24px;}
</style>

<div id="TopupCon" class="bOverlay bOverlay_chargeall">
   <div id="bodyCon">
   		 <div class="body">
	        <div class="clear spacer10"></div>
	        <span style="font-weight:bold;font-size:20px;"><s:text name="buzzads.recharge.topup.pay.online"/>:</span>
	        <div class="clear spacer10"></div>
	        <form id="topupForm" name="topupForm" method="POST" onsubmit="return verifyForm(this);">
	        <table class="rTable">
	            <tr>
	                <td style="width:80px;padding-top:20px;"><s:text name="buzzads.recharge.topup"/>：</td>
	                <td style="padding-top:20px;"><input type="text" min="10" max="${maxRechargePrice}" name="amount" value="0" style="width:120px;" required="required"/><s:text name="buzzads.recharge.topup.currency"/></td>
	            </tr>
	            <tr>
		            <td><s:text name="buzzads.recharge.topup.cost"/>：</td>
		            <td><input name="cost" disabled="disabled" value="0" style="width:120px;"/><s:text name="buzzads.recharge.topup.currency"/></td>
	            </tr>
	        </table>
	        <div class="clear spacer15"></div>
	        <button id="bButtonTopup" type="submit" class="bButton lightOrange right"><s:text name='buzzads.recharge.topup.pay.submit'/></button>
	        </form>
	        <div class="clear spacer15"></div>
	        <div style="text-align: right; font-size: 12px; color: #666;"><s:text name="buzzads.recharge.topup.alsodo"/><s:text name="buzzads.recharge.topup.pay.offline"/></div>
	    </div>
	    <div class="errorInput" style="visibility: visible; position: absolute; top: 236.8px; left: 982.5px;"></div>
   </div>

</div>
<div id="dialogTopup" class="popWin" style="display:none"></div>
<link rel="stylesheet" href="${css_path}/popWin.css" />
<script type="text/javascript" src="${js_path}/libs/popWin.js"></script>
<script type="text/javascript" charset="utf-8">

var isSubmitting = false;
function verifyForm(target) {
    if(isSubmitting) {
        return false;
    }
    hideStatusMessage();
    if (!$(target).data("validator").checkValidity()) {
        return false;
    }
    var valid = true;
    $("input:number").each(function() {
        if ($(this).val().replace(/[0-9]/g, "").length > 0 || $(this).val().indexOf("0") == 0) {
            displayStatusMessage('<s:text name="buzzads.recharge.topup.message"/>', "error");
            $(this).focus();
            valid = false;
            return false;
        }
    });
    if (!valid) {
        return false;
    }
    isSubmitting = true;
    displayStatusMessage('<s:text name="buzzads.recharge.wait"/>', "info");
    $("#bButtonTopup").html('<s:text name="buzzads.recharge.wait"/>');
    $(target).attr("action", "order");
    return true;
}
function regIntNum(str){
    var reg1 = /^\d+$/;
    if(str.match(reg1) == null)
        return false;
    else
        return true;
}

$(document).ready(function () {
	//create popWin
	var data={
			container:$("#dialogTopup"),
			width:500,
			height:360,
			title:"充值",
			content:$("#bodyCon"),
			closeCallback:function(){
			
			},
			afterCallback:function(){
			
			}
		}
	var popwin=new bshare.popWin(data);
	
    $("#dialogTopup").overlay({
        top: 0,
        mask: {
            color: '#e9e9e9',
            loadSpeed: 200,
            opacity: 0.5
        },
        closeOnClick: false,
        onLoad: function(e) {

        },
        onBeforeClose: function(e) {
            hideStatusMessage();
        }
    });
    $.tools.validator.localize('<s:text name="buzzads.lang.code"/>', {
        '*'         : '<s:text name="buzzads.field.error.general"/>',
        ':number'   : '<s:text name="buzzads.field.error.number"/>',
        '[max]'     : '<s:text name="buzzads.field.error.max3"/>',
        '[min]'     : '<s:text name="buzzads.field.error.min"/>',
        '[required]': '<s:text name="buzzads.field.error.required"/>'
    });

    $("#topupForm").validator({
        lang: '<s:text name="buzzads.lang.code"/>',
        messageClass: 'errorInput',
        onFail: function() {
            isSubmitting = false;
            $("#bButtonTopup").text('<s:text name="buzzads.recharge.topup.pay.online"/>');
            hideStatusMessage();
        }
    });
	function getMoney(o){
		var $o = $(o);
        $o.val($o.val().replace(/[^\d]/g,''));
        var $value = $o.val();
        if ($value >= ${maxRechargePrice}) {
            $value =${maxRechargePrice};
        }
    	$("input[name=cost]").val(($value * 1).toFixed(2));
		
	}
    $("input[name=amount]").keyup(function() {
    	getMoney(this);
    });
    $("input[name=amount]").change(function() {
    	getMoney(this);
    });
    
    
    $(".close").click(function () {
        $(this).closest(".bOverlay").hide();
    });



});
</script>
