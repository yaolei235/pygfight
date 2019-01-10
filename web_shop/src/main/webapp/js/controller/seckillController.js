app.controller('seckillController' ,function($scope,$controller,$location,seckillService){

    $controller('baseController',{$scope:$scope});   //继承



    // 保存品牌的方法:
    $scope.saveSeckill = function(){
        alert($scope.entity.id)
        var object;
        // 保存
        object = seckillService.add($scope.entity);
        object.success(function(response){
            // {success:true,message:xxx}
            // 判断保存是否成功:
            if(response.success==true){
                // 保存成功
                alert(response.message);
                $scope.reloadList();
            }else{
                // 保存失败
                alert(response.message);
            }
        });
    }

});

