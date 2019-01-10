// 定义服务层:
app.service("totalOrderService",function($http){
	this.findAll = function(){
		return $http.get("../totalOrder/findAll.do");
	}
	
	this.findByPage = function(page,rows){
		return $http.get("../totalOrder/findByPage.do?page="+page+"&rows="+rows);
	}
	
	this.save = function(entity){
		return $http.post("../totalOrder/save.do",entity);
	}
	
	this.update=function(entity){
		return $http.post("../totalOrder/update.do",entity);
	}
	
	this.findOne=function(id){
		return $http.get("../totalOrder/findOne.do?orderId="+id);
	}
	this.dele = function(ids){
		return $http.get("../totalOrder/delete.do?ids="+ids);
	}
	
	this.search = function(page,rows,searchEntity){
		return $http.post("../totalOrder/search.do?page="+page+"&rows="+rows,searchEntity);
	}
	
	this.selectOptionList = function(){
		return $http.get("../totalOrder/selectOptionList.do");
	}
});