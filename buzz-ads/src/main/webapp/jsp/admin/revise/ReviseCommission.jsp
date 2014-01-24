<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>Revise</title>

<div class="container-body">
    <div class="spacer20"></div>
    
    <div>
        <%@ include file="/jsp/admin/revise/LeftMenu.jsp"%>
        
        <div class="dataSearch container-right">
        	<div class="overviewPanel details" style="border-top-width:1px;">
			    <div class="inlineBlock panelTitle">
			    	数据订正
	             </div>
			</div>
            <div class="overview noBT">
	            <div class="clear spacer10"></div>
	            <div><strong>请选定日期订正前一天的佣金数据</strong></div>
	            <div class="clear spacer15"></div>
	            <form action="reviseAction" method="post">
	                <input class="bTextbox date left" type="date" id="datePickerStart" name="reviseDate" value="${reviseDate}" data-value="${reviseDate}" max="0">
	                <input class="bButton bButton24 left mSpacer_left10"  type="submit" value="订正" />
	            </form> 
	            <div class="clear"></div>
	            
	            <div class="clear spacer30"></div>
	            <div><a class="bLink orange" href="revisePublisherSettlement"><strong>点击订正该月的站长结账数据</strong></a></div>
	            <div class="clear spacer15"></div>
            </div>
        </div>
        
        <div class="clear"></div>
    </div>
    <div class="clear spacer20"></div>
    
    
</div>
<div class="clear"></div>

<link rel="stylesheet" href="${css_path}/calendar-date.css" type="text/css" />
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.toolbox.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.forms.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8">
    $(function() {
        $("#leftmenu-admin-revise").addClass("active");
        $("#topmenu-admin-revise").addClass("active");

        // init date pickers
        var fDay = 0;
        $.tools.dateinput.localize("zh", {
            months: "一月,二月,三月,四月,五月,六月,七月,八月,九月,十月,十一月,十二月",shortMonths: "一,二,三,四,五,六,七,八,九,十,十一,十二",days: "星期日,星期一,星期二,星期三,星期四,星期五,星期六",shortDays: "周日,周一,周二,周三,周四,周五,周六"
        });
        fDay = 1;

        $(":date").dateinput({ trigger: true, format: 'yyyy-mm-dd', lang: 'zh', firstDay: fDay });
        $(":date").bind("onShow onHide", function()  {
            $(this).parent().toggleClass("active"); 
        });
    });
</script>