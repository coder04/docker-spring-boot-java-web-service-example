package com.levo.dockerexample.controller;

import java.io.IOException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ElasticCacheController {

    @Value("${memcache.host}")
    private String memCacheHost;

    @PutMapping(value="/set-memcache")
    public boolean setTextValue() throws IOException {
        boolean isSet = false;
        MemcachedClient memcachedClient = new MemcachedClient( new DefaultConnectionFactory(), AddrUtil.getAddresses(memCacheHost));
        String key = "1000002";
        String value = "1000002-Test value in Text";
        Integer expires = Integer.parseInt("1000");
        try {
            memcachedClient.set(key, expires, value);
            isSet=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        memcachedClient.shutdown();
        return isSet;
    }

    @GetMapping(value="/get-memcache")
    public ResponseEntity<String> getTextValue() throws Exception {
        MemcachedClient memcachedClient = new MemcachedClient( new DefaultConnectionFactory(),AddrUtil.getAddresses(memCacheHost));
        String key = "1000002";
        Object obj=null;
        try {
            obj = memcachedClient.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        memcachedClient.shutdown();
        return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
    }

}
