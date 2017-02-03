package com.company;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Publisher {

    private Connection connection;
    private ConnectionFactory connectionFactory;
    private String queueName;

    public Publisher(String hostname, Integer port, String queueName) {
        this.queueName = queueName;

        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(hostname);
        connectionFactory.setPort(port);

    }

    public void closeConnection()
    {
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean openConnection()
    {
        try {
            connection = connectionFactory.newConnection();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (TimeoutException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void publish(String message) {

        try {
            Channel channel = connection.createChannel();
            channel.queueDeclare(this.queueName, false, false, false, null);
            channel.basicPublish("", queueName, null, message.getBytes());
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
