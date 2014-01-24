<%@ include file="/jsp/common/Init.jsp" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<title>404</title>
<div class="container-body">
	<div class="spacer50"></div>
	<div class="errorcon" style="width:980px;margin:auto">
		<div class="left">
			<img alt="" src="${image_path}/404.jpg">
		</div>
		<div class="left spacer_left15 ">
			<span style="color:black;font-weight:bold;font-size:100px;font-family:Arial,宋体,sans-serif;">404</span>
			<span style="color:black;font-size:40px;vertical-align:top;padding-top:10px;line-height:80px;font-weight:bold;">页面不存在！</span>
			<div class="spacer40"></div>
			<div>
				非常抱歉，您指定的页面不存在。 若有其他疑问或是建议，请和我们联系。联系方式如下：
				<div class="clear spacer20"></div>
				<div>
						<a class="bLinkU" title="feedback@bshare.cn" href="mailto:feedback@bshare.cn">feedback@bshare.cn</a>
				</div>
			</div>
			<div class="clear spacer20"></div>
			<div class="heading2">
				谢谢您的合作！
			</div>
			<div class="clear spacer20"></div>
			<div>
				<a class="bLinkU" style="height:24px;line-height:24px;" href="${cxt_path}/">返回到首页</a>
			</div>
		</div>
	</div>
	<div class="clear spacer50"></div>
</div>