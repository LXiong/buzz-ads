<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>CPM明细 - 效果报告</title>

<div class="container-body">
    <div class="spacer20"></div>
    <%@ include file="/jsp/admin/stats/LeftMenu.jsp"%>
    <div class="container-right" style="overflow:auto;">
        <div class="overviewPanel shadow">
            <div class="dataSearch">
                <form action="cpm" method="get" id="statsForm">
                        <!--<font style="font-weight: bold;">网站UUID:</font>
                         <s:if test="#request.sites != null && !#request.sites.isEmpty()">
                            <s:select value="#request.uuidString" list="#request.sites" id="wsel" name="uuidString" listKey="uuid" listValue="url"  emptyOption="false" cssClass="bTextbox" cssStyle="margin-left:15px;min-width:200px;width:300px;">
                            </s:select> 
                        </s:if> 
                        <s:else>
                            <span style="margin-left:15px;color:red;">没有网站</span>
                        </s:else> 
                        <input name="uuidString" type="text" class="bTextbox" value="${uuidString }" />-->
                    <script type="text/javascript">
                        currentPath = "admin/stats/cpm";
                    </script>
                    <%@ include file="/jsp/admin/stats/CommSearch.jsp"%>
                    <div class="clear"></div>
                </form>  
            </div> 
        </div>
        <div class="overviewPanel details">
            <div class="panelTitle">收入趋势</div>
        </div>
        <div class="overview" style="border-top:0px">
            <div style="margin-bottom:30px;position:relative">
                <table id="divChartShares" style="table-layout:fixed;width:94%;display:none;">${jsonDataPubCommission}</table>
                <div id="websiteChartShares"></div>
            </div>
        </div>
        <div class="clear"></div>
        <div class="overviewPanel details">
            <div class="panelTitle">详细数据</div>
        </div>
        <div class="overview" style="border-top:0px">
            <div class="clear spacer10"></div>
            <table class="bTable">
                <tr class="heading">
                    <th>日期</th>
                    <th class="textRight">原始展示量</th>
                    <th class="textRight">原始点击量</th>
                    <th class="textRight">有效展示量</th>
                    <th class="textRight">预计收入</th>
                </tr>
                <s:if test="#request.adminDailyStatistics != null && #request.adminDailyStatistics.size > 0">
                    <s:iterator value="#request.adminDailyStatistics" var="adminDailyStatistic" status="status">
                        <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
                            <td>
                                <s:date name="#adminDailyStatistic.dateDay" format="yyyy-MM-dd"/>
                            </td>
                            <td class="textRight">
                                <s:text name="global.format.number"><s:param value="%{#adminDailyStatistic.cpmViewNo}"/></s:text>
                            </td>
                            <td class="textRight">
                                <s:text name="global.format.number"><s:param value="%{#adminDailyStatistic.cpmClickNo}"/></s:text>
                            </td>
                            <td class="textRight">
                                <s:text name="global.format.number"><s:param value="%{#adminDailyStatistic.cpmViewNo}"/></s:text>
                            </td>
                            <td class="textRight">¥${adminDailyStatistic.cpmPubCommissionDouble}</td>
                        </tr>
                    </s:iterator>
                </s:if>
                <s:else>
                    <tr>
                        <td colspan="5" class="no-data">暂无数据</td>
                    </tr>
                </s:else>
            </table>
            <div class="clear spacer10"></div>
                <form action="${cxt_path}/admin/stats/cpm" id="filterSearchForm" method="post">
                    <div id="pagination" class="right pagination"></div>
                    <input type="hidden" value="${dateStart}" name="dateStart" />
                    <input type="hidden" value="${dateEnd}" name="dateEnd"/>
                    <input type="hidden" value="${availableStats}" name="availableStats" />
                </form>
             <div id="pageDesCon" class="right"></div>
            <div class="clear spacer15"></div>
        </div>
        <div><font class="disabled textRight">数据生成时间&nbsp;&nbsp;<span id="creationDate"></span></font></div>
        <div class="clear spacer15"></div>
    </div>
</div>
<link rel="stylesheet" href="${css_path}/pagination.css" type="text/css" />
<link rel="stylesheet" href="${css_path}/visualize.css" />
<!--[if IE]><script type="text/javascript" src="${js_path}/libs/excanvas.js"></script><![endif]-->
<script type="text/javascript" src="${js_path}/libs/jquery.visualize.js"></script>
<script type="text/javascript" src="${js_path}/libs/jquery.visualize.tooltip.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/jquery.pagination.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.forms.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8" >
$(function() {
	//submit form
	$("#wsel").change(function(){
		$("#statsForm").submit();
	});
    //menu active
    $("#topmenu-admin-home").addClass("active");
    $("#leftmenu-admin-stats-cpm").addClass("active");
    
    $("#pageStatsTable tr").mouseover(function(){
        $(this).addClass("pageStatSel");
    }).mouseout(function(){
        $(this).removeClass("pageStatSel");
    });
    
    //limit the search date
    $("form").live("submit",function(){
        var dateStart = $("#datePickerStart").val(),
        dateEnd = $("#datePickerEnd").val(),
        dateStartMillseconds = new Date(dateStart).getTime(),
        dateEndMillseconds = new Date(dateEnd).getTime(),
        threeMonMillseconds = 92 * 24 * 3600 * 1000;
        if(dateEndMillseconds - dateStartMillseconds > threeMonMillseconds){
            alert("对不起，只能查找三个月以内");
            return false;
        }
    });
  
    //init visualize
    $('#divChartShares').visualize({
       type: "area",
       lineDots: 'double',
       interaction: true,
       tooltip: true,
       lineMargin: 0,
       yLabelNumber: 5,
       xLabelInterval: ${xLabelInterval},
       showXLines: true,
       // needs to be container width minus 60
       height: 180, // needs to be container height minus 20
       chartId: "websiteChartShares",
       tooltipfilldot: true,
       tooltipdotenlarge: true,
       isRound:${isRound}
    });
    
    $('#divPie').visualize({
        type : "pie",
        // needs to be container width minus 60
        height : 250, // needs to be container height minus 20
        width: 500
    });
  
    // pagination
    function initPage(pageNum, pageSize, totalRecords) {
        var pn = parseInt(pageNum, 10) - 1;
        var ps = parseInt(pageSize, 10);
        var tr = parseInt(totalRecords, 10);
        if (pn < 0) {
            return;
        }
        var opt = {
            form: $("#filterSearchForm"),
            pageDesCon:$("#pageDesCon"),
            next_text: "下一页",
            current_page: pn,
            items_per_page: ps,
            num_display_entries: 5,
            num_edge_entries: 2,
            prev_text: "上一页"
        };
        $("#pagination").pagination(tr, opt);
    }
    initPage("${page.pageNum}","${page.pageSize}", "${page.totalRecords}");
    function getDate(){
        var dateTemp=new Date();
        return dateTemp.getFullYear() 
            + "-" + ('0'+(dateTemp.getMonth()+1)).slice(-2)
            + "-" + ('0'+dateTemp.getDate()).slice(-2)
            + " " + ('0'+dateTemp.getHours()).slice(-2)
            + ":" + ('0'+dateTemp.getMinutes()).slice(-2)
            + ":" + ('0'+dateTemp.getSeconds()).slice(-2);
    }
    $("#creationDate").html(getDate());
});
</script>