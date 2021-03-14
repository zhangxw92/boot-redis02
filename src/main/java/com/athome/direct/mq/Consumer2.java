package com.athome.direct.mq;

import com.athome.util.RabbitUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer2 {

    public static void main(String[] args) throws IOException {
        Connection connection = RabbitUtil.getConnection();
        Channel channel = connection.createChannel();
        //对构建出来的通道声明一个交换机，类型为direct
        channel.exchangeDeclare("logs_direct", "direct");
        //创建临时队列
        String queue = channel.queueDeclare().getQueue();
        //队列和交换机绑定，消费路由类型为error的消息,可以绑定多个路由
        channel.queueBind(queue, "logs_direct", "info");
        channel.queueBind(queue, "logs_direct", "warn");
        channel.queueBind(queue, "logs_direct", "error");


        channel.basicConsume(queue, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费2-info到的消息为：" + new String(body));
            }
        });
    }
}
