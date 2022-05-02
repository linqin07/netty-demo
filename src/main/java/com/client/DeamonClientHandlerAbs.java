package com.client;

import com.modle.Result;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Data;

@Data
public class DeamonClientHandlerAbs extends ChannelInboundHandlerAdapter {
    public Result result = new Result();
}
