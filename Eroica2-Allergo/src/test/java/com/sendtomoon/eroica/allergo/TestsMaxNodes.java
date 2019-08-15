package com.sendtomoon.eroica.allergo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.io.FileUtils;
import org.apache.zookeeper.CreateMode;

import com.alibaba.fastjson.JSONObject;
import com.sendtomoon.eroica.common.utils.URLUtils;
import com.sendtomoon.eroica2.allergo.impl.ZookeeperAllergoManager;

public class TestsMaxNodes {

	public static void main(String args[]) throws Exception {
		testlist();

		System.out.println("over....");

	}

	public static void testlist() {
		ZookeeperAllergoManager m = new ZookeeperAllergoManager();
		m.init(URLUtils.valueOf("zookeeper://192.168.0.9:20001"));
		final ZkClient zc = m.getClient();

		try {
			List<String> datas = zc.getChildren("/testMaxNodes");
			System.err.println(datas.get(60000));
			System.err.println(datas.size());
			byte bytes[] = JSONObject.toJSONString(datas).getBytes();
			System.err.println("bytes=" + bytes.length);
			FileUtils.writeByteArrayToFile(new File("d:/temp/temp.txt"), bytes);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void testCreate() throws Exception {
		ZookeeperAllergoManager m = new ZookeeperAllergoManager();
		m.init(URLUtils.valueOf("zookeeper://192.168.0.9:20001"));
		final ZkClient zc = m.getClient();
		try {
			zc.create("/testMaxNodes", null, CreateMode.PERSISTENT);
		} catch (Exception ex) {

		}
		final int max = 70000;
		final Count count = new Count();
		// -------------------------------------------

		List<Thread> ts = new ArrayList<Thread>();
		for (int i = 0; i < 10; i++) {
			ts.add(new Thread(new Runnable() {

				@Override
				public void run() {
					while ((++count.c) < max) {
						zc.create("/testMaxNodes/com.sendtomoon.eroica.nangua.abc.nangua.testand" + count.c, null,
								CreateMode.PERSISTENT);
						if (count.c % 100 == 0) {
							System.out.println("count=" + count.c);
						}
					}
				}
			}));

		}
		for (int i = 0; i < 10; i++) {
			ts.get(i).start();
		}
		System.in.read();
	}

}

class Count {
	int c = 0;

}