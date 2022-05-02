package com;

import com.client.DeamonClientHandlerAbs;
import com.client.handler.SystemInfoClientHandler;
import com.code.NettyMessageDecoder;
import com.code.NettyMessageEncoder;
import com.modle.Result;
import com.modle.SystemInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {
    private DeamonClientHandlerAbs handler;

    public Client(DeamonClientHandlerAbs handler) {
        this.handler = handler;
    }

    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();
        SystemInfoClientHandler infoClientHandler = new SystemInfoClientHandler();
        infoClientHandler.setSystemInfo(new SystemInfo());

        Client client = new Client(infoClientHandler);
        client.handler(1111, "127.0.0.1");
        // Result result = infoClientHandler.getResult();
        SystemInfo systemInfo = infoClientHandler.getSystemInfo();

        log.info("处理后的result: {}", systemInfo.toString());


    }

    public void handler(int port, String host) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
             .option(ChannelOption.SO_KEEPALIVE, true)
             .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 10 * 1024 * 1024, 1000 * 1024 * 1024))
             .handler(new ChannelInitializer<Channel>() {
                 @Override
                 protected void initChannel(Channel ch) throws Exception {
                     ch.pipeline().addLast(new ObjectEncoder());
                     ch.pipeline()
                       .addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null)));
                     // 设置服务器端的编码和解码
                     ch.pipeline().addLast(new NettyMessageDecoder());
                     ch.pipeline().addLast(new NettyMessageEncoder());

                     ch.pipeline().addLast(handler);
                 }
             });
            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
