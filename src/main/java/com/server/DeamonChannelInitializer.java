package com.server;

import com.code.NettyMessageDecoder;
import com.code.NettyMessageEncoder;
import com.server.handler.SystemInfoServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class DeamonChannelInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new ObjectEncoder());
        ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null))); // 最大长度
        // 指定编码器解码器
        ch.pipeline().addLast(new NettyMessageEncoder());
        ch.pipeline().addLast(new NettyMessageDecoder());

        ch.pipeline().addLast(new SystemInfoServerHandler());

    }
}
