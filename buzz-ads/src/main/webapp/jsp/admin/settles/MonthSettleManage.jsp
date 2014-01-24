<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>Monthly Settlements Management</title>

<style>
    .error{z-index:100;}
</style>

<div class="container-body">
	<div class="spacer20"></div>
	
	<div>
	    <s:if test="%{#request.currentPageRole == 'admin'}">
            <%@ include file="/jsp/finance/LeftMenu.jsp"%>
        </s:if>
        <s:elseif test="%{#request.currentPageRole == 'finance'}">
            <%@ include file="/jsp/finance/LeftFinanceMenu.jsp"%>
        </s:elseif>
        <s:else>
        </s:else>
		
		<input type="hidden" id="currentDate" value="<s:date name="#request.settleMonth" format="yyyy-MM-dd"/>" />
		<div class="container-right" style="overflow:auto;">
            <div class="overview">
            	<table class="bTable info" style="width:auto;">
	            	<tr>
	                    <td>Results for month:</td>
	                    <td style="padding-left:0px;"><span class="heading1"><s:date name="#request.settleMonth" format="yyyy-MM"/></span></td>
	                </tr>	
	                <tr>
	                    <td>Total Commission:</td>
	                    <td style="padding-left:0px;"><span class="heading1">¥${totalCommissionDouble}</span></td>
	                </tr>
	            </table>
	            
			    <div class="clear spacer5"></div>
			    <s:if test="%{#request.currentPageRole == 'admin'}">
			        <div><a class="left bLinkU"  href="${cxt_path}/settlement/payments">查看支付历史</a></div>
			    </s:if>
			    <s:if test="%{#request.currentPageRole == 'finance'}">
	                <div><a class="left bLinkU"  href="${cxt_path}/finance/settles/payments">查看支付历史</a></div>
	            </s:if>
	            
	            <div class="clear spacer15" style="border-bottom:1px solid #eee;"></div>
	            <div class="clear spacer15"></div>
	            <div class="dataSearch" style="line-height:25px;">
	            	<form id="searchForm" name="searchForm" action="" method="post">
	            	<input type="hidden" id="settleMonth" name="settleMonth" value="<s:date name="#request.settleMonth" format="yyyy-MM-DD"/>" />
			        <span class="left">结算月:</span>
			        <input id="settleMonthPicker" style="width:80px;" class="bTextbox left" type="date"
						min="<%=new java.text.SimpleDateFormat("2012-01-01").format(new java.util.Date())%>" 
			            max="<%=new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date())%>" 
			            value="<s:date name="#request.settleMonth" format="yyyy-MM"/>"/>&nbsp;&nbsp;
			        <span>账户:</span>
			        <input type="text" placeholder="Search by email..." id="searchName" class="bTextbox" style="width:150px;" name="searchName" value="${searchName }"/>&nbsp;&nbsp;
			        <span>余额:</span>
			        <s:select list="#{50:'大于50',100:'大于100',500:'大于500'}" listKey="key" listValue="value" name="settleAmount" cssClass="bTextbox" cssStyle="width: 100px;"
			                  headerKey="" headerValue="ALL">
			        </s:select>
			        <input id="settleMonthSubmit" type="submit" class="bButton bButton24" style="width:80px;" value="查询" />
			    	</form>  
			    </div>
			    <div class="clear spacer10"></div>
				<table class="bTable">
					<tr class="heading">
						<th>账户</th>
						<th>收款人</th>
						<th>账号</th>
						<th style="text-align:center;">开户行</th>
						<th class="textRight">余额</th>
						<th style="text-align:right;">操作</th>
					</tr>
					<tr class="heading-float fixed hidden" style="top:0;"></tr>
					
					<s:if test="#request['monthSettles'].size() > 0">
						<s:iterator value="#request['monthSettles']" var="iterObj" status="status" >
						<tr <s:if test="%{#status.odd}">style="background:#F2F6F9;"</s:if>>
							<td title="${iterObj.linkName}: ${iterObj.email}">
								<div class="spacer5"></div>
								<span title="${iterObj.email}"><core:partOfText value="${iterObj.email}" textmaxlength="30"/></span>
								<div class="spacer5"></div>
								联系人：<span title="${iterObj.linkName}"><core:partOfText value="${iterObj.linkName}" textmaxlength="30"/></span>
								<div class="spacer5"></div>
							</td>
							<td title="${iterObj.receiveName}"><core:partOfText value="${iterObj.receiveName}" textmaxlength="30"/></td>
							<td title="${iterObj.receiveAccount}"><core:partOfText value="${iterObj.receiveAccount}" textmaxlength="30"/></td>
							<td style="text-align:center;">支付宝</td>
							<td class="textRight">¥${iterObj.commissionDouble}</td>
							<td style="text-align:right;">
								<s:if test="%{#request.currentPageRole == 'admin'}">
								    <a class="bLink" href="${cxt_path}/settlement/detail?userId=${iterObj.userId}">查看</a>&nbsp;
								</s:if>
								<s:if test="%{#request.currentPageRole == 'finance'}">
								    <s:if test="#iterObj.commission > 10000">
	                                    <span class="bLink payBtn hand" userId="${iterObj.userId}">支付</span>
	                                </s:if>
								    <a class="bLink" href="${cxt_path}/finance/settles/detail?userId=${iterObj.userId}">查看</a>&nbsp;
								</s:if>
							</td>
						</tr>
						</s:iterator>
					</s:if>
					<s:else>
	                    <tr>
	                        <td colspan="6" class="no-data">暂无数据</td>
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
</div>
<div class="clear"></div>
<div id="popWin" class="popWin" style="display:none"></div>
<div id="adsWindowPopDiv">
		<table class="pay_table">
			<tr>
				<td>应付金额</td>
				<td id="totalcom"></td>
			</tr>
			<tr>
				<td>支付金额</td>
				<td><input id="payCommission" style="width:150px;" type="text" required="required" class="validator bTextbox" disabled="disabled" /> 元</td>
			</tr>
			<tr>
				<td>流水号</td>
				<td><input id="payNumber" style="width:150px;" type="text" required="required" class="validator bTextbox" /></td>
			</tr>
			<tr>
				<td>手续费</td>
				<td><input id="payFee" class="bTextbox" style="width:150px;" type="number" step="1" min="0"  value="" /> 元</td>
			</tr>
			<tr>
				<td>税</td>
				<td><input id="payTax" class="bTextbox" style="width:150px;" type="number" step="1" min="0" value="" /> 元</td>
			</tr>
			<tr>
				<td style="vertical-align:top;">备注</td>
				<td><textarea id="payComment" class="bTextbox" style="width:280px;height:60px;"></textarea></td>
			</tr>
			<tr>
				<td></td>
				<td><input id="payBtn" type="button" class="bButton lightOrange" style="" value="提交" />&nbsp;<input type="button" class="bButton backBtn " style="margin-left:20px;" value="取消" /></td>
			</tr>
		</table>
	</div>
