
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<s:set var="cxt_path" value="#application.CTX_PATH" />
<s:set var="main_cxt_path" value="#application.MAIN_CTX_PATH" />
<s:set var="one_cxt_path" value="#application.ONE_CTX_PATH" />

<s:set var="css_path" value="%{#application.STATIC_CTX_PATH + '/css'}" />
<s:set var="image_path" value="%{#application.STATIC_CTX_PATH + '/images'}" />
<s:set var="js_path" value="%{#application.STATIC_CTX_PATH + '/js'}" />

<s:set var="jquery_src_path" value="%{#application.STATIC_CTX_PATH + '/js/libs/jquery-1.7.min.js'}" />

