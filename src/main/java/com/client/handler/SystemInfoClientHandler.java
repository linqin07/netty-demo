package com.client.handler;

import com.client.DeamonClientHandlerAbs;
import com.modle.SystemInfo;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class SystemInfoClientHandler extends DeamonClientHandlerAbs {
    private static final Logger log = LoggerFactory.getLogger(SystemInfoClientHandler.class);

    private SystemInfo systemInfo;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("first channelActive");
        ctx.writeAndFlush(systemInfo);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("second channelRead");
        if (msg instanceof SystemInfo) {
            systemInfo = (SystemInfo) msg;
        }
        // super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("third channelReadComplete");
        super.channelReadComplete(ctx);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        this.result.setResultMsg(cause.getMessage());
        this.result.setStatus(0);
        log.error("exception", cause);
        ctx.close();
    }
}
