
function getSubList(pid,id,obj){
	obj.html("");
	obj.val("");
	obj.append("<option>--请选择--</option>");
	if(!pid){
		return;
	}
	var url="/management/jsondata/area/getSubAreaList";
	var params={parentId:pid};
	$.ajax({
		type: 'get',
		url:url,
		async: false,
		dataType: 'jsonp',
		data:params,
		success:function(data){
			if(data.statusCode=="0000"){
				var sublist=data.data;
				for(var i=0;i<sublist.length;i++){
					if(id&&sublist[i].area_id==id){
						obj.append("<option value='"+sublist[i].area_id+"' selected>"+sublist[i].area_name+"</option>");
					}else{
						obj.append("<option value='"+sublist[i].area_id+"'>"+sublist[i].area_name+"</option>");
					}
				}
			}
			
		},
		error:function(data){
			alert("网络错误");
		}
	});
}