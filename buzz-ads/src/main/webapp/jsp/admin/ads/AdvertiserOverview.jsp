<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>广告主数据概览</title>

<div class="container-body">
	<div class="spacer20"></div>
	<div>
		<%@ include file="/jsp/admin/stats/LeftMenu.jsp"%>
		<div class="container-right" style="overflow:auto;">
			<div class="overview">
				<table class="bTableInfo"  style="font-size:14px;" >
					<tbody>
						<tr>
							<td>上月的收入</td><td><font class="unit">¥</font><font class="number"> ${lastTotalStats.totalCommissionDouble}</font></td><td></td>
						</tr>
						<tr>
							<td >本月的收入</td><td><font class="unit">¥</font><font class="number"> ${currentTotalStats.totalCommissionDouble}</font></td>
							 <td >
                                <a class="bLink orange spacer_left50"  href="${cxt_path }/admin/finance/billing">查看明细 &gt;&gt;</a>
                            </td>
						</tr>
					</tbody>
		   	    </table>
		   	 </div>   
		   	 <div class="clear spacer15"></div>
		   	 <div class="overviewPanel shadow">
	            <div class="dataSearch formDate">
	                 <form action="overview" method="post" id="overviewForm">
	                     <div class="left">
			   	   			<s:select list="advertisers"  listKey="advertiserId" listValue="companyName" value="advId" name="advId" headerKey="0" headerValue="全部广告主" cssClass="bTextbox">
	                        </s:select>
	                        -
			   	    		<s:select list="campaigns"  listKey="id" listValue="name" value="campaignId" name="campaignId" headerKey="0" headerValue="全部活动" cssClass="bTextbox">
	                        </s:select>
			   	         </div>
	                     <script type="text/javascript">
	                         currentPath = "admin/overview";
	                         formId="overviewForm";
	                     </script>
	                     <%@ include file="/jsp/advertiser/campaigns/CommSearch.jsp"%>
	                     <div class="clear"></div>
	                 </form>  
	            </div> 
	        </div>  
	        <div class="overviewPanel details" style="border-top:0px;">
	            <div class="panelTitle">数据汇总</div>
	        </div>
		   <div class="overview" style="border-top:0px;" >
		        <table class="bTableInfo" style="width:100%;" cellpadding="0px" cellspacing="0px">
		        	<tbody>
		        		<tr>
		        			<td class="panelTitle" >展现量</td>
		        			<td class="panelTitle" >点击量</td>
		        			<td class="panelTitle" >点击率</td>
		        			<td class="panelTitle last" style="text-align:center;">收入</td>
		        		</tr>
		        		<tr>
                            <td class="panelCon"><s:text name="global.format.number"><s:param value="%{#request.totalStats['views']}"/></s:text></td>
                            <td class="panelCon"><s:text name="global.format.number"><s:param value="%{#request.totalStats['clicks']}"/></s:text></td>
                            <td class="panelCon">${totalStats.clickToView}</td>
                            <td class="panelCon last"><font class="unit">¥</font> ${totalStats.totalCommissionDouble}</td>
                        </tr>
		        	</tbody>
		        </table>
		  </div>
		  <%@ include file="/jsp/admin/ads/CommStats.jsp"%>
		  <div class="overviewPanel details" style="border-top:0px;">
            <div class="panelTitle">详细数据</div>
          </div>  
           <div class="overview" style="border-top:0px;" >
				<div class="clear spacer10"></div> 
		        <table class="bTable textRight">
                    <tr class="heading">
                        <th ><a class="sort" href="javascript:void(0)">时间</a> </th>
                        <th class="textRight">展现量</th>
                        <th class="textRight">点击量</th>
                        <th class="textRight">点击率</th>
                        <th class="textRight">收入</th>
                    </tr>
                    <s:if test="#request['stats'].size() > 0">
                            <s:iterator value="#request['stats']" var="stat" status="status" >
                                <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
                                    <td style="text-align:left;">${stat.dateDay}</td> 
                                    <td><s:text name="global.format.number"><s:param value="%{#stat.views}"/></s:text></td>
                                    <td><s:text name="global.format.number"><s:param value="%{#stat.clicks}"/></s:text></td>
                                    <td>${stat.clickToView}</td>
                                    <td>¥${stat.totalCommissionDouble}</td>
                                </tr>
                            </s:iterator>
                     </s:if>
                     <s:else>
                            <tr>
                                <td colspan="11" class="no-data">暂无数据</td>
                            </tr>
                     </s:else>
                </table>
                <div class="clear spacer5"></div>
                <form action="${cxt_path}/admin/overview" id="filterSearchForm" method="post">
                    <div id="pagination" class="right pagination"></div>
                    <input type="hidden" value="${dateStart}" name="dateStart" />
                    <input type="hidden" value="${dateEnd}" name="dateEnd"/>
                    <input type="hidden" value="${availableStats}" name="availableStats" />
                    <input type="hidden" value="${advId}" name="advId" />
                    <input type="hidden" value="${campaignId}" name="campaignId" />
                </form>
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
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/jquery.pagination.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.toolbox.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.forms.min.1.2.6.js"></script>

<script type="text/javascript">
	$(function() {
		$("#leftmenu-admin-overview").addClass("active");
		$("#topmenu-admin-home").addClass("active");
		$("#advId").change(function(){
			$("#campaignId").val(0);
			$("#overviewForm").submit();
		});
		$("#campaignId").change(function(){
			$("#overviewForm").submit();
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
