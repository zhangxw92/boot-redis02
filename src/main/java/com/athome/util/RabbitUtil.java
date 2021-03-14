package com.athome.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitUtil {

    private static ConnectionFactory connectionFactory;

    static{
        //创建mq的连接工厂
        connectionFactory = new ConnectionFactory();
        //设置主机
        connectionFactory.setHost("39.102.61.252");
        //设置端口
        connectionFactory.setPort(5672);
        //设置虚拟主机
        connectionFactory.setVirtualHost("/athome");
        //设置用户名和密码
        connectionFactory.setUsername("test");
        connectionFactory.setPassword("123456");
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static void close(Channel channel,Connection connection){
        try {
            if(channel!=null) channel.close();
            if(connection !=null) connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
