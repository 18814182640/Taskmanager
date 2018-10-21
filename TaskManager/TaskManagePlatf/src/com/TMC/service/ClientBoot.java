package com.TMC.service;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import javax.xml.stream.events.StartDocument;

import org.apache.commons.codec.binary.Base64;

import com.TMC.frame.Dialog.CommonDialog;
import com.TMS.model.Message;
import com.config.Config;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.base64.Base64Decoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import net.sf.json.JSONObject;

public class ClientBoot {

	private static ClientBoot serverBoot = new ClientBoot();
	private static Base64 base64 = new Base64();
	private long sqe = 0L;
	private Channel channel;
	private ChannelFuture future;
	private EventLoopGroup group;
	private ClientBoot() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				start();
			}
		}).start();;
	}
	
	public static ClientBoot getInstance(){
		return serverBoot;
	}

	public void start(){
		group = new NioEventLoopGroup();  //负责监听端口的线程  数据读写的线程
		try {
			Bootstrap bootstrap = new Bootstrap();//启动辅助类
			bootstrap.group(group);   //设置线程池
			bootstrap.channel(NioSocketChannel.class);  //使用NioSocketChannel创建一个channel,表示一个连接      socket工厂
			bootstrap.remoteAddress(Config.HOST, Config.PORT);
			bootstrap.option(ChannelOption.TCP_NODELAY, true); //关闭延时发送   不拼包
			bootstrap.handler(new ChannelInitializer<Channel>() {     // 处理类    channel工厂
				@Override
				protected void initChannel(Channel channel) throws Exception {
					ChannelPipeline pipeline = channel.pipeline();//获取管道
					ClientBoot.this.channel = channel;
					pipeline.addLast(new LineBasedFrameDecoder(1024*1024,true,true)); //通道字符串解码  最大长度1M
					pipeline.addLast(new Base64Decoder());     //base64解码
					pipeline.addLast(new StringDecoder(Charset.forName("UTF-8")));     //字符
					pipeline.addLast(new MessageHandle());  //自定义处理器
//					pipeline.addLast("ping", new IdleStateHandler(60, 20,60*10,TimeUnit.SECONDS));
				} 
			});
			future = bootstrap.connect().sync();
			future.channel().closeFuture().sync(); //等待监听端口关闭
		} catch (Exception e) {
			e.printStackTrace();
			group.shutdownGracefully();
		}
	}
	
	
	 public void send(Message message) throws Exception{
			try {
				if(channel!=null && channel.isActive()){
					if(message!=null){
						message.setTime(System.currentTimeMillis());
						message.setSeq(getSeq());
						JSONObject jsonObject = JSONObject.fromObject(message);
//						System.err.println("send:"+jsonObject.toString());
						byte [] data = base64.encode(jsonObject.toString().getBytes("UTF-8"));
						byte [] end = "\r\n".getBytes();
						ByteBuf byteBuf = Unpooled.directBuffer(data.length+end.length);
						byteBuf.writeBytes(data);
						byteBuf.writeBytes(end);
						channel.writeAndFlush(byteBuf);
					}
				}else{
					throw new RuntimeException("通信异常!");
				}
			} catch (Exception e) {
//				e.printStackTrace();
				close();
				new Thread(new  Runnable() {
					public void run() {
						start();
					}
				}).start();
				throw e;
			}
       }
	
	public boolean isOpen(){
		return future==null?false:future.isSuccess();
	}
	public void close(){
		if(group!=null){
			group.shutdownGracefully();
		}
	}
	
	synchronized private long getSeq(){
		return sqe++;
	}
	
	
	
	
	
	
}
