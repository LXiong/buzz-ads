<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>Ad Campaign - View</title>
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
        <%@ include file="/jsp/admin/orders/LeftMenu.jsp"%>
        
        <div class="container-right" style="overflow:auto;">
        	<s:if test="%{#request.campaign != null && #request.campaign.id > 0}">
        	
        	  <div class="overview bc">
				<p class="breadcrumb">
	        		<a class="bLinkD" style="padding-left:15px;" href="${cxt_path }/admin/ads/campaign">活动管理</a>&gt;&gt;
	        		<a class="orange" title="<s:property value="%{#request.campaign.name}" escape="false"/>" style="padding-left:10px;" href="javascript:void(0)">广告活动  - <core:partOfText value="${campaign.name}" textmaxlength="30" escape="false"/></a>
        		</p>
        	</div>
        	<div class="spacer15"></div>
        	<div class="overviewPanel details" style="border-width:1px;">
			    <div class="inlineBlock panelTitle">活动信息</div>
		   </div>
            <div class="overview noBT" >
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
           </s:if>
            <div class="overviewPanel details" style="border-top:0px;">
			    <div class="inlineBlock panelTitle">订单列表</div>
		   </div>  
		   <div class="overview noBT">
		   		<table class="bTable"> 
					<tr class="heading">
						<th>ID</th>
						<th>订单名称</th>
                        <th>开始时间</th>
                        <th>结束时间</th>
                        <th>默认出价</th>
						<th style="text-align:center;">状态</th>
						<th style="text-align:center;">操作</th>
					</tr>
					<tr class="heading-float fixed hidden" style="top:0;"></tr>
					<s:if test="#request['adOrderList'].size() > 0">
						<s:iterator value="#request['adOrderList']" var="order" status="status" >
						<tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
							<td>${order.id}</td>
                            <td><span title="<s:property value="%{#order.name}" escape="true"/>"><core:partOfText value="${order.name}" textmaxlength="20" escape="true"/></span></td>
                            <td><s:date name="#order.startDate" format="yyyy-MM-dd"/></td>
							<td>
								<s:if test="#order.endDate!=null">
									<s:date name="#order.endDate" format="yyyy-MM-dd"/>
								</s:if>
								<s:else>
									无截止日期
								</s:else>
							</td>

                            <td>¥ ${order.bidPriceDoubleNoComma}</td>

							<td style="text-align:center;">${order.statusName}<span class="help-popup-img"
                            <s:if test="#order.status.code == 0">title="该广告订单已就绪"</s:if>
                            <s:elseif test="#order.status.code == 1">title="该广告订单正常投放"</s:elseif>
                            <s:elseif test="#order.status.code == 2">title="该广告订单已被暂停，您可以再次启用"</s:elseif>
                            <s:elseif test="#order.status.code == 3">title="该广告订单已被管理员禁用，您无法再次启用"</s:elseif>
                            <s:elseif test="#order.status.code == 4">title="该广告订单由于超出预算或最大展示数等原因，已被系统自动挂起。完成充值或修改相关设置后广告订单状态会自动变为有效"</s:elseif>
                            <s:else>title="${ad.statusName}"</s:else>></span>
							</td>
							<td style="text-align:center;">
	                            <a class="bLink orderReview"  href="${cxt_path}/admin/orders/view?id=${order.id}">查看</a>

	                            <s:if test="#order.status.code != 1">
	                                <a class="bLink" href="javascript:void(0);" onclick="operateOrder(${order.id},1)">启用</a>
	                            </s:if>
	                            <s:if test="#order.status.code != 2">
	                                <a class="bLink" href="javascript:void(0);" onclick="operateOrder(${order.id},2)">暂停</a>
	                            </s:if>
	                            <s:if test="#order.status.code != 3">
	                                <a class="bLink" href="javascript:void(0);" onclick="operateOrder(${order.id},3)">禁用</a>
	                            </s:if>
	                        </td>
	                        </td>
						</tr>
						</s:iterator>
					</s:if>
	                <s:else>
	                    <tr>
	                        <td colspan="9" class="no-data">暂无数据</td>
	                    </tr>
	                </s:else>
				</table>	
		   </div>
		   <div class="overviewPanel details" style="border-top:0px;">
			    <div class="inlineBlock panelTitle">广告列表</div>
		   </div>  
		   <div class="overview noBT">
		   		<table class="bTable ellipsis">
					<tr class="heading">
						<th>ID</th>
						<th>广告名称</th>
						<th>广告类型</th>
						<th>目标URL</th>
						<th>广告标题</th>
						<th>状态</th>
						<th style="text-align:center;">操作</th>
					</tr>
					<s:if test="#request['adEntryList'].size() > 0">
					<tr class="heading-float fixed hidden" style="top:0;"></tr>
					
					<s:iterator value="#request['adEntryList']" var="ad" status="status" >
					<tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
						<td>${ad.id}</td>
                        <td><span title="<s:property value="%{#ad.name}" escape="true"/>"><core:partOfText value="${ad.name}" textmaxlength="15" escape="true"/></span></td>
                        <td>${ad.resourceType}</td>
                        <td><a target="_blank" class="bLinkU" title="<s:property value="%{#adEntry.link}" escape="true"/>" href="<core:replaceStrings value="${ad.link}" propertiesList="[UID],,[EID],,[BACKURL]," escape="true"/>"><core:partOfText value="${ad.link}" textmaxlength="30" escape="true"/></a></td>
					    <td><span title="<s:property value="%{#ad.title}" escape="true"/>"><core:partOfText value="${ad.title}" textmaxlength="15" escape="true"/></span></td>
                        <td>${ad.statusName}<span class="help-popup-img"
                            <s:if test="#ad.status.code == 0">title="该广告已就绪"</s:if>
                            <s:elseif test="#ad.status.code == 1">title="该广告正常投放"</s:elseif>
                            <s:elseif test="#ad.status.code == 2">title="该广告已被暂停，您可以再次启用"</s:elseif>
                            <s:elseif test="#ad.status.code == 3">title="该广告已被管理员禁用，您无法再次启用"</s:elseif>
                            <s:elseif test="#ad.status.code == 4">title="该广告由于超出预算或最大展示数等原因，已被系统自动挂起。完成充值或修改相关设置后广告状态会自动变为有效"</s:elseif>
                            <s:elseif test="#ad.status.toString() == 'VERIFYING'">title="该广告正在审核中"</s:elseif>
                            <s:elseif test="#ad.status.code == 7">title="该广告未通过审核"</s:elseif>
                            <s:else>title="${ad.statusName}"</s:else>></span></td>
						<td style="text-align:center;">
						   <a class="bLink orderReview" href="${cxt_path}/admin/orders/entry/view?id=${ad.id}">查看 </a>
						   <s:if test="#ad.status.code != 6 && #ad.status.code != 7">
	                            <s:if test="#ad.status.code != 1">
	                               <a class="bLink" href="#" onclick="operate(${ad.id},1)">启用</a>
	                            </s:if>
	                            <s:if test="#ad.status.code != 2">
	                               <a class="bLink" href="#" onclick="operate(${ad.id},2)">暂停</a>
	                            </s:if>
	                            <s:if test="#ad.status.code != 3">
	                               <a class="bLink" href="#" onclick="operate(${ad.id},3)">禁用</a>
	                            </s:if>
						   </s:if>
						</td>
					</tr>
					</s:iterator>
					</s:if>
	                <s:else>
	                    <tr>
	                        <td colspan="9" class="no-data">暂无数据</td>
	                    </tr>
	                </s:else>
				</table>
		   </div>
           <div class="clear spacer10"></div>
               
           <div class="clear"></div>
            </div>
        </div>
        <div class="clear"></div>
    </div>
    <div class="clear spacer20"></div>
    <div id="city" class="city"></div>
	<div id="popWin" class="popWin" style="display:none"></div>
</div>
<div class="clear"></div>



<script type="text/javascript" charset="utf-8">
    
    function isScrolledOut($elem) {
        var docViewTop = $(window).scrollTop();
        return elemTop <= docViewTop;
    }
    $(".orderReview").mousedown(function(){
    	if(window.localStorage){
    		localStorage.setItem("scrollTop",$("body").scrollTop());
    	}
    });
    
    //广告组操作
    function operateOrder(orderId,statusCode) {
        var confirmMsg = "";
        switch (statusCode) {
            case 1:
                confirmMsg = "就绪状态将许可此广告组投放";
                break;
            case 2:
                confirmMsg = "将暂停此广告组投放，但广告主可以启用";
                break;
            case 3:
                confirmMsg = "禁用此广告组后，广告将不能投放，广告主也不可修改";
                break;
            case 4:
                confirmMsg = "是否确定挂起该广告组？余额不足时可以使用这个状态，广告主充值后，广告组将会自动启用";
                break;
            default:
                confirmMsg = "余额不足时可以用这个状态，广告主充值后，广告组将会自动启用";
        }
        if(confirm(confirmMsg)) {
            var data = {"id":orderId,"statusCode":statusCode};
            displayStatusMessage("正在修改状态...请稍候...","success");
            $.ajax({
                type: "post",
                url: "${cxt_path}/admin/orders/updateStatus",
                data:data,
                dataType:"json",
                success: function (result) {
                    if(result.success){
                        displayStatusMessage("状态修改成功", "success");
                        window.setTimeout(function(){window.location=window.location;},500);
                    } else {
                        displayStatusMessage("状态修改失败:" + result.message, "error");
                    }
                },
                error: function (msg) {
                    displayStatusMessage("操作失败" + msg, "error");
                }
            });
        }
    }
    //广告操作
    function operate(adId,status) {
        var confirmMsg = "";
        switch (status) {
          case 1:
              confirmMsg = "就绪状态将许可此广告投放";
              break;
          case 2:
              confirmMsg = "将暂停此广告投放，但广告主可以启用";
              break;
          case 3:
              confirmMsg = "禁用此广告后，广告将不能投放，广告主也不可修改";
              break;
          default:
              confirmMsg = "";
        }
        if(confirm(confirmMsg)) {
            var data = {"adEntry.id":adId,"status":status};
            $.ajax({
                type: "post",
                url: "${cxt_path}/admin/ads/adentry/operate",
                data:data,
                dataType:"json",
                success: function (result) {
                    if(result.success){
                        displayStatusMessage("状态修改成功", "success");
                        window.location.reload();
                    } else {
                        displayStatusMessage("状态修改失败:" + result.message, "error");  
                    }
                },
                error: function (msg) {
                    displayStatusMessage("操作失败" + msg, "error");
                }
            });
        }
    }

    $(function() {
    	$("#leftmenu-admin-campaignManage").addClass("active");
		$("#topmenu-admin-ads").addClass("active");
        
        
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
            document.location='${cxt_path}/admin/ads/campaign';
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
           if(localStorage.getItem("scrollTop")){
        	   $("body").animate({scrollTop:localStorage.getItem("scrollTop")});
        	   localStorage.setItem("scrollTop","0");
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
                    url: "${cxt_path}/admin/campaigns/update",
                    data:campaignData,
                    dataType:"json",
                    success: function (data) {
                        if(data.success){
                             displayStatusMessage("保存成功！", "success"); 
                             window.setTimeout(function(){
                                     window.location="${cxt_path}/admin/ads/campaign";
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
