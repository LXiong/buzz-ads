<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>Clear Cache</title>

<div class="container-body">
    <div class="spacer20"></div>
    
    <div>
        <%@ include file="/jsp/admin/revise/LeftMenu.jsp"%>
        
        <div class="container-right">
          <div class="overviewPanel details" style="border-top-width:1px;">
			    <div class="inlineBlock panelTitle">
			    	清理用户缓存
	             </div>
			</div>
           <div class="overview noBT">
	            <div class="clear spacer10"></div>
	            <div><strong>输入用户ID，清空对应的用户和角色缓存</strong></div>
	            <div class="clear spacer15"></div>
	            <form action="flushCache" method="post">
	               	 <span style="line-height: 24px; height: 24px; display:inline-block;margin-right:10px;">用户ID</span>
	               	 <input name="userId" class="bTextbox" style="width:200px;margin-right:10px;" type="text" placeholder="Please enter a user ID..." />
	               	 <input class="bButton bButton24" type="submit" value="Clear Cache" />
	            </form> 
           </div>
        </div>
        <div class="clear"></div>
    </div>
    <div class="clear spacer20"></div>
</div>
<div class="clear"></div>

<script type="text/javascript" charset="utf-8">
    $(function() {
        $("#leftmenu-admin-cache").addClass("active");
        $("#topmenu-admin-revise").addClass("active");
    });
</script>