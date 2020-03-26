package com.ai.spring.boot.test.nio;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.IntBuffer;
import java.util.stream.IntStream;

/**
 * NIO Buffer测试类
 * 1) 使用创建子类实例对象的allocate()方法，创建一个Buffer类的 实例对象。
 * 2) 调用put方法，将数据写入到缓冲区中。
 * 3) 写入完成后，在开始读取数据前，调用Buffer.flip()方法，将 缓冲区转换为读模式。
 * 4) 调用get方法，从缓冲区中读取数据。
 * 5) 读取完成后，调用Buffer.clear()或Buffer.compact()方法，将缓 冲区转换为写入模式。
 * @author 石头
 * @Date 2020/3/3
 * @Version 1.0
 **/
@Slf4j
public class BufferUseTest {
    @Test
    public void testBufferPut(){
        IntBuffer intBuffer = IntBuffer.allocate(20);
        log.info("-------------after allocate--------");
        log.info("position={}",intBuffer.position());
        log.info("limit={}",intBuffer.limit());
        log.info("capacity={}",intBuffer.capacity());

        IntStream.range(0,5).forEach(num -> intBuffer.put(num));
        log.info("-------------after put 5 num--------");
        log.info("position={}",intBuffer.position());
        log.info("limit={}",intBuffer.limit());
        log.info("capacity={}",intBuffer.capacity());

        // 翻转缓冲区，从写模式翻转成读模式
        intBuffer.flip();
        log.info("-------------after flip--------");
        log.info("position={}",intBuffer.position());
        log.info("limit={}",intBuffer.limit());
        log.info("capacity={}",intBuffer.capacity());

        IntStream.range(0,2).forEach(num -> {
            int rs = intBuffer.get();
            log.info("rs = {}",rs);
        });
        log.info("-------------after get 2 int--------");
        log.info("position={}",intBuffer.position());
        log.info("limit={}",intBuffer.limit());
        log.info("capacity={}",intBuffer.capacity());

        IntStream.range(0,3).forEach(num -> {
            int rs = intBuffer.get();
            log.info("rs = {}",rs);
        });
        log.info("-------------after get 3 int--------");
        log.info("position={}",intBuffer.position());
        log.info("limit={}",intBuffer.limit());
        log.info("capacity={}",intBuffer.capacity());

        // 已经读完的数据，如果需要再读一遍，可以调用rewind()方法
        intBuffer.rewind();
        log.info("-------------after rewind--------");
        log.info("position={}",intBuffer.position());
        log.info("limit={}",intBuffer.limit());
        log.info("capacity={}",intBuffer.capacity());

        IntStream.range(0,5).forEach(num -> {
            if (num == 2){
                intBuffer.mark();
            }
            int rs = intBuffer.get();
            log.info("rs = {}",rs);
        });
        log.info("-------------after get 5 int--------");
        log.info("position={}",intBuffer.position());
        log.info("limit={}",intBuffer.limit());
        log.info("capacity={}",intBuffer.capacity());

        intBuffer.reset();
        log.info("-------------after reset--------");
        log.info("position={}",intBuffer.position());
        log.info("limit={}",intBuffer.limit());
        log.info("capacity={}",intBuffer.capacity());
        IntStream.range(2,5).forEach(num -> {
            int rs = intBuffer.get();
            log.info("rs = {}",rs);
        });

        // 在读取模式下，调用clear()方法将缓冲区切换为写入模式。
        intBuffer.clear();
        log.info("-------------after clear--------");
        log.info("position={}",intBuffer.position());
        log.info("limit={}",intBuffer.limit());
        log.info("capacity={}",intBuffer.capacity());
    }
}
