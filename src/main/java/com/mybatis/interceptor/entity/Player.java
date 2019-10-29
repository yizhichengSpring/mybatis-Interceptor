package com.mybatis.interceptor.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author yi
 * @ClassName Player
 * @Description TODO
 * @Date
 **/
@Data
@TableName("player")
public class Player {

    //@TableField("player_id")
    private Long playerId;

    //@TableField("team_id")
    private Long teamId;


    //@TableField("player_name")
    private String playerName;

    private Double height;

}
