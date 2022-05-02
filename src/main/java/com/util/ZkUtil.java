package com.util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class ZkUtil {
    private volatile CuratorFramework client;
    private static final String authorization = "lin:lin";
    private static final List<ACL> acls = new ArrayList<>();

    static {
        try {
            Id id = new Id("digest", DigestAuthenticationProvider.generateDigest(authorization));
            ACL acl = new ACL(ZooDefs.Perms.ALL, id);
            acls.add(acl);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public ZkUtil(String connectStr, int baseSleepTimeMs, int maxRetries) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        client = CuratorFrameworkFactory.builder()
                                        .connectString(connectStr)
                                        .authorization("digest", authorization.getBytes())
                                        .retryPolicy(retryPolicy)
                                        .sessionTimeoutMs(3000)
                                        .connectionTimeoutMs(2000)
                                        .build();
        client.start();

    }

    public void createZkNode(String nodePath) throws Exception {
        createZkNode(nodePath, null);
    }

    public void createZkNode(String nodePath, byte[] nodeData) throws Exception {
        if (null != nodeData) {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).withACL(acls)
                  .forPath(nodePath, nodeData);
        } else {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).withACL(acls).forPath(nodePath);
        }
    }

    public void createPermZkNode(String nodePath, byte[] nodeData) throws Exception {
        if (null != nodeData) {
            client.create().creatingParentsIfNeeded().withACL(acls).forPath(nodePath, nodeData);
        } else {
            client.create().creatingParentsIfNeeded().withACL(acls).forPath(nodePath);
        }
    }

    public void updateData(String nodePath, byte[] nodeData) throws Exception {
        client.setData().forPath(nodePath, nodeData);
    }

    public byte[] getData(String nodePath) throws Exception {
        return client.getData().storingStatIn(new Stat()).forPath(nodePath);
    }

    public List<String> getNodeChildren(String nodePath) throws Exception {
        return client.getChildren().forPath(nodePath);
    }

    public void deleteNode(String nodePath) throws Exception {
        client.delete().guaranteed().forPath(nodePath);
    }

    public void deleteNodeWithChildren(String nodePath) throws Exception {
        client.delete().guaranteed().deletingChildrenIfNeeded().forPath(nodePath);
    }
    public void close(){
        client.close();
    }

    public CuratorFramework getClient(){
        return client;
    }

    public Stat checkExists(String zkNode) throws Exception{
        return client.checkExists().forPath(zkNode);

    }
}
