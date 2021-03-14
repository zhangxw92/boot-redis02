package com.athome.topic.mq;

import com.athome.util.RabbitUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer10 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("topics", "topic");

        String queue = channel.queueDeclare().getQueue();

        channel.queueBind(queue, "topics", "user.*");

        channel.basicConsume(queue, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消息10消费到的消息：" + new String(body));
            }
        });

    }
}
