<%@ page contentType="text/html; charset=UTF-8" language="java"   %>
<%@taglib prefix="s" uri="/struts-tags" %>
<div id="fy" >
		<div style="float:right;">
		<s:if test="!query  || (query.returnRecords==0 && query.pageNum==0)">
			<img src="${image_path }/fy_first2.gif" width="16" height="16"  alt="首页" /><font color="#999999">首页</font>
			<img src="${image_path }/fy_back2.gif" width="16" height="16"  alt="上一页" /><font color="#999999">上一页</font>
		    <img src="${image_path }/fy_forward2.gif" width="16" height="16"  alt="下一页" /><font color="#999999">下一页</font>&nbsp;&nbsp;
			每页显示<select id="sel_pn_rownum" name="query.pageSize" >
			<option value="10" selected="selected">10 </option>
			</select>
			行
		</s:if> 
		<s:else>
			<s:set name="next" value="query.pageNum + 1" />
            <s:set name="prev" value="query.pageNum - 1" />
          	第&nbsp;${query.pageNum}&nbsp;页&nbsp;&nbsp;
			<s:if test="query.pageNum <= 1">
				<img src="${image_path }/fy_first2.gif" width="16" height="16"  alt="首页" /><font color="#999999">首页</font>
			    <img src="${image_path }/fy_back2.gif" width="16" height="16"  alt="上一页" /><font color="#999999">上一页</font>
        	</s:if>
        	<s:else>
        		<a  href="#"  onclick="changePage('1');return false;">
				    <img src="${image_path }/fy_first.gif" width="16" height="16"  alt="首页" />首页
				</a>
        	    <a  href="#"  onclick="changePage('<s:property value="#prev" />');return false;">
				    <img src="${image_path }/fy_back.gif" width="16" height="16"  alt="上一页" />上一页
				</a>
            </s:else>
            
            <s:if test="query.totalRecords == 0 || query.returnRecords < query.pageSize">
        		 <img src="${image_path }/fy_forward2.gif" width="16" height="16"  alt="下一页" /><font color="#999999">下一页</font>
        	</s:if>
        	<s:else>
        		<a  href="#" onclick="changePage('<s:property value="#next" />');return false;">
				     <img src="${image_path }/fy_forward.gif" width="16" height="16"  alt="下一页" />下一页
				</a>
       	    </s:else>

			每页显示<select id="sel_pn_rownum" name="query.pageSize" >
				<s:if test="query.pageSize == 10">
					<option value="10" selected="selected">10 </option>	
				</s:if>
				<s:else>
					<option value="10">10 </option>
				</s:else>
				<s:if test="query.pageSize == 20">
					<option value="20" selected="selected">20 </option>	
				</s:if>
				<s:else>
					<option value="20">20 </option>
				</s:else>
				<s:if test="query.pageSize == 50">
					<option value="50" selected="selected">50 </option>	
				</s:if>
				<s:else>
					<option value="50">50 </option>
				</s:else>

				<s:if test="query.pageSize == 100">
					<option value="100" selected="selected">100</option>	
				</s:if>
				<s:else>
					<option value="100">100</option>
				</s:else>
				</select>行
       	    <input id="pageNum" name="query.pageNum" type="hidden" value="<s:property value="query.pageNum" />" />			
       	</s:else>
		</div>
</div>

<script type="text/javascript">
	$(function() {
	     $('#sel_pn_rownum').change(onChangePageBysel);
	});
	
   function changePage(page) {
        $("#pageNum").val(page);
        doSearch();
    }

	function onChangePageBysel() {
		changePage(1);
	}
</script>