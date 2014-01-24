<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>广告主结账</title>

<%
String path = (String) request.getAttribute("javax.servlet.forward.request_uri");
String cxtPath = request.getContextPath();
path = path.substring(cxtPath.length());
request.setAttribute("curPath", path);
%>

<div class="container-body">
	<div class="spacer20"></div>
	<div>
		<s:if test="%{#request.curPath.startsWith('/advertiser')}">
            <sec:authorize ifAnyGranted="ROLE_ADVERTISER">
            <%@ include file="/jsp/advertiser/billing/LeftMenu.jsp"%>
            </sec:authorize>
        </s:if>
        <s:elseif test="%{#request.curPath.startsWith('advertiser')}">
            <sec:authorize ifAnyGranted="ROLE_ADVERTISER">
            <%@ include file="/jsp/advertiser/billing/LeftMenu.jsp"%>
            </sec:authorize>
        </s:elseif>
		<s:else>
		    <sec:authorize ifAnyGranted="ROLE_AD_ADMIN,ROLE_AD_FINANCE">
            <%@ include file="/jsp/admin/user/LeftMenu.jsp"%>
            </sec:authorize>
		</s:else>
		
		<%@ include file="/jsp/advertiser/payment/TopupDialog.jsp"%>
		<div class="container-right" style="overflow: auto;">
			<div class="overview">
				<table class="bTable info inlineBlock" style="width:80%;font-size:14px;">
					<tbody>
						<tr>
							<td>账户余额：</td> 
							<td><font class="unit  orange">¥</font> <font class=orangeNumber>${request.balanceDouble}</font></td>
                            <s:if test="%{#request.curPath.startsWith('/advertiser')}">
                                <sec:authorize ifAnyGranted="ROLE_ADVERTISER">
                                    <td><a id="advertiser_recharge" class="bButton lightOrange advertiser_recharge" href="javascript:void(0)">充值&gt;&gt;</a></td>
                                </sec:authorize>
                            </s:if>
						</tr>
					</tbody>
		   	    </table>
			</div>
			<div class="clear spacer15"></div>
			<div class="overviewPanel shadow" style="border-top-width:1px;">
				<div class="dataSearch ">
		        	 <form action="${cxt_path}${curPath }?view=${view}&adId=${adId}" method="post"  id="filterSearchForm" name="filterSearchForm">
		        	     
		                <div class="left" style="margin-right:10px;">
		                        <s:select list="#{10:'全部',0:'支出',1:'充值',2:'调整'}" listKey="key" listValue="value" value="types" name="types" id="types" cssClass="bTextbox" >
		                        </s:select>
		                </div>
		        	 	 <div style="overflow: hidden" class="right">
				        	 <input class="bTextbox date left" type="date" id="datePickerStart" name="dateStart    " value="${dateStart}" data-value="${dateStart}" max="${dateEnd}">
	                         <div class="left" style="padding:0px 10px;line-height:24px;height:24px;">-</div>
	                         <input class="bTextbox date left" type="date" data-orig-type="date" id="datePickerEnd" name="dateEnd" value="${dateEnd}" data-value="${dateEnd}"  max="0">
		                     <input class="bButton bButton24 left mSpacer_left10"  type="submit"  value="查询" />
		        	 	 </div>  
		        	 </form>  
		        </div>
			</div>
			<div class="overviewPanel details" style="border-top:0px;">
		   	    	<div class="panelTitle inlineBlock">
		   	    		数据列表
		   	    	</div>
		   	    	 <div class="inlineBlock spacer_left10">
			            <a class="bLink" href="javascript:viewBillingRecord('${cxt_path}${curPath }?view=', 'day')">按记录查看</a>
			            <a class="bLink" href="javascript:viewBillingRecord('${cxt_path}${curPath }?view=', 'month')">按月查看</a>
			        </div>
		   	</div>
			<div class="overview" style="border-top:0px;">
		        <table class="bTable textRight">
                    <tr class="heading">
                        <th>时间</th>
                        <s:if test="#request.view !='month'"><th>类型</th></s:if>
                        <th class="textRight">支出</th>
                        <th class="textRight">充值</th>
                        <th class="textRight">余额</th>
                    </tr>
                    <s:if test="#request.billings != null && #request['billings'].size() > 0">
                        <s:iterator value="#request['billings']" var="billing" status="status">
                            <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
		                         <td style="text-align: left">
		                             <s:if test="#request.view !='month'"><s:date name="#billing.billingDay" format="yyyy-MM-dd"/></s:if>
		                             <s:else><s:date name="#billing.billingDay" format="yyyy-MM"/></s:else>
		                         </td> 
		                         <s:if test="#request.view !='month'">
		                             <td style="text-align:left">
		                                 <s:if test="#billing.type == @com.buzzinate.buzzads.enums.AdvertiserBillingType@DEBIT_DAY">
                                             支出
                                         </s:if>
                                         <s:elseif test="#billing.type == @com.buzzinate.buzzads.enums.AdvertiserBillingType@REFILL_RECHARGE">
                                             充值
                                         </s:elseif>
                                         <s:elseif test="#billing.type == @com.buzzinate.buzzads.enums.AdvertiserBillingType@ADJUSTMENT">
                                             调整
                                         </s:elseif>
                                         <s:else>
                                             ??
                                         </s:else>
		                             </td>
		                         </s:if>
		                         <td class="textRight">¥${billing.debitsDouble}</td>
		                         <td class="textRight">¥${billing.creditsDouble}</td>
		                         <td class="textRight">¥${billing.balanceDouble}</td>
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
                <div id="pagination" class="right pagination"></div>
                <div id="pageDesCon" class="right"></div>
	            <div class="clear spacer15"></div>
	        </div>
	        <div><font class="disabled textRight">数据生成时间&nbsp;&nbsp;<span id="creationDate"></span></font></div>
	        <div class="clear spacer15"></div>
		</div>
		<div class="clear spacer20"></div>
	</div>
