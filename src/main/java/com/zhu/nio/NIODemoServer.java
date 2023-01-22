package com.zhu.nio;

import java.io.IOException;
/**
 * @author by zhuhcong
 * @descr
 * @date 2023/1/22 23:01
 */
public class NIODemoServer {
    public static void main(String[] args) throws IOException {
        int port = 8090;
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();

    }
}