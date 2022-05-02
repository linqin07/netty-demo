package com.util;

import com.code.ByteObjConverter;
import com.code.Event;
import com.code.RecvieMessage;
import com.modle.Result;
import com.modle.SystemInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectConvertUtil {
    public static byte[] convertModle(Result object) {
        RecvieMessage recevie = new RecvieMessage();
        recevie.setDatas(ByteObjConverter.objectToByte(object));
        recevie.setMsgType(Event.MESSAGE_SYSTEM_INFO);
        return ByteObjConverter.objectToByte(recevie);
    }

    public static byte[] convertModle(SystemInfo object) {
        RecvieMessage recevie = new RecvieMessage();
        recevie.setDatas(ByteObjConverter.objectToByte(object));
        recevie.setMsgType(Event.MESSAGE_SYSTEM_INFO);
        return ByteObjConverter.objectToByte(recevie);
    }

    public static byte[] convertModle(Object object) {
        RecvieMessage recevie = new RecvieMessage();
        recevie.setDatas(ByteObjConverter.objectToByte(object));
        recevie.setMsgType(Event.MESSAGE_DEFAULT);
        return ByteObjConverter.objectToByte(recevie);
    }

    /**
     * decodeMessage
     * @param recviejson
     * @return
     */
    public static Object convertModle(byte[] recviejson) {
        RecvieMessage recvie = (RecvieMessage) ByteObjConverter.byteToObject(recviejson);
        Object obj = null;
        log.info("解码 recvie.getMsgType(): {}", recvie.getMsgType());

        switch (recvie.getMsgType()) {
            case Event.MESSAGE_TYPE_RESULT:
                obj = (Result) ByteObjConverter.byteToObject(recvie.getDatas());
                break;
            case Event.MESSAGE_SYSTEM_INFO:
                obj = (SystemInfo) ByteObjConverter.byteToObject(recvie.getDatas());
                break;
            default:
                log.error("decode fail !!!");
                break;
        }
        return obj;
    }

    /**
     * encodeMessage
     * @param obj
     * @return
     */
    public static byte[] request(Object obj) {
        log.info("");
        if (obj instanceof Result) {
            Result result = (Result) obj;
            return convertModle(result);
        } else if (obj instanceof SystemInfo) {
            SystemInfo result = (SystemInfo) obj;
            return convertModle(result);
        } else {
            return convertModle(obj);
        }
    }
}
