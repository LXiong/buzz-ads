<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>媒体统计</title>

<div class="container-body">
    <div class="spacer20"></div>
    <%@ include file="/jsp/admin/stats/LeftMenu.jsp"%>
    <div class="container-right" style="overflow:auto;">
        <div class="overviewPanel shadow">
            <div class="dataSearch formDate">
                <form action="sites" method="get" id="statsForm">
                    <div class="left">
                    	<font style="font-weight: bold;">网站Url:</font>
                        <input name="siteUrl" type="text" class="bTextbox" style="margin-left:15px;min-width:200px;width:200px;" value="${siteUrl }" />
                        <input name="sortColumn" type="hidden" value="${sortColumn }" />
                        <input name="sequence" type="hidden" value="${sequence }" />
                    </div>
                    <script type="text/javascript">
                        currentPath = "admin/sites";
                    </script>
                    <%@ include file="/jsp/admin/stats/CommSearch.jsp"%>
                    <div class="clear"></div>
                </form>
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
                    <th width="23%">网站Uuid</th>
                    <th width="23%">网站Url</th>
                    <th class="textRight">
                        <s:if test="#request.sortColumn eq 'views'">
                            <a class="sort" href="javascript:sortOperate('views', <s:if test='#request.sequence eq "ASC"'>'DESC'</s:if><s:else>'ASC'</s:else>)"> 
                                                                                                   展示<span class="<s:if test='#request.sequence eq "ASC"'>sort_up_select</s:if><s:else>sort_down_select</s:else>"></span></a>
                        </s:if>
                        <s:else>
                            <a class="sort" href="javascript:sortOperate('views', 'DESC')">展示<span class="sort_down"></span></a>
                        </s:else>
                    </th>
                    <th class="textRight">
                        <s:if test="#request.sortColumn eq 'pageview'">
                            <a class="sort" href="javascript:sortOperate('pageview', <s:if test='#request.sequence eq "ASC"'>'DESC'</s:if><s:else>'ASC'</s:else>)"> 
                                                                                                    网站PV<span class="<s:if test='#request.sequence eq "ASC"'>sort_up_select</s:if><s:else>sort_down_select</s:else>"></span></a>
                        </s:if>
                        <s:else>
                            <a class="sort" href="javascript:sortOperate('pageview', 'DESC')"> 网站PV<span class="sort_down"></span></a>
                        </s:else>
                    </th>
                    <th class="textRight">
                        <s:if test="#request.sortColumn eq 'clicks'">
                            <a class="sort" href="javascript:sortOperate('clicks', <s:if test='#request.sequence eq "ASC"'>'DESC'</s:if><s:else>'ASC'</s:else>)"> 
                                                                                                    点击<span class="<s:if test='#request.sequence eq "ASC"'>sort_up_select</s:if><s:else>sort_down_select</s:else>"></span></a>
                        </s:if>
                        <s:else>
                            <a class="sort" href="javascript:sortOperate('clicks', 'DESC')">点击<span class="sort_down"></span></a>
                        </s:else>
                    </th>
                    <th class="textRight">点击率</th>
                    <th style="text-align:center">
                        <s:if test="#request.sortColumn eq 'comm'">
                            <a class="sort" href="javascript:sortOperate('comm', <s:if test='#request.sequence eq "ASC"'>'DESC'</s:if><s:else>'ASC'</s:else>)"> 
                                                                                                   预计收入<span class="<s:if test='#request.sequence eq "ASC"'>sort_up_select</s:if><s:else>sort_down_select</s:else>"></span></a>
                        </s:if>
                        <s:elseif test="#request.sortColumn eq ''|| #request.sortColumn == null ">
                            <a class="sort" href="javascript:sortOperate('comm', 'ASC')">预计收入<span class="sort_down_select"></span></a>
                        </s:elseif>
                        <s:else>
                            <a class="sort" href="javascript:sortOperate('comm', 'DESC')">预计收入<span class="sort_down"></span></a>
                        </s:else>             
                    </th>
                </tr>
                <s:if test="#request.siteList != null && #request.siteList.size > 0">
                    <s:iterator value="#request.siteList" var="site" status="status">
                        <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
                            <td>
                               <a href="${cxt_path}/admin/overview?uuidString=${site.uuid }&siteUrl=${site.url }&dateStart=${dateStart }&dateEnd=${dateEnd }">${site.uuid }</a>
                            </td>
                            <td>
                                ${site.url }
                            </td>
                            <td class="textRight">
                                ${site.views }
                            </td>
                            <td class="textRight">
                                ${site.pageview}
                            </td>
                            <td class="textRight">
                                ${site.clicks}
                            </td>
                            <td class="textRight">
                                ${site.clickToView}
                            </td>
                            <td style="text-align:center">¥${site.commDouble}</td>
                        </tr>
                    </s:iterator>
                </s:if>
                <s:else>
                    <tr>
                        <td colspan="10" class="no-data">暂无数据</td>
                    </tr>
                </s:else>
            </table>
            <div class="clear spacer10"></div>
                <form action="${cxt_path}/admin/sites" id="filterSearchForm" method="post">
                    <div id="pagination" class="right pagination"></div>
                    <input type="hidden" value="${dateStart}" name="dateStart" />
                    <input type="hidden" value="${dateEnd}" name="dateEnd"/>
                    <input type="hidden" value="${availableStats}" name="availableStats" />
                    <input name="sortColumn" type="hidden" value="${sortColumn }" />
                    <input name="sequence" type="hidden" value="${sequence }" />
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
    function sortOperate(sortColumn, sequence) {
    	var form = $("#statsForm")[0];
    	$("input[name='sortColumn']").each(function() {$(this).val(sortColumn);});
    	$("input[name='sequence']").each(function() {$(this).val(sequence);});
    	form.submit();
    }
$(function() {
	//submit form
	$("#wsel").change(function(){
		$("#statsForm").submit();
	});
    //menu active
    $("#topmenu-admin-home").addClass("active");
    $("#leftmenu-admin-stats-site").addClass("active");
    
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