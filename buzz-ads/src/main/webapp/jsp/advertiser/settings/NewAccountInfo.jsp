<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>新账户信息</title>

<div class="container-body">
    <div class="spacer20"></div>
    <div>
        <%@ include file="/jsp/advertiser/settings/LeftMenu.jsp"%>
        <div class="container-right" style="overflow:auto;">
	        <div class="warningMessage div-rounded-5" >
	            <p>在使用BuzzAds广告平台服务之前，请先完善帐号信息.</p>
	        </div>
	        <div class="clear spacer10"></div>
        <s:form id="advertiserAccount" action="save" method="post" name="advertiserAccount" onsubmit="verifyInputs();">
            <table class="viewTable  overview" style="width:100%">
                <tr><td class="heading4" style="color:#f60;">帐户信息</td><td></td></tr>
                <tr>
                    <td style="width:100px;font-weight:bold;">广告主ID：</td>
                    <td style="font-weight:bold;">${advertiserAccount.advertiserId}</td>
                </tr>
                <tr>
                    <td>公司名称：</td>
                    <td><input type="text" required="required" placeholder="请输入公司名称..." name="advertiserAccount.companyName" id="companyName" value="" class="validator bTextbox" maxlength="50"/></td>
                </tr>
                <tr>
                    <td>网站名称：</td>
                    <td><input type="text" required="required" placeholder="请输入网站名称..." name="advertiserAccount.websiteName" id="websiteName" value="" class="validator bTextbox" maxlength="30"/></td>
                </tr>
                <tr>
                    <td>网站URL：</td>
                    <td><input type="url" required="required" placeholder="请输入网站URL..." name="advertiserAccount.websiteUrl" id="websiteURL" value="" class="validator bTextbox" maxlength="30"/></td>
                </tr>
                
                <!--tr>
                    <td>经营种类：</td>
                    <td><input type="text" required="required" name="advertiserAccount.businessTypeValue" id="businessType" value="%{advertiserAccount.businessTypeValue}" class="validator bTextbox" maxlength="30"/></td>
                    
                    <td>
                        <table>
                            <tr>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="1" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="2" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="3" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="4" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="5" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="6" /></td>
                                <td>BS1</td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="7" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="8" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="9" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="10" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="11" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="12" /></td>
                                <td>BS1</td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="13" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="14" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="15" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="16" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="17" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="18" /></td>
                                <td>BS1</td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="19" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="20" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="21" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="22" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="23" /></td>
                                <td>BS1</td>
                                <td><input type="checkbox" text="BS1" name="advertiserAccount.businessTypeValue" value="24" /></td>
                                <td>BS1</td>
                            </tr>
                            
                        </table>
                        <div class="selectedType">
                            <ul>
                                
                            </ul>
                        </div>
                    </td>
                </tr>
                -->
                
                <!-- THIS IS THE ACCOUNT CONTACTS, THERE CAN BE MULTIPLE CONTACTS -->
                <tr><td></td><td></td></tr>
                <tr><td class="heading4" style="color:#f60;">联系人信息</td><td></td></tr>
                <tr>
                    <td>联系人：</td>
                    <td><input type="text" required="required" placeholder="请输入联系人..." name="advertiserContactInfo.name" id="name" value="" class="validator bTextbox" maxlength="20"/></td>
                </tr>
                <tr>
                    <td>Email：</td>
                    <td><input type="email" required="required" placeholder="请输入Email..." name="advertiserContactInfo.email" id="email" value="" class="validator bTextbox" maxlength="30"/></td>
                </tr>
                <tr>
                    <td>手机：</td>
                    <td><input type="mobile" required="required" placeholder="请输入电话..." name="advertiserContactInfo.mobile" id="mobile" value="" class="validator bTextbox" maxlength="30"/></td>
                </tr>
                <tr>
                    <td>QQ：</td>
                    <td><input type="text" placeholder="请输入QQ号..." name="advertiserContactInfo.qq" id="qq" value="" class="validator bTextbox" maxlength="30"/></td>
                </tr>
                <tr>
                    <td>地址：</td>
                    <td><input type="text" required="required" placeholder="请输入地址..." name="advertiserContactInfo.address" id="address" value="" class="validator bTextbox" maxlength="50"/></td>
                </tr>
                
                <tr><td></td><td></td></tr>
                <tr><td></td><td></td></tr>
                <tr>
                    <td style="vertical-align:top;">用户条款：</td>
                    <td>
                        <textarea style="width:90%;height:200px;background:#f8f8f8;" disabled="disabled">使用本服务之用户知悉以下条款，
