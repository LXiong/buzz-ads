<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<div class="right" style="overflow: hidden;margin-left:30px;">
    <input class="bTextbox date left" type="date" id="datePickerStart" name="dateStart" value="${dateStart}" data-value="${dateStart}" max="${dateEnd}">
    <div class="left" style="padding:0px 10px;line-height:24px;height:24px;">-</div>
    <input class="bTextbox date left" type="date" data-orig-type="date" id="datePickerEnd" name="dateEnd" value="${dateEnd}" data-value="${dateEnd}"  max="0">
    <input class="bButton bButton24 left mSpacer_left10"  type="submit"  value="查询" />
    <input type="hidden" name="queryRange" id="queryRange" value="${queryRange}" />
</div>
<div class="dateShortcut right">
   <a class="bLinkD" href="javascript:void(0)">今天</a>
   <a class="bLinkD" href="javascript:void(0)">昨天</a>
   <a class="bLinkD" href="javascript:void(0)">上周</a>
   <a class="bLinkD" href="javascript:void(0)">前七天</a>
   <a class="bLinkD" href="javascript:void(0)">本月</a>
   <a class="bLinkD" href="javascript:void(0)">上月</a>
   <a class="bLinkD" href="javascript:void(0)">上季度</a>
</div>

<link rel="stylesheet" href="${css_path}/calendar-date.css" type="text/css" />
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.toolbox.min.1.2.6.js"></script>
<script type="text/javascript">
    $(function(){
    	
    	$(".dateShortcut a").each(function () {
    		if(!formId){
    			$(this).attr("href", "${cxt_path}/" + currentPath + "?queryRange=" + ($(this).index() + 1));
    		}
    	});
    	$(".dateShortcut a").click(function(){
    		$("#queryRange").val($(this).index() + 1);
    		$("#"+formId).submit();
    	});
        //select date
        if(${queryRange}){
            $($(".dateShortcut a")[${queryRange}-1]).attr("class","orange selected");   
        }
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
        if ($(":date:first").length != 0) {
            $(":date:first").data("dateinput").change(function() {
                // we use it's value for the seconds input min option
                $(":date:last").data("dateinput").setMin(this.getValue(), true);
            });
            $(":date:last").data("dateinput").change(function() {
                // we use it's value for the first input max option
                $(":date:first").data("dateinput").setMax(this.getValue(), true);
            });
        }
    });
</script>