package com.modle;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    private static final long serialVersionUID = 2439833918765296876L;
    private int execType;

    private String resultMsg; //返回消息

    private int status;  //0失败，1成功
}
