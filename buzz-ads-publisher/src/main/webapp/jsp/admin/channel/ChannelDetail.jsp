<%@ include file="/jsp/common/Init.jsp" %>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<link rel="stylesheet" href="${css_path}/popWin.css"/>
<script type="text/javascript" src="${js_path}/libs/popWin.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/jquery.tools.toolbox.min.1.2.6.js"></script>

<title>详情</title>

<div class="container-body">
    <div class="spacer20"></div>
    <%@ include file="/jsp/admin/user/LeftMenu.jsp" %>
    <div class="container-right" style="overflow:auto;">
        <s:if test="channel.level==1">
            <div class="overviewPanel shadow">
                <div class="panelTitle">一级媒体ID：<s:property value="channel.id"></s:property></div>
            </div>
            <div class="overview" style="border-top:0px">
                <table class="viewTable">
                    <tr>
                        <td>域名：</td>
                        <td><span class="orange heading1"><s:property value="channel.domain"></s:property></span></td>
                    </tr>
                    <tr>
                        <td>UUID：</td>
                        <td><span class="orange heading1"><s:property value="channel.uuid"></s:property></span></td>
                    </tr>
                </table>
            </div>
            <div class="spacer15"></div>
            <div class="overviewPanel shadow">
                <div class="panelTitle left" style="height:34px;line-height:34px;">二级媒体表</div>
                <div class=" right"><a class="bButton lightOrange"
                                       href="${cxt_path}/admin/${channel.id}/channel/createPanel">新建二级媒体</a></div>

            </div>
            <div class="overview" style="border-top:0px">
                <table class="bTable">
                    <tbody>
                    <tr>
                        <th>ID</th>
                        <th>域名</th>
                        <th>类型</th>
                        <th>状态</th>
                        <th style="text-align:center">操作</th>
                    </tr>
                    <s:if test="%{subChannel.size > 0}">
                        <s:iterator status="status" var="ch" value="subChannel">
                            <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
                                <td>${ch.id}</td>
                                <td>${ch.domain}</td>
                                <td>${ch.displayType}</td>
                                <td>${ch.status}</td>
                                <td style="text-align:center">
                                    <a class="bLink view" href="${cxt_path }/admin/${ch.id}/channel"
                                       id="${ch.id}">查看</a>
                                    <s:if test="#ch.status == @com.buzzinate.buzzads.enums.ChannelStatusEnum@OPENED">
                                        <a class="bLink"
                                           href="javascript:popupResult(${ch.id},'FROZENED', '${ch.domain}')">冻结</a>
                                    </s:if>
                                    <a class="bLink mSpacer_left10"
                                       href="javascript:popupResult(${ch.id},'DELETED', '${ch.domain}')">删除</a>
                                    <s:if test="#ch.status == @com.buzzinate.buzzads.enums.ChannelStatusEnum@FROZENED">
                                        <a class="bLink" href="javascript:updateChannelState(${ch.id},'OPENED')">解冻</a>
                                    </s:if>
                                </td>
                            </tr>
                        </s:iterator>
                    </s:if>
                    <s:else>
                        <tr>
                            <td colspan="5" style="text-align: center">暂无数据</td>
                        </tr>
                    </s:else>
                    </tbody>
                </table>
            </div>
        </s:if>
        <s:else>
            <div class="overviewPanel shadow">
                <div class="panelTitle">二级媒体ID：<s:property value="channel.id"></s:property></div>
            </div>
            <div class="overview" style="border-top:0px">
                <table class="viewTable">
                    <tr>
                        <td>父媒体ID：</td>
                        <td><span class="orange heading1"><s:property value="topChannel.id"></s:property></span></td>
                    </tr>
                    <tr>
                        <td>父媒体域名：</td>
                        <td><span class="orange heading1"><s:property value="topChannel.domain"></s:property></span>
                        </td>
                    </tr>
                </table>
            </div>
        </s:else>
        <div class="spacer15"></div>
        <div class="overviewPanel shadow">
            <div class="panelTitle">媒体信息</div>
        </div>
        <div class="overview" style="border-top:0px">

            <s:form id="cForm">
                <s:hidden name="channel.id" value="%{channel.id}"/>
                <s:hidden name="channel.level" value="%{channel.level}"/>
                <s:hidden name="channel.uuid" value="%{channel.uuid}"/>
                <s:hidden name="channel.openTime" value="%{channel.openTime}"/>
                <s:hidden name="channel.closeTime" value="%{channel.closeTime}"/>
                <!-- action校验失败后能回显错误页面 -->
                <s:hidden name="topChannel.id" value="%{topChannel.id}"></s:hidden>
                <s:hidden name="topChannel.domain" value="%{topChannel.domain}"></s:hidden>
                <table class="viewTable" cellpadding="0" cellspacing="0" width="100%" style="table-layout:fixed;">
                    <s:if test="channel.level==2">
                        <tr>
                            <td style="width:100px;">域名：</td>
                            <td ><s:textfield name="channel.domain" cssClass="bTextbox"></s:textfield></td>
                            <td style="width:100px;"></td><td></td>
                        </tr>
                    </s:if>
                    <s:else>
                        <s:hidden name="channel.domain" value="%{channel.domain}"></s:hidden>
                    </s:else>
                    <tr>
                        <td style="width:100px;">状态：</td>
                        <td >
                            <s:select id="channelStatus" name="channel.status" disabled="true" cssClass="bTextbox"
                                      list="statusSelector"
                                      value="%{channel.status}"></s:select>
                            <s:if test="channel.status != @com.buzzinate.buzzads.enums.ChannelStatusEnum@DELETED">
                                操作:
                                <s:if test="%{channel.status == @com.buzzinate.buzzads.enums.ChannelStatusEnum@FROZENED}">
                                    <a class="bLink mSpacer_left10" href="javascript:updateChannelState('${channel.id}','OPENED')">解冻</a>
                                </s:if>
                                <s:else>
                                    <a class="bLink mSpacer_left10" href="javascript:popupResult('${channel.id}','FROZENED', '${channel.domain}')">冻结</a>
                                </s:else>
                                <a class="bLink mSpacer_left10" href="javascript:popupResult('${channel.id}','DELETED', '${channel.domain}')">删除</a>
                            </s:if>
                            <s:hidden id="operateResult" name="channel.operateResult"
                                      value="%{channel.operateResult}"></s:hidden>
                        </td>
                        <td  style="width:100px;">媒体类别：</td>
                        <td >
                            <s:select cssClass="bTextbox" name="channel.type" list="typeSelector"
                                      value="%{channel.type}"></s:select>
                        </td>
                    </tr>
                    <tr>
                        <td>网络类型：</td>
                        <td >
                            <s:checkboxlist name="channel.netWork"
                                            list="networkSelector"
                                            value="%{channel.netWork}" disabled="true"></s:checkboxlist>
                        </td>
                        <td >广告位类型：</td>
                        <td >
                            <s:checkboxlist name="channel.style"
                                            list="styleSelector"
                                            value="%{channel.style}" disabled="true"></s:checkboxlist>
                        </td>
                    </tr>
                    <tr>
                        <td>广告格式：</td>
                        <td >
                            <s:select name="channel.adType" cssClass="bTextbox"
                                      list="adTypeSelector"
                                      value="%{channel.adType}"></s:select>
                        </td>
                        <td>最低CPM价格：</td>
                        <td >
                            ¥ <input class="validator bTextbox" required="required" type="Number" step="0.01" min="0.01"
                                     id="channelCpmView" value="${channel.minCPMText}" style="width:70px;">
                            <span id="displayBidType" >CPM </span>
                            <span class="primary mSpacer_left10" id="cpm_msg"></span>
                            <s:hidden id="channelCpm" name="channel.minCPM" value="%{channel.minCPM}"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="vertical-align:top;">广告位截图：</td>
                        <td colspan="3">
                            <div class="" style="font-size:12px;">
                                <img id="resourceUrlImg" width="100" height="100" alt="图片" <s:if
                                        test="%{channel.adThumb != null}"> src="${channel.adThumb}"</s:if>/>
                                <input class="bButton mSpacer_left10"
                                       style="vertical-align: text-bottom;" type="button"
                                       id="imageFileUpload" value="上传图片"/>
                                <span style="font-size:16px;" class="primary mSpacer_left10" id="imgUrl_msg"></span>
                            </div>
                            <s:hidden id="resourceUrl" name="channel.adThumb" value="%{channel.adThumb}"></s:hidden>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4" style="text-align:center;">
                            <input type="submit" value="修改" class="bButton center lightOrange" id="btnChannelUpdate"/>
                            <input id="btnBack" class="bButton center mSpacer_left10" type="button" value=" 返回  ">
                        </td>
                    </tr>

                </table>


            </s:form>
        </div>
    </div>
