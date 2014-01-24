<%@ include file="/jsp/common/Init.jsp" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" %>


<title>BuzzAds – 国内首家社交定向广告平台</title>
<meta name="description" content="description" />
<meta name="keywords" content="keywords" />

<style>
.posA { position: absolute; }
.posR { position: relative; }
.center { margin: 0 auto; }
.txtC { text-align: center; }

.heading2 { font-weight: normal; }
.container-main { height: 384px; border-bottom: 1px solid #dadada; }
#bannerWrap { height: 384px; width: 980px; }

#loginWrap { top: 0; right: 0; }
#loginInner { border: 1px solid #dadada; height: 302px; padding-top: 60px; background: #f8f8f8; border-radius: 3px; } 
#loginInner iframe { height: 300px; width: 100%; }
.loginLabel { left: -5px; top: 20px; width: 147px; height: 40px; background: url(${image_path}/home-label.gif) no-repeat; }
.regLink { top: 24px; right: 26px; text-decoration: underline; }

#tagWrap { bottom: 10px; left: 0; z-index: 999; }
#tagWrap a { display: inline-block; height: 24px; width: 24px; line-height: 24px; background: #ddd; color: #666; 
    text-align: center; margin: 0 5px; }
#tagWrap a.active { background: #f88000; color: #fff; }

#feature .grid { width: 31%; line-height: 200%; font-size: 12px; color: #333; }
#feature .grid .imgWrap { border: 1px solid #dadada; border-radius: 5px; }
#feature .grid .imgWrap img { width: 100%; }
#feature .grid p { line-height: 200%; font-weight: normal !important; }
#feature .grid ul, #feature .grid li { list-style: none; }

#parter { border-bottom: 2px solid #fdba90; }
#parter .left { margin: 5px; }
</style>

<div class="container-main">
    <div class="container-center posR">
        <div id="bannerWrap" class="left">
            <a data-backcolor="#2a2a2a" href="javascript:;"><img src="${image_path}/banner/a/intro-1.jpg" /></a>
            <a data-backcolor="#9f3a38" href="javascript:;"><img src="${image_path}/banner/a/intro-2.jpg" /></a>
        </div>
        <div id="tagWrap" class="posA"></div>
        <div id="loginWrap" class="posA">
            <div class="spacer10"></div>
            <div id="loginInner" class="posR">
                <div class="loginLabel posA"></div>
                <iframe scrolling="no"; frameborder="0" 
                src="${one_cxt_path}/iframeLogin?enableOauth=false&amp;service=${main_cxt_path}/authentication&amp;targetUrl=${main_cxt_path}/loginCallback&amp;source=buzzads"></iframe>
                <div class="spacer5"></div>
                <a class="regLink bLinkOrange posA" href="${cxt_path}/register">立即注册</a>
                <div class="clear"></div>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>
<div class="clear"></div>

<div id="feature" class="container-center">
    <div class="spacer30"></div>
    <div class="grid left">
        <p class="heading2">精确地覆盖目标</p>
        <div class="spacer10"></div>
        <div class="imgWrap">
            <img src="${image_path}/banner/a/intro-small-1.jpg" />
        </div>
        <div class="spacer10"></div>
        <p class="heading4 txtC" style="color: #666;">...多维用户兴趣图谱，精准定位广告受众...</p>
    </div>
    <div class="grid right">
        <p class="heading2">最新资讯<a class="bLinkOrange right" href=""><font style="font-size: 12px; line-height: 48px;">More...</font></a></p>
        <div class="spacer10"></div>
        <ul>
            <li><p class="txtEllipsis"><a class="bLinkDark" href="" target="_blank">2013-04-22&nbsp;&nbsp;BuzzAds广告系统上线！</a></p></li>
        </ul>
    </div>
    <div class="grid center">
        <p class="heading2">强大的数据支持</p>
        <div class="spacer10"></div>
        <div class="imgWrap">
            <img src="${image_path}/banner/a/intro-small-2.jpg" />
        </div>
        <div class="spacer10"></div>
        <p class="heading4 txtC" style="color: #666;">...领先广告投放算法，最大提高转化率...</p>
    </div>
    <div class="clear"></div>
</div>
<div class="clear"></div>

<div id="parter" class="container-center">
    <div class="spacer50"></div>
    <p class="heading2"><a class="bLinkOrange" href="${cxt_path}/register">期待您的加入...</a></p>
    <div class="clear spacer15"></div>
</div>
<div class="clear"></div>

<script type="text/javascript" charset="utf-8">
var idx = 0, max = $("#bannerWrap").find("a").length, loop;
function switchBanner(i) {
	var tags = $("#tagWrap").find("a"), banners = $("#bannerWrap").find("a"),
	   color = banners.eq(i).attr("data-backcolor");
	tags.removeClass("active");
	tags.eq(i).addClass("active");
    banners.hide();
    $("#bannerWrap").closest(".container-main").css({ "background": color });
    banners.eq(i).fadeIn();
}
function initLoop() {
    loop = setInterval(function () {
    	idx = idx++ >= max - 1 ? 0 : idx;
    	switchBanner(idx);
    }, 5000);
}
function initBanners() {
	var bannerWrap = $("#bannerWrap"), tagWrap = $("#tagWrap");
	bannerWrap.find("a").each(function () {
		tagWrap.append('<a href="javascript:;">' + ($(this).index() + 1) + '</a>');
	});
	tagWrap.find("a").click(function () {
		clearInterval(loop);
		switchBanner($(this).index());
		initLoop();
	});
	tagWrap.find("a").first().trigger("click");
}
function loginSuccess() {
	$(".regLink").hide();
	window.location.href = "${main_cxt_path}/login";
}

$(function() {
	initBanners();
});
</script>