<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>核实广告</title>

<div class="container-body">
	<div class="spacer20"></div>
	<div>
		<%@ include file="/jsp/admin/orders/LeftMenu.jsp"%>
		
		<div class="container-right" style="overflow:auto;">
		<div class="overviewPanel shadow">
			<div class="left">
			   	    <form id="filterSearchForm" action="verify" method="post" name="filterSearchForm">
			   	          <s:select list="advertiserSelector"  listKey="key" listValue="value" value="advertiserId" name="advertiserId" headerKey="0" headerValue="全部广告主" cssClass="bTextbox">
	
	                      </s:select>
				   	</form>
			</div>
		</div>
		   	<div class="overview" style="border-top:0px;">
		   		
	            <div class="clear spacer10"></div>
	            <table class="bTable"> 
	                <tr class="heading">
	                    <th>创建时间</th>
	                    <th>广告主</th>
	                    <th>广告名称</th>
	                    <th>类型</th>
	                    <th>标题</th>
	                    <th>介绍</th>
	                    <th>显示URL</th>
	                    <th>目标URL</th>
	                    <th>图片</th>
	                    <th style="text-align:center;">操作</th>
	                </tr>
	                <tr class="heading-float fixed hidden" style="top:0;"></tr>
	                <s:if test="#request['adEntryList'].size() > 0">
	                    <s:iterator value="#request['adEntryList']" var="adEntry" status="status" >
	                    <tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
	                        <td title="<s:date name="#adEntry.updateAt" format="yyyy-MM-dd HH:mm:ss"/>"><s:date name="#adEntry.updateAt" format="yyyy-MM-dd HH:mm:ss"/></td>
	                        <td title="<s:property value="%{#adEntry.advertiserName}" escape="true"/>"><core:partOfText value="${adEntry.advertiserName}" textmaxlength="15" escape="true"/></td>
	                        <td title="<s:property value="%{#adEntry.name}" escape="true"/>"><core:partOfText value="${adEntry.name}" textmaxlength="15" escape="true"/></td>
	                        <td title="${adEntry.resourceType}"><core:partOfText value="${adEntry.resourceType}" textmaxlength="15" escape="true"/></td>
	                        <td title="<s:property value="%{#adEntry.title}" escape="true"/>"><core:partOfText value="${adEntry.title}" textmaxlength="20" escape="true"/></td>
	                        <td title="<s:property value="%{#adEntry.description}" escape="true"/>"><core:partOfText value="${adEntry.description}" textmaxlength="20" escape="true"/></td>
	                        <td title="<s:property value="%{#adEntry.displayUrl}" escape="true"/>"><a class="bLinkU" href="<core:getHref value="${adEntry.displayUrl}" escape="true"/>" target="_blank"><core:partOfText value="${adEntry.displayUrl}" textmaxlength="15" escape="true"/></a></td>
	                        <td title="<s:property value="%{#adEntry.link}" escape="true"/>"><a class="bLinkU" href="<core:getHref value="${adEntry.link}" escape="true"/>" target="_blank"><core:partOfText value="${adEntry.link}" textmaxlength="15" escape="true"/></a></td>
	                        <td title="<s:property value="%{#adEntry.resourceUrl}" escape="true"/>"><a class="bLinkU" href="<core:getHref value="${adEntry.resourceUrl}" escape="true"/>" target="_blank"><core:partOfText value="${adEntry.resourceUrl}" textmaxlength="15" escape="true"/></a></td>
	                        <td align="center"><a class="bLink reject" alt="${adEntry.id}" href="javascript:void(0)">拒绝</a>&nbsp;<a  class=" enable bLink spacer_left5" alt="${adEntry.id}" href="javascript:void(0)">允许</a></td>
	                    </tr>
	                    </s:iterator>
	                </s:if>
	                <s:else>
	                    <tr>
	                        <td colspan="10" class="no-data">暂无数据</td>
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
        <div class="clear"></div>
    </div>
    <div class="clear spacer20"></div>
    <div id="popWin" class="popWin" style="display:none"></div>
       
        
            <div id="adsWindowPopDiv">
                <div id="rejectWindow" >
                    <div>确定要拒绝吗？</div>
                    <div class="clear spacer20"></div>
                    <div style="text-align: center;">
	                    <input class="bButton lightOrange" id="rejectOk" type="button" value="确定" />
	                    <input id="rejectCancel" style="margin-left:20px;" class="bButton" type="button" value="取消" />
                    </div>
                    <div class="clear"></div>
                </div>
                <div id="enableWindow">
                    <div>确定验证通过吗？</div>
                    <div class="clear spacer20"></div>
                    <div style="text-align: center;">
                        <input class="bButton lightOrange" id="enableOk" type="button" value="确定" />
                        <input style="margin-left:20px;" id="enableCancel" class="bButton" type="button" value="取消" />
                    </div>
                    <div class="clear"></div>
                </div>
            </div>
        
</div>
<div class="clear"></div>

<link rel="stylesheet" href="${css_path}/pagination.css" type="text/css" />
<link rel="stylesheet" href="${css_path}/popWin.css" />
<script type="text/javascript" src="${js_path}/libs/popWin.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/jquery.pagination.js"></script>
<script type="text/javascript" charset="utf-8">

	var elemTop = $(".bTable .heading").offset().top;
	
	function isScrolledOut($elem) {
	    var docViewTop = $(window).scrollTop();
	    return elemTop <= docViewTop;
	}
	function doSearch() {
		$("#sName").val($.trim($("#sName").val()));
		$("#filterSearchForm").submit();
	}
	
	$(function() {
		//create popWin
		var data={
				container:$("#popWin"),
				width:320,
				height:160,
				title:"确认操作",
				content:$("#adsWindowPopDiv"),
				closeCallback:function(){
				
				},
				afterCallback:function(){
				
				}
			}
		var popwin=new bshare.popWin(data);
		
		$("#leftmenu-admin-verifyAds").addClass("active");
		$("#topmenu-admin-ads").addClass("active");
		$("#advertiserId").change(function(){
			$("#filterSearchForm").submit();
		});
		
		$(window).resize(function() {
            var html = "";
            $(".bTable .heading th").each(function() {
                html += "<th style='font-size:12px;text-align:" + $(this).css("text-align") + ";width:" + $(this).width() + "px;'>" + $(this).text() + "</th>";
            });
            $(".heading-float").html(html);
            $(".heading-float").css("left", $(".bTable .heading th").offset().left);
        }).resize();
        
        $(document).scroll(function() {
            if(isScrolledOut($(".bTable.heading"))) {
                $(".bTable .heading-float").show();
            } else {
                $(".bTable .heading-float").hide();
            }
        });
        
        $("#filterStatus").change(function() {
            $("#sName").val($.trim($("#sName").val()));
            $("#filterSearchForm").submit();
        });

        // pagination
        function initPage(pageNum, pageSize, totalRecords) {
            var pn = parseInt(pageNum, 10) - 1;
            var ps = parseInt(pageSize, 10);
            var tr = parseInt(totalRecords, 10);
            if (pn < 0) {
                return;
            }
            var opt = {
                form: $("#filterSearchForm"),
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
        
      
        
        //action function here
        $("#closeBtn").live("click",function(){
            topwindowHide();
        });
        //reject click
        var verifyId="";
        $(".reject").live("click",function(){
            popwin.show();
            verifyId=$(this).attr("alt");
            $("#adsWindowPop").addClass("showPop");
            $("#enableWindow").hide();
            $("#rejectWindow").show();
        });
        $("#rejectOk").click(function(){
            $.ajax({
                type: "post",
                url: "${cxt_path}/admin/ads/"+verifyId+"/reject",
                data:null,
                dataType:"json",
                async:false,
                success: function (data) {
                     if (data.success) {
                        window.location="${cxt_path}/admin/ads/verify?advertiserId="+"${advertiserId}";
                     }else{
                         displayStatusMessage("拒绝未成功！", "error"); 
                         popwin.close();
                     }
                },
                error: function (msg) {
                    displayStatusMessage("拒绝请求失败！", "error");   
                    popwin.close();
                }
            });
        });
        $("#enableOk").click(function(){
            $.ajax({
                type: "post",
                url: "${cxt_path}/admin/ads/"+verifyId+"/accept",
                data:null,
                dataType:"json",
                async:false,
                success: function (data) {
                     if (data.success) {
                        window.location="${cxt_path}/admin/ads/verify?advertiserId="+"${advertiserId}";
                     }else{
                         displayStatusMessage("审核未通过！", "error"); 
                         popwin.close();
                     }
                },
                error: function (msg) {
                    displayStatusMessage("允许请求失败！", "error");  
                    popwin.close();
                }
            });
        });
        
        $("#rejectCancel").click(function(){
           popwin.close();
        });
        //enable click
        $(".enable").click(function(){
            verifyId=$(this).attr("alt");
           	popwin.show();
            $("#enableWindow").show();
            $("#rejectWindow").hide();
        });
        $("#enableCancel").click(function(){
            popwin.close();
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
    });
</script>
