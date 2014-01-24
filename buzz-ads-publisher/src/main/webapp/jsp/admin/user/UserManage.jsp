<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>站长管理</title>

<div class="container-body">
	<div class="spacer20"></div>
		<%@ include file="/jsp/admin/user/LeftMenu.jsp"%>
		<div class="container-right" style="overflow:auto;">
			<div class="overviewPanel shadow">
				<div  class="right">
			   		<form id="userInfo" name="form" method="POST" action="${cxt_path}/admin/publisher">
						联系人:&nbsp;<input class="bTextbox" style="width:150px;" type="text" name="publisher.name" value="${publisher.name}" placeholder="Search by name..."/>&nbsp;&nbsp;
			   			Email帐户:&nbsp;<input class="bTextbox" style="width:150px;" type="text" name="publisher.email" value="${publisher.email}" placeholder="Search by email..."/>&nbsp;&nbsp;
						<input class="bButton bButton24" type="submit" value="查询" />    
					</form>
		   		</div>
			</div>
		   	<div class="overview noBT">
				<div class="clear spacer10"></div>
		        <table class="bTable">
		        	<tr class="heading">
		        		<th>Email帐户</th> 
		        		<th>网站</th>
		        		<th>联系人</th> 
		        		<th style="text-align:center;">状态</th> 
		        		<th style="text-align:center;">操作</th>
		        	</tr>
		        	<s:if test="#request['publishers'].size() > 0">
		        	<s:iterator value="#request['publishers']" var="pub" status="status" >
		        	<tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
		        		<td title="${pub.email}"><a class="bLinkU" href="mailto:${pub.email}" target="_blank"><core:partOfText value="${pub.email}" textmaxlength="30" escape="true"/></a></td>
		        		<td title="<s:iterator value="#pub.sites" var="site" status="s">${site.url},</s:iterator>">

		        		   <div style="max-height:50px;overflow:hidden;">
		        		       <s:iterator value="#pub.sites" var="site" status="s">${site.url}<br/></s:iterator>
		        		   </div>
		        		</td>
		        		<td title="<s:property value="%{#pub.name}" escape="true"/>"><core:partOfText value="${pub.name}" textmaxlength="30" escape="true"/></td>
		        		<s:if test="#pub.status.code == 0">
		        		    <td style="text-align:center;color:green;">正常</td>
		        		</s:if>
		        		<s:else>
		        		    <td style="text-align:center;color:red;">已冻结</td>
		        		</s:else>
		        		<td align="center"><a href="${cxt_path}/admin/publisher/view?userId=${pub.userId}" class="view bLink">查看</a>
		        		<a class="bLink frozen" userId="${pub.userId}" 
			        		<s:if test="#pub.status.code == 1">
			        		    style="display:none;"
			        		</s:if>
		        		    >冻结</a>
		        		<a class="bLink unfrozen" userId="${pub.userId}"
			        		<s:if test="#pub.status.code == 0">
			        		    style="display:none;"
			        		</s:if>
		        		    >解冻</a>
		        		<sec:authorize ifAnyGranted="ROLE_AD_ADMIN,ROLE_AD_FINANCE">
                            <a class="bLink view" href="${cxt_path}/admin/settles/detail?userId=${pub.userId}">账户明细</a>
                        </sec:authorize>
		        		</td>
		        	</tr>
		        	</s:iterator>
		        	</s:if>
		        	<s:else>
                    <tr>
                        <td colspan="5" class="no-data">暂无数据</td>
                    </tr>
                    </s:else>
		        </table>
		        <div class="clear spacer5"></div>
	            <div id="pagination" class="right pagination"></div>
	            <div id="pageDesCon" class="right"></div>
	            <div class="clear spacer15"></div>
	        </div>
	        <div><font class="disabled textRight">数据生成时间&nbsp;&nbsp;<span id="creationDate"></span></font></div>
	        <div class="clear spacer15"></div>
		</div>
		<div class="clear spacer20"></div>
</div>
<div id="popWin" class="popWin" style="display:none"></div>
<div id="adsWindowPopDiv"></div>
<div id="frozen" class="hidden">
	<div class="reset">确定要将此用户冻结？</div>
		<div class="lezhiGetCodeBtn" style="text-align:center;">
		<div class="spacer15"></div>
		<input type="submit" value="保存" class="frozenBtn bButton lightOrange">&nbsp;&nbsp;
		<input type="button" class="bButton backBtn" value="返回">
	</div>
</div>
<div id="unfrozen" class="hidden">
	<div class="reset">确定要为此用户解冻？</div>
		<div class="spacer15"></div>
		<div class="lezhiGetCodeBtn" style="text-align:center;">
		<input type="submit" value="保存" class="unfrozenBtn bButton lightOrange" >&nbsp;&nbsp;
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
	//create popWin
	var data={
			container:$("#popWin"),
			width:300,
			height:150,
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
            form: $("#userInfo"),
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
	
	
	
	$(".frozen").live("click",function(){
		var userId = $(this).attr("userId"),
		    index = $(".frozen").index(this);
		$(".frozenBtn").attr("userId",userId);
		$(".frozenBtn").attr("index",index);
		$("#adsWindowPopDiv").html($("#frozen").html());
		popwin.show();
	});
	
	$(".unfrozen").live("click",function(){
		var userId = $(this).attr("userId"),
		    index = $(".unfrozen").index(this);
		$(".unfrozenBtn").attr("userId",userId);
		$(".unfrozenBtn").attr("index",index);
		$("#adsWindowPopDiv").html($("#unfrozen").html());
		popwin.show();
	});
	
	
	
	//back btn
	$(".backBtn").live("click",function(){
		popwin.close();
	});
	
	//frozen action
	$(".frozenBtn").live("click",function(){
		popwin.close();
		var userId = $(this).attr("userId"),
		    index = $(this).attr("index"),
		    frozen = $($(".frozen").get(index));
		$.ajax({
			type: "POST",
			url: "${cxt_path}/admin/publisher/frozen",
			dataType: "json",
			data: "userId=" + userId,
			success: function(callback){
				if(callback.success){
					frozen.hide();
					frozen.next().show();
					frozen.parent().prev().css("color", "red").html("已冻结");
					displayStatusMessage("冻结成功");
				}else{
					displayStatusMessage(callback.message);
				}
			}
		});
	});
	
	//unfrozen action
	$(".unfrozenBtn").live("click",function(){
		popwin.close();
		var userId = $(this).attr("userId"),
        index = $(this).attr("index"),
        unfrozen = $($(".unfrozen").get(index));
		$.ajax({
			type: "POST",
			url: "${cxt_path}/admin/publisher/unfrozen",
			dataType: "json",
			data: "userId=" + userId,
			success: function(callback){
				if(callback.success){
					unfrozen.hide();
					unfrozen.prev().show();
                    unfrozen.parent().prev().css("color", "green").html("正常");
					displayStatusMessage("成功解冻");
				}else{
					displayStatusMessage(callback.message);
				}
			}
		});
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
	$("#leftmenu-admin-manage").addClass("active");
    $("#topmenu-admin-manage").addClass("active");
});
</script>
