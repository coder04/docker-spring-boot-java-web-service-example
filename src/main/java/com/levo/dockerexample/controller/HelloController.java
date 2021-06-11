package com.levo.dockerexample.controller;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("docker-java-app")
public class HelloController {

	private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

	@Value("${memcache.host}")
	private String memCacheHost;

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ResponseEntity<String> test() throws IOException {
		boolean isSet = false;
		logger.info("Connection for memcace started "+ memCacheHost);
		MemcachedClient memcachedClient = new MemcachedClient( new DefaultConnectionFactory(), AddrUtil.getAddresses(memCacheHost));
		String key = "1000002";
		String value = "1000002-Test value in Text";
		Object obj=null;
		Integer expires = Integer.parseInt("1000");
		try {
			memcachedClient.set(key, expires, value);
			obj = memcachedClient.get(key);
			logger.info(obj.toString());
			isSet = true;
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
		memcachedClient.shutdown();
		logger.info("Connection closed isSet "+isSet);
		return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
	}

}