1.用户遵守中华人民共和国相关法律法规，包括但不限于《中华 人民共和国计算机信息系统安全保护条例》、《计算机软件保护条例》、《最高人民法院关于审理涉及计算机网络著作权纠纷案件适用法律若干问题的解释(法释 [2004]1号)》、《全国人大常委会关于维护互联网安全的决定》、《互联网电子公告服务管理规定》、《互联网新闻信息服务管理规定》、《互联网著作权 行政保护办法》和《信息网络传播权保护条例》等有关计算机互联网规定和知识产权的法律和法规、实施办法。
2.不得利用本服务危害国家安全、泄露国家秘密，不得侵犯国家社会集体的和公民的合法权益，不得利用本服务制作、复制、查阅和传播下列信息：
(1) 违反宪法确定的基本原则的；
(2) 危害国家安全，泄漏国家机密，颠覆国家政权，破坏国家统一的；
(3) 损害国家荣誉和利益的；
(4) 煽动民族仇恨、民族歧视，破坏民族团结的；
(5) 破坏国家宗教政策，宣扬邪教和封建迷信的；
(6) 散布谣言，扰乱社会秩序，破坏社会稳定的；
(7) 散布淫秽、色情、赌博、暴力、恐怖或者教唆犯罪的；
(8) 侮辱或者诽谤他人，侵害他人合法权益的；
(9) 煽动非法集会、结社、游行、示威、聚众扰乱社会秩序的；
(10) 以非法民间组织名义活动的；
(11) 含有法律、行政法规禁止的其它内容的。
3.使用本服务之用户，同意本公司搜集分享及转贴之统计信息，本公司承诺不公布涉及隐私之注册资料，但下列情况除外：
(1) 事先获得用户的明确授权；
(2) 根据有关的法律法规要求；
(3) 按照相关政府主管部门的要求；
(4) 为维护社会公众的利益。
4.有关版权议题，本服务仅负责出示原出处之联结，如原内容或转载内容触及版权侵犯，本服务不负责任。
5.用户同意互相尊重，对自己的言论和行为负责。
(1) 发布信息时必须遵守中华人民共和国的各项有关法律法规，遵守网上一般道德及规范；
(2) 承担一切因不当行为而直接或间接导致的民事或刑事法律责任,所产生的不良后果与本服务无关；
(3) 不干扰或扰乱网络服务。
6.本服务有权对于严重损害用户权益（包括但不限于，分享次数超越平台及用户可接受范围、分享内容遭用户反映为垃圾讯息或分享讯息与原内容差异过大等）、公共权益或乙方利益的内容，乙方有权终止对该网站或用户进行服务，并支持协助互联网有关行政机关等进行追索和查处。
7.本服务可随时根据实际情况中断服务，无需对任何个人或第三方负责而随时中断服务。
8.本服务保留最终解释权。</textarea>
                    </td>
                </tr>
                <tr>
                    <td><s:hidden name="advertiserContactInfo.id" value=""/></td>
                    <td><div class="spacer30"></div><s:submit cssClass="bButton center lightOrange" value="保存" /></td>
                </tr>
            </table>
            </s:form>
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
               // _var = htmlEncode(_val);
                _var = _var.substring(0,49);
                $(this).val(_var);
            });
        });
    });
</script>
