package com.liulei.mybatis.util;

import com.liulei.mybatis.dialect.Dialect;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 杨号
 * @description mybatis-paginator查询count
 * @date 2018年9月14日
 */
@Slf4j
public class SQLHelper {

    private SQLHelper() {

    }

    /**
     * 查询总纪录数
     *
     * @param mappedStatement-mapped
     * @param transaction-transaction
     * @param parameterObject-参数
     * @param boundSql-boundSql
     * @param dialect-方言
     * @return 总记录数
     * @throws SQLException-sql查询错误
     */
    public static int getCount(final MappedStatement mappedStatement, final Transaction transaction,
                               final Object parameterObject, final BoundSql boundSql, Dialect dialect) throws SQLException {
        final String countSql = dialect.getCountSQL();
        log.debug("Total count SQL [{}] ", countSql);
        log.debug("Total count Parameters: {} ", parameterObject);
        Connection connection = transaction.getConnection();
        int count = 0;
        try (PreparedStatement countStmt = connection.prepareStatement(countSql)) {
            DefaultParameterHandler handler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
            handler.setParameters(countStmt);
            try (ResultSet rs = countStmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        }
        log.debug("Total count: {}", count);
        return count;

    }

    /**
     * @param sql-sql语句
     * @return 去掉order后的sql语句
     * @description 去除hql的orderby 子句，用于pagedQuery.
     */
    public static String removeOrders(String sql) {
        Assert.hasText(sql, "sql must have value");
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * @param sql-sql语句
     * @return 包含Distinct返回true，否则返回false
     * @description 是否包含Distinct
     */
    public static boolean isIncludeDistinct(String sql) {
        String hqlLowerCase = sql.toLowerCase(Locale.US);
        hqlLowerCase = hqlLowerCase.replace(" ", "");
        return hqlLowerCase.startsWith("selectdistinct");
    }

}
