<%@ include file="/jsp/common/Init.jsp" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="Content-Language" content="zh"/>
    <meta name="author" content="Buzzinate"/>
    <style type="text/css">
        <!--
        body {
            margin: 0;background-color: transparent;
        }

        .buzzads {
            position: relative;width: ${width}px;height: ${height}px;overflow: hidden;
        }

        a.logo {
            display: block;height: 18px;width: 26px;text-align: justify;letter-spacing: 20px;text-decoration: none;overflow: hidden;cursor: default;position: absolute;bottom: 0px;right: 0px;
        }

        .buzzads a.logo {
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, src="http://p.inte.sogou.com/testgpimg/sogou_icon_short.png", sizingMethod="image");background: url(http://p.inte.sogou.com/testgpimg/sogou_icon_short.png) no-repeat left top;_background: none;
        }

        .buzzads a.logo:hover {
            width: 78px;filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, src="http://p.inte.sogou.com/testgpimg/sogou_icon_long.png", sizingMethod="image");background: url(http://p.inte.sogou.com/testgpimg/sogou_icon_long.png) no-repeat left top;_background: none;
        }
        -->
    </style>
</head>
<body>
<script type="text/javascript" charset="utf-8">
    var d = document;
    var width = "${width}"||"468";
    var height = "${height}"||"60";
    //默认图片，若没有广告则默认显示该图片
//    var imgSrc = "http://mt.inte.sogou.com:8080/marketing/maskimg/" + width + "-" + height + ".jpg";
    var title = "由bShare提供的广告";
    var type = "${type}";


    if (type == 1) {
        //图片类广告
        d.write("<div id='ad_div' class='buzzads'>");
        d.write("<img id='adimg' src='${adimg}' style='z-index:0;position:absolute;' border='0' width='${width}px;' height='${height}px;' margin-top='0px'></img>");
        d.write("<a id='adlink' href='${adurl}' target='_blank' style='vertical-align:middle;cursor:pointer;background:white;filter:alpha(opacity=0);opacity:0;left:0px;top:0px;display:block;z-index:1;width:${width}px;height:${height}px;position:absolute;'>${adtitle}</a>");
    } else if (type == 2) {
        d.write("<div id='ad_div' class='buzzads'>");
        d.write("<a id='adlink' href='${adurl}' target='_blank' style='vertical-align:middle;cursor:pointer;background:white;left:0px;top:0px;display:block;z-index:1;width:${width}px;height:${height}px;position:absolute;'>${adtitle}</a>");
    } else if (type == 3) {
        //flash类广告,目前没有，无视之
    } else {
        //若没有匹配任何类型的广告则显示文字,并指向主网站。
        d.write("<div style='width:468px;height:60px;' class='buzzads'>");
//        d.write("<img id='adimg' style='z-index:0;' src='" + imgSrc + "'></img>");
        d.write("<a id='adlink' style='vertical-align:middle;cursor:pointer;background:white;left:0px;top:0px;display:block;z-index:1;position:absolute;width:468px;height:60px;' href='${main_cxt_path}' target='_blank'>" + title + "</a>");
    }
    <%--d.write("<a id='logo' class='logo' style='z-index:2;' href='${main_cxt_path}' target='_blank'></a>");--%>
    d.write("</div>");
</script>
</body>
</html>