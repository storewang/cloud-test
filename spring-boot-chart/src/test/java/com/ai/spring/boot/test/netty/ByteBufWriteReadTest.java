package com.ai.spring.boot.test.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.IllegalReferenceCountException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * netty ByteBuf 测试
 * 在默认情况下，当创建完一 个ByteBuf时，它的引用为1；
 * 每次调用retain()方法，它的引用就加1；
 * 每次调用release()方法，就是将引用计数减1；
 * 如果引用为0，再次访问 这个ByteBuf对象，将会抛出异常；
 * 如果引用为0，表示这个ByteBuf没 有哪个进程引用它，它占用的内存需要回收。
 * @author 石头
 * @Date 2020/3/5
 * @Version 1.0
 **/
@Slf4j
public class ByteBufWriteReadTest {
    /**
     * 1. 切片不会复制源ByteBuf的底层数据，底层数组和源ByteBuf的底层 数组是同一个。
     * 2. 切片不会改变源ByteBuf的引用计数。
     * 3. 从根本上说，slice()无参数方法所生成的切片就是源ByteBuf可读部 分的浅层复制
     * 4. 切片不可以写入，原因是：maxCapacity与writerIndex值相同。
     * 5. 切片和源ByteBuf的可读字节数相同，原因是：切片后的可读字节 数为自己的属性writerIndex – readerIndex，也就是源ByteBuf的 readableBytes() - 0。
     */
    @Test
    public void testSlice(){
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);
        print("动作：分配 ByteBuf(9, 100)", buffer);
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        print("动作：写入4个字节 (1,2,3,4)", buffer);
        ByteBuf slice = buffer.slice();
        print("动作：切片 slice", slice);
    }

    @Test
    public void testRef(){
        ByteBuf buffer  = ByteBufAllocator.DEFAULT.buffer();
        log.info("after create:"+buffer.refCnt());
        buffer.retain();
        log.info("after retain:"+buffer.refCnt());
        buffer.release();
        log.info("after release:"+buffer.refCnt());
        buffer.release();
        log.info("after release:"+buffer.refCnt());
        try{
            //错误:refCnt: 0,不能再retain
            buffer.retain();
            log.info("after retain:"+buffer.refCnt());
        }catch (IllegalReferenceCountException e){
            Assert.assertTrue("错误:refCnt: 0,不能再retain",Boolean.TRUE);
        }

    }
    @Test
    public void testWriteRead() {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        print("动作：分配 ByteBuf(9, 100)", buffer);
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        print("动作：写入4个字节 (1,2,3,4)", buffer);
        log.info("start==========:get==========");
        getByteBuf(buffer);
        print("动作：取数据 ByteBuf", buffer);
        log.info("start==========:read==========");
        readByteBuf(buffer);
        print("动作：读完 ByteBuf", buffer);
    }

    //读取一个字节
    private void readByteBuf(ByteBuf buffer) {
        while (buffer.isReadable()) {
            log.info("读取一个字节:" + buffer.readByte());
        }
    }


    //读取一个字节，不改变指针
    private void getByteBuf(ByteBuf buffer) {
        for (int i = 0; i < buffer.readableBytes(); i++) {
            log.info("读取一个字节:" + buffer.getByte(i));
        }
    }

    public static void print(String action, ByteBuf b) {
        log.info("after ===========" + action + "============");
        log.info("1.0 isReadable(): " + b.isReadable());
        log.info("1.1 readerIndex(): " + b.readerIndex());
        log.info("1.2 readableBytes(): " + b.readableBytes());
        log.info("2.0 isWritable(): " + b.isWritable());
        log.info("2.1 writerIndex(): " + b.writerIndex());
        log.info("2.2 writableBytes(): " + b.writableBytes());
        log.info("3.0 capacity(): " + b.capacity());
        log.info("3.1 maxCapacity(): " + b.maxCapacity());
        log.info("3.2 maxWritableBytes(): " + b.maxWritableBytes());
    }
}
