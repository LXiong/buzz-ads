<%
/* *
 功能：付完款后跳转的页面（返回页）
 版本：3.1
 日期：2010-08-27
 说明：
 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

 //***********页面功能说明***********
 创建该页面文件时，请留心该页面文件中无任何HTML代码及空格。
 该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。
 该页面调试工具请使用写文本函数log_result，该函数已被默认开启
 TRADE_FINISHED(表示交易已经成功结束，通用即时到帐反馈的交易状态成功标志);
 TRADE_SUCCESS(表示交易已经成功结束，高级即时到帐反馈的交易状态成功标志);
 该通知页面主要功能是：对于返回页面（return_url.php）做补单处理。如果没有收到该页面返回的 success 信息，支付宝会在24小时内按一定的时间策略重发通知
 //********************************
 * */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*"%>
<%@ page import="com.buzzinate.payment.alipay.config.*"%>
<%@ page import="com.buzzinate.payment.alipay.util.*"%>
${notifyMessage}