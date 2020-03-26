package com.ai.spring.boot.test.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * 几种Buffer类型测试
 * 4.1.16.Final buffer()方法返回的是Direct ByteBuf
 * Heap ByteBuf通过调用分配器的heapBuffer()方法来创 建；
 * 而Direct ByteBuf的创建，是通过调用分配器的directBuffer()方法
 * 如果hasArray()返回false，不一定代表缓冲区一定就是Direct ByteBuf直接缓冲区，也有可能是CompositeByteBuf缓冲区。
 * @author 石头
 * @Date 2020/3/5
 * @Version 1.0
 **/
@Slf4j
public class BufferTypeTest {
    final static Charset UTF_8 = Charset.forName("UTF-8");

    @Test
    public void intCompositeBufComposite() {
        CompositeByteBuf cbuf = Unpooled.compositeBuffer(3);
        cbuf.addComponent(Unpooled.wrappedBuffer(new byte[]{1, 2, 3}));
        cbuf.addComponent(Unpooled.wrappedBuffer(new byte[]{4}));
        cbuf.addComponent(Unpooled.wrappedBuffer(new byte[]{5, 6}));
        //合并成一个单独的缓冲区，这里返回java.NIO中的byteBuffer
        ByteBuffer nioBuffer = cbuf.nioBuffer(0, 6);
        byte[] bytes = nioBuffer.array();
        System.out.print("bytes = ");
        for (byte b : bytes) {
            System.out.print(b);
        }
        cbuf.release();
    }
    /**
     * 在很多通信编程场景下，需要多个ByteBuf组成一个完整的消息：
     * 例如HTTP协议传输时消息总是由Header（消息头）和Body（消息体） 组成的。
     * 如果传输的内容很长，就会分成多个消息包进行发送，
     * 消息中 的Header就需要重用，而不是每次发送都创建新的Header。
     */
    @Test
    public void byteBufComposite(){
        CompositeByteBuf cbuf = ByteBufAllocator.DEFAULT.compositeBuffer();
        //消息头
        ByteBuf headerBuf = Unpooled.copiedBuffer("疯狂创客圈:", UTF_8);
        //消息体1
        ByteBuf bodyBuf = Unpooled.copiedBuffer("高性能 Netty", UTF_8);
        cbuf.addComponents(headerBuf, bodyBuf);
        sendMsg(cbuf);
        // 在refCnt为0前, retain
        headerBuf.retain();
        cbuf.release();

        cbuf = ByteBufAllocator.DEFAULT.compositeBuffer();
        //消息体2
        bodyBuf = Unpooled.copiedBuffer("高性能学习社群", UTF_8);
        cbuf.addComponents(headerBuf, bodyBuf);
        sendMsg(cbuf);
        cbuf.release();
    }

    private void sendMsg(CompositeByteBuf cbuf) {
        //处理整个消息
        for (ByteBuf b : cbuf) {
            int length = b.readableBytes();
            byte[] array = new byte[length];
            //将CompositeByteBuf中的数据复制到数组中
            b.getBytes(b.readerIndex(), array);
            //处理一下数组中的数据
            System.out.print(new String(array, UTF_8));
        }
        System.out.println();
    }

    //堆缓冲区
    @Test
    public  void testHeapBuffer() {
        //取得堆内存
        ByteBuf heapBuf =  ByteBufAllocator.DEFAULT.heapBuffer();
        heapBuf.writeBytes("疯狂创客圈:高性能学习社群".getBytes(UTF_8));
        if (heapBuf.hasArray()) {
            //取得内部数组
            byte[] array = heapBuf.array();
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            int length = heapBuf.readableBytes();
            log.info(new String(array,offset,length, UTF_8));
        }
        heapBuf.release();

    }

    //直接缓冲区
    @Test
    public  void testDirectBuffer() {
        ByteBuf directBuf =  ByteBufAllocator.DEFAULT.directBuffer();
        directBuf.writeBytes("疯狂创客圈:高性能学习社群".getBytes(UTF_8));
        if (!directBuf.hasArray()) {
            int length = directBuf.readableBytes();
            byte[] array = new byte[length];
            //读取数据到堆内存
            directBuf.getBytes(directBuf.readerIndex(), array);
            log.info(new String(array, UTF_8));
        }
        directBuf.release();
    }
}
