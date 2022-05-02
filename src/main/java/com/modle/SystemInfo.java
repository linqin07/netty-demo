package com.modle;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class SystemInfo implements Serializable {
    private static final long serialVersionUID = 2701652423210357049L;

    private Map<String, Object> map;

    private int execType;

    private StringBuilder resultMsg = new StringBuilder(); //返回消息

    private int status;

    public void appendMsg(String msg) {
        this.resultMsg.append(msg+"\n");
    }

}
