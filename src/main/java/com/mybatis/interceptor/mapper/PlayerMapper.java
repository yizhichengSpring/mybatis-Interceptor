package com.mybatis.interceptor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mybatis.interceptor.entity.Player;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

/**
 * @author yi
 * @ClassName PlayerMapper
 * @Description TODO
 * @Date
 **/
public interface PlayerMapper extends BaseMapper<Player> {


    @Select("SELECT * FROM player ")
    List<Player> getList(Map params);
}
