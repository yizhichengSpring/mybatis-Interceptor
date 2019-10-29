package com.mybatis.interceptor.controller;
import com.mybatis.interceptor.entity.Player;
import com.mybatis.interceptor.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

/**
 * @author yi
 * @ClassName PlayController
 * @Description TODO
 * @Date
 **/
@RestController
@RequestMapping("/nba")
public class PlayController {

    @Autowired
    private PlayerService playerService;

    @RequestMapping("/player")
    public List<Player> getList(Map<String,Object> params) {
        List<Player> players = playerService.getList(params);
        return players;
    }
}
