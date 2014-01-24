<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>概览</title>

<style>
.revenue { vertical-align: middle; width: 60%; border-collapse: collapse; }
.revenue td { width: 25%; text-align: center; padding: 5px 0; border-right: 1px solid #dadada; }
.revenue td.last { border-right: none; }
</style>

<div class="container-body">
    <script type="text/javascript" >
        var isSitePage = false;
    </script>
    <div class="spacer20"></div>
    <%@ include file="/jsp/admin/stats/LeftMenu.jsp"%>
    <div class="container-right" style="overflow: auto;">
        <s:if test="#request.uuidString != null && #request.uuidString != ''">
	        <div class="overview bc">
	            <p class="breadcrumb">
	                <a class="bLinkD" style="padding-left:15px;" href="overview">媒体统计</a>&gt;&gt;
	                <a class="orange" title="" style="padding-left:10px;" href="javascript:void(0)">网站  - ${siteUrl }</a>
	            </p>
	        </div>
	        <script type="text/javascript" >
	            isSitePage = true;
	        </script>
	        <div class="clear spacer15"></div>
	    </s:if>
	    <s:if test="#request.uuidString == null || #request.uuidString == ''">
        <div class="overview">
            <table class="revenue left">
                <tr>
                    <td>昨日预计收入</td><td>未结算收入</td><td class="last">累计支付收入</td>
                </tr>
                <tr>
                    <td><font class="unit orange">¥</font><font class="orangeNumber">${request.yesterdayComm}</font></td>
                    <td><font class="unit orange">¥</font><font class="orangeNumber">${request.unpaidComm}</font></td>
                    <td class="last"><font class="unit orange">¥</font><font class="orangeNumber">${request.allComm}</font></td>
                </tr>
            </table>
            <div class="fix">
                <%-- <div class="spacer10"></div>
                <a class="bLinkOrange" href="${cxt_path}/admin/settlement">查看明细&nbsp;&gt;&gt;</a>
                <div class="clear spacer15"></div>
                <font class="disabled">预计佣金可能不等于实际佣金</font>
                <div class="clear"></div> --%>
            </div>
            <div class="clear"></div>
        </div>
        <div class="clear spacer15"></div>
        </s:if>
        
        <div class="overviewPanel shadow">
            <div class="dataSearch formDate">
                <form action="overview" method="get" id="statsForm">
                    <script type="text/javascript">
                        currentPath = "admin/overview";
                    </script>
                    <s:if test="#request.uuidString != null || #request.uuidString != ''">
                        <!-- 加入uuid检索条件 -->
                        <input type="hidden" id="uuidString" name="uuidString" value="${uuidString }">
                        <input type="hidden" id="siteUrl" name="siteUrl" value="${siteUrl }">
                    </s:if>
                    <%@ include file="/jsp/admin/stats/CommSearch.jsp"%>
                    <div class="clear"></div>
                    
                </form>  
            </div> 
        </div>
        <div class="overviewPanel details">
            <div class="panelTitle">佣金比例</div>
        </div>
        <div class="overview" style="border-top:0px">
            <table id="divPie" style="table-layout: fixed; width: 94%; display: none;">${jsonDataPubCommissionPie}</table>
            <div id="PieCon"></div>
        </div>
        <div class="overviewPanel details" style="border-top-width: 0px;">
            <div class="panelTitle">收入趋势</div>
        </div>
        <div class="overview" style="border-top:0px">
            <div style="margin-bottom:30px;position:relative">
                <div class="clear spacer15"></div>
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
                    <th class="textRight">展示</th>
                    <th class="textRight">点击</th>
                    <th class="textRight">点击率</th>
                    <th class="textRight">预计收入</th>
                    <th class="textRight">预计eCPM</th>
                </tr>
                <s:if test="#request.adminDailyStatistics != null && #request.adminDailyStatistics.size > 0">
                    <s:iterator value="#request.adminDailyStatistics" var="adminDailyStatistic" status="status">
                        <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
                            <td>
                                <s:date name="#adminDailyStatistic.dateDay" format="yyyy-MM-dd"/>
                            </td>
                            <td class="textRight">
                                <s:text name="global.format.number"><s:param value="%{#adminDailyStatistic.views}"/></s:text>
                            </td>
                            <td class="textRight">
                                <s:text name="global.format.number"><s:param value="%{#adminDailyStatistic.clicks}"/></s:text>
                            </td>
                            <td class="textRight">${adminDailyStatistic.clickToView}</td>
                            <td class="textRight">¥ ${adminDailyStatistic.pubCommissionDouble}</td>
                            <td class="textRight">${adminDailyStatistic.pubECPM}</td>
                            
                        </tr>
                    </s:iterator>
                </s:if>
                <s:else>
                    <tr>
                        <td colspan="6" class="no-data">暂无数据</td>
                    </tr>
                </s:else>
            </table>
            <div class="clear spacer10"></div>
                <form action="${cxt_path}/admin/overview" id="filterSearchForm" method="post">
                    <div id="pagination" class="right pagination"></div>
                    <input type="hidden" value="${dateStart}" name="dateStart" />
                    <input type="hidden" value="${dateEnd}" name="dateEnd"/>
                    <input type="hidden" value="${availableStats}" name="availableStats" />
                    <s:if test="#request.uuidString != null || #request.uuidString != ''">
                        <!-- 加入uuid检索条件 -->
                        <input type="hidden" name="uuidString" value="${uuidString }">
                        <input type="hidden" name="siteUrl" value="${siteUrl }">
                    </s:if>
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
<script type="text/javascript" charset="utf-8">
    $(function() {
        //submit form
        $("#wsel").change(function(){
            $("#statsForm").submit();
        });
        $("#topmenu-admin-home").addClass("active");
        
        if (isSitePage) {
            $("#leftmenu-admin-stats-site").addClass("active");
            $(".dateShortcut a").each(function () {
                $(this).attr("href", "${cxt_path}/" + currentPath + "?queryRange=" + ($(this).index() + 1) + 
                		"&uuidString=" + $('#uuidString').val() + "&siteUrl=" + $('#siteUrl').val());
            });
            $(document).attr("title","媒体管理");
        } else {
            $("#leftmenu-admin-stats").addClass("active");
        }
        //init visualize
        $('#divChartShares').visualize({
            type : "area",
            lineDots : 'double',
            interaction : true,
            tooltip : true,
            lineMargin : 0,
            yLabelNumber : 5,
            xLabelInterval :${xLabelInterval},
            showXLines : true,
            // needs to be container width minus 60
            height : 180, // needs to be container height minus 20
            chartId : "websiteChartShares",
            tooltipfilldot : true,
            tooltipdotenlarge : true,
            isRound:${isRound}
        });
        
        $('#divPie').visualize({
            type : "pie",
            // needs to be container width minus 60
            height : 250, // needs to be container height minus 20
            width:500,
            chartId: "PieCon",
            appendTitle:true,
            appendKey:true
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
        //initPage(1,10,30);
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

