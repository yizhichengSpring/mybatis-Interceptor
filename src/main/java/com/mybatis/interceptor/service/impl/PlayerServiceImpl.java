package com.mybatis.interceptor.service.impl;
import com.mybatis.interceptor.annotation.GroupBy;
import com.mybatis.interceptor.annotation.Limit;
import com.mybatis.interceptor.annotation.OrderBy;
import com.mybatis.interceptor.entity.Player;
import com.mybatis.interceptor.mapper.PlayerMapper;
import com.mybatis.interceptor.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * @author yi
 * @ClassName PlayerServiceImpl
 * @Description TODO
 * @Date
 **/
@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerMapper playerMapper;


    @GroupBy(column = "player_id")
    @OrderBy(orderColumn = "player_id")
    @Limit()
    @Override
    public List<Player> getList(Map<String,Object> params) {
        return playerMapper.getList(params);
    }
}
