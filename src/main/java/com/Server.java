package com;

import com.hearbeat.HearBeatRunable;
import com.server.DeamonChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
@Slf4j
public class Server {

    public static EventLoopGroup bossGroup = null;
    public static EventLoopGroup workerGroup = null;

    public static void main(String[] args) {
        // 写pid文件
        writePid();
        try {
            new Server().bind(1111);
        } catch (Exception e) {
            log.error("启动失败, {}", e);
            System.exit(0);
        }
    }

    public void bind(int port) throws Exception {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
         .channel(NioServerSocketChannel.class)
         .option(ChannelOption.SO_BACKLOG, 1024)
         // .option(ChannelOption.TCP_NODELAY, true)
         .childOption(ChannelOption.TCP_NODELAY, true)
         .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 10 * 1024 * 1024, 1000 * 1024 * 1024))
         .childOption(ChannelOption.SO_KEEPALIVE, Boolean.valueOf(true))
         .childHandler(new DeamonChannelInitializer());
        log.info("bind port:" + port);
        try {
            ChannelFuture f = b.bind(port).sync();
            // 心跳机制
            log.info("心跳机制");
            startHeartbeat(port, "127.0.0.1:2181", 60000, 60);

            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

    private static void startHeartbeat(int port, String connectStr, int baseSleepTimeMs, int maxRetries) throws Exception {
        HearBeatRunable hearBeatRunable = new HearBeatRunable(port, connectStr, baseSleepTimeMs, maxRetries);
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(hearBeatRunable, 0, 10, TimeUnit.SECONDS);
    }

    private static void writePid() {
        File pidFile = new File("pid");
        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        try {
            FileWriter fw = new FileWriter(pidFile);
            fw.write(pid);
            fw.close();
        } catch (IOException e) {
            log.info("无法写入PID文件,错误信息:{}", e);
        }
    }
}