</div>
<div id="popWin" class="popWin" style="display:none"></div>
<div id="adsWindowPopDiv"></div>
<div id="popWinUnfrozen" class="popWin" style="display:none"></div>
<div id="unfrozenCon"></div>
<div id="frozen" class="hidden">
    <div class="reset">
        <table class="viewTable" width="100%">
            <tr>
                <td>媒体域名：</td>
                <td id="domain"></td>
            </tr>
            <tr>
                <td colspan="2">
                    <div class="spacer15" style="border-top: 1px dashed #dadada;"></div>
                    冻结原因
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <textarea id="resultText" rows="10" cols="54"></textarea>
                </td>
            </tr>
        </table>
    </div>
    <div class="lezhiGetCodeBtn" style="text-align:right;">
        <div class="spacer15"></div>
        <input type="submit" value="确认" class="submitBtn bButton lightOrange">&nbsp;&nbsp;
        <input type="button" class="bButton backBtn" value="返回">
    </div>
</div>
<div id="delete" class="hidden">
    <div class="reset">
        <table class="viewTable" width="100%">
            <tr>
                <td>媒体域名：</td>
                <td id="domain"></td>
            </tr>
            <tr>
                <td colspan="2">
                    <div class="spacer15" style="border-top: 1px dashed #dadada;"></div>
                    删除原因
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <textarea id="resultText" rows="10" cols="54"></textarea>
                </td>
            </tr>
        </table>
    </div>
    <div class="lezhiGetCodeBtn" style="text-align:right;">
        <div class="spacer15"></div>
        <input type="submit" value="确认" class="submitBtn bButton lightOrange">&nbsp;&nbsp;
        <input type="button" class="bButton backBtn" value="返回">
    </div>
