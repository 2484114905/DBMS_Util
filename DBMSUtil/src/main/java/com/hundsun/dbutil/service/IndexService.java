package com.hundsun.dbutil.service;

import com.hundsun.dbutil.dao.IndexDAO;
import com.hundsun.dbutil.domain.Index;
import com.hundsun.dbutil.domain.MysqlIndex;
import com.hundsun.dbutil.domain.OracleIndex;
import com.hundsun.dbutil.util.ConfigUtil;
import com.hundsun.dbutil.util.DBUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用于获取create index 语句的集合
 * @author wangyang31647
 * @date 2020/08/10
 */
@Service
public class IndexService {
    public static String sqlPart1 = "CREATE INDEX ";
    public static final String sqlPart2 = "CREATE UNIQUE INDEX ";
    @Autowired
    private IndexDAO indexDAO;

    /**
     * 从oracle 或者mysql 数据库或去索引信息，生成标准的create index 语句，没有索引则返回空值
     * @param tableName 表名
     * @return create index 语句的集合
     */
    public List<String> getIndexStatement(String tableName){
        if (ConfigUtil.MYSQL.equalsIgnoreCase(ConfigUtil.sourceKind)){
            return getMysqlIndexString(indexDAO.getIndexFromMysql(tableName.toUpperCase()), tableName);
        }else {
            return getOracleIndexString(indexDAO.getIndexFromOracle(tableName.toUpperCase()), tableName);
        }

    }

    public static List<String> getMysqlIndexString(List<MysqlIndex> indices, String tableName){
        if (indices.isEmpty()){
            return null;
        }

        StringBuffer result;
        ArrayList<String> list = new ArrayList<>();
        for (MysqlIndex index : indices){
            if (index.getNonUnique() == 0){
                result = new StringBuffer(sqlPart2);
            }else {
                result = new StringBuffer(sqlPart1);
            }

            result.append(index.getIndexName()).append(" ON ").append(tableName);
            result.append("(").append(index.getColumnName()).append(")");
            list.add(result.substring(0));
        }

        return list;
    }

    public  List<String> getOracleIndexString(List<OracleIndex> indices, String tableName){
        if (indices.isEmpty()){
            return null;
        }

        StringBuffer result;
        ArrayList<String> list = new ArrayList<>();
        for (OracleIndex index : indices){
            if (index.getUniqueness().equalsIgnoreCase(ConfigUtil.UNIQUENESS)){
                result = new StringBuffer(sqlPart2);
            }else {
                result = new StringBuffer(sqlPart1);
            }

            result.append(index.getIndexName()).append(" ON ").append(tableName.toUpperCase());
            result.append("(").append(index.getColumnName().toUpperCase()).append(")");
            list.add(result.substring(0));
        }

        return list;
    }
}
