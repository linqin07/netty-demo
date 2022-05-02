package com.code;

import lombok.Data;

import java.io.Serializable;
@Data
public class RecvieMessage implements Serializable {
    private static final long serialVersionUID = -6574200835837458609L;

    private short msgType;

    private String data;

    private byte[] datas;
}
