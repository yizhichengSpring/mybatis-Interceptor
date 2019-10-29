package com.mybatis.interceptor.aspect;
import com.mybatis.interceptor.abstracts.BaseAspectAbstract;
import com.mybatis.interceptor.annotation.OrderBy;
import com.mybatis.interceptor.filterinterceptor.SQLLanguage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import static com.mybatis.interceptor.enums.SQLEnums.ORDERBY;

/**
 * @author yi
 * @ClassName DataFilterAspect
 * @Description TODO
 * @Date
 **/
@Component
@Aspect
@Order(1)
public class OrderByAspect extends BaseAspectAbstract {


    @Pointcut("@annotation(com.mybatis.interceptor.annotation.OrderBy)")
    public void orderByCut() {}


    @Before("orderByCut()")
    public void orderBy(JoinPoint point) {
        StringBuilder orderByBuilder = new StringBuilder(" ORDER BY ");
        MethodSignature methodSignature = (MethodSignature)point.getSignature();
        //获得对应注解
        OrderBy orderBy = methodSignature.getMethod().getAnnotation(OrderBy.class);
        if (!StringUtils.isEmpty(orderBy)) {
            String sort = orderBy.isAsc() ? " asc ":" desc" ;
            orderByBuilder.append(orderBy.orderColumn()).append(sort);
            putSQL(point,ORDERBY,new SQLLanguage(orderByBuilder.toString()));
        }
    }
}
