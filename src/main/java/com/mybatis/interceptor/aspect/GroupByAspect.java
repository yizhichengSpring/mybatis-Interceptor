package com.mybatis.interceptor.aspect;
import com.mybatis.interceptor.abstracts.BaseAspectAbstract;
import com.mybatis.interceptor.annotation.GroupBy;
import com.mybatis.interceptor.filterinterceptor.SQLLanguage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import static com.mybatis.interceptor.enums.SQLEnums.GROUPBY;

/**
 * @author yi
 * @ClassName GroupByAspect
 * @Description TODO
 * @Date
 **/
@Component
@Aspect
@Order(0)
public class GroupByAspect extends BaseAspectAbstract {

    @Pointcut("@annotation(com.mybatis.interceptor.annotation.GroupBy)")
    public void groupByCut() {}

    @Before("groupByCut()")
    public void groupBy(JoinPoint point) {
        StringBuilder groupByBuilder = new StringBuilder(" GROUP BY ");
        MethodSignature methodSignature = (MethodSignature)point.getSignature();
        //获得对应注解
        GroupBy groupBy =
                methodSignature.getMethod().getAnnotation(GroupBy.class);
        if (!StringUtils.isEmpty(groupBy)) {
            if (!StringUtils.isEmpty(groupBy.tableAlias())) {
                groupByBuilder.append(groupBy.tableAlias()+".");
            }
            groupByBuilder.append(groupBy.column());
            putSQL(point,GROUPBY,new SQLLanguage(groupByBuilder.toString()));
        }
    }
}
