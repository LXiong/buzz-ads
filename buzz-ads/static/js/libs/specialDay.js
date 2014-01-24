var spricalItem=[];
var isRepeat=false;
		function setSpricalDate(option){
			var item={};
			//create show contant div
			item.showCon=$('<div class="inlineBlock spacer_right5"><span>特定日期：</span><span class="spec"></span>，<span>预算：</span><span class="budget rightBorder"></span><div class="spacer10"></div></div>');
			item.spec=item.showCon.find(".spec");
			item.budget=item.showCon.find(".budget");
			//create date and budget div
			item.domH=$('<div class="specialCon" ><div class="spacer10"></div><input class="runcode specialDay" /><span  style="padding:0px 3px 0px 15px;" >预算：¥</span><input class="validator bTextbox dayBudget " required="required" step="1" min="0" type="Number" id="max_validator" value="100" style="width:70px;" /><a class="packer orange deleteDay" href="javascript:void(0)"> - 删除</a><span class="itemd_msg spacer_left15 primary"></span><span class="itemb_msg spacer_left15 primary"></span></div>');
			item.dom=item.domH;
			item.dayBudget=item.domH.find(".dayBudget");
			item.day=item.domH.find(".specialDay");
			item.ebmsg=item.domH.find(".itemb_msg");
			item.edmsg=item.domH.find(".itemd_msg");
			item.getBudget=function(){
				var budgetNum=RC.numFloat(item.dayBudget.val()); 
				if(RC.numVerify(budgetNum)){
					item.ebmsg.html("");
					return budgetNum;
				}else{
					item.ebmsg.html("请输入合法数字,非负整数或小数[小数最多精确到小数点后两位]！");
					return false;
				}
			}
			item.isRepeat=function(){
				for(var i=1;i<spricalItem.length;i++){
					if(spricalItem[i-1]==item)continue;
					if(RC.dateEqual(spricalItem[i-1].getDate(),item.getDate())){
						item.edmsg.html("特定日期中包含重复日期，请选择合法日期！");
						isRepeat= true;
						return isRepeat;
					}
				}
				isRepeat= false;
				item.edmsg.html("");
				return isRepeat;
			}
			//日期不能超过最大值
			item.getDate=function(){
				if(item.spec.html()==""){
					item.edmsg.html("日期不能为空，请选择合法日期！");
					return false;
				}else{
					if(!isRepeat ){
						item.edmsg.html("");
					}
					return item.spec.html();
				}
			}
			item.dayBudget.blur(function(){
				item.getBudget();
				
			})
			//设置日期
			if(option){
				if(option.date){
					item.showCon.appendTo($("#showSpecialDay"));
					$(".rightBorder").removeClass("last");
					$(".rightBorder:last").addClass("last");
					item.spec.html(option.date);
					item.dayBudget.val(option.budget);	
					item.day.val(option.date);
				}
				//设置预算
				if(option.budget){
					item.budget.html("¥ "+option.budget);
					item.dayBudget.val(option.budget);	
				}
			}
			
			item.budget.html("¥ "+item.getBudget());
			item.day.calendar({minDate:$("#datePickerStart").val(),maxDate:$("#datePickerEnd").val(),btnBar:false,onSetDate:function(){
				item.showCon.appendTo($("#showSpecialDay"));
				$(".rightBorder").removeClass("last");
				$(".rightBorder:last").addClass("last");
				item.spec.html(this.getDate('date'));
				//item.getDate();
				item.isRepeat();
			} });
			item.dayBudget.change(function(){
				var price=RC.numFloat(item.dayBudget.val());
				item.budget.html("¥ "+price);	
			});
			item.del=item.domH.find(".deleteDay");
			item.domH.appendTo($("#specialDay"));
			item.remove=function(o){
				o.dom.remove();
				o.showCon.remove();
				$(".rightBorder:last").addClass("last");
				item.removeDate(o);
			}
			item.removeDate=function(o){
				spricalItem=$.grep(spricalItem,function(n,i){
					return n==o;
				},true);
			}
			item.del.click(function(){
				item.remove(item);
			});
			item.setDate=function(minDate,maxDate){
				item.day.calendar({minDate:minDate,maxDate:maxDate,btnBar:false,onSetDate:function(){
					item.showCon.appendTo($("#showSpecialDay"));
					$(".rightBorder").removeClass("last");
					$(".rightBorder:last").addClass("last");
					item.spec.html(this.getDate('date'));
				} });
			}
			spricalItem.push(item);
			return item;
		}
		function getDataString(){
			var temp=[];
			if(isRepeat){
				return false;
			}else{
				for(var i=0;i<spricalItem.length;i++ ){
					var tempd=spricalItem[i].getDate();
					var tempb=parseFloat(spricalItem[i].getBudget()).mul(100);
					if(tempb && tempd){
						temp.push(tempd+","+tempb);
					}else{
						return false;
					}
				}
				return temp;
				
			}
			
		}
		
		
		function getMaxSpricalDay(){
			var tatalDay=[];
			for(var i=0;i<spricalItem.length;i++){
				//获取时间
				tatalDay.push(spricalItem[i].getDate());
			}
			return RC.maxDate(tatalDay);	
		}
		
		