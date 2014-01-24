<%@ page import="com.buzzinate.buzzads.enums.AdNetworkEnum" %>
<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>创建一个新的活动</title>

<div class="container-body">
	<div class="spacer20"></div>
	<div>
		<%@ include file="/jsp/advertiser/campaigns/LeftMenu.jsp"%>
		<div class="container-right" style="overflow: auto;">
			<div class="overview">
				<div style="width:980px" class="navCon">
					<table class="navTable" style="width:100%" cellpadding="0" cellspacing="0">
						<tr>
							<td><span>新建广告活动</span></td>
                            <td><span><a href="${cxt_path}/advertiser/group/new">新建广告组</a></span></td>
                            <td><span><a href="${cxt_path}/advertiser/entry/new">新建广告</a></span></td>
						</tr>
						<tr>
							<td><div class="spacer10"></div><div class="navBg"><div class="naPointer"></div></div></td>
							<td><div class="spacer10"></div><div class="navBg"></div></td>
							<td><div class="spacer10"></div><div class="navBg"></div></td>
						</tr>
					</table>
					<div class="spacer20"></div>
					<table class="viewTable" style="width:100%">
		                <tr>
		                    <td style="width:100px;padding-top:20px;">广告活动名称</td>
		                    <td><input class="validator bTextbox" required="required" maxlength="50" type="text" id="campaignName" value="" placeholder="不能超过50个字符"/><span class="primary mSpacer_left10" id="name_msg"></span></td>
		                </tr>
		                <tr>
		                    <td>投放网络</td>
		                    <td>
		                    	<div>
		                    	    <label>
		                    	        <input  style="margin:0px;"  type="radio" name="network" alt="<%=com.buzzinate.buzzads.enums.AdNetworkEnum.LEZHI.getCode()%>"  checked="true" />
		                    	        <span class="mSpacer_left10">乐知</span>
		                    	        <span title="<font style='color:#f60;font-weight:bold;'>乐知推荐</font>是目前最强大，最精准的个性化阅读类应用软件。<a class='bLinkU' target='_blank' href='http://www.lezhi.me'>去了解更多</a>！" class="help-popup-img"></span>
		                    	    </label>
		                    	    <label>
		                    	        <input type="radio" class="mSpacer_left10"  alt="<%=com.buzzinate.buzzads.enums.AdNetworkEnum.MULTIMEDIA.getCode()%>"  name="network" />
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
	                                <label><input style="margin:0px;" type="radio" alt="3" id="bidType" name="bidType" checked/><span class="mSpacer_left10">CPC</span></label>&nbsp;&nbsp;
	                                <label><input style="margin:0px;" type="radio" alt="0" id="bidType" name="bidType"/><span class="mSpacer_left10">CPM</span></label>
	                                <span class="primary mSpacer_left10" id="bidType_msg"></span>
                                </div>
		                    </td>
		                </tr>
		                 <tr>
		                	<td colspan="2">
                                <div class="spacer30" style="border-top: 1px dashed #dadada;"></div>
		                		<span class="heading4 orange">投放计划</span>
		                	</td>
		                </tr>
		                <tr>
		                    <td  style="vertical-align:top;padding-bottom:6px;">投放时间</td>
		                    <td style="padding-bottom:0;">
		                    	<div>
		                    		<span id="startDate"></span> - <span id="endDate">无截止日期</span> <a id="dateEdit" 
		                    		    class="packer orange spacer_left15" href="javascript:void(0)">修改<i></i></a>
		                    	</div>
		                    	<div class="" style="display:none;" id="dateCon">
		                    		<div class="spacer10"></div>
			                    	<div class="dataSearch label_date" >
				                    	 <div style="overflow:hidden;">
				                    	 	 <input class="bTextbox date left" type="date" id="datePickerStart" name="dateStart" value="${dateStart}" data-value="${dateStart}" style="margin:0px;">
						                      <div class="left" style="padding:0px 10px;line-height:32px;height:32px;">-</div>
						                      <input class="bTextbox date left" type="date" data-orig-type="date" id="datePickerEnd" name="dateEnd" value="${dateEnd}" data-value="${dateEnd}" >
						                      <span class="primary mSpacer_left10" id="date_msg"></span>
				                    	 </div>
					                      <div>
					                      	  <div class="spacer10"></div>
					                       	  <label><input type="checkbox" id="now" checked="true" /><span class="mSpacer_left10" style="display:inline-block;">当前日期</span></label>
						                	  <span style="display:inline-block;width:69px;"></span>
						                      <label><input type="checkbox" id="noEndDate" checked="true" /><span class="mSpacer_left10" style="display:inline-block;">无截止日期</span></label>
						                  </div>
			                    	</div>
		                    	</div>
		                    </td>
		                </tr>
		               
		                 
		                <tr>
		                    <td>地理位置</td>
		                    <td>
		                    	<div>
		                    		<span id="locationP">无限制</span><a id="locationEdit" class="packer orange spacer_left15" href="javascript:void(0)">修改<i></i></a>
		                    	</div>
		                    	<div  id="loactionCon" style="display:none;">
		                    		<div class="spacer10"></div>
		                    		<div class="label_date">
			                    		<input class="validator bTextbox" required="required" maxlength="50" type="text" id="location" value="" placeholder="上海,北京" />
			                    		<span style="display:inline-block;width:10px;"></span>
                        				<label><input type="checkbox" id="noLocation" checked="true"  /><span class="mSpacer_left10">无限制</span></label></div>
				                    	<span class="primary mSpacer_left10" id="loacation_msg"></span>
		                    		</div>
		                    	</div>
		                    </td>
		                </tr>
		                <tr>
		                	<td colspan="2">
                                <div class="spacer30" style="border-top: 1px dashed #dadada;"></div>
		                		<span class="heading4 orange">投放预算</span>
		                	</td>
		                </tr>
		                <tr>
		                    <td>投放预算</td>
		                    <td>
		                    	<div>
		                    		<span>总预算：</span><span id="totalP">无限制</span> ， <span>日预算：</span><span id="dayP">无限制</span>
		                    		<a id="primaryEdit" class="packer orange spacer_left15" href="javascript:void(0)">修改<i></i></a>
		                    		<div class="spacer10"></div>
		                    		<div id="showSpecialDay" class=""></div>
		                    	</div>
		                    	<div id="primaryCon" style="display:none">
		                    		<div class="spacer10"></div>
				                    <div class="label_checkbox">
				                    	<div class="spacer10"></div>
				                    	<span >总预算：¥</span> <input class="validator bTextbox" required="required" step="1" min="0" type="Number" id="max_validator" value="100" style="width:70px;" />
				                    	<label ><input type="checkbox" id="max_nolimit"  checked="true" />无限制</label>
				                     	<span style=" margin: 0px 0px 0px 70px ;">日预算：¥</span> <input class="validator bTextbox" required="required" step="1" min="0" type="Number" id="day_validator" value="100" style="width:70px;" />
				                     	<label ><input type="checkbox" id="day_nolimit"  checked="true" />无限制</label>
				                     	<span class="primary inlineBlock" id="day_msg" style="margin-top:10px"></span>
				                    	<div class="spacer10"></div>
				                    	<a id="setSpecialDay" class="packer orange" href="javascript:void(0)">设置指定特定日期预算<i></i></a>
				                    	<div id="specialDayCon" style="display:none;">
				                    		<div class="dataSearch" id="specialDay" style="overflow:hidden;">
				                    			
				                    			
				                    		</div>
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
		                    <input id="submitSave" class="bButton center lightOrange " type="button" value="保存并新建广告组" />
		                    <input id="submit" class="bButton center" style="margin-left:20px;" type="button" value="保存" />
		                    <input class="bButton center" style="margin-left:20px;" type="button" value="取消" onclick="document.location='${cxt_path}/advertiser/campaigns';" /></td>
		                </tr>
	            </table>
            </div>
            <div class="clear spacer50"></div>
			</div>
		</div>
		<div class="clear spacer50"></div>
		<div id="city" class="city"></div>
		<div id="popWin" class="popWin" style="display:none"></div>
	</div>
