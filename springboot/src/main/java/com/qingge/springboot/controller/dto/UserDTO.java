package com.qingge.springboot.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * 接收前端登录请求的参数
 *
 */
@Data
@Alias("userDTO")
public class UserDTO {

    private String username;
    private String password;

    private String nickname;
    private String avatarUrl;

}
