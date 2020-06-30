package com.ai.spring.boot.netty.ws.handler;

import com.ai.spring.boot.netty.ws.service.ServerHandlerService;
import com.ai.spring.boot.netty.ws.util.Consts;
import com.ai.spring.im.common.util.StringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * http request handler
 *
 * @author 石头
 * @Date 2020/6/28
 * @Version 1.0
 **/
@Slf4j
public class HttpRequestChannelHandler extends UserChannelHandler<FullHttpRequest>{
    private String wsContextPath;
    private ServerHandlerService serverHandlerService;
    public HttpRequestChannelHandler(String wsContextPath,ServerHandlerService serverHandlerService){
        this.wsContextPath        = wsContextPath;
        this.serverHandlerService = serverHandlerService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String uri = StringUtil.substringBefore(request.uri(), Consts.URL_SPLIT);
        if(wsContextPath.equalsIgnoreCase(uri)) {
            QueryStringDecoder query = new QueryStringDecoder(request.uri());
            Map<String, List<String>> map = query.parameters();
            List<String> tokens = map.get(Consts.URL_TOKEN_KEY);
            if(tokens != null && !tokens.isEmpty()) {
                String token = tokens.get(0);
                if (serverHandlerService.checkToken(token)){
                    ctx.channel().attr(AttributeKey.<String>valueOf(Consts.CHANNEL_TOKEN_KEY)).getAndSet(token);

                    request.setUri(uri);
                    ctx.fireChannelRead(request.retain());

                }else {
                    log.warn("---------[{}]token验证失败.--------",token);
                    ctx.close();
                }
            }else {
                log.warn("---------[{}]请携带token再请求ws连接.--------",request.uri());
                ctx.close();
            }
        }else {
            log.warn("---------[{}]请求url不匹配.--------",request.uri());
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("==>FullHttpRequest.err",cause);
        ctx.close();
    }
}
