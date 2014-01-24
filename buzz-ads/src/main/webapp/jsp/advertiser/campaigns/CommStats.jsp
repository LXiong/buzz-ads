<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>


<div class="overviewPanel details" style="border-top:0px;">
    <div class="inlineBlock panelTitle">效果趋势</div>
</div>
<div class="overview" style="border-top:0px">
    <form action="" name="availableStatsForm" id="availableStatsForm" method="post">
                <s:select list="viewTypeMap" value="availableStats" listKey="key" listValue="value" name="availableStats"  cssClass="bTextbox">
                </s:select>
                <input type="hidden" value="${dateStart}" name="dateStart" />
                <input type="hidden" value="${dateEnd }" name="dateEnd"/>
     </form>
     <div style="margin-bottom:30px;position:relative">
           <div class="clear spacer20"></div> 
           <table id="divChartShares" style="table-layout: fixed; width: 94%; display: none;">
               ${jsonDataStats}
           </table>
           <div id="websiteChartShares"></div>
     </div>
</div>

<link rel="stylesheet" href="${css_path}/visualize.css" />
<!--[if IE]><script type="text/javascript" src="${js_path}/libs/excanvas.js"></script><![endif]-->
<script type="text/javascript" src="${js_path}/libs/jquery.visualize.js"></script>
<script type="text/javascript" src="${js_path}/libs/jquery.visualize.tooltip.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.forms.min.1.2.6.js"></script>
<script type="text/javascript">
    $(function(){
        //switch stats
        $("#availableStats").change(function(){
            $("#availableStatsForm").submit();
        });
        var symbol="";
        var units="";
        var selectType=$("#availableStats").val();
        switch(selectType){
            case "1":
                symbol="原始展示量：";  
                units="";
                break;
            case "2":
                symbol="原始点击量：";  
                units="";
                break;  
            case "3":
                symbol="有效点击量：";
                units="";
                break; 
            case "4":
                symbol="有效展示量：";
                units="";
                break; 
            case "5":
                symbol="有效点击率：";  
                units="%";
                break;  
            case "6":
                symbol="支出：¥";
                units="";
                break;  
        }
        
        window.setTimeout(function(){
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
               isRound:true,
               symbol:symbol,
               unit:units
            });
        }, 0);
    });
</script>