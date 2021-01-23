package com.ai.spring.boot.test.wx;

import com.ai.spring.boot.im.IMApplication;
import com.ai.spring.boot.im.dto.wxchart.UserInfoDTO;
import com.ai.spring.boot.im.util.WxApiConst;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * Created by 石头 on 2020/9/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IMApplication.class)
public class WxApiTest {
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testGetUserInfo(){
        String accessToken = "37_j989kKEmWrOH7777bmdz4-180kSvkP4Md3B4XPjceTRT6gGXq92ki-UOMuVEAZd3vHtZpmQ9srnHyMFfjuPmovSXa33XOG_I7NNAxKfJI095P3GyVlwUjjv-mNnEM9fIArF1LTquZGWqq8rcXQXcACAFYF";
        String openId = "oibi0wEMq_NkO4tAobkAPlTtdudY";
        String userUrl = String.format(WxApiConst.userInfoUrl,accessToken,openId);

        UserInfoDTO userInfoDTO = restTemplate.getForObject(userUrl, UserInfoDTO.class);
        System.out.println(userInfoDTO);
        Assert.assertNotNull(userInfoDTO.getOpenid());
    }
}