<link rel="stylesheet" href="${css_path}/calendar-date-skin1.css" type="text/css" />
<link rel="stylesheet" href="${css_path}/pagination.css" type="text/css" />
<link rel="stylesheet" href="${css_path}/popWin.css" />
<script type="text/javascript" src="${js_path}/libs/popWin.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.min.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/libs/jquery.pagination.js"></script>
<script type="text/javascript" charset="utf-8">
	var elemTop = $(".bTable .heading").offset().top;
	
	function isScrolledOut($elem) {
	    var docViewTop = $(window).scrollTop();
	    return elemTop <= docViewTop;
	}

	$(function() {
		
		//create popWin
		var data={
				container:$("#popWin"),
				width:500,
				height:440,
				title:"支付操作",
				content:$("#adsWindowPopDiv"),
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
		};
		
		// init date pickers
        $.tools.dateinput.localize("zh", {
            months: "一月,二月,三月,四月,五月,六月,七月,八月,九月,十月,十一月,十二月",shortMonths: "一,二,三,四,五,六,七,八,九,十,十一,十二",days: "星期日,星期一,星期二,星期三,星期四,星期五,星期六",shortDays: "周日,周一,周二,周三,周四,周五,周六"
        });

        $(":date").dateinput({ trigger: true, format: 'yyyy-mm', lang: 'zh', firstDay: 1, selectors: true });
        $(":date").bind("onShow onHide", function()  {
            $(this).parent().toggleClass("active"); 
        });
	        
	    $("#calmonth").add("#calyear").bind("change",function(){
	    	var _months = String(parseInt($("#calmonth").val())+1).length > 1 ? parseInt($("#calmonth").val())+1 : "0" + (parseInt($("#calmonth").val())+1);
	    	$("#settleMonthPicker").val($("#calyear").val() + "-" + _months);
	   });
	    
	    $("#calprev").add("#calnext").bind("mouseup",function(){
	    	setTimeout(function(){$("#calmonth").change();},0);
	    });
	    
	    $("input").add("select").bind("change",function(){},false);
	    
		
		$("#leftmenu-finance-payments").addClass("active");
		$("#topmenu-finance-pay").addClass("active");
		
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
        
        //mask
    	function maskShow(){
    		var _hei = $(document).height();
    		$("#maskDiv").css({height:_hei+"px"});
    		$("#maskDiv").show();
    	}

    	function maskHide(){

    		$("#maskDiv").hide();
    	}
    	//action function here
    	$("#closeBtn").live("click",function(){
    		$("#adsWindowPop").removeClass("showPop");
    		$("select").show();
    		maskHide();
    		$(".error").hide();
    	});
    	
    	//back btn
    	$(".backBtn").live("click",function(){
    		popwin.close();
    		
    	});
        //pay page show
        $(".payBtn").live("click",function(){
        	var userId = $(this).attr("userId"),
        	totalcom = $(this).parent().prev().html();
        	$("#totalcom").html(totalcom);
        	$("#payCommission").val(totalcom.replace(new RegExp(",","gm"),"").substr(1));
        	$("#payFee").val("0.00");
        	$("#payTax").val("0.00");
        	$("#payNumber").val("");
        	$("#payBtn").attr("userId",userId);
        	popwin.show();
        });
        
        $.tools.validator.localize('zh', {
    		'[maxlength]'     : '长度超过限制',
    		'[required]': '不能为空',
    		':number': '必须为数字'
    	    });
        
    	$.tools.validator.conf.lang = "zh";
    	$.tools.validator.conf.offset = [0,0];
    	
    	$.tools.validator.fn("[type=number]", function(input, value) {
    	    return /^\d+.[0-9]{0,2}$/.test(value) ? true : {
    		zh: "最多2位小数"
    	    };
    	});
    	
    	//auto count payCommission
    	$("#payFee").add("#payTax").bind("blur",function(){
    		
			var totalcom = parseFloat($("#totalcom").html().replace(new RegExp(",","gm"),"").substr(1)),
			payFee = $("#payFee").val(),
			payTax = $("#payTax").val(),
			countTotalcom = 0;
			if(parseFloat($(this).val()) < 0 || $(this).val() == ''){
				$(this).val("0.00");
				return;
			}else if($(this).val().indexOf(".") != -1){
				$(this).val($(this).val().split(".")[0] + "." + $(this).val().split(".")[1].substr(0,2));
			}
			payFeeFloat = parseFloat(payFee.indexOf(".") != -1 ? payFee.split(".")[0] + "." + payFee.split(".")[1].substr(0,2) : payFee);
			payTaxFloat = parseFloat(payTax.indexOf(".") != -1 ? payTax.split(".")[0] + "." + payTax.split(".")[1].substr(0,2) : payTax);
			if(payTaxFloat + payFeeFloat > totalcom){
				$(this).val("0.00"); return;
			}
			countTotalcom = (totalcom.mul(100) - payFeeFloat.mul(100) - payTaxFloat.mul(100))/100;
			//$("#totalcom").html("¥ "+countTotalcom);
			$("#payCommission").val(countTotalcom);
    	});
        
        //pay action
        $("#payBtn").live("click",function(){
			var userId = $(this).attr("userId"),
			payCommission = $("#payCommission").val(),
			payNumber = $("#payNumber").val(),
			payFee = $("#payFee").val(),
			payTax = $("#payTax").val(),
			payComment = $("#payComment").val(),
			month = $("#currentDate").val();
			var inputs = $(".validator").validator(),
			isPass = inputs.data("validator").checkValidity();
			if(!isPass) return;
			$.ajax({
				type: "POST",
				url: "${cxt_path}/finance/pay",
				dataType: "json",
				data: "settleMonth="+ month +"&payment.userId=" + userId + "&paid=" + payCommission + "&payment.receiptNo=" + payNumber + "&fee=" + payFee + "&tax=" + payTax + "&payment.comment=" + payComment,
				success: function(callback){
					if(callback.success){
						displayStatusMessage("支付成功");
						$("#searchForm").submit();
					}else{
						displayStatusMessage(callback.message);
					}
				},
				error:function(msg){
					console.log(msg);
					
				}
			});
        });
        //trim input value
        $("form").live("submit",function(){
            $(this).find("input").each(function(){
                $(this).val($.trim($(this).val()));
            });
            $("#settleMonth").val($("#settleMonthPicker").val() + "-01");
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
                form: $("#searchForm"),
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
