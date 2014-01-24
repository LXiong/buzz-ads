<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<%
String path = (String) request.getAttribute("javax.servlet.forward.request_uri");
String cxtPath = request.getContextPath();
path = path.substring(cxtPath.length());
request.setAttribute("curPath", path);
%>

<title>修改账户信息</title>

<style>
    .error{color:red;}
</style>

<div class="container-body">
    <div class="spacer20"></div>
    <div>
        <s:if test="%{#request.curPath.startsWith('/advertiser')}">
            <sec:authorize ifAnyGranted="ROLE_ADVERTISER">
            <%@ include file="/jsp/advertiser/settings/LeftMenu.jsp"%>
            </sec:authorize>
        </s:if>
        <s:elseif test="%{#request.curPath.startsWith('advertiser')}">
            <sec:authorize ifAnyGranted="ROLE_ADVERTISER">
            <%@ include file="/jsp/advertiser/settings/LeftMenu.jsp"%>
            </sec:authorize>
        </s:elseif>
        <s:else>
            <sec:authorize ifAnyGranted="ROLE_AD_ADMIN">
            <%@ include file="/jsp/admin/user/LeftMenu.jsp"%>
            </sec:authorize>
        </s:else>
        
        <div class="container-right " style="overflow:auto;">
        <div class="overviewPanel details" style="border-top-width:1px;">
			    <div class="inlineBlock panelTitle">联系人操作</div>
		</div>
		<div class="overview noBT">
        <s:form id="advertiserAccount" action="saveContact" method="post" name="advertiserAccount" onsubmit="verifyInputs();">
            <input type="hidden" name="advertiserContactInfo.advertiserId" value="${advertiserAccount.advertiserId}"/>
            <table class="viewTable" style="width:100%">
                <tr>
                    <td style="width:100px;font-weight:bold;">广告主ID：</td>
                    <td style="font-weight:bold;">${advertiserAccount.advertiserId}</td>
                </tr>
                <tr>
                    <td>公司名称：</td>
                    <td>${advertiserAccount.companyName}</td>
                </tr>
                <tr>
                    <td>网站名称：</td>
                    <td>${advertiserAccount.websiteName}</td>
                </tr>
                <tr>
                    <td>网站URL：</td>
                    <td>${advertiserAccount.websiteUrl}</td>
                </tr>
                <tr><td></td><td></td></tr>
                <tr>
                    <td>联系人：</td>
                    <td><input type="text" required="required" placeholder="请输入联系人..." name="advertiserContactInfo.name" id="name" value="${advertiserContactInfo.name}" class="validator bTextbox" maxlength="20"/></td>
                </tr>
                <tr>
                    <td>Email：</td>
                    <td><input type="email" required="required" placeholder="请输入Email..." name="advertiserContactInfo.email" id="email" value="${advertiserContactInfo.email}" class="validator bTextbox" maxlength="30"/></td>
                </tr>
                <tr>
                    <td>手机：</td>
                    <td><input type="mobile" required="required" placeholder="请输入电话..." name="advertiserContactInfo.mobile" id="mobile" value="${advertiserContactInfo.mobile}" class="validator bTextbox" maxlength="30"/></td>
                </tr>
                <tr>
                    <td>QQ：</td>
                    <td><input type="text" placeholder="请输入QQ号..." name="advertiserContactInfo.qq" id="qq" value="${advertiserContactInfo.qq}" class="validator bTextbox" maxlength="30"/></td>
                </tr> 
                <tr>
                    <td>地址：</td>
                    <td><input type="text" required="required" placeholder="请输入地址..." name="advertiserContactInfo.address" id="address" value="${advertiserContactInfo.address}" class="validator bTextbox" maxlength="50"/></td>
                </tr>
	            <tr>
	                <td><s:hidden name="advertiserContactInfo.id" value="%{advertiserContactInfo.id}"/></td>
                    <td>
                        <div class="spacer15"></div>
                        <s:submit cssClass="bButton center lightOrange" value="保存" />
                        <input class="bButton center" style="margin-left: 20px;" type="button" value="返回" onclick="history.go(-1)" />
                    </td>
	            </tr>
		    </table>
            </s:form>
         </div>
        </div>
        <div class="clear spacer20"></div>
        <div class="clear spacer20"></div>
    </div>
</div>

<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.min.js"></script>
<script type="text/javascript" charset="utf-8" src="${js_path}/tools/jquery.tools.forms.min.1.2.6.js"></script>
<script type="text/javascript">

function htmlEncode(text) { 
    return text.replace(/&/g, '&amp').replace(/\"/g, '&quot;').replace(/\'/g, '&#39;').replace(/</g, '&lt;').replace(/>/g, '&gt;'); 
}

    var $validator;
    function verifyInputs() {
        if(!$validator.data("validator").checkValidity()) return false;
        return true;
    }
    
    $(function(){
        $.tools.validator.localize('zh', {
            '*'         : '无效的输入',
            ':email'    : 'Email无效',
            ':number'   : '无效的输入',
            ':url'      : 'URL无效',
            '[max]'     : '无效的输入',
            '[min]'     : '无效的输入',
            '[maxlength]' : '长度超过限制',
            '[required]': '不能为空'
            });
        $.tools.validator.conf.lang = "zh";
        $validator = $(".validator").validator();
    
        $("#leftmenu-advertiser-accountInfo").addClass("active");
        $("#topmenu-advertiser-settings").addClass("active");
        
        $("#leftmenu-admin-advertiserManage").addClass("active");
        $("#topmenu-admin-manage").addClass("active");
        
        //checkbox control selected list show or hide
        $("input[name='advertiserAccount.businessTypeValue']").live("click",function(){
            var add = $(this).attr("checked") == "checked" ? true : false, val = $(this).val();
            syncBusinussTypeChange(val,add);
        });
        
        //closeButton delete & control the checkbox is selected
        $(".closeButton").live("click",function(){
            var val = $(this).parent().attr("type");
            syncBusinussTypeChange(val,false);
        });
        
        //business type
        function syncBusinussTypeChange(val,add){
            var _currentCheckbox, _currentList;
            $("input[name='advertiserAccount.businessTypeValue']").each(function(){
                if($(this).val() == val){
                    if(!!add){
                        $(this).attr("checked",true);
                        $(".selectedType ul").append("<li type='" + val + "'>" + $(this).attr("text") + "<div class='closeButton'></div></li>");
                    }else{
                        $(this).attr("checked",false);
                        $(".selectedType li").each(function(){
                            if($(this).attr("type") == val){
                                $(this).detach();
                                return;
                            }
                        });
                    }
                }
            });
        }
        
        //validator
        $.tools.validator.localize('zh', {
            '[maxlength]'     : '长度超过限制',
            '[required]': '不能为空',
            ':email': '请输入email格式'
            });
        $("#advertiserAccount").validator({ lang: 'zh' });

        $.tools.validator.fn("[type=mobile]", function(input, value) {
            return /^((1[3-9]{1})+\d{9})$/.test(value) ? true : {
            zh: "请填写正确的手机号"
            };
        });
        
        //trim input value
        $("form").live("submit",function(){
            $(this).find("input").each(function(){
                var _val = $(this).val();
                _var = $.trim(_val);
                //_var = htmlEncode(_val);
                _var = _var.substring(0,49);
                $(this).val(_var);
            });
        });
    });
</script>
