app.service("contentService",function($http){
	this.findByCategoryId = function(categoryId){
		return $http.get("content/findByCategoryId.do?categoryId="+categoryId);
	}

    this.findByCategoryId1 = function(categoryId){
        return $http.get("content/findByCategoryId1.do?categoryId="+categoryId);
    }

    this.findByCategoryId2 = function(categoryId){
        return $http.get("content/findByCategoryId2.do?categoryId="+categoryId);
    }

    //查询商品分类信息
    this.findItemCatList = function () {
        return $http.get("content/showItemCat.do");
    }


});