</div>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/ajaxupload.js"></script>
<script type="text/javascript">
    var channelId = "${channel.id}";
    var domain = "${channel.domain}";
    //create popWin
    var data = {
        container: $("#popWin"),
        width: 500,
        height: 400,
        title: "冻结",
        content: $("#adsWindowPopDiv"),
        closeCallback: function () {

        },
        afterCallback: function () {

        }
    }
    var popwin = new bshare.popWin(data);
    //create popWin
    var dataUn = {
        container: $("#popWinUnfrozen"),
        width: 300,
        height: 150,
        title: "解冻",
        content: $("#unfrozenCon"),
        closeCallback: function () {

        },
        afterCallback: function () {

        }
    }
    var popwinUn = new bshare.popWin(dataUn);
    //back btn
    $(".backBtn").live("click",function(){
        popwin.close();
    });

    var $thumbnail = $("#thumbnail");
    var $btnChannelUpdate = $("#btnChannelUpdate");
    var $cForm = $("#cForm");
    var $channelCpm = $("#channelCpm");
    var $channelCpmView = $("#channelCpmView");
    $cForm.attr("action", "${cxt_path}/admin/${channel.id}/channel/update")

    $cForm.live("submit", function () {
        $(this).find("input").each(function () {
            $(this).val($.trim($(this).val()));
        });

    });

    $("#btnBack").live("click", function () {
        location.href = "${cxt_path}/admin/channel";
    });
    var isCpm=false;
    function getCpm(){
    	if(RC.numVerify($channelCpmView.val())){
    		 var newCpm = Math.round($channelCpmView.val() * 100);
             $channelCpm.val(newCpm);
            $("#cpm_msg").html("");
            isCpm=true;
       	}else{
       		$("#cpm_msg").html("请输入合法数字，非负整数，小数[小数最多精确到小数点后两位]！");
       		 isCpm=false;
       	}
    }

    $channelCpmView.change(function () {
        try {
        	getCpm();
        } catch (e) {

        }
    });
    $btnChannelUpdate.click(function(){
    	getCpm();
    	if(isCpm){
    		$("#cForm").submit();
    	}else{
    		return false;
    	}
    })
    new AjaxUpload('#imageFileUpload', {
        action: '${cxt_path}/admin/channel/thumbupload',
        autoSubmit: true,
        name: 'tempFile',
        responseType: "json",
        onChange: function (file, extension) {
        },
        onSubmit: function (file, extension) {
            hideStatusMessage();
            if (!(extension && /^(jpg|png|jpeg|gif)$/i.test(extension))) {
                displayStatusMessage('请选择正确的图片格式!', "error");
                return false; // cancel upload
            }
            this.setData({
                'imageFileName': file,
                'imageFileNameExtension': extension
            });
        },
        onComplete: function (file, response) {
            if (response.success) {
                $("#resourceUrl").val(response.contents.imageUrl);
                $("#resourceUrlImg")
                        .attr("src", response.contents.imageUrl);
                $("#imgDisplay").attr("src", response.contents.imageUrl);
                $("#errorMsg").hide();
                dataTemp.resourceUrl = response.contents.imageUrl;
                dataTemp.isImgUrl = true;
                $("#imgUrl_msg").html("");
                displayStatusMessage("图片上传成功,请点击保存按钮,将其保存.", "success");

            } else {
                dataTemp.isImgUrl = false;
                $("#imgUrl_msg").html(response.message);
                displayStatusMessage(response.message, "error");
            }
        }
    });

    //frozen action
    $(".submitBtn").live("click", function (index) {
        opertateResult = $('#resultText').val();
        popwin.close();
        updateChannelState(channelIdTmp, statusTmp);
    });

    // 弹出操作原因输入按钮进行状态变更
    function updateChannelState(channelId, status) {
        $.ajax({
            type: "post",
            url: "${cxt_path}/admin/media/updateState",
            data: {"channel.id": channelId, "channel.statusSelect": status, "channel.operateResult": opertateResult},
            dataType: "json",
            success: function (callback) {
                if (callback.success) {
                    $("#adsWindowPopDiv").html($("#frozen").html());
                    $("#adsWindowPopDiv").html($("#delete").html());
                    displayStatusMessage("操作成功");
                    // 由于成功为物理删除，所以删除后需跳转到列表页
                    if (status == 'DELETED') {
                    	location.href = '${cxt_path}/admin/channel';
                    }
                } else {
                    displayStatusMessage(callback.message, "error");
                }
                setTimeout(function () {
                    location.reload();
                }, 2000);
            },
            error: function (msg) {
                displayStatusMessage("操作失败" + msg, "error");
            }
        });
    }

    var opertateResult = "";
    var channelIdTmp = 0;
    var statusTmp = '';
    function popupResult(channelId, status, domain) {
        opertateResult = "";
        if (status == 'FROZENED') {
            $("#frozen").find('#domain').html(domain);
            $("#adsWindowPopDiv").html($("#frozen").html());
        } else if (status == 'DELETED') {
            $("#delete").find('#domain').html(domain);
            $("#adsWindowPopDiv").html($("#delete").html());
        }
        channelIdTmp = channelId;
        statusTmp = status;
        popwin.show();
    }

    //menu active
    $("#leftmenu-medium-manage").addClass("active");
    $("#topmenu-admin-manage").addClass("active");

</script>
