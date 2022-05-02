package com.code;

import com.util.ObjectConvertUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class NettyMessageEncoder extends MessageToMessageEncoder<Object>  {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        out.add(ObjectConvertUtil.request(msg));
    }
}
