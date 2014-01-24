<%@ include file="/jsp/common/Init.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<!-- Global floating notification start -->
<div id="notifications" class="hidden">
    <div class="notification-bar-container">
        <div class="notification-bar-bkg"></div>
        <div class="notification-bar">
            <div class="notification-bar-contents">
                <div class="notification-msg heading1"></div>
            </div>
        </div>
    </div>
</div>
<div id="status-messages-div" style="display:none;">
    <s:actionerror cssClass="action-error-messages"/>
    <s:actionmessage cssClass="action-messages"/>
</div>
<div id="field-error-messages-div" style="display:none;">
    <s:fielderror cssClass="field-error-messages"/>
</div>
<!-- Global floating notification END -->

<script>
	var statusMessageTimeout = 0;
	function displayStatusMessage(message, type) {
	    if($.trim(message).length<=0){
	        return;
	    }
	    var color = "#333", bgcolor = "#fff8cc", delay = 7000;
	    if(type == "error") {
	        delay = 30000;
	        color = "red";
	        bgcolor = "#FFD9D9";
	    } else if(type == "success") {
	        delay = 7000;
	        color = "green";
	        bgcolor = "#DDF8CC";
	    } else if(type == "info"){
	        delay = 10000;
	        color = "#333";
	        bgcolor = "#fff8cc";
	    }
	
	    $("#notifications .notification-msg").css("color", color);
	    $("#notifications .notification-bar-bkg").css("background", bgcolor);
	    if($("#notifications").is(":visible")) {
	        $("#notifications").hide();
	    }
	    $("#notifications .notification-msg").html(message);
	    $("#notifications").show();
	    // handle overflows:
	    $("#notifications .notification-bar-bkg").height($("#notifications .notification-bar").height());
	    
	    if (statusMessageTimeout != 0) {
	        clearTimeout(statusMessageTimeout);
	    }
	    statusMessageTimeout = setTimeout(function() { 
	        $("#notifications").slideUp(150);
	        $("#notifications .notification-msg").html(""); 
	    }, delay);
	}
	function hideStatusMessage() {
	    $("#notifications").hide();
	    $("#notifications .notification-msg").html("");
	    $(".field-error-messages").hide();
	    $(".errorInput").hide();
	}
	
	$(function(){
	    // Prepare for status message
	    var $statusMsgDiv = $("#status-messages-div");
	    var $errMegDiv = $("#field-error-messages-div");
	    if ($statusMsgDiv.text()) {
	        if ($(".action-error-messages").text()) {
	            displayStatusMessage($statusMsgDiv.html(), "error");
	        } else if ($(".action-messages").text()) {
	            displayStatusMessage( $statusMsgDiv.html(), "success");
	        }
	    }
	    if ($errMegDiv.text()) {
	        if ($(".field-error-messages").text()) {
	            displayStatusMessage($errMegDiv.html(), "error");
	        }
	        //$errMegDiv.show();
	    }
	    
	    // floating notification system
	    $("#notifications .notification-bar").click(function() {
	        $("#notifications").slideUp(150);
	    });
	});
</script>