package com.ai.spring.boot.test.nio;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO Channel测试类
 *
 * @author 石头
 * @Date 2020/3/3
 * @Version 1.0
 **/
@Slf4j
public class ChannelUseTest {

    @Test
    public void testFastFileCopyChannel() throws IOException {
        String srcFile = "G:\\e\\jeecg-boot-module-system-2.1.0.jar";
        String desFile = "G:\\e\\jeecg-boot-module-system-2.1.0_copy2.jar";

        long startTime = System.currentTimeMillis();

        FileInputStream fis   = null;
        FileChannel inChannel = null;
        FileOutputStream fos  = null;
        FileChannel outchannel = null;
        try {
            // 获取FileChannel通道
            fis = new FileInputStream(srcFile);
            inChannel = fis.getChannel();

            fos = new FileOutputStream(desFile);
            outchannel = fos.getChannel();

            // 总字节数
            long size = inChannel.size();
            // 偏移量
            long pos = 0;
            // 每次复制的量
            long count = 0;
            while (pos < size) {
                // 每次复制最多1024个字节，没有就复制剩余的
                count = size - pos > 1024 ? 1024 : size - pos;
                // 复制内存,偏移量pos + count长度
                pos += outchannel.transferFrom(inChannel, pos, count);
            }

            // 强制刷新到磁盘
            outchannel.force(true);
        }finally {
            //关闭所有可关闭的对象
            closeQuietyly(outchannel);
            closeQuietyly(fos);
            closeQuietyly(inChannel);
            closeQuietyly(fis);
        }
        long endTime = System.currentTimeMillis();
        log.info("base复制毫秒数：{}",endTime - startTime);
    }
    @Test
    public void testFileCopyChannel() throws IOException {
        String srcFile = "G:\\e\\jeecg-boot-module-system-2.1.0.jar";
        String desFile = "G:\\e\\jeecg-boot-module-system-2.1.0_copy1.jar";

        long startTime = System.currentTimeMillis();

        FileInputStream fis   = null;
        FileChannel inChannel = null;
        FileOutputStream fos  = null;
        FileChannel outchannel = null;
        try {
            // 获取FileChannel通道
            fis = new FileInputStream(srcFile);
            inChannel = fis.getChannel();

            fos = new FileOutputStream(desFile);
            outchannel = fos.getChannel();

            //RandomAccessFile aFile = new RandomAccessFile(srcFile,"rw");
            //FileChannel aInChannel = aFile.getChannel();

            // 读取FileChannel通道
            int length = -1;
            ByteBuffer buf = ByteBuffer.allocate(1024);
            // 从输入通道读取到buf
            while ((length = inChannel.read(buf))!= -1){
                // 第一次切换：翻转buf，变成读取模式
                buf.flip();
                int outlength = 0;
                // 将buf写入到输出的通道
                while ((outlength = outchannel.write(buf))!=0){
                    log.info("写入的字节数:{}",outlength);
                }
                // 第二次切换,清除buf,变成写入模式
                buf.clear();
            }

            // 强制刷新到磁盘
            outchannel.force(true);
        }finally {
            //关闭所有可关闭的对象
            closeQuietyly(outchannel);
            closeQuietyly(fos);
            closeQuietyly(inChannel);
            closeQuietyly(fis);
        }
        long endTime = System.currentTimeMillis();
        log.info("base复制毫秒数：{}",endTime - startTime);
    }

    private void closeQuietyly(Closeable closeable){
        if (closeable!=null){
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }
}
