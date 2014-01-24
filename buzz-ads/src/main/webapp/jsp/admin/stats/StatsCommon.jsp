<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<div class="overviewPanel shadow">
	<div class="right dataSearch">
            <form action="" method="post" id="filterSearchForm" name="filterSearchForm">
             <input class="bTextbox date left" type="date" id="datePickerStart" name="dateStart" value="${dateStart}" data-value="${dateStart}" max="${dateEnd}">
             <div class="left" style="padding: 0px 10px; line-height: 24px; height: 24px;">-</div>
             <input class="bTextbox date left" type="date" data-orig-type="date" id="datePickerEnd" name="dateEnd" value="${dateEnd}" data-value="${dateEnd}" max="0">
             <input class="bButton bButton24 left mSpacer_left10" type="submit"  value="查询" />
            </form>
        </div>
</div>
<div class="overviewPanel details" style="border-top:0px;">
 	<div class="panelTitle">数据汇总</div>
</div>
<div class="overview" style="border-top:0px;">
 	<table class="bTableInfo" style="width:100%;" cellpadding="0px" cellspacing="0px">
        <tbody>
            <tr>
                <td class="panelTitle">实际收入</td>
                <td class="panelTitle" >预计收入</td>
                <td class="panelTitle last">预计支出</td>
            </tr>
            <tr>
                <td class="panelCon" style="with:25%;"><font class="unit">¥</font> ${sum.cpsTotalConfirmedCommissionDouble}</td>
                <td class="panelCon" style="with:25%;"><font class="unit">¥</font> ${sum.totalCommissionDouble}</td>
               <td class="panelCon last" style="with:25%;"><font class="unit">¥</font> ${sum.pubCommissionDouble}</td>
            </tr>
        </tbody>
   	</table>
</div>
<script type="text/javascript">
//${cxt_path}/admin/stats
	$("#filterSearchForm").attr("action",window.location);
	
</script>