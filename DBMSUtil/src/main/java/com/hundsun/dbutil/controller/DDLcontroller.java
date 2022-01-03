package com.hundsun.dbutil.controller;

import com.hundsun.dbutil.service.DDLService;
import com.hundsun.dbutil.service.IndexService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 返回与表相关或列相关的SQL语句,包括创建索引的语句
 * @author wangyang31647
 * @date 2020/08/10
 */
@RestController
@CrossOrigin
public class DDLcontroller {
    @Autowired
    private DDLService ddlService;
    @Autowired
    private IndexService indexService;

    /**
     * 如果传入的列名为空，则返回建表语句，如果列名不为空，则返回表的修改语句
     * @param tableName 表名
     * @param columnName 列名
     * @param kind 目标数据库类型 mysql / oracle
     * @return create 语句 或 alter 语句
     */
    @RequestMapping("/getDDL")
    public String getDDL(@RequestParam(value = "tableName", required = true) String tableName,
                         @RequestParam(value = "columnName", required = false, defaultValue = "") String columnName,
                         @RequestParam(value = "kind", required = true) String kind){

        if (columnName == null || columnName.isEmpty()){
            return ddlService.getTableDDL(tableName, kind);
        }else {
            return ddlService.getColumnDDL(tableName, columnName, kind);
        }

    }

    /**
     * 当列名为空时， 返回创建索引语句的集合，否则返回空值
     * @param tableName 表名
     * @param columnName 列名
     * @return null 或 create index 语句
     */
    @RequestMapping("/getIndex")
    public List<String> getIndices(@RequestParam(value = "tableName", required = true) String tableName,
                                   @RequestParam(value = "columnName", required = false) String columnName) {
        if (columnName == null || columnName.isEmpty()){
            return indexService.getIndexStatement(tableName);
        }else {
            return null;
        }

    }
}
