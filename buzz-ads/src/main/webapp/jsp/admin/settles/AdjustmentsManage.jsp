<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>Adjustments Management</title>

<div class="container-body">
	<div class="spacer20"></div>
	
	<div>
	    <s:if test="%{#request.currentPageRole == 'admin'}">
            <%@ include file="/jsp/finance/LeftMenu.jsp"%>
        </s:if>
        <s:if test="%{#request.currentPageRole == 'finance'}">
            <%@ include file="/jsp/finance/LeftFinanceMenu.jsp"%>
        </s:if>
		
		<div class="container-right" style="overflow:auto;">
			<div class="overviewPanel shadow">
				<div class="right"><a class="bButton lightOrange" href="${cxt_path}/settlement/create">创建一个新的adjustment</a></div>
			</div>
			<div class="overview noBT" >
			    <div class="clear spacer10"></div>
				<table class="bTable">
					<tr class="heading">
						<th>时间</th>
						<th style="text-align:right;">广告主ID</th>
						<th style="text-align:right;">调整金额</th>
						<th>详细</th>
					</tr>
					<tr class="heading-float fixed hidden" style="top:0;"></tr>
					<s:if test="%{adjustments.size> 0}">
						<s:iterator value="adjustments" var="iterObj" status="status" >
						<tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
							<td>
	                            <s:date name="#iterObj.updateTime" format="yyyy-MM-dd HH:mm:ss"/>
							</td>
							<td style="text-align:right;">
							    <s:property value="#iterObj.receiverId"/>
							</td>
							<td style="text-align:right;">
							    ¥<s:text name="global.format.number"><s:param value="%{#iterObj.amount/100}"/></s:text>
							</td>
							<td title="${iterObj.detail}"><core:partOfText value="${iterObj.detail}" textmaxlength="80"/></td>
						</tr>
						</s:iterator>
					</s:if>
					<s:else>
	                    <tr>
	                        <td colspan="4" class="no-data">暂无数据</td>
	                    </tr>
	                </s:else>
				</table>
				<div class="clear spacer5"></div>
				<form action="${cxt_path}/settlement/adjustments" id="searchForm" method="post">
	                <div id="pagination" class="right pagination"></div>
	                <div id="pageDesCon" class="right"></div>
	            </form>
	            <div class="clear spacer15"></div>
	        </div>
	        <div><font class="disabled textRight">数据生成时间&nbsp;&nbsp;<span id="creationDate"></span></font></div>
	        <div class="clear spacer15"></div>
		</div>
		<div class="clear"></div>
	</div>
	<div class="clear spacer20"></div>
</div>

<link rel="stylesheet" href="${css_path}/calendar-date.css" type="text/css" />
<link rel="stylesheet" href="${css_path}/pagination.css" type="text/css" />
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.min.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/jquery.pagination.js"></script>
<script type="text/javascript" charset="utf-8">
	var elemTop = $(".bTable .heading").offset().top;
	
	function isScrolledOut($elem) {
	    var docViewTop = $(window).scrollTop();
	    return elemTop <= docViewTop;
	}

	$(function() {
		$("#leftmenu-finance-adjustments").addClass("active");
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
                form: $("#searchForm"),
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
