package org.cloudxue.imClient.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.im.common.bean.User;
import org.cloudxue.im.common.codec.SimpleProtobufDecoder;
import org.cloudxue.im.common.codec.SimpleProtobufEncoder;
import org.cloudxue.imClient.config.SystemConfig;
import org.cloudxue.imClient.handler.ExceptionHandler;
import org.cloudxue.imClient.handler.LoginResponseHandler;
import org.cloudxue.imClient.sender.LoginSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @ClassName ChatNettyClient
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2021/12/24 下午3:54
 * @Version 1.0
 **/
@Slf4j
@Data
@Service("chatNettyClient")
public class ChatNettyClient {
    @Value("${chat.server.ip}")
    private String host;
    @Value("${chat.server.port}")
    private int port;

    @Autowired
    private SystemConfig systemConfig;
    @Autowired
    private LoginResponseHandler loginResponseHandler;
    @Autowired
    private ExceptionHandler exceptionHandler;

    private Channel channel;
    private LoginSender loginSender;

    private boolean initFlag = true;
    private User user;
    private GenericFutureListener<ChannelFuture> connectedListener;

    private Bootstrap bootstrap;
    private EventLoopGroup g;

    public ChatNettyClient () {
        g = new NioEventLoopGroup();
    }

    public void doConnect() {
        try {
            bootstrap = new Bootstrap();

            bootstrap.group(g);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.remoteAddress(host, port);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast("decoder", new SimpleProtobufDecoder());
                    ch.pipeline().addLast("encoder", new SimpleProtobufEncoder());
                    ch.pipeline().addLast(loginResponseHandler);
                    ch.pipeline().addLast(exceptionHandler);
                }
            });

            log.info("客户端开始连接： SimpleIM");
            ChannelFuture f = bootstrap.connect();
            f.addListener(connectedListener);
        } catch (Exception e) {
            log.info("客户端连接失败！" + e.getMessage());
        }
    }

    public void close() {
        g.shutdownGracefully();
    }
}
