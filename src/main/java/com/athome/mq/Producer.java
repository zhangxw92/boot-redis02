package com.athome.mq;

import com.athome.util.RabbitUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

    @Test
    public void test() throws IOException, TimeoutException {
        Connection connection = RabbitUtil.getConnection();
        //获取连接中的channel
        Channel channel = connection.createChannel();
        //通道绑定对应消息队列
        //1、队列名称，如果不存在自动创建
        //2、durable 用来定义队列特性是否要持久化 ，true 是 false 否  当rabbitmq重启后，队列全部丢失
        //3、exclusive 是否独占队列 true 是 false否
        //4、autodelete 是否在消费消息后删除队列 true 是 false 否
        //5、额外参数
        channel.queueDeclare("bb", true, false, true, null);

        //发布消息
        //1、交换机名称
        //2、队列名称 routingkey 决定了向哪个队列发送消息
        //3、消息的额外设置 MessageProperties.PERSISTENT_BASIC用来设置消息持久化
        //4、消息的具体内容跟
        channel.basicPublish("", "bb", MessageProperties.PERSISTENT_BASIC, "Hello word".getBytes());

        RabbitUtil.close(channel, connection);
    }
}
