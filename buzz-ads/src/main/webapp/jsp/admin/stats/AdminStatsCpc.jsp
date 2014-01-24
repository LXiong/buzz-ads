<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>CPC明细 - 效果报告</title>

<div class="container-body">
    <div class="spacer20"></div>
    <%@ include file="/jsp/admin/stats/LeftMenu.jsp"%>
    <div class="container-right" style="overflow:auto;">
        <div class="overview">
        	<div class="left topUp">
	                <span>正在投放的CPC广告活动数量：${cpcCount }</span>
	        </div>
	        <div class="left" style="margin-left:40px; border-left: 1px solid #dadada; padding-left: 40px;">
				<table class="bTableInfo"  style="font-size:14px;" >
					<tbody>
						<tr>
							<td>上月的收入</td><td><font class="unit">¥</font><font class="number"> ${lastTotalStats.cpcTotalCommissionDouble}</font></td><td></td>
						</tr>
						<tr>
							<td >本月的收入</td><td><font class="unit">¥</font><font class="number"> ${currentTotalStats.cpcTotalCommissionDouble}</font></td>
							 <td >
	                              
	                           </td>
						</tr>
					</tbody>
		   	    </table>
	   	    </div>
	   	 </div>   
	   	<div class="clear spacer15"></div>
        <div class="overviewPanel shadow">
	            <div class="dataSearch formDate">
	                 <form action="cpc" method="post" id="overviewForm">
	                     <div class="left">
			   	   			<s:select list="advertisers"  listKey="advertiserId" listValue="companyName" value="advId" name="advId" headerKey="0" headerValue="全部广告主" cssClass="bTextbox">
	                        </s:select>
	                        -
			   	    		<s:select list="campaigns"  listKey="id" listValue="name" value="campaignId" name="campaignId" headerKey="0" headerValue="全部活动" cssClass="bTextbox">
	                        </s:select>
			   	         </div>
			   	         <input name="sortColumn" type="hidden" value="${sortColumn }" />
                         <input name="sequence" type="hidden" value="${sequence }" />
	                     <script type="text/javascript">
	                         currentPath = "admin/stats/cpc";
	                         formId="overviewForm";
	                     </script>
	                     <%@ include file="/jsp/advertiser/campaigns/CommSearch.jsp"%>
	                     <div class="clear"></div>
	                 </form>  
	            </div> 
	    </div>  
		  <div class="overviewPanel details" style="border-top:0px;">
            <div class="panelTitle">收入排名</div>
          </div>  
        <div class="clear"></div>
        <div class="overview" style="border-top:0px;" >
				<div class="clear spacer10"></div> 
		        <table class="bTable textRight">
                    <tr class="heading">
                        <th style="text-align:left;">广告活动</th>
                        <th style="text-align:left;">广告主</th>
                        <th class="textRight">
                            <s:if test="#request.sortColumn eq 'views'">
                                <a class="sort" href="javascript:sortOperate('views', <s:if test='#request.sequence eq "ASC"'>'DESC'</s:if><s:else>'ASC'</s:else>)">
                                                                                                                原始展示量<span class="<s:if test='#request.sequence eq "ASC"'>sort_up_select</s:if><s:else>sort_down_select</s:else>"></span></a>
                            </s:if>
                            <s:else>
                                <a class="sort" href="javascript:sortOperate('views', 'DESC')">原始展示量<span class="sort_down"></span></a>
                            </s:else>
                        </th>
                        <th class="textRight">
                            <s:if test="#request.sortColumn eq 'clicks'">
                                <a class="sort" href="javascript:sortOperate('clicks', <s:if test='#request.sequence eq "ASC"'>'DESC'</s:if><s:else>'ASC'</s:else>)">
                                                                                                            原始点击量<span class="<s:if test='#request.sequence eq "ASC"'>sort_up_select</s:if><s:else>sort_down_select</s:else>"></span></a>
                            </s:if>
                            <s:else>
                                <a class="sort" href="javascript:sortOperate('clicks', 'DESC')">原始点击量<span class="sort_down"></span></a>
                            </s:else>                
                        </th>
                        <th class="textRight">
                            <s:if test="#request.sortColumn eq 'cpcClicks'">
                                <a class="sort" href="javascript:sortOperate('cpcClicks', <s:if test='#request.sequence eq "ASC"'>'DESC'</s:if><s:else>'ASC'</s:else>)">
                                                                                                            有效点击量<span class="<s:if test='#request.sequence eq "ASC"'>sort_up_select</s:if><s:else>sort_down_select</s:else>"></span></a>
                            </s:if>
                            <s:else>
                                <a class="sort" href="javascript:sortOperate('cpcClicks', 'DESC')">有效点击量<span class="sort_down"></span></a>
                            </s:else>                
                        </th>
                        <th class="textRight">有效CTR</th>
                        <th style="text-align:center">
                            <s:if test="#request.sortColumn eq 'cpcCommission'">
                                <a class="sort" href="javascript:sortOperate('cpcCommission', <s:if test='#request.sequence eq "ASC"'>'DESC'</s:if><s:else>'ASC'</s:else>)">
                                                                                                       收入<span class="<s:if test='#request.sequence eq "ASC"'>sort_up_select</s:if><s:else>sort_down_select</s:else>"></span></a>
                            </s:if>
                            <s:elseif test="#request.sortColumn eq ''|| #request.sortColumn == null ">
                                <a class="sort" href="javascript:sortOperate('cpcCommission', 'ASC')">收入<span class="sort_down_select"></span></a>
                            </s:elseif>
                            <s:else>
                                <a class="sort" href="javascript:sortOperate('cpcCommission', 'DESC')">收入<span class="sort_down"></span></a>
                            </s:else>
                        </th>
                    </tr>
                    <s:if test="#request['stats'].size() > 0">
                            <s:iterator value="#request['stats']" var="stat" status="status" >
                                <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
                                    <td style="text-align:left;"><a class="bLink orange" href="${cxt_path }/admin/campaign/${campaignId}/detail?leftmenu=leftmenu-admin-stats-cpc">${stat.campName}</a></td>
                                      <td style="text-align:left;">${stat.advName}</td> 
                                    <td><s:text name="global.format.number"><s:param value="%{#stat.views}"/></s:text></td>
                                    <td><s:text name="global.format.number"><s:param value="%{#stat.clicks}"/></s:text></td>
                                    <td><s:text name="global.format.number"><s:param value="%{#stat.cpcClickNo}"/></s:text></td>
                                    <td>${stat.cpcCTR}</td>
                                    <td style="text-align:center">¥${stat.cpcTotalCommissionDouble}</td>
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
                <form action="${cxt_path}/admin/overview" id="filterSearchForm" method="post">
                    <div id="pagination" class="right pagination"></div>
                    <input type="hidden" value="${dateStart}" name="dateStart" />
                    <input type="hidden" value="${dateEnd}" name="dateEnd"/>
                    <input type="hidden" value="${advId}" name="advId" />
                    <input type="hidden" value="${campaignId}" name="campaignId" />
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
	    var form = $("#overviewForm")[0];
	    $("input[name='sortColumn']").each(function() {$(this).val(sortColumn);});
	    $("input[name='sequence']").each(function() {$(this).val(sequence);});
	    form.submit();
	}

$(function() {
	//submit overviewForm form
	$("#advId").change(function(){
			$("#campaignId").val(0);
			$("#overviewForm").submit();
	});
	$("#campaignId").change(function(){
		$("#overviewForm").submit();
	});
	
	//submit form
	$("#wsel").change(function(){
		$("#statsForm").submit();
	});
    //menu active
    $("#topmenu-admin-home").addClass("active");
    $("#leftmenu-admin-stats-cpc").addClass("active");
    
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
    //initPage("${page.pageNum}","${page.pageSize}", "${page.totalRecords}");
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