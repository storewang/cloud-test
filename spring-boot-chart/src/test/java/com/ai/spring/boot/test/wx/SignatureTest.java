package com.ai.spring.boot.test.wx;

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 石头 on 2020/9/12.
 */
public class SignatureTest {
    @Test
    public void checkSignature() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // 37_j989kKEmWrOH7777bmdz4-180kSvkP4Md3B4XPjceTRT6gGXq92ki-UOMuVEAZd3vHtZpmQ9srnHyMFfjuPmovSXa33XOG_I7NNAxKfJI095P3GyVlwUjjv-mNnEM9fIArF1LTquZGWqq8rcXQXcACAFYF
        // signature=85ce16a751d8474a4f5dad81eed7895f4335234a, timestamp=1599917568, nonce=334572976, echostr=3272554224029593483
        List<String> params = new ArrayList();
        params.add("wxyWxchart");
        params.add("1599923147");
        params.add("829350262");

        Collections.sort(params);
        String signature = list2Str(params);
        String encodeStr = getSha1(signature.getBytes("UTF-8"));
        System.out.println(encodeStr);
        Assert.assertEquals(encodeStr,"a11cfb78a391e034f43b347a2133cedd9e569852");


        /**
         * 公众号调用服务器接口，每次都会带signature,所以应该用拦截器进行signature验证
         url: signature=a11cfb78a391e034f43b347a2133cedd9e569852&timestamp=1599923147&nonce=829350262&openid=oibi0wEMq_NkO4tAobkAPlTtdudY

         <xml>
         <ToUserName><![CDATA[gh_f286de58d685]]></ToUserName>
         <FromUserName><![CDATA[oibi0wEMq_NkO4tAobkAPlTtdudY]]></FromUserName>
         <CreateTime>1599923147</CreateTime>
         <MsgType><![CDATA[event]]></MsgType>
         <Event><![CDATA[subscribe]]></Event>
         <EventKey><![CDATA[]]></EventKey>
         </xml>
         */
    }
    private String list2Str(List<String> params){
        StringBuffer sbf = new StringBuffer();
        params.stream().forEach(str -> sbf.append(str));
        return sbf.toString();
    }
    private String getSha1(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        byte[] result = digest.digest(input);
        StringBuffer sbf = new StringBuffer();
        for (int i =0;i<result.length;i++){
            sbf.append(Integer.toString((result[i] & 0xff) + 0x100,16).substring(1));
        }
        return sbf.toString();
    }
}
