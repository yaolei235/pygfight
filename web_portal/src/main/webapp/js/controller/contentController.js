app.controller("contentController",function($scope,contentService){
	$scope.contentList = [];
	// 根据分类ID查询广告的方法:
	$scope.findByCategoryId = function(categoryId){
		contentService.findByCategoryId(categoryId).success(function(response){
			$scope.contentList[categoryId] = response;
		});
	}

    $scope.contentList1 = [];
    // 根据分类ID查询广告的方法:
    $scope.findByCategoryId1 = function(categoryId){
        contentService.findByCategoryId1(categoryId).success(function(response){
            $scope.contentList1[categoryId] = response;
        });
    }

    $scope.contentList2 = [];
    // 根据分类ID查询广告的方法:
    $scope.findByCategoryId2 = function(categoryId){
        contentService.findByCategoryId2(categoryId).success(function(response){
            $scope.contentList2[categoryId] = response;
        });
    }
	
	//搜索,跳转到portal系统查询列表页面(传递参数）
	$scope.search=function(){
		location.href="http://localhost:8080/search.html#?keywords="+$scope.keywords;
	}

    //查询商品分类信息
    $scope.findItemCatList=function () {
        contentService.findItemCatList().success(function (response) {
            $scope.itemCatList=response;
        });
    }


	
});