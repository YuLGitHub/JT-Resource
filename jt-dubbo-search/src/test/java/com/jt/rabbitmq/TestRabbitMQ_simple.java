package com.jt.rabbitmq;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * 测试rabbitMQ中的简单模式
 * @author Tarena-java
 *
 */
public class TestRabbitMQ_simple {
	
	private Connection connection = null; // 定义连接
	
	/**
	 * 从rabbitMQ工厂中获取连接
	 * @throws IOException 
	 */
	@Before
	public void initConnection() throws IOException {
		// 创建工厂对象
		ConnectionFactory factory = new ConnectionFactory();
		// 设置参数
		factory.setHost("192.168.240.135");
		// 设定端口
		factory.setPort(5672);
		// 设定用户名
		factory.setUsername("jtadmin");
		factory.setPassword("jtadmin");
		// 设置虚拟主机
		factory.setVirtualHost("/jt");
		// 获取连接
		connection = factory.newConnection();
		
	}
	
	@Test
	public void provider() throws IOException {
		// 获取channel对象,控制队列和路由交换机
		Channel channel = connection.createChannel();
		// 定义队列
		/**
		 * queue:队列名称
		 * durable：是否持久化
		 * exclusive：独有的，是否是生产者独占
		 * autoDelete：是否自动删除，消息队列中没有消息，则自动删除队列
		 * arguments:提交参数一般为空
		 * 
		 */
		channel.queueDeclare("queue_simple", false, false, false, null);
		// 定义需要发送的消息
		String msg = "我是一个要发送的简单消息";
		// 将消息与队列绑定并发送
		/**
		 * exchange:交换机名称，没有交换机写null
		 * routingKey：路由key，消息发往队列的id，没有路由key，则写队列名称
		 * props：消息发送的额外参数，如果没有写null
		 * body：发送消息的二进制的字节码文件
		 */
		channel.basicPublish("", "queue_simple", null, msg.getBytes());
		// 关闭流
		channel.close();
		connection.close();
		System.out.println("恭喜队列使用简单一步");
	}
	// 定义消费者
	/**
	 * 1.获取Channel对象
	 * 2.定义消息队列
	 * 3.定义消费者对象
	 * 4.将消费者与队列消息绑定
	 * 5.通过循环的方式获取队列中的内容
	 * 6.将获取的数据转化为字符串
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws ConsumerCancelledException 
	 * @throws ShutdownSignalException 
	 */
	@Test
	public void consumer() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		Channel channel = connection.createChannel();
		channel.queueDeclare("queue_simple", false, false, false, null);
		
		QueueingConsumer consumer = new QueueingConsumer(channel);
		/**
		 * queue:队列名称
		 * autoAck:是否自动回复，队列确认后方能执行下次消息
		 * callback:回调参数，写的消费者对象
		 */
		channel.basicConsume("queue_simple", true, consumer);
		
		while(true) {
			// 获取队列中的内容
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			
			// 字符转换
			String msg = new String(delivery.getBody());
			
			System.out.println("消费者消费队列消息" + msg);
		}
	}
	
	
	
}
