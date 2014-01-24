<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>Admin数据概览</title>

<div class="container-body">
	<div class="spacer20"></div>
	<div>
		<%@ include file="/jsp/admin/stats/LeftMenu.jsp"%>
		
		<div class="container-right" style="overflow: auto;">
		  	<%@ include file="/jsp/admin/stats/StatsCommon.jsp" %>
        	<div class="overviewPanel details" style="border-top:0px;">
           	 	<div class="left tabTitle ">
					 	<a class=" orange" href="javascript:void(0)" style="cursor:default;">概&nbsp;&nbsp;览</a>
			         	<a class="mSpacer_left10" href="${cxt_path}/admin/stats/time" >按时间</a>
			         	<a class="mSpacer_left10" href="${cxt_path}/admin/stats/ads">按广告</a>	
			         	<a class=" mSpacer_left10" href="${cxt_path}/admin/stats/website">按网站</a>	        	
		        </div>
        	</div>
		  	<div class="overview" style="border-top:0px;">
				<div>
					<div class="clear spacer10"></div>
					<table class="bTable textRight">
						<tr class="heading">
							<th>时间</th>
							<th class="textRight">展示</th>
							<th class="textRight">点击</th>
							<th class="textRight">购买</th>
							<th class="textRight">CPS收入</th>
							<th class="textRight">CPS支出</th>
							<th class="textRight">CPC收入</th>
							<th class="textRight">CPC支出</th>
							<th class="textRight">预计收入</th>
							<th class="textRight">预计佣金</th>
						</tr>
						<s:if test="#request.adminStats != null && #request.adminStats.size > 0">
							<s:iterator value="#request.adminStats" var="stat" status="status">
								<tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
									<td style="text-align:left"><s:date name="#stat.dateDay" format="yyyy-MM-dd" /></td>
									<td><s:text name="global.format.number"><s:param value="%{#stat.views}"/></s:text></td>
									<td><s:text name="global.format.number"><s:param value="%{#stat.clicks}"/></s:text></td>
									<td><s:text name="global.format.number"><s:param value="%{#stat.cpsOrderNo}"/></s:text></td>
									<td>¥${stat.cpsTotalCommissionDouble}</td>
									<td>¥${stat.cpsPubCommissionDouble}</td>
									<td>¥${stat.cpcTotalCommissionDouble}</td>
									<td>¥${stat.cpcPubCommissionDouble}</td>
									<td>¥${stat.totalCommissionDouble}</td>
									<td>¥${stat.pubCommissionDouble}</td>
								</tr>
							</s:iterator>
						</s:if>
						<s:else>
		                    <tr>
		                        <td colspan="10" class="no-data">暂无数据</td>
		                    </tr>
	              		</s:else>
					</table>
					<div class="clear spacer5"></div>
	                <div id="pagination" class="right pagination"></div>
	                <div id="pageDesCon" class="right"></div>
		            <div class="clear spacer15"></div>
		        </div>
		  	</div>
		  	 <div><font class="disabled textRight">数据生成时间&nbsp;&nbsp;<span id="creationDate"></span></font></div>
		</div>
		<div class="clear spacer20"></div>
	</div>
</div>

<link rel="stylesheet" href="${css_path}/calendar-date.css" type="text/css" />
<link rel="stylesheet" href="${css_path}/pagination.css" type="text/css" />

<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.toolbox.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.forms.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/jquery.pagination.js"></script>
<script type="text/javascript" charset="utf-8">
	$(function() {
		$("#leftmenu-admin-stats").addClass("active");
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