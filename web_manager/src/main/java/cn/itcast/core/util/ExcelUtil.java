package cn.itcast.core.util;


import cn.itcast.core.pojo.user.User;
import org.apache.poi.hssf.usermodel.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param users 内容
     * @param wb HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, Object [] title, List<User> users, HSSFWorkbook wb){

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

        //创建标题
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]+"");
            cell.setCellStyle(style);
        }
        Map<String,Object> map=new HashMap<String,Object>();
        for (int i = 0; i < users.size(); i++) {
            row = sheet.createRow(i + 1);
            User user= users.get(i);
            map= UserToMapUtil.convert2Map(user);
            //System.out.println(map);
            for (int j = 0; j < title.length; j++) {
                String value=title[j]+"";
                String s = value.replaceAll("_", "");
                row.createCell(j).setCellValue(map.get(s)+"");

            }
        }

        return wb;
    }
}