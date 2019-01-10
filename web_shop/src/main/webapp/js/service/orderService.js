// 定义服务层:
app.service("orderService",function($http){
	this.findAll = function(){
		return $http.get("../order/findAll.do");
	}
	
	this.findByPage = function(page,rows){
		return $http.get("../order/findByPage.do?page="+page+"&rows="+rows);
	}
	
	this.save = function(entity){
		return $http.post("../order/save.do",entity);
	}
	
	this.update=function(entity){
		return $http.post("../order/update.do",entity);
	}
	
	this.findOne=function(id){
		return $http.get("../order/findOne.do?orderId="+id);
	}
	this.dele = function(ids){
		return $http.get("../order/delete.do?ids="+ids);
	}
	
	this.search = function(page,rows,searchEntity){
		return $http.post("../order/search.do?page="+page+"&rows="+rows,searchEntity);
	}
	
	this.selectOptionList = function(){
		return $http.get("../order/selectOptionList.do");
	}
});