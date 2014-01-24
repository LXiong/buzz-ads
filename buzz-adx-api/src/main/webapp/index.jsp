<%@ include file="/jsp/common/Init.jsp" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" %>


<title>AdMax – 国内首家社交定向广告平台</title>
<meta name="description" content="description" />
<meta name="keywords" content="keywords" />

<style>
.posA { position: absolute; }
.posR { position: relative; }
.center { margin: 0 auto; }
.txtC { text-align: center; }

.heading2 { font-weight: normal; }
.container-main { height: 384px; border-bottom: 1px solid #dadada; background: #fff; overflow: hidden; }
#bannerWrap { height: 384px; width: 980px; font-family: Microsoft Yahei; }
.regBtn { display: inline-block; bottom: 40px; left: 330px; font-size: 20px; text-decoration: underline; }
.regBtn i { display: inline-block; height: 24px; width: 24px; background: url(${image_path}/arrow-orange.jpg) 0 0; vertical-align: top; margin-left: 5px; }
.regBtn:hover { color: #fa8539; }
.regBtn:hover i { background-position: 0 -24px; }

#loginWrap { top: 0; left: 0; }
#loginInner { border: 1px solid #dadada; height: 302px; padding-top: 60px; background: #f8f8f8; border-radius: 3px; } 
#loginInner iframe { height: 300px; width: 100%; }
.loginLabel { left: -5px; top: 20px; width: 147px; height: 40px; background: url(${image_path}/home-label.gif) no-repeat; }
    
#featureWrap { width: 100%; border-collapse: collapse; font-family: Microsoft Yahei; }
#featureWrap td { width: 25%; border-style: dashed; border-color: #dadada; border-width: 1px 0; padding: 10px 0; font-size: 16px; color: #555; text-align: center; }
</style>

<div class="container-main">
    <div class="container-center posR">
        <div id="bannerWrap" class="posR left">
            <div class="posA" style="right: 0; top: 80px;"><img src="${image_path}/banner/p/intro-1.jpg" /></div>
            <div class="posA" style="font-size: 50px; color: #888; top: 30px; left: 330px;">依托乐知&nbsp;<font class="orange" 
                style="font-weight: bold;">精准</font>&nbsp;推荐</div>
            <div class="posA" style="font-size: 50px; color: #555; top: 120px; left: 380px;">挖掘剩余流量&nbsp;<font class="orange" 
                style="font-weight: bold;">价值</font></div>
            <a class="regBtn orange posA" href="${cxt_path}/register">立即注册<i></i></a>
        </div>
        <div id="tagWrap" class="posA"></div>
        <div id="loginWrap" class="posA">
            <div class="spacer10"></div>
            <div id="loginInner" class="posR">
                <div class="loginLabel posA"></div>
                <iframe scrolling="no"; frameborder="0" 
                src="${one_cxt_path}/iframeLogin?enableOauth=false&amp;service=${main_cxt_path}/authentication&amp;targetUrl=${main_cxt_path}/loginCallback&amp;source=admax"></iframe>
                <div class="spacer5"></div>
                <!-- <a class="regLink bLinkOrange posA" href="${cxt_path}/register">立即注册</a> -->
                <div class="clear"></div>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>

<div class="clear spacer15"></div>
<div class="container-center">
	<table id="featureWrap">
	    <tr>
	        <td>深入的用户人群分析</td>
	        <td>领先的广告优化算法</td>
	        <td>精准售卖资源</td>
	        <td>转化率高达0.5%~1%</td>
	        <td></td>
	    </tr>
	</table>
</div>
<div class="clear"></div>

<script type="text/javascript" charset="utf-8">
function loginSuccess() {
    $(".regLink").hide();
    window.location.href = "${main_cxt_path}/login";
}
$(function() {

});
</script>