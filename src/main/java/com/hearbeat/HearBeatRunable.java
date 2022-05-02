package com.hearbeat;

import com.util.JsonUtil;
import com.util.ZkUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class HearBeatRunable implements Runnable {
    private String localIp;
    private String pid;
    private int port;
    private String hostName;
    /**
     * 系统类型
     */
    private String osType;
    private int state = 1;
    private static volatile boolean isCreate = false;
    private String zkNode;
    private ZkUtil zkUtil;

    public HearBeatRunable(int port, String connectStr, int baseSleepTimeMs, int maxRetries) {
        this.pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        this.port = port;
        this.hostName = "none";
        try {
            this.hostName = InetAddress.getLocalHost().getHostName();
            // 可能存在多网卡问题。可能取值会不对
            this.localIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.osType = System.getProperty("os.name");
        zkNode = "/daemon/running/" + localIp;
        zkUtil = new ZkUtil(connectStr, baseSleepTimeMs, maxRetries);
    }

    @Override
    public void run() {
        try {
            state = 1;
            log.debug("reportHeartBeat");
            if (!isCreate) {
                zkUtil.createZkNode(zkNode);
                isCreate = true;
                zkUtil.updateData(zkNode, makeMessage().getBytes());
            } else {
                zkUtil.updateData(zkNode, makeMessage().getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String makeMessage() {
        Map<String, Object> info = new HashMap<String, Object>();
        info.put("localIp", localIp);
        info.put("pid", pid);
        info.put("port", port);
        info.put("hostName", hostName);
        info.put("osType", osType);
        return JsonUtil.SingletonHolder.toJsonString(info);
    }
}
