package cn.itcast.core.util;



import org.apache.poi.hssf.usermodel.*;
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
}