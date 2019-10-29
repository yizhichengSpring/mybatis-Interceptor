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

    private Long playerId;

    private Long teamId;

    private String playerName;

    private Double height;

}
