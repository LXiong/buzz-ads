<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>新建二级媒体</title>
<style>
    .revenue {  vertical-align: middle; width: 40%; border-collapse: collapse; }
    .revenue td { width: 25%; text-align: left; padding: 5px 0; border-right: 1px solid #dadada; }
    .revenue td.mSpacer_left10{text-align:center;}
    .revenue td.last { border-right: none; }
</style>

<div class="container-body">
    <div class="spacer20"></div>
    <%@ include file="/jsp/admin/user/LeftMenu.jsp"%>
    <div class="container-right" style="overflow:auto;">
        <div class="overviewPanel shadow">
            <div class="panelTitle">新建二级媒体</div>
        </div>
        <div class="overview" style="border-top:0px">
            <table class="viewTable">
                <tr>
                    <td>父媒体ID：</td>
                    <td><span class="orange heading1">${topChannel.id}</span></td>
                </tr>
                <tr>
                    <td>父媒体域名：</td>
                    <td><span class="orange heading1">${topChannel.domain}</span></td>
                </tr>
            </table>
        </div>
        <div class="overviewPanel details" >
            <div class="panelTitle">媒体信息</div>
        </div>
        <div class="overview" style="border-top:0px">
            <form method="post" action="${cxt_path}/admin/${topChannel.id}/channel/create" id="channelForm">
                <s:hidden name="channel.uuid" value="%{topChannel.uuid}"></s:hidden>
                <s:hidden name="topChannel.uuid" value="%{topChannel.uuid}"></s:hidden>
                <s:hidden name="topChannel.id" value="%{topChannel.id}"></s:hidden>
                <s:hidden name="topChannel.minCPM" value="%{topChannel.minCPM}"></s:hidden>
                <s:hidden name="topChannel.domain" value="%{topChannel.domain}"></s:hidden>
                <s:hidden name="channel.level" value="2"></s:hidden>
       		 	<table class="viewTable" cellpadding="0" cellspacing="0" width="100%" style="table-layout:fixed;">
       		 		<tr>
       		 			<td style="width:100px;">域名：</td>
       		 			<td>
       		 				<input style="width: 573px;" name="channel.domain" class="bTextbox" type="text" />
       		 			</td>
       		 			<td style="width:100px;"></td><td></td>
       		 		</tr>
       		 		<tr>
       		 			<td>状态：</td>
       		 			<td >
       		 				<s:select list="%{statusSelector}" id="status" name="channel.status"  value="@com.buzzinate.buzzads.enums.ChannelStatusEnum@OPENED" emptyOption="false" cssClass="bTextbox" cssStyle="min-width:200px;">
                            </s:select>
       		 			</td>
       		 			<td >媒体类别：</td>
       		 			<td>
       		 				<s:select list="%{typeSelector}" id="channelType" name="channel.type" value="@com.buzzinate.buzzads.enums.ChannelTypeEnum@ArtsAndEntertainment" emptyOption="false" cssClass="bTextbox" cssStyle="min-width:200px;">
                            </s:select>
                        </td>
                    </tr>
                    <tr>
                        <td >网络类型：</td>
                        <td>
                            <div>
                                <label>
                                    <input type="checkbox" alt="0" id="network" name="channel.netWork" value="LEZHI" checked="true"/>
                                    <span class="mSpacer_left10">乐知</span>
                                    <span title="<font style='color:#f60;font-weight:bold;'>乐知推荐</font>是目前最强大，最精准的个性化阅读类应用软件。<a class='bLinkU' target='_blank' href='http://www.lezhi.me'>去了解更多</a>！" class="help-popup-img"></span>
                                </label>
                                <span class="primary mSpacer_left10" id="network_msg"></span>
                            </div>
                        </td>
                        <td>广告位类型：</td>
                        <td>
                            <div>
                                <label>

                                    <input type="checkbox" alt="0" id="adsSize" name="channel.style" value="STYLE_120_120" checked="true"/>
                                    <span class="mSpacer_left10">120*120</span>
                                    <span title="<font style='color:#f60;font-weight:bold;'>广告位类型</font>目前只有120*120像素！" class="help-popup-img"></span>
                                </label>
                                <span class="primary mSpacer_left10" id="adsSize_msg"></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>广告格式：</td>
                        <td>
                            <s:select list="%{adTypeSelector}" id="adsType" name="channel.adType" value="%{channel==null?topChannel.adType:channel.adType}" emptyOption="false" cssClass="bTextbox" cssStyle="min-width:200px;">
                            </s:select>
                        </td>
                        <td>最低CPM价格：</td>
                        <td>
                            <div class="">
                                ¥ <input class="validator bTextbox"  required="required" type="Number" step="0.01" min="0.01" id="cpm" value="${channel==null?topChannel.minCPMText:channel.minCPMText}" style="width:70px;">
                                <s:hidden name="channel.minCPM" id="channelCpm" value="%{channel==null?topChannel.minCPM:channel.minCPM}"></s:hidden>
                                <span id="displayBidType" >CPM </span>
                                <span class="primary mSpacer_left10" id="cpm_msg"></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td style="vertical-align:middle">广告位截图：</td>
                        <td colspan="3">
                            <div class="left" style="font-size:12px;">
                                <img id="resourceUrlImg" width="100" height="100" src="${channel==null?'':channel.adThumb}"></img>
                                <input class="bButton"
                                       style="vertical-align: text-bottom;" type="button"
                                       id="imageFileUpload" value="上传图片" />
                                <span style="font-size:16px;" class="primary mSpacer_left10" id="imgUrl_msg"></span>

                            </div>
                            <s:hidden name="channel.adThumb" id="resourceUrl" value="%{channel==null?'':channel.adThumb}"></s:hidden>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4" style="text-align:center">
                            <div class="clear spacer15"></div>
                            <input id="edit" class="bButton center lightOrange" type="submit" value=" 保存 ">
                            <input id="btnBack" class="bButton center mSpacer_left10" type="button" value=" 返回  ">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="clear spacer15"></div>
    </div>
    <div class="clear spacer20"></div>
</div>
<script type="text/javascript" charset="utf-8"  src="${js_path}/libs/ajaxupload.js"></script>

<script type="text/javascript" charset="utf-8" >
    $(function(){
        new AjaxUpload('#imageFileUpload', {
            action : '${cxt_path}/admin/channel/thumbupload',
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
                    'imageFileName' : file,
                    'imageFileNameExtension' : extension
                });
            },
            onComplete : function(file, response) {
                if (response.success) {
                    $("#resourceUrl").val(response.contents.imageUrl);
                    $("#resourceUrlImg")
                            .attr("src", response.contents.imageUrl);
                    $("#imgDisplay").attr("src", response.contents.imageUrl);
                    $("#errorMsg").hide();
                    $("#imgUrl_msg").html("");
                    displayStatusMessage("图片上传成功,请点击保存按钮,将其保存.", "success");

                } else {
                    $("#imgUrl_msg").html(response.message);
                    displayStatusMessage(response.message, "error");
                }
            }
        });
        var $cpm = $("#cpm");
        var $minCpm = $("#channelCpm");
        var isCpm=false;
        function getCpm(){
        	if(RC.numVerify($cpm.val())){
       		 	var cpm = $cpm.val() * 100;
                $minCpm.val(cpm);
                $("#cpm_msg").html("");
                isCpm=true;
	       	}else{
	       		$("#cpm_msg").html("请输入合法数字，非负整数，小数[小数最多精确到小数点后两位]！");
	       		 isCpm=false;
	       	}
        }
        $cpm.change(function(){
            try {
            	getCpm();
            } catch (e) {

            }
        });
       
		$("#edit").click(function(){
			getCpm();
			if(isCpm){
				$("#channelForm").submit();
			}else{
				return false;
			}
			
		});
        $("#btnBack").live("click", function(){
            location.href = "${cxt_path}/admin/channel";
        })
        $("#leftmenu-medium-manage").addClass("active");
        $("#topmenu-admin-manage").addClass("active");
    });
</script>
