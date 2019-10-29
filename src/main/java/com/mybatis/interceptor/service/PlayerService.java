package com.mybatis.interceptor.service;
import com.mybatis.interceptor.entity.Player;
import java.util.List;
import java.util.Map;

public interface PlayerService {

    List<Player> getList(Map<String,Object> params);
}