</div>
<link rel="stylesheet" href="${css_path}/calendar-date.css" type="text/css" />
<link rel="stylesheet" href="${css_path}/city.css" type="text/css" />
<link rel="stylesheet" href="${css_path}/popWin.css" />
<link rel="stylesheet" href="${css_path}/lhgcalendar.css" />
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/lhgcalendar.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/city.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/specialDay.js"></script>
<script type="text/javascript" src="${js_path}/libs/popWin.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.forms.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.tooltip.min.1.2.7.js"></script>
<script type="text/javascript">
	$(function(){
		$(".packer").toggle(function () {
            var i = $(this).find("i");
            i.addClass(" selected");
        },function(){
        	var i = $(this).find("i");
        	i.removeClass(" selected");
        });
		
		$("#setSpecialDay").toggle(function(){$("#specialDayCon").show();},function(){$("#specialDayCon").hide();});
		
		
		
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
		
		//add mul function for float bug
		Number.prototype.mul = function (arg){
			var m=0,s1=arg.toString(),s2=this.toString();
		    try{m+=s1.split(".")[1].length;}catch(e){}
		    try{m+=s2.split(".")[1].length;}catch(e){}
		    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
		}
		
		
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

			    // temp fix for dateinput incompatibility with ie6 and ie7
			    //if (!($.browser.msie && ($.browser.version=="6.0" || $.browser.version=="7.0"))) {
			    $(":date").dateinput({ trigger: true, format: 'yyyy-mm-dd', lang: 'zh', firstDay: fDay });
			    $(":date").bind("onShow onHide", function()  {
		            $(this).parent().toggleClass("active"); 
		        });
		        if ($(":date:first").length != 0) {
		            $(":date:first").data("dateinput").change(function() {
		                // we use it's value for the seconds input min option
		                //if (!($.browser.msie && ($.browser.version=="6.0" || $.browser.version=="7.0"))) {
		                    // ie6, ie7 does not like this...
		                    $(":date:first").data("dateinput").setMin(RC.getDate(), true);
		                    $(":date:last").data("dateinput").setMin(this.getValue(), false);
		                    $("#startDate").html($("#datePickerStart").val());
		                    for(var i=0;i<spricalItem.length;i++){
		                    	spricalItem[i].setDate($("#datePickerStart").val(),$("#datePickerEnd").val());
		                    }
		                //}
		            });
		            $(":date:last").data("dateinput").change(function() {
		                // we use it's value for the first input max option
		                //if (!($.browser.msie && ($.browser.version=="6.0" || $.browser.version=="7.0"))) {
		                    // ie6, ie7 does not like this...
		                    $(":date:first").data("dateinput").setMax(this.getValue(), true);
		                    $("#endDate").html($("#datePickerEnd").val());
		                    for(var i=0;i<spricalItem.length;i++){
		                    	spricalItem[i].setDate($("#datePickerStart").val(),$("#datePickerEnd").val());
		                    }
		                    
		                //} 
		            });
		        }
		   $("#addSpecialDay").click(function(){
			   	setSpricalDate();
			});
		//choose campaign
		$("#topmenu-advertiser-adCampaign").addClass("active");
		$("#leftmenu-advertiser-quicklyCreate").addClass("active");
		
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
		
		
		//primary 
		$("#primaryEdit").toggle(function(){$("#primaryCon").show();},function(){$("#primaryCon").hide();});
		//date
		$("#dateEdit").toggle(function(){$("#dateCon").show();},function(){$("#dateCon").hide();});
		//loaction
		$("#locationEdit").toggle(function(){$("#loactionCon").show();},function(){$("#loactionCon").hide();});
		
		
		
		var cityDiv=new bshare.city(
		{container:$("#city"),
		 clickCallback:function(){
			if(this.selectedCity.length != 0){
				$("#location").val(this.selectedCity.join(","));
				$("#locationP").html(this.selectedCity.join(","));
			}
			else{
				 $("#location").val("");
				 $("#locationP").html("无限制");
			 }
			popwin.close();
		 },
		 cancelCallback:function(){
			popwin.close();
		 }
		});
		function getDate(){
			var d=new Date();
			var date=d.getFullYear()+"-"+((d.getMonth()+1)>9?(d.getMonth()+1):"0"+(d.getMonth()+1))+"-"+(d.getDate()>9?d.getDate():"0"+d.getDate());
			$("#datePickerStart").data("dateinput").setValue(date);
			return date;
		}
		//date click
		$("#now").click(function(){
			if($(this)[0].checked){
				getDate();
				$("#datePickerStart")[0].disabled="true";
				$("#startDate").html(getDate());
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
				var date=new Date(RC.dateToSprit($("#datePickerStart").val()));
                date.setMonth(date.getMonth( ) + 1);// default here to 1 month ahead
                $("#datePickerEnd").data("dateinput").setValue(date);
                
			}
		});
		init();
		function init(){
			//set default now 
			getDate();
			$("#startDate").html(getDate());
			$("#datePickerStart")[0].disabled="true";	
			//set default end 
			$("#datePickerEnd").val("");
			$("#datePickerEnd")[0].disabled="true";
			//set default day
			$("#day_validator").attr("disabled","true");
			//set default max
			$("#max_validator").attr("disabled","true");
			
			$("#location")[0].disabled="true";
			//$(spricalD.day).data("dateinput").setValue(date);
			
		}
		
		
		
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
    		if(network[0].checked){
				campaignData.network=network.attr("alt");
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
					$("#dayP").html("¥ "+RC.numFloat($("#day_validator").val()));
					$("#day_msg").html("");
					campaignData.maxBudgetDay=dayNum;
					campaignData.isMaxDayBudget=true;
				}else{
					$("#day_msg").html("请输入合法数字,非负整数或小数[ 小数最多精确到小数点后两位  ]！");
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
					$("#day_msg").html("总额度必须是数字并且要大于0");
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
    		if(spricalItem.length==0){
    			campaignData.dayBudgetStr="";
    			campaignData.isDayBudgetStr=true;	
    		}else{
        		if(getDataString()){
        			campaignData.dayBudgetStr=getDataString().join("_");
        			campaignData.isDayBudgetStr=true;
        		}else{
        			campaignData.isDayBudgetStr=false;
        		}
    		}
    	}
    	
    	var isSubmitting = false;
    	function submit(isSave){
    		getName();
    		getStartDate();
			getEndDate();
			campaignData.location=$("#noLocation")[0].checked?"":$("#location").val();
			getBidType();
			getNetwork();
			getBudget();
			getSpecialData();
			if(campaignData.isEnd && campaignData.isMaxBudgetTotal  && campaignData.isDayBudgetStr && campaignData.isMaxDayBudget && campaignData.isStart && campaignData.isBidType && campaignData.isName && campaignData.isNetWork){
				if (isSubmitting) return;
				isSubmitting = true;
				$.ajax({
					type: "post",
					url: "${cxt_path}/advertiser/campaign/create",
					data:campaignData,
					dataType:"json",
					success: function (data) {
						if (data.success) {
							displayStatusMessage("保存成功！", "success");
							if(isSave){
								window.setTimeout(function(){
		                             window.location="${cxt_path}/advertiser/campaigns";   
		                         }, 500);
							}else{
								window.setTimeout(function(){
		                             window.location="${cxt_path}/advertiser/group/new?campaignId="+data.contents.campId;   
		                         }, 500);
							}
						} else {
							displayStatusMessage(data.message, "error");
							isSubmitting = false;
						}
					},
					error: function (msg) {
						displayStatusMessage("创建活动失败", "error");
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
  });
</script>

