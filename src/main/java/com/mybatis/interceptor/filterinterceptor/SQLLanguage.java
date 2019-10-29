package com.mybatis.interceptor.filterinterceptor;

/**
 * @author yi
 * @ClassName SQLLanguage
 * @Description TODO
 * @Date
 **/
public class SQLLanguage {

    private String sqlLanguage;


    public SQLLanguage(String sqlLanguage) {
        this.sqlLanguage = sqlLanguage;
    }

    public SQLLanguage() {
    }

    @Override
    public String toString() {
        return sqlLanguage;
    }
}
