package com.athome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author zhangxw03
 * @Dat 2020-12-09 14:32
 * @Describe
 */
@RestController
public class GoodsController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${server.port}")
    private String serverPort;
    private static final String REDIS_LOCK = "redisLock";

    @GetMapping("buy")
    public String buy() {

        //如果只设置了setnx，程序走到第36行，部署应用的程序down机了，
        // 那么将无法释放rdis中存入的key，因此需要对这个key设置一个过期时间
        //高并发程序一定要考虑原子性
        //Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(REDIS_LOCK, UUID.randomUUID().toString());
        //保证原子性
        String value = UUID.randomUUID().toString();
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(REDIS_LOCK, value, 10, TimeUnit.SECONDS);
//        if (flag) {
//            stringRedisTemplate.expire(REDIS_LOCK, 10, TimeUnit.SECONDS);
//
//        }
        if (!flag) {
            return "抢锁失败";
        }

        try {
            //synchronized (this) {
            String s = stringRedisTemplate.opsForValue().get("goods:001");
            int result = s == null ? 0 : Integer.parseInt(s);
            if (result > 0) {
                int i = result - 1;
                stringRedisTemplate.opsForValue().set("goods:001", String.valueOf(i));
                System.out.println("商品剩余：+" + i + "  服务端口" + serverPort);
                return "商品剩余：+" + i + "  服务端口" + serverPort;
            } else {
                System.out.println("商品已经售罄" + serverPort);
                return "商品已经售罄";
            }
            // }
        } catch (NumberFormatException e) {
            System.out.println(e);
            return "程序异常";
        } finally {
            //直接删除key，带来的问题是可能会错删其他线程的key;解决方法增加判断，如果value相同再删
            if (stringRedisTemplate.opsForValue().get(REDIS_LOCK).equals(value)) {
                stringRedisTemplate.delete(REDIS_LOCK);
            }
        }

    }

}
