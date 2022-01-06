package org.cloudxue.imServer.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.codec.SimpleProtobufDecoder;
import org.cloudxue.im.common.codec.SimpleProtobufEncoder;
import org.cloudxue.imServer.handler.LoginRequestHandler;
import org.cloudxue.imServer.handler.ServerExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

/**
 * @ClassName ChatServer
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/22 上午10:50
 * @Version 1.0
 **/
@Data
@Slf4j
@Service("ChatServer")
public class ChatServer {
    @Value("${server.port}")
    private int port;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @Autowired
    private LoginRequestHandler loginRequestHandler;

    @Autowired
    private ServerExceptionHandler serverExceptionHandler;

    private ServerBootstrap bootstrap = new ServerBootstrap();

    public void run() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        try {
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.localAddress(new InetSocketAddress(port));
//            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new SimpleProtobufDecoder());
                    ch.pipeline().addLast(new SimpleProtobufEncoder());
                    //添加登录处理器,此处理器登录后删除
                    ch.pipeline().addLast("login",  loginRequestHandler);
                    //添加异常处理器
                    ch.pipeline().addLast(serverExceptionHandler);
                }
            });
            //绑定Server
            ChannelFuture channelFuture = bootstrap.bind().sync();
            log.info("SimpleIM server启动，端口： " + channelFuture.channel().localAddress());

            //监听通道关闭事件
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
