package com.example.demo;

import com.example.demo.pojo.SomeObject;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.CompositeCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;

@SpringBootApplication
public class RedissonTestApplication implements CommandLineRunner {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedissonClient redisson;

    public static void main(String[] args) {
        SpringApplication.run(RedissonTestApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        redisTemplate.opsForValue().set("a", new SomeObject("yang", 1, new byte[] {1, 2, 3}));
        System.out.println(redisTemplate.opsForValue().get("a"));

        RBucket<Object> rBucket = redisson.getBucket("a");
        System.out.println(rBucket.get());

        redisTemplate.opsForHash().put("s", "b", new SomeObject("wen", 2, null));
        System.out.println(redisTemplate.opsForHash().get("s", "b"));

        RMap<String, SomeObject> map = redisson.getMap("s", new CompositeCodec(new StringCodec(), new JsonJacksonCodec()));
        SomeObject b = map.get("b");
        System.out.println(b);
        System.out.println(Arrays.toString(b.getBytes()));
    }


}
