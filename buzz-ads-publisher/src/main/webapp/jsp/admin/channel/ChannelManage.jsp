<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>媒体管理</title>
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
            <div class="overview">
                <div>
                    <table class="revenue left">
                        <tbody>
                            <tr>
                                <td><span>媒体总数：</span><span class="orangeNumber">${channelCount }</span></td><td class="last mSpacer_left10"><span >活跃媒体数量：</span><span class="orangeNumber">${activeChannelCount }</span></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="clear spacer15"></div>
            <div class="overviewPanel shadow">
                 <form action="channel" id="mediumForm">
                   	<div class="left">
                   		 <div class="inlineBlock">
	                         <s:select list="channelStatusMap" id="mediumStruts" name="selectStatus" value="selectStatus" listKey="key" listValue="value" cssClass="bTextbox" cssStyle="min-width:200px;width:200px;">
	                         </s:select>
	                     </div>
	                     <div class="inlineBlock">
	                         <s:select list="channelTypeMap" id="mediumType" name="selectType" value="selectType" listKey="key" listValue="value" cssClass="bTextbox" cssStyle="margin-left:15px;min-width:200px;width:200px;">
	                         </s:select>
	                     </div>
                   	</div>
                     <input class="right bButton bButton24" style="width:80px;" id="exportCSV" value="导出CSV"/>
                 </form>
            </div>
            <div class="overview" style="border-top:0px;">
                <table class="bTable">
                    <tr class="heading">
                        <th>域名</th>
                        <th>站长</th>
                        <th class="textRight">媒体类型</th>
                        <th style="text-align:center">媒体类别</th>
                        <th class="textRight">网络类型</th>
                        <th class="textRight">日PV</th>
                        <th class="textRight">日点击</th>
                         <th class="textRight">日CTR</th>
                        <th style="text-align:center">状态</th>
                        <th class="textRight">开启时间</th>
                        <th class="textRight">关闭时间</th>
                        <th style="text-align:center">操作</th>
                    </tr>
                    <s:if test="#request.channels != null && #request.channels.size > 0">
                        <s:iterator value="#request.channels" var="ch" status="status">
                            <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
                                <td >${ch.domain}</td>
                                <td ><a href="publisher/view?userId=${ch.userId }">${ch.email}</a></td>
                                <td class="textRight">${ch.level}</td>
                                <td style="text-align:center">${ch.displayType}</td>
                                <td class="textRight">${ch.netWork}</td>
                                <td class="textRight"><s:text name="global.format.number"><s:param value="%{#ch.dailyViews}"/></s:text></td>
                                <td class="textRight"><s:text name="global.format.number"><s:param value="%{#ch.dailyClicks}"/></s:text></td>
                                <td class="textRight">${ch.dailyCTR}</td>
                                <td style="text-align:center">${ch.status}</td>
                                <td class="textRight"><s:date name="#ch.openTime" format="yyyy-MM-dd"/></td>
                                <td class="textRight"><s:date name="#ch.closeTime" format="yyyy-MM-dd"/></td>
                                <td style="text-align:center">
                                   <a class="bLink view" href="${cxt_path }/admin/${ch.id}/channel" >查看</a>
                                   	<s:if test="#ch.status == @com.buzzinate.buzzads.enums.ChannelStatusEnum@OPENED">
                                    	<a class="bLink" href="javascript:popupResult(${ch.id},'FROZENED', '${ch.domain}')" >冻结</a>
                               		</s:if>
                                    <s:if test="#ch.status == @com.buzzinate.buzzads.enums.ChannelStatusEnum@FROZENED">
                                        <a class="bLink" href="javascript:updateChannelState(${ch.id},'OPENED')" >解冻</a>
                                    </s:if>
                                    <s:if test="#ch.status != @com.buzzinate.buzzads.enums.ChannelStatusEnum@DELETED">
                                        <a class="bLink" href="javascript:popupResult(${ch.id},'DELETED', '${ch.domain}')" >删除</a>
                                    </s:if>
                                    <s:if test="#ch.level==1">
                                    <a class="bLink" href="${cxt_path}/admin/${ch.id}/channel/createPanel">新建二级媒体</a>
                                    </s:if>
                                </td>
                            </tr>
                        </s:iterator>
                    </s:if>
                    <s:else>
                        <tr>
                            <td colspan="11" class="no-data">暂无数据</td>
                        </tr>
                    </s:else>
                </table>
                  <div class="clear spacer10"></div>
			      <form action="channel" id="channelForm" method="post">
			          <div id="pagination" class="right pagination"></div>
			          <input type="hidden" name="selectStatus" value="${selectStatus}">
			          <input type="hidden" name="selectType" value="${selectType}">
			      </form>
			      <div id="pageDesCon" class="right"></div>
            </div>

            <div><font class="disabled textRight">数据生成时间&nbsp;&nbsp;<span id="creationDate"></span></font></div>
            <div class="clear spacer15"></div>
        </div>
        <div class="clear spacer20"></div>

