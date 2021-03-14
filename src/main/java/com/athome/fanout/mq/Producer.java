package com.athome.fanout.mq;

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

        //通道声明指定交换机，参数1交换机名称，参数2交换机类型，fanout 广播
        channel.exchangeDeclare("logs", "fanout");
        //参数1交换机名称  参数2 路由key
        channel.basicPublish("logs", "", null, "交换机".getBytes());

        RabbitUtil.close(channel, connection);
    }
}
