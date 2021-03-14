package com.athome.direct.mq;

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
        //绑定交换机，交换机的名称和类型
        channel.exchangeDeclare("logs_direct", "direct");
        //声明routingkey info
        String routingkey = "info";
        channel.basicPublish("logs_direct", routingkey, null, "hello world direct".getBytes());

        RabbitUtil.close(channel, connection);
    }
}