</div>
<div id="popWin" class="popWin" style="display:none"></div>
<div id="adsWindowPopDiv"></div>
<div id="popWinUnfrozen" class="popWin" style="display:none"></div>
<div id="unfrozenCon"></div>
<div id="frozen" class="hidden">
	<div class="reset">
		<table class="viewTable" width="100%">
			<tr>
				<td>媒体域名:</td>
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
                <td>媒体域名:</td>
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

<link rel="stylesheet" href="${css_path}/pagination.css" type="text/css" />
<link rel="stylesheet" href="${css_path}/calendar-date.css" type="text/css" />
<link rel="stylesheet" href="${css_path}/popWin.css" />
<script type="text/javascript" src="${js_path}/libs/popWin.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/jquery.tools.toolbox.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.forms.min.1.2.6.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/jquery.pagination.js"></script>
<script type="text/javascript" charset="utf-8" >
$(function(){

    $("#exportCSV").live("click", function(){
        $("#mediumForm").attr("action","${cxt_path}/admin/channel/export").attr("target","_blank").submit();
        $("#mediumForm").attr("action", "channel").attr("target","_self");
    })
    //submit form
    $("#mediumStruts").change(function(){
        $("#mediumForm").submit();
    });
    $("#mediumType").change(function(){
        $("#mediumForm").submit();
    });

    RC.CutSelect("mediumType");

    	var data={
			container:$("#popWin"),
			width:500,
			height:400,
			title:"确认操作",
			content:$("#adsWindowPopDiv"),
			closeCallback:function(){

			},
			afterCallback:function(){

			}
		}
	var popwin=new bshare.popWin(data);

    // pagination
    function initPage(pageNum, pageSize, totalRecords) {
        var pn = parseInt(pageNum, 10) - 1;
        var ps = parseInt(pageSize, 10);
        var tr = parseInt(totalRecords, 10);
        if (pn < 0) {
            return;
        }
        var opt = {
            form: $("#channelForm"),
            pageDesCon:$("#pageDesCon"),
            next_text: "下一页",
            current_page: pn,
            items_per_page: ps,
            num_display_entries: 5,
            num_edge_entries: 2,
            prev_text: "上一页"
        };
        $("#pagination").pagination(tr, opt);
    }
    initPage("${page.pageNum}","${page.pageSize}", "${page.totalRecords}");

    //back btn
    $(".backBtn").live("click",function(){
        popwin.close();
    });

    //trim input value
    $("form").live("submit",function(){
        $(this).find("input").each(function(){
            $(this).val($.trim($(this).val()));
        });
    });

    function getDate(){
        var dateTemp=new Date();
        return dateTemp.getFullYear()
            + "-" + ('0'+(dateTemp.getMonth()+1)).slice(-2)
            + "-" + ('0'+dateTemp.getDate()).slice(-2)
            + " " + ('0'+dateTemp.getHours()).slice(-2)
            + ":" + ('0'+dateTemp.getMinutes()).slice(-2)
            + ":" + ('0'+dateTemp.getSeconds()).slice(-2);
    }
    $("#creationDate").html(getDate());

    //menu active
    $("#leftmenu-medium-manage").addClass("active");
    $("#topmenu-admin-manage").addClass("active");
});

</script>
<script type="text/javascript">
//create popWin
var data={
        container:$("#popWin"),
		width:500,
		height:400,
        title:"确认操作",
        content:$("#adsWindowPopDiv"),
        closeCallback:function(){

        },
        afterCallback:function(){

        }
    }
var popwin=new bshare.popWin(data);

var opertateResult = "";
var channelIdTmp = 0;
var statusTmp = '';
function popupResult(channelId, status, domain) {
    opertateResult = "";
    if(status == 'FROZENED') {
    	$("#frozen").find('#domain').html(domain);
        $("#adsWindowPopDiv").html($("#frozen").html());
    } else if(status == 'DELETED') {
    	$("#delete").find('#domain').html(domain);
        $("#adsWindowPopDiv").html($("#delete").html());
    }
    channelIdTmp = channelId;
    statusTmp = status;
    popwin.show();
}

//frozen action
$(".submitBtn").live("click", function(index){
    opertateResult = $(this).parent().parent().find('#resultText').val();
    popwin.close();
    updateChannelState(channelIdTmp, statusTmp);
});

// 弹出操作原因输入按钮进行状态变更
function updateChannelState(channelId, status) {
    $.ajax({
        type: "post",
        url: "${cxt_path}/admin/media/updateState",
        data: {"channel.id" : channelId, "channel.statusSelect" : status, "channel.operateResult" : opertateResult},
        dataType:"json",
        success: function (callback) {
            if(callback.success){
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
            setTimeout(function() {
            	location.reload();
            }, 1000);
        },
        error: function (msg) {
            displayStatusMessage("操作失败" + msg, "error");
        }
    });
}
</script>
