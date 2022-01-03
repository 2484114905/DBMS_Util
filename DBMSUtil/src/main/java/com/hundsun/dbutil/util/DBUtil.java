package com.hundsun.dbutil.util;

import com.hundsun.dbutil.dao.ColumnDAO;
import com.hundsun.dbutil.dao.IndexDAO;
import com.hundsun.fund.ecmp.common.log.LoggersUtil;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * @author wangyang31647
 * @date 2020/07/20 基于mybatis的数据库工具类/
 */
public class DBUtil {

    private static SqlSession session;

    /**
     * mybatis配置文件路径
     */
    private static String resource;

    public static String getResource() {
        return resource;
    }

    public static void setResource(String resource) {
        DBUtil.resource = resource;
    }

    /**
     * 获取mybatis中的SqlSession实例
     */
    public static SqlSession getSession() {
        if (session == null){
            InputStream inputStream = null;
            if (resource == null) {
                // 如果没有显式指定配置文件，使用默认配置文件
                resource = "mybatis-config.xml";
            }
            try {
                inputStream = Resources.getResourceAsStream(resource);
            } catch (IOException e) {
                LoggersUtil.error(e.getMessage(), e);
            }

            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
                .build(inputStream);
            session = sqlSessionFactory.openSession();
        }

        return session;
    }

    /**
     * 通过SqlSession 返回 ColumnDAO 对象
     * @return ColumnDAO
     */
   public static ColumnDAO getColumnMapper(){
        SqlSession sqlSession = getSession();
        ColumnDAO columnDAO = sqlSession.getMapper(ColumnDAO.class);
        return columnDAO;
   }

    /**
     * 通过SqlSession 返回 IndexDAO 对象
     * @return IndexDAO
     */
   public static IndexDAO getIndexMapper(){
       SqlSession sqlSession = getSession();
       IndexDAO indexDAO = sqlSession.getMapper(IndexDAO.class);
       return indexDAO;
   }
}
