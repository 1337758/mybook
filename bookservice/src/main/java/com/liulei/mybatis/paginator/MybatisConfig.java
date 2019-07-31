package com.liulei.mybatis.paginator;

import com.liulei.mybatis.dialect.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @author 杨号
 * @description 配置文件类
 * @date 2018年9月14日
 */
@Configuration
@EnableTransactionManagement
@Slf4j
public class MybatisConfig implements TransactionManagementConfigurer {
    @Autowired
    private DataSource dataSource;

    @Bean
    public OffsetLimitInterceptor offsetLimitInterceptor() throws SQLException {
        OffsetLimitInterceptor offsetLimitInterceptor = new OffsetLimitInterceptor();
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            String databaseProductName = metaData.getDatabaseProductName();
            String dbName = databaseProductName.toLowerCase();
            if (dbName.contains("mysql")) {
                offsetLimitInterceptor.setDialectClass(MySQLDialect.class.getName());
            } else if (dbName.contains("oracle")) {
                offsetLimitInterceptor.setDialectClass(OracleDialect.class.getName());
            } else if (dbName.contains("db2")) {
                offsetLimitInterceptor.setDialectClass(DB2Dialect.class.getName());
            } else if (dbName.contains("postgre")) {
                offsetLimitInterceptor.setDialectClass(PostgreSQLDialect.class.getName());
            } else if (dbName.contains("sql server")) {
                offsetLimitInterceptor.setDialectClass(SQLServerDialect.class.getName());
            } else if (dbName.contains("h2")) {
                offsetLimitInterceptor.setDialectClass(H2Dialect.class.getName());
            } else if (dbName.contains("hsql")) {
                offsetLimitInterceptor.setDialectClass(HSQLDialect.class.getName());
            } else if (dbName.contains("sybase")) {
                offsetLimitInterceptor.setDialectClass(SybaseDialect.class.getName());
            } else if (dbName.indexOf("sinoregal") != -1) {
                // sinoregal dynamic server,sinodb
                offsetLimitInterceptor.setDialectClass(InformixDialect.class.getName());
            } else if (dbName.indexOf("argon") != -1) {
                // argon dynamic server华胜
                offsetLimitInterceptor.setDialectClass(InformixDialect.class.getName());
            } else if (dbName.indexOf("ibm informix") != -1) {
                // ibm informix dynamic server南大
                offsetLimitInterceptor.setDialectClass(InformixDialect.class.getName());
            } else if (dbName.indexOf("informix") != -1) {
                // Informix Dynamic Server
                offsetLimitInterceptor.setDialectClass(InformixDialect.class.getName());
            } else {
                throw new IllegalArgumentException("Unsupport Database [" + databaseProductName + "]");
            }
            log.info("Current databaseProductName is [" + databaseProductName + "]");
        }
        return offsetLimitInterceptor;
    }

    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

}
