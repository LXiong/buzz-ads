<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>广告明细</title>
<div class="container-body">
	<div class="spacer20"></div>
	<div>
		<%@ include file="/jsp/common/LeftMenu.jsp"%>
		<div class="container-right" style="overflow:auto;">
		   	<div class="overview">
		   		<table class="bTable info" style="font-size:14px;">	
		   			<tr>
                        <td><span>广告标题: <font class="spacer_left5" style="font-size:14px;font-weight:bold;">[转]年轻时免不了一场颠沛流亡</font></span></td>
                    </tr>
                    <tr>
                        <td><span>广告连接: <font class="spacer_left5 fontSize12">http://123.com</font></span></td>
                    </tr>
                    <tr>
                        <td><span>选择月份: <font class="primary spacer_left5 fontSize12">12月</font></span></td>
                    </tr>
                     <tr>
                        <td><span>结算方式: <font class="spacer_left5 fontSize12">CPS</font></span></td>
                    </tr>
                     <tr>
                        <td><span>单<span style="padding-left:24px;" ></span>价: <font class="spacer_left5 fontSize12">20.00</font></span></td>
                    </tr>
                    <tr>
                        <td><span>有效计次: <font class=" spacer_left5 fontSize12">20 次</font></span></td>
                    </tr>
                    <tr>
                        <td><span>已确认金额: <font class="primary spacer_left5 fontSize12">12313.00</font></span></td>
                    </tr>
                     <tr>
                        <td><span>未确认金额: <font class="primary spacer_left5 fontSize12">12313.00</font></span></td>
                    </tr>
		   		</table>
			    <div class="clear spacer30"></div>
		        <div>
		        	<input class="bButton left"  type="submit" style="width:50px;" value="全部" />   
		        	<input class="bButton left  mSpacer_left10"  type="submit" style="width:50px;" value="已确认" />   
		        	<input class="bButton left mSpacer_left10"   type="submit" style="width:50px;" value="未确认" />   
		        	<input class="bButton left  mSpacer_left10"  type="submit" style="width:50px;" value="退单" />  
		        </div>
		        
		        <div class="clear spacer20"></div>
		        <table class="bTable">
		        	<tr class="heading">
		        		<th style="width:50px;">序号</th> 
		        		<th>网站</th> 
		        		<th>时间</th>
		        		<th>状态</th>
		        	</tr>
		        	<tr>
		        		<td>1</td>
		        		<td>www.bshare.cn</td>
		        		<td>2012/12/21 21:12:00</td>
		        		<td>未确认</td>
		        	</tr>
		        	<tr>
		        		<td>2</td>
		        		<td>www.bshare.cn</td>
		        		<td>2012/12/21 21:12:00</td>
		        		<td>已确认</td>
		        	</tr>
		        	<tr>
		        		<td>3</td>
		        		<td>www.bshare.cn</td>
		        		<td>2012/12/21 21:12:00</td>
		        		<td>退单</td>
		        	</tr>
		        </table>
		        <div class="clear spacer15"></div>
		   	</div>
		</div>
		<div class="clear spacer20"></div>
	</div>
</div>
<link rel="stylesheet" href="${css_path}/calendar-date.css" type="text/css" />

