package com.code;

import com.util.ObjectConvertUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class NettyMessageDecoder extends MessageToMessageDecoder<byte[]> {
    @Override
    protected void decode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception {
        try {
            Object outobj = ObjectConvertUtil.convertModle(msg);
            out.add(outobj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
