package com.mybatis.interceptor.abstracts;

import com.mybatis.interceptor.filterinterceptor.SQLLanguage;
import com.mybatis.interceptor.enums.SQLEnums;
import org.aspectj.lang.JoinPoint;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author yi
 * @ClassName BaseAspectAbstract
 * @Description 基础切面处理类，每一个Spring的切面都需要去继承此抽象类
 * @Date
 **/
public abstract class BaseAspectAbstract {

    private static TreeMap<Integer,SQLLanguage> CONTAINERS = new TreeMap<>();

    public void putSQL(JoinPoint point,SQLEnums sqlEnums,SQLLanguage sqlLanguage) {
        CONTAINERS.put(sqlEnums.getId(),sqlLanguage);
        //获取方法里的参数
        Object parmas = point.getArgs()[0];
        Map map = (Map) parmas;
        map.put("SQL",getSQL());
    }


    public TreeMap<Integer,SQLLanguage> getSQL() {
        return CONTAINERS;
    }


}
