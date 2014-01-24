window.RC={};
RC["numVerify"]=function(str){
	var userreg=/^[0-9]+([.]{1}[0-9]{1,2})?$/;
	return userreg.test(str);
}
RC["numInteger"]=function(str){
	var userreg=/^[0-9]+$/;
	return userreg.test(str);
}
RC["numFloat"]=function(str){
	str=RC.numInteger(str)?str+".00":str;
	return str;
}
RC["getDate"]=function(){
	var d=new Date();
	var date=d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
	return date;
}
RC["hourToMin"]=function(time){
	var temp=time.split(":");
	var hour=temp[0];
	return hour*60+(temp[1]);
}
RC["CutSelect"]=function(id){
	$("#"+id).find("option").each(function(){
	     var s=$(this).text();
	     $(this).attr("title",s);
	     if(s.length>20)
	     {
	      s=s.substring(0,20)+"...";
	     }
	     $(this).html(s);
	    });
}
RC["getDate"]=function(){
	var dateTemp=new Date();
	return dateTemp.getFullYear() 
			+ "-" + ('0'+(dateTemp.getMonth()+1)).slice(-2)
			+ "-" + ('0'+dateTemp.getDate()).slice(-2)
			+ " " + ('0'+dateTemp.getHours()).slice(-2)
			+ ":" + ('0'+dateTemp.getMinutes()).slice(-2)
			+ ":" + ('0'+dateTemp.getSeconds()).slice(-2);
}
RC["stripscript"]=function (s) 
{ 
	return s.replace(/&/g, '&amp').replace(/\"/g, '&quot;').replace(/\'/g, '&#39;').replace(/</g, '&lt;').replace(/>/g, '&gt;'); 
}
RC["isURL"]=function(str_url){
    var strRegex     = "((https?|ftp|gopher|telnet|file|notes|ms-help):((//)|(\\\\))+[\w\d:#@%/;$()~_?\+-=\\\.&]*)";
     var re=new RegExp(strRegex); 
     if (re.test(str_url)){
         return (true); 
     }else{ 
         return (false); 
     }
}
RC["timeTo24"]=function (time) 
{ 
	 var t=time.split(":");
	 t[0]=parseInt(t[0])+12;
	 return t.join(":");
}
RC["timeTo12"]=function (time) 
{ 
	 var t=time.split(":");
	 t[0]=parseInt(t[0])-12;
	 return t.join(":");
}
RC["dateBefore"]=function(start,end){
	var s=start.split("-");
	var e=end.split("-");
	var sd=new Date(s[0],s[1],s[2]);
	var ed=new Date(e[0],e[1],e[2]);
	return sd.getTime()>=ed.getTime();
}
RC["dateEqual"]=function(start,end){
	var s=start.split("-");
	var e=end.split("-");
	var sd=new Date(s[0],s[1],s[2]);
	var ed=new Date(e[0],e[1],e[2]);
	return sd.getTime()==ed.getTime();
}
RC["maxDate"]=function(date){
	var temp=date[0];
	for(var i=1;i<date.length;i++){
		if(RC.dateBefore(date[i],temp)){
			temp=date[i];
		}
	}
	return temp;
}
RC["dateToSprit"]=function(time){
	var temp=time.split("-");
	return temp[0]+"/"+temp[1]+"/"+temp[2];
}
RC["trim"]=function(str){
	var str=str.replace(/(^\s+)|(\s+$)/g,"");
	return str.replace(/\s/g,"");
}
RC["topTip"]=function(){
	// init help popups
	$("span.help-popup-img[title]").tooltip({
	    tipClass: 'helpPopupBoxes',
	    position: 'top right',
	    delay: 100,
	    layout: '<div><span></span></div>'
	});
}
