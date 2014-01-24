<%@ include file="/jsp/common/Init.jsp"%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<div style="margin-top: 100px; text-align: center; font-family: Microsoft Yahei; font-size: 16px;">
登录成功! 请稍候...
</div>

<script type="text/javascript" charset="utf-8">

var _callback;
if (window.opener && window.opener.parent && window.opener.parent.loginSuccess) {
    _callback = window.opener.parent.loginSuccess;
} else if (window.opener && window.opener.loginSuccess) {
    _callback = window.opener.loginSuccess;
} else if (window.parent && window.parent.loginSuccess) {
    _callback = window.parent.loginSuccess;
}

if (!!_callback) {
    _callback();
}

window.close();

</script>
</body>
</html>