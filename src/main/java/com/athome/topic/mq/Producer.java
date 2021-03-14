package com.athome.topic.mq;

import com.athome.util.RabbitUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Producer {

    @Test
    public void test() throws IOException {
        Connection connection = RabbitUtil.getConnection();
        Channel channel = connection.createChannel();
        //绑定交换机和交换机类型
        channel.exchangeDeclare("topics", "topic");
        //声明路由
        String routingkey = "user.save.aa";
        //消息发送
        channel.basicPublish("topics", routingkey, null, "hello world".getBytes());

        RabbitUtil.close(channel, connection);
    }
}
