package com.server.handler;

import com.modle.SystemInfo;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SystemInfoServerHandler extends ChannelInboundHandlerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(SystemInfoServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // super.channelRead(ctx, msg);
        if (msg instanceof SystemInfo) {
            SystemInfo systemInfo = (SystemInfo) msg;
            try {
                Properties props = System.getProperties();
                InetAddress addr = InetAddress.getLocalHost();
                String ip = addr.getHostAddress();
                Map<String, Object> infos = new HashMap<String, Object>();
                infos.put("ip", ip);
                infos.put("hostname", addr.getHostName());
                infos.put("osName", props.getProperty("os.name"));
                infos.put("osVersion", props.getProperty("os.version"));
                infos.put("javaHome", props.getProperty("java.home"));
                infos.put("javaVersion", props.getProperty("java.runtime.version"));
                ((SystemInfo) msg).setMap(infos);
            } catch (Exception e) {
                // TODO: handle exception
                logger.error("异常", e);
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                systemInfo.appendMsg(sw.toString());
            }
            ctx.writeAndFlush(systemInfo);
        } else {
            ctx.fireChannelRead(msg);   //继续执行
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        // cause.printStackTrace();
        // ctx.close();
    }
}
