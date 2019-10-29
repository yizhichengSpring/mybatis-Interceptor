package com.mybatis.interceptor.aspect;
import com.mybatis.interceptor.abstracts.BaseAspectAbstract;
import com.mybatis.interceptor.annotation.Limit;
import com.mybatis.interceptor.filterinterceptor.SQLLanguage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import static com.mybatis.interceptor.enums.SQLEnums.LIMIT;

/**
 * @author yi
 * @ClassName LimitAspect
 * @Description TODO
 * @Date
 **/
@Component
@Aspect
@Order(2)
public class LimitAspect extends BaseAspectAbstract {

    @Pointcut("@annotation(com.mybatis.interceptor.annotation.Limit)")
    public void limitCut() {}

    @Before("limitCut()")
    public void limit(JoinPoint point) {
        StringBuilder limitBuilder = new StringBuilder(" LIMIT ");
        MethodSignature methodSignature = (MethodSignature)point.getSignature();
        //获得对应注解
        Limit limit =
                methodSignature.getMethod().getAnnotation(Limit.class);
        if (!StringUtils.isEmpty(limit)) {
            limitBuilder.append(limit.page()).append(",").append(limit.pageSize());
            putSQL(point,LIMIT,new SQLLanguage(limitBuilder.toString()));
        }

    }
}
