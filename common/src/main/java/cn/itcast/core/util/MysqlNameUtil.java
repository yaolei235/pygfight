package cn.itcast.core.util;

/**
 * 这个类保存了部分表名和要用给数据库的名称
 用来查询表的列名
 */
public interface MysqlNameUtil {

     String MYSQL_NAME="pyg330";
    //品牌表
    String TABLE_BRAND="tb_brand";
    //规格表
    String TABLE_SPEC="tb_specification";
    //模板表
    String TABLE_TYPE_TEMP="tb_type_template";
    //分类表
    String TABLE_ITEM_CAT="tb_item_cat";
}
