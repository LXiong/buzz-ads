<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>创建一个新的广告组</title>

<style>
    .infoWrap { background: #f8f8f8; padding: 5px; }
    .viewTable2 tr td { padding: 5px 4px; }
</style>

<div class="container-body">
	<div class="spacer20"></div>
	<div>
		<%@ include file="/jsp/advertiser/campaigns/LeftMenu.jsp"%>
		<div class="container-right" style="overflow: auto;">
			<div class="overview">
				<div style="width:980px" class="navCon">
					<table class="navTable" style="width:100%" cellpadding="0" cellspacing="0">
						<tr>
						    <td><span><a href="${cxt_path}/advertiser/campaign/new">新建广告活动</a></span></td>
                            <td><span>新建广告组</span></td>
                            <td><span><a href="${cxt_path}/advertiser/entry/new">新建广告</a></span></td>
						</tr>
						<tr>
							<td><div class="spacer10"></div><div class="navBg"></div></td>
							<td><div class="spacer10"></div><div class="navBg"><div class="naPointer"></div></div></td>
							<td><div class="spacer10"></div><div class="navBg"></div></td>
						</tr>
					</table>
					<div class="spacer20"></div>
					
					<div class="infoWrap">
						<table class="viewTable viewTable2">
	                        <tr>
	                            <td style="width:100px;"><span>选择广告活动</span></td>
	                            <td>
	                                <s:select list="campaigns" listKey="id" listValue="name" value="campaign.id" name="campaingsList" cssClass="bTextbox" cssStyle="width: 250px;">
	                                </s:select>
	                            </td>
	                        </tr>
	                        <tr><td colspan="2" style="padding-top: 0;"><a id="campaignSet" class="packer orange"href="javascript:void(0)">显示广告活动设置<i></i></a></td></tr>
	                        <tr><td colspan="2">
		                        <div id="campaignCon" class="label_date small" style="display:none">
		                            <table class="viewTable">
		                                <tr >
		                                    <td>投放网络</td>
		                                    <td id="camp_network" class="orange">
		                                        <s:iterator value="#request.campaign.network" var="ntwk" status="status">
		                                            <%-- <s:if test="#ntwk == @com.buzzinate.buzzads.enums.AdNetworkEnum@LEZHI">
		                                                                                                                                                  乐知
		                                            </s:if>
		                                            <s:elseif test="#ntwk == @com.buzzinate.buzzads.enums.AdNetworkEnum@BSHARE">
		                                                bShare
		                                            </s:elseif>
		                                            <s:elseif test="#ntwk == @com.buzzinate.buzzads.enums.AdNetworkEnum@BUZZADS">
		                                                BuzzAds
		                                            </s:elseif>
		                                            <s:else>
		                                                ??
		                                            </s:else> --%>
		                                            ${ntwk.text }
		                                            <s:if test="!#status.last">,&nbsp;</s:if>
		                                        </s:iterator>
		                                    </td>
		                                    
		                                </tr>
		                                <tr>
		                                    <td>出价类型</td>
		                                    <td id="camp_bidType" class="orange">${campaign.bidType }</td>
		                                </tr>
		                                <tr>
		                                    <td>投放预算</td>
		                                    <td class="orange">
		                                        <span>日预算：
		                                        <s:if test="campaign.maxDayBudgetDouble == '0.00'">
		                                        <font  id="cDayBudget">无限制</font>
		                                        </s:if>
		                                        <s:else>
		                                        <font class="orange" id="cDayBudget">¥${campaign.maxDayBudgetDouble }</font>
		                                        </s:else>
		                                        </span> , <span>总预算：
		                                        <s:if test="campaign.maxTotalBudgetDouble == '0.00' ">
		                                        <font  id="cTotalBudget">无限制</font>
		                                        </s:if>
		                                        <s:else>
		                                        <font class="orange" id="cTotalBudget">¥${campaign.maxTotalBudgetDouble}</font>
		                                        </s:else>
		                                        </span> 
		                                        <s:iterator value="campaign.dayBudgets" var="cd" status="status">
						                       		<div class="clear spacer10"></div>
						                       		<div>
						                       			<span>特定日期：<font class="yellowH3"><s:date name="#cd.budgetDay" format="yyyy-MM-dd"  /></font></span>
						                       			<span>预算：<font class="yellowH3">¥${cd.budgetDouble}</font></span>
						                       		</div>
						                   		</s:iterator>
		                                    </td>
		                                </tr>
		                                <tr>
		                                    <td>投放时间</td>
		                                    <td class="orange">
		                                        <span id="camp_start"><s:date name="campaign.startDate" format="yyyy-MM-dd"/> </span>
		                                        <span id="camp_end">
		                                            - <s:if test="campaign.endDate != null ">  <s:date name="campaign.endDate" format="yyyy-MM-dd"/></s:if> 
		                                            <s:else>
		                                                <font >无截止日期</font>
		                                            </s:else>
		                                        </span>
		                                    </td>
		                                </tr>
		                                <tr>
		                                    <td>地理位置</td>
		                                    <td class="orange">
		                                         <s:if test="campaign.viewLocation !='' ">
		                                            <span class="orange" id="cLocations">${campaign.viewLocation}</span>
		                                        </s:if>
		                                         <s:else>
		                                            <font >无限制</font>
		                                        </s:else>
		                                    </td>
		                                </tr>
		                        </table>
		                        </div>
	                        
	                        </td></tr>
	                    </table>
					</div>
					
					<div class="spacer15"></div>
					<table class="viewTable" style="width:100%">
		                    <tr>
		                        <td style="width:100px;">广告组名称</td>
		                        <td><input class="validator bTextbox" required="required" maxlength="50" type="text" id="name" value="" placeholder="不能超过50个字符"/><span class="primary mSpacer_left10" id="name_msg"></span></td>
		                    </tr>
		                    
		                     
		                    <tr>
		                        <td>默认出价</td>
		                        <td>
			                        <div class="">
			                        	¥ <input class="validator bTextbox" required="required" type="Number" step="0.01" min="0.01" id="bid" value="0.29" style="width:70px;" />
			                        	<span id="displayBidType" style=" margin: 0px 50px 0px 0px;">
		                                    <s:if test="#request.campaign.bidType == (@com.buzzinate.buzzads.enums.BidTypeEnum@CPC)">
		                                        CPC
		                                    </s:if><s:else>
		                                        CPM
		                                    </s:else>
		                                </span>
			                        	<span class="primary mSpacer_left10" id="bid_msg"></span>
			                        </div>
		                        </td>
		                    </tr>
		                     <tr>
			                	<td colspan="2">
                                    <div class="spacer15" style="border-top: 1px dashed #dadada;"></div>
			                		<span class="heading4 orange">投放策略</span>
			                	</td>
			                </tr>
		                     <tr>
		                        <td  style="vertical-align:top">投放时间</td>
		                        <td style="padding-bottom:0;">
		                        	<div>
			                    		<span id="startDate"><s:date name="campaign.startDate" format="yyyy-MM-dd"/></span> - <span id="endDate">无截止日期</span> <a id="dateEdit" 
			                    		   class="packer orange spacer_left15" href="javascript:void(0)">修改<i></i></a>
			                    	</div>
			                    	<div class="" style="display:none;" id="dateCon">
			                    		<div class="spacer10"></div>	
			                        	<div class="dataSearch label_date">
			                        	   	<div style="overflow:hidden;">
			                        	   		<input class="bTextbox date left" type="date" id="datePickerStart" name="dateStart" value='<s:date name="campaign.startDate" format="yyyy-MM-dd"/>' data-value="<s:date name="campaign.startDate" format="yyyy-MM-dd"/>" style="margin:0px;">
						                        <div class="left" style="padding:0px 10px;line-height:32px;height:32px;">-</div>
						                        <input class="bTextbox date left" type="date" data-orig-type="date" id="datePickerEnd" name="dateEnd" value='${dateStart}' data-value="${dateStart}" >
						                        <span class="primary mSpacer_left10" id="date_msg"></span>
			                        	   	</div>
					                       <div>
					                      	  <div class="spacer10"></div>
					                       	  <label><input type="checkbox" id="now"  /><span class="mSpacer_left10" style="display:inline-block;">广告活动开始日期</span></label>
						                	  <span style="display:inline-block;width:20px;"></span>
						                      <label><input type="checkbox" id="noEndDate"  /><span class="mSpacer_left10" style="display:inline-block;">无截止日期</span></label>
						                  </div>
						                </div>
			                        </div>
		                        </td>
		                    </tr>
		                     <tr>
		                        <td>投放计划</td>
		                        <td>
		                        	<div>
		                        		<span id="showDays">全天候展示</span> &nbsp;&nbsp;
		                        		<span>时间：</span><span id="showTime">24 小时</span> 
		                        	    <a id="timeEdit" class="packer orange spacer_left15" href="javascript:void(0)">修改<i></i></a>
		                        	</div>
		                        	<div id="weeklyCon" style="display:none;">
		                        		<div class="spacer10"></div>
			                        	<div class="label_date"  >
			                        		<div id="chooseType">
				                                <label><input type="radio" id="week" style="margin:0px 10px 0px 0px;" name="group1" checked="true" alt="0" />全天候展示</label>
				                                <span style="display:inline-block;width:10px;"></span>
				                                <label><input type="radio" id="specificDays" style="margin:0px 10px 0px 0px;" name="group1" alt="1" />特定天</label>
				                                 <span class="primary mSpacer_left10" id="schedule_msg"></span>
				                            </div>
				                            
			                        		<div class="weekly_item" id="weekly" style="display:none;" >
			                        		    <div class="clear spacer10"></div>  
			                        			<label><input type="checkbox" alt="0"/>每周一</label>
				                        		<label><input type="checkbox" alt="1"/>每周二</label>
				                        		<label><input type="checkbox" alt="2"/>每周三</label>
				                        		<label><input type="checkbox" alt="3"/>每周四</label>
				                        		<label><input type="checkbox" alt="4"/>每周五</label>
				                        		<label><input type="checkbox" alt="5"/>每周六</label>
				                        		<label><input type="checkbox" alt="6"/>每周日</label>
			                        		</div>
			                        		
			                        		<div class="clear spacer15"></div>  
			                        		<div id="timeType">
				                        		<label><input type="radio" id="allhours" style="margin:0px 10px 0px 0px;" name="group2" checked="checked" alt="0" />24小时</label>
				                        		<span style="display:inline-block;width:6px;"></span>
				                        		<label><input type="radio" id="specificTimes" style="margin:0px 10px 0px 0px;" name="group2" alt="1" />选择特定的时间</label>
				                        		<span class="primary mSpacer_left10" id="timeType_msg"></span>
				                        	</div>
				                        	<div class="dataSearch" id="time" style="display:none;">
				                        		<div class="spacer10"></div>
				                        		<div id="startTime" class="inlineBlock"></div><span style="vertical-align: top;line-height: 30px; padding: 0px 10px;"> 至   </span> <div id="endTime" class="inlineBlock"></div>
				                        	</div>
			                        	</div>
		                        	</div>
		                        	
		                        </td>
		                   </tr>
		                   <tr id="putInTypeCon">
		                   		<td>投放方式 <span title="<font style='color:#f60;font-weight:bold;'>投放方式：</font>投放方式决定了在预算有限的情况下，预算在一天之中的分配方式。" class="help-popup-img"></span></td>
		                   		<td>
		                   			<label><input style="margin:0px;" type="radio" alt="0" id="equality" name="putInWay" 
		                                  checked="true"
		                                /><span class="mSpacer_left10">均匀投放</span></label>
		                                <span title="<font style='color:#f60;font-weight:bold;'>均匀投放：</font>预算有限的情况下在一整天内均匀地分配预算和覆盖受众群体。使用此选项可避免在一天之中较早消耗预算。" class="help-popup-img"></span>
		                                &nbsp;&nbsp;
		                                <label>
		                                 
		                                <input style="margin:0px;" type="radio" alt="1" id="quick" name="putInWay"
		                                    
		                                /><span class="mSpacer_left10">加速投放</span></label>
		                                <span title="<font style='color:#f60;font-weight:bold;'>加速投放：</font>在合理的范围内尽快展示广告，但可能会导致在当天晚些时候广告无法展示，而直到第二天才能开始展示。" class="help-popup-img"></span>
		                                <span class="primary mSpacer_left10" id="putInWay_msg"></span>
		                   		</td>
		                   </tr>
		                    <tr>
		                   		<td>频次上限 <span title="<font style='color:#f60;font-weight:bold;'>频次上限 ：</font>频次上限用于限制广告向单个用户展示的次数。" class="help-popup-img"></span></td>
		                   		<td>
			                        <div class="">
			                        	<input class="validator bTextbox" required="required" type="Number" step="1" min="1" id="rate" value="10" disabled="disabled" style="width:70px;" />
			                        	<select name="" class="bTextbox" id="rateSelect" style="margin-left:15px;" disabled="disabled">
			                        		<option value="1">每广告组</option>
			                        		<option value="2">每广告</option>
			                        	</select>
			                        	<label class="spacer_left15"><input type="checkbox" id="noRate" checked="true" ><span class="mSpacer_left10" style="display:inline-block;">无限制</span></label>
			                        	<span class="primary mSpacer_left10" id="rate_msg"></span>
			                        </div>
		                        </td>
		                   </tr>
                            <tr>
                                <td>关键词</td>
                                <td>
                                    <input class="validator bTextbox" required="required" maxlength="50" type="text" id="keywords" value="" placeholder='例如：车,运动   多个关键词用英文逗号","分隔' />
                                    <span style="display:inline-block;width:39px;"></span>
                                    <span class="primary mSpacer_left10" id="keywords_msg"></span>
                                </td>
                            </tr>
                            <tr>
                                <td style="vertical-align: top;"><div class="spacer10"></div>受众群体
                                </td>
                                <td>
	                                <div class="spacer10"></div>
	                                <%@ include file="/jsp/advertiser/campaigns/group/CommTargeting.jsp"%>
                                </td>
                            </tr>
                            <s:if test="ChannelTargetOpen == true">
	                            <tr>
	                                <td style="vertical-align: top;"><div class="spacer10"></div>媒体定向</td>
	                                <td>
		                                <div class="spacer10"></div>
		                                <%@ include file="/jsp/advertiser/campaigns/group/ChannelTargeting.jsp"%>
	                                </td>
	                            </tr>
                            </s:if>
		                    <tr>
		                        <td></td>
		                        <td>
                                <div class="spacer15"></div>
		                        <input id="submitSave" class="bButton center lightOrange" type="button" value="保存并创建广告" />
		                        <input id="submit" class="bButton center" style="margin-left:20px;" type="button" value="保存" />
		                        <input class="bButton center" style="margin-left:20px;" type="button" value="取消" onclick="document.location='${cxt_path}/advertiser/campaign/${campaign.id}/groups';" /></td>
		                    </tr>
		                </table>
				</div>
                <div class="clear spacer50"></div>
			</div>
		</div>
		<div class="clear spacer20"></div>
		
	</div>
</div>
<link rel="stylesheet" href="${css_path}/calendar-date.css" type="text/css" />
<link rel="stylesheet" href="${css_path}/timeSelect.css" type="text/css" />
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.forms.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/timeSelect.js"></script>
<script type="text/javascript">
    function initDatePickers() {
    	if("${campaign.startDate}"){
            $("#datePickerStart").removeAttr("disabled");
            $("#now").removeAttr("checked");
            
            $("#camp_start").html('<s:date name="campaign.startDate" format="yyyy-MM-dd"/>');
            $("#datePickerStart").data("dateinput").setValue('<s:date name="campaign.startDate" format="yyyy-MM-dd"/>');
        } else {
            var d=new Date();
            var date=d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
            $("#datePickerStart").data("dateinput").setValue(date);
            $("#datePickerStart")[0].disabled="true";
            $("#now").attr('checked','checked');
        }
        
        if("${campaign.endDate}"){
            $("#datePickerEnd").removeAttr("disabled");
            $("#noEndDate").removeAttr("checked");
            
            $("#camp_end").html(' - <s:date name="campaign.endDate" format="yyyy-MM-dd"/>');
            $("#datePickerEnd").data("dateinput").setValue('<s:date name="campaign.endDate" format="yyyy-MM-dd"/>');
        } else {
            $("#datePickerEnd").val("");
            $("#datePickerStart").data("dateinput").setMax("", true);
            $("#datePickerEnd")[0].disabled="true";
            $("#endDate").html("无截止日期");
            $("#noEndDate").attr('checked','checked');
            
            $("#camp_end").html(" - <font>无截止日期</font>");
        }
    }
    
	$(function(){
		var dataTemp={}, curCamp = {};
		$(".packer").toggle(function () {
            var i = $(this).find("i");
            i.addClass(" selected");
        },function(){
        	var i = $(this).find("i");
        	i.removeClass(" selected");
        });
        
		// show campaign info
		$("#campaignSet").toggle(function(){$("#campaignCon").show();},function(){$("#campaignCon").hide();} );
		//date edit
		$("#dateEdit").toggle(function(){$("#dateCon").show();},function(){$("#dateCon").hide();});
		//time edit
		$("#timeEdit").toggle(function(){$("#weeklyCon").show();},function(){$("#weeklyCon").hide();});
		
		//add mul function for float bug
		Number.prototype.mul = function (arg){
			var m=0,s1=arg.toString(),s2=this.toString();
		    try{m+=s1.split(".")[1].length;}catch(e){}
		    try{m+=s2.split(".")[1].length;}catch(e){}
		    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
		}
		
		//create startTime
		var startTime=new bshare.timeSelect({container:$("#startTime"),period:"AM",afterFun:function(){getHours();}});
		var endTime=new bshare.timeSelect({container:$("#endTime"),period:"PM",afterFun:function(){getHours();}});
	
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
                $(":date:first").data("dateinput").setMin(RC.getDate(), true);
                $(":date:last").data("dateinput").setMin(this.getValue(), false);
                $("#startDate").html($("#datePickerStart").val());
            });
            $(":date:last").data("dateinput").change(function() {
                // we use it's value for the first input max option
                $(":date:first").data("dateinput").setMax(this.getValue(), true);
	            $("#endDate").html($("#datePickerEnd").val());
            });
        }
		//choose campaign
		$("#topmenu-advertiser-adCampaign").addClass("active");
		$("#leftmenu-advertiser-quicklyCreate").addClass("active");
		function getDate(){
			var d=new Date();
			var date=d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
			$("#datePickerStart").data("dateinput").setValue(date);
		}
		
		function init(){
			getDate();
			RC.CutSelect("campaingsList");
			if("${adOrder.endDateStr }" == ""){
				$("#noEndDate").attr("checked","true");
				$("#datePickerEnd").val("");
				$("#datePickerEnd")[0].disabled="true";
			}
			if("${campaign.maxDayBudgetDouble }"== '0.00'){
				$("#putInTypeCon").hide();
				dataTemp.isPutInWay = true;
			}else{
				$("#putInTypeCon").show();
			}
		}
		init();
		//
		var item=$("#chooseType").find("input:radio");
		item.click(function(){
			if($(this).attr("alt")==0){
				$("#weekly").hide();
			}else if($(this).attr("alt")==1){
				$("#weekly").show();
			}
		});
		var times=$('#timeType').find("input:radio");
		times.click(function(){
			if($(this).attr("alt")==0){
				$("#time").hide();
			}else if($(this).attr("alt")==1){
				$("#time").show();
			}
		});
		
		$("#max_nolimit").click(function(){
			if(($(this)[0]).checked){
				$("#max_validator").attr("disabled","true");
			}else{
				$("#max_validator").removeAttr("disabled");
			}
		});
		
		//date click
		$("#now").click(function(){
			if($(this)[0].checked){
				if(curCamp.startDateStr == ""){
					var d=new Date();
					date=d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
				} else {
					date = new Date(RC.dateToSprit(curCamp.startDateStr));
				}
				$("#datePickerStart").data("dateinput").setValue(date);
                $("#datePickerStart")[0].disabled="true";
			}else{
				$("#datePickerStart").removeAttr("disabled");
			}
		});
		$("#noEndDate").click(function(){
			if($(this)[0].checked){ 
				$("#datePickerEnd").val("");
				$("#datePickerStart").data("dateinput").setMax("", true);
				$("#datePickerEnd")[0].disabled="true";
				$("#endDate").html("无截止日期");
			}else{
				$("#datePickerEnd").removeAttr("disabled");
				if(curCamp.endDateStr == ""){
                    date=new Date(RC.dateToSprit($("#datePickerStart").val()));
                    date.setMonth(date.getMonth( ) + 1);// default here to 1 month ahead
                } else {
                    date = new Date(RC.dateToSprit(curCamp.endDateStr));
                }
                $("#datePickerEnd").data("dateinput").setValue(date);
			}
		});
		
		// 出价类型改变时候动作
       /* var $bidType = $("input[name='bidType']:radio");
        $bidType.bind("click", function() {
        	$bidType.each(function() {
        		if ($(this).attr('checked')) {
                    $('#displayBidType').html($(this).next().html());
                }
        	});
        });*/
		
    	
    	curCamp.id=${campaign.id};
    	curCamp.network="${campaign.network}";
    	curCamp.maxDayBudgetDouble="${campaign.maxDayBudgetDouble}";
    	curCamp.maxTotalBudgetDouble="${campaign.maxTotalBudgetDouble}";
    	curCamp.viewLocation="${campaign.viewLocation}";
    	curCamp.startDateStr="${campaign.startDateStr}";
    	curCamp.endDateStr="${campaign.endDateStr}";
    	dataTemp.campaignId=${campaign.id};
    	
    	//change campaingsList 
        $("#campaingsList").change(function(){
            $.ajax({
                type: "post",
                url: "${cxt_path}/advertiser/campaign/"+$("#campaingsList").val()+"/get",
                dataType:"json",
                success: function (data) {
                    var camp=data.contents.campaign;
                    curCamp = camp;
                    
                    var ntkHtml = "";
                    for(var i=0; i<camp.network.length; i++) {
                    	var ntk = camp.network[i];
                    	ntkHtml += ",";
	                    if (ntk == '<%=com.buzzinate.buzzads.enums.AdNetworkEnum.LEZHI%>') {
	                    	ntkHtml += "乐知";
	                    }
	                    else if (ntk == '<%=com.buzzinate.buzzads.enums.AdNetworkEnum.BSHARE%>') {
                            ntkHtml += "bShare";
                        }
	                    else if (ntk == '<%=com.buzzinate.buzzads.enums.AdNetworkEnum.BUZZADS%>') {
                            ntkHtml += "BuzzAds";
                        }
	                    else if (ntk == '<%=com.buzzinate.buzzads.enums.AdNetworkEnum.MULTIMEDIA%>') {
                            ntkHtml += "富媒体";
                        } 
	                    else {
	                    	ntkHtml += "??";
	                    }
                    }
                    if (camp.maxTotalBudgetDouble == "0.00") {
                    	$("#cTotalBudget").html("无限制").css("color", "red");
                    } else {
                        $("#cTotalBudget").html(camp.maxTotalBudgetDouble).addClass("orange");
                    }
                    if (camp.maxDayBudgetDouble == "0.00") {
                        $("#cDayBudget").html("无限制").css("color", "red");
                        $("#putInTypeCon").hide();
                    } else {
                        $("#cDayBudget").html(camp.maxDayBudgetDouble).addClass("orange");
                        $("#putInTypeCon").show();
                    }
                    
                    $("#camp_network").html(ntkHtml.length > 0 ? ntkHtml.substr(1) : ntkHtml);
                    if (camp.viewLocation == "") {
                    	$("#cLocations").html("无限制").css("color", "red");
                    } else {
                        $("#cLocations").html(camp.viewLocation).addClass("orange");
                    }
                    
                    $("#camp_bidType").html(camp.bidType);
                    $("#displayBidType").html(camp.bidType);
                    // 在campaign改变时也改变下面的类型
                    var $bidType=$("input[name='bidType']:radio");
                    $bidType.each(function(idx) {
                    	var bidTypeVal = $(this).attr('alt');
                    	if(camp.bidType == 'CPM' &&  bidTypeVal == '0') {
                    		 $(this).attr('checked', true);
                    		 $('#displayBidType').html('CPM');
                    	} else if(camp.bidType == 'CPC' &&  bidTypeVal == '3') {
                             $(this).attr('checked', true);
                             $('#displayBidType').html('CPC');
                        }
                    });
                    
                    if(camp.startDate){
                    	$("#datePickerStart").removeAttr("disabled");
                    	$("#now").removeAttr("checked");
                        $("#camp_start").html(camp.startDateStr);
                        var dates = new Date(RC.dateToSprit(camp.startDateStr));
                        $("#datePickerStart").data("dateinput").setValue(dates);
                    } else {
                        var d=new Date();
                        var date=d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
                        $("#datePickerStart").data("dateinput").setValue(date);
                        $("#datePickerStart")[0].disabled="true";
                        $("#now").attr('checked','checked');
                    }
                    
                    if(camp.endDate){
                    	$("#datePickerEnd").removeAttr("disabled");
                    	$("#noEndDate").removeAttr("checked");
                    	 var datee = new Date(RC.dateToSprit(camp.endDateStr));
                        $("#camp_end").html(" - "+camp.endDateStr);
                       $("#datePickerEnd").data("dateinput").setValue(datee);
                    } else {
                    	$("#datePickerEnd").val("");
                        $("#datePickerStart").data("dateinput").setMax("", true);
                        $("#datePickerEnd")[0].disabled="true";
                        $("#endDate").html("无截止日期");
                        $("#noEndDate").attr('checked','checked');
                        
                    	$("#camp_end").html(" - <font>无截止日期</font>");
                    }
                    
                    dataTemp.campaignId=$("#campaingsList").val();
                   
                },
                error: function (msg) {
                    displayStatusMessage("提交错误", "error");   
                }
            });
        });
        
    	//getName
    	function getName(){
    		if($("#name").val()!="" && $("#name").val().length <= 50){
				dataTemp.name=RC.stripscript($.trim($("#name").val()));
				$("#name_msg").html("");
				dataTemp.isName=true;
			}else if($("#name").val()==""){
				$("#name_msg").html("请输入广告组名称");
				dataTemp.isName=false;
			}else if($("#name").val().length > 50){
				$("#name_msg").html("名称不能大于50个字");
				dataTemp.isName=false;
			}
    		
    		
    	}
    	
    	function getNetwork(){
    		var network=$("#network");
    		if(network[0].checked){
				dataTemp.netWorkStr=network.attr("alt");
				$("#network_msg").html("");
				dataTemp.isNetWork=true;
			}else{
				$("#network_msg").html("请选择投放网络");
				dataTemp.isNetWork=false;
			}
    	}
    	function getBidType(){
    		var $bidType=$("input[id='bidType']:radio:checked");
            if($bidType.size() > 0){
                dataTemp.bidType=$bidType.first().attr('alt');
                $("#bidType_msg").html("");
                dataTemp.isBidType = true;
            }else{
                $("#bidType_msg").html("请选择出价类型");
                dataTemp.isBidType=false;
            }
    	}
    	function getPutInWay(){
			if("${campaign.maxDayBudgetDouble }"!= '0.00'){
				var $putInWay=$("input[name='putInWay']:radio:checked");
	    		if($putInWay.size() > 0){
	                dataTemp.adsType=$putInWay.first().attr('alt');
	                $("#putInWay_msg").html("");
	                dataTemp.isPutInWay = true;
	            }else{
	                $("#putInWay_msg").html("请选择投放计划");
	                dataTemp.isPutInWay=false;
	            }
			}else{
				dataTemp.isPutInWay = true;
			}
    	}
    	$("#noRate").click(function(){
    		if($(this)[0].checked){
				$("#rate")[0].disabled="true";
				$("#rateSelect")[0].disabled="true";
			}else{
				$("#rate").removeAttr("disabled");
				$("#rateSelect").removeAttr("disabled");
			}
    	});
    	$("#rate").blur(function(){
    		getRate();
    	});
    	function getRate(){
    		var tempc=$("#noRate")[0].checked;
    		if(tempc){
    			$("#rate_msg").html("");
    			dataTemp.frequencyCap="";
    			dataTemp.rateSelect="";
    			dataTemp.isFrequencyCap=true;
    		}else{
    			var tempr=$("#rate").val();
    			if(RC.numInteger(tempr)){
    				$("#rate_msg").html("");
    				dataTemp.frequencyCap=tempr;
    				dataTemp.rateSelect=$('#rateSelect').val();
    				dataTemp.isFrequencyCap=true;
    			}else{
    				$("#rate_msg").html("请输入合法数字,非负整数！");
    				dataTemp.isFrequencyCap=false;
				}
    			
    		}
    	}
    	
    	function getStart (){
    		if($("#datePickerStart").val()!=""){
				dataTemp.startDate=$("#datePickerStart").val();
				$("#date_msg").html("");
				dataTemp.isStart=true;
			}else{
				dataTemp.start=$("#datePickerStart").val();
				$("#date_msg").html("请选择开始时间");
				dataTemp.isStart=false;
			}
    	}
    	
    	 function getBid(){
             var bid=$("#bid").val();
             if(RC.numVerify(bid)){
                 bid=parseFloat(bid);
                 var bidNum=bid.mul(100);
               	if(bidNum!=0){
               	  $("#bid_msg").html("");
                   dataTemp.bidPrice=bidNum;
                   dataTemp.isBid=true;
                   return;
               	}
             }
             $("#bid_msg").html("请输入合法数字,非0，非负整数，小数[小数最多精确到小数点后两位]！");
             dataTemp.isBid=false;
         }

        function getAudienceCategories() {
            dataTemp.audienceCategories = $("#audienceCategories").val();
        }
        function getChannel() {
            dataTemp.channelIdStr = $("#channelIdStr").val();
        }
        
    	function getSpecificDays(){
			var days=$("#weekly").find("input:checkbox");
			var selected=[];
			$.each(days,function(i,n){
				if($(n)[0].checked){
					selected.push(i+1);
				}
			});
			return selected.join(",");
		}
    	function getWeekly(str){
        	var temp=str.split(",");
        	var items=[];
        	for(var i=0;i<temp.length;i++){
        		switch(temp[i]){
        		case '1':
        			items.push("周一");
        			break;
        		case '2':
        			items.push("周二");
        			break;
        		case '3':
        			items.push("周三");
        			break;
        		case '4':
        			items.push("周四");
        			break;
        		case '5':
        			items.push("周五");
        			break;
        		case '6':
        			items.push("周六");
        			break;
        		case '7':
        			items.push("周日");
        			break;
        		}
        	}
        	return items.join(",");
        }
    	function getWeek(){
			//week
			var week=$("#week")[0].checked;
			if(week){
				dataTemp.isWeek=true;
				$("#schedule_msg").html("");
				$("#showDays").html("全天候展示");
			}else{
					var chooseDays=getSpecificDays();
					if(chooseDays==""){
						$("#schedule_msg").html("请选择投放计划");
						dataTemp.isWeek=false;
					}else{
						$("#schedule_msg").html("");
						dataTemp.scheduleDayStr=chooseDays;
						$("#showDays").html(getWeekly(chooseDays));
						dataTemp.isWeek=true;
					}
			}
    	}
    	
    	$("#week").click(function(){
        	getWeek();
        })
        $("#specificDays").click(function(){
        	if (getSpecificDays() == "") {
        		// auto select first checkbox if none is selected...
        		$("#weekly").find("input[type=checkbox]:first").click();
        	}
        	getWeek();
        });
        $("#weekly").find("input:checkbox").click(function(){
        	getWeek();
        })
    	
    	function getHours(){
    		//hours
			var hours=$("#allhours")[0].checked;
			var specificTimes=$("#specificTimes")[0].checked;
			if(hours){
				dataTemp.isHours=true;
				$("#timeType_msg").hide().html("");
				$("#showTime").html("24小时");
			}else if(specificTimes){
				var start=startTime.getTime();
				var end=endTime.getTime();
				var startD=RC.hourToMin(start);
				var endD=RC.hourToMin(end);
				if(parseInt(startD) < parseInt(endD)){
					dataTemp.scheduleTimeStr=start+"-"+end;
					$("#showTime").html(start+" 至   " + end + "");
					$("#timeType_msg").hide().html("");
					dataTemp.isHours=true;
				}else{
					$("#timeType_msg").show().html("请正确时间");
					dataTemp.isHours=false;
				}
			}
    	}
    	
        $("#allhours").click(function(){
            getHours();
        });
        $("#specificTimes").click(function(){
            getHours();
        });
    	$("#name").blur(function(){
    		getName();
    	});
    	
    	$("#bid").blur(function(){
    		getBid();
    	})
    	
    	var isSubmitting = false;
    	function submit(isSave){
    		getName();
    		getStart();
    		getBid();
			dataTemp.endDate=$("#datePickerEnd").val();
			dataTemp.keywords=RC.trim($("#keywords").val());
			
			getPutInWay();
			getRate();
            getAudienceCategories();
            getChannel();
			getWeek();
			getHours();
			if(dataTemp.isBid && dataTemp.isStart && dataTemp.isFrequencyCap && dataTemp.isPutInWay && dataTemp.isName && dataTemp.isWeek && dataTemp.isHours ){
				if (isSubmitting) return;
                isSubmitting = true;
				$.ajax({
					type: "post",
					url: "${cxt_path}/advertiser/group/create",
					data:dataTemp,
					dataType:"json",
					success: function (data) {
						if(data.success) {
							displayStatusMessage("保存成功！", "success"); 
	                       	if(isSave){
		                       	 window.setTimeout(function(){
		                             window.location="${cxt_path}/advertiser/campaign/"+ $("#campaingsList").val() +"/groups";  
		                        }, 50);	
	                       	}else{
	                       		window.setTimeout(function(){
		                            window.location="${cxt_path}/advertiser/entry/new?campaignId="+ $("#campaingsList").val() +"&orderId="+data.contents.orderId;  
		                        }, 50);	
	                       	}						
						} else {
							displayStatusMessage(data.message, "error");
							isSubmitting = false;
						}
					},
					error: function (msg) {
						displayStatusMessage("创建广告组失败", "error");
						isSubmitting = false;
					}
				});
			}
    	}
		//submit click
		$("#submit").click(function(){ 
			submit(true);
		});
		//submit click
		$("#submitSave").click(function(){
			submit(false);
		});
		
		initDatePickers();
  });
</script>

