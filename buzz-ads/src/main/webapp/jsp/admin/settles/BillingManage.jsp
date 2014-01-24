<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>账单管理</title>

<div class="container-body">
	<div class="spacer20"></div>
	<div>
	     <%@ include file="/jsp/finance/LeftMenu.jsp"%>
			<div class="container-right" style="overflow:auto;">
				<div class="overviewPanel shadow">
					<form action="${cxt_path}/admin/finance/billing?view=${view}" method="post" id="filterSearchForm" name="filterSearchForm">
					      <div class=" left" >
					           <s:select list="#request.advertiserNames"  listKey="key" listValue="value" headerKey="0" headerValue="全部" name="adId" id="adId" cssClass="bTextbox" ></s:select> - 
					           <s:select list="#{10:'全部',0:'支出',1:'充值',2:'调整'}" value="types" listKey="key" listValue="value" name="types" id="types" cssClass="bTextbox" ></s:select>
					      </div>
					      <div class="dataSearch right" >
					           <div class="right">
						           <input class="bTextbox date left" type="date" id="datePickerStart" style="width:120px;" name="dateStart" value="${dateStart}" data-value="${dateStart}" max="${dateEnd}">
						           <div class="left" style="padding:0px 10px;line-height:24px;height:24px;">-</div>
						           <input class="bTextbox date left" style="width:120px;" type="date" data-orig-type="date" id="datePickerEnd" name="dateEnd" value="${dateEnd}" data-value="${dateEnd}" max="0">
						           <input class="bButton bButton24 left mSpacer_left10"   type="submit"  value="查询" />
					           </div>
					      </div>
				      </form>
				</div>
				<div class="overviewPanel details" style="border-top:0px;">
				    <div class="inlineBlock panelTitle">
				    	<span>数据展示</span>
			             <a class="bLink orange spacer_left15" href="${cxt_path}/admin/finance/billing?view=day">按记录查看</a>
			             <a class="bLink orange spacer_left15" href="${cxt_path}/admin/finance/billing?view=month">按月查看</a>
		             </div>
				</div>
				<div class="overview" style="border-top:0px;">
				    <div class="clear spacer10"></div>
					<table class="bTable">
						<tr class="heading">
							<th>时间</th>
							<th>账号</th>
							<s:if test="#request.view !='month'"><th>类型</th></s:if>
							<th class="textRight">支出</th>
							<th class="textRight">充值</th>
							<th class="textRight">余额</th>
						</tr>
						<tr class="heading-float fixed hidden" style="top:0;"></tr>
					    <s:if test="#request.billings != null && #request['billings'].size() > 0">
		                    <s:iterator value="#request['billings']" var="billing" status="status">
		                        <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
		                         <td style="text-align:left">
		                             <s:if test="#request.view !='month'"><s:date name="#billing.billingDay" format="yyyy-MM-dd"/></s:if>
		                             <s:else><s:date name="#billing.billingDay" format="yyyy-MM"/></s:else>
		                         </td> 
		                         <td style="text-align: left">${billing.companyName}</td> 
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
		                        <td colspan="7" class="no-data">暂无数据</td>
		                    </tr>
		                </s:else>
					</table>
					<div class="clear spacer5"></div>
		            <div id="pagination" class="right pagination"></div>
		            <div id="pageDesCon" class="right"></div>
		            <div class="clear spacer15"></div>
		        </div>
		        <div><font class="disabled textRight">数据生成时间&nbsp;&nbsp;<span id="creationDate"></span></font></div>
		        <div class="clear spacer15"></div>
			</div>
	</div>
	<div class="clear spacer20"></div>
</div>

<link rel="stylesheet" href="${css_path}/calendar-date.css" type="text/css" />
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.min.js"></script>
<link rel="stylesheet" href="${css_path}/pagination.css" type="text/css" />
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/jquery.pagination.js"></script>
<script type="text/javascript" charset="utf-8">
	var elemTop = $(".bTable .heading").offset().top;
	function isScrolledOut($elem) {
	    var docViewTop = $(window).scrollTop();
	    return elemTop <= docViewTop;
	}

	$(function() {
		RC.CutSelect("adId");
		// init date pickers
	    var fDay = 0;
        $.tools.dateinput.localize("zh", {
            months: "一月,二月,三月,四月,五月,六月,七月,八月,九月,十月,十一月,十二月",shortMonths: "一,二,三,四,五,六,七,八,九,十,十一,十二",days: "星期日,星期一,星期二,星期三,星期四,星期五,星期六",shortDays: "周日,周一,周二,周三,周四,周五,周六"
        });
        fDay = 1;

        $(":date").dateinput({ trigger: true, format: 'yyyy-mm-dd', lang: 'zh', firstDay: 1, selectors: true });
        $(":date").bind("onShow onHide", function()  {
            $(this).parent().toggleClass("active"); 
        });
		
		$("#leftmenu-finance-billing").addClass("active");
		$("#topmenu-finance-pay").addClass("active");
		
		$(window).resize(function() {
            var html = "";
            $(".bTable .heading th").each(function() {
                html += "<th style='font-size:12px;text-align:" + $(this).css("text-align") + ";width:" + $(this).width() + "px;'>" + $(this).text() + "</th>";
            });
            $(".heading-float").html(html);
            $(".heading-float").css("left", $(".bTable .heading th").offset().left);
        }).resize();
        $(document).scroll(function() {
            if(isScrolledOut($(".bTable.heading"))) {
                $(".bTable .heading-float").show();
            } else {
                $(".bTable .heading-float").hide();
            }
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
