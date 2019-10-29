## 前言
在日常业务开发中，我们免不了每天要和SQL语句接触，虽然现在的ORM框架已经为我们省去了很多工作，封装了一套CRUD供我们使用，在垂直业务中，我们基本不再需要写SQL语句，但是很多业务之间是关联性比较强的，我们还是需要自己写SQL语句，同时，有很多地方，一些条件出现了很多次。

``` java
SELECT * FROM table order by id desc limi 0, 10
```

或者一些管理系统中，我们不光要做菜单权限，还需要做数据权限，例如，订单数据，我们只想让同部门及部门以下的人看见，这样的SQL语句，如果我们都写在Mapper文件里，一是可读性差，二是这样的SQL语句在系统里多次出现，无法复用。所以，我利用业余时间，开发了一个让我们少些很多SQL语句的模块，让我们不再业务缠身。

## 第一步 创建数据库表

![](https://tva1.sinaimg.cn/large/006y8mN6gy1g8f3o9zjbsj30mj04pgls.jpg)


---

![](https://tva1.sinaimg.cn/large/006y8mN6gy1g8f3r0tgogj30b10id0uo.jpg)

我们创建一个nba数据库，里面有一张队伍表，一张球员表，我们先看球员表这四个字段，很简单，我们不再说明。


## 第二步 定义你需要的条件

这时候，我们来了一个业务，我们需要查询球员表中，身高最高的前十名。好，我们先写一下这条SQL语句

``` java
SELECT * FROM player ORDER BY height LIMIT 0, 10
```

然后，你开始了你的编码工作

**service层**

``` java
  public List<Player> getList(Map<String,Object> params) {
        return playerMapper.getList(params);
    }
    
```
**mapper层**

``` java

    @Select("SELECT * FROM player ")
    List<Player> getList(Map params);
```

这时候有人发现，你SQL语句中后面的部分呢？排序条件，分页条件在哪里?

我们再回到service层，加上我们的注解
 
``` java
    @OrderBy(orderColumn = "height")
    @Limit()
    @Override
    public List<Player> getList(Map<String,Object> params) {
        return playerMapper.getList(params);
    }
```

- 在方法上加入OrderBy注解，注解里加上需要排序的字段名，默认倒序
- 在方法上加入Limit注解，则代表该方法中的SQL语句需要分页，默认一页10条数据。

贴一下，排序和分页的注解代码。
**@OrderBy**

``` java
/**
 * 自定义排序注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OrderBy {


    /**
     * 表的别名  多表联查时使用
     */
    String tableAlias() default "";

    /**
     * 排序字段
     */
    String orderColumn() default "";


    /**
     * ASC/DESC 默认倒序
     * @return
     */
    boolean isAsc() default false;

}

```
**@Limit**

``` java
/**
 * 自定义分页注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Limit {

    /**
     * 当前页面
     * @return
     */
    int page() default 0;


    /**
     * 每页显示数量
     * @return
     */
    int pageSize() default 10;



}

```

##  第三步 处理我们的注解

当程序开始运行之后，Spring 会去寻找哪些方法使用了条件注解，并且将其中的字段拼接成对应的SQL语句，保存在一个TreeMap容器中。

**OrderBy切面类**

``` java
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

```

- 切面类中的@Order(1)注解为优先级，数字越小越先执行，所以这也是OrderBy在Limit前面的原因。在OrderBy切面类中，我们生成了对应的排序语句，


``` java
ORDER BY XX DESC
```

**Limit切面类**

``` java
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

```
最后将生成对应的分页语句 

``` java
LIMIT 0, 10
```


---
我们可以看到，两个切面类都继承了**BaseAspectAbstract**这个抽象类

``` java
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
```

在两个切面类中的最后，我们调用了父类的putSQL方法。同时传入三个参数。

1. JoinPoint  (该类中封装了很多调用切入点的方法)
2. 对应的枚举属性，主要目的是安全
3. 拼接的SQL语句放入SQLLanguage对象中


---
看一下刚才说到的枚举类


``` java
public enum SQLEnums {

    /**
     *  勿:轻易修改 枚举中的id值 此属性为 TreeMap的key 由其去控制SQL语句拼接的顺序
     *  TreeMap为有序（对Integer来说，其自然排序就是数字的升序；对String来说，其自然排序就是按照字母表排序）
     *  所以数字越靠前 则拼接SQL语句越靠前执行，目前拼接顺序为
     *  SELECT * FROM table GROUP BY ORDER BY xxx LIMIT 0, 10
     */
    GROUPBY(1,"GROUP BY"),
    ORDERBY(2,"ORDER BY"),
    LIMIT(3,"LIMIT");

    private int id;

    private String condition;



    SQLEnums(int id,String condition) {
        this.id = id;
        this.condition = condition;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}

```
