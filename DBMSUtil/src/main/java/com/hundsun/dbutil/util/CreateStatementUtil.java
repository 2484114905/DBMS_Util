package com.hundsun.dbutil.util;

import com.hundsun.fund.ecmp.common.log.LoggersUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.ibatis.session.SqlSession;

/**
 * 该类用于获取oracle 或mysql 数据库中的建表语句
 * @author wangyang31647
 * @date 2020/08/10
 */
public class CreateStatementUtil {

    /**
     * 获取数据库中的建表语句，当需要获取整张表的建表语句且不需要转换时使用此类
     * @param tableName 需要获取建表语句的表名
     * @return String ，即建表语句
     */
    public static String getCreateString(String tableName) {
        SqlSession session = DBUtil.getSession();
        /**
         * 直接使用mybatis提供的JDBC接口，注意避免使用PreparedStatement对象，PreparedStatement对象会为传入SQL
         * 语句的参数值加上特殊符号，可能造成SQL语法错误，所以使用字符串拼接的方式
         */
        Connection connection = session.getConnection();
        String mysql = "show create table " + ConfigUtil.schemaName + ".";
        String oracle1 = "select dbms_metadata.get_ddl('TABLE','";
        String oracle2 = "') from dual";
        String result = null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet;
            // 从mysql获取DDL
            if (ConfigUtil.MYSQL.equalsIgnoreCase(ConfigUtil.sourceKind)) {
                resultSet = statement.executeQuery(mysql + tableName);
                while (resultSet.next()) {
                    result = resultSet.getString(2);
                }
            } else {// 从Oracle获取DDL,注意oracle中表名要转换成大写
                resultSet = statement.executeQuery(oracle1 + tableName.toUpperCase() + oracle2);
                while (resultSet.next()) {
                    result = resultSet.getString(1);
                    result = modifyOracleCreateString(result);
                }
            }

        } catch (SQLException e) {
            LoggersUtil.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 对getCreateString()进行封装，用于获取mysql建表语句
     */
//    public static String getMysqlCreateString(String tableName) {
//        return getCreateString(tableName, true);
//    }

    /**
     * 对getCreateString()进行封装，用于获取mysql建表语句中某一字段的相关信息
     */
//    public static String getMysqlCreateStringOfColumn(String tableName, String column) {
//        String result = getMysqlCreateString(tableName);
//        return getColumnString(result, column, true);
//    }

    /**
     * 对getCreateString()进行封装，用于获取oracle建表语句
     */
//    public static String getOracleCreateString(String tableName) {
//        return getCreateString(tableName, false);
//    }

    /**
     * 对getCreateString()进行封装，用于获取oracle建表语句中某一字段的相关信息
     */
//    public static String getOracleCreateStringOfColumn(String tableName, String column) {
//        String result = getOracleCreateString(tableName);
//        return getColumnString(result, column, false);
//    }



//    public static String getColumnString(String createTableString, String column, boolean kind) {
//        String columns = createTableString.substring(createTableString.indexOf("(") + 1,
//            createTableString.lastIndexOf(")"));
//        String[] trimmedColumns = columns.split(",");
//        for (String s : trimmedColumns) {
//            s = s.trim();
//            // 适用于mysql
//            if (kind) {
//                if (s.toLowerCase().startsWith("`" + column.toLowerCase() + "`")) {
//                    return s;
//                }
//            } else {// 适用于oracle
//                if (s.toLowerCase().startsWith("\"" + column.toLowerCase() + "\"")) {
//                    return s;
//                }
//            }
//
//        }
//        return null;
//    }

    public static String modifyOracleCreateString(String createString){
        String result = createString;
        if (result.contains(ConfigUtil.SEGMENT) && !result.contains("\"" + ConfigUtil.SEGMENT + "\"")){
            result = result.substring(0, result.lastIndexOf(ConfigUtil.SEGMENT));
        }

        if (result.contains(ConfigUtil.TABLE_SPACE) && !result.contains("\"" + ConfigUtil.TABLE_SPACE + "\"")){
            result = result.substring(0, result.lastIndexOf(ConfigUtil.TABLE_SPACE)) + ")";
        }

        String s = "\"" + ConfigUtil.userName + "\".";
        result = result.replace(s, "");
        result = result.replaceAll("\"", "");
        return result;
    }
}
