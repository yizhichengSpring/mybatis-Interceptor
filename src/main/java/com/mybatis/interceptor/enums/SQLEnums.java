package com.mybatis.interceptor.enums;

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
