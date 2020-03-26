package com.ai.spring.boot.im.conf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * TODO
 *
 * @author 石头
 * @Date 2020/3/18
 * @Version 1.0
 **/
@Slf4j
public class MyFastJsonHttpMessageConverter extends FastJsonHttpMessageConverter {
    private FastJsonConfig fastJsonConfig = new FastJsonConfig();
    @Override
    public FastJsonConfig getFastJsonConfig() {
        return fastJsonConfig;
    }
    @Override
    public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
        super.setFastJsonConfig(fastJsonConfig);
    }

    @Override
    public Object read(Type type, //
                       Class<?> contextClass, //
                       HttpInputMessage inputMessage //
    ) throws IOException, HttpMessageNotReadableException {
        return readType(getType(type, contextClass), inputMessage);
    }

    private Object readType(Type type, HttpInputMessage inputMessage) throws IOException {

        try {
            InputStream in = inputMessage.getBody();
            byte[] bytes   = new byte[in.available()];
            in.read(bytes);
            printMessge(bytes);

            return JSON.parseObject(bytes,0,bytes.length,fastJsonConfig.getCharset(),type,fastJsonConfig.getFeatures());
        } catch (JSONException ex) {
            throw new HttpMessageNotReadableException("JSON parse error: " + ex.getMessage(), ex);
        } catch (IOException ex) {
            throw new HttpMessageNotReadableException("I/O error while reading input message", ex);
        }
    }

    private void printMessge(byte[] bytes){
        String messg = new String(bytes,fastJsonConfig.getCharset());
        log.info("-----------json messg :{}--------------",messg);
    }
}
