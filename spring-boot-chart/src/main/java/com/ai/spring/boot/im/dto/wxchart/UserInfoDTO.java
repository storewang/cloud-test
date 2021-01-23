package com.ai.spring.boot.im.dto.wxchart;

import lombok.Data;

import java.util.List;

/**
 * Created by 石头 on 2020/9/13.
 */
@Data
public class UserInfoDTO {
    private Integer subscribe;
    private String openid;
    private String nickname;
    private Integer sex;
    private String city;
    private String country;
    private String province;
    private String language;
    private String headimgurl;
    private Long subscribe_time;
    private String unionid;
    private String remark;
    private Integer groupid;
    private List<Integer> tagid_list;
    private String subscribe_scene;
    private Long qr_scene;
    private String qr_scene_str;
}
