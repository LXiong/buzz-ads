var bshare=bshare||{};

(function(BS){
	BS.Accordion=function(option){
		var self=this; 
		this.settings=$.extend({
		allClose:true,
		selecteIndex:[0,0]
		},option||{});
		this.container=option.container;
		this.data=option.items;
		this.items=[];
		self.selectedTitle=null;
		self.selectItem=null;
		if(this.settings.width)this.container.css({width:this.settings.width});
		this.dom=$('<ul class="accordion"></ul>').appendTo(this.container);
		for(var i=0,n=this.data.length;i<n;i++){
			self.createItem(this.data[i]);
		}
		//self.selectByIndex(self.settings.selecteIndex);
	};
	BS.Accordion.prototype={
		show:function(item){
			var self=this;
			if(item.items.length!=0){
				item.title.addClass("activeRight");
			}else{
				item.title.addClass("active");
			}
			item.selected=true;
			item.body.stop();
			item.body.show();
			self.selectedTitle=item;
		},
		hide:function(item){
			item.title.removeClass("active");
			item.title.removeClass("activeRight");
			item.selected=false;
			item.body.stop();
			item.body.hide();
		},
		createItem:function(d){
			var self=this;
			var item={};
			var d=item.data=d;
			item.items=[];
			item.id=d.id;
			item.dom=$('<li></li>');
			item.title=$('<div class="menu-left-button" style="display:block"><span class="x" style="display:none;"></span><a href='+d.href+' title='+d.title+'>'+d.title+'</a></div>').appendTo(item.dom);
			item.body=$('<ul class="menu-left-buttonC" ></ul>').appendTo(item.dom);
			item.clearDiv=$('<div class="clear spacer10"></div>').appendTo(item.dom);
			item.crateItem=function(d){
				var cItem={};
				cItem.id=d.id;
				cItem.parentId=d.parentId;
				cItem.dom=$('<li><a href='+d.href+' title='+d.title+'>'+d.title+'</a></li>').appendTo(item.body);
				cItem.dom.click(function(){
					item.selectItem(cItem);
					//self.settings.loadCon.load(d.href);
					self.selectItem=cItem;
				});
				
				item.items.push(cItem);
			};
			if(d.items){
				for(var i=0,n=d.items.length;i<n;i++){
					item.crateItem(d.items[i]);
				}
			}
			item.dom.appendTo(self.dom);
			if(self.settings.allClose){ 
				item.body.css("display","none");
			}
			
		    self.items.push(item);
			item.title.click(function(){
				//if(self.selectedTitle){
				//	self.hide(self.selectedTitle);
				//	if(self.selectItem)self.selectItem.dom.removeClass("active");
				//	item.selected=false;
				//}
				//if(!d.items){
				//	self.show(item);
				//	item.selected=true;
				//}else{
					if(item.selected){
						self.hide(item);
						item.selected=false;
					}else{
						self.show(item);
						item.selected=true;
					}
				//}
			});
			item.selectItem=function(item){
				if(self.selectItem)self.selectItem.dom.removeClass("active");
				item.dom.addClass("active");
			};
		},
		selectByIndex:function(index){
			var self=this;
			if(self.selectedTitle){
				self.hide(self.selectedTitle);
				if(self.selectItem)self.selectItem.dom.removeClass("active");
			}
			for(var i=0;i<self.items.length;i++){
				self.items[i].body.css("display","none");
			}
			if(!$.isArray(index)){
				self.show(self.items[index]);
			}else{
				self.show(self.items[index[0]]);
				if(self.items[index[0]].items[index[1]]){
					self.items[index[0]].selectItem(self.items[index[0]].items[index[1]]);
					self.selectItem=self.items[index[0]].items[index[1]];
				}
			}
			//self.items[index[0]].selected=true;
		},
		findItemById:function(d){
			var self=this;
			var items={};
			if(d.parentId){
				for(var i=0;i<self.items.length;i++){
					if(self.items[i].id===d.parentId){
						items.parentItem=self.items[i];
						if(d.id){
							var cItems=self.items[i].items;
							for(var j=0;j<cItems.length;j++){
								if(cItems[j].id===d.id){
									items.cItem=cItems[j];
									return items;
								}
							}
							return items;
						}
					}
				}
			}else if(d.id){
				for(var i=0;i<self.items.length;i++){
					if(self.items[i].id===d.id){
						items.parentItem=self.items[i];
						return items;
					}
				}
			}
			return items;
		},
		selectById:function(d){
			var self=this;
			if(self.selectedTitle){
				self.hide(self.selectedTitle);
				if(self.selectItem)self.selectItem.dom.removeClass("active");
			}
			for(var i=0;i<self.items.length;i++){
				self.items[i].body.css("display","none");
			}
			var items=self.findItemById(d);
			if(items.parentItem)self.show(items.parentItem);
			if(items.cItem){
				items.parentItem.selectItem(items.cItem);
			}
			
		},
		getUrlPara:function (paraName){
			var sUrl = location.href;
			var sReg = "(?:\\?|&){1}"+paraName+"=([^&]*)";
			var re=new RegExp(sReg,"gi");
			re.exec(sUrl);
			return RegExp.$1;
		},
		getDate:function(){
			var dateTemp=new Date();
			return dateTemp.getFullYear() 
					+ "-" + ('0'+(dateTemp.getMonth()+1)).slice(-2)
					+ "-" + ('0'+dateTemp.getDate()).slice(-2)
					+ " " + ('0'+dateTemp.getHours()).slice(-2)
					+ ":" + ('0'+dateTemp.getMinutes()).slice(-2)
					+ ":" + ('0'+dateTemp.getSeconds()).slice(-2);
		},
		stripscript:function (s) 
		{ 
			return s.replace(/&/g, '&amp').replace(/\"/g, '&quot;').replace(/\'/g, '&#39;').replace(/</g, '&lt;').replace(/>/g, '&gt;'); 
		},
		isURL:function(str_url){
	        var strRegex     = "((https?|ftp|gopher|telnet|file|notes|ms-help):((//)|(\\\\))+[\w\d:#@%/;$()~_?\+-=\\\.&]*)";
	         var re=new RegExp(strRegex); 
	         if (re.test(str_url)){
	             return (true); 
	         }else{ 
	             return (false); 
	         }
		}
	}
	
})(bshare)
