package com.hundsun.dbutil.dao;

import com.hundsun.dbutil.domain.MysqlIndex;
import com.hundsun.dbutil.domain.OracleIndex;
import java.util.List;

/**
 * 从数据库中获取一张表中除主键索引外的所有索引信息
 * @author wangyang31647
 * @date 2020/08/10
 */
public interface IndexDAO {

    /**
     * 从mysql中获取索引信息
     * @param tableName 表名
     * @return  索引信息的集合
     */
    List<MysqlIndex> getIndexFromMysql(String tableName);

    /**
     * 从oracle中获取索引信息
     * @param tableName 表名
     * @return  索引信息的集合
     */
    List<OracleIndex> getIndexFromOracle(String tableName);

}
