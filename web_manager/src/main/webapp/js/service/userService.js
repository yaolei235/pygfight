// 定义服务层:
app.service("userService",function($http){

    this.findByPage = function(page,rows){
        return $http.get("../user/findByPage.do?page="+page+"&rows="+rows);
    }

    this.search = function(page,rows,searchEntity){
        return $http.post("../user/search.do?page="+page+"&rows="+rows,searchEntity);
    }
    //导出excel文件请求
    this.getExcel = function(){
        window.location.href="../report/exportExcel.do";
    }
});