package com.mybatis.interceptor.filterinterceptor;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * @author yi
 * @ClassName DataFilterInterceptor
 * @Description 拦截器
 * @Date
 **/
@Intercepts(
        @Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class,Integer.class}
                )
)
@Component
public class DataFilterInterceptor extends AbstractSqlParserHandler implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler =
                PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        //SQLLanguage 解析
        sqlParser(metaObject);
        //非查询操作
        MappedStatement mappedStatement = (MappedStatement)metaObject.getValue("delegate.mappedStatement");
        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }

        //取出原始SQL 取出参数
        BoundSql boundSql = (BoundSql)metaObject.getValue("delegate.boundSql");
        String dataSql = boundSql.getSql();
        Object paramObj = boundSql.getParameterObject();
        Map map = (Map) paramObj;
        String sqlLanguage = getSQLLanguage(map);
        String sql = dataSql+sqlLanguage;
        //重写sql
        metaObject.setValue("delegate.boundSql.sql",sql);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }




    private String getSQLLanguage(Map<String,Object> map) {
        TreeMap<Integer,SQLLanguage> sqlMap = (TreeMap) map.get("SQL");
        StringBuilder sqlBuilder = new StringBuilder();
        for (Map.Entry treeMap:sqlMap.entrySet()) {
            SQLLanguage sql = (SQLLanguage) treeMap.getValue();
            if (null != sql) {
                sqlBuilder.append(sql);
            }
        }
        return sqlBuilder.toString();
    }
}
