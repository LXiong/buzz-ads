<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>创建一个新的广告</title>

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
                <div style="width: 980px" class="navCon left">
                    <table class="navTable" style="width:100%" cellpadding="0" cellspacing="0">
                        <tr>
                            <td><span><a href="${cxt_path}/advertiser/campaign/new">新建广告活动</a></span></td>
                            <td><span><a href="${cxt_path}/advertiser/group/new?campaignId=${campaignId}">新建广告组</a></span></td>
                            <td><span>新建广告</span></td>
                        </tr>
                        <tr>
                            <td><div class="spacer10"></div><div class="navBg"></div></td>
                            <td><div class="spacer10"></div><div class="navBg"></div></td>
                            <td><div class="spacer10"></div><div class="navBg"><div class="naPointer"></div></div></td>
                        </tr>
                    </table>
                    <div class="spacer20"></div>
                    <div class="infoWrap">
                        <form action="${cxt_path}/advertiser/entry/new" name="availableStatsForm" id="availableStatsForm" method="post" onsubmit="return false;">
                            <table class="viewTable" style="width:100%">
                                <tr>
                                    <td style="width:100px;"> 选择广告活动</td>
                                    <td><s:select list="%{#request.campaignSelector}"
                                            value="%{#request.campaignId}" listKey="key" listValue="value"
                                            name="campaignSelector" id="campaignSelector"
                                            cssClass="bTextbox" cssStyle="width: 250px;" headerKey="0">
                                    </s:select></td>
                                    <td style="width:100px;"> 选择广告组</td>
                                    <td><s:select list="%{#request.groupSelector}"
                                            value="%{#request.orderId}" listKey="key" listValue="value"
                                            name="groupSelector" id="groupSelector" cssClass="bTextbox"
                                            cssStyle="width: 250px;" headerKey="0">
                                        </s:select> <s:hidden name="campaignId" id="campaignId"
                                            value="%{#request.campaignId}" /> <s:hidden name="orderId"
                                            id="orderId" value="%{#request.orderId}" />
                                </tr>
                                <tr>
                                    <td colspan="2" style="padding-top: 0;"><a id="campaignInfo" class="packer orange" href="javascript:void(0)">显示广告活动信息<i></i></a></td>
                                    <td colspan="2" style="padding-top: 0;"><a id="groupInfo" class="packer orange" href="javascript:void(0)">显示广告组信息<i></i></a></td>
                                </tr>
                                <tr>
                                    <td colspan="2" style="padding: 0 6px;">
                                        <div id="campaignCon" class="label_date small" style="display:none;">
                                            <table class="viewTable">
					                        	<tr>
					                                <td>投放网络</td>
					                                <td class="orange"><s:iterator
					                                        value="#request.selectedCampaign.network" var="ntwk"
					                                        status="status">
					                                        <s:if
					                                            test="#ntwk == @com.buzzinate.buzzads.enums.AdNetworkEnum@LEZHI">乐知
					                                    </s:if>
					                                        <s:elseif
					                                            test="#ntwk == @com.buzzinate.buzzads.enums.AdNetworkEnum@BSHARE">
					                                        bShare
					                                    </s:elseif>
					                                        <s:elseif
					                                            test="#ntwk == @com.buzzinate.buzzads.enums.AdNetworkEnum@BUZZADS">
					                                        BuzzAds
					                                    </s:elseif>
                                                        <s:elseif test="#ntwk == @com.buzzinate.buzzads.enums.AdNetworkEnum@MULTIMEDIA">
                                                            富媒体
                                                        </s:elseif>
					                                    <s:else>
					                                        ??
					                                    </s:else>
					                                        <s:if test="!#status.last">,&nbsp;</s:if>
					                                    </s:iterator></td>
					                            </tr>
					                            <tr>
					                                <td style="width:60px;">出价类型</td>
					                                <td class="yellowH3">${selectedCampaign.bidType}</td>
					                                
					                            </tr>
					                            <tr>
					                            	<td>预算</td>
					                            	<td class="orange">
					                            		<span > <font style="color:#333">日预算：</font>
								                        <s:if test="selectedCampaign.maxDayBudgetDouble == 0.00 ">
								                        <font >无限制</font>
								                        </s:if>
								                        <s:else>
								                      	<font class="orange">￥${selectedCampaign.maxDayBudgetDouble }</font>
								                        </s:else>
								                        </span> , <span><font style="color:#333">总预算：</font>
								                        <s:if test="selectedCampaign.maxTotalBudgetDouble == 0.00 ">
								                        <font >无限制</font>
								                        </s:if>
								                        <s:else>
								                        <font class="orange">￥${selectedCampaign.maxTotalBudgetDouble}</font>
								                        </s:else>
								                        </span>
								                        <s:iterator value="selectedCampaign.dayBudgets" var="cd" status="status">
								                       		<div class="clear spacer10"></div>
								                       		<div>
								                       			<span style="color:#333">特定日期：<font class="yellowH3"><s:date name="#cd.budgetDay" format="yyyy-MM-dd"  /></font></span>
								                       			<span style="color:#333">预算：<font class="yellowH3">¥${cd.budgetDouble}</font></span>
								                       		</div>
								                   		</s:iterator> 
					                            	</td>
					                            </tr >
					                            
					                            <tr>
					                                <td>投放时间</td>
					                                <td class="orange"><s:date name="selectedCampaign.startDate"
					                                        format="yyyy-MM-dd" /> - <s:if
					                                        test="selectedCampaign.endDate != undefined">
					                                        <s:date name="selectedCampaign.endDate" format="yyyy-MM-dd" />
					                                    </s:if> <s:else>
					                                        <font>无截止日期</font>
					                                    </s:else></td>
					                            </tr>
					                            
					                            <tr>
					                            	<td>地理位置</td>
					                            	<td  class="orange">
					                            		<s:if test="selectedCampaign.viewLocation !='' ">
								                            <span class="orange">${selectedCampaign.viewLocation}</span>
								                        </s:if>
								                         <s:else>
								                            <font >无限制</font>
								                        </s:else>
					                            	</td>
					                            </tr>
					                        </table>
                                        </div>
                                    </td>
                                    <td colspan="2" style="padding: 0 6px;">
                                        <div id="groupCon" class="label_date small" style="display:none;">
                                            <table class="viewTable" >
					                            <tr>
					                                <td style="width:70px;">默认出价</td>
					                                <td class="orange">¥ ${selectedGroup.bidPriceDouble} / ${selectedGroup.campBidType}</td>
					                            </tr>
					                            <tr>
					                                <td>投放时间</td>
					                                <td class="orange"><s:date name="selectedGroup.startDate"
					                                        format="yyyy-MM-dd" /> - <s:if
					                                        test="selectedGroup.endDate != undefined">
					                                        <s:date name="selectedGroup.endDate" format="yyyy-MM-dd" />
					                                    </s:if> <s:else>
					                                        <font>无截止日期</font>
					                                    </s:else></td>
					                            </tr>
					                            <tr>
					                                <td>投放计划</td>
					                                <td class="orange"><font style="color: #000">特定的天：</font>${selectedGroup.viewDay}<font
					                                    style="color: #000">，时间：</font> <font id="specificTime"></font></td>
					                            </tr>
					                         	<s:if test="selectedCampaign.maxDayBudgetDouble !='0.00'">
						                            <tr>
						                                <td>投放方式</td>
						                                <td class="orange">
						                                <s:if test="selectedGroup.adsType.code == 0">
							                        		均匀投放
							                        	</s:if>
							                        	<s:elseif test="selectedGroup.adsType.code == 1">
							                        		加速投放
							                        	</s:elseif>
						                                </td>
						                            </tr>
					                            </s:if>
					                            <tr>
					                                <td>频次上限</td>
					                                <td class="orange">
					                                	<s:if test="selectedGroup.frequencyCap != 0 && selectedGroup.orderFrequency != 0">
							                        		${selectedGroup.frequencyCap} 每广告组 / 天
							                        	</s:if>
							                        	<s:elseif test="selectedGroup.frequencyCap != 0 && selectedGroup.entryFrequency != 0">
							                        		${selectedGroup.frequencyCap} 每广告 / 天
							                        	</s:elseif>
							                        	<s:else>
							                        		<font >无限制</font>
							                        	</s:else>
					                                	
					                                </td>
					                            </tr>
					                            
					                            <tr>
					                                <td>关键词</td>
					                                <td class="orange"><s:if
					                                        test="selectedGroup.keywords !='' ">
					                                        <span title="${selectedGroup.keywords}"><core:partOfText
					                                                value="${selectedGroup.keywords}" textmaxlength="100"
					                                                escape="true" /></span>
					                                    </s:if> <s:else>
					                                        <font >无限制</font>
					                                    </s:else></td>
					                            </tr>
					                        </table>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="clear"></div>
                </div>
                
                <div class="clear spacer15"></div>
                <div class="left" style="width:600px;">
                    <table class="viewTable" style="width: 100%">
                         <tr>
                             <td style="width: 100px;">广告名称</td>
                             <td><input class="validator bTextbox" required="required"
                                 maxlength="50" type="text" id="name" name="entryName" value=""
                                 style="width: 305px;" placeholder="不能超过50个字符" /><span
                                 class="primary mSpacer_left10" id="name_msg"></span></td>
                         </tr>


                            	 <s:if  test="#ntwk != @com.buzzinate.buzzads.enums.AdNetworkEnum@MULTIMEDIA">
                                 <tr>
                                     <td>广告类型</td>
                                     <td>
		                             <s:select list="#{0:'纯文字',1:'图文'}"
		                                     value="%{#request.resourceType}" listKey="key"
		                                     listValue="value" name="resourceType" cssClass="bTextbox"
		                                     cssStyle="width: 100px;" headerKey="0">
	                                 </s:select>
                                     <s:hidden name="position" value="9"/>
                                     </td>
                                 </tr>
	                             </s:if> 
	                             <s:else>
                                 <tr>
                                     <td>广告类型</td>
                                     <td>
                                         <s:select list="#{2:'Flash'}"
                                                   value="2" listKey="key"
                                                   listValue="value" name="resourceType" cssClass="bTextbox"
                                                   cssStyle="width: 100px;" headerKey="0">
                                         </s:select>
                                     </td>
                                 </tr>
                                 <tr>
                                    <td>展示位置</td>
                                    <td>
	                             	<s:select list="#{1:'右下浮窗'}"
		                                     value="%{#request.position}" listKey="key"
		                                     listValue="value" name="position" cssClass="bTextbox"
		                                     cssStyle="width: 100px;" headerKey="0">
	                                 </s:select>
                                     </td>
                                 </tr>
	                             </s:else>  
                          <s:if  test="#ntwk != @com.buzzinate.buzzads.enums.AdNetworkEnum@MULTIMEDIA">
	                         <tr>
	                             <td>广告标题</td>
	                             <td>
	                                 <div>
	                                     <input placeholder="请输入标签..." class="bTextbox"
	                                         style="width: 305px;" type="text" id="headline"
	                                         name="entryTitle" value="" /><span
	                                         class="primary mSpacer_left10" id="headline_msg"></span>
	                                 </div>
	                             </td>
	                         </tr>
	                         <tr style="display: none">
	                             <td style="vertical-align: top;">描述</td>
	                             <td>
	                                 <div class="">
	                                     <textarea name="description" id="descriptLine" cols="28"
	                                         rows="5" placeholder="请输入广告描述"
	                                         style="overflow: auto; resize: none;"></textarea>
	                                     <span class="primary mSpacer_left10" id="descript_msg"
	                                         style="vertical-align: top;"></span>
	                                 </div>
	                             </td>
	                         </tr>
	                         <tr id="resourceUrlImgCon" style="display: none">
	                             <td style="vertical-align: top;">广告图片</td>
	                             <td>
	                                 <div id="imgCon" class="left">
	                                     <img id="resourceUrlImg" alt="广告图片"
	                                         src="<s:property value="%{#request.adEntry.resourceUrl}" escape="true"/>"
	                                         width="100" height="100">
	                                 </div>
                                     <div class="left" style="margin-left:15px;">
                                     	<div id="uploadModePanel">
                                         <label>
                                             <input value="local"  style="margin:0px;"  type="radio" name="uploadMode" alt="0"   checked="true" />
                                             <span class="mSpacer_left10">上传</span>
                                         </label>
                                         <label>
                                             <input value="remote"  style="margin:0px;"  type="radio" name="uploadMode" alt="1"  />
                                             <span class="mSpacer_left10">远程</span>
                                         </label>
                                         <div style="margin-top:5px; display:none;" id="remote_dom" >
                                             <input placeholder="远程地址" class="bTextbox"
                                                    style="width: 190px;" type="text" id="remoteResourceUrl" />
		                                     <div class="spacer10"></div>               
                                             <span style="font-size:16px; display:block;" class="primary " id="remote_imgUrl_msg"></span>       
                                         </div>
                                     </div>
	                                 <div id="uploadPanel" style="font-size:12px;margin-top:5px;">
	                                     <div>图片格式：jpg，png，jpeg，gif</div>
	                                     <div class="spacer5"></div>
	                                     <div >最佳尺寸：120像素 x 120像素</div>
	                                     <div class="spacer5"></div>
	                                     <div >图片大小：小于30K</div> 
	                                     <div style="margin-top: 5px;">
		                                     <input class="bButton"
		                                             style="vertical-align: text-bottom;" type="button"
		                                             id="imageFileUpload" value="上传" />
		                                     <span style="font-size:16px;" class="primary mSpacer_left10" id="imgUrl_msg"></span>
		                                 </div>
                                     </div>
                                    </div> 
	                             </td>
	                         </tr>
	                         <tr style="display: none;">
	                             <td>显示网址</td>
	                             <td>
	                                 <div class="">
	                                     <input type="text" value="lezhi.me" /> <span
	                                         class="primary mSpacer_left10" id="display_msg"></span>
	                                 </div>
	                             </td>
	                         </tr>
                             <tr id="materialSize">
                                 <td>素材尺寸</td>
                                 <td>
                                     <s:select id="sizeSelector" list="%{bannerSizeSelector}" value="%{size}" cssClass="bTextbox" cssStyle="width: 100px;" />
                                 </td>
                             </tr>
	                         
	                         
	                      </s:if>
	                      <s:else>
	                      		<tr>
	                             <td style="vertical-align:top;">广告素材</td>
	                             <td style="white-space: nowrap;">
                                     <div id="resourceUrlFlash" class="left">
                                         <object type="application/x-shockwave-flash" data="<s:property value="%{#request.adEntry.resourceUrl}" escape="true"/>" width="100" height="100" id="vcastr3"  style="border: 1px solid #ccc;">
                                         <param name="movie" value="<s:property value="%{#request.adEntry.resourceUrl}" escape="true"/>"/>
                                         <param name="allowFullScreen" value="false" />
                                         <param name="wmode" value="transparent"/>
                                         </object>
                                     </div>
	                                <div class="left" style="margin-left:15px;">
	                                	 <div id="uploadModePanel">
		                                    <label>
				                    	        <input  style="margin:0px;"  type="radio" name="uploadMode" value="local" alt="0"  checked="true" />
				                    	        <span class="mSpacer_left10">上传</span>
				                    	    </label>
				                    	    <label>
				                    	        <input  style="margin:0px;"  type="radio" name="uploadMode" value="remote"  alt="1"  />
				                    	        <span class="mSpacer_left10">远程</span>
				                    	    </label>
				                    	    <div style="margin-top: 5px; display:none;" id="remote_dom" >
				                    	    	 <input placeholder="远程地址" class="bTextbox"
		                                         style="width: 190px;" type="text" id="remoteResourceUrl"
		                                         name="entryTitle"/>
		                                         <div class="spacer10"></div>
		                                         <span style="font-size:16px;display:block;" class="primary " id="remote_flashUrl_msg"></span>
				                    	    </div>
		                                 </div>
	                                     <div id="uploadPanel"  style="font-size:12px;margin-top: 5px;" id="">
	                                         <div>富媒体格式：swf</div>
	                                         <div class="spacer5"></div>
	                                         <div >swf大小：小于512K</div>
	                                         <div class="spacer10"></div>
	                                         <input class="bButton"
	                                                style="vertical-align: text-bottom;" type="button"
	                                                id="flashFileUpload" value="上传" />
	                                         <span style="font-size:16px;" class="primary mSpacer_left10" id="flashUrl_msg"></span>
	                                     </div>
	                                </div>
	                             </td>
	                         </tr>
                             <tr id="materialSize">
                                  <td>素材尺寸</td>
                                  <td>
                                      <s:select id="sizeSelector" list="%{mmSizeSelector}" value="%{size}" cssClass="bTextbox" cssStyle="width: 100px;" />
                                  </td>
                              </tr>
                             
	                      </s:else>
	                      <tr>
	                             <td>目标网址</td>
	                             <td>
	                                 <div>
	                                     <s:select list="#{1:'http://',2:'https://'}" value="1"
	                                         listKey="key" listValue="value" name="agreement"
	                                         cssClass="bTextbox" cssStyle="width: 100px;" headerKey="0">
	                                     </s:select>
	                                     <input placeholder="请输入目标网址..." class="bTextbox" style="width: 200px;" type="text" id="desturl" value="" />
	                                     <input type="hidden" id="entry_link" name="link" /> <span class="primary mSpacer_left10" id="desturl_msg"></span>
	                                 </div>
	                             </td>
	                       </tr>
	                       <tr>
                                  <td></td>
                                  <td>
                                      <div class="spacer30"></div>
                                      <input id="submitSave" class="bButton center lightOrange" type="submit" value="保存并新建广告" />
                                      <input id="submit" class="bButton center" style="margin-left:20px;" type="submit" value="保存" />
                                      <input class="bButton center" style="margin-left:20px;" type="button" value="取消" onclick="document.location='${cxt_path}/advertiser/group/${orderId}/entries';" />
                                  </td>
                              </tr>
	                      
                        <input id="resourceUrl" name="resourceUrl"
                               type="hidden" class="bTextbox"
                               value="<s:property value="%{#request.adEntry.resourceUrl}" escape="true"/>" />
                         <s:hidden name="campaignId" id="campaignId"
                             value="%{#request.campaignId}" />
                         <s:hidden name="orderId" id="orderId"
                             value="%{#request.orderId}" />
                     </table>
                </div>
                   
                <div class="left">
                    <div class="spacer20"></div>
                    <ul class="adPreview">
                        <li class="heading4 orange">广告预览:</li>
                        <li>
                            <div class="clear spacer10"></div>
                            <s:if  test="#ntwk == @com.buzzinate.buzzads.enums.AdNetworkEnum@LEZHI">
                            <div>乐知</div>
                            </s:if>
                            <s:elseif test="#ntwk == @com.buzzinate.buzzads.enums.AdNetworkEnum@MULTIMEDIA">
                            <div>富媒体</div>
                            </s:elseif>
                            <s:else>
                                ??
                            </s:else>
                            <div class="clear spacer10"></div>
                            <s:if  test="#ntwk != @com.buzzinate.buzzads.enums.AdNetworkEnum@MULTIMEDIA">
                            <div id="preview_text">
                                <span class="yellowH3">文本</span>
                                <div class="previewCon">
                                    <ul class="textCon">
                                        <li><a class="bLink descriptLine"
                                            style="display: inline-block; vertical-align: middle; width: 300px;"
                                            href="javascript:void(0)" target="_Blank"><span
                                                style="color: #ccc">广告描述</span></a></li>
                                    </ul>
                                </div>
                            </div>

                            <div id="preview_imgText" style="display: none;">
                                <span class="yellowH3">图文</span>
                                <div class="previewCon">
                                    <ul class="textImgCon">
                                        <li><a href="javascript:void(0)" id="imglink"
                                            target="_Blank">
                                                <div>
                                                    <img id="imgDisplay" alt="广告图片"
                                                        style="width: 100px; height: 100px" />
                                                </div>
                                                <div class="clear spacer5"></div>
                                                <div class="descriptLine"
                                                    style="width: 100px; word-wrap: break-word; word-break: normal;"></div>
                                        </a></li>
                                    </ul>
                                </div>
                            </div>
                            </s:if>
                            <s:else>
                            <div id="preview_flash">
                                <span class="yellowH3">富媒体</span>
                                <div class="previewCon">
                                    <ul class="textImgCon">
                                        <li><a href="javascript:void(0)" id="imglink"
                                               target="_Blank">
                                            <div id="flashDisplay">

                                            </div>
                                            <div class="clear spacer5"></div>
                                            <div class="descriptLine"
                                                 style="width: 100px; word-wrap: break-word; word-break: normal;"></div>
                                        </a></li>
                                    </ul>
                                </div>
                            </div>
                            </s:else>
                        </li>
                    </ul>

                </div>
                <div class="clear spacer50"></div>
            </div>
        </div>
        <div class="clear spacer20"></div>

    </div>
</div>
<link rel="stylesheet" href="${css_path}/calendar-date.css"     type="text/css" />
<script type="text/javascript" charset="utf-8"  src="${js_path}/tools/jquery.tools.forms.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8"  src="${js_path}/libs/ajaxupload.js"></script>
<script type="text/javascript">


    $(function() {
    	
    	
    	$(".packer").toggle(function () {
            var i = $(this).find("i");
            i.addClass(" selected");
        },function(){
        	var i = $(this).find("i");
        	i.removeClass(" selected");
        });
        
        // show campaign info
        $("#campaignInfo").toggle(function(){$("#campaignCon").show();},function(){ $("#campaignCon").hide();});
        $("#groupInfo").toggle(function(){ $("#groupCon").show();},function(){$("#groupCon").hide();});

        var imageButton = $("#imageFileUpload")[0];
        var flashButton = $("#flashFileUpload")[0];
        function init() {
            RC.CutSelect("campaignSelector");
            RC.CutSelect("groupSelector");
            var adOrderTime="${selectedGroup.scheduleTimeStr }";
            if(!flashButton){
            	 $("#materialSize").hide();
            }
         	if(adOrderTime !=""){
         		var timeTemp=adOrderTime.split("-");
         		$("#specificTime").html(timeTemp[0]+" 至 "+timeTemp[1] + "");
         	}else{
         		$("#specificTime").html("无投放时间限制");
         	}
        }
        init();

        //set paramete
        var dataTemp = {};
        dataTemp.campaignId=${request.campaignId};
        dataTemp.orderId=${request.orderId};

        // create image file uploader:
        var originalFile = $('<div/>').text("${request.adEntry.resourceUrl}")
                .html();

        $("#sizeSelector").change(function(){
			var size=($(this).val()).match(/\d+/g);
			if(imageButton){
	            $("#imgDisplay").attr("style","width:"+size[0]+"px;height:"+size[1]+"px;");
			}else{
				$("#flashDisplay").attr("style","width:"+size[0]+"px;height:"+size[1]+"px;");
			}
		});
        function buildFlash(o) {
        	var item={};
           	if( (typeof o) !="object"){
           		item.width='100';
            	item.height='100';
            	item.url=o;
           	}else{
           		var size=(o.size).match(/\d+/g);	
           		item=o;
           		item.width=size[0];
           		item.height=size[1];
           	}
        	return "<object type='application/x-shockwave-flash' data='" + item.url + "' width='"+item.width+"' height='"+item.height+"' bgColor='#ffffff' style='border:1px solid #ccc;'>" +
                            "<param name='movie' value='" + item.url + "'/>" +
                            "<param name='allowFullScreen' value='false'/>" +
                            "</object>";
        };
        //remote url function
        function getResourceUrl(){
        	var tempUrl = $("#remoteResourceUrl").val();
            if(tempUrl !="" ){
            	dataTemp.resourceUrl=tempUrl;
            	dataTemp.isImgUrl = true;
            	$("#remote_imgUrl_msg").html("");
                $("#resourceUrlImg").attr("src", dataTemp.resourceUrl);
                var size=($("#sizeSelector").val()).match(/\d+/g);
                $("#imgDisplay").attr("src", tempUrl).attr("style","width:"+size[0]+"px;height:"+size[1]+"px;");
                $("#remote_flashUrl_msg").html("");
                $("#flashDisplay").html(buildFlash({url:tempUrl,size:$("#sizeSelector").val()}));
                $("#resourceUrlFlash").html(buildFlash(tempUrl));
            }else{
            	dataTemp.resourceUrl="";
            	$("#remote_imgUrl_msg").html("请输入图片远程链接！");
            	$("#remote_flashUrl_msg").html("请输入富媒体远程链接！");
            	dataTemp.isImgUrl = false;
            }
        }
        $("#remoteResourceUrl").blur(function(){
        	getResourceUrl();
        });
        if (imageButton) {
        new AjaxUpload("#imageFileUpload", {
            action : '${cxt_path}/advertiser/entry/upload',
            autoSubmit : true,
            name : 'tempFile',
            responseType : "json",
            onChange : function(file, extension) {
            },
            onSubmit : function(file, extension) {
                hideStatusMessage();
                if (!(extension && /^(jpg|png|jpeg|gif)$/i.test(extension))) {
                    displayStatusMessage('请选择正确的图片格式!', "error");
                    return false; // cancel upload
                }
                this.setData({
                    'fileName' : file,
                    'fileNameExtension' : extension
                });
            },
            onComplete : function(file, response) {
                if (response.success) {
                    $("#resourceUrl").val(response.contents.resourceUrl);
                    $("#resourceUrlImg")
                            .attr("src", response.contents.resourceUrl);
                    var size=($("#sizeSelector").val()).match(/\d+/g);
                    $("#imgDisplay").attr("src",response.contents.resourceUrl).attr("style","width:"+size[0]+"px;height:"+size[1]+"px;");
                    $("#errorMsg").hide();
                    dataTemp.resourceUrl = response.contents.resourceUrl;
                    dataTemp.isImgUrl = true;
                    $("#imgUrl_msg").html("");
                    displayStatusMessage("图片上传成功,请点击保存按钮,将其保存.", "success");

                } else {
                    dataTemp.isImgUrl = false;
                    $("#imgUrl_msg").html(response.message);
                    displayStatusMessage(response.message, "error");
                }
            }
        });}
        if (flashButton){
            new AjaxUpload("#flashFileUpload", {
                action : '${cxt_path}/advertiser/entry/upload',
                autoSubmit : true,
                name : 'tempFile',
                responseType : "json",
                onChange : function(file, extension) {
                },
                onSubmit : function(file, extension) {
                    hideStatusMessage();
                    if (!(extension && /^(swf)$/i.test(extension))) {
                        displayStatusMessage('富媒体仅支持swf格式!', "error");
                        return false; // cancel upload
                    }
                    this.setData({
                        'fileName' : file,
                        'fileNameExtension' : extension
                    });
                },
                onComplete : function(file, response) {
                    if (response.success) {
                        var flashUrl = response.contents.resourceUrl;
                        var flashEmbedCode = buildFlash({url:flashUrl,size:$("#sizeSelector").val()});
                        $("#resourceUrl").val(flashUrl);
                        $("#resourceUrlFlash")
                                .html(buildFlash(flashUrl));
                        $("#flashDisplay").html(flashEmbedCode);
                        $("#errorMsg").hide();
                        dataTemp.resourceUrl = flashUrl;
                        dataTemp.isImgUrl = true;
                        $("#flashUrl_msg").html("");
                        displayStatusMessage("富媒体上传成功,请点击保存按钮,将其保存.", "success");
                    } else {
                        dataTemp.isImgUrl = false;
                        $("#flashUrl_msg").html(response.message);
                        displayStatusMessage(response.message, "error");
                    }
                }
            });}

        var $uploadPanel = $("#uploadPanel");
        var $remoteResourceUrl = $("#remote_dom");
        $("#uploadModePanel input[name='uploadMode']").change(function(){
            $remoteResourceUrl.val("");
            if ($(this).val() == "remote") {
                $uploadPanel.hide();
                $remoteResourceUrl.show();
            } else {
                $uploadPanel.show();
                $remoteResourceUrl.hide();
            }
        });

        $("#revertImage").click(function() {
            $("#resourceUrl").val(originalFile);
            $("#resourceUrlImg").attr("src", originalFile);
        });
        $("#resourceUrl").blur(function() {
            $("#resourceUrlImg").attr("src", $(this).val());
        });

        //ad type choose img
        $("#resourceType").change(function() {
            var type = $("#resourceType").val();
            switch (type) {
            case "0":
                $("#preview_text").show();
                $("#resourceUrlImgCon").hide();
                $("#preview_imgText").hide();
                $("#materialSize").hide();
                break;
            case "1":
                $("#resourceUrlImgCon").show();
                $("#preview_imgText").show();
                $("#materialSize").show();
                break;
            }
            setPreview();
        });
        //ad type choose text 
        function getUrl() {
            var agreement = $("#agreement  option:selected").text();
            var url = $("#desturl").val();
            return agreement + url;
        }

        function setPreview() {
            var adtype = $("#resourceType  option:selected").val();
            var headlinestr = RC.stripscript($.trim($("#headline").val()))
            if (adtype == 0) {
                $(".descriptLine").html(headlinestr).attr("href", getUrl())
                        .attr("title", headlinestr);
            } else if (adtype == 1) {
                $(".descriptLine").html(headlinestr).attr("href", getUrl()).attr("title", headlinestr);
                $("#imglink").attr("href", getUrl());
            }
        }
        function setUrl() {
            var adtype = $("#resourceType  option:selected").val();
            if (adtype == 0) {
                $(".descriptLine").attr("href", getUrl());
            } else if (adtype == 1) {
                $(".descriptLine").attr("href", getUrl());
                $("#imglink").attr("href", getUrl());
            }
        }
        setPreview();
        //descriptLine 
        /*$("#descriptLine").blur(function(){
            setPreview();
        });*/
        $("#agreement").change(function() {
            setUrl();
        })
        $("#desturl").blur(function() {
            setUrl();
        })

        // init date pickers
        var fDay = 0;
        if ('zh' == "zh") {
            $.tools.dateinput.localize("zh", {
                months : "一月,二月,三月,四月,五月,六月,七月,八月,九月,十月,十一月,十二月",
                shortMonths : "一,二,三,四,五,六,七,八,九,十,十一,十二",
                days : "星期日,星期一,星期二,星期三,星期四,星期五,星期六",
                shortDays : "周日,周一,周二,周三,周四,周五,周六"
            });
            fDay = 1;
        } else if ('zh' == "zh_TW") {
            $.tools.dateinput.localize("zh_TW", {
                months : "一月,二月,三月,四月,五月,六月,七月,八月,九月,十月,十一月,十二月",
                shortMonths : "一,二,三,四,五,六,七,八,九,十,十一,十二",
                days : "星期日,星期一,星期二,星期三,星期四,星期五,星期六",
                shortDays : "周日,周一,周二,周三,周四,周五,周六"
            });
            fDay = 1;
        }

        // temp fix for dateinput incompatibility with ie6 and ie7
        //if (!($.browser.msie && ($.browser.version=="6.0" || $.browser.version=="7.0"))) {
        $(":date").dateinput({
            trigger : true,
            format : 'yyyy-mm-dd',
            lang : 'zh',
            firstDay : fDay
        });

        //choose campaign
        $("#topmenu-advertiser-adCampaign").addClass("active");
        $("#leftmenu-advertiser-quicklyCreate").addClass("active");

        //
        var item = $("#chooseType").find("input:radio");
        item.click(function() {
            if ($(this).attr("alt") == 0) {
                $("#weekly").hide();
            } else if ($(this).attr("alt") == 1) {
                $("#weekly").show();
            }
        });
        var times = $('#timeType').find("input:radio");
        times.click(function() {
            if ($(this).attr("alt") == 0) {
                $("#time").hide();
            } else if ($(this).attr("alt") == 1) {
                $("#time").show();
            }
        });

        //form group
        $("#bid_nolimit").click(function() {
            if (($(this)[0]).checked) {
                $("#bid").attr("disabled", "true");
            } else {
                $("#bid").removeAttr("disabled");
            }
        });
        $("#max_nolimit").click(function() {
            if (($(this)[0]).checked) {
                $("#max_validator").attr("disabled", "true");
            } else {
                $("#max_validator").removeAttr("disabled");
            }
        });

        //date click
        $("#now").click(
                function() {
                    if ($(this)[0].checked) {
                        var d = new Date();
                        var date = d.getFullYear() + "-" + (d.getMonth() + 1)
                                + "-" + d.getDate();
                        $("#datePickerStart").data("dateinput").setValue(date);
                        $("#datePickerStart")[0].disabled = "true";
                    } else {
                        $("#datePickerStart").removeAttr("disabled");
                    }
                });
        $("#noEndDate").click(function() {
            if ($(this)[0].checked) {
                $("#datePickerEnd")[0].disabled = "true";
            } else {
                $("#datePickerEnd").removeAttr("disabled");
            }
        });

        //mask
        function maskShow() {
            var _hei = $(document).height();
            $("#maskDiv").css({
                height : _hei + "px"
            });
            $("#maskDiv").show();
        }

        function maskHide() {
            $("#maskDiv").hide();
        }
        //action function here
        $("#closeBtn").live("click", function() {
            $("#adsWindowPop").removeClass("showPop");
            $("select").show();
            maskHide();
            $(".error").hide();
        });

        function getName() {
            if ($("#name").val() != "" && $("#name").val().length <= 50) {
                dataTemp.entryName = RC.stripscript($.trim($("#name").val()));
                $("#name_msg").html("");
                dataTemp.isName = true;
            } else if ($("#name").val() == "") {
                $("#name_msg").html("请输入广告名称");
                dataTemp.isName = false;
            } else if ($("#name").val().length > 50) {
                $("#name_msg").html("名称不能大于50个字");
                dataTemp.isName = false;
            }
        }

        function getHeadline() {
            var headline = $("#headline").val();
            if (headline !== "") {
                dataTemp.entryTitle = RC.stripscript($.trim(headline));
                $("#headline_msg").html("");
                dataTemp.isHeadline = true;
            } else {
                $("#headline_msg").html("请输入标题");
                dataTemp.isHeadline = false;
            }
        }

        function getDescriptLine() {
            return;
            var desc = $("#descriptLine").val();
            if (desc != "") {
                dataTemp.descriptLine = headline;
                $("#descript_msg").html("");
                dataTemp.isDescriptLine = true;
            } else {
                $("#descript_msg").html("请输入广告描述");
                dataTemp.isDescriptLine = false;
            }
        }

        function getDesturl() {
            var agreement = $("#agreement  option:selected").text();
            var desturl = $("#desturl").val();
            if (desturl !== "") {
                var url = RC.isURL(agreement + desturl);
                if (url) {
                    dataTemp.link = agreement + desturl;
                    dataTemp.agreement=$("#agreement  option:selected").val();
                    $("#desturl_msg").html("");
                    $("#entry_link").val(dataTemp.link);
                    dataTemp.isDesturl = true;
                } else {
                    $("#desturl_msg").html("请输入正确的地址");
                    dataTemp.isDesturl = false;
                }

            } else {
                $("#desturl_msg").html("请输入目标地址");
                dataTemp.isDesturl = false;
            }
        }

        $("#name").blur(function() {
            getName();
        });

        $("#headline").blur(function() {
            getHeadline();
            setPreview();
        });

        /*
            $("#descriptLine").blur(function(){
                getDescriptLine();      
            });
         */

        $("#desturl").blur(function() {
            getDesturl();
        });

        //switch stats
        $("#campaignSelector").change(function() {
            $("#campaignId").val($("#campaignSelector").val());
            $("#orderId").val(0);
            $("#availableStatsForm").submit();

        });

        $("#groupSelector").change(function() {
            $("#campaignId").val($("#campaignSelector").val());
            $("#orderId").val($("#groupSelector").val());
            $("#availableStatsForm").submit();
        });
        
        
        var isSubmitting = false;
        function submit(isSave){
            getName();
            var adtype = $("#resourceType  option:selected").val();
            dataTemp.resourceType = adtype;
            getHeadline();
            //getDescriptLine();
            var $resourceType = $("#resourceType").val();
            getDesturl();
            dataTemp.size = $("#sizeSelector").val();
           
            if (!dataTemp.isImgUrl &&  $resourceType== "1") {
                $("#imgUrl_msg").html("请上传广告图片或提供链接");
                dataTemp.isImgUrl = false;
                return false;
            } else if (!dataTemp.resourceUrl && $resourceType == "2") {
                $("#flashUrl_msg").html("请上传富媒体或提供链接");
                dataTemp.isImgUrl = false;
                return false;
            }else{
            	dataTemp.isImgUrl = true;
            } 
            if( $("#uploadModePanel input[name='uploadMode']:checked").attr("alt")==1){
         	   getResourceUrl();
            }
            if ($resourceType == 2) {
                dataTemp.position = $("#position option:selected").val();
            }
            if (dataTemp.isName && dataTemp.isHeadline && dataTemp.isImgUrl && dataTemp.isDesturl && dataTemp.size) {
                if (isSubmitting) return;
                isSubmitting = true;
                $.ajax({
                    type: "post",
                    url: "${cxt_path}/advertiser/entry/createAction",
                    data:dataTemp,
                    dataType:"json",
                    success: function (data) {
                        if(data.success) {
                            if(isSave){
                                displayStatusMessage("保存成功！", "success"); 
                                 window.setTimeout(function(){
                                     window.location="${cxt_path}/advertiser/group/"+${request.orderId}+"/entries";  
                                    }, 50); 
                                }else{
                                    displayStatusMessage("保存成功并继续新建广告！", "success"); 
                                    window.setTimeout(function(){
                                        window.location="${cxt_path}/advertiser/entry/new?campaignId="+ ${request.campaignId} +"&orderId="+${request.orderId};  
                                    }, 50); 
                                }                       
                            } else {
                                displayStatusMessage(data.message, "error");
                                isSubmitting = false;
                            }
                        },
                        error: function (msg) {
                            displayStatusMessage("创建广告失败", "error");
                            isSubmitting = false;
                        }
                    });
            }
        }
        $("#submit").click(function(){
            submit(true); 
        });
        $("#submitSave").click(function(){
            submit(false);
        })
        

    });
</script>

