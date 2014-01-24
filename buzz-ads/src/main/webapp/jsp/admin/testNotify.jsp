<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<title>Admin Test</title>
<h1>请填写正确的userinfo信息：uuid_adentryId</h1>
<form action="${cxt_path}/admin/test/generate" method="post">
    <textarea rows="10" cols="150" name="source">date=20121106&time=000000&promotionID=21&comm=15.0000&totalPrice=0.0000&ocd=Test2333352&promotionName=admin%E4%B8%AD%E6%96%87test&type=1&userinfo=3d342389-ab30-4f7a-8adf-ca5236e49d49_10&extinfo=&status=0&paid=2&confirm=0</textarea>
    <br>
    <input type="submit"  value="生成通知url">
</form>


<s:if test="notifyUrl != null">
    <textarea rows="10" cols="150">http://ads.buzzinate.com/api/chanet/notify?${notifyUrl }</textarea>
</s:if>
