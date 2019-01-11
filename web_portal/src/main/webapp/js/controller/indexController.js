app.controller('indexController',function ($scope,indexService) {

    //查询商品分类信息
    $scope.findItemCatList=function () {
        indexService.findItemCatList().success(function (response) {
            $scope.itemCatList=response;
        })
    }
});