<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>广告过滤设置</title>

<div class="container-body">
    <div class="spacer20"></div>
    <div>
        <%@ include file="/jsp/publisher/ads/LeftMenu.jsp"%>
        <div class="container-right" style="overflow:auto;">
        <div class="overviewPanel shadow">
            <div class="panelTitle inlineBlock">广告过滤</div>
         	<form id="uuidForm" method="get" theme="simple" action="ads" class="inlineBlock mSpacer_left10" >
         		<s:select list="%{#request.sites}" name="uuid" listKey="uuid" listValue="url" value="uuid" id="uuid"  cssClass=" bTextbox" cssStyle="width:300px;"   ></s:select> 
         	</form>    
        </div>
        <div class="overview" style="border-top:0px">
        	<s:form id="setKeyWords" name="setKeyWords" action="ads/keywords"  method="post" theme="simple" > 
	         	<span class="bLabel">关键字： </span> 
	            <input type="text" class="bTextbox" name="blackKeywords" value="${publisherSiteConfig.blackKeywords}"  placeholder="请输入关键字,如:汽车,运动  多个关键词用英文逗号",'分隔'"/>
	            <input type="hidden" name="uuid" class="key_uuid" />
	            <input type="submit"  class="bButton center lightOrange mSpacer_left10" id="set"  value="设置" />
        	</s:form>
        	<div class="clear spacer10"></div>
        	<s:form id="addBlackDomain" name="addBlackDomain" action="ads/domain/add"   method="post" theme="simple" onsubmit="return false;">
		         <span class="bLabel">域名：</span>
		         <input type="text" class="bTextbox" name="domain" value="" placeholder="请输入域名,如:admax.buzzinate.com"/>
		         <input type="hidden" name="uuid" class="key_uuid" />
		          <input type="submit"  class="bButton center lightOrange mSpacer_left10" id="addDomain" value="添加" />
		          <span style="display:none" class="primary mSpacer_left10" id="addDomainMsg"></span>
	        </s:form>
	        <div class="clear spacer10"></div>
	        <s:form id="addBlackLink" name="addBlackDomain" action="ads/domain/add"  method="post" theme="simple" onsubmit="return false;" >
				 <span class="bLabel">广告链接： </span>  
	             <input type="text" class="bTextbox" name="entryLink" value="" placeholder="请输入连接,如:admax.buzzinate.com/car" />
	             <input type="hidden" name="uuid" class="key_uuid" />
	             <input type="submit"  class="bButton center lightOrange mSpacer_left10" id="addLink" value="添加" />
	             <span style="display:none" class="primary mSpacer_left10" id="addLinkMsg"></span>
	        </s:form>
        </div>
        <div class="overviewPanel details">
            <div class="panelTitle inlineBlock">过滤展示</div>
        </div>
           <div class="overview" style="border-top:0px">
               
                <div class="inlineBlock" style="width:48%">
                	  <h3>域名</h3>
                	  <div class="clear spacer10"></div>
	                <s:if test="#request['publisherSiteConfig.blackDomains'].size() > 0">
	                    <s:iterator value="#request['publisherSiteConfig.blackDomains']" var="blackDomain" status="status">
	                        <s:if test="#blackDomain.type.code == 0">
	                          <div class="left mSpacer_left10">
		                          <span class="closeCon"> ${blackDomain.url} <a class="closeLink" href="${cxt_path}/publisher/ads/domain/remove?blackId=${blackDomain.id}"></a></span>
		                          <div class="clear spacer10"></div> 
	                           </div>
	                        </s:if>
	                    </s:iterator>
	                </s:if>
                </div>       
                
               <div class="inlineBlock" style="width:48%; vertical-align: top;">
               		<h3>广告链接</h3>
               		<div class="clear spacer10"></div>
	                <s:if test="#request['publisherSiteConfig.blackDomains'].size() > 0">
	                    <s:iterator value="#request['publisherSiteConfig.blackDomains']" var="blackDomain" status="status">
	                         <s:if test="#blackDomain.type.code == 1">
	                          <div class="left mSpacer_left10">
	                           <span class="closeCon"> ${blackDomain.url} <a class="closeLink" href="${cxt_path}/publisher/ads/domain/remove?blackId=${blackDomain.id}"></a></span>
	                           <div class="clear spacer10"></div> 
	                           </div>
	                        </s:if>
	                    </s:iterator>
	                </s:if>
               </div>                                      
           </div>
        </div>
        <div class="clear spacer20"></div>
    </div>
</div>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.forms.min.1.2.6.js"></script>
<script type="text/javascript">
	$(function(){
		//menu active
		$("#topmenu-publisher-ads").addClass("active");
		$("#leftmenu-publisher-ads").addClass("active");
		$(".bButton").click(function(){
			$(".key_uuid").val($("#uuid").val());
		});
		$("#uuid").change(function(){
			$("#uuidForm").submit();
		});
		$("#addLink").click(function(){
			if($("input[name='entryLink']").val()!=""){
				$("#addLinkMsg").hide().html("");
				$("#addBlackLink").submit();
			}else{
				$("#addLinkMsg").show().html("请输入过滤连接");
			}
		});
		$("#addDomain").click(function(){
			if($("input[name='domain']").val()!=""){
				$("#addDomainMsg").hide().html("");
				$("#addBlackDomain").submit();
			}else{
				$("#addDomainMsg").show().html("请输入过滤域名");
			}
		});
	});
</script>

