package com.ai.spring.boot.netty.json.test;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 *
 * @author 石头
 * @Date 2021/1/8
 * @Version 1.0
 **/
@Slf4j
public class WebSocketClientTest {
    private AtomicInteger startedNum = new AtomicInteger(0);
    @Test
    public void testSendMsgWithNettyWebsocket(){
        String sendMsg = "{\n" +
                "    \"from\": {\n" +
                "        \"userCode\": \"e3f4fa340486f916f3752679888dca43067290c053f3760491a587608d8d8bdcb4f880751a90900a9c85a49b1895fc77\",\n" +
                "        \"userId\": 1,\n" +
                "        \"userName\": \"用户01\"\n" +
                "    },\n" +
                "    \"to\": {\n" +
                "        \"userCode\": \"20a502515b1cbc3626d02a69bc53a3ab4066647275e89bba52474049723f76f6450e2c11d5f75a35a69425df47dad813\",\n" +
                "        \"userId\": 3,\n" +
                "        \"userName\": \"用户03\"\n" +
                "    },\n" +
                "    \"tos\": null,\n" +
                "    \"content\": \"欢迎来到美丽的漓江！！！\",\n" +
                "    \"msgType\": 6,\n" +
                "    \"createTime\": 1609903607538,\n" +
                "    \"msgId\": \"MSG-1346659451166846978\"\n" +
                "}";
        WebSocketClient client = new ReactorNettyWebSocketClient();
        client.execute(URI.create("ws://localhost:8040/ws?token=67c987017fe26e6d29f8ae018145ffcc"), session ->
            session.send(Flux.just(session.textMessage(sendMsg)))
            .thenMany(session.receive().take(1000).map(WebSocketMessage::getPayloadAsText))
            .doOnNext(System.out::println)
            .then())
        .block(Duration.ofMillis(5000));

    }

    @Test
    public void testSendMsgWithJavaWebsocket() throws InterruptedException {
        org.java_websocket.client.WebSocketClient client = createWebSocketClient();
        client.setConnectionLostTimeout(0);
        client.connectBlocking();

        String sendMsg = "{\n" +
                "    \"from\": {\n" +
                "        \"userCode\": \"e3f4fa340486f916f3752679888dca43067290c053f3760491a587608d8d8bdcb4f880751a90900a9c85a49b1895fc77\",\n" +
                "        \"userId\": 1,\n" +
                "        \"userName\": \"用户01\"\n" +
                "    },\n" +
                "    \"to\": {\n" +
                "        \"userCode\": \"20a502515b1cbc3626d02a69bc53a3ab4066647275e89bba52474049723f76f6450e2c11d5f75a35a69425df47dad813\",\n" +
                "        \"userId\": 3,\n" +
                "        \"userName\": \"用户03\"\n" +
                "    },\n" +
                "    \"tos\": null,\n" +
                "    \"content\": \"欢迎来到美丽的漓江！！！\",\n" +
                "    \"msgType\": 6,\n" +
                "    \"createTime\": 1609903607538,\n" +
                "    \"msgId\": \"MSG-1346659451166846978\"\n" +
                "}";

        if (client.isOpen()){
            client.send(ByteBuffer.wrap(sendMsg.getBytes()));
        }
    }

    private org.java_websocket.client.WebSocketClient createWebSocketClient(){
        final org.java_websocket.client.WebSocketClient client = new org.java_websocket.client.WebSocketClient(URI.create("ws://localhost:8040/ws?token=67c987017fe26e6d29f8ae018145ffcc")){

            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                log.info("[{}]开始建立链接...","214eac8035bb9e31bb2dfa31bd328c35");

            }

            @Override
            public void onMessage(String s) {
                log.info("[{}]收到消息:{}...","214eac8035bb9e31bb2dfa31bd328c35",s);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                log.info("[{}]客户端已关闭!","214eac8035bb9e31bb2dfa31bd328c35");
                log.info("[{}]开始尝试重新连接:{}...","214eac8035bb9e31bb2dfa31bd328c35",startedNum.get());
                try {
                    if (startedNum.get() < 5){
                        createWebSocketClient();

                    }else {
                        log.info("[{}]尝试重新连接次数过多:{}...","214eac8035bb9e31bb2dfa31bd328c35",startedNum.get());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("[{}]重新连接失败,请检查网络!","214eac8035bb9e31bb2dfa31bd328c35");
                }

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                log.info("[{}]客户端发生错误,即将关闭!","214eac8035bb9e31bb2dfa31bd328c35");
            }
        };
        return client;
    }
}
