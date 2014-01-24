<%@include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>活动统计详情</title>
<div class="container-body">
    <div class="spacer20"></div>
    <div>
        <%@ include file="/jsp/admin/stats/LeftMenu.jsp"%>
        <div class="container-right" style="overflow: auto;">
        	<div class="overview">
	            <div class="left topUp">
	                <span>活动名称：</span>
	                <span><font style="vertical-align:middle;" class="orangeNumber"><core:partOfText value="${campaign.name}" textmaxlength="30" escape="false"/></font></span>
	            </div>
	            <div class="left" style="margin-left:40px; border-left: 1px solid #dadada; padding-left: 40px;">
	                <table class="bTableInfo" style="font-size:14px;">
	                    <tbody>
	                        <tr>
	                            <td>广告主</td><td><font class="number"><s:property value="%{advertiserName}"/></font></td><td></td>
	                        </tr>
	                        <tr>
	                            <td>收入总览</td><td><font class="unit">¥</font> <font class="number"><s:property value="%{totalStatistic.totalCommissionDouble}"/></font></td>
	                            <td>
	                            	<s:a cssClass="bLink orange spacer_left50" action="admin/campaigns/view?id=%{campaignId}" target="_blank">活动详情 &gt;&gt;</s:a>
	                             </td>
	                        </tr>
	                    </tbody>
	                </table>
	            </div>
	        </div>
	        <div class="spacer15"></div>
            <div class="overviewPanel shadow">
                <div class="right dataSearch">
                    <form action="" class="right" method="post" id="filterSearchForm" name="filterSearchForm">
                        <input class="bTextbox date left" type="date" id="datePickerStart" name="dateStart" value="${dateStart}" data-value="${dateStart}" max="${dateEnd}">
                        <div class="left" style="padding: 0px 10px; line-height: 24px; height: 24px;">-</div>
                        <input class="bTextbox date left" type="date" data-orig-type="date" id="datePickerEnd" name="dateEnd" value="${dateEnd}" data-value="${dateEnd}" max="0">
                        <input class="bButton bButton24 left mSpacer_left10" type="submit"  value="查询" />
                    </form>
                </div>
            </div>
            <div class="overview noBT">
            	 <div class="spacer10"></div>
	             <table class="bTable">
	                 <tr>
	                     <th>日期</th><th>原始展示量</th><th>原始点击量</th>
	
	                         <s:if test="campaign.bidType==@com.buzzinate.buzzads.enums.BidTypeEnum@CPM">
	                             <th>有效展示量</th>
	                         </s:if>
	                         <s:elseif test="campaign.bidType==@com.buzzinate.buzzads.enums.BidTypeEnum@CPC">
	                             <th>有效点击量</th><th>有效CTR</th>
	                         </s:elseif>
	                         <s:else>
	                             <th>有效订单量</th>
	                         </s:else>
	
	                     <th>收入</th>
	                 </tr>
	                 <s:if test="dailyStatistics.size>0">
	                 <s:iterator value="dailyStatistics" var="ds" status="status">
	                    <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
	                        <td>${ds.dateDay}</td>
	                        <td>${ds.views}</td>
	                        <td>${ds.clicks}</td>
	                        <s:if test="campaign.bidType==@com.buzzinate.buzzads.enums.BidTypeEnum@CPM">
	                            <td>${ds.cpmViewNo}</td>
	                        </s:if>
	                        <s:elseif test="campaign.bidType==@com.buzzinate.buzzads.enums.BidTypeEnum@CPC">
	                            <td>${ds.cpcClickNo}</td><td>${ds.cpcCTR}</td>
	                        </s:elseif>
	                        <s:else>
	                            <td>${ds.cpsOrderNo}</td>
	                        </s:else>
	                        <td>¥${ds.totalCommissionDouble}</td>
	                    </tr>
	                 </s:iterator>
	                 </s:if>
	                 <s:else>
	                     <tr>
	                         <td colspan="<s:if test='campaign.bidType==@com.buzzinate.buzzads.enums.BidTypeEnum@CPC'>6</s:if><s:else>5</s:else>" class="no-data">暂无数据</td>
	                     </tr>
	                 </s:else>
	             </table>
            </div>
             <div class="clear spacer5"></div>
             <div id="pagination" class="right pagination"></div>
             <div id="pageDesCon" class="right"></div>
             <div class="clear spacer15"></div>
             <div><font class="disabled textRight">数据生成时间&nbsp;&nbsp;<span id="creationDate"></span></font></div>
        <div class="clear spacer20"></div>
        </div>
    </div>

</div>

<link rel="stylesheet" href="${css_path}/calendar-date.css" type="text/css" />
<link rel="stylesheet" href="${css_path}/pagination.css" type="text/css" />

<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.toolbox.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.forms.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/jquery.pagination.js"></script>
<script type="text/javascript" charset="utf-8">
    $(function() {
    	var templocal=window.location+"";
    	var leftId=(templocal.split("?")[1]).split("=")[1];
        $("#"+leftId).addClass("active");
        $("#topmenu-admin-home").addClass("active");
        // init date pickers
        var fDay = 0;
        $.tools.dateinput.localize("zh", {
            months : "一月,二月,三月,四月,五月,六月,七月,八月,九月,十月,十一月,十二月",
            shortMonths : "一,二,三,四,五,六,七,八,九,十,十一,十二",
            days : "星期日,星期一,星期二,星期三,星期四,星期五,星期六",
            shortDays : "周日,周一,周二,周三,周四,周五,周六"
        });
        fDay = 1;

        $(":date").dateinput({
            trigger : true,
            format : 'yyyy-mm-dd',
            lang : 'zh',
            firstDay : fDay
        });
        $(":date").bind("onShow onHide", function() {
            $(this).parent().toggleClass("active");
        });
        if ($(":date:first").length != 0) {
            $(":date:first").data("dateinput").change(
                    function() {
                        // we use it's value for the seconds input min option
                        $(":date:last").data("dateinput").setMin(
                                this.getValue(), true);
                    });
            $(":date:last").data("dateinput").change(
                    function() {
                        // we use it's value for the first input max option
                        $(":date:first").data("dateinput").setMax(
                                this.getValue(), true);
                    });
        }

        // pagination
        function initPage(pageNum, pageSize, totalRecords) {
            var pn = parseInt(pageNum, 10) - 1;
            var ps = parseInt(pageSize, 10);
            var tr = parseInt(totalRecords, 10);
            if (pn < 0) {
                return;
            }
            var opt = {
                form : $("#filterSearchForm"),
                pageDesCon : $("#pageDesCon"),
                next_text : "下一页",
                current_page : pn,
                items_per_page : ps,
                num_display_entries : 5,
                num_edge_entries : 2,
                prev_text : "上一页"
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