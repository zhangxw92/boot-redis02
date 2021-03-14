package com.athome.mq;

import com.athome.util.RabbitUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerWork2 {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtil.getConnection();

        Channel channel = connection.createChannel();
        channel.basicQos(1);

        channel.queueDeclare("work", true, false, false, null);
        channel.basicConsume("work", false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("..............消息体：" + new String(body));
                //手动确认，参数1、手动确认标识；参数2、每次确认一个
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
