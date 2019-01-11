//服务层
app.service('indexService',function($http){

    //查询商品分类信息
    this.findItemCatList = function () {
        return $http.get("index/showItemCat.do");
    }


});