package com.sendtomoon.eroica.allergo.impl;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

import com.sendtomoon.eroica.common.utils.URLUtils;
import com.sendtomoon.eroica2.allergo.impl.ZookeeperAllergoManager;

public class ZookeeperAllergoManagerTests2 {

	public static void main(String args[]) {
		ZookeeperAllergoManager m = new ZookeeperAllergoManager();
		m.init(URLUtils.valueOf("zookeeper://127.0.0.1:2181?rootPath=/test/abcdd/abceedd/aaa"));
		ZkClient zk = m.getClient();
		zk.create("/n/a", null, CreateMode.PERSISTENT);
		zk.create("/n/a/b/c", null, CreateMode.PERSISTENT);

	}
}
