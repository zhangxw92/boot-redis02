package com.athome.controller;

import com.athome.config.RedisUtil;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.Collections;
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
    @Autowired
    private Redisson redisson;

    @GetMapping("buy")
    public String buy() {

        //如果只设置了setnx，程序走到第36行，部署应用的程序down机了，
        // 那么将无法释放rdis中存入的key，因此需要对这个key设置一个过期时间
        //高并发程序一定要考虑原子性
        //Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(REDIS_LOCK, UUID.randomUUID().toString());
        //保证原子性
//        String value = UUID.randomUUID().toString();
//        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(REDIS_LOCK, value, 10, TimeUnit.SECONDS);
////        if (flag) {
////            stringRedisTemplate.expire(REDIS_LOCK, 10, TimeUnit.SECONDS);
////
////        }
//        if (!flag) {
//            return "抢锁失败";
//        }

        RLock lock = redisson.getLock(REDIS_LOCK);
        lock.lock();
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
//            //直接删除key，带来的问题是可能会错删其他线程的key;解决方法增加判断，如果value相同再删
////            if (stringRedisTemplate.opsForValue().get(REDIS_LOCK).equals(value)) {
////                stringRedisTemplate.delete(REDIS_LOCK);
////            }
//            //上面这种方式不能保证原子性，使用Jedis调用lua脚本保证原子性删除
//            Jedis jedis = RedisUtil.getJedis();
//            String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
//                    "    return redis.call(\"del\",KEYS[1])\n" +
//                    "else\n" +
//                    "    return 0\n" +
//                    "end\n";
//
//            try {
//                Object eval = jedis.eval(script, Collections.singletonList(REDIS_LOCK), Collections.singletonList(value));
//                if ("1".equals(eval.toString())) {
//                    System.out.println("释放key成功");
//                } else {
//                    System.out.println("释放key失败");
//                }
//            } finally {
//                jedis.close();
//            }
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        //以上这种方式实现分布式锁还存在的两个问题：
        //1、如何保证设置的redis key的过期时间大于业务的处理时间，需要一个缓存续命机制，待思考！
        //2、在集群环境下，因为redis保证的是高可用和分区容错性，并不能保证数据强一致性，因此会出现数据丢失的情况，
        //因此可以采用redission来解决分布式锁
    }

}
