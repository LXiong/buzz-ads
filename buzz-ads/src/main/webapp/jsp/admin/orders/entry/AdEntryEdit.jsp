<%@ include file="/jsp/common/Init.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<title>Ad Entry - Edit</title>

<div class="container-body">
    <div class="spacer20"></div>
    
    <div>
        <%@ include file="/jsp/admin/orders/LeftMenu.jsp"%>
        <div class="container-right">
            <div class="overview">
            	<s:if test="%{#request.adEntry != null && #request.adEntry.id > 0}">
                <form id="adEntryEdit" name="adEntryEditAction" action="${cxt_path}/admin/orders/entry/editAction" method="POST">
                    <table class="bTable form" style="width:50%;">
                        <s:hidden name="id" value="%{#request.adEntry.id}"/>
                        <tr>
                            <td>Ad Entry ID</td>
                            <td><input name="id2" type="number" min="1" class="bTextbox" value="${request.adEntry.id}" disabled="disabled"/></td>
                        </tr>
                        <tr>
                            <td>Ad Order ID</td>
                            <td><input name="orderId" type="number" min="1" class="bTextbox" value="${request.adEntry.orderId}" disabled="disabled"/></td>
                        </tr>
                        <tr>
                            <td>Link</td>
                            <td><input name="link" type="url" class="bTextbox" value="<s:property value="%{#request.adEntry.link}" escape="true"/>" disabled="disabled"/></td>
                        </tr>
                        <tr>
                            <td>Title</td>
                            <td><input name="title" type="text" class="bTextbox" value="<s:property value="%{#request.adEntry.title}" escape="true"/>" required="required"/></td>
                        </tr>
                        <tr>
                            <td>Keywords</td>
                            <td><input name="keyWords" type="text" class="bTextbox" value="<s:property value="%{#request.adEntry.keyWords}" escape="true"/>" /></td>
                        </tr>
                        <tr>
                            <td>Resource Type</td>
                            <td>
                            <s:select id="resourceType" name="resourceType" cssClass="bTextbox24" value="%{#request.adEntry.resourceType}" list="%{#request.resourceTypeSelector}"></s:select>
                           	 <span id="errorMsg" class="errorMsg"></span>
                            </td>
                        </tr>
                        <tr id="resourceUrlImgCon" style="display:none">
                            <td style="vertical-align:top;line-height:24px;">Resource Picture</td>
                            <td >
                           		<input id="resourceUrl" name="resourceUrl" type="hidden" class="bTextbox" value="<s:property value="%{#request.adEntry.resourceUrl}" escape="true"/>"/>
                            	<div id="imgCon">
                                	 <div class="spacer5"></div>
	                                 <img id="resourceUrlImg" alt="广告创意" src="<s:property value="%{#request.adEntry.resourceUrl}" escape="true"/>" width="100" height="100">
	                                 <input class="bButton" style="vertical-align:text-bottom;" type="button" id="imageFileUpload" value="上传" />
	                                 <input class="bButton" style="vertical-align:text-bottom;" type="button" id="revertImage" value="Revert" />
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>
                                <button class="bButton lightOrange" type="submit">Submit</button>
                                <a style="margin-left:10px;vertical-align:bottom;padding-top:3px;" class="bButton" href="${cxt_path}/admin/orders/entry/view?id=${request.adEntry.id}">Cancel</a>
                            </td>
                        </tr>
                    </table>
                </form>
            </s:if>
            <s:else>
                <div>No ad entry found!</div>
            </s:else>
            
            <div class="clear"></div>
            </div>
        </div>
        <div class="clear"></div>
    </div>
    <div class="clear spacer20"></div>
</div>
<div class="clear"></div>

<script type="text/javascript" charset="utf-8" src="${js_path}/libs/ajaxupload.js"></script>
<script type="text/javascript" charset="utf-8">
    $(function() {
    	$("#leftmenu-admin-ads").addClass("active");
        $("#topmenu-admin-ads").addClass("active");
        //if resource type is IMAGE ,display resource url
        <s:if test="%{#request['adEntry'].resourceType == @com.buzzinate.buzzads.enums.AdEntryTypeEnum@IMAGE}">
       		 imgShow();
     	</s:if>
     	// create image file uploader:
        var originalFile = $('<div/>').text("${request.adEntry.resourceUrl}").html() ;
        new AjaxUpload('#imageFileUpload', { 
            action: '${cxt_path}/admin/orders/entry/upload', autoSubmit: true, name: 'tempFile', responseType: "json",
            onChange: function(file, extension) {},
            onSubmit: function(file, extension) {
                hideStatusMessage();
                if (!(extension && /^(jpg|png|jpeg|gif)$/i.test(extension))){
                    displayStatusMessage('Invalid extension!', "error");
                    return false; // cancel upload
                }
                this.setData({
                    'fileName': file,
                    'fileNameExtension': extension
                });
            },
            onComplete: function(file, response) {
                if (response.success) {
                	$("#resourceUrl").val(response.contents.imageUrl);
                    $("#resourceUrlImg").attr("src", response.contents.imageUrl);
                    $("#errorMsg").hide();
                    displayStatusMessage("Image uploaded successfully! Please submit to save changes.", "success"); 
                } else {
                	displayStatusMessage(response.message, "error");   
                }  
            }
        });
        
        $("#revertImage").click(function() {
        	$("#resourceUrl").val(originalFile);
            $("#resourceUrlImg").attr("src",originalFile);
        });
        $("#resourceUrl").blur(function() {
            $("#resourceUrlImg").attr("src", $(this).val());
        });
        
        function imgShow(){
        	$("#resourceUrlImgCon").show();
        	$("#errorMsg").hide();
        }
        function imgHide(){
        	$("#errorMsg").hide();
        	$("#resourceUrl").attr("value","");
        	$("#resourceUrlImgCon").hide();	
        }

       	// when resource type change,perform this method
        $("#resourceType").change(function(){
			var v=$(this).val();
			switch(v){
			case"IMAGE":
					imgShow();
				 break;
			case "TEXT":
					imgHide();
				 break;
			case "UNKNOWN":
				$("#errorMsg").html("<span> * </span>请选择正确的资源类型").show();
				break;
			}
		});
     	// when form return true,determine to submit
        $("#adEntryEdit").submit(function(){
        	var reType=$("#resourceType").val();
        	if( reType=="UNKNOWN"){
        		$("#errorMsg").html("<span> * </span>请选择正确的资源类型").show();
        		return false;
        	}else if(reType=="IMAGE"){
        		var url=$("#resourceUrl").attr("value");
        		if(url!=""){
        			return true;
        		}
        		$("#errorMsg").html("<span> * </span>请上传图片").show().appendTo($("#imgCon"));
        		return false;
        	}else if(reType=="TEXT"){
        		$("#resourceUrl").attr("value","");
        	}
        	return true;
        })
    });
</script>