</div>
<link rel="stylesheet" href="${css_path}/calendar-date.css" type="text/css" />
<link rel="stylesheet" href="${css_path}/pagination.css" type="text/css" />
<link rel="stylesheet" href="${css_path}/visualize.css" />
<!--[if IE]><script type="text/javascript" src="${js_path}/libs/excanvas.js"></script><![endif]-->
<script type="text/javascript" src="${js_path}/libs/jquery.visualize.js"></script>
<script type="text/javascript" src="${js_path}/libs/jquery.visualize.tooltip.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/jquery.pagination.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.toolbox.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.forms.min.1.2.6.js"></script>
<link rel="stylesheet" href="${css_path}/pagination.css" type="text/css" />
<script type="text/javascript">
	$(function(){
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
		
	    $("#leftmenu-advertiser-ads").addClass("active");
		$("#topmenu-advertiser-billing").addClass("active");
		
		$("#leftmenu-admin-advertiserManage").addClass("active");
	    $("#topmenu-admin-manage").addClass("active");
	    
		//init visualize
	    $('#divChartShares').visualize({
	       type: "area",
	       lineDots: 'double',
	       interaction: true,
	       tooltip: true,
	       lineMargin: 0,
	       yLabelNumber: 5,
	       xLabelInterval: 10,
	       showXLines: true,
	       // needs to be container width minus 60
	       height: 180, // needs to be container height minus 20
	       chartId: "websiteChartShares",
	       tooltipfilldot: true,
	       tooltipdotenlarge: true,
	       isRound:true
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
	    
	    $("#advertiser_recharge").click(function(){
            $("#dialogTopup").data("overlay").load();
        });
	});
</script>
<script type="text/javascript">
	function viewBillingRecord(action, viewType) {
		var form = document.getElementById('filterSearchForm');
		form.action = action + viewType+"&adId="+${adId};
		form.submit();
	}
</script>