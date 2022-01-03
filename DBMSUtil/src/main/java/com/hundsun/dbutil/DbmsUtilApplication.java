package com.hundsun.dbutil;

import com.hundsun.dbutil.util.ConfigUtil;
import com.hundsun.dbutil.util.DBUtil;
import com.hundsun.fund.ecmp.common.log.LoggersUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.ibatis.io.Resources;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * spring 启动类
 * @author wangyang31647
 * @date 2020/08/10
 */
@SpringBootApplication
@MapperScan("com.hundsun.dbutil.dao")
public class DbmsUtilApplication {

	public static void main(String[] args) {
		// 启动时获取关于数据库的元数据
		Connection connection = DBUtil.getSession().getConnection();
		DatabaseMetaData metaData = null;
		try {
			metaData = connection.getMetaData();
			String sourceKind = metaData.getDatabaseProductName();
			ConfigUtil.sourceKind = sourceKind;
			ConfigUtil.schemaName = connection.getCatalog();
			ConfigUtil.userName = metaData.getUserName();
			System.out.println("username: " + ConfigUtil.userName);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		InputStream mysqlIn = null;
		InputStream oracleIn = null;
		try {
			mysqlIn = Resources.getResourceAsStream(ConfigUtil.MYSQL_TO_ORACLE_MAP_LOCATION);
			ConfigUtil.mysqlToOracleMap.load(mysqlIn);
			oracleIn = Resources.getResourceAsStream(ConfigUtil.ORACLE_TO_MYSQL_MAP_LOCATION);
			ConfigUtil.oracleToMysqlMap.load(oracleIn);
			mysqlIn.close(); oracleIn.close();
		} catch (FileNotFoundException e) {
			LoggersUtil.error(e.getMessage(), e);
		} catch (IOException e) {
			LoggersUtil.error(e.getMessage(), e);
		}

		SpringApplication.run(DbmsUtilApplication.class, args);
	}

}
