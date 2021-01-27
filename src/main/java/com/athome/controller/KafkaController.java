package com.athome.controller;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Controller;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/kafka")
public class KafkaController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private String topic = "test";
    private String hello = "HelloWorld";

    @ResponseBody
    @GetMapping("/test")
    public void test() {

//        String s = JSON.toJSONString(hello);
//        ListenableFuture send = kafkaTemplate.send(topic, s);
//        ListenableFuture<SendResult<String, String>> send2 = kafkaTemplate.send(topic, UUID.randomUUID().toString(), "hellword2");
        ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(topic, 1, "1", "123");

        try {
            SendResult<String, String> stringStringSendResult = send.get();
            System.out.println(stringStringSendResult.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
