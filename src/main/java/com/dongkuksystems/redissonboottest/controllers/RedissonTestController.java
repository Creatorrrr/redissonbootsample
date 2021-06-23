package com.dongkuksystems.redissonboottest.controllers;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dongkuksystems.redissonboottest.models.dtos.RedissonLockOption;

@RestController
public class RedissonTestController {
	@Autowired
	private RedissonClient redisson;

	@GetMapping("/locks/{key}")
	public ResponseEntity<Boolean> getIslocked(@PathVariable(required = true) String key) throws Exception {
		RLock lock = redisson.getLock(key);

		return ResponseEntity.ok().body(lock.isLocked());
	}

	@PostMapping("/locks/{key}/lock")
	public ResponseEntity<Boolean> lock(@PathVariable(required = true) String key) throws Exception {
		RLock lock = redisson.getLock(key);
		lock.lock();

		return ResponseEntity.ok().body(lock.isLocked());
	}

	@PostMapping("/locks/{key}/unlock")
	public ResponseEntity<Boolean> unlock(@PathVariable(required = true) String key,
			@RequestBody RedissonLockOption option) throws Exception {
		RLock lock = redisson.getLock(key);
		if (option.isForce())
			lock.forceUnlock();
		else
			lock.unlock();

		return ResponseEntity.ok().body(lock.isLocked());
	}
}
