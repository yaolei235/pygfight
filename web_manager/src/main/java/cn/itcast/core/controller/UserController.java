package cn.itcast.core.controller;


import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.UserService;
import cn.itcast.core.util.ExcelUtil;
import com.alibaba.dubbo.config.annotation.Reference;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;
    @Autowired
    private HttpServletResponse response;

    /**
     * 导出报表
     * @return
     */
    @RequestMapping("/exportExcel")
    public Result export() {
        //获取数据

        //excel标题
        List<String> list =userService.findTitle();
        Object [] title =list.toArray();
        //excel文件名
        String fileName = "用户表"+ System.currentTimeMillis() + ".xls";

        //sheet名  表名
        String sheetName = "用户表";
        //users数据
        List<User> allUser = userService.findAllUser();
        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, allUser, null);
        //获取项目路径

        OutputStream outputStream=null;
        try {
            setResponseHeader(response,fileName);
            //输出文件
            outputStream = response.getOutputStream();

            wb.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
                try {
                    if (outputStream!=null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
            }
        }
        return new Result(true,"成功");
    }
    //发送响应流方法
    private void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
