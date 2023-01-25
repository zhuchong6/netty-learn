package com.zhu.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

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
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup(2);
        try {
            //创建ServerBootstrap
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //因为你正在使用的是 NIO 传输，所以你指定了NioEventLoopGroup来接受和处理新的连接
            serverBootstrap.group(boss, worker)
                    //指定所使用的NIO传输channel，这里就是父Channel
                    .channel(NioServerSocketChannel.class)
                    //使用指定的端口设置套接字地址
                    .localAddress(new InetSocketAddress(port))
                    //处理BossGroup中的事件,主要监听已经绑定到本地port端口的套接字
                    .handler(new ChannelInitializer<ServerChannel>() {
                        @Override
                        protected void initChannel(ServerChannel ch) throws Exception {
                            System.out.println("已经绑定到本地"+port+"端口，正在监听！");
                        }
                    })
                    //处理WorkerGroup中的事件，主要处理传入客户端连接的Channel
                    //添加一个echoServerHandler到子Channel的ChannelPipeline，
                    // 当一个新的连接被接受时，一个新的子Channel将会被创建，用于处理入站消息通知
                    // 而ChannelInitializer将会把一个你的EchoServerHandler的实例添加到该Channel的ChannelPipeline中
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            System.out.println("新连接进入。。。");
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
            boss.shutdownGracefully().sync();
            worker.shutdownGracefully().sync();
        }
    }
}