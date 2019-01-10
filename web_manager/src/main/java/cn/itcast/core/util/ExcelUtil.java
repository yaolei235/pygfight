package cn.itcast.core.util;



import cn.itcast.core.pojo.good.Brand;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ExcelUtil {

    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param lists 内容
     * @param wb HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, Object [] title, List<ArrayList<String>> lists, HSSFWorkbook wb){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        //声明列对象
        HSSFCell cell = null;

        //创建标题  用户id	用户名	用户手机号	收货人地区名称	收货人	用户订单号	 商品	数量 订单总价	  	 支付状态
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]+"");
            cell.setCellStyle(style);
        }
        /**
         * list集合
         */
        for (int i = 0; i < lists.size(); i++) {
            row = sheet.createRow(i+1);
            ArrayList<String> list = lists.get(i);
            for (int j = 0; j < list.size(); j++) {
                String s = list.get(j);
                row.createCell(j).setCellValue(s);
            }
        }

        return wb;
    }

    //导人excel表
    public static  List<Brand> readExcel(String fileName, List<String> title) throws Exception {
        URL url = new URL(fileName);
        InputStream is = url.openConnection().getInputStream();
        Workbook hssfWorkbook = null;
        if (fileName.endsWith("xlsx")) {
            hssfWorkbook = new XSSFWorkbook(is);//Excel 2007
        } else if (fileName.endsWith("xls")) {
            hssfWorkbook = new HSSFWorkbook(is);//Excel 2003

        }
        List<Brand> list = new ArrayList<>();
        Brand brand = new Brand();
        // 循环工作表Sheet   从1开始,因为一般第一行都是列名
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {

            //HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                //HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                Row hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {

                    Cell id = hssfRow.getCell(0);
                    Cell name = hssfRow.getCell(1);
                    Cell first_char = hssfRow.getCell(2);
                    Cell status = hssfRow.getCell(3);

                    brand.setId(Long.parseLong(id.toString().split("\\.")[0]));
                    brand.setName(name.toString());
                    brand.setFirstChar(first_char.toString().substring(0,1));
                    brand.setStatus(status.toString().split("\\.")[0]);
                    list.add(brand);
                }
            }

            }
        return list;
    }
}