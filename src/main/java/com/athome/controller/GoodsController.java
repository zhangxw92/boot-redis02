package com.athome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("buy")
    public String buy() {
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

    }

}
