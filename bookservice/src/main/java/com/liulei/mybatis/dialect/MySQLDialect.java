package com.liulei.mybatis.dialect;

import com.liulei.mybatis.paginator.PageParam;
import org.apache.ibatis.mapping.MappedStatement;

public class MySQLDialect extends Dialect{

    public MySQLDialect(MappedStatement mappedStatement, Object parameterObject, PageParam pageParam) {
        super(mappedStatement, parameterObject, pageParam);
    }
    
    @Override
	protected String getLimitString(String sql, String offsetName,int offset, String limitName, int limit) {
        StringBuilder buffer = new StringBuilder( sql.length()+20 ).append(sql);
        if (offset > 0) {
            buffer.append(" limit ?, ?");
            setPageParameter(offsetName, offset, Integer.class);
            setPageParameter(limitName, limit, Integer.class);
        } else {
            buffer.append(" limit ?");
            setPageParameter(limitName, limit, Integer.class);
        }
        return buffer.toString();
	}   
  
}
