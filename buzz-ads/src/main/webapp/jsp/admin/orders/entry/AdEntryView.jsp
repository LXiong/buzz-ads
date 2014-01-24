<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>Ad Entry - View</title>

<div class="container-body">
    <div class="spacer20"></div>
    
    <div>
        <%@ include file="/jsp/admin/orders/LeftMenu.jsp"%>
        
        <div class="container-right">
           
            <div class="overview bc">
            		<p class="breadcrumb">
	                    <a class="bLinkD" style="padding-left:15px;" href="${cxt_path}/admin/ads/campaign">活动管理</a>
	                    &gt;&gt;
	                    <a class="bLinkD" href="${cxt_path}/admin/campaigns/view?id=${adCampaign.id}">广告活动  - <core:partOfText value="${adCampaign.name}" textmaxlength="30" escape="true"/></a>
	                    &gt;&gt;
	                    <a class="bLinkD" href="${cxt_path}/admin/orders/view?id=${adOrder.id}">广告组 - <core:partOfText value="${adOrder.name}" textmaxlength="30" escape="true"/></a>
	                    &gt;&gt;
	                    <a class="orange" href="javascript:void(0)">广告 - <core:partOfText value="${adEntry.name}" textmaxlength="30" escape="true"/></a>
                    </p> 
            </div>
            <div class="spacer10"></div>	
	          <div class="overviewPanel details" style="border-top:1px solid #ddd;">
	   	    	<div class="inlineBlock panelTitle">广告编辑</div>
	   	    </div>  
	   	    <div style="display:none;">
				<s:iterator
                   value="#request.adCampaign.network" var="ntwk"
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
               </s:iterator>
			</div>
	         <s:if test="%{#request.adEntry != null && #request.adEntry.id > 0}">
	           <div class="overview" style="border-top:0px;">
	            <div style="width:770px" class="navCon left">
	                <table class="viewTable" style="width:100%">
	                    <tr>
	                        <td style="width:100px">名称</td>
	                        <td><input class="validator bTextbox"  required="required" maxlength="50" type="text" id="name" name="entryName" value="${adEntry.name}" placeholder="不能超过50个字符"/><span class="primary mSpacer_left10" id="name_msg"></span></td>
	                    </tr>
	                    <tr>
	                         <td>广告状态</td>
	                        <td>
	                            <s:if test="adEntry.status.code == 1 || adEntry.status.code == 2 ">
	                                <s:select list="#{1:'启用',2:'暂停'}" value="adEntry.status.code" listKey="key" listValue="value" name="entryStatus" cssClass="bTextbox" headerKey="0">
	                                </s:select>
	                            </s:if>
	                            <s:else>
	                                <s:select list="#{1:'启用',2:'暂停',3:'禁止',6:'待审核',7:'未通过'}" value="adEntry.status.code" disabled="true" listKey="key" listValue="value" name="entryStatus" cssClass="bTextbox" headerKey="0" headerValue="---请选择---">
	                                </s:select>
	                            </s:else>
	                        </td>
	                    </tr>
	                    <s:if  test="#request.adEntry.resourceType.code==0||#request.adEntry.resourceType.code==1">
		                    <tr>
		                        <td>广告类型</td>
		                        <td>
		                            <s:select list="#{0:'纯文字',1:'图文'}"  value="%{#request.adEntry.resourceType.code}" listKey="key" listValue="value" name="resourceType"   cssClass="bTextbox" cssStyle="width: 100px;" headerKey="0" >
		                            </s:select>
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
                             value="%{#request.adEntry.position.code}" listKey="key"
                             listValue="value" name="position" cssClass="bTextbox"
                             cssStyle="width: 100px;" headerKey="0">
                        </s:select>
                           </td>
                       </tr>
                    </s:else>
                    <s:if  test="#request.adEntry.resourceType.code==0||#request.adEntry.resourceType.code==1">
	                     <tr>
	                        <td>广告标题</td>
	                        <td>
	                            <div><input class="bTextbox" type="text" id="headline" name="entryTitle" value="${adEntry.title}"   /><span class="primary mSpacer_left10" id="headline_msg"></span></div>
	                        </td>
	                    </tr>
	                    <tr style="display:none">
	                        <td style="vertical-align: top;">描述</td>
	                        <td>
	                            <div class="">
	
	                                <textarea name="description"  id="descriptLine" cols="28" rows="5" placeholder="请输入广告描述" style="overflow:auto;resize: none;" ></textarea>
	
	                                <span class="primary mSpacer_left10" id="descript_msg" style="vertical-align: top;"></span>
	                            </div>
	                        </td>
	                    </tr>
	                    <tr id="resourceUrlImgCon" style="display:none" >
	                            <td style="vertical-align:top;">广告图片</td>
	                            <td>
	                                 <div id="imgCon" class="left">
	                                     <img id="resourceUrlImg" alt="广告图片"
	                                         src="<s:property value="%{#request.adEntry.resourceUrl}" escape="true"/>"
	                                         width="100" height="100">
	                                 </div>
                                     <div class="left" style="margin-left:15px;">
                                     	<div id="uploadModePanel">
                                         <label>
                                             <input value="local"  style="margin:0px;"  type="radio" name="uploadMode"  alt="0" checked="true" />
                                             <span class="mSpacer_left10">上传</span>
                                         </label>
                                         <label>
                                             <input value="remote"  style="margin:0px;"  type="radio" name="uploadMode"  alt="1"  />
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
	                    <tr style="display:none;">
	                        <td>显示网址</td>
	                        <td>
	                            <div class="">
	                                <input type="text" value="lezhi.me" />
	                                <span class="primary mSpacer_left10" id="display_msg"></span>
	                            </div>
	                        </td>
	                    </tr>
	                    <tr id="materialSize">
                        	<td>素材尺寸</td>
	                        <td>
	                            <s:select id="sizeSelector" list="%{bannerSizeSelector}" value="%{adEntry.size}" cssClass="bTextbox" cssStyle="width: 100px;" />
	                        </td>
	                    </tr>
	                     
	                    </s:if>
                   		<s:else>
                   		<tr>
	                             <td style="vertical-align:top;">广告素材</td>
	                             <td>
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
				                    	    <div style="margin-top: 5px;" id="remote_dom" style="display:none;">
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
                                      <s:select id="sizeSelector" list="%{mmSizeSelector}" value="%{adEntry.size}" cssClass="bTextbox" cssStyle="width: 100px;" />
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
	                        <input id="submit" class="bButton center lightOrange" type="submit" value="保存"  />
	                        <input class="bButton center" style="margin-left:20px;" type="button" value="返回" onclick="history.go(-1)" /></td>
	                    </tr>
	                    <s:hidden name="campaignId" id="campaignId" value="%{#request.adEntry.campaignId}" />
	                    <s:hidden name="orderId" id="orderId" value="%{#request.adEntry.orderId}" />
	                    <s:hidden name="id" id="entryId" value="%{#request.adEntry.id}" />
	                </table>
	            </div>
            <div class="left">
                <div class="spacer20"></div>
                    <ul class="adPreview">
                        <li class="heading4 orange">广告预览:</li>
                        <li>
                            <div class="clear spacer10"></div>
                            <s:if  test="#request.adEntry.resourceType.code==0||#request.adEntry.resourceType.code==1">
                            <div>乐知</div>
                            </s:if>
                            <s:elseif test="#request.adEntry.resourceType.code==2">
                            <div>富媒体</div>
                            </s:elseif>
                            <div class="clear spacer10"></div>
                            <s:if  test="#request.adEntry.resourceType.code==0||#request.adEntry.resourceType.code==1">
                            <div id="preview_text">
                                <span class="yellowH3">文本</span>
                                <div class="previewCon" >
                                    <ul class="textCon">
                                        <li><a class="bLink descriptLine" style="display:inline-block;vertical-align: middle;min-width:300px;" href="javascript:void(0)" target="_Blank"><span style="color:#ccc">${adEntry.title}</span></a></li>
                                    </ul>
                                </div>
                            </div>
                        
                            <div id="preview_imgText" style="display: none;">
                                <span class="yellowH3">图文</span>
                                <div class="previewCon"  >
                                    <ul class="textImgCon">
                                        <li >
                                            <a href="javascript:void(0)" id="imglink" target="_Blank">
                                                <div>
                                                    <img id="imgDisplay"  alt="广告图片"  src="<s:property value="%{#request.adEntry.resourceUrl}" escape="true"/>" style="width:100px;height:100px"  />
                                                </div>
                                                <div class="clear spacer5"></div>
                                                <div class="descriptLine" style="width: 100px;word-wrap: break-word; word-break: normal;" >
                                                    ${adEntry.title}
                                                </div>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                            </s:if>
                            <s:elseif test="#request.adEntry.resourceType.code==2">
                            <div id="preview_flash">
                                <span class="yellowH3">富媒体</span>
                                <div class="previewCon">
                                    <ul class="textImgCon">
                                        <li><a href="javascript:void(0)" id="imglink" target="_Blank">
                                            <div id="flashDisplay"></div>
                                            <div class="clear spacer5"></div>
                                            <div class="descriptLine" style="width: 100px; word-wrap: break-word; word-break: normal;"></div>
                                        </a></li>
                                    </ul>
                                </div>
                            </div>
                            </s:elseif>
                        </li>
                    </ul>
                    
            </div>
            <div class="clear spacer50"></div>
            </div>

	            </s:if>
	            <s:else>
	                <div>No ad entry found!</div>
	            </s:else>
	            
	            <div class="clear"></div>
            </div>
            
        </div>
        <div class="clear"></div>
    </div>
    <div class="clear spacer20"></div>
</div>
<div class="clear"></div>

<link rel="stylesheet" href="${css_path}/calendar-date.css" type="text/css" />
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.forms.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/ajaxupload.js"></script>
<script type="text/javascript" charset="utf-8">
$(function(){
    //set paramete
    var dataTemp={};
    dataTemp.campaignId=${adCampaign.id};
    dataTemp.orderId=${adOrder.id};
    dataTemp.id=${request.adEntry.id}
    //更改活动状态
    $("#entryStatus").change(function(){
        var urltemp="";
        if($(this).val()==1){
            urltemp='${cxt_path}/advertiser/entry/${adEntry.id}/enable';
        }else if($(this).val()==2){
            urltemp='${cxt_path}/advertiser/entry/${adEntry.id}/pause';
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
    var imageButton = $("#imageFileUpload")[0];
    var flashButton = $("#flashFileUpload")[0];
    function changeImgSize(){
    	var size=($("#sizeSelector").val()).match(/\d+/g);
		if(imageButton){
            $("#imgDisplay").attr("style","width:"+size[0]+"px;height:"+size[1]+"px;");
		}else{
			$("#flashDisplay").attr("style","width:"+size[0]+"px;height:"+size[1]+"px;");
		}
    }
    //
    function init(){
        var adType="${request.adEntry.resourceType.code}";
        if(adType=="1"){
            $("#resourceUrlImgCon").show();
            $("#preview_imgText").show();
            dataTemp.isImgUrl=true;
        }
        dataTemp.resourceUrl="${request.adEntry.resourceUrl}";
        var linkStr="${request.adEntry.link}";
        var temp=linkStr.indexOf("://")+3;
        var ag=linkStr.substring(0,temp);
        var l=linkStr.substring(temp);
        $("#desturl").val(l);
        if(ag=="http://"){
             $("#agreement").val(1);    
        }else{
            $("#agreement").val(2); 
        }
        $("#flashDisplay").html(buildFlash({url:"${request.adEntry.resourceUrl}",size:$("#sizeSelector").val()}));
        if($("#resourceType").val()=="0"){
			$("#materialSize").hide();
		}else if($("#resourceType").val()=="1"){
			changeImgSize();
		}
    }
    init();
    
    $("#sizeSelector").change(function(){
    	changeImgSize();
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
	//upload
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
	        }); }
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
    
    
    // create image file uploader:
    var originalFile = $('<div/>').text("${request.adEntry.resourceUrl}").html() ;
    
    
    $("#revertImage").click(function() {
        $("#resourceUrl").val(originalFile);
        $("#resourceUrlImg").attr("src",originalFile);
    });
    $("#resourceUrl").blur(function() {
        $("#resourceUrlImg").attr("src", $(this).val());
    });
    
    //ad type choose img
    $("#resourceType").change(function(){
        var type=$("#resourceType").val();
        switch(type){
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
    function getUrl(){
        var agreement=$("#agreement  option:selected").text();
        var url=$("#desturl").val();
        return agreement+url;
    }
    
    function setPreview(){
        var adtype=$("#resourceType  option:selected").val();
        var headlinestr=RC.stripscript($.trim($("#headline").val()))
        if(adtype==0){
            $(".descriptLine").html(headlinestr).attr("href",getUrl()).attr("title",headlinestr);
        }else if(adtype==1){
            $(".descriptLine").html(headlinestr).attr("href",getUrl()).attr("title",headlinestr);
            $("#imglink").attr("href",getUrl());
        }
    }
    function setUrl(){
        var adtype=$("#resourceType  option:selected").val();
        if(adtype==0){
            $(".descriptLine").attr("href",getUrl());
        }else if(adtype==1){
            $(".descriptLine").attr("href", getUrl());
            $("#imglink").attr("href",getUrl());
        }
    }
    setPreview();
    //descriptLine 
    /*$("#descriptLine").blur(function(){
        setPreview();
    });*/
    $("#agreement").change(function(){
        setUrl();
    })
    $("#desturl").blur(function(){
        setUrl();
    })
    
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
    
    //choose campaign
    $("#leftmenu-admin-campaignManage").addClass("active");
	$("#topmenu-admin-ads").addClass("active");

    
    
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
    
    //form group
    $("#bid_nolimit").click(function(){
        if(($(this)[0]).checked){
            $("#bid").attr("disabled","true");
        }else{
            $("#bid").removeAttr("disabled");
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
            $("#datePickerEnd")[0].disabled="true";
        }else{
            $("#datePickerEnd").removeAttr("disabled");
        }
    });
    
    function getName(){
        if($("#name").val()!="" && $("#name").val().length <= 50){
            dataTemp.entryName=RC.stripscript($("#name").val());
            $("#name_msg").html("");
            dataTemp.isName=true;
        }else if($("#name").val()==""){
            $("#name_msg").html("请输入广告名称");
            dataTemp.isName=false;
        }else if($("#name").val().length > 50){
            $("#name_msg").html("名称不能大于50个字");
            dataTemp.isName=false;
        }
    }
    
    function getHeadline(){
        var headline=$("#headline").val();
        if(headline!==""){
            dataTemp.entryTitle=RC.stripscript($.trim(headline));;
            $("#headline_msg").html("");
            dataTemp.isHeadline=true;
        }else{
            $("#headline_msg").html("请输入标题");
            dataTemp.isHeadline=false;
        }
    }
    
    function getDescriptLine(){return;
        var desc=$("#descriptLine").val();
        if(desc!=""){
            dataTemp.descriptLine=headline;
            $("#descript_msg").html("");
            dataTemp.isDescriptLine=true;
        }else{
            $("#descript_msg").html("请输入广告描述");
            dataTemp.isDescriptLine=false;
        }
    }
    
    function getDesturl(){
        var agreement=$("#agreement  option:selected").text();          
        var desturl=$("#desturl").val();
        if(desturl !==""){
            var url=RC.isURL(agreement+desturl);
            if(url){
                dataTemp.link=agreement + desturl;
                $("#desturl_msg").html("");
                $("#entry_link").val(url);
                dataTemp.isDesturl=true;    
            }else{
                $("#desturl_msg").html("请输入正确的地址");
                dataTemp.isDesturl=false;
            }
        
        }else{
            $("#desturl_msg").html("请输入目标地址");
            dataTemp.isDesturl=false;
        }
    }
    
    $("#name").blur(function(){
        getName();
    });
    
    $("#headline").blur(function(){
        getHeadline();   
        setPreview();
    });
    
    /*
        $("#descriptLine").blur(function(){
            getDescriptLine();      
        });
    */
    
    $("#desturl").blur(function(){
        getDesturl();
    });
    
    //switch stats
    $("#campaignSelector").change(function(){
        $("#campaignId").val($("#campaignSelector").val());
        $("#orderId").val(0);
        $("#availableStatsForm").submit();
        
    });
    
    $("#groupSelector").change(function(){
        $("#campaignId").val($("#campaignSelector").val());
        $("#orderId").val($("#groupSelector").val());
        $("#availableStatsForm").submit();
    });



   // var isSubmitting = false;
    function submit(isSave){
        getName();
        var adtype = $("#resourceType  option:selected").val();
        dataTemp.resourceType = adtype;
        getHeadline();
        //getDescriptLine();
        getDesturl();
       
        dataTemp.size = $("#sizeSelector").val();
        if (!dataTemp.isImgUrl && $("#resourceType").val() == "1") {
            $("#imgUrl_msg").html("请上传广告图片或提供链接");
            dataTemp.isImgUrl = false;
            return false;
        } else if (!dataTemp.resourceUrl && $resourceType == "2") {
            $("#flashUrl_msg").html("请上传富媒体或提供链接");
            dataTemp.isImgUrl = false;
            return false;
        } else{
        	dataTemp.isImgUrl = true;
        }
		if( $("#uploadModePanel input[name='uploadMode']:checked").attr("alt")==1){
     	   getResourceUrl();
        }
        var $resourceType = $("#resourceType").val();
        if ($resourceType == 2) {
            dataTemp.position = $("#position option:selected").val();
        }
        if (dataTemp.isName && dataTemp.isHeadline && dataTemp.isImgUrl && dataTemp.isDesturl && dataTemp.size) {
            //if (isSubmitting) return;
           // isSubmitting = true;
            $.ajax({
                type: "post",
                url: "${cxt_path}/admin/orders/entry/update",
                data:dataTemp,
                dataType:"json",
                success: function (data) {
                    if(data.success) {
                    	displayStatusMessage("修改成功！", "success"); 
                        window.setTimeout(function(){
                            window.location="${cxt_path}/admin/campaigns/view?id=${adCampaign.id}";
                        }, 500); 
                    } else {
                    	displayStatusMessage("修改失败！", "false"); 
                    }
                }
                });
    }
    }
    $("#submit").click(function(){
        submit(true); 
    });
    
});
</script>
