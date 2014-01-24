<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>设置 - ${campaign.name} - BuzzAds</title>
<link rel="stylesheet" href="${css_path}/calendar-date.css" type="text/css" />
<link rel="stylesheet" href="${css_path}/city.css" type="text/css" />
<link rel="stylesheet" href="${css_path}/popWin.css" />
<link rel="stylesheet" href="${css_path}/lhgcalendar.css" />
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/lhgcalendar.js"></script>
<script type="text/javascript" src="${js_path}/libs/popWin.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/city.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.forms.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.tooltip.min.1.2.7.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/specialDay.js"></script>

<div class="container-body">
	<div class="spacer20"></div>
	<div>
		<%@ include file="/jsp/advertiser/campaigns/LeftMenu.jsp"%>
		<div class="container-right" style="overflow: auto;">
	        <div class="overview bc">
				<p class="breadcrumb">
					<a class="bLinkD" style="padding-left:15px;" href="${cxt_path}/advertiser/campaigns">全部活动</a> &gt;&gt;
					<a class="orange" title="<s:property value="%{#request.campaign.name}" escape="false"/>" style="padding-left:10px;" href="javascript:void(0)">广告活动  - <core:partOfText value="${campaign.name}" textmaxlength="30" escape="false"/></a> 
				</p>
			</div>
			<div class="clear spacer10"></div>
			<div class="overviewPanel details" style="border-top:1px solid #ddd;">
	   	    	<div class="inlineBlock panelTitle">广告活动编辑</div>
	   	    </div>
	   	    <div class="overview" style="border-top:0px;">
			    <div style="width:980px" class="navCon">
				<table class="viewTable spread" style="width:100%">
                    <tr>
                        <td style="width:100px">活动名称</td>
                        <td><input class="validator bTextbox" required="required" maxlength="50" type="text" id="campaignName" value="${campaign.name }" placeholder="不能超过50个字符"/><span class="primary mSpacer_left10" id="name_msg"></span></td>
                    </tr>
                    <tr>
                    	<td>状态</td>
						<td>
							<s:if test="campaign.status.code == 1 || campaign.status.code == 2 ">
	                            <s:select list="#{1:'启用',2:'暂停'}" value="campaign.status.code" listKey="key" listValue="value" name="campaignStatus" cssClass="bTextbox" headerKey="0" >
	                            </s:select>
	                        </s:if>
	                        <s:else>
	                            <s:select list="#{1:'启用',2:'暂停',3:'禁止',4:'挂起'}" value="campaign.status.code" disabled="true" listKey="key" listValue="value" name="availableStats1" cssClass="bTextbox" headerKey="0" headerValue="---请选择---">
	                            </s:select>
	                        </s:else>
						</td>
                    </tr>
                    <tr>
                        <td>投放网络</td>
                        <td>
                        	<div>
                        	    <label>
                        	        <input type="checkbox" alt="<%=com.buzzinate.buzzads.enums.AdNetworkEnum.LEZHI.getCode()%>" name="network" 
                        	            <s:if test="#request.campaign.network.contains(@com.buzzinate.buzzads.enums.AdNetworkEnum@LEZHI)">
                        	                checked="true" 
                        	            </s:if> disabled="true" />
                        	        <span class="mSpacer_left10">乐知</span>
                                    <span title="<font style='color:#f60;font-weight:bold;'>乐知推荐</font>是目前最强大，最精准的个性化阅读类应用软件。<a class='bLinkU' target='_blank' href='http://www.lezhi.me'>去了解更多</a>！" class="help-popup-img"></span>
                        	    </label>
                        	    <label style="margin-left:15px;">
	                    	        <input type="checkbox" class="mSpacer_left10"  alt="<%=com.buzzinate.buzzads.enums.AdNetworkEnum.MULTIMEDIA.getCode()%>"  name="network"
	                    	        	<s:if test="#request.campaign.network.contains(@com.buzzinate.buzzads.enums.AdNetworkEnum@MULTIMEDIA)">
                        	                checked="true" 
                        	            </s:if> disabled="true" />
	                    	        <span class="mSpacer_left10">富媒体</span>
	                    	        <span title="右下角弹窗" class="help-popup-img"></span>
	                    	    </label>
                        	    <span class="primary mSpacer_left10" id="network_msg"></span>
                        	</div>
                        </td>
                    </tr>    
                     <tr>
                        <td>出价类型</td>
                        <td>
                        	<div>
	                        	<label><input style="margin:0px;" disabled="disabled" type="radio" alt="3" id="bidType" name="bidType" 
		                        	<s:if test="#request.campaign.bidType == (@com.buzzinate.buzzads.enums.BidTypeEnum@CPC)">
		                        	  checked="true"
		                        	</s:if>
	                        	/><span class="mSpacer_left10">CPC</span></label>&nbsp;&nbsp;
	                        	<label><input style="margin:0px;" disabled="disabled" type="radio" alt="0" id="bidType" name="bidType"
		                        	<s:if test="#request.campaign.bidType == (@com.buzzinate.buzzads.enums.BidTypeEnum@CPM)">
	                                  checked="true"
	                                </s:if>
	                        	/><span class="mSpacer_left10">CPM</span></label>
	                        	<span class="primary mSpacer_left10" id="bidType_msg"></span>
	                        </div>
                        </td>
                    </tr>
                    <tr>
	                	 <td colspan="2">
                            <div class="spacer15" style="border-top: 1px dashed #dadada;"></div>
	                		<span class="heading4 orange">投放计划</span>
	                	 </td>
		            </tr>
                    <tr>
                        <td  style="vertical-align:top">投放时间</td>
                        <td style="padding-bottom:0;">
                        	<div>
		                    		<span id="startDate">${campaign.startDateStr }</span> - 
		                    		<span id="endDate"><s:if test="%{campaign.endDateStr == null || campaign.endDateStr  == ''}">无截止日期</s:if><s:else>${campaign.endDateStr }</s:else></span> 
		                    		<a id="dateEdit" class="packer orange spacer_left15" href="javascript:void(0)">修改<i></i></a>
		                    		<span id="warnMsg" class="packer orange spacer_left15"></span>
		                    </div>
		                    <div class="" style="display:none;" id="dateCon">
		                    		<div class="spacer10"></div>
			                    	<div class="dataSearch label_date" >
				                    	 <div style="overflow:hidden;">
				                    	 	  <input   class="bTextbox date left" type="date" id="datePickerStart" name="dateStart" value="${campaign.startDateStr }" style="margin:0px;">
						                      <div class="left" style="padding:0px 10px;line-height:32px;height:32px;">-</div>
						                      <input   class="bTextbox date left" type="date" data-orig-type="date" id="datePickerEnd" name="dateEnd" value="${campaign.endDateStr }" >
						                      <span class="primary mSpacer_left10" id="date_msg"></span>
				                    	 </div>
					                      <div>
					                      	  <div class="spacer10"></div>
					                       	  <label><input type="checkbox" id="now"  /><span class="mSpacer_left10" style="display:inline-block;">当前日期</span></label>
						                	  <span style="display:inline-block;width:69px;"></span>
						                      <label><input type="checkbox" id="noEndDate"  /><span class="mSpacer_left10" style="display:inline-block;">无截止日期</span></label>
						                  </div>
			                    	</div>
		                    </div>
                        </td>
                    </tr>
                    <tr>
                        <td>地理位置</td>
                        <td>
                        	<div>
		                    	 <span id="locationP"><s:if test="%{campaign.viewLocation == null || campaign.viewLocation  == ''}">无限制</s:if><s:else>${campaign.viewLocation }</s:else></span>
		                    	 <a id="locationEdit" class="packer orange spacer_left15" href="javascript:void(0)">修改<i></i></a>
		                    </div>
		                    <div  id="loactionCon" style="display:none;">
		                    		<div class="spacer10"></div>
		                    		<div class="label_date">
			                    		<input class="validator bTextbox" required="required" maxlength="50" type="text" id="location" value="${campaign.viewLocation}" placeholder="上海,北京" />
			                    		<span style="display:inline-block;width:10px;"></span>
                        				<label><input type="checkbox" id="noLocation"  /><span class="mSpacer_left10">无限制</span></label></div>
				                    	<span class="primary mSpacer_left10" id="loacation_msg"></span>
		                   </div>
                        </td>
                    </tr>
                    <tr>
	                	<td colspan="2">
                            <div class="spacer15" style="border-top: 1px dashed #dadada;"></div>
	                		<span class="heading4 orange">投放预算</span>
	                	</td>
		            </tr>
                    <tr>
                        <td>投放预算</td>
                        <td>
                        	<div>
		                    	 <span>总预算：</span><span id="totalP"><s:if test="%{campaign.maxTotalBudgetDouble == 0.00}"><font>无限制</font></s:if><s:else>¥${campaign.maxTotalBudgetDouble}</s:else> </span> ， 
		                    	 <span>日预算：</span><span id="dayP"><s:if test="%{campaign.maxDayBudgetDouble == 0.00}"><font >无限制</font></s:if><s:else>¥${campaign.maxDayBudgetDouble}</s:else> </span>
		                    	 <a id="primaryEdit" class="packer orange spacer_left15" href="javascript:void(0)">修改<i></i></a>
		                    	 <div class="spacer10"></div>
		                    	 <div id="showSpecialDay" class=""></div>
		                    </div>
		                    <div id="primaryCon" style="display:none">
		                    		<div class="spacer10"></div>
				                    <div class="label_checkbox">
				                    	<div class="spacer10"></div>
				                    	<span >总预算：¥</span> <input class="validator bTextbox" required="required" step="1" min="0" type="Number" id="max_validator" value="${campaign.maxTotalBudgetDoubleNoComma}" style="width:70px;" />
				                    	<label ><input type="checkbox" id="max_nolimit"   />无限制</label>
				                     	<span style=" margin: 0px 0px 0px 70px ;">日预算：¥</span> <input class="validator bTextbox" required="required" step="1" min="0" type="Number" id="day_validator" value="${campaign.maxDayBudgetDoubleNoComma}" style="width:70px;" />
				                     	<label ><input type="checkbox" id="day_nolimit"   />无限制</label>
				                     	<span class="primary inlineBlock" id="day_msg" ></span>
				                    	<div class="spacer10"></div>
				                    	<a id="setSpecialDay" class="packer orange" href="javascript:void(0)">设置指定特定日期预算<i></i></a>
				                    	<div id="specialDayCon" style="display:none;">
				                    		<div class="dataSearch" id="specialDay" style="overflow:hidden;">
				                    			
				                    			
				                    		</div>
				                    		<s:iterator value="#request.campaign.dayBudgets" var="cd" status="status">
				                    			<script type="text/javascript">
				                    				var d="${cd.budgetDay }".split(" ");
				                    				setSpricalDate({date:d[0],budget:"${cd.budgetDouble}"});
				                    			</script>
				                    		</s:iterator>
				                    		<div>
				                    			<div class="spacer10"></div>
				                    			<a class="packer orange addSpecialDay" id="addSpecialDay" href="javascript:void(0)"> + 添加</a>
				                    		</div>
				                    	</div>
				                    	<div class="spacer10"></div>
				                    </div>
		                    </div>
                        </td>
                    </tr>
                    
                    <tr>
                        <td></td>
                        <td>
                            <div class="spacer15"></div>
	                        <input id="submit" class="bButton center lightOrange" type="button" value="保存" />
	                        <input class="bButton center" style="margin-left:20px;" type="button" value="取消"  id="cancel" />
                        </td>
                    </tr>
                </table>
            </div>
            <div class="clear spacer50"></div>
			</div>
		</div>
		<div class="clear spacer20"></div>
		<div id="city" class="city"></div>
		<div id="popWin" class="popWin" style="display:none"></div>
	</div>
</div>

<script type="text/javascript">
	$(function(){
		
		$("#setSpecialDay").toggle(function(){$("#specialDayCon").show();},function(){$("#specialDayCon").hide();});
		
		$("#addSpecialDay").click(function(){
			var maxDateTemp=$("#datePickerEnd").val();
			if(maxDateTemp==""){
				setSpricalDate();
			}else{
				setSpricalDate({maxDate:maxDateTemp});
			}
		   	
		});
		//create popWin
		var data={
				container:$("#popWin"),
				width:500,
				height:415,
				title:"选择城市",
				content:$("#city"),
				closeCallback:function(){
					
				},
				afterCallback:function(){
				
				}
			}
		var popwin=new bshare.popWin(data);

		$(".packer").toggle(function () {
            var i = $(this).find("i");
            i.addClass(" selected");
        },function(){
        	var i = $(this).find("i");
        	i.removeClass(" selected");
        });
		
		//cretate city
		var cityDiv=new bshare.city(
				{container:$("#city"),
				 clickCallback:function(){
					 if(this.selectedCity.length!=0){
						 $("#location").val(this.selectedCity.join(","));
						 $("#locationP").html(this.selectedCity.join(","));
					 }else{
						 $("#location").val("");
						 $("#locationP").html("无限制");
					 }
					
					popwin.close();
				 },
				 cancelCallback:function(){
					 popwin.close();
				 }
				});
		
		$("#cancel").click(function(){
			document.location='${cxt_path}/advertiser/campaigns';
		});
		
		var isChoose=true;
		//primary 
		$("#primaryEdit").click(function(){
			if(isChoose){
				$("#primaryCon").show();
				isChoose=false;
			}else{
				$("#primaryCon").hide();
				isChoose=true;
			}
		});
		
		//date
		$("#dateEdit").toggle(function(){$("#dateCon").show();$("#warnMsg").html("修改活动投放时间，若所属广告组的投放时间超出范围，也会被重置！");},function(){$("#dateCon").hide();$("#warnMsg").html("");});
		//loaction
		$("#locationEdit").toggle(function(){$("#loactionCon").show();},function(){$("#loactionCon").hide();});
		//add mul function for float bug
		Number.prototype.mul = function (arg){
			var m=0,s1=arg.toString(),s2=this.toString();
		    try{m+=s1.split(".")[1].length}catch(e){}
		    try{m+=s2.split(".")[1].length}catch(e){}
		    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
		}
		//更改活动状态
        $("#campaignStatus").change(function(){
            var urltemp="";
            if($(this).val()==1){
                urltemp='${cxt_path}/advertiser/campaign/${campaign.id}/enable';
            }else if($(this).val()==2){
                urltemp='${cxt_path}/advertiser/campaign/${campaign.id}/pause';
            }
            $.ajax({
                type: "post",
                url: urltemp,
                dataType:"json",
                success: function (data) {
                if(data.success){
                    displayStatusMessage("状态修改成功", "success");
                } else {
                    displayStatusMessage("状态修改失败", "error");  
                }
                },
                error: function (msg) {
                    displayStatusMessage("修改失败", "error");   
                }
            });
        
        });
		
		// init date pickers
	    var fDay = 0;
	    if('zh' == "zh") {
	        $.tools.dateinput.localize("zh", {
	            months: "一月,二月,三月,四月,五月,六月,七月,八月,九月,十月,十一月,十二月",shortMonths: "一,二,三,四,五,六,七,八,九,十,十一,十二",days: "星期日,星期一,星期二,星期三,星期四,星期五,星期六",shortDays: "周日,周一,周二,周三,周四,周五,周六"
	        });
	        fDay = 1;
	    } else if('zh' == "zh_TW") {
	        $.tools.dateinput.localize("zh_TW", {
	            months: "一月,二月,三月,四月,五月,六月,七月,八月,九月,十月,十一月,十二月",shortMonths: "一,二,三,四,五,六,七,八,九,十,十一,十二",days: "星期日,星期一,星期二,星期三,星期四,星期五,星期六",shortDays: "周日,周一,周二,周三,周四,周五,周六"
	        });
	        fDay = 1;
	    }

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
                for(var i=0;i<spricalItem.length;i++){
                	spricalItem[i].setDate($("#datePickerStart").val(),$("#datePickerEnd").val());
                }
	        });
	        $(":date:last").data("dateinput").change(function() {
	            // we use it's value for the first input max option
	            $(":date:first").data("dateinput").setMax(this.getValue(), true);
	            $("#endDate").html($("#datePickerEnd").val());
	            for(var i=0;i<spricalItem.length;i++){
                	spricalItem[i].setDate($("#datePickerStart").val(),$("#datePickerEnd").val());
                }
	            
	        });
	    }
	        
	  //page init set 
		function init(){
			var dayBudget="${campaign.maxDayBudgetDouble}";
			var totalBudget="${campaign.maxTotalBudgetDouble}";
			if(dayBudget==0){
				$("#day_validator").attr("disabled","true");
				$("#day_nolimit").attr("checked","true");
			}
			if(totalBudget==0){
				$("#max_validator").attr("disabled","true");
				$("#max_nolimit").attr("checked","true");
			}
			if("${campaign.endDateStr }" == ""){
				$("#noEndDate").attr("checked","true");
				$("#datePickerEnd").val("");
				$("#datePickerEnd")[0].disabled="true";
			}
			if("${campaign.viewLocation}" == ''){
				$("#noLocation").attr("checked","true");
				
				$("#location")[0].disabled="true";
			}
			  $(":date:first").data("dateinput").setMin(RC.getDate(), false);
              $(":date:last").data("dateinput").setMin(RC.getDate(), false);
           if("${campaign.viewLocation}" !== ""){
        	   cityDiv.selectByName("${campaign.viewLocation}");  
           }
              
		}
		init();
	    
		//choose campaign
		$("#topmenu-advertiser-adCampaign").addClass("active");
		$("#leftmenu-advertiser-campaign").addClass("active");
		//form confirmation
		$("#day_nolimit").click(function(){
			if(($(this)[0]).checked){
				$("#day_validator").attr("disabled","true");
			}else{
				$("#day_validator").removeAttr("disabled");
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
				var d=new Date();
				var date=d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
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
				if("${campaign.endDateStr}" == ""){
					var date=new Date(RC.dateToSprit($("#datePickerStart").val()));
					date.setMonth(date.getMonth( ) + 1);// default here to 1 month ahead
				} else {
					date = new Date(RC.dateToSprit("${campaign.endDateStr}"));
				}
                $("#datePickerEnd").data("dateinput").setValue(date);
			}
		});
		
		
    	$("#location").click(function(){
    		popwin.show();
    	});
    	$("#noLocation").click(function(){
    		if($(this)[0].checked){
				$("#location")[0].disabled="true";
				$("#locationP").html("无限制");
			}else{
				$("#location").removeAttr("disabled");
				$("#locationP").html($("#location").val()==""?"请选择城市":$("#location").val());
			}
    	});
    	
    	var campaignData={};
    	campaignData.id=${campaign.id};
    	function getEndDate(){
    		if($("#datePickerEnd").val() !=""){
				var timeEnd=new Date($("#datePickerEnd").val());
				var timeStart=new Date(campaignData.start);
				if(timeStart>timeEnd){
					$("#date_msg").html("结束时间不能小于开始时间");
					campaignData.isEnd=false;
				}else{
					$("#date_msg").html="";
					campaignData.end=$("#datePickerEnd").val();
					campaignData.isEnd=true;
				}
			}else{
				campaignData.isEnd=true;
			}
    	}
    	function getName(){
    		if($("#campaignName").val()!="" && $("#campaignName").val().length <= 50){
    			var cn=RC.stripscript($.trim($("#campaignName").val()));
    			campaignData.name=cn;
				$("#name_msg").html("");
				campaignData.isName=true;
			}else if($("#campaignName").val().length > 50){
				$("#name_msg").html("名称不能大于50个字");
				campaignData.isName=false;
			}else{
				$("#name_msg").html("请输入活动名称");
				campaignData.isName=false;
			}
    	}
    	$("#campaignName").blur(function(){
    		getName();
    	});
    	
    	function getStartDate(){
    		if($("#datePickerStart").val()!=""){
				campaignData.start=$("#datePickerStart").val();
				$("#date_msg").html("");
				campaignData.isStart=true;
			}else{
				campaignData.start=$("#datePickerStart").val();
				$("#date_msg").html("请选择开始时间");
				campaignData.isStart=false;
			}
    	}
    	function getBidType(){
    		var $bidType=$("input[name='bidType']:radio:checked");
    		
    		if($bidType.size() > 0){
				campaignData.bidType=$bidType.first().attr('alt');
				$("#bidType_msg").html("");
				campaignData.isBidType = true;
			}else{
				$("#bidType_msg").html("请选择出价类型");
				campaignData.isBidType=false;
			}
    	}
    	function getNetwork(){
    		var network=$("input[name='network']:checked");
    		if(network.length>0){
    			var tempnet=[];
				for(var i=0;i<network.length;i++){
					tempnet.push($(network[i]).attr("alt"));
				}
				campaignData.network=tempnet.join(",");
				$("#network_msg").html("");
				campaignData.isNetWork=true;
    		}else{
    			$("#network_msg").html("请选择投放网络");
				campaignData.isNetWork=false;
    		}
    	}
    	function getBudget(){
    		var dayTemp=$("#day_nolimit")[0].checked;
    		var maxTemp=$("#max_nolimit")[0].checked;
    		if(dayTemp){
				campaignData.maxBudgetDay=0;
				campaignData.isMaxDayBudget=true;
			}else {
				var dayNum=$("#day_validator").val();
				if(RC.numVerify(dayNum)){
					dayNum=parseFloat($("#day_validator").val()).mul(100);
					$("#day_msg").html("");
					$("#dayP").html("¥ "+RC.numFloat($("#day_validator").val()));
					campaignData.maxBudgetDay=dayNum;
					campaignData.isMaxDayBudget=true;
				}else{
					$("#day_msg").html("请输入合法数字,非负整数或小数[小数最多精确到小数点后两位]！");
					campaignData.isMaxDayBudget=false;
					return;
				}
				
			}
    		if(maxTemp){
				campaignData.maxBudgetTotal=0;
				campaignData.isMaxBudgetTotal=true;
			}else{
				var maxNum=$("#max_validator").val();
				if(RC.numVerify(maxNum)){
					$("#day_msg").html("");
					maxNum=parseFloat($("#max_validator").val()).mul(100);	
					$("#totalP").html("¥ "+RC.numFloat($("#max_validator").val()));
					campaignData.maxBudgetTotal=parseFloat($("#max_validator").val()).mul(100);
					campaignData.isMaxBudgetTotal=true;
				}else{
					$("#day_msg").html("请输入合法数字,非负整数或小数[小数最多精确到小数点后两位]！");
					campaignData.isMaxBudgetTotal=false;
					return;
				}
			}
    		if(!dayTemp && !maxTemp ){
				var day=parseFloat($("#day_validator").val()).mul(100);
				var max=parseFloat($("#max_validator").val()).mul(100);
				if(day && max){
					if(day>max){
						$("#day_msg").html("总额度必须大于或等于日预算");
						campaignData.isMaxDayBudget=false;
						return;
					}else{
						$("#day_msg").html("");
						campaignData.maxBudgetDay=day;
						campaignData.maxBudgetTotal=max;
						campaignData.isMaxDayBudget=true;
						campaignData.isMaxBudgetTotal=true;
					}
				}else if(!day){
					if(max){
						$("#day_msg").html("");
					}
					$("#day_msg").html("日预算必须是数字");
					campaignData.isMaxDayBudget=false;
				}else if(!max){
					if(day){
						$("#day_msg").html("");
					}
					$("#day_msg").html("总预算必须是数字并且要大于0");
					campaignData.isMaxBudgetTotal=false;
				}
			}
    	}
    	$("#day_validator").change(function(){
    		getBudget();
    	});
    	$("#max_validator").change(function(){
    		getBudget();
    	});
    	$("#day_nolimit").click(function(){
    		var dayTemp=$("#day_nolimit")[0].checked;
    		if(!dayTemp){
    			getBudget();
    		}else{
    			$("#day_msg").html("");
    			$("#dayP").html("无限制");
    		}
    	});
    	$("#max_nolimit").click(function(){
    		var maxTemp=$("#max_nolimit")[0].checked;
    		if(!maxTemp){
    			getBudget();
    		}else{
    			$("#day_msg").html("");
    			$("#totalP").html("无限制");
    		}
    	});
    	function getSpecialData(){
    		if(getDataString()){
    			campaignData.dayBudgetStr=getDataString().join("_");
    			campaignData.isDayBudgetStr=true;
    		}else{
    			campaignData.isDayBudgetStr=false;
    		}
    		
    	}
    	function submit(){
    		getName();
    		getStartDate();
			getEndDate();
			campaignData.location=$("#noLocation")[0].checked?"":$("#location").val();
			getBidType();
			getNetwork();
			getBudget();
			getSpecialData();
			if(campaignData.isMaxDayBudget && campaignData.isMaxBudgetTotal && campaignData.isDayBudgetStr && campaignData.isMaxDayBudget && campaignData.isStart &&  campaignData.isBidType && campaignData.isName &&  campaignData.isNetWork){
				$.ajax({
					type: "post",
					url: "${cxt_path}/advertiser/campaign/update",
					data:campaignData,
					dataType:"json",
					success: function (data) {
						if(data.success){
							 displayStatusMessage("保存成功！", "success"); 
							 window.setTimeout(function(){
								 if (${specifiedCampPage}) {
									 window.location="${cxt_path}/advertiser/campaign/${campaign.id}/groups";
								 } else {
									 window.location="${cxt_path}/advertiser/campaigns";
								 }
							}, 500);
						}else{
							displayStatusMessage("活动设置失败,"+data.message, "error");   
						}
					},
					error: function (msg) {
						displayStatusMessage("活动设置失败", "error");   
					}
				});
			}
    	}
		//submit click
		$("#submit").click(function(){ 
			submit();
		});
  });
		
		
	
</script>

