package com.dongkuksystems.redissonboottest;

import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisTest {
	@Autowired
	private RedissonClient redisson;

	@Test
	public void testRedisSetGet() throws JSONException {
		String key = "testkey";
		String value = "testvalue";

		RBucket<String> rBucket = redisson.getBucket(key);
		rBucket.set(value);

		String redisValue = rBucket.get();
		System.out.println("redisValue: " + redisValue);

		Thread t = new Thread() {
			public void run() {
				RBucket<String> rBucket1 = redisson.getBucket(key);
				System.out.println("thread1: " + rBucket1.isExists());
			};
		};

		t.start();
		try {
			t.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		rBucket.delete();

		Thread t2 = new Thread() {
			public void run() {
				RBucket<String> rBucket2 = redisson.getBucket(key);
				System.out.println("thread2: " + rBucket2.isExists());
			};
		};

		t2.start();
		try {
			t2.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	@Test
	public void testRedissonLock() throws JSONException {
		RLock lock = redisson.getLock("lock");
		lock.lock(1, TimeUnit.SECONDS);
		System.out.println("1 " + lock.isLocked());

		Thread t = new Thread() {
			public void run() {
				RLock lock1 = redisson.getLock("lock");
				System.out.println("2 " + lock1.isLocked());
			};
		};

		t.start();
		try {
			t.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Thread t2 = new Thread() {
			public void run() {
				RLock lock1 = redisson.getLock("lock");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("3 " + lock1.isLocked());
			};
		};

		t2.start();
		try {
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRedissonQueue() throws JSONException {
		RQueue<String> queue = redisson.getQueue("queue");
		queue.add("item1");
		queue.add("item2");
		queue.add("item3");
		queue.add("item4");

		queue.forEach((item) -> {
			System.out.println(item);
		});
		
		queue.poll();

		Thread t = new Thread() {
			public void run() {
				RQueue<String> queue1 = redisson.getQueue("queue");
				queue1.forEach((item) -> {
					System.out.println("thread1: " + item);
				});
			};
		};

		t.start();
		try {
			t.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}