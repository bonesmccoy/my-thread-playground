package com.company;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Publisher {

    private Connection connection;
    private ConnectionFactory connectionFactory;
    private final String hostname = "localhost";
    private final Integer port = 5672;
    private String queueName = "MY_QUEUE";
    private String routingName = "my-routing";
    private String exchangeName = "my-exchange";


    public Publisher() {
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
            channel.exchangeDeclare(exchangeName,"direct", true);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, routingName);

            channel.basicPublish(exchangeName, routingName, null, message.getBytes());

            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
