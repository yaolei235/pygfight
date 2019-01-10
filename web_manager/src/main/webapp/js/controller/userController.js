// 定义控制器:
app.controller("userController",function($scope,$controller,$http,userService){
    // AngularJS中的继承:伪继承
    $controller('baseController',{$scope:$scope});

    // 查询所有的品牌列表的方法:
    $scope.findAll = function(){
        // 向后台发送请求:
        userService.findAll().success(function(response){
            $scope.list = response;
        });
    }

    // 分页查询
    $scope.findByPage = function(page,rows){
        // 向后台发送请求获取数据:
        userService.findByPage(page,rows).success(function(response){
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        });
    }

    // 导出excel表:
    $scope.getExcel = function(){
        userService.getExcel().success(function(response){

            if(response.success==false){
                alert(response.message);
            }
        });
    }




    $scope.findUsers = function(){
        userService.findUsers().success(function(response){
                $scope.list = response;

        });
    }

});
