package com.TMS.service;

import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.TMS.database.DataBaseUnit;
import com.config.Config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.base64.Base64Decoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerBoot {

	private Log log = LogFactory.getLog(ServerBoot.class);
	private static ServerBoot serverBoot = new ServerBoot();
	private ChannelFuture future;
	private EventLoopGroup boss,woker;
	private ServerBoot() {
	}
	
	public static ServerBoot getInstance(){
		return serverBoot;
	}

	public void start(){
		log.info("开启Sever");
		boss = new NioEventLoopGroup();  //负责监听端口的线程
		woker = new NioEventLoopGroup(); //负责数据读写的线程
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();//启动辅助类
			bootstrap.group(boss, woker);   //设置线程池
			bootstrap.channel(NioServerSocketChannel.class);  //使用NioServerSocketChannel创建一个channel,表示一个连接      socket工厂
			bootstrap.childHandler(new ChannelInitializer<Channel>() {     // 处理类    channel工厂
				@Override
				protected void initChannel(Channel channel) throws Exception {
					ChannelPipeline pipeline = channel.pipeline();//获取管道	
					pipeline.addLast(new LineBasedFrameDecoder(1024*1024,true,true)); //通道字符串解码  最大长度1M
					pipeline.addLast(new Base64Decoder());     //base64解码
					pipeline.addLast(new StringDecoder(Charset.forName("UTF-8")));     //字符
					pipeline.addLast(new RequestHandler(log));  //自定义处理器
					
				}
			});
			bootstrap.option(ChannelOption.SO_BACKLOG, 1024);  //设置链接缓冲池大小
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); //设置链接活性，清除死链接 
			bootstrap.childOption(ChannelOption.TCP_NODELAY, true); //关闭延时发送   不拼包
			
			DataBaseUnit.init(); //空方法，只是触发类加载而已，用饿汉式单例就不需要了
//			DataBaseUnit.openDataBase();
//			log.info("连接数据库");
			future = bootstrap.bind(Config.HOST,Config.PORT).sync();
			log.info("开始监听");
			future.channel().closeFuture().sync(); //等待服务端监听端口关闭
			log.info("关闭监听");
			DataBaseUnit.closeDataBase();
			log.info("关闭数据库");
		} catch (Exception e) {
			e.printStackTrace();
			boss.shutdownGracefully();
			woker.shutdownGracefully();
		}
	}
	
	public void close(){
		if(future!=null){
			boss.shutdownGracefully();
			woker.shutdownGracefully();
			log.info("关闭通道");
			RequestHandler.AllLogginClient.clear();
		}
	}
	
	
}
