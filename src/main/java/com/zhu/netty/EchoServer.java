package com.zhu.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author by zhuhcong
 * @descr
 * @date 2023/1/23 23:43
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        if( args.length != 1){
            //提示设置端口值
            System.err.println("Usage: "+ EchoServer.class.getSimpleName()+" <port>");
        }
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    public void start() throws InterruptedException {
        final EchoServerHandler echoServerHandler = new EchoServerHandler();
        //创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建ServerBootstrap
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //因为你正在使用的是 NIO 传输，所以你指定了NioEventLoopGroup来接受和处理新的连接
            serverBootstrap.group(group)
                    //指定所使用的NIO传输channel，这里就是父Channel
                    .channel(NioServerSocketChannel.class)
                    //使用指定的端口设置套接字地址
                    .localAddress(new InetSocketAddress(port))
                    //添加一个echoServerHandler到子Channel的ChannelPipeline，
                    // 当一个新的连接被接受时，一个新的子Channel将会被创建，用于处理入站消息通知
                    // 而ChannelInitializer将会把一个你的EchoServerHandler的实例添加到该Channel的ChannelPipeline中
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //echoServerHandler 中@Sharable表示我们可以使用同样的实例
                            ch.pipeline().addLast(echoServerHandler);
                        }
                    });

            //异步绑定服务器，调用sync方法阻塞等待直到绑定完成
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            //获取Channel的closeFuture，并且阻塞当前线程直到它完成
            channelFuture.channel().closeFuture().sync();
        }finally {
            //关闭EventLoopGroup释放所有资源
            group.shutdownGracefully().sync();
        }
    }
}