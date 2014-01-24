<%@ include file="/jsp/common/Init.jsp"%>
<%@ taglib prefix="core" uri="/buzzads-core"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<style>
.targetWrap { font-size: 12px; }
.targetWrap p { line-height: 25px; margin-bottom: 5px; }
.targetWrap .cateList { width: 50%; border: 1px solid #ccc; padding: 5px; height: 251px; overflow: auto; background: #fff; }
.targetWrap .add, .targetWrap .delete { padding: 0 5px; line-height: 160%; color: #666; 
    cursor: pointer; background: #eee; margin: 2px; text-indent: 0; width: 16px; text-align: center;
    border: 1px solid #ccc; }
.targetWrap .delete { margin-right: 10px; }
.targetWrap .cateList p { border: 1px solid #ddd; background: #f9f9f9; }
.targetWrap .cateList p.selected { color: #999; }
.targetWrap .cateList p.selected .add { color: #ccc; cursor: default; border-color: #eaeaea; }
.targetWrap .subList { display: none; margin-left: 40px; }
.targetWrap .subList p { text-indent: 5px; }
.targetWrap .fix { padding-left: 20px; }
.targetWrap .fix .title { font-size: 14px; font-weight: bold; color: #666; border-bottom: 1px solid #ccc; }
.targetWrap .fix p { margin-bottom: 0; }
#destUsrList { height: 224px; overflow: auto; }
</style>

<div class="targetWrap" id="targetWrap">
    <div class="cateList left">
        <s:iterator value="#request['catelist']" var="cate" status="status" >
        <div class="cate aaaa">
            <p style="position:relative;"><a class="packer bLinkDark" href="javascript:;"><i></i></a><font class="name clip" title="${cate.name}">${cate.name}</font><a 
                id="${cate.id}" class="add right0">»</a></p>
            <div class="subList">
                <s:iterator value="#cate['subCategories']" var="subcate" status="status" >
                <p style="position:relative;"><font class="name clip" style="width:225px;" title="${subcate.name}">${subcate.name}</font><a id="${subcate.id}" class="add right0">»</a></p>
                <div class="clear"></div>
                </s:iterator>
            </div>
        </div>
        </s:iterator>
    </div>
    <div class="fix">
        <p class="title">选定的受众群体:</p>
        <div class="spacer10"></div>
        <div id="destUsrList"></div>
    </div>
    <div class="clear"></div>
    <input id="audienceCategories" type="hidden" name="audienceCategories" value="${adOrder.audienceCategories}" />
</div>
<script type="text/javascript">
function selectCate(target) {
	var id = target.id, selected = $("#audienceCategories").val(), 
	    destUsrList = $("#destUsrList"), elem; 

	$(target).closest("p").addClass("selected");
	
	elem = $('<p style="position:relative;"><a ref="' + id + '" class="delete left0">«</a> <span class="name clip" title="'+$(target).closest("p").find("font").text()+'" style="margin-left:38px;">' + $(target).closest("p").find("font").text() + 
			'</span></p><div class="clear"></div>');
	
	elem.find(".delete").click(function () {
	    var ref = $(this).attr("ref"), s = $("#audienceCategories").val();
	    $("#" + ref).closest("p").removeClass("selected");
	    s = s.replace(ref + ",", "").replace("," + ref, "").replace(ref, "");
	    $("#audienceCategories").val(s);
	    $(this).closest("p").remove();
	});
	elem.appendTo(destUsrList);

    if (("," + selected + ",").indexOf("," + id + ",") > -1) {
        return;
    }
	if (!!selected) {
	    $("#audienceCategories").val(selected + "," + id);
	} else {
	    $("#audienceCategories").val(id);
	}
}
function initTargetList() {
    $("#targetWrap .packer").toggle(function () {
        var subList = $(this).closest(".cate").find(".subList");
        subList.show();
    },function(){
    	 var subList = $(this).closest(".cate").find(".subList");
    	 subList.hide();
    });
    $("#targetWrap .add").click(function () {
    	var id = this.id, selected = $("#audienceCategories").val();
        if (("," + selected + ",").indexOf("," + id + ",") > -1) {
            return;
        }
    	selectCate(this);
    });
}

function initTargetUsrCate() {
	var targets = $("#audienceCategories").val().split(",");
	for (var i = 0, length = targets.length; i < length; ++i) {
		selectCate(document.getElementById(targets[i]));
	}
}

$(function () {
	initTargetList();
	if (!!$("#audienceCategories").val()) {
		initTargetUsrCate();
	}
});
</script>
